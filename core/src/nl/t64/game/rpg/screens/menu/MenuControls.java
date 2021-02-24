package nl.t64.game.rpg.screens.menu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;

import java.util.List;


public class MenuControls extends MenuScreen {

    private static final float COLUMN_0_WIDTH = 200f;
    private static final float COLUMN_1_WIDTH = 160f;
    private static final float COLUMN_2_WIDTH = 160f;

    @Override
    void setupScreen() {
        setFontColor();
        createTables();
        applyListeners();
        stage.addActor(table);
    }

    @Override
    public void render(float dt) {
        Gdx.gl.glClearColor(0f, 0f, 0f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act(dt);
        stage.draw();
    }

    private void createTables() {
        // styles
        var textStyle = new Label.LabelStyle(menuFont, fontColor);

        // actors
        List<Label> labels = List.of(new Label("", textStyle),
                                     new Label("Keyboard", textStyle),
                                     new Label("Gamepad", textStyle),
                                     new Label("Movement", textStyle),
                                     new Label("Arrows", textStyle),
                                     new Label("L3", textStyle),
                                     new Label("Move fast", textStyle),
                                     new Label("Shift", textStyle),
                                     new Label("R1", textStyle),
                                     new Label("Move slow", textStyle),
                                     new Label("Ctrl", textStyle),
                                     new Label("L1", textStyle),
                                     new Label("Action", textStyle),
                                     new Label("A", textStyle),
                                     new Label("A", textStyle),
                                     new Label("Inventory", textStyle),
                                     new Label("I", textStyle),
                                     new Label("Y", textStyle),
                                     new Label("Quest log", textStyle),
                                     new Label("L", textStyle),
                                     new Label("X", textStyle),
                                     new Label("Party", textStyle),
                                     new Label("P", textStyle),
                                     new Label("Select", textStyle),
                                     new Label("Pause", textStyle),
                                     new Label("Esc", textStyle),
                                     new Label("Start", textStyle));
        labels.forEach(label -> label.setAlignment(Align.right));

        // tables
        table = new Table();
        table.setFillParent(true);
        table.top().padTop(PAD_TOP).right().padRight(PAD_RIGHT);
        table.columnDefaults(0).width(COLUMN_0_WIDTH);
        table.columnDefaults(1).width(COLUMN_1_WIDTH);
        table.columnDefaults(2).width(COLUMN_2_WIDTH);
        for (int i = 0; i < labels.size(); i++) {
            if (i % 3 == 0) table.row();
            table.add(labels.get(i));
        }
    }

    private void applyListeners() {
        stage.addListener(new ListenerKeyConfirm(super::processBackButton));
        stage.addListener(new ListenerKeyCancel(super::processBackButton));
    }

}
