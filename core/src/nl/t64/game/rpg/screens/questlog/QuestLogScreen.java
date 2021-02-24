package nl.t64.game.rpg.screens.questlog;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import nl.t64.game.rpg.Utils;
import nl.t64.game.rpg.audio.AudioCommand;
import nl.t64.game.rpg.audio.AudioEvent;
import nl.t64.game.rpg.constants.Constant;
import nl.t64.game.rpg.constants.ScreenType;
import nl.t64.game.rpg.screens.ScreenToLoad;


public class QuestLogScreen implements ScreenToLoad {

    private static final float QUESTS_WINDOW_POSITION_X = 63f;
    private static final float QUESTS_WINDOW_POSITION_Y = 50f;
    private static final float SUMMARY_WINDOW_POSITION_X = 18f;
    private static final float SUMMARY_WINDOW_POSITION_Y = 834f;
    private static final float TASKS_WINDOW_POSITION_X = 18f;
    private static final float TASKS_WINDOW_POSITION_Y = 50f;
    private static final float LABEL_PADDING_BOTTOM = 26f;

    private final Stage stage;

    public QuestLogScreen() {
        this.stage = new Stage();
    }

    public static void load() {
        Utils.getAudioManager().handle(AudioCommand.SE_PLAY_ONCE, AudioEvent.SE_SCROLL);
        Utils.getScreenManager().openParchmentLoadScreen(ScreenType.QUEST_LOG);
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
        Utils.setGamepadInputProcessor(stage);
        stage.addListener(new QuestLogScreenListener(this::closeScreen));

        var questLogUI = new QuestLogUI();
        questLogUI.questListWindow.setPosition(QUESTS_WINDOW_POSITION_X, QUESTS_WINDOW_POSITION_Y);
        questLogUI.summaryWindow.setPosition((Gdx.graphics.getWidth() / 2f) + SUMMARY_WINDOW_POSITION_X,
                                             SUMMARY_WINDOW_POSITION_Y);
        questLogUI.taskListWindow.setPosition((Gdx.graphics.getWidth() / 2f) + TASKS_WINDOW_POSITION_X,
                                              TASKS_WINDOW_POSITION_Y);
        questLogUI.addToStage(stage);
        questLogUI.applyListeners();

        var buttonLabel = new Label(createText(), new Label.LabelStyle(new BitmapFont(), Color.BLACK));
        buttonLabel.setPosition((QUESTS_WINDOW_POSITION_X + (questLogUI.questListWindow.getWidth() / 2f))
                                - (buttonLabel.getWidth() / 2f), LABEL_PADDING_BOTTOM);
        stage.addActor(buttonLabel);

        stage.setKeyboardFocus(questLogUI.questListTable.questList);
        stage.setScrollFocus(questLogUI.questListTable.scrollPane);
    }

    @Override
    public void render(float dt) {
        Gdx.gl.glClearColor(0f, 0f, 0f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act(dt);
        stage.draw();
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
        stage.clear();
    }

    @Override
    public void dispose() {
        stage.dispose();
    }

    @Override
    public void setBackground(Image screenshot, Image parchment) {
        stage.addActor(screenshot);
        stage.addActor(parchment);
    }

    private void closeScreen() {
        Gdx.input.setInputProcessor(null);
        Utils.setGamepadInputProcessor(null);
        Utils.getAudioManager().handle(AudioCommand.SE_PLAY_ONCE, AudioEvent.SE_SCROLL);
        fadeParchment();
    }

    private void fadeParchment() {
        var screenshot = (Image) stage.getActors().get(0);
        var parchment = (Image) stage.getActors().get(1);
        stage.clear();
        setBackground(screenshot, parchment);
        parchment.addAction(Actions.sequence(Actions.fadeOut(Constant.FADE_DURATION),
                                             Actions.run(() -> Utils.getScreenManager().setScreen(ScreenType.WORLD))));
    }

    private String createText() {
        if (Utils.isGamepadConnected()) {
            return "[L1] Page-up      [R1] Page-down      [B] Back";
        } else {
            return "[Left] Page-up      [Right] Page-down      [L] Back";
        }
    }

}
