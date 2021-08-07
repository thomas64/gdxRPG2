package nl.t64.game.rpg.screens.inventory.itemslot

import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable
import com.badlogic.gdx.utils.Scaling
import nl.t64.game.rpg.Utils.gameData
import nl.t64.game.rpg.Utils.resourceManager
import nl.t64.game.rpg.components.party.inventory.DescriptionCreator
import nl.t64.game.rpg.components.party.inventory.InventoryDescription
import nl.t64.game.rpg.components.party.inventory.InventoryGroup
import nl.t64.game.rpg.components.party.inventory.InventoryItem
import nl.t64.game.rpg.screens.inventory.InventoryUtils


class InventoryImage(val inventoryItem: InventoryItem) : Image() {

    val inventoryGroup: InventoryGroup = inventoryItem.group

    init {
        val textureRegion = resourceManager.getAtlasTexture(inventoryItem.id)
        super.setDrawable(TextureRegionDrawable(textureRegion))
        super.setScaling(Scaling.none)
    }

    fun createCopy(): InventoryImage {
        return createCopy(inventoryItem.amount)
    }

    fun createCopy(amount: Int): InventoryImage {
        val copyItem = inventoryItem.createCopy(amount)
        return InventoryImage(copyItem)
    }

    fun isSameItemAs(candidateImage: InventoryImage): Boolean {
        return inventoryItem.hasSameIdAs(candidateImage.inventoryItem)
    }

    fun isStackable(): Boolean = inventoryItem.isStackable
    fun getAmount(): Int = inventoryItem.amount

    fun getComparelessDescription(totalMerchant: Int): List<InventoryDescription> {
        val descriptionCreator = DescriptionCreator(inventoryItem, totalMerchant)
        return descriptionCreator.createItemDescription()
    }

    fun getSingleDescription(totalMerchant: Int): List<InventoryDescription> {
        val descriptionCreator = DescriptionCreator(inventoryItem, totalMerchant)
        val selectedHeroId = InventoryUtils.getSelectedHeroId()
        val heroItem = gameData.party.getCertainHero(selectedHeroId)
        return descriptionCreator.createItemDescriptionComparingToHero(heroItem)
    }

    fun getDualDescription(otherItem: InventoryImage, totalMerchant: Int): List<InventoryDescription> {
        val descriptionCreator = DescriptionCreator(inventoryItem, totalMerchant)
        return descriptionCreator.createItemDescriptionComparingToItem(otherItem.inventoryItem)
    }

}
