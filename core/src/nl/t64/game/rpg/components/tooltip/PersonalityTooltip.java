package nl.t64.game.rpg.components.tooltip;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import nl.t64.game.rpg.components.party.PersonalityItem;
import nl.t64.game.rpg.components.party.SkillItemId;
import nl.t64.game.rpg.screens.inventory.InventoryUtils;


public class PersonalityTooltip extends BaseTooltip {

    void setVisible(boolean visible) {
        window.setVisible(visible);
    }

    void updateDescription(PersonalityItem personalityItem) {
        window.clear();

        final int totalLoremaster = InventoryUtils.getSelectedHero().getCalculatedTotalSkillOf(SkillItemId.LOREMASTER);
        final String description = personalityItem.getDescription(totalLoremaster);
        final var labelStyle = new Label.LabelStyle(new BitmapFont(), Color.WHITE);
        final var label = new Label(description, labelStyle);
        window.add(label);

        window.pack();
    }

}
