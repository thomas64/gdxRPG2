package nl.t64.game.rpg.screens.world.cutscene;

import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import nl.t64.game.rpg.Utils;
import nl.t64.game.rpg.audio.AudioCommand;
import nl.t64.game.rpg.audio.AudioEvent;
import nl.t64.game.rpg.constants.Constant;
import nl.t64.game.rpg.screens.world.entity.Direction;
import nl.t64.game.rpg.screens.world.entity.EntityState;
import nl.t64.game.rpg.sfx.TransitionAction;
import nl.t64.game.rpg.sfx.TransitionType;

import java.util.List;


public class SceneIntro extends CutsceneScreen {

    private CutsceneActor door1;
    private CutsceneActor mozes;
    private CutsceneActor grace;
    private CutsceneActor oldWoman;

    @Override
    void prepare() {
        door1 = CutsceneActor.createDoor("door_simple_left3");
        mozes = CutsceneActor.createCharacter("mozes");
        grace = CutsceneActor.createCharacter("girl01");
        oldWoman = CutsceneActor.createCharacter("oldwoman01");
        actorsStage.addActor(door1);
        actorsStage.addActor(mozes);
        actorsStage.addActor(grace);
        actorsStage.addActor(oldWoman);
        actions = List.of(callGraceToBed(),
                          walkIntoTheHouse(),
                          graceSneaksOutside(),
                          startToSearch(),
                          searchNorth(),
                          moveToFallenTree(),
                          startGame());
    }

    private Action callGraceToBed() {
        return Actions.sequence(
                Actions.run(() -> {
                    setMap("honeywood");
                    setCameraPosition(0f, 1380f);
                    title.setText("""
                                          The land of Adan
                                                                                
                                          Honeywood Village
                                                                                
                                          582 AD""");
                    title.setPosition(camera.position.x - (title.getWidth() / 2f), camera.position.y);
                    door1.setPosition(528f, 1344f);
                    mozes.setPosition(528f, 1344f);
                    grace.setPosition(200f, 1310f);
                    grace.setEntityState(EntityState.RUNNING);
                    grace.setVisible(true);
                }),
                Actions.delay(1f),
                Actions.addAction(Actions.sequence(
                        Actions.alpha(0f),
                        Actions.visible(true),
                        Actions.fadeIn(Constant.FADE_DURATION),
                        Actions.delay(5f),
                        Actions.fadeOut(Constant.FADE_DURATION),
                        Actions.visible(false),
                        Actions.alpha(1f)
                ), title),
                Actions.delay(7f),

                Actions.addAction(TransitionAction.transition(TransitionType.FADE_IN), transition),

                Actions.addAction(Actions.sequence(
                        Actions.parallel(
                                Actions.repeat(6, Actions.sequence(
                                        Actions.moveBy(0f, -48f, 0.5f),
                                        Actions.run(() -> grace.setDirection(Direction.EAST)),
                                        Actions.moveBy(48f, 0f, 0.5f),
                                        Actions.run(() -> grace.setDirection(Direction.NORTH)),
                                        Actions.moveBy(0f, 48f, 0.5f),
                                        Actions.run(() -> grace.setDirection(Direction.WEST)),
                                        Actions.moveBy(-48f, 0f, 0.5f),
                                        Actions.run(() -> grace.setDirection(Direction.SOUTH))
                                )),
                                actionWalkSound(grace, 12f, FAST_STEP)
                        ),
                        Actions.run(() -> grace.setEntityState(EntityState.IDLE)),
                        Actions.run(() -> grace.setDirection(Direction.EAST))
                ), grace),
                Actions.addAction(Actions.sequence(
                        Actions.delay(5f),
                        Actions.run(() -> {
                            door1.setEntityState(EntityState.OPENED);
                            Utils.getAudioManager().handle(AudioCommand.SE_PLAY_ONCE, AudioEvent.SE_SMALL_DOOR);
                        })
                ), door1),
                Actions.addAction(Actions.sequence(
                        Actions.delay(6f),
                        Actions.alpha(0f),
                        Actions.visible(true),
                        Actions.fadeIn(0.3f),
                        Actions.delay(0.5f),
                        Actions.run(() -> mozes.setEntityState(EntityState.WALKING)),
                        Actions.moveBy(0f, -24f, 1f),
                        Actions.run(() -> mozes.setEntityState(EntityState.IDLE)),
                        Actions.delay(0.3f),
                        Actions.run(() -> mozes.setDirection(Direction.EAST)),
                        Actions.delay(1f),
                        Actions.run(() -> mozes.setDirection(Direction.SOUTH)),
                        Actions.delay(1f),
                        Actions.run(() -> mozes.setDirection(Direction.WEST)),
                        Actions.delay(1f),
                        Actions.run(() -> showConversationDialog("cutscene_intro_1"))
                ), mozes)
        );
    }

