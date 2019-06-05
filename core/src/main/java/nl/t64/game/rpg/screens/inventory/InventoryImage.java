package nl.t64.game.rpg.screens.inventory;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Scaling;
import nl.t64.game.rpg.Utils;
import nl.t64.game.rpg.components.inventory.InventoryGroup;
import nl.t64.game.rpg.components.inventory.InventoryItem;


class InventoryImage extends Image {

    final InventoryGroup inventoryGroup;
    final InventoryItem inventoryItem;

    InventoryImage(InventoryItem inventoryItem) {
        this.inventoryItem = inventoryItem;
        this.inventoryGroup = inventoryItem.getGroup();
        TextureRegion textureRegion = Utils.getResourceManager().getAtlasTexture(inventoryItem.getId());
        super.setDrawable(new TextureRegionDrawable(textureRegion));
        super.setScaling(Scaling.none);
    }

    boolean isSameItemAs(InventoryImage candidateImage) {
        return inventoryItem.hasSameIdAs(candidateImage.inventoryItem.getId());
    }

    boolean isStackable() {
        return inventoryItem.isStackable();
    }

    String getDescription() {
        return inventoryItem.getDescription();
    }
}
