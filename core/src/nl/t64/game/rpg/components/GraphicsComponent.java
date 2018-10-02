package nl.t64.game.rpg.components;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import nl.t64.game.rpg.Utility;
import nl.t64.game.rpg.constants.Constant;
import nl.t64.game.rpg.constants.Direction;
import nl.t64.game.rpg.constants.EntityState;
import nl.t64.game.rpg.screens.WorldScreen;


public abstract class GraphicsComponent implements Component {

    private static final float FRAME_DURATION = 0.25f;
    TextureRegion currentFrame = null;
    EntityState state;
    Vector2 position;
    Direction direction;
    private Animation<TextureRegion> walkNorthAnimation;
    private Animation<TextureRegion> walkSouthAnimation;
    private Animation<TextureRegion> walkWestAnimation;
    private Animation<TextureRegion> walkEastAnimation;

    public abstract void update();

    public abstract void render(Batch batch);

    void setFrame() {
        switch (state) {
            case IDLE:
            case IMMOBILE:
                setCurrentFrame(0f);
                break;
            case WALKING:
                setCurrentFrame(WorldScreen.playTime);
                break;
            default:
                throw new IllegalArgumentException(String.format("EntityState '%s' not usable.", state));
        }
    }

    private void setCurrentFrame(float frameTime) {
        switch (direction) {
            case NORTH:
                currentFrame = walkNorthAnimation.getKeyFrame(frameTime);
                break;
            case SOUTH:
                currentFrame = walkSouthAnimation.getKeyFrame(frameTime);
                break;
            case WEST:
                currentFrame = walkWestAnimation.getKeyFrame(frameTime);
                break;
            case EAST:
                currentFrame = walkEastAnimation.getKeyFrame(frameTime);
                break;
            default:
                throw new IllegalArgumentException(String.format("Direction '%s' not usable.", direction));
        }
    }

    void loadWalkingAnimation(String spritePath) {
        Texture texture = Utility.getTextureAsset(spritePath);
        TextureRegion[][] textureFrames = TextureRegion.split(texture, Constant.TILE_SIZE, Constant.TILE_SIZE);

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

        walkSouthAnimation = new Animation<>(FRAME_DURATION, walkSouthFrames, Animation.PlayMode.LOOP);
        walkWestAnimation = new Animation<>(FRAME_DURATION, walkWestFrames, Animation.PlayMode.LOOP);
        walkEastAnimation = new Animation<>(FRAME_DURATION, walkEastFrames, Animation.PlayMode.LOOP);
        walkNorthAnimation = new Animation<>(FRAME_DURATION, walkNorthFrames, Animation.PlayMode.LOOP);
    }

}
