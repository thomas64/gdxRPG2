package nl.t64.game.rpg.components;

import com.badlogic.gdx.graphics.g2d.Batch;
import nl.t64.game.rpg.Utility;
import nl.t64.game.rpg.constants.Constant;
import nl.t64.game.rpg.events.*;


public class NpcGraphicsComponent extends GraphicsComponent {

    private static final String TAG = NpcGraphicsComponent.class.getSimpleName();
    private static final String DEFAULT_SPRITE_PATH = "sprites/characters/woman1.png";

    public NpcGraphicsComponent() {
        Utility.loadTextureAsset(DEFAULT_SPRITE_PATH);
        loadWalkingAnimation(DEFAULT_SPRITE_PATH);
    }

    @Override
    public void receive(Event event) {
        if (event instanceof StartStateEvent) {
            state = ((StartStateEvent) event).getState();
        }
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
    }

    @Override
    public void dispose() {
        Utility.unloadAsset(DEFAULT_SPRITE_PATH);
    }

    @Override
    public void update() {
        setFrame();
    }

    @Override
    public void render(Batch batch) {
        batch.draw(currentFrame, position.x, position.y, Constant.TILE_SIZE, Constant.TILE_SIZE);
    }

}
