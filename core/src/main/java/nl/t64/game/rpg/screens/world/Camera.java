package nl.t64.game.rpg.screens.world;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;


class Camera extends OrthographicCamera {

    private float mapWidth;
    private float mapHeight;

    Camera() {
        Viewport viewport = new ScreenViewport(this);
        viewport.update(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        super.zoom = 0.5f;
    }

    void setPosition(Vector2 playerPosition) {
        focusCameraOn(playerPosition);
        setCameraOnMapEdge();
        update();
    }

    void setNewMapSize(float mapWidth, float mapHeight) {
        this.mapWidth = mapWidth;
        this.mapHeight = mapHeight;
    }

    private void focusCameraOn(Vector2 playerPosition) {
        position.set(playerPosition, 0f);
    }

    private void setCameraOnMapEdge() {
        float halfCameraWidth = getZoomedCameraWidth() / 2 - getHorizontalSpaceBetweenCameraAndMapEdge();
        float halfCameraHeight = getZoomedCameraHeight() / 2 - getVerticalSpaceBetweenCameraAndMapEdge();
        position.x = MathUtils.clamp(position.x, halfCameraWidth, mapWidth - halfCameraWidth);
        position.y = MathUtils.clamp(position.y, halfCameraHeight, mapHeight - halfCameraHeight);
    }

    private float getHorizontalSpaceBetweenCameraAndMapEdge() {
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