    private Action walkIntoTheHouse() {
        return Actions.sequence(
                Actions.delay(1f),
                Actions.addAction(Actions.sequence(
                        Actions.run(() -> grace.setEntityState(EntityState.WALKING)),
                        actionMoveBy(grace, 100f, 0f, 5f, NORMAL_STEP),
                        Actions.run(() -> grace.setEntityState(EntityState.IDLE)),
                        Actions.delay(0.5f),
                        Actions.run(() -> grace.setDirection(Direction.WEST)),
                        Actions.delay(1f),
                        Actions.run(() -> grace.setDirection(Direction.EAST)),
                        Actions.delay(1f),
                        Actions.parallel(
                                Actions.sequence(
                                        Actions.run(() -> grace.setEntityState(EntityState.WALKING)),
                                        Actions.moveTo(528f, 1310f, 6f),
                                        Actions.run(() -> grace.setDirection(Direction.NORTH)),
                                        Actions.moveTo(528f, 1344f, 1f)
                                ),
                                actionWalkSound(grace, 7f, NORMAL_STEP)
                        ),
                        Actions.fadeOut(0.3f)
                ), grace),
                Actions.addAction(Actions.sequence(
                        Actions.delay(9f),
                        Actions.run(() -> mozes.setDirection(Direction.NORTH)),
                        Actions.run(() -> mozes.setEntityState(EntityState.WALKING)),
                        Actions.moveBy(0f, 24f, 1f),
                        Actions.fadeOut(0.3f)
                ), mozes),
                Actions.delay(16f),

                actionFadeOut(),

                Actions.delay(1f),
                Actions.run(() -> {
                    setMap("honeywood_mozes_house");
                    setCameraPosition(0f, 720f);
                    door1.setVisible(false);
                    mozes.setPosition(96f, 528f);
                    mozes.setEntityState(EntityState.IDLE);
                    mozes.setDirection(Direction.WEST);
                    grace.setPosition(48, 534f);
                    grace.setEntityState(EntityState.IDLE);
                    grace.setDirection(Direction.EAST);
                }),
                Actions.addAction(Actions.alpha(1f), mozes),
                Actions.addAction(Actions.alpha(1f), grace),

                actionFadeIn(),

                Actions.delay(1f),
                Actions.run(() -> showConversationDialog("cutscene_intro_2"))
        );
    }

    private Action graceSneaksOutside() {
        return Actions.sequence(
                Actions.delay(1.5f),

                actionFadeOut(),

                Actions.run(() -> {
                    mozes.setPosition(192f, 524f);
                    mozes.setDirection(Direction.SOUTH);
                    grace.setDirection(Direction.SOUTH);
                }),
                Actions.delay(2f),
                Actions.addAction(Actions.alpha(0.7f), transition),
                Actions.run(() -> Utils.getAudioManager().handle(AudioCommand.BGM_PLAY_ONCE, AudioEvent.BGM_CELLAR)),
                Actions.addAction(Actions.sequence(
                        Actions.delay(5f),
                        Actions.run(() -> grace.setDirection(Direction.EAST)),
                        Actions.delay(4f),
                        Actions.run(() -> grace.setEntityState(EntityState.WALKING)),
                        Actions.moveBy(48f, 0f, 2f),
                        Actions.run(() -> grace.setEntityState(EntityState.IDLE)),
                        Actions.delay(2f),
                        Actions.run(() -> grace.setDirection(Direction.SOUTH)),
                        Actions.run(() -> grace.setEntityState(EntityState.WALKING)),
                        Actions.moveBy(0f, -72f, 2f),
                        Actions.run(() -> grace.setDirection(Direction.EAST)),
                        Actions.moveBy(144f, 0f, 3.5f),
                        Actions.run(() -> grace.setEntityState(EntityState.IDLE)),
                        Actions.run(() -> grace.setDirection(Direction.NORTH)),
                        Actions.delay(3f),
                        Actions.run(() -> grace.setDirection(Direction.SOUTH)),
                        Actions.delay(0.5f),
                        Actions.run(() -> grace.setEntityState(EntityState.WALKING)),
                        Actions.moveBy(-48f, -360f, 7f),
                        Actions.run(() -> Utils.getAudioManager().handle(AudioCommand.SE_PLAY_ONCE, AudioEvent.SE_SMALL_DOOR)),
                        Actions.delay(3f),
                        Actions.visible(false)
                ), grace),
                Actions.delay(32f),

                actionFadeOut(),

                Actions.delay(1f),
                Actions.run(() -> {
                    Utils.getAudioManager().handle(AudioCommand.BGM_PLAY_LOOP, AudioEvent.BGM_HOUSE);
                    mozes.setPosition(192f, 534f);
                    followActor(mozes);
                }),

                actionFadeIn(),

                Actions.addAction(Actions.sequence(
                        Actions.delay(2f),
                        Actions.run(() -> mozes.setDirection(Direction.EAST)),
                        Actions.run(() -> mozes.setEntityState(EntityState.WALKING)),
                        Actions.moveBy(48f, 0f, 2f),
                        Actions.run(() -> mozes.setEntityState(EntityState.IDLE)),
                        Actions.run(() -> mozes.setDirection(Direction.WEST)),
                        Actions.run(() -> showConversationDialog("cutscene_intro_3", "mozes"))
                ), mozes)
        );
    }

