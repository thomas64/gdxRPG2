package nl.t64.game.rpg.screens.inventory;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import nl.t64.game.rpg.Utils;


class InventorySlotTooltip {

    private static final String SPRITE_SLOT = "sprites/inventoryslot.png";
    private static final int PADDING = 10;

    Window window;
    private Label description;

    InventorySlotTooltip() {
        this.window = new Window("", createWindowSkin());
        this.description = new Label("", createLabelSkin());

        this.window.add(this.description);
        this.window.pad(PADDING);
        this.window.pack();
        this.window.setVisible(false);
    }

    private static Skin createWindowSkin() {
        var windowSkin = new Skin();
        var texture = Utils.getResourceManager().getTextureAsset(SPRITE_SLOT);
        var ninepatch = new NinePatch(texture, 1, 1, 1, 1);
        var drawable = new NinePatchDrawable(ninepatch);
        windowSkin.add("default", new Window.WindowStyle(new BitmapFont(), Color.GREEN, drawable));
        return windowSkin;
    }

    private static Skin createLabelSkin() {
        var labelSkin = new Skin();
        labelSkin.add("default", new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        return labelSkin;
    }

    void setVisible(InventorySlot inventorySlot, boolean visible) {
        window.setVisible(visible);
        if (inventorySlot == null) {
            return;
        }
        if (!inventorySlot.hasItem()) {
            window.setVisible(false);
        }
    }

    void updateDescription(InventorySlot inventorySlot) {
        if (inventorySlot.hasItem()) {
            description.setText(inventorySlot.getCertainInventoryImage().getDescription());
            window.pack();
        } else {
            description.setText("");
            window.pack();
        }
    }

}
