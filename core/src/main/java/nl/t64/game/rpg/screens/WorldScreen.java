package nl.t64.game.rpg.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import lombok.Getter;
import nl.t64.game.rpg.Camera;
import nl.t64.game.rpg.Engine;
import nl.t64.game.rpg.components.character.Character;
import nl.t64.game.rpg.components.character.*;
import nl.t64.game.rpg.constants.CharacterState;
import nl.t64.game.rpg.constants.Constant;
import nl.t64.game.rpg.constants.GameState;
import nl.t64.game.rpg.events.character.StartDirectionEvent;
import nl.t64.game.rpg.events.character.StartPositionEvent;
import nl.t64.game.rpg.events.character.StartStateEvent;
import nl.t64.game.rpg.listeners.WorldScreenListener;
import nl.t64.game.rpg.profile.ProfileManager;
import nl.t64.game.rpg.profile.ProfileObserver;
import nl.t64.game.rpg.screens.menu.PauseMenu;
import nl.t64.game.rpg.tiled.GameMap;
import nl.t64.game.rpg.tiled.Hero;
import nl.t64.game.rpg.tiled.Npc;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;


public class WorldScreen implements Screen, ProfileObserver {

    private static final String TAG = WorldScreen.class.getSimpleName();
    private static final Color TRANSPARENT = new Color(0f, 0f, 0f, 0.5f);

    private static boolean showGrid = false;
    private static boolean showObjects = false;
    private static boolean showDebug = false;

    private Engine engine;

    private GameState gameState;
    private Camera camera;
    @Getter
    private GameMap currentMap;
    private OrthogonalTiledMapRenderer mapRenderer;
    private InputMultiplexer multiplexer;
    @Getter
    private Character player;
    @Getter
    private List<Character> npcCharacters;
    private ShapeRenderer shapeRenderer;

    private boolean isMapChanged = false;

    public WorldScreen(Engine engine) {
        this.engine = engine;
        this.camera = new Camera();
        this.mapRenderer = new OrthogonalTiledMapRenderer(null);
        this.multiplexer = new InputMultiplexer();
        this.multiplexer.addProcessor(new WorldScreenListener(this::openPauseMenu, this::openInventoryScreen));
        this.player = new Character(new PlayerInput(this.multiplexer), new PlayerPhysics(), new PlayerGraphics());
        this.shapeRenderer = new ShapeRenderer();
    }

    @Override
    public void onNotifyCreate(ProfileManager profileManager) {
        loadMap(Constant.STARTING_MAP);
        currentMap.setPlayerSpawnLocationForNewLoad(Constant.STARTING_MAP);
        onNotifySave(profileManager);
    }

    @Override
    public void onNotifySave(ProfileManager profileManager) {
        profileManager.setProperty("mapTitle", currentMap.getMapTitle());
    }

    @Override
    public void onNotifyLoad(ProfileManager profileManager) {
        String mapTitle = profileManager.getProperty("mapTitle", String.class);
        loadMap(mapTitle);
        currentMap.setPlayerSpawnLocationForNewLoad(mapTitle);
    }

    @Override
    public void show() {
        gameState = GameState.RUNNING;
        Gdx.input.setInputProcessor(multiplexer);
    }

