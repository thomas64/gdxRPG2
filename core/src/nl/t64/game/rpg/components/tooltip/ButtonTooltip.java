package nl.t64.game.rpg.components.tooltip;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.ui.Label;


public class ButtonTooltip extends BaseTooltip {

    void setVisible(boolean visible) {
        window.setVisible(visible);
    }

    void updateDescription(String title) {
        window.clear();
        final var labelStyle = new Label.LabelStyle(new BitmapFont(), Color.WHITE);
        final var label = new Label(title, labelStyle);
        window.add(label);
        window.pack();
    }

}
