package nl.t64.game.rpg.screens.world;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import lombok.Getter;
import nl.t64.game.rpg.sfx.ShakeCamera;


public class Camera extends OrthographicCamera {

    private final ShakeCamera shakeCam;
    @Getter
    private final Viewport viewport;
    private float mapWidth;
    private float mapHeight;

    public Camera() {
        this.shakeCam = new ShakeCamera();
        this.viewport = new ScreenViewport(this);
        this.viewport.update(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        this.reset();
    }

    boolean zoom() {
        if (mapWidth / zoom > Gdx.graphics.getWidth() && mapHeight / zoom > Gdx.graphics.getHeight()) {
            float zoomNumberWidth = mapWidth / Gdx.graphics.getWidth();
            float zoomNumberHeight = mapHeight / Gdx.graphics.getHeight();
            zoom = Math.max(zoomNumberWidth, zoomNumberHeight);
            return true;
        } else {
            return false;
        }
    }

    void reset() {
        zoom = 0.5f;
    }

    void startShaking() {
        shakeCam.startShaking();
    }

    public void setPosition(float x, float y) {
        setPosition(new Vector2(x, y));
    }

    void setPosition(Vector2 playerPosition) {
        playerPosition = getPositionOnMapEdges(playerPosition);
        if (shakeCam.isShaking()) {
            playerPosition = shakeCam.getNewShakePosition().add(playerPosition);
        }
        focusCameraOn(playerPosition);
        update();
    }

    public void setNewMapSize(float mapWidth, float mapHeight) {
        this.mapWidth = mapWidth;
        this.mapHeight = mapHeight;
    }

    private void focusCameraOn(Vector2 newPosition) {
        position.set(newPosition, 0f);
    }

    private Vector2 getPositionOnMapEdges(Vector2 playerPosition) {
        float halfCameraWidth = getZoomedCameraWidth() / 2 - getHorizontalSpaceBetweenCameraAndMapEdge();
        float halfCameraHeight = getZoomedCameraHeight() / 2 - getVerticalSpaceBetweenCameraAndMapEdge();
        return new Vector2(MathUtils.clamp(playerPosition.x, halfCameraWidth, mapWidth - halfCameraWidth),
                           MathUtils.clamp(playerPosition.y, halfCameraHeight, mapHeight - halfCameraHeight));
    }

    float getHorizontalSpaceBetweenCameraAndMapEdge() {
        float hSpace = 0f;
        if (mapWidth < getZoomedCameraWidth()) {
            hSpace = (getZoomedCameraWidth() - mapWidth) / 2f;
        }
        return hSpace;
    }

    private float getVerticalSpaceBetweenCameraAndMapEdge() {
        float vSpace = 0f;
        if (mapHeight < getZoomedCameraHeight()) {
            vSpace = (getZoomedCameraHeight() - mapHeight) / 2f;
        }
        return vSpace;
    }

    private float getZoomedCameraHeight() {
        return viewportHeight * zoom;
    }

    private float getZoomedCameraWidth() {
        return viewportWidth * zoom;
    }

}
