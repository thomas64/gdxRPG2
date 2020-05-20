package nl.t64.game.rpg.screens.inventory;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Scaling;
import nl.t64.game.rpg.Utils;
import nl.t64.game.rpg.components.party.DescriptionCreator;
import nl.t64.game.rpg.components.party.InventoryDescription;
import nl.t64.game.rpg.components.party.InventoryGroup;
import nl.t64.game.rpg.components.party.InventoryItem;

import java.util.List;


public class InventoryImage extends Image {

    final InventoryGroup inventoryGroup;
    public final InventoryItem inventoryItem;

    public InventoryImage(InventoryItem inventoryItem) {
        this.inventoryItem = inventoryItem;
        this.inventoryGroup = inventoryItem.getGroup();
        TextureRegion textureRegion = Utils.getResourceManager().getAtlasTexture(inventoryItem.getId());
        super.setDrawable(new TextureRegionDrawable(textureRegion));
        super.setScaling(Scaling.none);
    }

    boolean isSameItemAs(InventoryImage candidateImage) {
        return inventoryItem.hasSameIdAs(candidateImage.inventoryItem);
    }

    boolean isStackable() {
        return inventoryItem.isStackable();
    }

    int getAmount() {
        return inventoryItem.getAmount();
    }

    void setAmount(int amount) {
        inventoryItem.setAmount(amount);
    }

    public List<InventoryDescription> getSingleDescription(int totalMerchant) {
        final var descriptionCreator = new DescriptionCreator(inventoryItem, totalMerchant);
        return descriptionCreator.createItemDescriptionComparingToHero(InventoryUtils.getSelectedHero());
    }

    public List<InventoryDescription> getLeftDescription(InventoryImage otherItem, int totalMerchant) {
        final var descriptionCreator = new DescriptionCreator(inventoryItem, totalMerchant);
        return descriptionCreator.createItemDescriptionComparingToItem(otherItem.inventoryItem);
    }

}