    private Action startToSearch() {
        return Actions.sequence(
                Actions.addAction(Actions.sequence(
                        Actions.run(() -> mozes.setDirection(Direction.NORTH)),
                        Actions.delay(0.5f),
                        Actions.run(() -> Utils.getAudioManager().handle(AudioCommand.SE_PLAY_ONCE, AudioEvent.SE_CHEST)),
                        Actions.delay(2f),
                        Actions.parallel(
                                Actions.sequence(
                                        Actions.run(() -> mozes.setDirection(Direction.SOUTH)),
                                        Actions.run(() -> mozes.setEntityState(EntityState.RUNNING)),
                                        Actions.moveBy(0f, -72f, 0.5f),
                                        Actions.moveBy(-48f, -400f, 2f),
                                        Actions.run(() -> Utils.getAudioManager().handle(AudioCommand.SE_PLAY_ONCE, AudioEvent.SE_SMALL_DOOR)),
                                        Actions.moveBy(0f, -20f, 0.1f)
                                ),
                                actionWalkSound(mozes, 2.6f, FAST_STEP)
                        )
                ), mozes),
                Actions.delay(5.5f),

                actionFadeOut(),

                Actions.run(() -> {
                    setMap("honeywood");
                    door1.setVisible(true);
                    oldWoman.setVisible(true);
                    oldWoman.setPosition(1800f, 1340f);
                    oldWoman.setDirection(Direction.EAST);
                    mozes.setPosition(528f, 1320f);
                    mozes.setEntityState(EntityState.IDLE);
                }),
                Actions.delay(1f),

                actionFadeIn(),

                Actions.addAction(Actions.sequence(
                        Actions.run(() -> mozes.setDirection(Direction.WEST)),
                        Actions.delay(0.3f),
                        Actions.run(() -> mozes.setDirection(Direction.EAST)),
                        Actions.delay(0.3f),
                        Actions.run(() -> mozes.setDirection(Direction.WEST)),
                        Actions.delay(0.3f),
                        Actions.run(() -> mozes.setDirection(Direction.SOUTH)),
                        Actions.delay(0.3f),
                        Actions.run(() -> mozes.setDirection(Direction.EAST)),
                        Actions.delay(0.3f),
                        Actions.run(() -> mozes.setDirection(Direction.WEST)),
                        Actions.delay(0.3f),
                        Actions.run(() -> mozes.setEntityState(EntityState.RUNNING)),
                        actionMoveBy(mozes, -96f, 0f, 0.5f, FAST_STEP),
                        Actions.run(() -> mozes.setEntityState(EntityState.IDLE)),
                        Actions.run(() -> mozes.setDirection(Direction.NORTH)),
                        Actions.delay(1f),
                        Actions.run(() -> mozes.setDirection(Direction.SOUTH)),
                        Actions.delay(0.3f),
                        Actions.run(() -> mozes.setDirection(Direction.WEST)),
                        Actions.delay(0.3f),
                        Actions.run(() -> mozes.setDirection(Direction.EAST)),
                        Actions.delay(0.3f),
                        Actions.run(() -> mozes.setDirection(Direction.NORTH)),
                        Actions.delay(0.3f),
                        Actions.run(() -> mozes.setEntityState(EntityState.RUNNING)),
                        actionMoveBy(mozes, 0f, 288f, 1f, FAST_STEP),
                        Actions.run(() -> mozes.setEntityState(EntityState.IDLE)),
                        Actions.run(() -> mozes.setDirection(Direction.EAST)),
                        Actions.delay(1f),
                        Actions.run(() -> mozes.setDirection(Direction.WEST)),
                        Actions.delay(0.3f),
                        Actions.run(() -> mozes.setDirection(Direction.EAST)),
                        Actions.delay(0.3f),
                        Actions.run(() -> mozes.setEntityState(EntityState.RUNNING)),
                        Actions.parallel(
                                Actions.sequence(
                                        Actions.moveBy(360f, 0f, 1.3f),
                                        Actions.moveBy(400f, -192f, 1.5f),
                                        Actions.moveBy(500f, 0f, 2f)
                                ),
                                actionWalkSound(mozes, 4.8f, FAST_STEP)
                        ),
                        Actions.run(() -> mozes.setEntityState(EntityState.IDLE)),
                        Actions.run(() -> showConversationDialog("cutscene_intro_4")),
                        Actions.delay(1f),
                        Actions.run(() -> oldWoman.setDirection(Direction.WEST))
                ), mozes)
        );
    }

