package nl.t64.game.rpg.screens.world.entity;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import nl.t64.game.rpg.Utils;
import nl.t64.game.rpg.constants.Constant;
import nl.t64.game.rpg.screens.world.entity.events.*;


public class PhysicsNpc extends PhysicsComponent {

    private static final float WANDER_BOX_SIZE = 240f;
    private static final float WANDER_BOX_POSITION = -96f;

    private Entity npcEntity;
    private Rectangle wanderBox;

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
                npcEntity.send(new WaitEvent(currentPosition, onActionEvent.playerPosition));
            }
        }
        if (event instanceof OnBumpEvent onBumpEvent) {
            if (onBumpEvent.biggerBoundingBox.overlaps(boundingBox) || onBumpEvent.checkRect.overlaps(boundingBox)) {
                npcEntity.send(new WaitEvent(currentPosition, onBumpEvent.playerPosition));
            }
        }
    }

    @Override
    public void dispose() {
        // empty
    }

    @Override
    public void update(Entity thisNpcEntity, float dt) {
        this.npcEntity = thisNpcEntity;
        relocate(dt);
        checkObstacles();
        npcEntity.send(new PositionEvent(currentPosition));
        if (isSelected) {
            isSelected = false;
            Utils.getBrokerManager().componentObservers.notifyShowConversationDialog(conversationId, npcEntity);
        }
    }

    private void initNpc(LoadEntityEvent loadEvent) {
        state = loadEvent.state;
        currentPosition = loadEvent.position;
        direction = loadEvent.direction;
        conversationId = loadEvent.conversationId;

        wanderBox = new Rectangle(currentPosition.x + WANDER_BOX_POSITION,
                                  currentPosition.y + WANDER_BOX_POSITION,
                                  WANDER_BOX_SIZE, WANDER_BOX_SIZE);
        setBoundingBox();
    }

    private void relocate(float dt) {
        if (state.equals(EntityState.WALKING)) {
            move(dt);
        }
    }

    private void checkObstacles() {
        if (state.equals(EntityState.WALKING)) {
            boolean moveBack1 = checkWanderBox();
            boolean moveBack2 = checkBlockers();
            if (moveBack1 || moveBack2) {
                npcEntity.send(new CollisionEvent());
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

    @Override
    public void debug(ShapeRenderer shapeRenderer) {
        shapeRenderer.setColor(Color.PURPLE);
        if (!state.equals(EntityState.IMMOBILE)
            && !state.equals(EntityState.FLOATING)) {
            shapeRenderer.rect(wanderBox.x, wanderBox.y, wanderBox.width, wanderBox.height);
        }
        shapeRenderer.setColor(Color.YELLOW);
        shapeRenderer.rect(boundingBox.x, boundingBox.y, boundingBox.width, boundingBox.height);
    }

}
