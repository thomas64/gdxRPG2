package nl.t64.game.rpg.screens.questlog;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import nl.t64.game.rpg.Utils;
import nl.t64.game.rpg.constants.ScreenType;
import nl.t64.game.rpg.screens.ScreenToLoad;
import nl.t64.game.rpg.screens.inventory.ListenerMouseImageButton;


public class QuestLogScreen implements ScreenToLoad {

    private static final String BUTTON_CLOSE_UP = "close_up";
    private static final String BUTTON_CLOSE_OVER = "close_over";
    private static final String BUTTON_CLOSE_DOWN = "close_down";

    private static final float QUESTS_WINDOW_POSITION_X = 63f;
    private static final float QUESTS_WINDOW_POSITION_Y = 50f;
    private static final float SUMMARY_WINDOW_POSITION_X = 18f;
    private static final float SUMMARY_WINDOW_POSITION_Y = 834f;
    private static final float TASKS_WINDOW_POSITION_X = 18f;
    private static final float TASKS_WINDOW_POSITION_Y = 50f;

    private static final float BUTTON_SIZE = 32f;
    private static final float BUTTON_SPACE = 5f;
    private static final float RIGHT_SPACE = 25f;
    private static final float TOP_SPACE = 50f;

    private final Stage stage;
    private QuestLogUI questLogUI;

    public QuestLogScreen() {
        this.stage = new Stage();
    }

    public static void load() {
        Utils.getScreenManager().openParchmentLoadScreen(ScreenType.QUEST_LOG);
    }

    @Override
    public void show() {
        Gdx.input.setCursorCatched(false);
        Gdx.input.setInputProcessor(stage);
        stage.addListener(new QuestLogScreenListener(this::closeScreen));
        createButtonTable();

        questLogUI = new QuestLogUI();
        questLogUI.questListWindow.setPosition(QUESTS_WINDOW_POSITION_X, QUESTS_WINDOW_POSITION_Y);
        questLogUI.summaryWindow.setPosition((Gdx.graphics.getWidth() / 2f) + SUMMARY_WINDOW_POSITION_X,
                                             SUMMARY_WINDOW_POSITION_Y);
        questLogUI.taskListWindow.setPosition((Gdx.graphics.getWidth() / 2f) + TASKS_WINDOW_POSITION_X,
                                              TASKS_WINDOW_POSITION_Y);
        questLogUI.addToStage(stage);
        questLogUI.applyListeners(stage);
        stage.setKeyboardFocus(questLogUI.questListTable.questList);
        stage.setScrollFocus(questLogUI.questListTable.scrollPane);
    }

    @Override
    public void render(float dt) {
        Gdx.gl.glClearColor(0f, 0f, 0f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act(dt);
        stage.draw();
        questLogUI.update();
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
        questLogUI.unloadAssets();
        stage.clear();
        Gdx.input.setInputProcessor(null);
    }

    @Override
    public void dispose() {
        // todo, de buttons bewaren hun mouse-over state op de een of andere vage manier.
        stage.clear();
        stage.dispose();
    }

    @Override
    public void setBackground(Image screenshot, Image parchment) {
        stage.addActor(screenshot);
        stage.addActor(parchment);
    }

    private void createButtonTable() {
        var closeButton = createImageButton(BUTTON_CLOSE_UP, BUTTON_CLOSE_OVER, BUTTON_CLOSE_DOWN);
        closeButton.addListener(new ListenerMouseImageButton(this::closeScreen));

        var buttonTable = new Table();
        buttonTable.add(closeButton).size(BUTTON_SIZE).spaceBottom(BUTTON_SPACE).row();
        buttonTable.pack();
        buttonTable.setPosition(Gdx.graphics.getWidth() - buttonTable.getWidth() - RIGHT_SPACE,
                                Gdx.graphics.getHeight() - buttonTable.getHeight() - TOP_SPACE);
        stage.addActor(buttonTable);
    }

    private void closeScreen() {
        Utils.getScreenManager().setScreen(ScreenType.WORLD);
        Gdx.input.setCursorCatched(true);
    }

    private ImageButton createImageButton(String up, String over, String down) {
        var buttonStyle = new ImageButton.ImageButtonStyle();
        buttonStyle.up = createDrawable(up);
        buttonStyle.down = createDrawable(down);
        buttonStyle.over = createDrawable(over);
        return new ImageButton(buttonStyle);
    }

    private NinePatchDrawable createDrawable(String atlasId) {
        var textureRegion = Utils.getResourceManager().getAtlasTexture(atlasId);
        var ninePatch = new NinePatch(textureRegion, 1, 1, 1, 1);
        return new NinePatchDrawable(ninePatch);
    }

}
