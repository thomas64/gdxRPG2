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
import nl.t64.game.rpg.Utility;
import nl.t64.game.rpg.constants.Constant;
import nl.t64.game.rpg.constants.Direction;
import nl.t64.game.rpg.constants.EntityState;
import nl.t64.game.rpg.screens.WorldScreen;

import java.util.UUID;


public class Entity {

    private static final String TAG = Entity.class.getSimpleName();
    private static final String DEFAULT_SPRITE_PATH = "sprites/characters/hero1.png";

    private final int frameWidth = Constant.TILE_SIZE;
    private final int frameHeight = Constant.TILE_SIZE;

    @Getter
    private Rectangle boundingBox;

    private Vector2 oldPosition;
    @Getter
    private Vector2 currentPosition;
    @Getter
    @Setter
    private Direction direction = null;

    @Getter
    private Sprite playerSprite = null;
    @Getter
    private TextureRegion currentFrame = null;

    private Vector2 velocity;

    private String entityId;

    @Getter
    @Setter
    private EntityState state = EntityState.IDLE;

    private Animation<TextureRegion> walkNorthAnimation;
    private Animation<TextureRegion> walkSouthAnimation;
    private Animation<TextureRegion> walkWestAnimation;
    private Animation<TextureRegion> walkEastAnimation;

    public Entity() {
        this.entityId = UUID.randomUUID().toString();
        this.boundingBox = new Rectangle();
        this.oldPosition = new Vector2();
        this.currentPosition = new Vector2();
        this.velocity = new Vector2(192f, 192f);  // 48 * 4

        Utility.loadTextureAsset(DEFAULT_SPRITE_PATH);
        loadDefaultSprite();
        loadAllAnimations();
    }

    public void update() {
        setBoundingBox(0.75f, 0.75f);
    }

    public void setBoundingBox(float percentageWidth, float percentageHeight) {
        float width = frameWidth * percentageWidth;
        float height = frameHeight * percentageHeight;
        float widthReduction = 1.00f - percentageWidth;
        float x = currentPosition.x + (frameWidth * (widthReduction / 2));
        float y = currentPosition.y;
        boundingBox.set(x, y, width, height);
    }

    public void init(Vector2 spawnPosition, Direction spawnDirection) {
        oldPosition.x = spawnPosition.x;
        oldPosition.y = spawnPosition.y;
        currentPosition.x = spawnPosition.x;
        currentPosition.y = spawnPosition.y;
        playerSprite.setX(spawnPosition.x);
        playerSprite.setY(spawnPosition.y);
        direction = spawnDirection;
    }

    public Vector2 getCenter() {
        float x = playerSprite.getX() + playerSprite.getWidth() / 2;
        float y = playerSprite.getY() + playerSprite.getHeight() / 2;
        return new Vector2(x, y);
    }

    public void setFrame() {
        if (state == EntityState.IDLE) {
            setStandingStillFrame();
        } else if (state == EntityState.WALKING) {
            setWalkingFrame();
        }
    }

    public void setStandingStillFrame() {
        Texture texture = Utility.getTextureAsset(DEFAULT_SPRITE_PATH);
        TextureRegion[][] textureFrames = TextureRegion.split(texture, frameWidth, frameHeight);

        switch (direction) {
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
        }
    }

    private void setWalkingFrame() {
        switch (direction) {
            case NORTH:
                currentFrame = walkNorthAnimation.getKeyFrame(WorldScreen.playTime);
                break;
            case SOUTH:
                currentFrame = walkSouthAnimation.getKeyFrame(WorldScreen.playTime);
                break;
            case WEST:
                currentFrame = walkWestAnimation.getKeyFrame(WorldScreen.playTime);
                break;
            case EAST:
                currentFrame = walkEastAnimation.getKeyFrame(WorldScreen.playTime);
                break;
        }
    }

    public void move(float dt) {
        oldPosition = currentPosition.cpy();

        switch (direction) {
            case NORTH:
                currentPosition.y += velocity.y * dt;
                break;
            case SOUTH:
                currentPosition.y -= velocity.y * dt;
                break;
            case WEST:
                currentPosition.x -= velocity.x * dt;
                break;
            case EAST:
                currentPosition.x += velocity.x * dt;
                break;
        }
        playerSprite.setX(currentPosition.x);
        playerSprite.setY(currentPosition.y);
    }

    public void moveBack() {
        switch (direction) {
            case NORTH:
                currentPosition.y -= 1;
                break;
            case SOUTH:
                currentPosition.y += 1;
                break;
            case WEST:
                currentPosition.x += 1;
                break;
            case EAST:
                currentPosition.x -= 1;
                break;
        }
        float roundedX = Math.round(currentPosition.x);
        float roundedY = Math.round(currentPosition.y);
        setCurrentPostion(roundedX, roundedY);
    }

    public void alignToGrid() {
        float roundedX = Math.round(currentPosition.x / Constant.TILE_SIZE) * Constant.TILE_SIZE;
        float roundedY = Math.round(currentPosition.y / Constant.TILE_SIZE) * Constant.TILE_SIZE;
        setCurrentPostion(roundedX, roundedY);
    }

    public void dispose() {
        Utility.unloadAsset(DEFAULT_SPRITE_PATH);
    }

    private void setCurrentPostion(float currentPositionX, float currentPositionY) {
        playerSprite.setX(currentPositionX);
        playerSprite.setY(currentPositionY);
        currentPosition.x = currentPositionX;
        currentPosition.y = currentPositionY;
    }

    private void loadDefaultSprite() {
        Texture texture = Utility.getTextureAsset(DEFAULT_SPRITE_PATH);
        TextureRegion[][] textureFrames = TextureRegion.split(texture, frameWidth, frameHeight);
        playerSprite = new Sprite(textureFrames[0][0].getTexture(), 0, 0, frameWidth, frameHeight);
    }

    private void loadAllAnimations() {
        loadWalkingAnimation();
    }

    private void loadWalkingAnimation() {
        Texture texture = Utility.getTextureAsset(DEFAULT_SPRITE_PATH);
        TextureRegion[][] textureFrames = TextureRegion.split(texture, frameWidth, frameHeight);

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

        walkSouthAnimation = new Animation<>(0.25f, walkSouthFrames, Animation.PlayMode.LOOP);
        walkWestAnimation = new Animation<>(0.25f, walkWestFrames, Animation.PlayMode.LOOP);
        walkEastAnimation = new Animation<>(0.25f, walkEastFrames, Animation.PlayMode.LOOP);
        walkNorthAnimation = new Animation<>(0.25f, walkNorthFrames, Animation.PlayMode.LOOP);
    }

}
