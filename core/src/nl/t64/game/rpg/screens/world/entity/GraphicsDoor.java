package nl.t64.game.rpg.screens.world.entity;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.Array;
import nl.t64.game.rpg.Utils;
import nl.t64.game.rpg.components.door.Door;
import nl.t64.game.rpg.constants.Constant;
import nl.t64.game.rpg.screens.world.entity.events.Event;
import nl.t64.game.rpg.screens.world.entity.events.LoadEntityEvent;
import nl.t64.game.rpg.screens.world.entity.events.StateEvent;


public class GraphicsDoor extends GraphicsComponent {

    private final Door door;
    private final Animation<TextureRegion> openAnimation;

    public GraphicsDoor(Door door) {
        this.door = door;
        this.openAnimation = loadOpenAnimation();
    }

    @Override
    public void receive(Event event) {
        if (event instanceof LoadEntityEvent loadEvent) {
            position = loadEvent.position;
            state = loadEvent.state;
        }
        if (event instanceof StateEvent stateEvent) {
            state = stateEvent.state;
        }
    }

    @Override
    public void update(float dt) {
        setFrame(dt);
    }

    @Override
    public void render(Batch batch) {
        batch.draw(currentFrame, position.x, position.y, door.getWidth(), door.getHeight());
    }

    @Override
    public void renderOnMiniMap(Entity entity, Batch batch, ShapeRenderer shapeRenderer) {
        // empty
    }

    @Override
    void setFrame(float dt) {
        if (frameTime >= 1f) {
            frameTime = 1f;
        } else if (state.equals(EntityState.OPENED)) {
            frameTime = frameTime + dt;
        } else {
            frameTime = 0f;
        }
        currentFrame = openAnimation.getKeyFrame(frameTime);
    }

    private Animation<TextureRegion> loadOpenAnimation() {
        TextureRegion[][] textureFrames = Utils.getDoorImage(door.getSpriteId(), (int) door.getWidth());
        Array<TextureRegion> frames = new Array<>(new TextureRegion[]{
                textureFrames[0][0], textureFrames[1][0], textureFrames[2][0], textureFrames[3][0]});
        return new Animation<>(Constant.FAST_FRAMES, frames, Animation.PlayMode.NORMAL);
    }

}
