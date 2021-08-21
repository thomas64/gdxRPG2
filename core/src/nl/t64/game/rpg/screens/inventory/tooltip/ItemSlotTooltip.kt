package nl.t64.game.rpg.screens.inventory.tooltip

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.scenes.scene2d.actions.Actions
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.utils.Align
import nl.t64.game.rpg.Utils
import nl.t64.game.rpg.Utils.gameData
import nl.t64.game.rpg.components.party.CalcAttributeId
import nl.t64.game.rpg.components.party.SuperEnum
import nl.t64.game.rpg.components.party.inventory.InventoryDescription
import nl.t64.game.rpg.components.party.inventory.InventoryGroup
import nl.t64.game.rpg.components.party.inventory.ThreeState
import nl.t64.game.rpg.components.party.skills.SkillItemId
import nl.t64.game.rpg.constants.Constant
import nl.t64.game.rpg.screens.inventory.InventoryUtils
import nl.t64.game.rpg.screens.inventory.itemslot.InventoryImage
import nl.t64.game.rpg.screens.inventory.itemslot.ItemSlot


private const val SLOT_SIZE = 64f
private const val THREE_QUARTERS = SLOT_SIZE * 0.75f
private const val COLUMN_SPACING = 20f
private const val HALF_SPACING = 10f
private const val EMPTY_ROW = ""
private const val LEFT_TITLE = EMPTY_ROW
private const val RIGHT_TITLE = "Currently Equipped"
private val ORANGE = Color(-0x6fff01)
private const val DELAY = 0.5f

open class ItemSlotTooltip : BaseTooltip() {

    override fun toggle(itemSlot: ItemSlot?) {
        if (itemSlot?.hasItem() == true) {
            val isEnabled = gameData.isTooltipEnabled
            gameData.isTooltipEnabled = !isEnabled
            setupTooltip(itemSlot)
            window.isVisible = !isEnabled
        }
    }

    override fun toggleCompare(itemSlot: ItemSlot?) {
        if (itemSlot?.hasItem() == true && !window.hasActions()) {
            val isEnabled = gameData.isComparingEnabled
            gameData.isComparingEnabled = !isEnabled
            updateDescription(itemSlot)
        }
    }

    fun refresh(itemSlot: ItemSlot) {
        hide()
        if (gameData.isTooltipEnabled && itemSlot.hasItem()) {
            setupTooltip(itemSlot)
            window.addAction(Actions.sequence(Actions.delay(DELAY),
                                              Actions.show()))
        }
    }

    private fun setupTooltip(itemSlot: ItemSlot) {
        val localCoords = Vector2(itemSlot.originX, itemSlot.originY)
        itemSlot.localToStageCoordinates(localCoords)
        updateDescription(itemSlot)
        window.setPosition(localCoords.x + THREE_QUARTERS, localCoords.y + THREE_QUARTERS)
        window.toFront()
    }

    open fun updateDescription(itemSlot: ItemSlot) {
        window.clear()
        if (itemSlot.hasItem()) {
            val hoveredImage = itemSlot.getCertainInventoryImage()
            val hoveredItem = hoveredImage.inventoryItem
            val inventoryGroup = hoveredItem.group
            val selectedHero = InventoryUtils.getSelectedHero()
            val errorMessage = selectedHero.createMessageIfHeroHasNotEnoughFor(hoveredItem)
            val equippedItem = selectedHero.getInventoryItem(inventoryGroup)

            if (itemSlot.isOnHero()) {
                createSingleTooltip(hoveredImage)
            } else if (inventoryGroup == InventoryGroup.RESOURCE
                || inventoryGroup == InventoryGroup.POTION
                || inventoryGroup == InventoryGroup.ITEM
            ) {
                createResourceTooltip(hoveredImage)
            } else if (errorMessage == null
                && equippedItem != null
                && gameData.isComparingEnabled
            ) {
                createDualTooltip(hoveredImage, InventoryImage(equippedItem))
            } else {
                createSingleTooltip(hoveredImage)
            }
        }
        window.pack()
    }

    fun createResourceTooltip(inventoryImage: InventoryImage) {
        val hoveredTable = createDefaultTooltip(inventoryImage)
        window.add(hoveredTable)

        window.add().row()
        window.add(createLabel(EMPTY_ROW, Color.WHITE)).row()
        val description = inventoryImage.inventoryItem.description.joinToString(System.lineSeparator())
        window.add(createLabel(description, Color.WHITE))
    }

    open fun createSingleTooltip(inventoryImage: InventoryImage) {
        val hoveredTable = createDefaultTooltip(inventoryImage)
        addPossibleDescription(inventoryImage, hoveredTable)
        window.add(hoveredTable)
    }

    private fun createDefaultTooltip(inventoryImage: InventoryImage): Table {
        val hoveredTable = Table()
        hoveredTable.defaults().align(Align.left)

        val totalMerchant = gameData.party.getSumOfSkill(SkillItemId.MERCHANT)
        val descriptionList = inventoryImage.getSingleDescription(totalMerchant).toMutableList()
        removeLeftUnnecessaryAttributes(descriptionList)
        descriptionList.forEach { addToTable(hoveredTable, it, createSingleLabelStyle(it)) }
        return hoveredTable
    }

    private fun createDualTooltip(hoveredImage: InventoryImage, equippedImage: InventoryImage) {
        val hoveredTable = createLeftTooltip(hoveredImage, equippedImage)
        val equippedTable = createRightTooltip(equippedImage, hoveredImage)
        window.add(hoveredTable)
        window.add().spaceRight(HALF_SPACING)
        window.add(equippedTable)
    }

