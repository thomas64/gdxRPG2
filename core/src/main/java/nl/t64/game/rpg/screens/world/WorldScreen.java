package nl.t64.game.rpg.screens.world;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.ai.pfa.DefaultGraphPath;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.badlogic.gdx.utils.ScreenUtils;
import lombok.Getter;
import nl.t64.game.rpg.Utils;
import nl.t64.game.rpg.components.character.Character;
import nl.t64.game.rpg.components.character.*;
import nl.t64.game.rpg.components.party.HeroContainer;
import nl.t64.game.rpg.components.party.HeroItem;
import nl.t64.game.rpg.components.party.PartyContainer;
import nl.t64.game.rpg.constants.Constant;
import nl.t64.game.rpg.constants.ConversationCommand;
import nl.t64.game.rpg.constants.GameState;
import nl.t64.game.rpg.constants.ScreenType;
import nl.t64.game.rpg.events.character.LoadCharacterEvent;
import nl.t64.game.rpg.events.character.PathUpdateEvent;
import nl.t64.game.rpg.screens.inventory.InventoryLoadScreen;
import nl.t64.game.rpg.screens.world.conversation.ConversationDialog;
import nl.t64.game.rpg.screens.world.pathfinding.TiledNode;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;


public class WorldScreen implements Screen, MapObserver, ComponentObserver {

    private static final int[] UNDER_LAYERS = new int[]{0, 1, 2, 3, 4, 5};
    private static final int[] OVER_LAYERS = new int[]{6, 7, 8};
    private static final String PARTY_FULL_CONVERSATION = "partyfull";

    private static boolean showGrid = false;
    private static boolean showObjects = false;
    private static boolean showDebug = false;

    private GameState gameState;
    private Camera camera;

    private OrthogonalTiledMapRenderer mapRenderer;
    private InputMultiplexer multiplexer;
    private Character player;
    @Getter
    private List<Character> partyMembers;
    @Getter
    private List<Character> npcCharacters;
    private Character currentNpcCharacter;
    private ShapeRenderer shapeRenderer;
    private PartyWindow partyWindow;
    private ConversationDialog conversationDialog;

    private DebugBox debugBox;

    public WorldScreen() {
        this.camera = new Camera();
        this.mapRenderer = new OrthogonalTiledMapRenderer(null);
        this.multiplexer = new InputMultiplexer();
        this.multiplexer.addProcessor(new WorldScreenListener(this::openMenuPause,
                                                              this::openInventoryScreen,
                                                              this::showHidePartyWindow));
        this.player = new Character(new InputPlayer(this.multiplexer), new PhysicsPlayer(), new GraphicsPlayer());
        this.npcCharacters = new ArrayList<>(0);
        this.shapeRenderer = new ShapeRenderer();
        this.partyWindow = new PartyWindow();
        this.conversationDialog = new ConversationDialog(this::handleConversationCommand);

        this.debugBox = new DebugBox(this.player);

        Utils.getMapManager().addObserver(this);
    }

    @Override
    public void onMapChanged(GameMap currentMap) {
        mapRenderer.setMap(currentMap.tiledMap);
        camera.setNewMapSize(currentMap.getPixelWidth(), currentMap.getPixelHeight());
        player.send(new LoadCharacterEvent(currentMap.playerSpawnDirection,
                                           currentMap.playerSpawnLocation));
        npcCharacters.forEach(Character::unregisterObserver);
        npcCharacters = new NpcCharactersLoader(currentMap).createNpcs();
        npcCharacters.forEach(npcCharacter -> npcCharacter.registerObserver(this));
        partyMembers = new PartyMembersLoader(player).loadPartyMembers();
    }

    @Override
    public void onNotifyShowConversationDialog(String conversationId, String characterId, Character npcCharacter) {
        this.currentNpcCharacter = npcCharacter;
        player.resetInput();
        conversationDialog.loadConversation(conversationId, characterId);
        conversationDialog.show();
    }

    @Override
    public void show() {
        gameState = GameState.RUNNING;
        Gdx.input.setInputProcessor(multiplexer);
    }

