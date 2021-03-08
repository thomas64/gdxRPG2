package nl.t64.game.rpg.screens.world.entity;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import nl.t64.game.rpg.Utils;
import nl.t64.game.rpg.constants.Constant;
import nl.t64.game.rpg.screens.world.entity.events.*;

import java.util.List;


public class PhysicsNpc extends PhysicsComponent {

    private static final float WANDER_BOX_SIZE = 240f;
    private static final float WANDER_BOX_POSITION = -96f;

    private Entity npcCharacter;
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
        if (event instanceof SelectEvent) {
            if (state.equals(EntityState.INVISIBLE)) {
                Utils.getScreenManager().getWorldScreen()
                     .getVisibleNpcOfInvisibleNpcBy(conversationId)
                     .send(new SelectEvent());
            } else {
                isSelected = true;
            }
        }
    }

    @Override
    public void dispose() {
        // empty
    }

    @Override
    public void update(Entity thisNpcCharacter, float dt) {
        this.npcCharacter = thisNpcCharacter;
        relocate(dt);
        checkObstacles();
        npcCharacter.send(new PositionEvent(currentPosition));
        if (isSelected) {
            isSelected = false;
            componentSubject.notifyShowConversationDialog(conversationId, npcCharacter);
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
            boolean moveBack2 = checkBlocker();
            boolean moveBack3 = checkOtherCharacters();
            if (moveBack1 || moveBack2 || moveBack3) {
                npcCharacter.send(new CollisionEvent());
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

    private boolean checkBlocker() {
        boolean moveBack = false;
        while (Utils.getMapManager().areBlockersCurrentlyBlocking(boundingBox)) {
            moveBack();
            moveBack = true;
        }
        return moveBack;
    }

    private boolean checkOtherCharacters() {
        boolean moveBack = false;
        List<Entity> theOtherCharacters = Utils.getScreenManager().getWorldScreen()
                                               .createCopyOfCharactersWithPlayerButWithoutThisNpc(npcCharacter);
        for (Entity otherCharacter : theOtherCharacters) {
            while (boundingBox.overlaps(otherCharacter.getBoundingBox())) {
                moveBack();
                moveBack = true;
            }
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
