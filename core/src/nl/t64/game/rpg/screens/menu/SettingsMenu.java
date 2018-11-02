package nl.t64.game.rpg.screens.menu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import lombok.Setter;
import nl.t64.game.rpg.GdxRpg2;
import nl.t64.game.rpg.Utility;
import nl.t64.game.rpg.constants.Constant;
import nl.t64.game.rpg.listeners.ButtonMouseListener;
import nl.t64.game.rpg.listeners.ConfirmKeyListener;
import nl.t64.game.rpg.listeners.VerticalKeyListener;


public class SettingsMenu implements Screen {

    private static final String MENU_FONT = "fonts/fff_tusj.ttf";
    private static final int MENU_SIZE = 30;
    private static final int MENU_SPACE_BOTTOM = 10;

    private static final String MENU_ITEM_FULL_SCREEN = "Toggle full screen";
    private static final String MENU_ITEM_BACK = "Back";

    private static final int NUMBER_OF_ITEMS = 2;
    private static final int EXIT_INDEX = 1;

    @Setter
    private Screen fromScreen;

    private Stage stage;

    private Image screenshot;
    private Image blur;

    private BitmapFont menuFont;
    private Table table;
    private TextButton fullScreenButton;
    private TextButton backButton;

    private VerticalKeyListener verticalKeyListener;

    private int selectedIndex;
    private boolean isFullScreen;

    public SettingsMenu() {
        this.stage = new Stage();
        createFonts();
        this.selectedIndex = 1;
        this.isFullScreen = false;
    }

    public void setBackground(Image screenshot, Image blur) {
        this.screenshot = screenshot;
        this.blur = blur;
        stage.addActor(screenshot);
        stage.addActor(blur);
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
        setupScreen();
    }

    private void setupScreen() {
        this.table = createTable();
        applyListeners();
        this.stage.addActor(this.table);
        this.stage.setKeyboardFocus(this.table);
        setCurrentTextButtonToRed();
    }

    @Override
    public void render(float dt) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act(dt);
        verticalKeyListener.updateSelectedIndex(selectedIndex);
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().setScreenSize(width, height);
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
        Gdx.input.setInputProcessor(null);
    }

    @Override
    public void dispose() {
        // menuFont.dispose(); is already disposed in MainMenu?
        stage.clear();
        stage.dispose();
    }

    private void updateIndex(Integer newIndex) {
        selectedIndex = newIndex;
        setAllTextButtonsToWhite();
        setCurrentTextButtonToRed();
    }

    private void selectMenuItem() {
        switch (selectedIndex) {
            case 0:
                processToggleButton();
                break;
            case 1:
                processBackButton();
                break;
            default:
                throw new IllegalArgumentException("SelectedIndex not found.");
        }
    }

    private void processToggleButton() {
        if (isFullScreen) {
            Gdx.graphics.setWindowedMode(Constant.SCREEN_WIDTH, Constant.SCREEN_HEIGHT);
        } else {
            Gdx.graphics.setFullscreenMode(Gdx.graphics.getDisplayMode());
        }
        isFullScreen = !isFullScreen;
    }

    private void processBackButton() {
        if (fromScreen instanceof PauseMenu) {
            ((PauseMenu) fromScreen).setBackground(screenshot, blur);
        }
        GdxRpg2.getInstance().setScreen(fromScreen);
        fromScreen = null;
    }

    private void setAllTextButtonsToWhite() {
        for (Actor actor : table.getChildren()) {
            ((TextButton) actor).getStyle().fontColor = Color.WHITE;
        }
    }

    private void setCurrentTextButtonToRed() {
        ((TextButton) table.getChildren().get(selectedIndex)).getStyle().fontColor = Constant.DARK_RED;
    }

    private void createFonts() {
        Utility.loadTrueTypeAsset(MENU_FONT, MENU_SIZE);
        menuFont = Utility.getTrueTypeAsset(MENU_FONT);
    }

    private Table createTable() {
        // styles
        TextButton.TextButtonStyle buttonStyle = new TextButton.TextButtonStyle();
        buttonStyle.font = menuFont;
        buttonStyle.fontColor = Color.WHITE;

        // actors
        fullScreenButton = new TextButton(MENU_ITEM_FULL_SCREEN, new TextButton.TextButtonStyle(buttonStyle));
        backButton = new TextButton(MENU_ITEM_BACK, new TextButton.TextButtonStyle(buttonStyle));

        // table
        Table newTable = new Table();
        newTable.setFillParent(true);
        newTable.add(fullScreenButton).spaceBottom(MENU_SPACE_BOTTOM).row();
        newTable.add(backButton).spaceBottom(MENU_SPACE_BOTTOM).row();
        return newTable;
    }

    private void applyListeners() {
        verticalKeyListener = new VerticalKeyListener(this::updateIndex, NUMBER_OF_ITEMS);
        table.addListener(verticalKeyListener);
        table.addListener(new ConfirmKeyListener(this::updateIndex, this::selectMenuItem, EXIT_INDEX));
        fullScreenButton.addListener(createButtonMouseListener(0));
        backButton.addListener(createButtonMouseListener(1));
    }

    private ButtonMouseListener createButtonMouseListener(int index) {
        return new ButtonMouseListener(this::updateIndex, this::selectMenuItem, index);
    }

}
