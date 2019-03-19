package nl.t64.game.rpg;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;


public class Camera extends OrthographicCamera {

    private float mapWidth;
    private float mapHeight;

    public Camera() {
        Viewport viewport = new FitViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), this);
        viewport.update(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        zoom = 0.5f;
    }

    public void setPosition(Vector2 playerPosition) {
        focusCameraOn(playerPosition);
        setCameraOnMapEdge();
        update();
    }

    public void setNewMapSize(float mapWidth, float mapHeight) {
        this.mapWidth = mapWidth;
        this.mapHeight = mapHeight;
    }

    private void focusCameraOn(Vector2 playerPosition) {
        position.set(playerPosition, 0);
    }

    private void setCameraOnMapEdge() {
        float halfCameraWidth = getZoomedCameraWidth() / 2 - getHorizontalSpaceBetweenCameraAndMapEdge();
        float halfCameraHeight = getZoomedCameraHeight() / 2 - getVerticalSpaceBetweenCameraAndMapEdge();
        position.x = MathUtils.clamp(position.x, halfCameraWidth, mapWidth - halfCameraWidth);
        position.y = MathUtils.clamp(position.y, halfCameraHeight, mapHeight - halfCameraHeight);
    }

    private float getHorizontalSpaceBetweenCameraAndMapEdge() {
        float hSpace = 0;
        if (mapWidth < getZoomedCameraWidth()) {
            hSpace = (getZoomedCameraWidth() - mapWidth) / 2;
        }
        return hSpace;
    }

    private float getVerticalSpaceBetweenCameraAndMapEdge() {
        float vSpace = 0;
        if (mapHeight < getZoomedCameraHeight()) {
            vSpace = (getZoomedCameraHeight() - mapHeight) / 2;
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
