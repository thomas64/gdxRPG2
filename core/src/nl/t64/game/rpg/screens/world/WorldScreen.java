package nl.t64.game.rpg.screens.world;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.ai.pfa.DefaultGraphPath;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.utils.GdxRuntimeException;
import lombok.Getter;
import nl.t64.game.rpg.Utils;
import nl.t64.game.rpg.audio.AudioEvent;
import nl.t64.game.rpg.components.conversation.ConversationGraph;
import nl.t64.game.rpg.components.loot.Loot;
import nl.t64.game.rpg.components.quest.QuestGraph;
import nl.t64.game.rpg.constants.Constant;
import nl.t64.game.rpg.constants.GameState;
import nl.t64.game.rpg.constants.ScreenType;
import nl.t64.game.rpg.screens.inventory.InventoryScreen;
import nl.t64.game.rpg.screens.inventory.PartyObserver;
import nl.t64.game.rpg.screens.inventory.tooltip.MessageTooltip;
import nl.t64.game.rpg.screens.loot.*;
import nl.t64.game.rpg.screens.shop.ShopScreen;
import nl.t64.game.rpg.screens.world.conversation.ConversationDialog;
import nl.t64.game.rpg.screens.world.conversation.ConversationObserver;
import nl.t64.game.rpg.screens.world.entity.*;
import nl.t64.game.rpg.screens.world.entity.events.LoadEntityEvent;
import nl.t64.game.rpg.screens.world.entity.events.PathUpdateEvent;
import nl.t64.game.rpg.screens.world.messagedialog.MessageDialog;
import nl.t64.game.rpg.screens.world.pathfinding.TiledNode;
import nl.t64.game.rpg.sfx.TransitionImage;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;


