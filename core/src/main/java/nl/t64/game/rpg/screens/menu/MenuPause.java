package nl.t64.game.rpg.screens.menu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import nl.t64.game.rpg.Utils;
import nl.t64.game.rpg.constants.Constant;
import nl.t64.game.rpg.constants.ScreenType;
import nl.t64.game.rpg.screens.ScreenManager;


public class MenuPause extends MenuScreen {

    private static final int MENU_SPACE_BOTTOM = 10;

    private static final String MENU_ITEM_CONTINUE = "Continue";
    private static final String MENU_ITEM_LOAD_GAME = "Load Game";
    private static final String MENU_ITEM_SETTINGS = "Settings";
    private static final String MENU_ITEM_MAIN_MENU = "Main Menu";

    private static final int NUMBER_OF_ITEMS = 4;
    private static final int EXIT_INDEX = 0;

    private static final String DIALOG_MESSAGE = "Any unsaved progress will be lost.\nAre you sure?";

    private Table table;
    private TextButton continueButton;
    private TextButton loadGameButton;
    private TextButton settingsButton;
    private TextButton mainMenuButton;
    private DialogQuestion progressLostDialog;

    private ListenerKeyVertical listenerKeyVertical;

    private int selectedIndex;

    public MenuPause() {
        this.selectedIndex = 0;
    }

    @Override
    void setupScreen() {
        table = createTable();
        progressLostDialog = new DialogQuestion(this::openMenuMain, DIALOG_MESSAGE);
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
        listenerKeyVertical.updateSelectedIndex(selectedIndex);
        progressLostDialog.update(); // for updating the index in de listener.
        stage.draw();
    }

    public void updateIndex(Integer newIndex) {
        selectedIndex = newIndex;
        setAllTextButtonsToWhite();
        setCurrentTextButtonToRed();
    }

    private void selectMenuItem() {
        var screenManager = Utils.getScreenManager();
        switch (selectedIndex) {
            case 0 -> processContinueButton(screenManager);
            case 1 -> processLoadGameButton(screenManager);
            case 2 -> processSettingsButton(screenManager);
            case 3 -> processMainMenuButton();
            default -> throw new IllegalArgumentException("SelectedIndex not found.");
        }
    }

    private void processContinueButton(ScreenManager screenManager) {
        screenManager.setScreen(ScreenType.WORLD);
    }

    private void processLoadGameButton(ScreenManager screenManager) {
        var menuLoad = screenManager.getMenuScreen(ScreenType.MENU_LOAD);
        menuLoad.setFromScreen(ScreenType.MENU_PAUSE);
        menuLoad.setBackground(screenshot);
        screenManager.setScreen(ScreenType.MENU_LOAD);
    }

    private void processSettingsButton(ScreenManager screenManager) {
        var menuSettings = screenManager.getMenuScreen(ScreenType.MENU_SETTINGS);
        menuSettings.setFromScreen(ScreenType.MENU_PAUSE);
        menuSettings.setBackground(screenshot);
        screenManager.setScreen(ScreenType.MENU_SETTINGS);
    }

    private void processMainMenuButton() {
        progressLostDialog.show(stage);
    }

    private void openMenuMain() {
        Utils.getScreenManager().setScreen(ScreenType.MENU_MAIN);
    }

    private void setAllTextButtonsToWhite() {
        for (Actor actor : table.getChildren()) {
            ((TextButton) actor).getStyle().fontColor = Color.WHITE;
        }
    }

    private void setCurrentTextButtonToRed() {
        ((TextButton) table.getChildren().get(selectedIndex)).getStyle().fontColor = Constant.DARK_RED;
    }

    private Table createTable() {
        // styles
        var buttonStyle = new TextButton.TextButtonStyle();
        buttonStyle.font = menuFont;
        buttonStyle.fontColor = Color.WHITE;

        // actors
        continueButton = new TextButton(MENU_ITEM_CONTINUE, new TextButton.TextButtonStyle(buttonStyle));
        loadGameButton = new TextButton(MENU_ITEM_LOAD_GAME, new TextButton.TextButtonStyle(buttonStyle));
        settingsButton = new TextButton(MENU_ITEM_SETTINGS, new TextButton.TextButtonStyle(buttonStyle));
        mainMenuButton = new TextButton(MENU_ITEM_MAIN_MENU, new TextButton.TextButtonStyle(buttonStyle));

        // table
        var newTable = new Table();
        newTable.setFillParent(true);
        newTable.add(continueButton).spaceBottom(MENU_SPACE_BOTTOM).row();
        newTable.add(loadGameButton).spaceBottom(MENU_SPACE_BOTTOM).row();
        newTable.add(settingsButton).spaceBottom(MENU_SPACE_BOTTOM).row();
        newTable.add(mainMenuButton).spaceBottom(MENU_SPACE_BOTTOM).row();
        return newTable;
    }

    private void applyListeners() {
        listenerKeyVertical = new ListenerKeyVertical(this::updateIndex, NUMBER_OF_ITEMS);
        table.addListener(listenerKeyVertical);
        table.addListener(new ListenerKeyConfirm(this::updateIndex, this::selectMenuItem, EXIT_INDEX));
        continueButton.addListener(new ListenerMouseTextButton(this::updateIndex, this::selectMenuItem, 0));
        loadGameButton.addListener(new ListenerMouseTextButton(this::updateIndex, this::selectMenuItem, 1));
        settingsButton.addListener(new ListenerMouseTextButton(this::updateIndex, this::selectMenuItem, 2));
        mainMenuButton.addListener(new ListenerMouseTextButton(this::updateIndex, this::selectMenuItem, 3));
    }

}
