package nl.t64.game.rpg.screens.world;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.ScreenUtils;
import lombok.Getter;
import nl.t64.game.rpg.Utils;
import nl.t64.game.rpg.components.character.Character;
import nl.t64.game.rpg.components.character.GraphicsPlayer;
import nl.t64.game.rpg.components.character.InputPlayer;
import nl.t64.game.rpg.components.character.PhysicsPlayer;
import nl.t64.game.rpg.constants.Constant;
import nl.t64.game.rpg.constants.GameState;
import nl.t64.game.rpg.constants.ScreenType;
import nl.t64.game.rpg.events.character.StartDirectionEvent;
import nl.t64.game.rpg.events.character.StartPositionEvent;
import nl.t64.game.rpg.screens.InventoryScreen;
import nl.t64.game.rpg.screens.menu.MenuPause;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class WorldScreen implements Screen, MapObserver {

    private static boolean showGrid = false;
    private static boolean showObjects = false;
    private static boolean showDebug = false;

    private GameState gameState;
    private Camera camera;

    private OrthogonalTiledMapRenderer mapRenderer;
    private InputMultiplexer multiplexer;
    private Character player;
    @Getter
    private List<Character> npcCharacters;
    private ShapeRenderer shapeRenderer;
    private PartyWindow partyWindow;

    private DebugBox debugBox;

    public WorldScreen() {
        this.camera = new Camera();
        this.mapRenderer = new OrthogonalTiledMapRenderer(null);
        this.multiplexer = new InputMultiplexer();
        this.multiplexer.addProcessor(new WorldScreenListener(this::openMenuPause,
                                                              this::openInventoryScreen,
                                                              this::showHidePartyWindow));
        this.player = new Character(new InputPlayer(this.multiplexer), new PhysicsPlayer(), new GraphicsPlayer());
        this.shapeRenderer = new ShapeRenderer();
        this.partyWindow = new PartyWindow();

        this.debugBox = new DebugBox(this.player);

        Utils.getMapManager().addObserver(this);
    }

    @Override
    public void onMapChanged(GameMap currentMap) {
        mapRenderer.setMap(currentMap.tiledMap);
        camera.setNewMapSize(currentMap.getPixelWidth(), currentMap.getPixelHeight());
        player.send(new StartPositionEvent(currentMap.playerSpawnLocation));
        player.send(new StartDirectionEvent(currentMap.playerSpawnDirection));
        npcCharacters = new NpcCharactersLoader(currentMap).createNpcs();
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

        updateCharacters(dt);
        updateCameraPosition();
        renderMapLayers();
        renderGrid();
        renderObjects();
        renderDebugBox(dt);
        partyWindow.render(dt);
    }

    public void removeNpcCharacter(Character npcCharacter) {
        npcCharacters.remove(npcCharacter);
    }

    public List<Character> createCopyOfCharactersWithPlayerButWithoutThisNpc(Character thisNpcCharacter) {
        var theOtherCharacters = new ArrayList<>(npcCharacters);
        theOtherCharacters.add(player);
        theOtherCharacters.remove(thisNpcCharacter);
        return Collections.unmodifiableList(theOtherCharacters);
    }

    private void updateCharacters(float dt) {
        player.update(dt);
        List.copyOf(npcCharacters).forEach(npcCharacter -> npcCharacter.update(dt));
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

    private void openMenuPause() {
        player.resetInput();
        var menuPause = (MenuPause) Utils.getScreenManager().getScreen(ScreenType.MENU_PAUSE);
        menuPause.setBackground(createScreenshot());
        Utils.getScreenManager().setScreen(ScreenType.MENU_PAUSE);
        menuPause.updateIndex(0);
    }

    private void openInventoryScreen() {
        player.resetInput();
        var inventoryScreen = (InventoryScreen) Utils.getScreenManager().getScreen(ScreenType.INVENTORY);
        inventoryScreen.setBackground(createScreenshot());
        Utils.getScreenManager().setScreen(ScreenType.INVENTORY);
    }

    private Image createScreenshot() {
        return new Image(ScreenUtils.getFrameBufferTexture());
    }

    private void showHidePartyWindow() {
        partyWindow.showHide();
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
        partyWindow.dispose();
        debugBox.dispose();
    }

    static void setShowGrid() {
        showGrid = !showGrid;
    }

    static void setShowObjects() {
        showObjects = !showObjects;
    }

    static void setShowDebug() {
        showDebug = !showDebug;
    }

    private void renderGrid() {
        if (showGrid) {
            shapeRenderer.setColor(Color.DARK_GRAY);
            shapeRenderer.begin(ShapeRenderer.ShapeType.Line);

            float mapPixelWidth = Utils.getMapManager().currentMap.getPixelWidth();
            float mapPixelHeight = Utils.getMapManager().currentMap.getPixelHeight();
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
            Utils.getMapManager().currentMap.debug(shapeRenderer);
            shapeRenderer.end();
        }
    }

    private void renderDebugBox(float dt) {
        if (showDebug) {
            debugBox.render(dt);
        }
    }

}
