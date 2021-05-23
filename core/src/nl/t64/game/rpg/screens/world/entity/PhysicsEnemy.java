package nl.t64.game.rpg.screens.world.entity;

import com.badlogic.gdx.ai.pfa.DefaultGraphPath;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import nl.t64.game.rpg.Utils;
import nl.t64.game.rpg.constants.Constant;
import nl.t64.game.rpg.screens.world.entity.events.*;
import nl.t64.game.rpg.screens.world.pathfinding.PathfindingObstacleChecker;
import nl.t64.game.rpg.screens.world.pathfinding.TiledNode;


public class PhysicsEnemy extends PhysicsComponent {

    private static final float WANDER_BOX_SIZE = 240f;
    private static final float WANDER_BOX_POSITION = -96f;

    private Entity enemyEntity;
    private Rectangle wanderBox;
    private DefaultGraphPath<TiledNode> path;
    private boolean isDetectingPlayer;

    public PhysicsEnemy() {
        this.velocity = Constant.MOVE_SPEED_1;
        this.boundingBoxWidthPercentage = 0.80f;
        this.boundingBoxHeightPercentage = 0.30f;
        this.isDetectingPlayer = false;
    }

    @Override
    public void receive(Event event) {
        if (event instanceof LoadEntityEvent loadEvent) {
            initNpc(loadEvent);
        }
        if (event instanceof StateEvent stateEvent) {
            state = stateEvent.state;
        }
        if (event instanceof DirectionEvent directionEvent) {
            direction = directionEvent.direction;
        }
        if (event instanceof PathUpdateEvent pathUpdateEvent) {
            path = pathUpdateEvent.path;
        }
        if (event instanceof OnDetectionEvent onDetectionEvent) {
            if (onDetectionEvent.detectionRange().contains(onDetectionEvent.ownPosition())) {
                isDetectingPlayer = true;
            }
        }
    }

    @Override
    public void dispose() {
        // empty
    }

    @Override
    public void update(Entity thisEnemyEntity, float dt) {
        this.enemyEntity = thisEnemyEntity;
        relocate(dt);
        if (isDetectingPlayer) {
            checkObstaclesWhileDetecting(dt);
        } else {
            checkObstaclesWhileWandering();
        }
        enemyEntity.send(new PositionEvent(currentPosition));
    }

    private void initNpc(LoadEntityEvent loadEvent) {
        state = loadEvent.state;
        currentPosition = loadEvent.position;
        direction = loadEvent.direction;
        setWanderBox();
        setBoundingBox();
    }

    private void relocate(float dt) {
        switch (state) {
            case WALKING, FLYING -> move(dt);
        }
    }

    private void checkObstaclesWhileDetecting(float dt) {
        isDetectingPlayer = false;
        setWanderBox();
        if (!Utils.getBrokerManager().blockObservers.getCurrentBlockersFor(boundingBox).isEmpty()) {
            Vector2 positionInGrid = enemyEntity.getPositionInGrid();
            direction = new PathfindingObstacleChecker(positionInGrid, direction).getNewDirection();
            currentPosition.set(oldPosition);
            move(dt);
        }
    }

    private void checkObstaclesWhileWandering() {
        switch (state) {
            case WALKING, FLYING -> {
                boolean moveBack1 = checkWanderBox();
                boolean moveBack2 = checkBlockers();
                if (moveBack1 || moveBack2) {
                    enemyEntity.send(new CollisionEvent());
                }
            }
        }
    }

    private boolean checkWanderBox() {
        boolean moveBack = false;
        while (!wanderBox.contains(boundingBox)) {
            moveBack();
            moveBack = true;
        }
        return moveBack;
    }

    private boolean checkBlockers() {
        boolean moveBack = false;
        while (doesBoundingBoxOverlapsBlockers()) {
            moveBack();
            moveBack = true;
        }
        return moveBack;
    }

    private void setWanderBox() {
        wanderBox = new Rectangle(currentPosition.x + WANDER_BOX_POSITION,
                                  currentPosition.y + WANDER_BOX_POSITION,
                                  WANDER_BOX_SIZE, WANDER_BOX_SIZE);
    }

    @Override
    public void debug(ShapeRenderer shapeRenderer) {
        shapeRenderer.setColor(Color.PURPLE);
        if (!state.equals(EntityState.IMMOBILE)
            && !state.equals(EntityState.IDLE_ANIMATING)) {
            shapeRenderer.rect(wanderBox.x, wanderBox.y, wanderBox.width, wanderBox.height);
        }
        shapeRenderer.setColor(Color.YELLOW);
        shapeRenderer.rect(boundingBox.x, boundingBox.y, boundingBox.width, boundingBox.height);

        shapeRenderer.setColor(Color.MAGENTA);
        for (TiledNode tiledNode : path) {
            final int x = (int) (tiledNode.x * Constant.HALF_TILE_SIZE);
            final int y = (int) (tiledNode.y * Constant.HALF_TILE_SIZE);
            shapeRenderer.rect(x, y, Constant.HALF_TILE_SIZE, Constant.HALF_TILE_SIZE);
        }
    }

}
