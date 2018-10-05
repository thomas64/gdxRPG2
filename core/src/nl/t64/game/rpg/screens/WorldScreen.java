package nl.t64.game.rpg.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import nl.t64.game.rpg.Camera;
import nl.t64.game.rpg.MapManager;
import nl.t64.game.rpg.Utility;
import nl.t64.game.rpg.components.*;
import nl.t64.game.rpg.constants.Constant;
import nl.t64.game.rpg.constants.MapLayerName;
import nl.t64.game.rpg.entities.Entity;
import nl.t64.game.rpg.events.LoadSpriteEvent;
import nl.t64.game.rpg.events.StartDirectionEvent;
import nl.t64.game.rpg.events.StartPositionEvent;
import nl.t64.game.rpg.events.StartStateEvent;
import nl.t64.game.rpg.tiled.Npc;
import nl.t64.game.rpg.tiled.Portal;
import nl.t64.game.rpg.tiled.SpawnPoint;


public class WorldScreen implements Screen {

    private static final String TAG = WorldScreen.class.getSimpleName();
    private static final Color TRANSPARENT = new Color(0, 0, 0, 0.5f);

    public static boolean showGrid = false;
    public static boolean showObjects = false;
    public static boolean showDebug = false;

    public static float playTime = 0f;

    private MapManager mapManager;
    private Entity player;
    private Array<Entity> npcEntities;
    private Camera camera;
    private OrthogonalTiledMapRenderer mapRenderer;

    public WorldScreen() {
        mapManager = new MapManager();
    }

    @Override
    public void show() {
        player = new Entity(new PlayerInputComponent(), new PlayerPhysicsComponent(), new PlayerGraphicsComponent());
        camera = new Camera();
        mapRenderer = new OrthogonalTiledMapRenderer(mapManager.getCurrentMap().getTiledMap());
    }

    @Override
    public void render(float dt) {
        playTime += dt;

        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        updateMap();
        updateEntities(dt);
        updateCameraPosition();
        renderMapLayers();
        renderGrid();
        renderObjects();
        renderDebugBox(dt);
    }

    private void updateMap() {
        if (mapManager.isMapChanged()) {
            mapRenderer.setMap(mapManager.getCurrentMap().getTiledMap());
            camera.setNewMapSize(mapManager.getCurrentMap().getPixelWidth(),
                                 mapManager.getCurrentMap().getPixelHeight());
            loadNpcEntities();
            player.send(new StartPositionEvent(mapManager.getCurrentMap().getPlayerSpawnLocation()));
            player.send(new StartDirectionEvent(mapManager.getCurrentMap().getPlayerSpawnDirection()));
            mapManager.setMapChanged(false);
        }
    }

    private void loadNpcEntities() {
        npcEntities = new Array<>();
        for (Npc npc : mapManager.getCurrentMap().getNpcs()) {
            Entity entity = new Entity(new NpcInputComponent(), new NpcPhysicsComponent(), new NpcGraphicsComponent());
            npcEntities.add(entity);
            entity.send(new StartStateEvent(npc.getState()));
            entity.send(new StartDirectionEvent(npc.getDirection()));
            entity.send(new StartPositionEvent(npc.getPosition()));
            LoadSpriteEvent loadSpriteEvent = Utility.getAllPeopleSpriteConfigsFromJson().get(npc.getName());
            entity.send(loadSpriteEvent);
        }
    }

    private void updateEntities(float dt) {
        player.update(mapManager, npcEntities, dt);
        Array<Entity> copyEntities = new Array<>(npcEntities);
        copyEntities.add(player);
        for (Entity entity : npcEntities) {
            entity.update(mapManager, copyEntities, dt);
        }
    }

    private void updateCameraPosition() {
        camera.setPosition(player.getPosition());
        mapRenderer.setView(camera);
    }

    private void renderMapLayers() {
        mapRenderer.getBatch().begin();
        renderMapLayer(MapLayerName.UNDER0_NONE.name().toLowerCase());
        renderMapLayer(MapLayerName.UNDER1_GROUND.name().toLowerCase());
        renderMapLayer(MapLayerName.UNDER2_NONE.name().toLowerCase());
        renderMapLayer(MapLayerName.UNDER3_NONE.name().toLowerCase());
        renderMapLayer(MapLayerName.UNDER4_OBJECTS.name().toLowerCase());
        renderMapLayer(MapLayerName.UNDER5_NONE.name().toLowerCase());
        renderEntities();
        renderMapLayer(MapLayerName.OVER6_OBJECTS.name().toLowerCase());
        renderMapLayer(MapLayerName.OVER7_NONE.name().toLowerCase());
        renderMapLayer(MapLayerName.OVER8_NONE.name().toLowerCase());
        mapRenderer.getBatch().end();
    }

