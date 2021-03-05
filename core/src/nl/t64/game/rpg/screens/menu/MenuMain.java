package nl.t64.game.rpg.screens.menu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.ScreenUtils;
import nl.t64.game.rpg.Utils;
import nl.t64.game.rpg.audio.AudioCommand;
import nl.t64.game.rpg.audio.AudioEvent;
import nl.t64.game.rpg.constants.ScreenType;


public class MenuMain extends MenuScreen {

    private static final String BACKGROUND = "sprites/titlescreen.jpg";

    private static final String MENU_ITEM_NEW_GAME = "New Game";
    private static final String MENU_ITEM_LOAD_GAME = "Load Game";
    private static final String MENU_ITEM_SETTINGS = "Settings";
    private static final String MENU_ITEM_CREDITS = "Credits";
    private static final String MENU_ITEM_EXIT = "Exit";

    private static final int NUMBER_OF_ITEMS = 5;

    private ListenerKeyVertical listenerKeyVertical;

    public MenuMain() {
        super.startScreen = ScreenType.MENU_MAIN;
        super.selectedMenuIndex = 0;
    }

    @Override
    void setupScreen() {
        Utils.getAudioManager().handle(AudioCommand.BGM_PLAY_LOOP, AudioEvent.BGM_TITLE);
        Texture texture = Utils.getResourceManager().getTextureAsset(BACKGROUND);
        setBackground(new Image(texture));
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
            case 0 -> processButton(ScreenType.MENU_MAIN, ScreenType.MENU_NEW);
            case 1 -> processButton(ScreenType.MENU_MAIN, ScreenType.MENU_LOAD);
            case 2 -> processButton(ScreenType.MENU_MAIN, ScreenType.MENU_SETTINGS);
            case 3 -> processButton(ScreenType.MENU_MAIN, ScreenType.MENU_CREDITS);
            case 4 -> processExitButton();
            default -> throw new IllegalArgumentException("SelectedIndex not found.");
        }
    }

    private void processExitButton() {
        Gdx.app.exit();
    }

    private Table createTable() {
        // styles
        var buttonStyle = new TextButton.TextButtonStyle();
        buttonStyle.font = menuFont;
        buttonStyle.fontColor = fontColor;

        // actors
        var newGameButton = new TextButton(MENU_ITEM_NEW_GAME, new TextButton.TextButtonStyle(buttonStyle));
        var loadGameButton = new TextButton(MENU_ITEM_LOAD_GAME, new TextButton.TextButtonStyle(buttonStyle));
        var settingsButton = new TextButton(MENU_ITEM_SETTINGS, new TextButton.TextButtonStyle(buttonStyle));
        var creditsButton = new TextButton(MENU_ITEM_CREDITS, new TextButton.TextButtonStyle(buttonStyle));
        var exitButton = new TextButton(MENU_ITEM_EXIT, new TextButton.TextButtonStyle(buttonStyle));

        // table
        var newTable = new Table();
        newTable.setFillParent(true);
        newTable.top().padTop(PAD_TOP).right().padRight(PAD_RIGHT);
        newTable.defaults().right();
        newTable.add(newGameButton).row();
        newTable.add(loadGameButton).row();
        newTable.add(settingsButton).row();
        newTable.add(creditsButton).row();
        newTable.add(exitButton);
        return newTable;
    }

    private void applyListeners() {
        listenerKeyVertical = new ListenerKeyVertical(super::updateMenuIndex, NUMBER_OF_ITEMS);
        var listenerKeyConfirm = new ListenerKeyConfirm(this::selectMenuItem);
        table.addListener(listenerKeyVertical);
        table.addListener(listenerKeyConfirm);
    }

}
