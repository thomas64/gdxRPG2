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
import lombok.Getter;
import nl.t64.game.rpg.Utils;
import nl.t64.game.rpg.audio.AudioCommand;
import nl.t64.game.rpg.audio.AudioEvent;
import nl.t64.game.rpg.components.conversation.ConversationGraph;
import nl.t64.game.rpg.components.loot.Loot;
import nl.t64.game.rpg.components.quest.QuestGraph;
import nl.t64.game.rpg.constants.Constant;
import nl.t64.game.rpg.constants.GameState;
import nl.t64.game.rpg.screens.inventory.tooltip.MessageTooltip;
import nl.t64.game.rpg.screens.loot.FindScreen;
import nl.t64.game.rpg.screens.loot.ReceiveScreen;
import nl.t64.game.rpg.screens.loot.RewardScreen;
import nl.t64.game.rpg.screens.shop.ShopScreen;
import nl.t64.game.rpg.screens.world.conversation.ConversationDialog;
import nl.t64.game.rpg.screens.world.conversation.ConversationObserver;
import nl.t64.game.rpg.screens.world.entity.Entity;
import nl.t64.game.rpg.screens.world.entity.GraphicsPlayer;
import nl.t64.game.rpg.screens.world.entity.InputPlayer;
import nl.t64.game.rpg.screens.world.entity.PhysicsPlayer;
import nl.t64.game.rpg.screens.world.entity.events.LoadEntityEvent;
import nl.t64.game.rpg.screens.world.entity.events.PathUpdateEvent;
import nl.t64.game.rpg.screens.world.messagedialog.MessageDialog;
import nl.t64.game.rpg.screens.world.pathfinding.TiledNode;
import nl.t64.game.rpg.sfx.TransitionImage;
import nl.t64.game.rpg.subjects.ComponentObserver;
import nl.t64.game.rpg.subjects.LootObserver;
import nl.t64.game.rpg.subjects.MapObserver;
import nl.t64.game.rpg.subjects.PartyObserver;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;


