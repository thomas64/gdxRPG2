package nl.t64.game.rpg.screens.inventory;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Scaling;
import nl.t64.game.rpg.Utils;
import nl.t64.game.rpg.components.party.InventoryGroup;
import nl.t64.game.rpg.components.party.InventoryItem;

import java.util.List;
import java.util.Map;


class InventoryImage extends Image {

    final InventoryGroup inventoryGroup;
    final InventoryItem inventoryItem;
    private List<Map.Entry<String, String>> description;

    InventoryImage(InventoryItem inventoryItem) {
        this.inventoryItem = inventoryItem;
        this.inventoryGroup = inventoryItem.getGroup();
        this.description = null;
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

    String getDescriptionKeys() {
        if (description == null) {
            description = inventoryItem.createDescription();
        }

        var sb = new StringBuilder();
        for (Map.Entry<String, String> pair : description) {
            sb.append(pair.getKey()).append('\n');
        }
        sb.reverse();
        sb.delete(0, 1);
        sb.reverse();
        return sb.toString();
    }

    String getDescriptionValues() {
        if (description == null) {
            description = inventoryItem.createDescription();
        }

        var sb = new StringBuilder();
        for (Map.Entry<String, String> pair : description) {
            sb.append(pair.getValue()).append('\n');
        }
        sb.reverse();
        sb.delete(0, 1);
        sb.reverse();
        return sb.toString();
    }

}
