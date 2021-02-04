package nl.t64.game.rpg.components.character;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.Array;
import nl.t64.game.rpg.Utils;
import nl.t64.game.rpg.components.door.Door;
import nl.t64.game.rpg.constants.Constant;
import nl.t64.game.rpg.events.Event;
import nl.t64.game.rpg.events.character.LoadCharacterEvent;
import nl.t64.game.rpg.events.character.StateEvent;


public class GraphicsDoor extends GraphicsComponent {

    private final Door door;
    private final Animation<TextureRegion> openAnimation;

    public GraphicsDoor(Door door) {
        this.door = door;
        this.openAnimation = loadOpenAnimation();
    }

    @Override
    public void receive(Event event) {
        if (event instanceof LoadCharacterEvent loadEvent) {
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
    public void render(Character character, Batch batch, ShapeRenderer shapeRenderer) {
        batch.draw(currentFrame, position.x, position.y, door.getWidth(), door.getHeight());
    }

    @Override
    void setFrame(float dt) {
        if (frameTime >= 1f) {
            frameTime = 1f;
        } else if (state.equals(CharacterState.OPENED)) {
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