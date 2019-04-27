package nl.t64.game.rpg.components.character;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import nl.t64.game.rpg.SpriteConfig;
import nl.t64.game.rpg.Utility;
import nl.t64.game.rpg.constants.Constant;
import nl.t64.game.rpg.events.Event;
import nl.t64.game.rpg.events.character.*;


public class PlayerGraphics extends GraphicsComponent {

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
    public void update(float dt) {
        setFrame(dt);
    }

    @Override
    public void render(Character player, Batch batch, ShapeRenderer shapeRenderer) {
        batch.draw(currentFrame, position.x, position.y, Constant.TILE_SIZE, Constant.TILE_SIZE);
    }

    private void setFrameDuration(float moveSpeed) {
        if (moveSpeed == Constant.MOVE_SPEED_1) {
            frameDuration = Constant.SLOW_FRAMES;
        } else if (moveSpeed == Constant.MOVE_SPEED_2) {
            frameDuration = Constant.NORMAL_FRAMES;
        } else if (moveSpeed == Constant.MOVE_SPEED_3) {
            frameDuration = Constant.FAST_FRAMES;
        } else if (moveSpeed == Constant.MOVE_SPEED_4) {
            frameDuration = Constant.NO_FRAMES;
        }
        walkNorthAnimation.setFrameDuration(frameDuration);
        walkSouthAnimation.setFrameDuration(frameDuration);
        walkWestAnimation.setFrameDuration(frameDuration);
        walkEastAnimation.setFrameDuration(frameDuration);
    }

}
