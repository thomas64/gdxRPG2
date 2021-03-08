package nl.t64.game.rpg.screens.questlog;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.ScreenUtils;
import nl.t64.game.rpg.Utils;
import nl.t64.game.rpg.audio.AudioCommand;
import nl.t64.game.rpg.audio.AudioEvent;
import nl.t64.game.rpg.constants.ScreenType;
import nl.t64.game.rpg.screens.ScreenToLoad;


public class QuestLogScreen extends ScreenToLoad {

    private static final float QUESTS_WINDOW_POSITION_X = 63f;
    private static final float QUESTS_WINDOW_POSITION_Y = 50f;
    private static final float SUMMARY_WINDOW_POSITION_X = 18f;
    private static final float SUMMARY_WINDOW_POSITION_Y = 834f;
    private static final float TASKS_WINDOW_POSITION_X = 18f;
    private static final float TASKS_WINDOW_POSITION_Y = 50f;
    private static final float LABEL_PADDING_BOTTOM = 26f;

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
        ScreenUtils.clear(Color.BLACK);
        stage.act(dt);
        stage.draw();
    }

    @Override
    public void hide() {
        stage.clear();
    }

    @Override
    public void dispose() {
        stage.dispose();
    }

    private void closeScreen() {
        Gdx.input.setInputProcessor(null);
        Utils.setGamepadInputProcessor(null);
        Utils.getAudioManager().handle(AudioCommand.SE_PLAY_ONCE, AudioEvent.SE_SCROLL);
        fadeParchment();
    }

    private String createText() {
        if (Utils.isGamepadConnected()) {
            return "[L1] Page-up      [R1] Page-down      [B] Back";
        } else {
            return "[Left] Page-up      [Right] Page-down      [L] Back";
        }
    }

}
