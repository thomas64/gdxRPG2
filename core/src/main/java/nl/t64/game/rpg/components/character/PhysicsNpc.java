package nl.t64.game.rpg.components.character;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import nl.t64.game.rpg.Utils;
import nl.t64.game.rpg.constants.CharacterState;
import nl.t64.game.rpg.constants.Constant;
import nl.t64.game.rpg.events.Event;
import nl.t64.game.rpg.events.character.*;

import java.util.List;


public class PhysicsNpc extends PhysicsComponent {

    private static final float WANDER_BOX_SIZE = 240f;
    private static final float WANDER_BOX_POSITION = -96f;

    private Character npcCharacter;
    private Rectangle wanderBox;

    final String npcId;
    String conversationId;
    boolean isSelected;

    public PhysicsNpc(String npcId) {
        this.npcId = npcId;
        this.isSelected = false;
        this.velocity = Constant.MOVE_SPEED_1;
        this.boundingBoxWidthPercentage = 0.60f;
        this.boundingBoxHeightPercentage = 0.30f;
    }

    @Override
    public void receive(Event event) {
        if (event instanceof LoadCharacterEvent) {
            initNpc((LoadCharacterEvent) event);
        }
        if (event instanceof StateEvent) {
            state = ((StateEvent) event).state;
        }
        if (event instanceof DirectionEvent) {
            direction = ((DirectionEvent) event).direction;
        }
        if (event instanceof SelectEvent) {
            if (state.equals(CharacterState.INVISIBLE)) {
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
    public void update(Character thisNpcCharacter, float dt) {
        this.npcCharacter = thisNpcCharacter;
        relocate(dt);
        checkObstacles();
        npcCharacter.send(new PositionEvent(currentPosition));
        if (isSelected) {
            isSelected = false;
            notifyShowConversationDialog(conversationId, npcId);
        }
    }

    private void initNpc(LoadCharacterEvent loadEvent) {
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
        if (state.equals(CharacterState.WALKING)) {
            move(dt);
        }
    }

    private void checkObstacles() {
        if (state.equals(CharacterState.WALKING)) {
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
        List<Character> theOtherCharacters = Utils.getScreenManager().getWorldScreen()
                                                  .createCopyOfCharactersWithPlayerButWithoutThisNpc(npcCharacter);
        for (Character otherCharacter : theOtherCharacters) {
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
        if (!state.equals(CharacterState.IMMOBILE)
                && !state.equals(CharacterState.FLOATING)) {
            shapeRenderer.rect(wanderBox.x, wanderBox.y, wanderBox.width, wanderBox.height);
        }
        shapeRenderer.setColor(Color.YELLOW);
        shapeRenderer.rect(boundingBox.x, boundingBox.y, boundingBox.width, boundingBox.height);
    }

}
