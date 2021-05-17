package nl.t64.game.rpg.screens.world.cutscene;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.ScreenUtils;
import nl.t64.game.rpg.Utils;
import nl.t64.game.rpg.audio.AudioCommand;
import nl.t64.game.rpg.constants.Constant;
import nl.t64.game.rpg.constants.ScreenType;
import nl.t64.game.rpg.screens.world.Camera;
import nl.t64.game.rpg.screens.world.GameMap;
import nl.t64.game.rpg.screens.world.TextureMapObjectRenderer;
import nl.t64.game.rpg.screens.world.conversation.ConversationDialog;
import nl.t64.game.rpg.screens.world.conversation.ConversationObserver;
import nl.t64.game.rpg.sfx.TransitionAction;
import nl.t64.game.rpg.sfx.TransitionImage;
import nl.t64.game.rpg.sfx.TransitionType;

import java.util.List;


abstract class CutsceneScreen implements Screen, ConversationObserver {

    private static final String TITLE_FONT = "fonts/spectral_regular_24.ttf";
    private static final int FONT_SIZE = 24;

    static final float NORMAL_STEP = 0.5f;
    static final float FAST_STEP = 0.25f;

    private final TextureMapObjectRenderer mapRenderer;
    final Camera camera;
    final Stage actorsStage;
    final Stage transitionStage;
    final Actor transition;
    final Label title;
    List<Action> actions;
    boolean isBgmFading;
    private ConversationDialog conversationDialog;
    private int actionId;
    private Actor followingActor;
    private boolean isCameraFixed;

    CutsceneScreen() {
        this.camera = new Camera();
        this.mapRenderer = new TextureMapObjectRenderer(this.camera);
        this.actorsStage = new Stage(this.camera.getViewport());
        this.transitionStage = new Stage(this.camera.getViewport());
        this.transition = new TransitionImage();
        this.title = this.createTitle();
        this.followingActor = new Actor();
        this.isCameraFixed = true;
        this.isBgmFading = false;
    }

    private Label createTitle() {
        var font = Utils.getResourceManager().getTrueTypeAsset(TITLE_FONT, FONT_SIZE);
        var style = new Label.LabelStyle(font, Color.WHITE);
        var label = new Label("", style);
        label.setAlignment(Align.center);
        label.setVisible(false);
        return label;
    }

    @Override
    public void show() {
        conversationDialog = new ConversationDialog();
        conversationDialog.conversationObservers.addObserver(this);
        actionId = 0;

        transitionStage.clear();
        transitionStage.addActor(transition);
        transitionStage.addActor(title);

        actorsStage.clear();
        Gdx.input.setInputProcessor(actorsStage);
        Utils.setGamepadInputProcessor(actorsStage);
        actorsStage.addListener(new CutSceneListener(this::exitScreen));

        prepare();
        actorsStage.addAction(actions.get(actionId));
    }

    @Override
    public void render(float dt) {
        ScreenUtils.clear(Color.BLACK);

        if (isCameraFixed) {
            camera.update();
        } else {
            camera.setPosition(followingActor.getX(), followingActor.getY());
        }

        mapRenderer.updateCamera();
        actorsStage.act(dt);
        mapRenderer.renderAll(new Vector2(followingActor.getX(), followingActor.getY()), actorsStage::draw);

        conversationDialog.update(dt);

        transitionStage.act(dt);
        if (isBgmFading) {
            Utils.getAudioManager().fadeBgmBgs();
        }
        transitionStage.draw();
    }

    @Override
    public void resize(int width, int height) {
        // empty
    }

    @Override
    public void pause() {
        // empty
    }

    @Override
    public void resume() {
        // empty
    }

    @Override
    public void hide() {
        // empty
    }

    @Override
    public void dispose() {
        mapRenderer.dispose();
        conversationDialog.dispose();
        actorsStage.dispose();
        transitionStage.dispose();
    }

    @Override
    public void onNotifyExitConversation() {
        conversationDialog.hideWithFade();
        Gdx.input.setInputProcessor(actorsStage);
        Utils.setGamepadInputProcessor(actorsStage);
        actionId++;
        actorsStage.addAction(actions.get(actionId));
    }

    abstract void prepare();

    abstract void exitScreen();

    Action getLastAction(String mapTitle, String cutsceneId) {
        conversationDialog.conversationObservers.removeObserver(this);
        Gdx.input.setInputProcessor(null);
        Utils.setGamepadInputProcessor(null);
        return Actions.sequence(
                Actions.addAction(TransitionAction.transition(TransitionType.FADE_OUT), transition),
                Actions.delay(Constant.FADE_DURATION),
                Actions.delay(1f),
                Actions.addAction(Actions.alpha(1f), transition),
                Actions.run(() -> {
                    Utils.getMapManager().loadMapAfterCutscene(mapTitle, cutsceneId);
                    Utils.getScreenManager().setScreen(ScreenType.WORLD);
                })
        );
    }

    void setMap(String mapId) {
        var manager = Utils.getMapManager();
        manager.loadMap(mapId);
        GameMap currentMap = manager.getCurrentMap();
        mapRenderer.setMap(manager.getTiledMap());
        camera.setNewMapSize(currentMap.getPixelWidth(), currentMap.getPixelHeight());
    }

    void showConversationDialog(String conversationId) {
        conversationDialog.loadConversation(conversationId);
        conversationDialog.show();
    }

    void showConversationDialog(String conversationId, String entityId) {
        conversationDialog.loadConversation(conversationId, entityId);
        conversationDialog.show();
    }

    void followActor(Actor actor) {
        followingActor = actor;
        isCameraFixed = false;
    }

    void setCameraPosition(float x, float y) {
        camera.setPosition(x, y);
        isCameraFixed = true;
    }

    Action actionFadeIn() {
        return Actions.sequence(
                Actions.addAction(TransitionAction.transition(TransitionType.FADE_IN), transition),
                Actions.delay(Constant.FADE_DURATION)
        );
    }

    Action actionFadeOut() {
        return Actions.sequence(
                Actions.run(() -> isBgmFading = true),
                Actions.addAction(TransitionAction.transition(TransitionType.FADE_OUT), transition),
                Actions.delay(Constant.FADE_DURATION),
                Actions.run(() -> isBgmFading = false)
        );
    }

    Action actionMoveTo(CutsceneActor actor, float x, float y, float duration, float stepSpeed) {
        return Actions.parallel(
                Actions.moveTo(x, y, duration),
                actionWalkSound(actor, duration, stepSpeed)
        );
    }

    Action actionMoveBy(CutsceneActor actor, float amountX, float amountY, float duration, float stepSpeed) {
        return Actions.parallel(
                Actions.moveBy(amountX, amountY, duration),
                actionWalkSound(actor, duration, stepSpeed)
        );
    }

    Action actionWalkSound(CutsceneActor actor, float duration, float stepSpeed) {
        return Actions.repeat(
                (int) (duration / stepSpeed), Actions.sequence(
                        Actions.run(() -> Utils.getAudioManager().handle(
                                AudioCommand.SE_PLAY_ONCE,
                                Utils.getMapManager().getGroundSound(actor.getX(), actor.getY()))),
                        Actions.delay(stepSpeed)
                ));
    }

}
