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


public class MenuSettings extends MenuScreen {

    private static final int MENU_SPACE_BOTTOM = 10;

    private static final String MENU_ITEM_FULL_SCREEN = "Toggle fullscreen";
    private static final String MENU_ITEM_CONTROLS = "View controls";
    private static final String MENU_ITEM_BACK = "Back";

    private static final int NUMBER_OF_ITEMS = 3;
    private static final int EXIT_INDEX = 2;

    private ScreenType fromScreen;

    private Table table;
    private TextButton fullscreenButton;
    private TextButton controlsButton;
    private TextButton backButton;

    private ListenerKeyVertical listenerKeyVertical;

    public MenuSettings() {
        super.selectedMenuIndex = EXIT_INDEX;
    }

    @Override
    void setFromScreen(ScreenType screenType) {
        this.fromScreen = screenType;
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
            case 0 -> processFullscreenButton();
            case 1 -> processControlsButton(screenManager);
            case 2 -> processBackButton(screenManager);
            default -> throw new IllegalArgumentException("SelectedIndex not found.");
        }
    }

    private void processFullscreenButton() {
        Utils.getSettings().toggleFullscreen();
    }

    private void processControlsButton(ScreenManager screenManager) {
        var menuControls = screenManager.getMenuScreen(ScreenType.MENU_CONTROLS);
        menuControls.setFromScreen(fromScreen);
        if (fromScreen.equals(ScreenType.MENU_PAUSE)) {
            menuControls.setBackground(screenshot);
        }
        screenManager.setScreen(ScreenType.MENU_CONTROLS);
    }

    private void processBackButton(ScreenManager screenManager) {
        if (fromScreen.equals(ScreenType.MENU_PAUSE)) {
            var menuPause = screenManager.getMenuScreen(fromScreen);
            menuPause.setBackground(screenshot);
        }
        screenManager.setScreen(fromScreen);
        fromScreen = null;
    }

    @Override
    void setAllTextButtonsToWhite() {
        for (Actor actor : table.getChildren()) {
            ((TextButton) actor).getStyle().fontColor = Color.WHITE;
        }
    }

    @Override
    void setCurrentTextButtonToRed() {
        ((TextButton) table.getChildren().get(selectedMenuIndex)).getStyle().fontColor = Constant.DARK_RED;
    }

    private Table createTable() {
        // styles
        var buttonStyle = new TextButton.TextButtonStyle();
        buttonStyle.font = menuFont;
        buttonStyle.fontColor = Color.WHITE;

        // actors
        fullscreenButton = new TextButton(MENU_ITEM_FULL_SCREEN, new TextButton.TextButtonStyle(buttonStyle));
        controlsButton = new TextButton(MENU_ITEM_CONTROLS, new TextButton.TextButtonStyle(buttonStyle));
        backButton = new TextButton(MENU_ITEM_BACK, new TextButton.TextButtonStyle(buttonStyle));

        // table
        var newTable = new Table();
        newTable.setFillParent(true);
        newTable.add(fullscreenButton).spaceBottom(MENU_SPACE_BOTTOM).row();
        newTable.add(controlsButton).spaceBottom(MENU_SPACE_BOTTOM).row();
        newTable.add(backButton);
        return newTable;
    }

    private void applyListeners() {
        listenerKeyVertical = new ListenerKeyVertical(this::updateMenuIndex, NUMBER_OF_ITEMS);
        table.addListener(listenerKeyVertical);
        table.addListener(new ListenerKeyConfirm(this::updateMenuIndex, this::selectMenuItem, EXIT_INDEX));
        fullscreenButton.addListener(new ListenerMouseTextButton(this::updateMenuIndex, this::selectMenuItem, 0));
        controlsButton.addListener(new ListenerMouseTextButton(this::updateMenuIndex, this::selectMenuItem, 1));
        backButton.addListener(new ListenerMouseTextButton(this::updateMenuIndex, this::selectMenuItem, 2));
    }

}
