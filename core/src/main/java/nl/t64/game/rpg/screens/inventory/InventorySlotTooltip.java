package nl.t64.game.rpg.screens.inventory;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.GdxRuntimeException;
import nl.t64.game.rpg.components.party.CalcType;
import nl.t64.game.rpg.components.party.InventoryDescription;
import nl.t64.game.rpg.components.party.SkillType;
import nl.t64.game.rpg.components.party.SuperEnum;


class InventorySlotTooltip extends BaseToolTip {

    private static final float SPACING = 20f;

    void setVisible(InventorySlot inventorySlot, boolean visible) {
        window.setVisible(visible);
        if (inventorySlot == null) {
            throw new GdxRuntimeException("");
//            return; // todo, kan de if weg?
        }
        if (!inventorySlot.hasItem()) {
            window.setVisible(false);
        }
    }

    void updateDescription(InventorySlot inventorySlot) {
        window.clear();
        if (inventorySlot.hasItem()) {
            addAttributesForDescription(inventorySlot.getCertainInventoryImage());
        }
        window.pack();
    }

    private void addAttributesForDescription(InventoryImage inventoryImage) {
        for (InventoryDescription attribute : inventoryImage.getDescription()) {
            var labelStyle = createLabelStyle(attribute);
            window.add(new Label(getKey(attribute), labelStyle)).spaceRight(SPACING);
            window.add(new Label(getValue(attribute), labelStyle)).row();
        }
    }

    private Label.LabelStyle createLabelStyle(InventoryDescription attribute) {
        Color color;
        if (attribute.isEnough()) {
            color = Color.WHITE;
        } else {
            color = Color.RED;
        }
        return new Label.LabelStyle(new BitmapFont(), color);
    }

    private String getKey(InventoryDescription attribute) {
        return ((SuperEnum) attribute.getKey()).getTitle();
    }

    private String getValue(InventoryDescription attribute) {
        if (attribute.getValue() instanceof SkillType) {
            return ((SkillType) attribute.getValue()).getTitle();
        } else if (attribute.getKey().equals(CalcType.BASE_HIT)) {
            return String.format("%s%%", attribute.getValue());
        } else {
            return String.valueOf(attribute.getValue());
        }
    }

}
