package nl.t64.game.rpg.screens.world.entity;

import com.badlogic.gdx.ai.pfa.DefaultGraphPath;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import nl.t64.game.rpg.Utils;
import nl.t64.game.rpg.constants.Constant;
import nl.t64.game.rpg.screens.world.entity.events.*;
import nl.t64.game.rpg.screens.world.pathfinding.PathfindingObstacleChecker;
import nl.t64.game.rpg.screens.world.pathfinding.TiledNode;


public class PhysicsEnemy extends PhysicsComponent {

    private static final int MINIMAL_NODES_BEFORE_STARTING_A_BATTLE = 5;

    private String battleId;
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
            state = stateEvent.state();
        }
        if (event instanceof DirectionEvent directionEvent) {
            direction = directionEvent.direction();
        }
        if (event instanceof PathUpdateEvent pathUpdateEvent) {
            path = pathUpdateEvent.path();
        }
        if (event instanceof DetectionEvent detectionEvent) {
            isDetectingPlayer = detectionEvent.isDetectingPlayer();
        }
    }

    private void initNpc(LoadEntityEvent loadEvent) {
        state = loadEvent.state;
        currentPosition = loadEvent.position;
        direction = loadEvent.direction;
        battleId = loadEvent.conversationOrBattleId;
        setWanderBox();
        setBoundingBox();
    }

    @Override
    public void update(Entity thisEnemyEntity, float dt) {
        entity = thisEnemyEntity;
        relocate(dt);
        if (isDetectingPlayer) {
            checkObstaclesWhileDetecting(dt);
        } else {
            checkObstacles();
        }
        entity.send(new PositionEvent(currentPosition));
        if (isNearbyPlayer()) {
            Utils.getBrokerManager().componentObservers.notifyShowBattleScreen(battleId);
        }
    }

    private void checkObstaclesWhileDetecting(float dt) {
        setWanderBox();
        if (!Utils.getBrokerManager().blockObservers.getCurrentBlockersFor(boundingBox).isEmpty()) {
            Vector2 positionInGrid = entity.getPositionInGrid();
            direction = new PathfindingObstacleChecker(positionInGrid, direction).getNewDirection();
            currentPosition.set(oldPosition);
            move(dt);
        }
    }

    private boolean isNearbyPlayer() {
        return path.getCount() > 0 && path.getCount() <= MINIMAL_NODES_BEFORE_STARTING_A_BATTLE;
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
