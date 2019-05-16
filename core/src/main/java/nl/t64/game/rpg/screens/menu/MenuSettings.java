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


public class MenuSettings extends MenuScreen {

    private static final int MENU_SPACE_BOTTOM = 10;

    private static final String MENU_ITEM_FULL_SCREEN = "Toggle fullscreen";
    private static final String MENU_ITEM_BACK = "Back";

    private static final int NUMBER_OF_ITEMS = 2;
    private static final int EXIT_INDEX = 1;

    private ScreenType fromScreen;

    private Table table;
    private TextButton fullscreenButton;
    private TextButton backButton;

    private ListenerKeyVertical listenerKeyVertical;

    private int selectedIndex;

    public MenuSettings() {
        super();
        this.selectedIndex = 1;
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
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act(dt);
        listenerKeyVertical.updateSelectedIndex(selectedIndex);
        stage.draw();
    }

    private void updateIndex(Integer newIndex) {
        selectedIndex = newIndex;
        setAllTextButtonsToWhite();
        setCurrentTextButtonToRed();
    }

    private void selectMenuItem() {
        switch (selectedIndex) {
            case 0:
                processFullscreenButton();
                break;
            case 1:
                processBackButton();
                break;
            default:
                throw new IllegalArgumentException("SelectedIndex not found.");
        }
    }

    private void processFullscreenButton() {
        Utils.getSettings().toggleFullscreen();
    }

    private void processBackButton() {
        if (fromScreen == ScreenType.MENU_PAUSE) {
            var menuPause = Utils.getScreenManager().getMenuScreen(fromScreen);
            menuPause.setBackground(screenshot);
        }
        Utils.getScreenManager().setScreen(fromScreen);
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

    private Table createTable() {
        // styles
        TextButton.TextButtonStyle buttonStyle = new TextButton.TextButtonStyle();
        buttonStyle.font = menuFont;
        buttonStyle.fontColor = Color.WHITE;

        // actors
        fullscreenButton = new TextButton(MENU_ITEM_FULL_SCREEN, new TextButton.TextButtonStyle(buttonStyle));
        backButton = new TextButton(MENU_ITEM_BACK, new TextButton.TextButtonStyle(buttonStyle));

        // table
        Table newTable = new Table();
        newTable.setFillParent(true);
        newTable.add(fullscreenButton).spaceBottom(MENU_SPACE_BOTTOM).row();
        newTable.add(backButton).spaceBottom(MENU_SPACE_BOTTOM).row();
        return newTable;
    }

    private void applyListeners() {
        listenerKeyVertical = new ListenerKeyVertical(this::updateIndex, NUMBER_OF_ITEMS);
        table.addListener(listenerKeyVertical);
        table.addListener(new ListenerKeyConfirm(this::updateIndex, this::selectMenuItem, EXIT_INDEX));
        fullscreenButton.addListener(new ListenerMouseTextButton(this::updateIndex, this::selectMenuItem, 0));
        backButton.addListener(new ListenerMouseTextButton(this::updateIndex, this::selectMenuItem, 1));
    }

}
