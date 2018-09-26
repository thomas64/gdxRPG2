package nl.t64.game.rpg.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Rectangle;
import nl.t64.game.rpg.Logger;
import nl.t64.game.rpg.MapManager;
import nl.t64.game.rpg.PlayerController;
import nl.t64.game.rpg.entities.Entity;
import nl.t64.game.rpg.tiled.Portal;

public class AdventureScreen implements Screen {

    private static final String TAG = AdventureScreen.class.getSimpleName();
    private static MapManager mapManager;
    private static Entity player;
    private PlayerController controller;
    private TextureRegion currentPlayerFrame;
    private Sprite currentPlayerSprite;
    private OrthogonalTiledMapRenderer mapRenderer = null;
    private OrthographicCamera camera = null;

    public AdventureScreen() {
        mapManager = new MapManager();
    }

    @Override
    public void show() {
        setupViewport(15f, 15f);

        camera = new OrthographicCamera();
        camera.setToOrtho(false, VIEWPORT.viewportWidth, VIEWPORT.viewportHeight);

        mapRenderer = new OrthogonalTiledMapRenderer(mapManager.getCurrentMap(), MapManager.UNIT_SCALE);
        mapRenderer.setView(camera);

        Logger.unitScaleValue(TAG, mapRenderer.getUnitScale());

        player = new Entity();
        player.init(mapManager.getPlayerSpawnLocationUnitScaled());

        currentPlayerSprite = player.getFrameSprite();

        controller = new PlayerController(player);
        Gdx.input.setInputProcessor(controller);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camera.position.set(currentPlayerSprite.getX(), currentPlayerSprite.getY(), 0f);
        camera.update();

        player.update(delta);
        currentPlayerFrame = player.getFrame();

        checkPortals(player.boundingBox);
        if (!isInCollisionWithBlocker(player.boundingBox)) {
            player.setNextPositionToCurrent();
        }
        controller.update(delta);

        renderMapLayers();
    }

    private void renderMapLayers() {
        mapRenderer.setView(camera);
        mapRenderer.render();

        mapRenderer.getBatch().begin();
        renderMapLayer("under0Ground");
        renderMapLayer("under1Trees");
        float x = currentPlayerSprite.getX();
        float y = currentPlayerSprite.getY();
        mapRenderer.getBatch().draw(currentPlayerFrame, x, y, 1, 1);
        renderMapLayer("over2Trees");
        mapRenderer.getBatch().end();
    }

    private void renderMapLayer(String layerName) {
        mapRenderer.renderTileLayer((TiledMapTileLayer) mapManager.getCurrentMap().getLayers().get(layerName));
    }

    @Override
    public void resize(int width, int height) {
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void hide() {
    }

    @Override
    public void dispose() {
        player.dispose();
        controller.dispose();
        Gdx.input.setInputProcessor(null);
        mapRenderer.dispose();
    }

    private void setupViewport(float width, float height) {
        // Make the viewport a percentage of the total display area
        VIEWPORT.virtualWidth = width;
        VIEWPORT.virtualHeight = height;
        // Pixel dimensions of display
        VIEWPORT.physicalWidth = Gdx.graphics.getWidth();
        VIEWPORT.physicalHeight = Gdx.graphics.getHeight();
        // Aspect ratio for current viewport
        VIEWPORT.aspectRatio = width / height;

        // Update viewport if there could be skewing
        if (VIEWPORT.physicalWidth / VIEWPORT.physicalHeight >= VIEWPORT.aspectRatio) {
            // Letterbox left and right
            VIEWPORT.viewportWidth = height * (VIEWPORT.physicalWidth / VIEWPORT.physicalHeight);
            VIEWPORT.viewportHeight = height;
        } else {
            // Letterbox above and below
            VIEWPORT.viewportWidth = width;
            VIEWPORT.viewportHeight = width * (VIEWPORT.physicalHeight / VIEWPORT.physicalWidth);
        }

        Logger.worldRenderer(TAG,
                VIEWPORT.virtualWidth, VIEWPORT.virtualHeight,
                VIEWPORT.viewportWidth, VIEWPORT.viewportHeight,
                VIEWPORT.physicalWidth, VIEWPORT.physicalHeight);
    }

    private boolean isInCollisionWithBlocker(Rectangle playerRect) {
        for (RectangleMapObject blocker : mapManager.getBlockers()) {
            if (playerRect.overlaps(blocker.getRectangle())) {
                return true;
            }
        }
        return false;
    }

    private void checkPortals(Rectangle playerRect) {
        for (Portal portal : mapManager.getPortals()) {
            if (playerRect.overlaps(portal.getRectangle())) {

                mapManager.loadMap(portal.getToMapName());
                mapManager.setPlayerSpawnLocation(portal);

                player.init(mapManager.getPlayerSpawnLocationUnitScaled());
                mapRenderer.setMap(mapManager.getCurrentMap());
                Logger.portalActivated(TAG);
                return;
            }
        }
    }

    private static class VIEWPORT {
        static float viewportWidth;
        static float viewportHeight;
        static float virtualWidth;
        static float virtualHeight;
        static float physicalWidth;
        static float physicalHeight;
        static float aspectRatio;
    }

}
