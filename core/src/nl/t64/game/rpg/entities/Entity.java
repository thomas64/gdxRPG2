package nl.t64.game.rpg.entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import lombok.Getter;
import lombok.Setter;
import nl.t64.game.rpg.Logger;
import nl.t64.game.rpg.Utility;
import nl.t64.game.rpg.constants.Constant;
import nl.t64.game.rpg.constants.Direction;
import nl.t64.game.rpg.constants.EntityState;

import java.util.UUID;

public class Entity {

    private static final String TAG = Entity.class.getSimpleName();
    private static final String DEFAULT_SPRITE_PATH = "sprites/characters/hero1.png";

    private final int frameWidth = Constant.TILE_SIZE;
    private final int frameHeight = Constant.TILE_SIZE;

    @Getter
    private Rectangle boundingBox;

    @Getter
    private Vector2 nextPlayerPosition;
    @Getter
    private Vector2 currentPlayerPostion;

    @Getter
    @Setter
    private EntityState state = EntityState.IDLE;

    @Getter
    private float frameTime = 0f;

    @Getter
    private Sprite playerSprite = null;
    @Getter
    private TextureRegion currentFrame = null;

    private Vector2 velocity;
    private String entityId;

    @Getter
    private Direction currentDirection = Direction.WEST;
    private Direction previousDirection = Direction.NORTH;

    private Animation<TextureRegion> walkNorthAnimation;
    private Animation<TextureRegion> walkSouthAnimation;
    private Animation<TextureRegion> walkWestAnimation;
    private Animation<TextureRegion> walkEastAnimation;
    private Array<TextureRegion> walkNorthFrames;
    private Array<TextureRegion> walkSouthFrames;
    private Array<TextureRegion> walkWestFrames;
    private Array<TextureRegion> walkEastFrames;

    public Entity() {
        this.entityId = UUID.randomUUID().toString();
        this.boundingBox = new Rectangle();
        this.nextPlayerPosition = new Vector2();
        this.currentPlayerPostion = new Vector2();
        this.velocity = new Vector2(192f, 192f);  // 48 * 4

        Utility.loadTextureAsset(DEFAULT_SPRITE_PATH);
        loadDefaultSprite();
        loadAllAnimations();
    }

    public void update(float delta) {
        frameTime = (frameTime + delta) % 5; // want to avoid overflow
        setBoundingBoxSize(0f, 0.5f);  // hitbox at the feet
    }

    public void init(Vector2 spawnPosition) {
        currentPlayerPostion.x = spawnPosition.x;
        currentPlayerPostion.y = spawnPosition.y;
        nextPlayerPosition.x = spawnPosition.x;
        nextPlayerPosition.y = spawnPosition.y;
    }

    public void setStandingStillFrame() {
        Texture texture = Utility.getTextureAsset(DEFAULT_SPRITE_PATH);
        TextureRegion[][] textureFrames = TextureRegion.split(texture, frameWidth, frameHeight);

        switch (currentDirection) {
            case NORTH:
                currentFrame = textureFrames[3][1];
                break;
            case SOUTH:
                currentFrame = textureFrames[0][1];
                break;
            case WEST:
                currentFrame = textureFrames[1][1];
                break;
            case EAST:
                currentFrame = textureFrames[2][1];
                break;
            default:
                break;
        }
    }

    public void setBoundingBoxSize(float percentageWidthReduced, float percentageHeightReduced) {
        float width;
        float height;

        float widthReductionAmount = 1.0f - percentageWidthReduced; //.8f for 20% (1 - .20)
        float heightReductionAmount = 1.0f - percentageHeightReduced; //.8f for 20% (1 - .20)

        if (widthReductionAmount > 0 && widthReductionAmount < 1) {
            width = frameWidth * widthReductionAmount;
        } else {
            width = frameWidth;
        }
        if (heightReductionAmount > 0 && heightReductionAmount < 1) {
            height = frameHeight * heightReductionAmount;
        } else {
            height = frameHeight;
        }

        if (width == 0 || height == 0) {
            Logger.boundingBoxIsZero(TAG, width, height);
        }

        float minX = nextPlayerPosition.x;
        float minY = nextPlayerPosition.y;
        boundingBox.set(minX, minY, width, height);
    }

    private void loadDefaultSprite() {
        Texture texture = Utility.getTextureAsset(DEFAULT_SPRITE_PATH);
        TextureRegion[][] textureFrames = TextureRegion.split(texture, frameWidth, frameHeight);
        playerSprite = new Sprite(textureFrames[0][0].getTexture(), 0, 0, frameWidth, frameHeight);
        currentFrame = textureFrames[0][0];
    }

    private void loadAllAnimations() {
        loadWalkingAnimation();
    }

    private void loadWalkingAnimation() {
        Texture texture = Utility.getTextureAsset(DEFAULT_SPRITE_PATH);
        TextureRegion[][] textureFrames = TextureRegion.split(texture, frameWidth, frameHeight);

        walkNorthFrames = new Array<>(4);
        walkSouthFrames = new Array<>(4);
        walkWestFrames = new Array<>(4);
        walkEastFrames = new Array<>(4);

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

        walkSouthAnimation = new Animation<>(0.25f, walkSouthFrames, Animation.PlayMode.LOOP);
        walkWestAnimation = new Animation<>(0.25f, walkWestFrames, Animation.PlayMode.LOOP);
        walkEastAnimation = new Animation<>(0.25f, walkEastFrames, Animation.PlayMode.LOOP);
        walkNorthAnimation = new Animation<>(0.25f, walkNorthFrames, Animation.PlayMode.LOOP);
    }

    public void dispose() {
        Utility.unloadAsset(DEFAULT_SPRITE_PATH);
    }

    private void setFrame() {
        switch (currentDirection) {
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
                break;
        }
    }

    public void setCurrentPostion(float currentPositionX, float currentPositionY) {
        playerSprite.setX(currentPositionX);
        playerSprite.setY(currentPositionY);
        currentPlayerPostion.x = currentPositionX;
        currentPlayerPostion.y = currentPositionY;
    }

    public void setDirection(Direction direction) {
        previousDirection = currentDirection;
        currentDirection = direction;
        setFrame();
    }

    public void setNextPositionToCurrent() {
        setCurrentPostion(nextPlayerPosition.x, nextPlayerPosition.y);
    }

    public void calculateNextPosition(Direction direction, float deltaTime) {
        float testX = currentPlayerPostion.x;
        float testY = currentPlayerPostion.y;

        velocity.scl(deltaTime);

        switch (direction) {
            case NORTH:
                testY += velocity.y;
                break;
            case SOUTH:
                testY -= velocity.y;
                break;
            case WEST:
                testX -= velocity.x;
                break;
            case EAST:
                testX += velocity.x;
                break;
        }

        nextPlayerPosition.x = testX;
        nextPlayerPosition.y = testY;

        velocity.scl(1 / deltaTime);
    }

    public void alignToGrid() {
        float roundedX = Math.round(currentPlayerPostion.x / Constant.TILE_SIZE) * Constant.TILE_SIZE;
        float roundedY = Math.round(currentPlayerPostion.y / Constant.TILE_SIZE) * Constant.TILE_SIZE;
        setCurrentPostion(roundedX, roundedY);
    }
}
