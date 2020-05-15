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


public class MenuControls extends MenuScreen {

    private static final float TEXT_TABLE_Y = 50f;
    private static final float COLUMN_0_WIDTH = 200f;
    private static final float COLUMN_1_WIDTH = 120f;

    private static final String MENU_ITEM_BACK = "Back";
    private static final int EXIT_INDEX = 0;

    private ScreenType fromScreen;

    private Table textTable;
    private Table buttonTable;
    private TextButton backButton;

    public MenuControls() {
        super.selectedMenuIndex = EXIT_INDEX;
    }

    @Override
    void setFromScreen(ScreenType screenType) {
        this.fromScreen = screenType;
    }

    @Override
    void setupScreen() {
        createTables();
        applyListeners();
        stage.addActor(textTable);
        stage.addActor(buttonTable);
        stage.setKeyboardFocus(buttonTable);
        setCurrentTextButtonToRed();
    }

    @Override
    public void render(float dt) {
        Gdx.gl.glClearColor(0f, 0f, 0f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act(dt);
        stage.draw();
    }

    @Override
    void setAllTextButtonsToWhite() {
        for (Actor actor : buttonTable.getChildren()) {
            ((TextButton) actor).getStyle().fontColor = Color.WHITE;
        }
    }

    @Override
    void setCurrentTextButtonToRed() {
        ((TextButton) buttonTable.getChildren().get(selectedMenuIndex)).getStyle().fontColor = Constant.DARK_RED;
    }

    private void selectMenuItem() {
        var screenManager = Utils.getScreenManager();
        if (selectedMenuIndex == 0) {
            processBackButton(screenManager);
        } else {
            throw new IllegalArgumentException("SelectedIndex not found.");
        }
    }

    private void processBackButton(ScreenManager screenManager) {
        if (fromScreen.equals(ScreenType.MENU_PAUSE)) {
            var menuSettings = screenManager.getMenuScreen(ScreenType.MENU_SETTINGS);
            menuSettings.setBackground(screenshot);
        }
        screenManager.setScreen(ScreenType.MENU_SETTINGS);
        fromScreen = null;
    }

    private void createTables() {
        // styles
        var textStyle = new Label.LabelStyle(menuFont, Color.WHITE);
        var buttonStyle = new TextButton.TextButtonStyle();
        buttonStyle.font = menuFont;
        buttonStyle.fontColor = Color.WHITE;

        // actors
        backButton = new TextButton(MENU_ITEM_BACK, new TextButton.TextButtonStyle(buttonStyle));

        // tables
        textTable = new Table();
        textTable.setFillParent(true);
        textTable.columnDefaults(0).width(COLUMN_0_WIDTH);
        textTable.columnDefaults(1).width(COLUMN_1_WIDTH);
        textTable.setY(TEXT_TABLE_Y);
        textTable.add(new Label("Movement", textStyle));
        textTable.add(new Label("Arrows", textStyle));
        textTable.row();
        textTable.add(new Label("Move fast", textStyle));
        textTable.add(new Label("Shift", textStyle));
        textTable.row();
        textTable.add(new Label("Move slow", textStyle));
        textTable.add(new Label("Ctrl", textStyle));
        textTable.row();
        textTable.add(new Label("Action", textStyle));
        textTable.add(new Label("A", textStyle));
        textTable.row();
        textTable.add(new Label("Inventory", textStyle));
        textTable.add(new Label("I", textStyle));
        textTable.row();
        textTable.add(new Label("Party", textStyle));
        textTable.add(new Label("P", textStyle));
        textTable.row();
        textTable.add(new Label("Pause", textStyle));
        textTable.add(new Label("Esc", textStyle));

        buttonTable = new Table();
        buttonTable.setFillParent(true);
        buttonTable.setY(-(TEXT_TABLE_Y + (textTable.getPrefHeight() / 2f)));
        buttonTable.add(backButton);
    }

    private void applyListeners() {
        buttonTable.addListener(new ListenerKeyConfirm(this::updateMenuIndex, this::selectMenuItem, EXIT_INDEX));
        backButton.addListener(new ListenerMouseTextButton(this::updateMenuIndex, this::selectMenuItem, 0));
    }

}
