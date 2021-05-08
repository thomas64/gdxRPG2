package nl.t64.game.rpg.screens.world.cutscene;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.badlogic.gdx.utils.ScreenUtils;
import nl.t64.game.rpg.Utils;
import nl.t64.game.rpg.audio.AudioCommand;
import nl.t64.game.rpg.constants.Constant;
import nl.t64.game.rpg.screens.world.Camera;
import nl.t64.game.rpg.screens.world.GameMap;
import nl.t64.game.rpg.screens.world.TextureMapObjectRenderer;
import nl.t64.game.rpg.screens.world.conversation.ConversationDialog;
import nl.t64.game.rpg.screens.world.conversation.ConversationObserver;
import nl.t64.game.rpg.screens.world.entity.*;
import nl.t64.game.rpg.sfx.TransitionAction;
import nl.t64.game.rpg.sfx.TransitionImage;
import nl.t64.game.rpg.sfx.TransitionType;

import java.util.List;


abstract class CutsceneScreen implements Screen, ConversationObserver {

    private final Camera camera;
    private final TextureMapObjectRenderer mapRenderer;
    final Stage actorsStage;
    final Stage transitionStage;
    final Actor transition;
    List<Action> actions;
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
        this.followingActor = new Actor();
        this.isCameraFixed = true;
    }

    @Override
    public void show() {
        conversationDialog = new ConversationDialog();
        conversationDialog.conversationObservers.addObserver(this);
        actionId = 0;

        transitionStage.clear();
        transitionStage.addActor(transition);

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

    Action getLastAction() {
        conversationDialog.conversationObservers.removeObserver(this);
        Gdx.input.setInputProcessor(null);
        Utils.setGamepadInputProcessor(null);
        return Actions.sequence(
                Actions.addAction(TransitionAction.transition(TransitionType.FADE_OUT), transition),
                Actions.delay(Constant.FADE_DURATION),
                Actions.delay(1f),
                Actions.addAction(Actions.alpha(1f), transition)
        );
    }

    Action actionMoveBy(CutsceneActor actor, Direction direction, float distance, float duration, float stepSpeed) {
        float x = 0f;
        float y = 0f;
        switch (direction) {
            case NORTH -> {
                x = 0f;
                y = distance;
            }
            case SOUTH -> {
                x = 0f;
                y = -distance;
            }
            case WEST -> {
            }
            case EAST -> {
            }
            case NONE -> throw new GdxRuntimeException("No action for direction NONE.");
        }

        return Actions.sequence(
                actionWalk(actor, direction),
                Actions.addAction(Actions.moveBy(x, y, duration), actor),
                actionRepeatWalkSound(actor, duration, stepSpeed)
        );
    }

    Action actionMoveTo(CutsceneActor actor, Direction direction, float x, float y, float duration, float stepSpeed) {
        return Actions.sequence(
                actionWalk(actor, direction),
                Actions.addAction(Actions.moveTo(x, y, duration), actor),
                actionRepeatWalkSound(actor, duration, stepSpeed)
        );
    }

    void setMap(String mapId) {
        var manager = Utils.getMapManager();
        manager.loadMap(mapId);
        GameMap currentMap = manager.getCurrentMap();
        mapRenderer.setMap(manager.getTiledMap());
        camera.setNewMapSize(currentMap.getPixelWidth(), currentMap.getPixelHeight());
    }

    CutsceneActor createActor(String actorId) {
        var entity = new Entity(actorId, new InputEmpty(), new PhysicsNpc(), new GraphicsNpc(actorId));
        return new CutsceneActor(entity, EntityState.IDLE, Direction.SOUTH);
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

    private Action actionWalk(CutsceneActor actor, Direction direction) {
        return Actions.run(() -> {
            actor.setEntityState(EntityState.WALKING);
            actor.setDirection(direction);
        });
    }

    private Action actionRepeatWalkSound(Actor actor, float duration, float stepSpeed) {
        return Actions.repeat(
                (int) (duration / stepSpeed), Actions.sequence(
                        Actions.run(() -> Utils.getAudioManager().handle(
                                AudioCommand.SE_PLAY_ONCE,
                                Utils.getMapManager().getGroundSound(actor.getX(), actor.getY()))),
                        Actions.delay(stepSpeed)
                ));
    }

}