    private Action searchNorth() {
        return Actions.sequence(
                Actions.addAction(Actions.sequence(
                        Actions.run(() -> oldWoman.setDirection(Direction.NORTH)),
                        Actions.delay(2f),
                        Actions.run(() -> oldWoman.setDirection(Direction.SOUTH)),
                        Actions.run(() -> oldWoman.setEntityState(EntityState.WALKING)),
                        Actions.moveBy(0f, -100f, 10f),
                        Actions.visible(false)
                ), oldWoman),
                Actions.addAction(Actions.parallel(
                        Actions.sequence(
                                Actions.run(() -> mozes.setEntityState(EntityState.RUNNING)),
                                Actions.moveBy(230f, 200f, 1.3f),
                                Actions.run(() -> mozes.setDirection(Direction.NORTH)),
                                Actions.moveBy(0f, 300f, 1.3f)
                        ),
                        actionWalkSound(mozes, 2.6f, FAST_STEP)
                ), mozes),
                Actions.delay(3.5f),

                actionFadeOut(),

                Actions.run(() -> {
                    setMap("honeywood_forest_path");
                    mozes.setPosition(480f, -48f);
                }),
                Actions.delay(1f),

                actionFadeIn(),

                Actions.addAction(Actions.sequence(
                        actionMoveBy(mozes, 0f, 500f, 2f, FAST_STEP),
                        Actions.run(() -> mozes.setEntityState(EntityState.IDLE)),
                        Actions.delay(1f),
                        Actions.run(() -> mozes.setDirection(Direction.SOUTH)),
                        Actions.delay(1f),
                        Actions.run(() -> showConversationDialog("cutscene_intro_5", "mozes"))
                ), mozes),
                Actions.addAction(Actions.sequence(
                        Actions.delay(1.3f),
                        Actions.run(() -> Utils.getAudioManager().handle(AudioCommand.SE_PLAY_ONCE, AudioEvent.SE_BANG)),
                        Actions.delay(0.5f),
                        Actions.run(camera::startShaking),
                        Actions.run(() -> Utils.getMapManager().updateQuestLayers())
                ))
        );
    }

    private Action moveToFallenTree() {
        return Actions.sequence(
                Actions.addAction(Actions.sequence(
                        Actions.delay(1f),
                        Actions.run(() -> mozes.setEntityState(EntityState.WALKING)),
                        actionMoveTo(mozes, 480f, 60f, 5f, NORMAL_STEP),
                        Actions.run(() -> mozes.setEntityState(EntityState.IDLE)),
                        Actions.delay(1f),
                        Actions.run(() -> showConversationDialog("cutscene_intro_6", "mozes"))
                ), mozes)
        );
    }

    private Action startGame() {
        return Actions.sequence(
                Actions.delay(0.5f),
                Actions.run(this::exitScreen)
        );
    }

    @Override
    void exitScreen() {
        actorsStage.addAction(getLastAction("honeywood_forest_path", "scene_intro"));
    }

}
