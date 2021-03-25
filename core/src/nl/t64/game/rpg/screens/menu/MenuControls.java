package nl.t64.game.rpg.screens.menu;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.ScreenUtils;
import nl.t64.game.rpg.Utils;

import java.util.List;
import java.util.stream.IntStream;


public class MenuControls extends MenuScreen {

    private static final float COLUMN_0_WIDTH = 200f;
    private static final float COLUMN_1_WIDTH = 120f;

    private boolean isGamepadConnected;

    @Override
    void setupScreen() {
        isGamepadConnected = Utils.isGamepadConnected();
        setFontColor();
        createTables();
        applyListeners();
        stage.addActor(table);
    }

    @Override
    public void render(float dt) {
        ScreenUtils.clear(Color.BLACK);
        stage.act(dt);
        stage.draw();
        if (isGamepadConnected != Utils.isGamepadConnected()) {
            refreshScreen();
        }
    }

    private void createTables() {
        var textStyle = new Label.LabelStyle(menuFont, fontColor);
        List<Label> labels = createInputLabels(textStyle);
        labels.forEach(label -> label.setAlignment(Align.right));

        table = new Table();
        table.setFillParent(true);
        table.top().padTop(PAD_TOP).right().padRight(PAD_RIGHT);
        table.columnDefaults(0).width(COLUMN_0_WIDTH);
        table.columnDefaults(1).width(COLUMN_1_WIDTH);
        IntStream.range(0, labels.size())
                 .forEach(index -> addToTable(index, labels));
    }

    private void addToTable(int index, List<Label> labels) {
        if (index % 2 == 0) {
            table.row();
        }
        table.add(labels.get(index));
    }

    private void applyListeners() {
        stage.addListener(new ListenerKeyConfirm(super::processBackButton));
        stage.addListener(new ListenerKeyCancel(super::processBackButton));
    }

    private void refreshScreen() {
        stage.getRoot().clearListeners();
        table.clear();
        setupScreen();
    }

    private List<Label> createInputLabels(Label.LabelStyle textStyle) {
        if (isGamepadConnected) {
            return createGamepadLabels(textStyle);
        } else {
            return createKeyboardLabels(textStyle);
        }
    }

    private List<Label> createKeyboardLabels(Label.LabelStyle textStyle) {
        return List.of(new Label("Movement", textStyle),
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
                       new Label("Map", textStyle),
                       new Label("M", textStyle),
                       new Label("Party", textStyle),
                       new Label("P", textStyle),
                       new Label("Pause", textStyle),
                       new Label("Esc", textStyle));
    }

    private List<Label> createGamepadLabels(Label.LabelStyle textStyle) {
        return List.of(new Label("Movement", textStyle),
                       new Label("L3", textStyle),
                       new Label("Move fast", textStyle),
                       new Label("R1", textStyle),
                       new Label("Move slow", textStyle),
                       new Label("L1", textStyle),
                       new Label("Action", textStyle),
                       new Label("A", textStyle),
                       new Label("Inventory", textStyle),
                       new Label("Y", textStyle),
                       new Label("Quest log", textStyle),
                       new Label("X", textStyle),
                       new Label("Map", textStyle),
                       new Label("Select", textStyle),
                       new Label("Party", textStyle),
                       new Label("R3", textStyle),
                       new Label("Pause", textStyle),
                       new Label("Start", textStyle));
    }

}
