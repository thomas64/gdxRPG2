package nl.t64.game.rpg.screens.menu;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.ScreenUtils;
import nl.t64.game.rpg.Utils;
import nl.t64.game.rpg.audio.AudioCommand;
import nl.t64.game.rpg.audio.AudioEvent;
import nl.t64.game.rpg.constants.ScreenType;


public class MenuPause extends MenuScreen {

    private static final String MENU_ITEM_CONTINUE = "Continue";
    private static final String MENU_ITEM_LOAD_GAME = "Load Game";
    private static final String MENU_ITEM_SETTINGS = "Settings";
    private static final String MENU_ITEM_MAIN_MENU = "Main Menu";

    private static final int NUMBER_OF_ITEMS = 4;
    private static final int EXIT_INDEX = 0;

    private static final String DIALOG_MESSAGE = """
            All progress after the last save will be lost.
            Are you sure you want to exit?""";

    private ListenerKeyVertical listenerKeyVertical;

    public MenuPause() {
        super.startScreen = ScreenType.MENU_PAUSE;
        super.selectedMenuIndex = EXIT_INDEX;
    }

    public static void load() {
        Utils.getAudioManager().handle(AudioCommand.SE_PLAY_ONCE, AudioEvent.SE_MENU_CONFIRM);
        var menuPause = Utils.getScreenManager().getMenuScreen(ScreenType.MENU_PAUSE);
        menuPause.setBackground(Utils.createScreenshot(true));
        Utils.getScreenManager().setScreen(ScreenType.MENU_PAUSE);
        menuPause.updateMenuIndex(EXIT_INDEX);
    }

    @Override
    void setupScreen() {
        setFontColor();
        table = createTable();
        applyListeners();
        stage.addActor(table);
        stage.setKeyboardFocus(table);
        setCurrentTextButtonToSelected();
    }

    @Override
    public void render(float dt) {
        ScreenUtils.clear(Color.BLACK);
        stage.act(dt);
        Utils.getAudioManager().fadeBgmBgs();
        listenerKeyVertical.updateSelectedIndex(selectedMenuIndex);
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
        new DialogQuestion(this::openMenuMain, DIALOG_MESSAGE).show(stage);
    }

    private void openMenuMain() {
        Utils.getMapManager().disposeOldMaps();
        Utils.getScreenManager().setScreen(ScreenType.MENU_MAIN);
    }

    private Table createTable() {
        // styles
        var buttonStyle = new TextButton.TextButtonStyle();
        buttonStyle.font = menuFont;
        buttonStyle.fontColor = fontColor;

        // actors
        var continueButton = new TextButton(MENU_ITEM_CONTINUE, new TextButton.TextButtonStyle(buttonStyle));
        var loadGameButton = new TextButton(MENU_ITEM_LOAD_GAME, new TextButton.TextButtonStyle(buttonStyle));
        var settingsButton = new TextButton(MENU_ITEM_SETTINGS, new TextButton.TextButtonStyle(buttonStyle));
        var mainMenuButton = new TextButton(MENU_ITEM_MAIN_MENU, new TextButton.TextButtonStyle(buttonStyle));

        // table
        var newTable = new Table();
        newTable.setFillParent(true);
        newTable.defaults().center();
        newTable.add(continueButton).row();
        newTable.add(loadGameButton).row();
        newTable.add(settingsButton).row();
        newTable.add(mainMenuButton);
        Actor logo = stage.getActors().peek();
        newTable.top().padTop((logo.getHeight() * logo.getScaleY()) + LOGO_PAD + PAD_TOP)
                .right().padRight(((logo.getWidth() * logo.getScaleX()) / 2f) - (newTable.getPrefWidth() / 2f) + LOGO_PAD);
        return newTable;
    }

    private void applyListeners() {
        listenerKeyVertical = new ListenerKeyVertical(super::updateMenuIndex, NUMBER_OF_ITEMS);
        var listenerKeyConfirm = new ListenerKeyConfirm(this::selectMenuItem);
        var listenerKeyCancel = new ListenerKeyCancel(super::updateMenuIndex, this::selectMenuItem, EXIT_INDEX);
        var listenerKeyStart = new ListenerKeyStart(super::updateMenuIndex, this::selectMenuItem, EXIT_INDEX);
        table.addListener(listenerKeyVertical);
        table.addListener(listenerKeyConfirm);
        table.addListener(listenerKeyCancel);
        table.addListener(listenerKeyStart);
    }

}
