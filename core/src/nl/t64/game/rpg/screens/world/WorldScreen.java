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
import nl.t64.game.rpg.components.conversation.ConversationGraph;
import nl.t64.game.rpg.components.loot.Loot;
import nl.t64.game.rpg.components.quest.QuestGraph;
import nl.t64.game.rpg.constants.Constant;
import nl.t64.game.rpg.constants.GameState;
import nl.t64.game.rpg.constants.ScreenType;
import nl.t64.game.rpg.events.character.LoadCharacterEvent;
import nl.t64.game.rpg.events.character.PathUpdateEvent;
import nl.t64.game.rpg.screens.LoadScreen;
import nl.t64.game.rpg.screens.inventory.PartyObserver;
import nl.t64.game.rpg.screens.inventory.PartySubject;
import nl.t64.game.rpg.screens.loot.LootObserver;
import nl.t64.game.rpg.screens.loot.LootSubject;
import nl.t64.game.rpg.screens.shop.ShopScreen;
import nl.t64.game.rpg.screens.world.conversation.ConversationDialog;
import nl.t64.game.rpg.screens.world.conversation.ConversationObserver;
import nl.t64.game.rpg.screens.world.pathfinding.TiledNode;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;


public class WorldScreen implements Screen,
                                    MapObserver, ComponentObserver, PartyObserver, LootObserver, ConversationObserver {

    private static final int[] UNDER_LAYERS = new int[]{0, 1, 2, 3, 4, 5};
    private static final int[] OVER_LAYERS = new int[]{6, 7, 8};

    private static boolean showGrid = false;
    private static boolean showObjects = false;
    private static boolean showDebug = false;

    private GameState gameState;
    private final Camera camera;
    private final OrthogonalTiledMapRenderer mapRenderer;
    private final InputMultiplexer multiplexer;
    private final ShapeRenderer shapeRenderer;
    private final PartyWindow partyWindow;
    private final ConversationDialog conversationDialog;
    private final MessageDialog messageDialog;
    private final DebugBox debugBox;

    private final Character player;
    @Getter
    private List<Character> partyMembers;
    @Getter
    private List<Character> npcCharacters;
    private Character currentNpcCharacter;
    @Getter
    private List<Character> lootList;

    public WorldScreen() {
        this.camera = new Camera();
        this.mapRenderer = new OrthogonalTiledMapRenderer(null);
        this.multiplexer = new InputMultiplexer();
        this.multiplexer.addProcessor(new WorldScreenListener(this::openMenuPause,
                                                              this::openInventoryScreen,
                                                              this::showHidePartyWindow));
        this.player = new Character(Constant.PLAYER_ID,
                                    new InputPlayer(this.multiplexer),
                                    new PhysicsPlayer(),
                                    new GraphicsPlayer());
        this.player.registerObserver(this);
        this.npcCharacters = new ArrayList<>(0);
        this.lootList = new ArrayList<>(0);
        this.shapeRenderer = new ShapeRenderer();
        this.partyWindow = new PartyWindow();
        this.conversationDialog = new ConversationDialog();
        this.messageDialog = new MessageDialog(this.multiplexer);

        this.debugBox = new DebugBox(this.player);

        Utils.getMapManager().addObserver(this);
        ((PartySubject) Utils.getScreenManager().getScreen(ScreenType.INVENTORY)).addObserver(this);
        ((LootSubject) Utils.getScreenManager().getScreen(ScreenType.LOOT)).addObserver(this);
        ((LootSubject) Utils.getScreenManager().getScreen(ScreenType.REWARD)).addObserver(this);
        this.conversationDialog.addObserver(this);
    }

    // MapObserver /////////////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    public void onMapChanged(GameMap currentMap) {
        mapRenderer.setMap(currentMap.tiledMap);
        camera.setNewMapSize(currentMap.getPixelWidth(), currentMap.getPixelHeight());
        player.send(new LoadCharacterEvent(currentMap.playerSpawnDirection,
                                           currentMap.playerSpawnLocation));
        npcCharacters.forEach(Character::unregisterObserver);
        lootList.forEach(Character::unregisterObserver);
        npcCharacters = new NpcCharactersLoader(currentMap).createNpcs();
        lootList = new LootLoader(currentMap, true).createLoot();
        npcCharacters.forEach(npcCharacter -> npcCharacter.registerObserver(this));
        lootList.forEach(loot -> loot.registerObserver(this));
        partyMembers = new PartyMembersLoader(player).loadPartyMembers();
        currentMap.setTiledGraph();
    }

    // ComponentObserver ///////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    public void onNotifyShowConversationDialog(String conversationId, Character npcCharacter) {
        this.currentNpcCharacter = npcCharacter;
        player.resetInput();
        conversationDialog.loadConversation(conversationId, npcCharacter.getId());
        conversationDialog.show();
    }

    @Override
    public void onNotifyShowNoteDialog(String noteId) {
        player.resetInput();
        conversationDialog.loadNote(noteId);
        conversationDialog.show();
    }

    @Override
    public void onNotifyShowLootDialog(Loot loot) {
        var lootScreenLoader = new LootScreenLoader(this::openLoadScreen, loot);
        lootScreenLoader.openLootScreen();
    }

    @Override
    public void onNotifyShowLootDialog(Loot loot, String message) {
        var lootScreenLoader = new LootScreenLoader(this::openLoadScreen, loot);
        onNotifyShowMessageDialog(message);
        messageDialog.setScreenAfterHide(lootScreenLoader::openLootScreen);
    }

    @Override   // also ConversationObserver
    public void onNotifyShowMessageDialog(String message) {
        player.resetInput();
        messageDialog.setMessage(message);
        messageDialog.show();
    }

    // PartyObserver ///////////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    public void onNotifyHeroDismissed() {
        partyMembers = new PartyMembersLoader(player).loadPartyMembers();
    }

    // LootObserver ////////////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    public void onNotifyLootTaken() {
        lootList.forEach(Character::unregisterObserver);
        lootList = new LootLoader(Utils.getMapManager().currentMap, false).createLoot();
        lootList.forEach(loot -> loot.registerObserver(this));
    }

    @Override
    public void onNotifyRewardTaken() {
        String conversationId = currentNpcCharacter.getConversationId();
        QuestGraph quest = Utils.getGameData().getQuests().getQuestById(conversationId);
        ConversationGraph conversation = Utils.getGameData().getConversations().getConversationById(conversationId);
        quest.finish();
        conversation.setCurrentPhraseId(Constant.PHRASE_ID_QUEST_FINISHED);
    }

    // ConversationObserver ////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    public void onNotifyExitConversation() {
        conversationDialog.hideWithFade();
        show();
    }

    @Override
    public void onNotifyLoadShop() {
        conversationDialog.hide();
        show();
        openShopScreen();
    }

    @Override
    public void onNotifyShowRewardDialog(Loot reward) {
        var rewardScreenLoader = new RewardScreenLoader(this::openLoadScreen, reward);
        rewardScreenLoader.openLootScreen();
        conversationDialog.hideWithFade();
    }

    @Override
    public void onNotifyHeroJoined() {
        Utils.getMapManager().removeFromBlockers(currentNpcCharacter.getBoundingBox());
        currentNpcCharacter.unregisterObserver();
        npcCharacters = npcCharacters.stream()
                                     .filter(npcCharacter -> !npcCharacter.equals(currentNpcCharacter))
                                     .collect(Collectors.toUnmodifiableList());
        partyMembers = new PartyMembersLoader(player).loadPartyMembers();
    }

    @Override
    public void onNotifyHeroDismiss() {
        throw new IllegalCallerException("Impossible to dismiss Hero from WorldScreen.");
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

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

        Utils.getMapManager().updateQuestBlockers();
        updateCharacters(dt);
        updateCameraPosition();
        renderMapLayers();
        renderGrid();
        renderObjects();
        updateDebugBox(dt);
        partyWindow.update(dt);
        conversationDialog.update(dt);
        messageDialog.update(dt);
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
        lootList.forEach(loot -> loot.update(dt));
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

        lootList.forEach(loot -> loot.render(mapRenderer.getBatch(), shapeRenderer));

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

    private void openMenuPause() {
        player.resetInput();
        var menuPause = Utils.getScreenManager().getMenuScreen(ScreenType.MENU_PAUSE);
        menuPause.setBackground(createScreenshot(true));
        Utils.getScreenManager().setScreen(ScreenType.MENU_PAUSE);
        menuPause.updateMenuIndex(0);
    }

    private void openInventoryScreen() {
        openLoadScreen(ScreenType.INVENTORY_LOAD);
    }

    private void openShopScreen() {
        var shopScreen = (ShopScreen) Utils.getScreenManager().getScreen(ScreenType.SHOP);
        shopScreen.setNpcId(currentNpcCharacter.getId());
        shopScreen.setShopId(currentNpcCharacter.getConversationId());

        openLoadScreen(ScreenType.SHOP_LOAD);
    }

    private void openLoadScreen(ScreenType screenType) {
        player.resetInput();
        render(0f);
        var loadScreen = (LoadScreen) Utils.getScreenManager().getScreen(screenType);
        loadScreen.setBackground(createScreenshot(true));
        Utils.getScreenManager().setScreen(screenType);
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
        messageDialog.dispose();
        debugBox.dispose();
    }

    static void setShowGrid() {
        if (Utils.getSettings().isInDebugMode()) {
            showGrid = !showGrid;
        }
    }

    static void setShowObjects() {
        if (Utils.getSettings().isInDebugMode()) {
            showObjects = !showObjects;
        }
    }

    static void setShowDebug() {
        if (Utils.getSettings().isInDebugMode()) {
            showDebug = !showDebug;
        }
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
            lootList.forEach(loot -> loot.debug(shapeRenderer));
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
