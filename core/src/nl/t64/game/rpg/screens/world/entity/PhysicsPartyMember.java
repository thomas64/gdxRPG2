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


public class PhysicsPartyMember extends PhysicsComponent {

    private DefaultGraphPath<TiledNode> path;

    public PhysicsPartyMember() {
        this.boundingBoxWidthPercentage = 0.50f;
        this.boundingBoxHeightPercentage = 0.20f;
    }

    @Override
    public void receive(Event event) {
        if (event instanceof LoadEntityEvent loadEvent) {
            state = loadEvent.state;
            currentPosition = loadEvent.position;
            setBoundingBox();
        }
        if (event instanceof StateEvent stateEvent) {
            state = stateEvent.state();
        }
        if (event instanceof DirectionEvent directionEvent) {
            direction = directionEvent.direction();
        }
        if (event instanceof OnDetectionEvent onDetectionEvent) {
            velocity = onDetectionEvent.moveSpeed();
        }
        if (event instanceof PathUpdateEvent pathUpdateEvent) {
            path = pathUpdateEvent.path();
        }
    }

    @Override
    public void update(Entity partyMember, float dt) {
        entity = partyMember;
        relocate(dt);
        checkObstacles(dt);
        partyMember.send(new PositionEvent(currentPosition));
    }

    private void checkObstacles(float dt) {
        if (!Utils.getBrokerManager().blockObservers.getCurrentBlockersFor(boundingBox).isEmpty()) {
            Vector2 positionInGrid = entity.getPositionInGrid();
            direction = new PathfindingObstacleChecker(positionInGrid, direction).getNewDirection();
            currentPosition.set(oldPosition);
            move(dt);
        }
    }

    @Override
    public void debug(ShapeRenderer shapeRenderer) {
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
