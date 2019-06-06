package nl.t64.game.rpg.components.character;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import nl.t64.game.rpg.Utils;
import nl.t64.game.rpg.constants.CharacterState;
import nl.t64.game.rpg.events.Event;
import nl.t64.game.rpg.events.character.*;

import java.util.List;


public class PhysicsNpc extends PhysicsComponent {

    private static final float WANDER_BOX_SIZE = 240f;
    private static final float WANDER_BOX_POSITION = -96f;

    private Rectangle wanderBox;

    public PhysicsNpc() {
        this.velocity = 48f;
        this.boundingBoxWidthPercentage = 0.80f;
        this.boundingBoxHeightPercentage = 1.00f;
    }

    @Override
    public void receive(Event event) {
        if (event instanceof StartStateEvent) {
            state = ((StartStateEvent) event).state;
        }
        if (event instanceof StateEvent) {
            state = ((StateEvent) event).state;
        }
        if (event instanceof StartPositionEvent) {
            currentPosition = ((StartPositionEvent) event).position;
            wanderBox = new Rectangle(currentPosition.x + WANDER_BOX_POSITION,
                                      currentPosition.y + WANDER_BOX_POSITION,
                                      WANDER_BOX_SIZE, WANDER_BOX_SIZE);
            setBoundingBox();
        }
        if (event instanceof StartDirectionEvent) {
            direction = ((StartDirectionEvent) event).direction;
        }
        if (event instanceof DirectionEvent) {
            direction = ((DirectionEvent) event).direction;
        }
    }

    @Override
    public void dispose() {
        // empty
    }

    @Override
    public void update(Character thisNpcCharacter, float dt) {
        relocate(dt);
        checkObstacles(thisNpcCharacter);
        thisNpcCharacter.send(new PositionEvent(currentPosition));
    }

    private void relocate(float dt) {
        if (state == CharacterState.WALKING) {
            move(dt);
        }
    }

    private void checkObstacles(Character thisNpcCharacter) {
        if (state == CharacterState.WALKING) {
            boolean moveBack1 = checkWanderBox();
            boolean moveBack2 = checkBlocker();
            boolean moveBack3 = checkOtherCharacters(thisNpcCharacter);
            if (moveBack1 || moveBack2 || moveBack3) {
                thisNpcCharacter.send(new CollisionEvent());
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

    private boolean checkOtherCharacters(Character thisNpcCharacter) {
        boolean moveBack = false;
        List<Character> theOtherCharacters = Utils.getScreenManager().getWorldScreen()
                                                  .createCopyOfCharactersWithPlayerButWithoutThisNpc(thisNpcCharacter);
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
        if (state != CharacterState.IMMOBILE) {
            shapeRenderer.rect(wanderBox.x, wanderBox.y, wanderBox.width, wanderBox.height);
        }
        shapeRenderer.setColor(Color.YELLOW);
        shapeRenderer.rect(boundingBox.x, boundingBox.y, boundingBox.width, boundingBox.height);
    }

}
