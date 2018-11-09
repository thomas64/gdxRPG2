package nl.t64.game.rpg.components.character;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import nl.t64.game.rpg.Engine;
import nl.t64.game.rpg.constants.CharacterState;
import nl.t64.game.rpg.events.Event;
import nl.t64.game.rpg.events.character.*;

import java.util.List;


public class NpcPhysics extends PhysicsComponent {

    private static final String TAG = NpcPhysics.class.getSimpleName();
    private static final int WANDER_BOX_SIZE = 240;
    private static final int WANDER_BOX_POSITION = -96;

    private Rectangle wanderBox;

    public NpcPhysics() {
        this.velocity = 48f;
        this.boundingBoxWidthPercentage = 0.80f;
        this.boundingBoxHeightPercentage = 1.00f;
    }

    @Override
    public void receive(Event event) {
        if (event instanceof StartStateEvent) {
            state = ((StartStateEvent) event).getState();
        }
        if (event instanceof StateEvent) {
            state = ((StateEvent) event).getState();
        }
        if (event instanceof StartPositionEvent) {
            currentPosition = ((StartPositionEvent) event).getPosition();
            wanderBox = new Rectangle(currentPosition.x + WANDER_BOX_POSITION,
                                      currentPosition.y + WANDER_BOX_POSITION,
                                      WANDER_BOX_SIZE, WANDER_BOX_SIZE);
            setBoundingBox();
        }
        if (event instanceof StartDirectionEvent) {
            direction = ((StartDirectionEvent) event).getDirection();
        }
        if (event instanceof DirectionEvent) {
            direction = ((DirectionEvent) event).getDirection();
        }
    }

    @Override
    public void dispose() {
        // empty
    }

    @Override
    public void update(Character npcCharacter, Engine engine,
                       List<Character> npcCharactersPlusLastOnePlayer, float dt) {
        updateTheseFields(npcCharacter, engine, npcCharactersPlusLastOnePlayer);
        relocate(dt);
        checkObstacles();
        thisCharacter.send(new PositionEvent(currentPosition));
        clearTheseFields();
    }

    private void relocate(float dt) {
        if (state == CharacterState.WALKING) {
            move(dt);
        }
    }

    private void checkObstacles() {
        if (state == CharacterState.WALKING) {
            boolean moveBack1 = checkWanderBox();
            boolean moveBack2 = checkBlocker();
            boolean moveBack3 = checkOtherCharacters();
            if (moveBack1 || moveBack2 || moveBack3) {
                thisCharacter.send(new CollisionEvent());
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
        while (engine.getWorldScreen().getCurrentMap().areBlockersCurrentlyBlocking(boundingBox)) {
            moveBack();
            moveBack = true;
        }
        return moveBack;
    }

    private boolean checkOtherCharacters() {
        boolean moveBack = false;
        for (Character otherCharacter : theOtherCharacters) {
            if (otherCharacter.equals(thisCharacter)) {
                continue;
            }
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
        shapeRenderer.rect(wanderBox.x, wanderBox.y, wanderBox.width, wanderBox.height);
        shapeRenderer.setColor(Color.YELLOW);
        shapeRenderer.rect(boundingBox.x, boundingBox.y, boundingBox.width, boundingBox.height);
    }

}
