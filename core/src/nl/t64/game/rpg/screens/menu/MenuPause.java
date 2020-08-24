package nl.t64.game.rpg.screens.menu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import nl.t64.game.rpg.Utils;
import nl.t64.game.rpg.constants.ScreenType;


public class MenuPause extends MenuScreen {

    private static final String MENU_ITEM_CONTINUE = "Continue";
    private static final String MENU_ITEM_LOAD_GAME = "Load Game";
    private static final String MENU_ITEM_SETTINGS = "Settings";
    private static final String MENU_ITEM_MAIN_MENU = "Main Menu";

    private static final int NUMBER_OF_ITEMS = 4;
    private static final int EXIT_INDEX = 0;

    private static final String DIALOG_MESSAGE = "Any unsaved progress will be lost." + System.lineSeparator() + "Are you sure?";

    private TextButton continueButton;
    private TextButton loadGameButton;
    private TextButton settingsButton;
    private TextButton mainMenuButton;
    private DialogQuestion progressLostDialog;

    private ListenerKeyVertical listenerKeyVertical;

    public MenuPause() {
        super.startScreen = ScreenType.MENU_PAUSE;
        super.selectedMenuIndex = EXIT_INDEX;
    }

    @Override
    void setupScreen() {
        setFontColor();
        table = createTable();
        progressLostDialog = new DialogQuestion(this::openMenuMain, DIALOG_MESSAGE);
        applyListeners();
        stage.addActor(table);
        stage.setKeyboardFocus(table);
        setCurrentTextButtonToSelected();
    }

    @Override
    public void render(float dt) {
        Gdx.gl.glClearColor(0f, 0f, 0f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act(dt);
        listenerKeyVertical.updateSelectedIndex(selectedMenuIndex);
        progressLostDialog.update(); // for updating the index in de listener.
        stage.draw();
    }

    private void selectMenuItem() {
        switch (selectedMenuIndex) {
            case 0 -> processContinueButton();
            case 1 -> processButton(ScreenType.MENU_PAUSE, ScreenType.MENU_LOAD);
            case 2 -> processButton(ScreenType.MENU_PAUSE, ScreenType.MENU_SETTINGS);
            case 3 -> processMainMenuButton();
            default -> throw new IllegalArgumentException("SelectedIndex not found.");
        }
    }

    private void processContinueButton() {
        Utils.getScreenManager().setScreen(ScreenType.WORLD);
    }

    private void processMainMenuButton() {
        progressLostDialog.show(stage);
    }

    private void openMenuMain() {
        Utils.getScreenManager().setScreen(ScreenType.MENU_MAIN);
    }

    private Table createTable() {
        // styles
        var buttonStyle = new TextButton.TextButtonStyle();
        buttonStyle.font = menuFont;
        buttonStyle.fontColor = fontColor;

        // actors
        continueButton = new TextButton(MENU_ITEM_CONTINUE, new TextButton.TextButtonStyle(buttonStyle));
        loadGameButton = new TextButton(MENU_ITEM_LOAD_GAME, new TextButton.TextButtonStyle(buttonStyle));
        settingsButton = new TextButton(MENU_ITEM_SETTINGS, new TextButton.TextButtonStyle(buttonStyle));
        mainMenuButton = new TextButton(MENU_ITEM_MAIN_MENU, new TextButton.TextButtonStyle(buttonStyle));

        // table
        var newTable = new Table();
        newTable.setFillParent(true);
        newTable.top().padTop(PAD_TOP).right().padRight(PAD_RIGHT);
        newTable.defaults().right();
        newTable.add(continueButton).row();
        newTable.add(loadGameButton).row();
        newTable.add(settingsButton).row();
        newTable.add(mainMenuButton);
        return newTable;
    }

    private void applyListeners() {
        listenerKeyVertical = new ListenerKeyVertical(this::updateMenuIndex, NUMBER_OF_ITEMS);
        table.addListener(listenerKeyVertical);
        table.addListener(new ListenerKeyConfirm(this::updateMenuIndex, this::selectMenuItem, EXIT_INDEX));
        continueButton.addListener(new ListenerMouseTextButton(this::updateMenuIndex, this::selectMenuItem, 0));
        loadGameButton.addListener(new ListenerMouseTextButton(this::updateMenuIndex, this::selectMenuItem, 1));
        settingsButton.addListener(new ListenerMouseTextButton(this::updateMenuIndex, this::selectMenuItem, 2));
        mainMenuButton.addListener(new ListenerMouseTextButton(this::updateMenuIndex, this::selectMenuItem, 3));
    }

}
