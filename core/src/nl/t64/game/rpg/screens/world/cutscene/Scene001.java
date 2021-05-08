package nl.t64.game.rpg.screens.world.cutscene;

import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import nl.t64.game.rpg.Utils;
import nl.t64.game.rpg.constants.Constant;
import nl.t64.game.rpg.constants.ScreenType;
import nl.t64.game.rpg.screens.world.entity.Direction;
import nl.t64.game.rpg.screens.world.entity.EntityState;
import nl.t64.game.rpg.sfx.TransitionAction;
import nl.t64.game.rpg.sfx.TransitionType;

import java.util.List;


public class Scene001 extends CutsceneScreen {

    private CutsceneActor luana;
    private CutsceneActor guard;

    @Override
    void prepare() {
        luana = createActor("luana");
        guard = createActor("soldier01");
        actorsStage.addActor(luana);
        actorsStage.addActor(guard);
        actions = List.of(sayHelloToPlayer(), talkToGuard(), runAway());
    }

    private Action sayHelloToPlayer() {
        return Actions.sequence(
                Actions.run(() -> {
                    setMap("starter_path");
                    setCameraPosition(0f, 0f);
                }),
                Actions.delay(1f),
                Actions.addAction(TransitionAction.transition(TransitionType.FADE_IN), transition),
                Actions.delay(Constant.FADE_DURATION),
                Actions.run(() -> {
                    luana.setPosition(480f, 550f);
                    luana.setVisible(true);
                }),
                Actions.delay(1f),
                actionMoveBy(luana, Direction.SOUTH, 250, 3f, 0.5f),
                Actions.run(() -> luana.setEntityState(EntityState.IDLE)),
                Actions.delay(1f),
                Actions.run(() -> showConversationDialog("cutscene01_1", "luana"))
        );
    }

    private Action talkToGuard() {
        return Actions.sequence(
                actionMoveBy(luana, Direction.SOUTH, 400f, 2f, 0.25f),
                Actions.addAction(TransitionAction.transition(TransitionType.FADE_OUT), transition),
                Actions.delay(Constant.FADE_DURATION),
                Actions.run(() -> {
                    setMap("hero_s_hometown");
                    luana.setPosition(1920f, 1872f);
                    followActor(luana);
                    guard.setPosition(1968f, 1704f);
                    guard.setVisible(true);
                    guard.setDirection(Direction.WEST);
                }),
                Actions.addAction(TransitionAction.transition(TransitionType.FADE_IN), transition),
                Actions.delay(Constant.FADE_DURATION),
                actionMoveTo(luana, Direction.SOUTH, 1920f, 1704f, 1f, 0.25f),
                Actions.run(() -> {
                    luana.setEntityState(EntityState.IDLE);
                    luana.setDirection(Direction.EAST);
                }),
                Actions.run(() -> showConversationDialog("cutscene01_2"))
        );
    }

    private Action runAway() {
        return Actions.sequence(
                actionMoveBy(luana, Direction.NORTH, 200f, 1f, 0.25f),
                Actions.run(this::exitScreen)
        );
    }

    @Override
    void exitScreen() {
        actorsStage.addAction(Actions.sequence(
                getLastAction(),
                Actions.run(() -> {
                    Utils.getMapManager().loadMapAfterCutscene("starter_path", "scene001");
                    Utils.getScreenManager().setScreen(ScreenType.WORLD);
                })
        ));
    }

}
