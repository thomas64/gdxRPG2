package nl.t64.game.rpg.screens.world.entity;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import nl.t64.game.rpg.Utils;
import nl.t64.game.rpg.constants.Constant;
import nl.t64.game.rpg.screens.world.entity.events.*;


public class PhysicsNpc extends PhysicsComponent {

    String conversationId;
    private boolean isSelected;

    public PhysicsNpc() {
        this.isSelected = false;
        this.velocity = Constant.MOVE_SPEED_1;
        this.boundingBoxWidthPercentage = 0.50f;
        this.boundingBoxHeightPercentage = 0.30f;
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
        if (event instanceof OnActionEvent onActionEvent) {
            if (onActionEvent.checkRect.overlaps(boundingBox)) {
                isSelected = true;
                entity.send(new WaitEvent(currentPosition, onActionEvent.playerPosition));
            }
        }
        if (event instanceof OnBumpEvent onBumpEvent) {
            if (onBumpEvent.biggerBoundingBox.overlaps(boundingBox) || onBumpEvent.checkRect.overlaps(boundingBox)) {
                entity.send(new WaitEvent(currentPosition, onBumpEvent.playerPosition));
            }
        }
    }

    private void initNpc(LoadEntityEvent loadEvent) {
        state = loadEvent.state;
        currentPosition = loadEvent.position;
        direction = loadEvent.direction;
        conversationId = loadEvent.conversationId;
        setWanderBox();
        setBoundingBox();
    }

    @Override
    public void update(Entity thisNpcEntity, float dt) {
        entity = thisNpcEntity;
        relocate(dt);
        checkObstacles();
        entity.send(new PositionEvent(currentPosition));
        if (isSelected) {
            isSelected = false;
            Utils.getBrokerManager().componentObservers.notifyShowConversationDialog(conversationId, entity);
        }
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
    }

}
