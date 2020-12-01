package nl.t64.game.rpg.components.character;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.Array;
import nl.t64.game.rpg.Utils;
import nl.t64.game.rpg.constants.Constant;
import nl.t64.game.rpg.events.Event;
import nl.t64.game.rpg.events.character.LoadCharacterEvent;

import java.util.stream.IntStream;


public class GraphicsSparkle extends GraphicsComponent {

    private static final String SPARKLE_PATH = "sprites/objects/sparkle.png";
    private static final int ANIMATION_LENGTH = 35;

    private final Animation<TextureRegion> animation;

    public GraphicsSparkle() {
        this.animation = createAnimation();
    }

    @Override
    public void receive(Event event) {
        if (event instanceof LoadCharacterEvent loadEvent) {
            position = loadEvent.position;
        }
    }

    @Override
    public void update(float dt) {
        setFrame(dt);
    }

    @Override
    public void render(Character sparkle, Batch batch, ShapeRenderer shapeRenderer) {
        batch.draw(currentFrame, position.x, position.y, Constant.TILE_SIZE, Constant.TILE_SIZE);
    }

    @Override
    void setFrame(float dt) {
        frameTime = (frameTime + dt) % 12;
        currentFrame = animation.getKeyFrame(frameTime);
    }

    private Animation<TextureRegion> createAnimation() {
        TextureRegion[][] textures = Utils.getSplitTexture(SPARKLE_PATH, (int) Constant.TILE_SIZE);

        Array<TextureRegion> frames = new Array<>(ANIMATION_LENGTH);
        IntStream.rangeClosed(0, 29)
                 .forEach(i -> frames.add(textures[4][0]));
        frames.add(textures[3][2]);
        frames.add(textures[3][1]);
        frames.add(textures[0][1]);
        frames.add(textures[3][1]);
        frames.add(textures[3][2]);

        return new Animation<>(Constant.FAST_FRAMES, frames, Animation.PlayMode.LOOP);
    }

}
