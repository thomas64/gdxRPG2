package nl.t64.game.rpg.screens.inventory;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import nl.t64.game.rpg.components.party.SuperEnum;


class StatTooltip extends BaseToolTip {

    void setVisible(boolean visible) {
        window.setVisible(visible);
    }

    void updateDescription(SuperEnum superEnum) {
        window.clear();

        String description = superEnum.getDescription(InventoryUtils.selectedHero);
        var labelStyle = new Label.LabelStyle(new BitmapFont(), Color.WHITE);
        var label = new Label(description, labelStyle);
        window.add(label);

        window.pack();
    }

}