    private fun createLeftTooltip(hoveredImage: InventoryImage, equippedImage: InventoryImage): Table {
        val hoveredTable = Table(window.skin).apply {
            background = Utils.createTooltipRightBorder()
            padRight(HALF_SPACING)
            defaults().align(Align.left)
            add(createLabel(LEFT_TITLE, Color.WHITE)).row()
        }
        val totalMerchant = gameData.party.getSumOfSkill(SkillItemId.MERCHANT)
        val descriptionList = hoveredImage.getDualDescription(equippedImage, totalMerchant).toMutableList()
        removeLeftUnnecessaryAttributes(descriptionList)
        descriptionList.forEach { addToTable(hoveredTable, it, createLeftLabelStyle(it)) }
        addPossibleDescription(hoveredImage, hoveredTable)
        return hoveredTable
    }

    private fun createRightTooltip(equippedImage: InventoryImage, hoveredImage: InventoryImage): Table {
        val equippedTable = Table().apply {
            defaults().align(Align.left)
            add(createLabel(RIGHT_TITLE, Color.LIGHT_GRAY)).row()
        }

        val totalMerchant = gameData.party.getSumOfSkill(SkillItemId.MERCHANT)
        val descriptionList = equippedImage.getDualDescription(hoveredImage, totalMerchant).toMutableList()
        removeRightUnnecessaryAttributes(descriptionList)
        descriptionList.forEach { addToTable(equippedTable, it, createRightLabelStyle(it)) }
        addPossibleDescription(equippedImage, equippedTable)
        return equippedTable
    }

    fun addPossibleDescription(inventoryImage: InventoryImage, table: Table) {
        if (inventoryImage.inventoryItem.description.isNotEmpty()) {
            val description = inventoryImage.inventoryItem.description.joinToString(System.lineSeparator())
            table.add(createLabel(description, Color.VIOLET)).colspan(2)
        }
    }

    fun addToTable(hoveredTable: Table, attribute: InventoryDescription, labelStyle: LabelStyle?) {
        hoveredTable.add(Label(getKey(attribute), labelStyle)).spaceRight(COLUMN_SPACING)
        hoveredTable.add(Label(getValue(attribute), labelStyle)).row()
    }

    open fun removeLeftUnnecessaryAttributes(descriptionList: MutableList<InventoryDescription>) {
        removeBuy(descriptionList)
        removeSell(descriptionList)
    }

    open fun removeRightUnnecessaryAttributes(descriptionList: MutableList<InventoryDescription>) {
        removeBuy(descriptionList)
        removeSell(descriptionList)
    }

    fun removeBuy(descriptionList: MutableList<InventoryDescription>) {
        descriptionList.removeIf { it.key == Constant.DESCRIPTION_KEY_BUY }
        descriptionList.removeIf { it.key == Constant.DESCRIPTION_KEY_BUY_PIECE }
        descriptionList.removeIf { it.key == Constant.DESCRIPTION_KEY_BUY_TOTAL }
    }

    fun removeSell(descriptionList: MutableList<InventoryDescription>) {
        descriptionList.removeIf { it.key == Constant.DESCRIPTION_KEY_SELL }
        descriptionList.removeIf { it.key == Constant.DESCRIPTION_KEY_SELL_PIECE }
        descriptionList.removeIf { it.key == Constant.DESCRIPTION_KEY_SELL_TOTAL }
    }

    fun createSingleLabelStyle(attribute: InventoryDescription): LabelStyle {
        return if (isBuyOrSellValue(attribute)) {
            createLabelStyle(Color.GOLD)
        } else when (attribute.compare) {
            ThreeState.SAME, ThreeState.MORE -> createLabelStyle(Color.WHITE)
            ThreeState.LESS -> createLabelStyle(Color.RED)
        }
    }

    private fun createLeftLabelStyle(attribute: InventoryDescription): LabelStyle {
        return if (isBuyOrSellValue(attribute)) {
            createLabelStyle(Color.GOLD)
        } else when (attribute.compare) {
            ThreeState.SAME -> createLabelStyle(Color.WHITE)
            ThreeState.LESS -> createLabelStyle(ORANGE)
            ThreeState.MORE -> createLabelStyle(Color.LIME)
        }
    }

    private fun createRightLabelStyle(attribute: InventoryDescription): LabelStyle {
        return if (isBuyOrSellValue(attribute)) {
            createLabelStyle(Color.GOLD)
        } else createLabelStyle(Color.WHITE)
    }

    private fun getKey(attribute: InventoryDescription): String {
        return if (attribute.key is SuperEnum) {
            attribute.key.title
        } else {
            attribute.key.toString()
        }
    }

    private fun getValue(attribute: InventoryDescription): String {
        return when {
            attribute.value is SkillItemId -> attribute.value.title
            attribute.key == CalcAttributeId.BASE_HIT -> String.format("%s%%", attribute.value)
            else -> attribute.value.toString()
        }
    }

    private fun createLabel(text: String, color: Color): Label {
        return Label(text, createLabelStyle(color))
    }

    private fun createLabelStyle(color: Color): LabelStyle {
        return LabelStyle(BitmapFont(), color)
    }

    private fun isBuyOrSellValue(attribute: InventoryDescription): Boolean {
        return attribute.key == Constant.DESCRIPTION_KEY_BUY_TOTAL
                || attribute.key == Constant.DESCRIPTION_KEY_SELL_TOTAL
                || attribute.key == Constant.DESCRIPTION_KEY_BUY_PIECE
                || attribute.key == Constant.DESCRIPTION_KEY_SELL_PIECE
                || attribute.key == Constant.DESCRIPTION_KEY_BUY
                || attribute.key == Constant.DESCRIPTION_KEY_SELL
    }

}
