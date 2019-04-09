package nl.t64.game.rpg.components.character;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import nl.t64.game.rpg.Engine;
import nl.t64.game.rpg.SpriteConfig;
import nl.t64.game.rpg.Utility;
import nl.t64.game.rpg.constants.CharacterState;
import nl.t64.game.rpg.constants.Constant;
import nl.t64.game.rpg.constants.Direction;


abstract class GraphicsComponent implements Component {

    private static final int SPRITE_GROUP_WIDTH = 144;
    private static final int SPRITE_GROUP_HEIGHT = 192;

    float frameDuration;
    TextureRegion currentFrame = null;
    CharacterState state;
    Vector2 position;
    Direction direction;
    Animation<TextureRegion> walkNorthAnimation;
    Animation<TextureRegion> walkSouthAnimation;
    Animation<TextureRegion> walkWestAnimation;
    Animation<TextureRegion> walkEastAnimation;

    public abstract void update();

    public abstract void render(Character character, Batch batch, ShapeRenderer shapeRenderer);

    void setFrame() {
        switch (state) {
            case IDLE:
            case ALIGNING:
            case IMMOBILE:
                setCurrentFrame(0f);
                break;
            case WALKING:
                if (frameDuration == 0f) { // no player animation when high speed moving.
                    setCurrentFrame(0f);
                } else {
                    setCurrentFrame(Engine.getRunTime());
                }
                break;
            default:
                throw new IllegalArgumentException(String.format("CharacterState '%s' not usable.", state));
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

    void loadWalkingAnimation(SpriteConfig spriteConfig) {
        String path = spriteConfig.getCharPath();
        int row = spriteConfig.getRow() - 1;
        int col = spriteConfig.getCol() - 1;

        Texture texture = Utility.getTextureAsset(path);
        TextureRegion[][] splitOfEight = TextureRegion.split(texture, SPRITE_GROUP_WIDTH, SPRITE_GROUP_HEIGHT);
        TextureRegion personSprite = splitOfEight[row][col];
        TextureRegion[][] textureFrames = personSprite.split((int) Constant.TILE_SIZE, (int) Constant.TILE_SIZE);

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
