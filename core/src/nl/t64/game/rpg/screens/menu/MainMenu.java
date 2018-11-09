package nl.t64.game.rpg.screens.menu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import nl.t64.game.rpg.Engine;
import nl.t64.game.rpg.Utility;
import nl.t64.game.rpg.constants.Constant;
import nl.t64.game.rpg.listeners.ButtonMouseListener;
import nl.t64.game.rpg.listeners.ConfirmKeyListener;
import nl.t64.game.rpg.listeners.VerticalKeyListener;


public class MainMenu implements Screen {

    private static final String TITLE_FONT = "fonts/colonna.ttf";
    private static final int TITLE_SIZE = 200;
    private static final int TITLE_SPACE_BOTTOM = 75;

    private static final String MENU_FONT = "fonts/fff_tusj.ttf";
    private static final int MENU_SIZE = 30;
    private static final int MENU_SPACE_BOTTOM = 10;

    private static final String TITLE_LABEL = "gdxRPG2";

    private static final String MENU_ITEM_NEW_GAME = "New Game";
    private static final String MENU_ITEM_LOAD_GAME = "Load Game";
    private static final String MENU_ITEM_SETTINGS = "Settings";
    private static final String MENU_ITEM_EXIT = "Exit";

    private static final int NUMBER_OF_ITEMS = 4;
    private static final int EXIT_INDEX = 3;

    private Engine engine;
    private Stage stage;

    private BitmapFont titleFont;
    private BitmapFont menuFont;
    private Table table;
    private TextButton newGameButton;
    private TextButton loadGameButton;
    private TextButton settingsButton;
    private TextButton exitButton;

    private VerticalKeyListener verticalKeyListener;

    private int selectedIndex;

    public MainMenu(Engine engine) {
        this.engine = engine;
        this.stage = new Stage();

        createFonts();
        this.table = createTable();

        applyListeners();
        this.stage.addActor(this.table);
        this.stage.setKeyboardFocus(this.table);

        this.selectedIndex = 0;
        setCurrentTextButtonToRed();
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
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
        Gdx.input.setInputProcessor(null);
    }

    @Override
    public void dispose() {
        titleFont.dispose();
        menuFont.dispose();
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
                engine.setScreen(engine.getNewGameMenuScreen());
                break;
            case 1:
                engine.setScreen(engine.getLoadGameMenuScreen());
                engine.getLoadGameMenuScreen().setFromScreen(this);
                break;
            case 2:
                engine.setScreen(engine.getSettingsMenuScreen());
                engine.getSettingsMenuScreen().setFromScreen(this);
                break;
            case 3:
                Gdx.app.exit();
                break;
            default:
                throw new IllegalArgumentException("SelectedIndex not found.");
        }
    }

    private void setAllTextButtonsToWhite() {
        for (Actor actor : table.getChildren()) {
            if (actor instanceof TextButton) {
                ((TextButton) actor).getStyle().fontColor = Color.WHITE;
            }
        }
    }

    private void setCurrentTextButtonToRed() {
        selectedIndex += 1; // because the title is also in the table.
        ((TextButton) table.getChildren().get(selectedIndex)).getStyle().fontColor = Constant.DARK_RED;
        selectedIndex -= 1;
    }

    private void createFonts() {
        Utility.loadTrueTypeAsset(TITLE_FONT, TITLE_SIZE);
        titleFont = Utility.getTrueTypeAsset(TITLE_FONT);
        Utility.loadTrueTypeAsset(MENU_FONT, MENU_SIZE);
        menuFont = Utility.getTrueTypeAsset(MENU_FONT);
    }

    private Table createTable() {
        // styles
        Label.LabelStyle titleStyle = new Label.LabelStyle(titleFont, Constant.DARK_RED);
        TextButton.TextButtonStyle buttonStyle = new TextButton.TextButtonStyle();
        buttonStyle.font = menuFont;
        buttonStyle.fontColor = Color.WHITE;

        // actors
        Label titleLabel = new Label(TITLE_LABEL, titleStyle);
        newGameButton = new TextButton(MENU_ITEM_NEW_GAME, new TextButton.TextButtonStyle(buttonStyle));
        loadGameButton = new TextButton(MENU_ITEM_LOAD_GAME, new TextButton.TextButtonStyle(buttonStyle));
        settingsButton = new TextButton(MENU_ITEM_SETTINGS, new TextButton.TextButtonStyle(buttonStyle));
        exitButton = new TextButton(MENU_ITEM_EXIT, new TextButton.TextButtonStyle(buttonStyle));

        // table
        Table newTable = new Table();
        newTable.setFillParent(true);
        newTable.add(titleLabel).spaceBottom(TITLE_SPACE_BOTTOM).row();
        newTable.add(newGameButton).spaceBottom(MENU_SPACE_BOTTOM).row();
        newTable.add(loadGameButton).spaceBottom(MENU_SPACE_BOTTOM).row();
        newTable.add(settingsButton).spaceBottom(MENU_SPACE_BOTTOM).row();
        newTable.add(exitButton).spaceBottom(MENU_SPACE_BOTTOM).row();
        return newTable;
    }

    private void applyListeners() {
        verticalKeyListener = new VerticalKeyListener(this::updateIndex, NUMBER_OF_ITEMS);
        table.addListener(verticalKeyListener);
        table.addListener(new ConfirmKeyListener(this::updateIndex, this::selectMenuItem, EXIT_INDEX));
        newGameButton.addListener(createButtonMouseListener(0));
        loadGameButton.addListener(createButtonMouseListener(1));
        settingsButton.addListener(createButtonMouseListener(2));
        exitButton.addListener(createButtonMouseListener(3));
    }

    private ButtonMouseListener createButtonMouseListener(int index) {
        return new ButtonMouseListener(this::updateIndex, this::selectMenuItem, index);
    }

}