public class WorldScreen implements Screen,
                                    MapObserver, ComponentObserver, PartyObserver, LootObserver, ConversationObserver {

    private static boolean showGrid = false;
    private static boolean showObjects = false;
    private static boolean showDebug = false;

    private GameState previousGameState;
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
    private List<Entity> npcEntities;
    private Entity currentNpcEntity;
    private List<Entity> lootList;
    private List<Entity> doorList;

    public WorldScreen() {
        this.stage = new Stage();
        this.camera = new Camera();
        this.mapRenderer = new TextureMapObjectRenderer(this.camera);
        this.multiplexer = new InputMultiplexer();
        this.multiplexer.addProcessor(createListener());
        this.player = new Entity(Constant.PLAYER_ID,
                                 new InputPlayer(this.multiplexer), new PhysicsPlayer(), new GraphicsPlayer());
        this.npcEntities = new ArrayList<>(0);
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
        conversationDialog.conversationObservers.addObserver(this);
        Utils.getBrokerManager().componentObservers.addObserver(this);
        Utils.getBrokerManager().mapObservers.addObserver(this);
        Utils.getBrokerManager().partyObservers.addObserver(this);
        Utils.getBrokerManager().lootObservers.addObserver(this);
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
        npcEntities = new NpcEntitiesLoader(currentMap).createNpcs();
        lootList = new LootLoader(currentMap).createLoot();
        doorList = new DoorLoader(currentMap).createDoors();
        partyMembers = new PartyMembersLoader(player).loadPartyMembers();
        currentMap.setTiledGraph();
    }

    @Override
    public void onNotifyShakeCamera() {
        camera.startShaking();
    }

    // ComponentObserver ///////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    public void onNotifyShowConversationDialog(String conversationId, Entity npcEntity) {
        this.currentNpcEntity = npcEntity;
        player.resetInput();
        conversationDialog.loadConversation(conversationId, npcEntity.getId());
        conversationDialog.show();
    }

    @Override
    public void onNotifyShowConversationDialog(String conversationId, String entityId) {
        player.resetInput();
        conversationDialog.loadConversation(conversationId, entityId);
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
        lootList.forEach(observer -> Utils.getBrokerManager().actionObservers.removeObserver(observer));
        lootList.forEach(observer -> Utils.getBrokerManager().blockObservers.removeObserver(observer));
        lootList = new LootLoader(Utils.getMapManager().getCurrentMap()).createLoot();
    }

    @Override
    public void onNotifyRewardTaken() {
        String conversationId = currentNpcEntity.getConversationId();
        QuestGraph quest = Utils.getGameData().getQuests().getQuestById(conversationId);
        ConversationGraph conversation = Utils.getGameData().getConversations().getConversationById(conversationId);
        quest.finish();
        conversation.setCurrentPhraseId(Constant.PHRASE_ID_QUEST_FINISHED);
    }

    @Override
    public void onNotifyReceiveTaken() {
        String conversationId = currentNpcEntity.getConversationId();
        QuestGraph quest = Utils.getGameData().getQuests().getQuestById(conversationId);
        ConversationGraph conversation = Utils.getGameData().getConversations().getConversationById(conversationId);
        quest.accept(conversationDialog.conversationObservers);
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
        ShopScreen.load(currentNpcEntity.getId(), currentNpcEntity.getConversationId());
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
        Utils.getBrokerManager().blockObservers.removeObserver(currentNpcEntity);
        npcEntities = npcEntities.stream()
                                 .filter(npcEntity -> !npcEntity.equals(currentNpcEntity))
                                 .toList();
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
        switch (gameState) {
            case PAUSED -> { /* do nothing here*/ }
            case MINIMAP -> renderMiniMap();
            case RUNNING -> renderAll(dt);
        }
    }

    private void renderMiniMap() {
        updateCameraPosition();
        mapRenderer.renderMap();
        // todo, eventually remove shaperenderer and use sprite icons for minimap.
        shapeRenderer.setProjectionMatrix(camera.combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        player.renderOnMiniMap(mapRenderer.getBatch(), shapeRenderer);
        npcEntities.forEach(npc -> npc.renderOnMiniMap(mapRenderer.getBatch(), shapeRenderer));
        Utils.getMapManager().drawFogOfWar(shapeRenderer);
        shapeRenderer.end();
    }

    private void renderAll(float dt) {
        Utils.getMapManager().updateFogOfWar(player.getPosition(), dt);
        Utils.getMapManager().updateQuestLayers();
        updateEntities(dt);
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

    private void updateEntities(float dt) {
        if (!isInTransition()) {
            player.update(dt);
        }
        doorList.forEach(door -> door.update(dt));
        lootList.forEach(loot -> loot.update(dt));
        npcEntities.forEach(enemyNpc -> enemyNpc.send(new PathUpdateEvent(getPathOf(enemyNpc))));
        npcEntities.forEach(npcEntity -> npcEntity.update(dt));
        partyMembers.forEach(partyMember -> partyMember.send(new PathUpdateEvent(getPathOf(partyMember))));
        partyMembers.forEach(partyMember -> partyMember.update(dt));
    }

    private void updateCameraPosition() {
        camera.setPosition(player.getPosition());
        mapRenderer.updateCamera();
    }

    private void renderEntities() {
        lootList.forEach(loot -> loot.render(mapRenderer.getBatch()));
        doorList.stream()
                .filter(door -> door.getPosition().y >= player.getPosition().y)
                .forEach(door -> door.render(mapRenderer.getBatch()));

        List<Entity> allEntities = new ArrayList<>();
        allEntities.addAll(Utils.reverseList(partyMembers));
        allEntities.addAll(npcEntities);
        allEntities.add(player);
        allEntities.sort(Comparator.comparingDouble((Entity entity) -> entity.getPosition().y).reversed());
        allEntities.forEach(entity -> entity.render(mapRenderer.getBatch()));

        doorList.stream()
                .filter(door -> door.getPosition().y < player.getPosition().y)
                .forEach(door -> door.render(mapRenderer.getBatch()));
    }

    private DefaultGraphPath<TiledNode> getPathOf(Entity npc) {
        final Vector2 startPoint = npc.getPositionInGrid();
        final Vector2 endPoint = getEndPoint(npc);
        return Utils.getMapManager().getCurrentMap().tiledGraph.findPath(startPoint, endPoint);
    }

    private Vector2 getEndPoint(Entity npc) {
        int index = partyMembers.indexOf(npc);
        if (index <= 0) {
            return player.getPositionInGrid();
        } else {
            return partyMembers.get(index - 1).getPositionInGrid();
        }
    }

    public void doBeforeLoadScreen() {
        player.resetInput();
        render(0f);
    }

    private void showHidePartyWindow() {
        partyWindow.showHide();
    }

    private void openMiniMap() {
        if (camera.zoom()) {
            Utils.getAudioManager().handle(AudioCommand.SE_PLAY_ONCE, AudioEvent.SE_MINIMAP);
            gameState = GameState.MINIMAP;
            multiplexer.removeProcessor(0);
            multiplexer.addProcessor(0, new MiniMapListener(this::closeMiniMap));
        } else {
            Utils.getAudioManager().handle(AudioCommand.SE_PLAY_ONCE, AudioEvent.SE_MENU_ERROR);
        }
    }

    private void closeMiniMap() {
        Utils.getAudioManager().handle(AudioCommand.SE_PLAY_ONCE, AudioEvent.SE_MINIMAP);
        gameState = GameState.RUNNING;
        multiplexer.removeProcessor(0);
        multiplexer.addProcessor(0, createListener());
        camera.reset();
    }

    private WorldScreenListener createListener() {
        return new WorldScreenListener(this::doBeforeLoadScreen, this::showHidePartyWindow, this::openMiniMap);
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
        previousGameState = gameState;
        gameState = GameState.PAUSED;
    }

    @Override
    public void resume() {
        gameState = previousGameState;
    }

    @Override
    public void hide() {
        previousGameState = gameState;
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
        if (Utils.getPreferenceManager().isInDebugMode()) {
            showGrid = !showGrid;
        }
    }

    static void setShowObjects() {
        if (Utils.getPreferenceManager().isInDebugMode()) {
            showObjects = !showObjects;
        }
    }

    static void setShowDebug() {
        if (Utils.getPreferenceManager().isInDebugMode()) {
            showDebug = !showDebug;
        }
    }

    private void renderGrid() {
        if (showGrid) {
            shapeRenderer.setProjectionMatrix(camera.combined);
            shapeRenderer.setColor(Color.DARK_GRAY);
            shapeRenderer.begin(ShapeRenderer.ShapeType.Line);

            float mapPixelWidth = Utils.getMapManager().getCurrentMap().getPixelWidth();
            float mapPixelHeight = Utils.getMapManager().getCurrentMap().getPixelHeight();
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
            shapeRenderer.setProjectionMatrix(camera.combined);
            shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
            player.debug(shapeRenderer);
            doorList.forEach(door -> door.debug(shapeRenderer));
            lootList.forEach(loot -> loot.debug(shapeRenderer));
            npcEntities.forEach(npcEntity -> npcEntity.debug(shapeRenderer));
            partyMembers.forEach(partyMember -> partyMember.debug(shapeRenderer));
            Utils.getMapManager().getCurrentMap().debug(shapeRenderer);
            shapeRenderer.end();
        }
    }

    private void updateDebugBox(float dt) {
        if (showDebug) {
            debugBox.update(dt);
        }
    }

}
