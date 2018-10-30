package nl.t64.game.rpg.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.utils.ObjectMap;
import lombok.Getter;
import nl.t64.game.rpg.Camera;
import nl.t64.game.rpg.MapManager;
import nl.t64.game.rpg.Utility;
import nl.t64.game.rpg.components.character.Character;
import nl.t64.game.rpg.components.character.*;
import nl.t64.game.rpg.constants.CharacterState;
import nl.t64.game.rpg.constants.Constant;
import nl.t64.game.rpg.constants.GameState;
import nl.t64.game.rpg.events.character.LoadSpriteEvent;
import nl.t64.game.rpg.events.character.StartDirectionEvent;
import nl.t64.game.rpg.events.character.StartPositionEvent;
import nl.t64.game.rpg.events.character.StartStateEvent;
import nl.t64.game.rpg.profile.ProfileManager;
import nl.t64.game.rpg.tiled.GameMap;
import nl.t64.game.rpg.tiled.Npc;

import java.util.ArrayList;
import java.util.List;


public class WorldScreen implements Screen {

    private static final String TAG = WorldScreen.class.getSimpleName();
    private static final Color TRANSPARENT = new Color(0, 0, 0, 0.5f);
    private static final String PEOPLE1_SPRITE_CONFIG = "configs/sprites_people1.json";

    private static boolean showGrid = false;
    private static boolean showObjects = false;
    private static boolean showDebug = false;
    @Getter
    private static float playTime = 0f;
    @Getter
    private static GameState gameState;

    private Camera camera;
    private GameMap currentMap;
    private OrthogonalTiledMapRenderer mapRenderer;
    private Character player;
    private List<Character> npcCharacters;
    private ShapeRenderer shapeRenderer;

    public WorldScreen() {
        ProfileManager.getInstance().addObserver(MapManager.getInstance());
        this.camera = new Camera();
        MapManager.getInstance().setCamera(this.camera);
        this.mapRenderer = new OrthogonalTiledMapRenderer(null);
        MapManager.getInstance().setMapChanged(true);
        this.player = new Character(new PlayerInput(), new PlayerPhysics(), new PlayerGraphics());
        this.shapeRenderer = new ShapeRenderer();
    }

    @Override
    public void show() {
        setGameState(GameState.RUNNING);
    }

    @Override
    public void render(float dt) {
        updatePlayTime(dt);

        if (gameState == GameState.PAUSED) {
            player.updateInput(dt);
            return;
        }

        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        updateMap();
        updateCharacters(dt);
        updateCameraPosition();
        renderMapLayers();
        renderGrid();
        renderObjects();
        renderDebugBox(dt);
    }

    private void updateMap() {
        if (MapManager.getInstance().isMapChanged()) {
            currentMap = MapManager.getInstance().getCurrentMap();
            mapRenderer.setMap(currentMap.getTiledMap());
            camera.setNewMapSize(currentMap.getPixelWidth(), currentMap.getPixelHeight());
            loadNpcCharacters();
            player.send(new StartPositionEvent(currentMap.getPlayerSpawnLocation()));
            player.send(new StartDirectionEvent(currentMap.getPlayerSpawnDirection()));
            MapManager.getInstance().setMapChanged(false);
        }
    }

    private void loadNpcCharacters() {
        npcCharacters = new ArrayList<>();
        for (Npc npc : currentMap.getNpcs()) {
            Character npcCharacter = new Character(new NpcInput(), new NpcPhysics(), new NpcGraphics());
            npcCharacters.add(npcCharacter);
            npcCharacter.send(new StartStateEvent(npc.getState()));
            npcCharacter.send(new StartDirectionEvent(npc.getDirection()));
            npcCharacter.send(new StartPositionEvent(npc.getPosition()));
            ObjectMap<String, LoadSpriteEvent> peopleData = Utility.getAllSpriteConfigsFromJson(PEOPLE1_SPRITE_CONFIG);
            LoadSpriteEvent loadSpriteEvent = peopleData.get(npc.getName());
            npcCharacter.send(loadSpriteEvent);
            if (npc.getState() == CharacterState.IMMOBILE) {
                currentMap.addToBlockers(npcCharacter.getBoundingBox());
            }
        }
    }

