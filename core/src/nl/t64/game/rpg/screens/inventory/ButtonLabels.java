package nl.t64.game.rpg.screens.inventory;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import nl.t64.game.rpg.Utils;


record ButtonLabels(Stage stage) {

    void create() {
        var labelStyle = new Label.LabelStyle(new BitmapFont(), Color.BLACK);

        var buttonTopLeftLabel = new Label(createTopLeftText(), labelStyle);
        buttonTopLeftLabel.setPosition(35f, 996f);
        stage.addActor(buttonTopLeftLabel);

        var buttonTopRightLabel = new Label(createTopRightText(), labelStyle);
        buttonTopRightLabel.setPosition(Gdx.graphics.getWidth() - 52f, 996f);
        stage.addActor(buttonTopRightLabel);

        var buttonMiddleLeftLabel = new Label(createMiddleLeftText(), labelStyle);
        buttonMiddleLeftLabel.setPosition(35f, 772f);
        stage.addActor(buttonMiddleLeftLabel);

        var buttonMiddleRightLabel = new Label(createMiddleRightText(), labelStyle);
        buttonMiddleRightLabel.setPosition(Gdx.graphics.getWidth() - 52f, 772f);
        stage.addActor(buttonMiddleRightLabel);

        var buttonBottomLeftLabel = new Label(createBottomLeftText(), labelStyle);
        buttonBottomLeftLabel.setPosition(300f, 26f);
        stage.addActor(buttonBottomLeftLabel);

        var buttonBottomRightLabel = new Label(createBottomRightText(), labelStyle);
        buttonBottomRightLabel.setPosition(890f, 26f);
        stage.addActor(buttonBottomRightLabel);
    }

    private String createTopLeftText() {
        if (Utils.isGamepadConnected()) {
            return "[L1]";
        } else {
            return "[Q]";
        }
    }

    private String createTopRightText() {
        if (Utils.isGamepadConnected()) {
            return "[R1]";
        } else {
            return "[W]";
        }
    }

    private String createMiddleLeftText() {
        if (Utils.isGamepadConnected()) {
            return "[R3]";
        } else {
            return "[Z]";
        }
    }

    private String createMiddleRightText() {
        if (Utils.isGamepadConnected()) {
            return "[R3]";
        } else {
            return "[X]";
        }
    }

    private String createBottomLeftText() {
        if (Utils.isGamepadConnected()) {
            return "[A] De/Equip      [Y] Dismiss hero      [Start] Sort inventory";
        } else {
            return "[A] De/Equip      [D] Dismiss hero      [Space] Sort inventory";
        }
    }

    private String createBottomRightText() {
        if (Utils.isGamepadConnected()) {
            return "[Select] Toggle tooltip      [L3] Toggle compare      [B] Back";
        } else {
            return "[T] Toggle tooltip      [C] Toggle compare      [ I ] Back";
        }
    }

}