    @Override
    public void render(float dt) {
        if (gameState.equals(GameState.PAUSED)) {
            return;
        }

        Gdx.gl.glClearColor(0f, 0f, 0f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        updateCharacters(dt);
        updateCameraPosition();
        renderMapLayers();
        renderGrid();
        renderObjects();
        updateDebugBox(dt);
        partyWindow.update(dt);
        conversationDialog.update(dt);
    }

    public List<Character> createCopyOfCharactersWithPlayerButWithoutThisNpc(Character thisNpcCharacter) {
        var theOtherCharacters = new ArrayList<>(npcCharacters);
        theOtherCharacters.add(player);
        theOtherCharacters.remove(thisNpcCharacter);
        return Collections.unmodifiableList(theOtherCharacters);
    }

    public Character getVisibleNpcOfInvisibleNpcBy(String conversationId) {
        return npcCharacters.stream()
                            .filter(npc -> npc.getConversationId().equals(conversationId))
                            .findFirst()
                            .orElseThrow(() -> new GdxRuntimeException("No same conversation found for invisible npc."));
    }

    private void updateCharacters(float dt) {
        player.update(dt);
        npcCharacters.forEach(npcCharacter -> npcCharacter.update(dt));
        partyMembers.forEach(partyMember -> partyMember.send(new PathUpdateEvent(getPathOf(partyMember))));
        partyMembers.forEach(partyMember -> partyMember.update(dt));
    }

    private void updateCameraPosition() {
        camera.setPosition(player.getPosition());
        mapRenderer.setView(camera);
    }

    private void renderMapLayers() {
        mapRenderer.render(UNDER_LAYERS);
        renderCharacters();
        mapRenderer.render(OVER_LAYERS);
    }

    private void renderCharacters() {
        shapeRenderer.setProjectionMatrix(camera.combined);
        mapRenderer.getBatch().begin();

        List<Character> allCharacters = new ArrayList<>();
        allCharacters.addAll(Utils.reverseList(partyMembers));
        allCharacters.addAll(npcCharacters);
        allCharacters.add(player);
        allCharacters.sort(Comparator.comparingDouble((Character c) -> c.getPosition().y)
                                     .reversed());
        allCharacters.forEach(character -> character.render(mapRenderer.getBatch(), shapeRenderer));

        mapRenderer.getBatch().end();
    }

    private DefaultGraphPath<TiledNode> getPathOf(Character partyMember) {
        final Vector2 startPoint = partyMember.getPositionInGrid();
        final Vector2 endPoint;
        final int index = partyMembers.indexOf(partyMember);
        if (index == 0) {
            endPoint = player.getPositionInGrid();
        } else {
            endPoint = partyMembers.get(index - 1).getPositionInGrid();
        }
        return Utils.getMapManager().currentMap.tiledGraph.findPath(startPoint, endPoint);
    }

    private void handleConversationCommand(ConversationCommand command) {
        switch (command) {
            case EXIT_CONVERSATION -> {
                conversationDialog.hide();
                show();
            }
            case JOIN_PARTY -> tryToAddHeroToParty();
            case LOAD_STORE -> {
            }
        }
    }

    private void tryToAddHeroToParty() {
        HeroContainer heroes = Utils.getGameData().getHeroes();
        PartyContainer party = Utils.getGameData().getParty();
        String heroId = currentNpcCharacter.getCharacterId();
        HeroItem hero = heroes.getHero(heroId);

        if (party.isFull()) {
            conversationDialog.loadConversation(PARTY_FULL_CONVERSATION, heroId);
        } else {
            heroes.removeHero(heroId);
            party.addHero(hero);
            refreshNpcCharacters();
            handleConversationCommand(ConversationCommand.EXIT_CONVERSATION);
        }
    }

    private void refreshNpcCharacters() {
        Utils.getMapManager().removeFromBlockers(currentNpcCharacter.getBoundingBox());
        npcCharacters = npcCharacters.stream()
                                     .filter(npcCharacter -> !npcCharacter.equals(currentNpcCharacter))
                                     .collect(Collectors.toUnmodifiableList());
        partyMembers = new PartyMembersLoader(player).loadPartyMembers();
    }

    private void openMenuPause() {
        player.resetInput();
        var menuPause = Utils.getScreenManager().getMenuScreen(ScreenType.MENU_PAUSE);
        menuPause.setBackground(createScreenshot(true));
        Utils.getScreenManager().setScreen(ScreenType.MENU_PAUSE);
        menuPause.updateMenuIndex(0);
    }

    private void openInventoryScreen() {
        player.resetInput();
        var inventoryLoadScreen = (InventoryLoadScreen) Utils.getScreenManager().getScreen(ScreenType.INVENTORY_LOAD);
        inventoryLoadScreen.setBackground(createScreenshot(false));
        Utils.getScreenManager().setScreen(ScreenType.INVENTORY_LOAD);
    }

    private Image createScreenshot(boolean withBlur) {
        var screenshot = new Image(ScreenUtils.getFrameBufferTexture());
        if (withBlur) {
            screenshot.setColor(Color.DARK_GRAY);
        }
        return screenshot;
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
        conversationDialog.dispose();
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
            partyMembers.forEach(partyMember -> partyMember.debug(shapeRenderer));
            Utils.getMapManager().currentMap.debug(shapeRenderer);
            shapeRenderer.end();
        }
    }

    private void updateDebugBox(float dt) {
        if (showDebug) {
            debugBox.update(dt);
        }
    }

}
