package nl.t64.game.rpg.components.character;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import nl.t64.game.rpg.SpriteConfig;
import nl.t64.game.rpg.Utils;
import nl.t64.game.rpg.constants.Constant;
import nl.t64.game.rpg.events.Event;
import nl.t64.game.rpg.events.character.*;


public class GraphicsPlayer extends GraphicsComponent {

    public GraphicsPlayer() {
        SpriteConfig spriteConfig = Utils.getResourceManager().getSpriteConfig(Constant.PLAYER_ID);
        loadWalkingAnimation(spriteConfig);
    }

    @Override
    public void receive(Event event) {
        if (event instanceof LoadCharacterEvent) {
            direction = ((LoadCharacterEvent) event).direction;
        }
        if (event instanceof StateEvent) {
            state = ((StateEvent) event).state;
        }
        if (event instanceof DirectionEvent) {
            direction = ((DirectionEvent) event).direction;
        }
        if (event instanceof PositionEvent) {
            position = ((PositionEvent) event).position;
        }
        if (event instanceof SpeedEvent) {
            float moveSpeed = ((SpeedEvent) event).moveSpeed;
            setFrameDuration(moveSpeed);
        }
    }

    @Override
    public void update(float dt) {
        setFrame(dt);
    }

    @Override
    public void render(Character player, Batch batch, ShapeRenderer shapeRenderer) {
        batch.draw(currentFrame, position.x, position.y, Constant.TILE_SIZE, Constant.TILE_SIZE);
    }

}
