package nl.t64.game.rpg.screens.inventory;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.badlogic.gdx.utils.GdxRuntimeException;
import nl.t64.game.rpg.Utils;


class InventorySlotTooltip {

    private static final String SPRITE_SLOT = "sprites/inventoryslot.png";
    private static final float PADDING = 10f;
    private static final float SPACING = 20f;

    final Window window;
    private final Label descriptionLeft;
    private final Label descriptionRight;

    InventorySlotTooltip() {
        var windowStyle = createWindowStyle();
        var labelStyle = new Label.LabelStyle(new BitmapFont(), Color.WHITE);
        this.window = new Window("", windowStyle);
        this.descriptionLeft = new Label("", labelStyle);
        this.descriptionRight = new Label("", labelStyle);

        this.window.add(this.descriptionLeft).spaceRight(SPACING);
        this.window.add(this.descriptionRight);
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
        if (inventorySlot.hasItem()) {
            InventoryImage inventoryImage = inventorySlot.getCertainInventoryImage();
            descriptionLeft.setText(inventoryImage.getDescriptionKeys());
            descriptionRight.setText(inventoryImage.getDescriptionValues());
            window.pack();
        } else {
            descriptionLeft.setText("");
            descriptionRight.setText("");
            window.pack();
        }
    }

}