    private void renderMapLayer(String layerName) {
        MapLayer mapLayer = mapManager.getCurrentMap().getTiledMap().getLayers().get(layerName);
        mapRenderer.renderTileLayer((TiledMapTileLayer) mapLayer);
    }

    private void renderEntities() {
        for (Entity entity : npcEntities) {
            entity.render(mapRenderer.getBatch());
        }
        player.render(mapRenderer.getBatch());
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
        mapRenderer.dispose();
    }

    private void renderGrid() {
        if (showGrid) {
            ShapeRenderer shapeRenderer = new ShapeRenderer();
            shapeRenderer.setProjectionMatrix(camera.combined);
            shapeRenderer.setColor(Color.DARK_GRAY);
            shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
            float mapPixelWidth = mapManager.getCurrentMap().getPixelWidth();
            float mapPixelHeight = mapManager.getCurrentMap().getPixelHeight();
            for (int x = 0; x < mapPixelWidth; x = x + Constant.TILE_SIZE) {
                shapeRenderer.line(x, 0, x, mapPixelHeight);
            }
            for (int y = 0; y < mapPixelHeight; y = y + Constant.TILE_SIZE) {
                shapeRenderer.line(0, y, mapPixelWidth, y);
            }
            shapeRenderer.end();
            shapeRenderer.dispose();
        }
    }

    private void renderObjects() {
        if (showObjects) {
            ShapeRenderer shapeRenderer = new ShapeRenderer();
            shapeRenderer.setProjectionMatrix(camera.combined);
            shapeRenderer.begin(ShapeRenderer.ShapeType.Line);

            Rectangle rect;

            shapeRenderer.setColor(Color.YELLOW);
            for (RectangleMapObject blocker : mapManager.getCurrentMap().getBlockers()) {
                rect = blocker.getRectangle();
                shapeRenderer.rect(rect.x, rect.y, rect.width, rect.height);
            }
            rect = player.getBoundingBox();
            shapeRenderer.rect(rect.x, rect.y, rect.width, rect.height);
            for (Entity entity : npcEntities) {
                rect = entity.getBoundingBox();
                shapeRenderer.rect(rect.x, rect.y, rect.width, rect.height);
            }

//            shapeRenderer.setColor(Color.GREEN);
//            for (Entity entity : npcEntities){
//                rect = entity.getPhysicsComponent().getWanderBox();
//                shapeRenderer.rect(rect.x, rect.y, rect.width, rect.height);
//            }

            shapeRenderer.setColor(Color.BLUE);
            for (Portal portal : mapManager.getCurrentMap().getPortals()) {
                rect = portal.getRectangle();
                shapeRenderer.rect(rect.x, rect.y, rect.width, rect.height);
            }
            for (SpawnPoint spawnPoint : mapManager.getCurrentMap().getSpawnPoints()) {
                rect = spawnPoint.getRectangle();
                shapeRenderer.rect(rect.x, rect.y, rect.width, rect.height);
            }
            shapeRenderer.end();
            shapeRenderer.dispose();
        }
    }

    private void renderDebugBox(float dt) {
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
                    "dt:\n" +
                    "playTime:\n" +
                    "\n" +
//                    "timeUp:\n" +
//                    "timeDown:\n" +
//                    "timeLeft:\n" +
//                    "timeRight:\n" +
//                    "timeDelay:\n" +
//                    "\n" +
                    "currentPositionX:\n" +
                    "currentPositionY:\n" +
                    "\n" +
                    "currentPositionTiledX:\n" +
                    "currentPositionTiledY:\n" +
                    "\n";
//                    "direction:\n" +
//                    "state:\n";

            String debug2 = "" +
                    Gdx.graphics.getFramesPerSecond() + "\n" +
                    String.valueOf(dt).substring(0, 5) + "\n" +
                    String.valueOf(playTime).substring(0, String.valueOf(playTime).length() - 4) + "\n" +
                    "\n" +
//                    player.getInputComponent().getTimeUp() + "\n" +
//                    player.getInputComponent().getTimeDown() + "\n" +
//                    player.getInputComponent().getTimeLeft() + "\n" +
//                    player.getInputComponent().getTimeRight() + "\n" +
//                    player.getInputComponent().getTimeDelay() + "\n" +
//                    "\n" +
                    player.getPosition().x + "\n" +
                    player.getPosition().y + "\n" +
                    "\n" +
                    (int) player.getPosition().x / Constant.TILE_SIZE + "\n" +
                    (int) player.getPosition().y / Constant.TILE_SIZE + "\n" +
                    "\n";
//                    player.getPhysicsComponent().getDirection() + "\n" +
//                    player.getState() + "\n";

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
            shapeRenderer.dispose();
            batch.dispose();
            font.dispose();
        }
    }

}
