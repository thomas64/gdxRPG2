package nl.t64.game.rpg.screens.inventory

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle
import nl.t64.game.rpg.Utils.createTopBorder
import nl.t64.game.rpg.components.party.stats.StatItem
import nl.t64.game.rpg.constants.Constant
import nl.t64.game.rpg.screens.inventory.tooltip.PersonalityTooltip


private const val FIRST_COLUMN_WIDTH = 190f
private const val SECOND_COLUMN_WIDTH = 40f
private const val THIRD_COLUMN_WIDTH = 35f

internal class StatsTable(tooltip: PersonalityTooltip) : BaseTable(tooltip) {

    init {
        table.columnDefaults(0).width(FIRST_COLUMN_WIDTH)
        table.columnDefaults(1).width(SECOND_COLUMN_WIDTH)
        table.columnDefaults(2).width(THIRD_COLUMN_WIDTH)

        container.add(table)
        container.background = createTopBorder()
        container.addListener(ListenerKeyVertical { updateIndex(it) })
    }

    override fun fillRows() {
        fillStats()
        fillExperience()
    }

    private fun updateIndex(deltaIndex: Int) {
        selectedIndex += deltaIndex
        if (selectedIndex < 0) {
            selectedIndex = selectedHero.getAllStats().size - 1
        } else {
            if (selectedIndex >= selectedHero.getAllStats().size) {
                selectedIndex = 0
            }
        }
        hasJustUpdated = true
    }

    private fun fillStats() {
        val statItemList = selectedHero.getAllStats()
        statItemList.indices.forEach { fillRow(statItemList[it], it) }
    }

    private fun fillExperience() {
        table.add("").row()
        fillRow("XP to Invest", selectedHero.getXpToInvest())
        fillRow("Total XP", selectedHero.getTotalXp())
        fillRow("Next Level", selectedHero.getXpNeededForNextLevel())
    }

    private fun fillRow(statItem: StatItem, index: Int) {
        val statTitle = Label(statItem.name, LabelStyle(font, Color.BLACK))
        table.add(statTitle)
        if (table.hasKeyboardFocus() && index == selectedIndex) {
            setSelected(statTitle, statItem)
        }
        table.add(statItem.rank.toString())
        val totalExtra = selectedHero.getExtraStatForVisualOf(statItem)
        addExtraToTable(totalExtra)
    }

    private fun setSelected(statTitle: Label, statItem: StatItem) {
        statTitle.style.fontColor = Constant.DARK_RED
        if (hasJustUpdated) {
            hasJustUpdated = false
            tooltip.setPosition(FIRST_COLUMN_WIDTH / 1.5f) { getTooltipY() }
            tooltip.refresh(statTitle, statItem)
        }
    }

    private fun fillRow(key: String, value: Int) {
        table.add(key)
        table.add(value.toString())
        table.add("").row()
    }

    private fun getTooltipY(): Float {
        val rowHeight = table.getRowHeight(0)
        return container.height - (rowHeight * selectedIndex) - (rowHeight * 0.5f)
    }

}
