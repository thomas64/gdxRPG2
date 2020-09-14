package nl.t64.game.rpg.screens.menu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.Align;

import java.util.List;


public class MenuControls extends MenuScreen {

    private static final float COLUMN_0_WIDTH = 200f;
    private static final float COLUMN_1_WIDTH = 120f;
    private static final float BUTTON_TOP = 30f;

    private static final String MENU_ITEM_BACK = "Back";
    private static final int EXIT_INDEX = 0;

    private Table textTable;
    private TextButton backButton;

    public MenuControls() {
        super.selectedMenuIndex = EXIT_INDEX;
    }

    @Override
    void setupScreen() {
        setFontColor();
        createTables();
        applyListeners();
        stage.addActor(textTable);
        stage.addActor(table);
        stage.setKeyboardFocus(table);
        setCurrentTextButtonToSelected();
    }

    @Override
    public void render(float dt) {
        Gdx.gl.glClearColor(0f, 0f, 0f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act(dt);
        stage.draw();
    }

    private void selectMenuItem() {
        if (selectedMenuIndex == 0) {
            processBackButton();
        } else {
            throw new IllegalArgumentException("SelectedIndex not found.");
        }
    }

    private void createTables() {
        // styles
        var textStyle = new Label.LabelStyle(menuFont, fontColor);
        var buttonStyle = new TextButton.TextButtonStyle();
        buttonStyle.font = menuFont;
        buttonStyle.fontColor = fontColor;

        // actors
        backButton = new TextButton(MENU_ITEM_BACK, new TextButton.TextButtonStyle(buttonStyle));
        List<Label> labels = List.of(new Label("Movement", textStyle),
                                     new Label("Arrows", textStyle),
                                     new Label("Move fast", textStyle),
                                     new Label("Shift", textStyle),
                                     new Label("Move slow", textStyle),
                                     new Label("Ctrl", textStyle),
                                     new Label("Action", textStyle),
                                     new Label("A", textStyle),
                                     new Label("Inventory", textStyle),
                                     new Label("I", textStyle),
                                     new Label("Quest log", textStyle),
                                     new Label("L", textStyle),
                                     new Label("Party", textStyle),
                                     new Label("P", textStyle),
                                     new Label("Pause", textStyle),
                                     new Label("Esc", textStyle));
        labels.forEach(label -> label.setAlignment(Align.right));


        // tables
        textTable = new Table();
        textTable.setFillParent(true);
        textTable.top().padTop(PAD_TOP).right().padRight(PAD_RIGHT);
        textTable.columnDefaults(0).width(COLUMN_0_WIDTH);
        textTable.columnDefaults(1).width(COLUMN_1_WIDTH);
        for (int i = 0; i < labels.size(); i++) {
            if (i % 2 == 0) textTable.row();
            textTable.add(labels.get(i));
        }

        // button table
        table = new Table();
        table.setFillParent(true);
        table.top().padTop(BUTTON_TOP + textTable.getPrefHeight()).right().padRight(PAD_RIGHT);
        table.defaults().right();
        table.add(backButton);
    }

    private void applyListeners() {
        table.addListener(new ListenerKeyConfirm(this::updateMenuIndex, this::selectMenuItem, EXIT_INDEX));
        backButton.addListener(new ListenerMouseTextButton(this::updateMenuIndex, this::selectMenuItem, 0));
    }

}
