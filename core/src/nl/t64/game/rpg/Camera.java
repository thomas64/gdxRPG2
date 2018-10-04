package nl.t64.game.rpg;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import nl.t64.game.rpg.constants.Constant;


public class Camera extends OrthographicCamera {

    private float mapWidth;
    private float mapHeight;

    public Camera() {
        Viewport viewport = new FitViewport(GdxRpg2.WIDTH, GdxRpg2.HEIGHT, this);
        viewport.update(GdxRpg2.WIDTH, GdxRpg2.HEIGHT);

        // zoom();
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

    private void zoom() {
        setToOrtho(false, 16 * Constant.TILE_SIZE, 9 * Constant.TILE_SIZE);
    }

    private void focusCameraOn(Vector2 playerPosition) {
        position.set(playerPosition, 0);
    }

    private void setCameraOnMapEdge() {
        float halfCameraWidth = viewportWidth / 2 - getHorizontalSpaceBetweenCameraAndMapEdge();
        float halfCameraHeight = viewportHeight / 2 - getVerticalSpaceBetweenCameraAndMapEdge();
        position.x = MathUtils.clamp(position.x, halfCameraWidth, mapWidth - halfCameraWidth);
        position.y = MathUtils.clamp(position.y, halfCameraHeight, mapHeight - halfCameraHeight);
    }

    private float getHorizontalSpaceBetweenCameraAndMapEdge() {
        float hSpace = 0;
        if (mapWidth < viewportWidth) {
            hSpace = (viewportWidth - mapWidth) / 2;
        }
        return hSpace;
    }

    private float getVerticalSpaceBetweenCameraAndMapEdge() {
        float vSpace = 0;
        if (mapHeight < viewportHeight) {
            vSpace = (viewportHeight - mapHeight) / 2;
        }
        return vSpace;
    }

}
