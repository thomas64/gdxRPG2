package nl.t64.game.rpg.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Rectangle;
import nl.t64.game.rpg.Logger;
import nl.t64.game.rpg.MapManager;
import nl.t64.game.rpg.PlayerController;
import nl.t64.game.rpg.constants.Constant;
import nl.t64.game.rpg.entities.Entity;
import nl.t64.game.rpg.tiled.Portal;
import nl.t64.game.rpg.tiled.SpawnPoint;

public class AdventureScreen implements Screen {

    private static final String TAG = AdventureScreen.class.getSimpleName();
    private static final Color TRANSPARENT = new Color(0, 0, 0, 0.5f);

    public static boolean showGrid = false;
    public static boolean showObjects = false;
    public static boolean showDebug = false;

    private float playTime = 0f;
    private MapManager mapManager;
    private Entity player;
    private PlayerController controller;
    private Sprite currentPlayerSprite;
    private OrthogonalTiledMapRenderer mapRenderer = null;
    private OrthographicCamera camera = null;

    public AdventureScreen() {
        mapManager = new MapManager();
    }

    @Override
    public void show() {
        camera = new OrthographicCamera();
        camera.setToOrtho(false);

        mapRenderer = new OrthogonalTiledMapRenderer(mapManager.getCurrentMap());
        mapRenderer.setView(camera);

        player = new Entity();
        player.init(mapManager.getPlayerSpawnLocation());

        currentPlayerSprite = player.getPlayerSprite();

        controller = new PlayerController(player);
        Gdx.input.setInputProcessor(controller);
    }

    @Override
    public void render(float delta) {
        playTime += delta;

        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camera.position.set(currentPlayerSprite.getX(), currentPlayerSprite.getY(), 0f);
        camera.update();

        player.update(delta);

        checkPortals(player.getBoundingBox());

        if (!isInCollisionWithBlocker(player.getBoundingBox())) {
            player.setNextPositionToCurrent();
        }

        controller.update(delta);

        renderMapLayers();
        renderGrid();
        renderObjects();
        renderDebugBox(delta);
    }

    private void renderMapLayers() {
        mapRenderer.setView(camera);
        mapRenderer.render();

        mapRenderer.getBatch().begin();
        renderMapLayer("under0Ground");
        renderMapLayer("under1Trees");
        renderPlayer();
        renderMapLayer("over2Trees");
        mapRenderer.getBatch().end();
    }

    private void renderMapLayer(String layerName) {
        mapRenderer.renderTileLayer((TiledMapTileLayer) mapManager.getCurrentMap().getLayers().get(layerName));
    }

