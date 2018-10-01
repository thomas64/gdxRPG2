package nl.t64.game.rpg.components;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import nl.t64.game.rpg.Utility;
import nl.t64.game.rpg.constants.Constant;
import nl.t64.game.rpg.constants.EntityState;
import nl.t64.game.rpg.entities.Entity;
import nl.t64.game.rpg.screens.WorldScreen;


public class GraphicsComponent {

    private static final String TAG = GraphicsComponent.class.getSimpleName();
    private static final String DEFAULT_SPRITE_PATH = "sprites/characters/hero1.png";
    private static final float FRAME_DURATION = 0.25f;

    private Animation<TextureRegion> walkNorthAnimation;
    private Animation<TextureRegion> walkSouthAnimation;
    private Animation<TextureRegion> walkWestAnimation;
    private Animation<TextureRegion> walkEastAnimation;

    private Sprite sprite = null;
    private TextureRegion currentFrame = null;

    public GraphicsComponent() {
        Utility.loadTextureAsset(DEFAULT_SPRITE_PATH);
        loadDefaultSprite();
        loadAllAnimations();
    }

    public void update(Entity entity) {
        setFrame(entity);
    }

    public void render(Batch batch, Entity entity) {
        float entityX = entity.getPhysicsComponent().getCurrentPosition().x;
        float entityY = entity.getPhysicsComponent().getCurrentPosition().y;
        batch.draw(currentFrame, entityX, entityY, Constant.TILE_SIZE, Constant.TILE_SIZE);
    }

    private void setFrame(Entity entity) {
        if (entity.getState() == EntityState.IDLE) {
            setStandingStillFrame(entity);
        } else if (entity.getState() == EntityState.WALKING) {
            setWalkingFrame(entity);
        }
    }

    private void setStandingStillFrame(Entity entity) {
        switch (entity.getPhysicsComponent().getDirection()) {
            case NORTH:
                currentFrame = walkNorthAnimation.getKeyFrame(0f);
                break;
            case SOUTH:
                currentFrame = walkSouthAnimation.getKeyFrame(0f);
                break;
            case WEST:
                currentFrame = walkWestAnimation.getKeyFrame(0f);
                break;
            case EAST:
                currentFrame = walkEastAnimation.getKeyFrame(0f);
                break;
        }
    }

    private void setWalkingFrame(Entity entity) {
        switch (entity.getPhysicsComponent().getDirection()) {
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

    public Vector2 getCenter() {
        float x = sprite.getX() + sprite.getWidth() / 2;
        float y = sprite.getY() + sprite.getHeight() / 2;
        return new Vector2(x, y);
    }

    public void dispose() {
        Utility.unloadAsset(DEFAULT_SPRITE_PATH);
    }

    private void loadDefaultSprite() {
        Texture texture = Utility.getTextureAsset(DEFAULT_SPRITE_PATH);
        TextureRegion[][] textureFrames = TextureRegion.split(texture, Constant.TILE_SIZE, Constant.TILE_SIZE);
        sprite = new Sprite(textureFrames[0][0].getTexture(), 0, 0, Constant.TILE_SIZE, Constant.TILE_SIZE);
    }

    private void loadAllAnimations() {
        loadWalkingAnimation();
    }

    private void loadWalkingAnimation() {
        Texture texture = Utility.getTextureAsset(DEFAULT_SPRITE_PATH);
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
