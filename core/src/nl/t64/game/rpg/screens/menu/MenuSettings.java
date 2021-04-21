package nl.t64.game.rpg.screens.menu;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.ScreenUtils;
import nl.t64.game.rpg.Utils;
import nl.t64.game.rpg.constants.ScreenType;


public class MenuSettings extends MenuScreen {

    private static final String MENU_ITEM_FULL_SCREEN_ON = "Fullscreen: On";
    private static final String MENU_ITEM_FULL_SCREEN_OFF = "Fullscreen: Off";
    private static final String MENU_ITEM_MUSIC_ON = "Music: On";
    private static final String MENU_ITEM_MUSIC_OFF = "Music: Off";
    private static final String MENU_ITEM_SOUND_ON = "Sound: On";
    private static final String MENU_ITEM_SOUND_OFF = "Sound: Off";
    private static final String MENU_ITEM_DEBUG_MODE_ON = "Debug mode: On";
    private static final String MENU_ITEM_DEBUG_MODE_OFF = "Debug mode: Off";
    private static final String MENU_ITEM_CONTROLS = "View controls";
    private static final String MENU_ITEM_BACK = "Back";

    private static final int NUMBER_OF_ITEMS = 6;
    private static final int EXIT_INDEX = 5;

    private TextButton fullscreenButton;
    private TextButton musicButton;
    private TextButton soundButton;
    private TextButton debugModeButton;

    private ListenerKeyVertical listenerKeyVertical;

    public MenuSettings() {
        super.selectedMenuIndex = EXIT_INDEX;
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
        listenerKeyVertical.updateSelectedIndex(selectedMenuIndex);
        stage.draw();
    }

    private void selectMenuItem() {
        switch (selectedMenuIndex) {
            case 0 -> processFullscreenButton();
            case 1 -> processMusicButton();
            case 2 -> processSoundButton();
            case 3 -> processDebugModeButton();
            case 4 -> processButton(ScreenType.MENU_SETTINGS, ScreenType.MENU_CONTROLS);
            case 5 -> processBackButton();
            default -> throw new IllegalArgumentException("SelectedIndex not found.");
        }
    }

    private void processFullscreenButton() {
        Utils.getPreferenceManager().toggleFullscreen();
        fullscreenButton.setText(getMenuItemFullScreen());
    }

    private void processMusicButton() {
        boolean mustPlayBgmImmediately = startScreen.equals(ScreenType.MENU_MAIN);
        Utils.getPreferenceManager().toggleMusic(mustPlayBgmImmediately);
        musicButton.setText(getMenuItemMusic());
    }

    private void processSoundButton() {
        Utils.getPreferenceManager().toggleSound();
        soundButton.setText(getMenuItemSound());
    }

    private void processDebugModeButton() {
        Utils.getPreferenceManager().toggleDebugMode();
        debugModeButton.setText(getMenuItemDebugMode());
    }

    private Table createTable() {
        // styles
        var buttonStyle = new TextButton.TextButtonStyle();
        buttonStyle.font = menuFont;
        buttonStyle.fontColor = fontColor;

        // actors
        fullscreenButton = new TextButton(getMenuItemFullScreen(), new TextButton.TextButtonStyle(buttonStyle));
        musicButton = new TextButton(getMenuItemMusic(), new TextButton.TextButtonStyle(buttonStyle));
        soundButton = new TextButton(getMenuItemSound(), new TextButton.TextButtonStyle(buttonStyle));
        debugModeButton = new TextButton(getMenuItemDebugMode(), new TextButton.TextButtonStyle(buttonStyle));
        var controlsButton = new TextButton(MENU_ITEM_CONTROLS, new TextButton.TextButtonStyle(buttonStyle));
        var backButton = new TextButton(MENU_ITEM_BACK, new TextButton.TextButtonStyle(buttonStyle));

        // table
        var newTable = new Table();
        newTable.setFillParent(true);
        newTable.top().padTop(PAD_TOP).right().padRight(PAD_RIGHT);
        newTable.defaults().right();
        newTable.add(fullscreenButton).row();
        newTable.add(musicButton).row();
        newTable.add(soundButton).row();
        newTable.add(debugModeButton).row();
        newTable.add(controlsButton).row();
        newTable.add(backButton);
        return newTable;
    }

    private String getMenuItemFullScreen() {
        return Utils.getPreferenceManager().isFullscreen() ? MENU_ITEM_FULL_SCREEN_ON : MENU_ITEM_FULL_SCREEN_OFF;
    }

    private String getMenuItemMusic() {
        return Utils.getPreferenceManager().isMusicOn() ? MENU_ITEM_MUSIC_ON : MENU_ITEM_MUSIC_OFF;
    }

    private String getMenuItemSound() {
        return Utils.getPreferenceManager().isSoundOn() ? MENU_ITEM_SOUND_ON : MENU_ITEM_SOUND_OFF;
    }

    private String getMenuItemDebugMode() {
        return Utils.getPreferenceManager().isInDebugMode() ? MENU_ITEM_DEBUG_MODE_ON : MENU_ITEM_DEBUG_MODE_OFF;
    }

    private void applyListeners() {
        listenerKeyVertical = new ListenerKeyVertical(super::updateMenuIndex, NUMBER_OF_ITEMS);
        var listenerKeyConfirm = new ListenerKeyConfirm(this::selectMenuItem);
        var listenerKeyCancel = new ListenerKeyCancel(super::updateMenuIndex, this::selectMenuItem, EXIT_INDEX);
        table.addListener(listenerKeyVertical);
        table.addListener(listenerKeyConfirm);
        table.addListener(listenerKeyCancel);
    }

}
