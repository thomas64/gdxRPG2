package nl.t64.game.rpg.entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import nl.t64.game.rpg.Logger;
import nl.t64.game.rpg.MapManager;
import nl.t64.game.rpg.Utility;
import nl.t64.game.rpg.constants.Direction;
import nl.t64.game.rpg.constants.EntityState;

import java.util.UUID;

public class Entity {

    private static final String TAG = Entity.class.getSimpleName();
    private static final String DEFAULT_SPRITE_PATH = "sprites/characters/hero1.png";

    public static Rectangle boundingBox;

    private final int frameWidth = 48;
    private final int frameHeight = 48;
    private Vector2 nextPlayerPosition;
    private Vector2 currentPlayerPostion;
    private EntityState state = EntityState.IDLE;
    private float frameTime = 0f;
    private Sprite frameSprite = null;
    private TextureRegion currentFrame = null;
    private Vector2 velocity;
    private String entityId;
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
        boundingBox = new Rectangle();
        this.nextPlayerPosition = new Vector2();
        this.currentPlayerPostion = new Vector2();
        this.velocity = new Vector2(2f, 2f);

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

        setBoundingBoxSizeAccountingForUnitScale(width, height);
    }

    private void setBoundingBoxSizeAccountingForUnitScale(float width, float height) {
        float minX;
        float minY;
        if (MapManager.UNIT_SCALE > 0) {
            minX = nextPlayerPosition.x / MapManager.UNIT_SCALE;
            minY = nextPlayerPosition.y / MapManager.UNIT_SCALE;
        } else {
            minX = nextPlayerPosition.x;
            minY = nextPlayerPosition.y;
        }
        boundingBox.set(minX, minY, width, height);
    }

    private void loadDefaultSprite() {
        Texture texture = Utility.getTextureAsset(DEFAULT_SPRITE_PATH);
        TextureRegion[][] textureFrames = TextureRegion.split(texture, frameWidth, frameHeight);
        frameSprite = new Sprite(textureFrames[0][0].getTexture(), 0, 0, frameWidth, frameHeight);
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

    public void setState(EntityState state) {
        this.state = state;
    }

    public Sprite getFrameSprite() {
        return frameSprite;
    }

    public TextureRegion getFrame() {
        return currentFrame;
    }

    private void setFrame(float deltaTime) {
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
        frameSprite.setX(currentPositionX);
        frameSprite.setY(currentPositionY);
        currentPlayerPostion.x = currentPositionX;
        currentPlayerPostion.y = currentPositionY;
    }

    public void setDirection(Direction direction, float deltaTime) {
        previousDirection = currentDirection;
        currentDirection = direction;
        setFrame(deltaTime);
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

}