public class WorldScreen implements Screen,
                                    MapObserver, ComponentObserver, PartyObserver, LootObserver, ConversationObserver {

    private static boolean showGrid = false;
    private static boolean showObjects = false;
    private static boolean showDebug = false;

    private GameState gameState;
    private final Stage stage;
    private final Camera camera;
    private final TextureMapObjectRenderer mapRenderer;
    private final InputMultiplexer multiplexer;
    private final ShapeRenderer shapeRenderer;
    private final PartyWindow partyWindow;
    private final ConversationDialog conversationDialog;
    private final MessageDialog messageDialog;
    private final MessageTooltip messageTooltip;
    private final DebugBox debugBox;

    private final Entity player;
    @Getter
    private List<Entity> partyMembers;
    @Getter
    private List<Entity> npcCharacters;
    private Entity currentNpcCharacter;
    @Getter
    private List<Entity> lootList;
    @Getter
    private List<Entity> doorList;

    public WorldScreen() {
        this.stage = new Stage();
        this.camera = new Camera();
        this.mapRenderer = new TextureMapObjectRenderer(this.camera);
        this.multiplexer = new InputMultiplexer();
        this.multiplexer.addProcessor(new WorldScreenListener(this::doBeforeLoadScreen, this::showHidePartyWindow));
        this.player = new Entity(Constant.PLAYER_ID,
                                 new InputPlayer(this.multiplexer), new PhysicsPlayer(), new GraphicsPlayer());
        this.player.registerObserver(this);
        this.npcCharacters = new ArrayList<>(0);
        this.lootList = new ArrayList<>(0);
        this.doorList = new ArrayList<>(0);
        this.shapeRenderer = new ShapeRenderer();
        this.partyWindow = new PartyWindow();
        this.conversationDialog = new ConversationDialog();
        this.messageDialog = new MessageDialog(this.multiplexer);
        this.messageTooltip = new MessageTooltip();

        this.debugBox = new DebugBox(this.player);

        this.setObservers();
    }

    private void setObservers() {
        conversationDialog.conversationSubject.addObserver(this);
        Utils.getMapManager().mapSubject.addObserver(this);

        var inventoryScreen = (InventoryScreen) Utils.getScreenManager().getScreen(ScreenType.INVENTORY);
        inventoryScreen.partySubject.addObserver(this);
        var findScreen = (FindScreen) Utils.getScreenManager().getScreen(ScreenType.FIND);
        findScreen.lootSubject.addObserver(this);
        var rewardScreen = (RewardScreen) Utils.getScreenManager().getScreen(ScreenType.REWARD);
        rewardScreen.lootSubject.addObserver(this);
        var receiveScreen = (ReceiveScreen) Utils.getScreenManager().getScreen(ScreenType.RECEIVE);
        receiveScreen.lootSubject.addObserver(this);
    }

    // MapObserver /////////////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    public void onNotifyMapWillChange(Runnable changeMap, Color transitionColor) {
        var transition = new TransitionImage(transitionColor);
        stage.addActor(transition);
        transition.addAction(Actions.sequence(Actions.alpha(0f),
                                              Actions.fadeIn(Constant.FADE_DURATION),
                                              Actions.run(changeMap),
                                              Actions.removeActor()));
    }

    @Override
    public void onNotifyMapChanged(GameMap currentMap) {
        mapRenderer.setMap(currentMap.tiledMap);
        camera.setNewMapSize(currentMap.getPixelWidth(), currentMap.getPixelHeight());
        player.send(new LoadEntityEvent(currentMap.playerSpawnDirection, currentMap.playerSpawnLocation));
        npcCharacters.forEach(Entity::unregisterObserver);
        lootList.forEach(Entity::unregisterObserver);
        doorList.forEach(Entity::unregisterObserver);
        npcCharacters = new NpcCharactersLoader(currentMap).createNpcs();
        lootList = new LootLoader(currentMap, true).createLoot();
        doorList = new DoorLoader(currentMap).createDoors();
        npcCharacters.forEach(npcCharacter -> npcCharacter.registerObserver(this));
        lootList.forEach(loot -> loot.registerObserver(this));
        doorList.forEach(door -> door.registerObserver(this));
        partyMembers = new PartyMembersLoader(player).loadPartyMembers();
        currentMap.setTiledGraph();
    }

    @Override
    public void onNotifyShakeCamera() {
        camera.startShaking();
    }

    // ComponentObserver ///////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    public void onNotifyShowConversationDialog(String conversationId, Entity npcCharacter) {
        this.currentNpcCharacter = npcCharacter;
        player.resetInput();
        conversationDialog.loadConversation(conversationId, npcCharacter.getId());
        conversationDialog.show();
    }

    @Override
    public void onNotifyShowConversationDialog(String conversationId, String characterId) {
        player.resetInput();
        conversationDialog.loadConversation(conversationId, characterId);
        conversationDialog.show();
    }

    @Override
    public void onNotifyShowNoteDialog(String noteId) {
        player.resetInput();
        conversationDialog.loadNote(noteId);
        conversationDialog.show();
    }

    @Override
    public void onNotifyShowFindDialog(Loot loot, AudioEvent event, String message) {
        messageDialog.setScreenAfterHide(() -> onNotifyShowFindDialog(loot, event));
        onNotifyShowMessageDialog(message);
    }

    @Override
    public void onNotifyShowFindDialog(Loot loot, AudioEvent event) {
        doBeforeLoadScreen();
        FindScreen.load(loot, event);
    }

    @Override
    public void onNotifyShowMessageDialog(String message) {
        player.resetInput();
        messageDialog.show(message, AudioEvent.SE_CONVERSATION_NEXT);
    }

    // PartyObserver ///////////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    public void onNotifyHeroDismissed() {
        partyMembers = new PartyMembersLoader(player).loadPartyMembers();
    }

    // LootObserver ////////////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    public void onNotifyLootTaken() {
        lootList.forEach(Entity::unregisterObserver);
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

    @Override
    public void onNotifyReceiveTaken() {
        String conversationId = currentNpcCharacter.getConversationId();
        QuestGraph quest = Utils.getGameData().getQuests().getQuestById(conversationId);
        ConversationGraph conversation = Utils.getGameData().getConversations().getConversationById(conversationId);
        quest.accept(this::onNotifyShowMessageTooltip);
        conversation.setCurrentPhraseId(Constant.PHRASE_ID_QUEST_ACCEPT);
    }

    // ConversationObserver ////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    public void onNotifyExitConversation() {
        conversationDialog.hideWithFade();
        show();
    }

    @Override
    public void onNotifyShowMessageTooltip(String message) {
        messageTooltip.show(message, stage);
    }

    @Override
    public void onNotifyShowLevelUpDialog(String message) {
        player.resetInput();
        messageDialog.show(message, AudioEvent.SE_LEVELUP);
    }

    @Override
    public void onNotifyLoadShop() {
        conversationDialog.hide();
        show();
        doBeforeLoadScreen();
        ShopScreen.load(currentNpcCharacter.getId(), currentNpcCharacter.getConversationId());
    }

    @Override
    public void onNotifyShowRewardDialog(Loot reward, String levelUpMessage) {
        stage.addAction(Actions.sequence(Actions.run(conversationDialog::hideWithFade),
                                         Actions.delay(Constant.DIALOG_FADE_OUT_DURATION),
                                         Actions.run(() -> RewardScreen.load(reward, levelUpMessage))));
    }

    @Override
    public void onNotifyShowReceiveDialog(Loot receive) {
        stage.addAction(Actions.sequence(Actions.run(conversationDialog::hideWithFade),
                                         Actions.delay(Constant.DIALOG_FADE_OUT_DURATION),
                                         Actions.run(() -> ReceiveScreen.load(receive))));
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

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    public void show() {
        gameState = GameState.RUNNING;
        Gdx.input.setInputProcessor(multiplexer);
        Utils.setGamepadInputProcessor(multiplexer);
        Utils.getMapManager().continueAudio();
    }

    @Override
    public void render(float dt) {
        if (gameState.equals(GameState.PAUSED)) {
            return;
        }

        Utils.getMapManager().updateQuestLayers();
        updateCharacters(dt);
        updateCameraPosition();
        mapRenderer.renderAll(player.getPosition(), this::renderEntities);
        renderGrid();
        renderObjects();
        updateDebugBox(dt);
        partyWindow.update(dt);
        conversationDialog.update(dt);
        messageDialog.update(dt);

        stage.act(dt);
        if (isInTransition()) {
            Utils.getMapManager().fadeAudio();
        }
        stage.draw();
    }

    public List<Entity> createCopyOfCharactersWithPlayerButWithoutThisNpc(Entity thisNpcCharacter) {
        var theOtherCharacters = new ArrayList<>(npcCharacters);
        theOtherCharacters.add(player);
        theOtherCharacters.remove(thisNpcCharacter);
        return List.copyOf(theOtherCharacters);
    }

    public Entity getVisibleNpcOfInvisibleNpcBy(String conversationId) {
        return npcCharacters.stream()
                            .filter(npc -> npc.getConversationId().equals(conversationId))
                            .findFirst()
                            .orElseThrow(() -> new GdxRuntimeException("No same conversation found for invisible npc."));
    }

    private void updateCharacters(float dt) {
        if (!isInTransition()) {
            player.update(dt);
        }
        doorList.forEach(door -> door.update(dt));
        lootList.forEach(loot -> loot.update(dt));
        npcCharacters.forEach(npcCharacter -> npcCharacter.update(dt));
        partyMembers.forEach(partyMember -> partyMember.send(new PathUpdateEvent(getPathOf(partyMember))));
        partyMembers.forEach(partyMember -> partyMember.update(dt));
    }

    private void updateCameraPosition() {
        camera.setPosition(player.getPosition());
        mapRenderer.updateCamera();
    }

    private void renderEntities() {
        shapeRenderer.setProjectionMatrix(camera.combined);
        lootList.forEach(loot -> loot.render(mapRenderer.getBatch(), shapeRenderer));
        doorList.stream()
                .filter(door -> door.getPosition().y >= player.getPosition().y)
                .forEach(door -> door.render(mapRenderer.getBatch(), shapeRenderer));

        List<Entity> allCharacters = new ArrayList<>();
        allCharacters.addAll(Utils.reverseList(partyMembers));
        allCharacters.addAll(npcCharacters);
        allCharacters.add(player);
        allCharacters.sort(Comparator.comparingDouble((Entity character) -> character.getPosition().y).reversed());
        allCharacters.forEach(character -> character.render(mapRenderer.getBatch(), shapeRenderer));

        doorList.stream()
                .filter(door -> door.getPosition().y < player.getPosition().y)
                .forEach(door -> door.render(mapRenderer.getBatch(), shapeRenderer));
    }

    private DefaultGraphPath<TiledNode> getPathOf(Entity partyMember) {
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

    private void doBeforeLoadScreen() {
        player.resetInput();
        render(0f);
    }

    private void showHidePartyWindow() {
        partyWindow.showHide();
    }

    private boolean isInTransition() {
        return stage.getActors().notEmpty() && stage.getActors().peek() instanceof TransitionImage;
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
        Utils.setGamepadInputProcessor(null);
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
        stage.dispose();
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
            doorList.forEach(door -> door.debug(shapeRenderer));
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