    @Override
    public void render(float dt) {
        if (gameState == GameState.PAUSED) {
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

    public void loadMap(String mapTitle) {
        disposeOldMap();
        currentMap = new GameMap(mapTitle, engine.getData());
        isMapChanged = true;
    }

    private void disposeOldMap() {
        if (currentMap != null) {
            currentMap.dispose();
        }
    }

    private void updateMap() {
        if (isMapChanged) {
            mapRenderer.setMap(currentMap.getTiledMap());
            camera.setNewMapSize(currentMap.getPixelWidth(), currentMap.getPixelHeight());
            loadNpcCharacters();
            player.send(new StartPositionEvent(currentMap.getPlayerSpawnLocation()));
            player.send(new StartDirectionEvent(currentMap.getPlayerSpawnDirection()));
            isMapChanged = false;
        }
    }

    private void loadNpcCharacters() {
        npcCharacters = new ArrayList<>();
        for (Npc npc : currentMap.getNpcs()) {
            String spriteId = npc.getName();
            var npcCharacter = new Character(new NpcInput(), new NpcPhysics(), new NpcGraphics(spriteId));
            loadNpcCharacter(npc, npcCharacter);
        }
        for (Hero hero : currentMap.getHeroes()) {
            String spriteId = hero.getName();
            var heroCharacter = new Character(new NpcInput(), new HeroPhysics(spriteId), new NpcGraphics(spriteId));
            loadNpcCharacter(hero, heroCharacter);
        }
    }

    private void loadNpcCharacter(Npc npc, Character npcCharacter) {
        npcCharacters.add(npcCharacter);
        npcCharacter.send(new StartStateEvent(npc.getState()));
        npcCharacter.send(new StartDirectionEvent(npc.getDirection()));
        npcCharacter.send(new StartPositionEvent(npc.getPosition()));
        if (npc.getState() == CharacterState.IMMOBILE) {
            currentMap.addToBlockers(npcCharacter.getBoundingBox());
        }
    }

    public void removeNpcCharacter(Character npcCharacter) {
        npcCharacters.remove(npcCharacter);
        currentMap.removeFromBlockers(npcCharacter.getBoundingBox());
    }

    public List<Character> createCopyOfCharactersWithPlayerButWithoutThisNpc(Character thisNpcCharacter) {
        var theOtherCharacters = new ArrayList<>(npcCharacters);
        theOtherCharacters.add(player);
        theOtherCharacters.remove(thisNpcCharacter);
        return theOtherCharacters;
    }

    private void updateCharacters(float dt) {
        player.update(engine, dt);
        var npcCharactersCopy = new ArrayList<>(npcCharacters);
        npcCharactersCopy.forEach(npcCharacter -> npcCharacter.update(engine, dt));
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

    private void openPauseMenu() {
        player.resetInput();
        PauseMenu pauseMenu = engine.getPauseMenuScreen();
        pauseMenu.setBackground();
        engine.setScreen(pauseMenu);
        pauseMenu.updateIndex(0);
    }

    private void openInventoryScreen() {
        player.resetInput();
        engine.setScreen(new InventoryScreen(engine));
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

    @Override
    public void resize(int width, int height) {
        // empty
    }

    @Override
    public void pause() {
        gameState = GameState.PAUSED;
    }

    @Override
    public void resume() {
        gameState = GameState.RUNNING;
    }

    @Override
    public void hide() {
        gameState = GameState.PAUSED;
        Gdx.input.setInputProcessor(null);
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
            currentMap.debug(shapeRenderer);
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

            int w = 150;
            int h = 300;
            int x = 0 - (Constant.SCREEN_WIDTH / 4);
            int y = 0 - (Constant.SCREEN_HEIGHT / 4) + ((Constant.SCREEN_HEIGHT / 2) - h);
            shapeRenderer.rect(x, y, w, h);
            shapeRenderer.end();
            Gdx.gl.glDisable(GL20.GL_BLEND);
            shapeRenderer.setProjectionMatrix(camera.combined);

            String debug1 = "" +
                    "FPS:\n" +
                    "dt:\n" +
                    "runTime:\n" +
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

            String runTime = String.valueOf(Engine.getRunTime());
            String debug2 = "" +
                    Gdx.graphics.getFramesPerSecond() + "\n" +
                    String.valueOf(dt).substring(0, 5) + "\n" +
                    runTime.substring(0, runTime.length() - 4) + "\n" +
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

            var lines1 = debug1.lines().collect(Collectors.toList());
            var lines2 = debug2.lines().collect(Collectors.toList());
            var batch = new SpriteBatch();
            var font = new BitmapFont();
            font.setColor(Color.WHITE);
            batch.begin();
            int lineHeight = 15;
            int xFirstColumn = 0;
            int xSecondColumn = 200;
            IntStream.range(0, lines1.size()).forEach(i -> {
                font.draw(batch, lines1.get(i), xFirstColumn, Gdx.graphics.getHeight() - (i * lineHeight));
                font.draw(batch, lines2.get(i), xSecondColumn, Gdx.graphics.getHeight() - (i * lineHeight));
            });
            batch.end();
            batch.dispose();
            font.dispose();
        }
    }

}