    private void updateCharacters(float dt) {
        player.update(npcCharacters, dt);
        List<Character> copyCharacters = new ArrayList<>(npcCharacters);
        copyCharacters.add(player);
        npcCharacters.forEach(npcCharacter -> npcCharacter.update(copyCharacters, dt));
    }

    private void updateCameraPosition() {
        camera.setPosition(player.getPosition());
        mapRenderer.setView(camera);
    }

    private void renderMapLayers() {
        int[] underLayers = {0, 1, 2, 3, 4, 5};
        mapRenderer.render(underLayers);
        renderCharacters();
        int[] overLayers = {6, 7, 8};
        mapRenderer.render(overLayers);
    }

    private void renderCharacters() {
        shapeRenderer.setProjectionMatrix(camera.combined);
        mapRenderer.getBatch().begin();
        npcCharacters.forEach(npcCharacter -> npcCharacter.render(mapRenderer.getBatch(), shapeRenderer));
        player.render(mapRenderer.getBatch(), shapeRenderer);
        mapRenderer.getBatch().end();
    }

    public static void setGameState(GameState newGameState) {
        switch (newGameState) {
            case RUNNING:
                gameState = GameState.RUNNING;
                break;
            case PAUSED:
                if (gameState == GameState.PAUSED) {
                    gameState = GameState.RUNNING;
                } else if (gameState == GameState.RUNNING) {
                    gameState = GameState.PAUSED;
                }
                break;
            default:
                gameState = GameState.RUNNING;
                break;
        }
    }

    public static void setShowGrid() {
        showGrid = !showGrid;
    }

    public static void setShowObjects() {
        showObjects = !showObjects;
    }

    public static void setShowDebug() {
        showDebug = !showDebug;
    }

    private static void updatePlayTime(float dt) {
        playTime += dt;
    }

    @Override
    public void resize(int width, int height) {
        // empty
    }

    @Override
    public void pause() {
        setGameState(GameState.PAUSED);
    }

    @Override
    public void resume() {
        setGameState(GameState.RUNNING);
    }

    @Override
    public void hide() {
        setGameState(GameState.PAUSED);
    }

    @Override
    public void dispose() {
        player.dispose();
        mapRenderer.dispose();
        shapeRenderer.dispose();
    }

    private void renderGrid() {
        if (showGrid) {
            shapeRenderer.setColor(Color.DARK_GRAY);
            shapeRenderer.begin(ShapeRenderer.ShapeType.Line);

            float mapPixelWidth = currentMap.getPixelWidth();
            float mapPixelHeight = currentMap.getPixelHeight();
            for (float x = 0; x < mapPixelWidth; x = x + Constant.TILE_SIZE) {
                shapeRenderer.line(x, 0, x, mapPixelHeight);
            }
            for (float y = 0; y < mapPixelHeight; y = y + Constant.TILE_SIZE) {
                shapeRenderer.line(0, y, mapPixelWidth, y);
            }
            shapeRenderer.end();
        }
    }

    private void renderObjects() {
        if (showObjects) {
            shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
            player.debug(shapeRenderer);
            npcCharacters.forEach(npcCharacter -> npcCharacter.debug(shapeRenderer));
            MapManager.getInstance().debug(shapeRenderer);
            shapeRenderer.end();
        }
    }

    private void renderDebugBox(float dt) {
        if (showDebug) {
            Gdx.gl.glEnable(GL20.GL_BLEND);
            Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
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
            shapeRenderer.setProjectionMatrix(camera.combined);

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
//                    "oldPositionX:\n" +
//                    "oldPositionY:\n" +
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
//                    player.getOldPosition().x + "\n" +
//                    player.getOldPosition().y + "\n" +
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
            batch.dispose();
            font.dispose();
        }
    }

}
