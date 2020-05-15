package nl.t64.game.rpg.screens.menu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import nl.t64.game.rpg.Utils;
import nl.t64.game.rpg.constants.Constant;
import nl.t64.game.rpg.constants.ScreenType;
import nl.t64.game.rpg.screens.ScreenManager;


public class MenuMain extends MenuScreen {

    private static final float TITLE_SPACE_BOTTOM = 75f;
    private static final float MENU_SPACE_BOTTOM = 10f;

    private static final String TITLE_LABEL = "gdxRPG2";

    private static final String MENU_ITEM_NEW_GAME = "New Game";
    private static final String MENU_ITEM_LOAD_GAME = "Load Game";
    private static final String MENU_ITEM_SETTINGS = "Settings";
    private static final String MENU_ITEM_EXIT = "Exit";

    private static final int NUMBER_OF_ITEMS = 4;
    private static final int EXIT_INDEX = 3;

    private Table table;
    private TextButton newGameButton;
    private TextButton loadGameButton;
    private TextButton settingsButton;
    private TextButton exitButton;

    private ListenerKeyVertical listenerKeyVertical;

    public MenuMain() {
        super.selectedMenuIndex = 0;
    }

    @Override
    void setupScreen() {
        table = createTable();
        applyListeners();
        stage.addActor(table);
        stage.setKeyboardFocus(table);
        setCurrentTextButtonToRed();
    }

    @Override
    public void render(float dt) {
        Gdx.gl.glClearColor(0f, 0f, 0f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act(dt);
        listenerKeyVertical.updateSelectedIndex(selectedMenuIndex);
        stage.draw();
    }

    private void selectMenuItem() {
        var screenManager = Utils.getScreenManager();
        switch (selectedMenuIndex) {
            case 0 -> processNewGameButton(screenManager);
            case 1 -> processLoadGameButton(screenManager);
            case 2 -> processSettingsButton(screenManager);
            case 3 -> processExitButton();
            default -> throw new IllegalArgumentException("SelectedIndex not found.");
        }
    }

    private void processNewGameButton(ScreenManager screenManager) {
        screenManager.setScreen(ScreenType.MENU_NEW);
    }

    private void processLoadGameButton(ScreenManager screenManager) {
        var menuLoad = screenManager.getMenuScreen(ScreenType.MENU_LOAD);
        menuLoad.setFromScreen(ScreenType.MENU_MAIN);
        screenManager.setScreen(ScreenType.MENU_LOAD);
    }

    private void processSettingsButton(ScreenManager screenManager) {
        var menuSettings = screenManager.getMenuScreen(ScreenType.MENU_SETTINGS);
        menuSettings.setFromScreen(ScreenType.MENU_MAIN);
        screenManager.setScreen(ScreenType.MENU_SETTINGS);
    }

    private void processExitButton() {
        Gdx.app.exit();
    }

    @Override
    void setAllTextButtonsToWhite() {
        for (Actor actor : table.getChildren()) {
            if (actor instanceof TextButton textButton) {
                textButton.getStyle().fontColor = Color.WHITE;
            }
        }
    }

    @Override
    void setCurrentTextButtonToRed() {
        selectedMenuIndex += 1; // because the title is also in the table.
        ((TextButton) table.getChildren().get(selectedMenuIndex)).getStyle().fontColor = Constant.DARK_RED;
        selectedMenuIndex -= 1;
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
        newTable.add(exitButton);
        return newTable;
    }

    private void applyListeners() {
        listenerKeyVertical = new ListenerKeyVertical(this::updateMenuIndex, NUMBER_OF_ITEMS);
        table.addListener(listenerKeyVertical);
        table.addListener(new ListenerKeyConfirm(this::updateMenuIndex, this::selectMenuItem, EXIT_INDEX));
        newGameButton.addListener(new ListenerMouseTextButton(this::updateMenuIndex, this::selectMenuItem, 0));
        loadGameButton.addListener(new ListenerMouseTextButton(this::updateMenuIndex, this::selectMenuItem, 1));
        settingsButton.addListener(new ListenerMouseTextButton(this::updateMenuIndex, this::selectMenuItem, 2));
        exitButton.addListener(new ListenerMouseTextButton(this::updateMenuIndex, this::selectMenuItem, 3));
    }

}
