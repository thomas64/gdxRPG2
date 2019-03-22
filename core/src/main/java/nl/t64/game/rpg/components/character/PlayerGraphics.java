package nl.t64.game.rpg.components.character;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import nl.t64.game.rpg.Utility;
import nl.t64.game.rpg.constants.Constant;
import nl.t64.game.rpg.events.Event;
import nl.t64.game.rpg.events.character.*;


public class PlayerGraphics extends GraphicsComponent {

    private static final String TAG = PlayerGraphics.class.getSimpleName();
    private static final float SLOW_FRAMES = 0.50f;
    private static final float NORMAL_FRAMES = 0.25f;
    private static final float FAST_FRAMES = 0.15f;
    private static final float NO_FRAMES = 0f;

    public PlayerGraphics() {
        SpriteConfig spriteConfig = Utility.getSpriteConfig(Constant.PLAYER_ID);
        loadWalkingAnimation(spriteConfig);
    }

    @Override
    public void receive(Event event) {
        if (event instanceof StateEvent) {
            state = ((StateEvent) event).getState();
        }
        if (event instanceof StartDirectionEvent) {
            direction = ((StartDirectionEvent) event).getDirection();
        }
        if (event instanceof DirectionEvent) {
            direction = ((DirectionEvent) event).getDirection();
        }
        if (event instanceof PositionEvent) {
            position = ((PositionEvent) event).getPosition();
        }
        if (event instanceof SpeedEvent) {
            float moveSpeed = ((SpeedEvent) event).getMoveSpeed();
            setFrameDuration(moveSpeed);
        }
    }

    @Override
    public void dispose() {
        // empty
    }

    @Override
    public void update() {
        setFrame();
    }

    @Override
    public void render(Character player, Batch batch, ShapeRenderer shapeRenderer) {
        batch.draw(currentFrame, position.x, position.y, Constant.TILE_SIZE, Constant.TILE_SIZE);
    }

    private void setFrameDuration(float moveSpeed) {
        if (moveSpeed == Constant.MOVE_SPEED_1) {
            frameDuration = SLOW_FRAMES;
        } else if (moveSpeed == Constant.MOVE_SPEED_2) {
            frameDuration = NORMAL_FRAMES;
        } else if (moveSpeed == Constant.MOVE_SPEED_3) {
            frameDuration = FAST_FRAMES;
        } else if (moveSpeed == Constant.MOVE_SPEED_4) {
            frameDuration = NO_FRAMES;
        }
        walkNorthAnimation.setFrameDuration(frameDuration);
        walkSouthAnimation.setFrameDuration(frameDuration);
        walkWestAnimation.setFrameDuration(frameDuration);
        walkEastAnimation.setFrameDuration(frameDuration);
    }

}
