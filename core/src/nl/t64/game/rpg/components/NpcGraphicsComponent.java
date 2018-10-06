package nl.t64.game.rpg.components;

import com.badlogic.gdx.graphics.g2d.Batch;
import nl.t64.game.rpg.constants.Constant;
import nl.t64.game.rpg.events.*;


public class NpcGraphicsComponent extends GraphicsComponent {

    private static final String TAG = NpcGraphicsComponent.class.getSimpleName();

    public NpcGraphicsComponent() {
        this.frameDuration = 0.25f;
    }

    @Override
    public void receive(Event event) {
        if (event instanceof LoadSpriteEvent) {
            LoadSpriteEvent loadSpriteEvent = (LoadSpriteEvent) event;
            loadWalkingAnimation(loadSpriteEvent.getPath(), loadSpriteEvent.getCol(), loadSpriteEvent.getRow());
        }
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