    private void renderPlayer() {
        float playerX = currentPlayerSprite.getX();
        float playerY = currentPlayerSprite.getY();
        mapRenderer.getBatch().draw(player.getCurrentFrame(), playerX, playerY, Constant.TILE_SIZE, Constant.TILE_SIZE);
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

                player.init(mapManager.getPlayerSpawnLocation());
                mapRenderer.setMap(mapManager.getCurrentMap());
                Logger.portalActivated(TAG);
                return;
            }
        }
    }

    private void renderGrid() {
        if (showGrid) {
            ShapeRenderer shapeRenderer = new ShapeRenderer();
            shapeRenderer.setProjectionMatrix(camera.combined);
            shapeRenderer.setColor(Color.DARK_GRAY);
            shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
            int mapWidth = mapManager.getCurrentMap().getProperties().get("width", Integer.class);
            int mapHeight = mapManager.getCurrentMap().getProperties().get("height", Integer.class);
            int mapTileWidth = mapManager.getCurrentMap().getProperties().get("tilewidth", Integer.class);
            int mapTileHeight = mapManager.getCurrentMap().getProperties().get("tileheight", Integer.class);
            int mapPixelWidth = mapWidth * mapTileWidth;
            int mapPixelHeight = mapHeight * mapTileHeight;
            for (int x = 0; x < mapPixelWidth; x = x + Constant.TILE_SIZE) {
                shapeRenderer.line(x, 0, x, mapPixelHeight);
            }
            for (int y = 0; y < mapPixelHeight; y = y + Constant.TILE_SIZE) {
                shapeRenderer.line(0, y, mapPixelWidth, y);
            }
            shapeRenderer.end();
        }
    }

    private void renderObjects() {
        if (showObjects) {
            ShapeRenderer shapeRenderer = new ShapeRenderer();
            shapeRenderer.setProjectionMatrix(camera.combined);
            shapeRenderer.begin(ShapeRenderer.ShapeType.Line);

            Rectangle rect;

            shapeRenderer.setColor(Color.YELLOW);
            for (RectangleMapObject blocker : mapManager.getBlockers()) {
                rect = blocker.getRectangle();
                shapeRenderer.rect(rect.x, rect.y, rect.width, rect.height);
            }
            rect = player.getBoundingBox();
            shapeRenderer.rect(rect.x, rect.y, rect.width, rect.height);

            shapeRenderer.setColor(Color.BLUE);
            for (Portal portal : mapManager.getPortals()) {
                rect = portal.getRectangle();
                shapeRenderer.rect(rect.x, rect.y, rect.width, rect.height);
            }
            for (SpawnPoint spawnPoint : mapManager.getSpawnPoints()) {
                rect = spawnPoint.getRectangle();
                shapeRenderer.rect(rect.x, rect.y, rect.width, rect.height);
            }
            shapeRenderer.end();
        }
    }

    private void renderDebugBox(float delta) {
        if (showDebug) {
            Gdx.gl.glEnable(GL20.GL_BLEND);
            Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
            ShapeRenderer shapeRenderer = new ShapeRenderer();
            shapeRenderer.setProjectionMatrix(camera.projection);
            shapeRenderer.setColor(TRANSPARENT);
            shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
            int w = 300;
            int h = 600;
            int x = 0 - (Gdx.graphics.getWidth() / 2);
            int y = 0 - (Gdx.graphics.getHeight() / 2) + (Gdx.graphics.getHeight() - h);
            shapeRenderer.rect(x, y, w, h);
            shapeRenderer.end();
            Gdx.gl.glDisable(GL20.GL_BLEND);

            String debug1 = "" +
                    "FPS:\n" +
                    "delta:\n" +
                    "playTime:\n" +
                    "\n" +
                    "timeUp:\n" +
                    "timeDown:\n" +
                    "timeLeft:\n" +
                    "timeRight:\n" +
                    "timeDelay:\n" +
                    "\n" +
                    "currentPlayerPositionX:\n" +
                    "currentPlayerPositionY:\n" +
                    "\n" +
                    "currentPlayerPositionTiledX:\n" +
                    "currentPlayerPositionTiledY:\n" +
                    "\n" +
                    "nextPlayerPositionX:\n" +
                    "nextPlayerPositionY:\n" +
                    "\n" +
                    "nextPlayerPositionTiledX:\n" +
                    "nextPlayerPositionTiledY:\n" +
                    "\n" +
                    "currentDirection:\n" +
                    "state:\n" +
                    "frameTime:";

            String debug2 = "" +
                    Gdx.graphics.getFramesPerSecond() + "\n" +
                    String.valueOf(delta).substring(0, 5) + "\n" +
                    String.valueOf(playTime).substring(0, String.valueOf(playTime).length() - 4) + "\n" +
                    "\n" +
                    controller.getTimeUp() + "\n" +
                    controller.getTimeDown() + "\n" +
                    controller.getTimeLeft() + "\n" +
                    controller.getTimeRight() + "\n" +
                    controller.getTimeDelay() + "\n" +
                    "\n" +
                    player.getCurrentPlayerPostion().x + "\n" +
                    player.getCurrentPlayerPostion().y + "\n" +
                    "\n" +
                    (int) player.getCurrentPlayerPostion().x / Constant.TILE_SIZE + "\n" +
                    (int) player.getCurrentPlayerPostion().y / Constant.TILE_SIZE + "\n" +
                    "\n" +
                    player.getNextPlayerPosition().x + "\n" +
                    player.getNextPlayerPosition().y + "\n" +
                    "\n" +
                    (int) player.getNextPlayerPosition().x / Constant.TILE_SIZE + "\n" +
                    (int) player.getNextPlayerPosition().y / Constant.TILE_SIZE + "\n" +
                    "\n" +
                    player.getCurrentDirection() + "\n" +
                    player.getState() + "\n" +
                    player.getFrameTime();

            String[] lines1 = debug1.split("\n");
            String[] lines2 = debug2.split("\n");
            SpriteBatch batch = new SpriteBatch();
            BitmapFont font = new BitmapFont();
            font.setColor(Color.WHITE);
            batch.begin();
            int lineHeight = 15;
            int xFirstColumn = 0;
            int xSecondColumn = 200;
            for (int i = 0; i < lines1.length; i++) {
                font.draw(batch, lines1[i], xFirstColumn, Gdx.graphics.getHeight() - (i * lineHeight));
                font.draw(batch, lines2[i], xSecondColumn, Gdx.graphics.getHeight() - (i * lineHeight));
            }
            batch.end();
        }
    }

}
