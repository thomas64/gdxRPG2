package nl.t64.game.rpg.screens.inventory;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.GdxRuntimeException;
import nl.t64.game.rpg.Utils;
import nl.t64.game.rpg.components.party.CalcType;
import nl.t64.game.rpg.components.party.InventoryDescription;
import nl.t64.game.rpg.components.party.SkillType;
import nl.t64.game.rpg.components.party.SuperEnum;


class InventorySlotTooltip {

    private static final String SPRITE_SLOT = "sprites/tooltip.png";
    private static final float PADDING = 10f;
    private static final float SPACING = 20f;

    final Window window;

    InventorySlotTooltip() {
        var windowStyle = createWindowStyle();
        this.window = new Window("", windowStyle);
        this.window.defaults().align(Align.left);
        this.window.pad(PADDING);
        this.window.pack();
        this.window.setVisible(false);
    }

    private static Window.WindowStyle createWindowStyle() {
        var texture = Utils.getResourceManager().getTextureAsset(SPRITE_SLOT);
        var ninepatch = new NinePatch(texture, 1, 1, 1, 1);
        var drawable = new NinePatchDrawable(ninepatch);
        return new Window.WindowStyle(new BitmapFont(), Color.GREEN, drawable);
    }

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
