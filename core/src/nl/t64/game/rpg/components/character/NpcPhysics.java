package nl.t64.game.rpg.components.character;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import nl.t64.game.rpg.constants.CharacterState;
import nl.t64.game.rpg.events.Event;
import nl.t64.game.rpg.events.character.*;
import nl.t64.game.rpg.managers.MapManager;

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
    public void update(Character npcCharacter, List<Character> npcCharactersPlusLastOnePlayer, float dt) {
        relocate(dt);
        checkObstacles(npcCharacter, npcCharactersPlusLastOnePlayer);
        npcCharacter.send(new PositionEvent(currentPosition));
    }

    private void relocate(float dt) {
        if (state == CharacterState.WALKING) {
            move(dt);
        }
    }

    private void checkObstacles(Character npcCharacter, List<Character> npcCharactersPlusLastOnePlayer) {
        if (state == CharacterState.WALKING) {
            boolean moveBack1 = checkWanderBox();
            boolean moveBack2 = checkBlocker();
            boolean moveBack3 = checkOtherCharacters(npcCharacter, npcCharactersPlusLastOnePlayer);
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
        while (MapManager.getInstance().getCurrentMap().isInCollisionWithBlocker(boundingBox)) {
            moveBack();
            moveBack = true;
        }
        return moveBack;
    }

    private boolean checkOtherCharacters(Character npcCharacter, List<Character> npcCharactersPlusLastOnePlayer) {
        boolean moveBack = false;
        for (Character otherCharacter : npcCharactersPlusLastOnePlayer) {
            if (otherCharacter.equals(npcCharacter)) {
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
