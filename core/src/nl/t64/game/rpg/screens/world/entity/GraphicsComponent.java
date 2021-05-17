package nl.t64.game.rpg.screens.world.entity;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.GdxRuntimeException;
import nl.t64.game.rpg.Utils;
import nl.t64.game.rpg.constants.Constant;


abstract class GraphicsComponent implements Component {

    float frameTime = 0f;
    float frameDuration;
    TextureRegion currentFrame = null;
    EntityState state;
    Vector2 position;
    Direction direction;
    Animation<TextureRegion> walkNorthAnimation;
    Animation<TextureRegion> walkSouthAnimation;
    Animation<TextureRegion> walkWestAnimation;
    Animation<TextureRegion> walkEastAnimation;

    public abstract void update(float dt);

    public abstract void render(Batch batch);

    public abstract void renderOnMiniMap(Entity entity, Batch batch, ShapeRenderer shapeRenderer);

    @Override
    public void dispose() {
        // empty
    }

    void setFrame(float dt) {
        switch (state) {
            case INVISIBLE:
                return;
            case IDLE:
            case ALIGNING:
            case IMMOBILE:
                setCurrentFrame(Constant.NO_FRAMES);
                // the next line sets player always just 1 dt moment before the end of normal stance when standing still.
                frameTime = frameDuration - dt;
                break;
            case WALKING:
            case FLOATING:
                frameTime = (frameTime + dt) % 12; // dividable by 0.15, 0.25 and 0.5, these are player speed frames.
                if (frameDuration == Constant.NO_FRAMES) { // no player animation when high speed moving.
                    setCurrentFrame(Constant.NO_FRAMES);
                } else {
                    setCurrentFrame(frameTime);
                }
                break;
            default:
                throw new IllegalArgumentException(String.format("EntityState '%s' not usable.", state));
        }
    }

    private void setCurrentFrame(float frameTime) {
        if (frameTime < 0) {
            frameTime = Constant.NO_FRAMES;
        }
        currentFrame = switch (direction) {
            case NORTH -> walkNorthAnimation.getKeyFrame(frameTime);
            case SOUTH -> walkSouthAnimation.getKeyFrame(frameTime);
            case WEST -> walkWestAnimation.getKeyFrame(frameTime);
            case EAST -> walkEastAnimation.getKeyFrame(frameTime);
            case NONE -> throw new IllegalArgumentException("Direction 'NONE' is not usable.");
        };
    }

    void setFrameDuration(float moveSpeed) {
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

    void loadWalkingAnimation(String spriteId) {
        TextureRegion[][] textureFrames = Utils.getCharImage(spriteId);

        Array<TextureRegion> walkSouthFrames = new Array<>(new TextureRegion[]{
                textureFrames[0][1], textureFrames[0][0], textureFrames[0][1], textureFrames[0][2]});

        Array<TextureRegion> walkWestFrames = new Array<>(new TextureRegion[]{
                textureFrames[1][1], textureFrames[1][0], textureFrames[1][1], textureFrames[1][2]});

        Array<TextureRegion> walkEastFrames = new Array<>(new TextureRegion[]{
                textureFrames[2][1], textureFrames[2][0], textureFrames[2][1], textureFrames[2][2]});

        Array<TextureRegion> walkNorthFrames = new Array<>(new TextureRegion[]{
                textureFrames[3][1], textureFrames[3][0], textureFrames[3][1], textureFrames[3][2]});

        walkSouthAnimation = new Animation<>(frameDuration, walkSouthFrames, Animation.PlayMode.LOOP);
        walkWestAnimation = new Animation<>(frameDuration, walkWestFrames, Animation.PlayMode.LOOP);
        walkEastAnimation = new Animation<>(frameDuration, walkEastFrames, Animation.PlayMode.LOOP);
        walkNorthAnimation = new Animation<>(frameDuration, walkNorthFrames, Animation.PlayMode.LOOP);
    }

    Animation<TextureRegion> getAnimation() {
        if (state.equals(EntityState.RUNNING)) {
            setFrameDuration(Constant.MOVE_SPEED_3);
        } else {
            setFrameDuration(Constant.MOVE_SPEED_2);
        }
        return switch (direction) {
            case NORTH -> walkNorthAnimation;
            case SOUTH -> walkSouthAnimation;
            case WEST -> walkWestAnimation;
            case EAST -> walkEastAnimation;
            case NONE -> throw new GdxRuntimeException("No animation for direction NONE.");
        };
    }

}
