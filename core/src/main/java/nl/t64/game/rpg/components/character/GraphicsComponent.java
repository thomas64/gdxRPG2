package nl.t64.game.rpg.components.character;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import nl.t64.game.rpg.Utils;
import nl.t64.game.rpg.constants.CharacterState;
import nl.t64.game.rpg.constants.Constant;
import nl.t64.game.rpg.constants.Direction;


abstract class GraphicsComponent extends ComponentSubject implements Component {

    private float frameTime = 0f;
    float frameDuration;
    TextureRegion currentFrame = null;
    CharacterState state;
    Vector2 position;
    Direction direction;
    Animation<TextureRegion> walkNorthAnimation;
    Animation<TextureRegion> walkSouthAnimation;
    Animation<TextureRegion> walkWestAnimation;
    Animation<TextureRegion> walkEastAnimation;

    public abstract void update(float dt);

    public abstract void render(Character character, Batch batch, ShapeRenderer shapeRenderer);

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
                throw new IllegalArgumentException(String.format("CharacterState '%s' not usable.", state));
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

        Array<TextureRegion> walkNorthFrames = new Array<>(4);
        Array<TextureRegion> walkSouthFrames = new Array<>(4);
        Array<TextureRegion> walkWestFrames = new Array<>(4);
        Array<TextureRegion> walkEastFrames = new Array<>(4);

        walkSouthFrames.insert(0, textureFrames[0][1]);
        walkSouthFrames.insert(1, textureFrames[0][0]);
        walkSouthFrames.insert(2, textureFrames[0][1]);
        walkSouthFrames.insert(3, textureFrames[0][2]);

        walkWestFrames.insert(0, textureFrames[1][1]);
        walkWestFrames.insert(1, textureFrames[1][0]);
        walkWestFrames.insert(2, textureFrames[1][1]);
        walkWestFrames.insert(3, textureFrames[1][2]);

        walkEastFrames.insert(0, textureFrames[2][1]);
        walkEastFrames.insert(1, textureFrames[2][0]);
        walkEastFrames.insert(2, textureFrames[2][1]);
        walkEastFrames.insert(3, textureFrames[2][2]);

        walkNorthFrames.insert(0, textureFrames[3][1]);
        walkNorthFrames.insert(1, textureFrames[3][0]);
        walkNorthFrames.insert(2, textureFrames[3][1]);
        walkNorthFrames.insert(3, textureFrames[3][2]);

        walkSouthAnimation = new Animation<>(frameDuration, walkSouthFrames, Animation.PlayMode.LOOP);
        walkWestAnimation = new Animation<>(frameDuration, walkWestFrames, Animation.PlayMode.LOOP);
        walkEastAnimation = new Animation<>(frameDuration, walkEastFrames, Animation.PlayMode.LOOP);
        walkNorthAnimation = new Animation<>(frameDuration, walkNorthFrames, Animation.PlayMode.LOOP);
    }

}
