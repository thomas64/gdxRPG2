package nl.t64.game.rpg.components.party.spells

import com.fasterxml.jackson.annotation.JsonProperty
import nl.t64.game.rpg.components.party.PersonalityItem
import kotlin.math.roundToInt


private val TRAINING_COSTS = listOf(20, 8, 12, 16, 20, 24, 28, 32, 36, 40, 0)
private const val MAXIMUM = 10

/* todo: moeten alle stats en skills ipv in individuele classes in json komen net zoals spells?
    wellicht komt er nog een hoop verschillende logica in de verschillende classes. dan is json niet handig. */

class SpellItem(
    val name: String = "",
    private val school: SchoolType = SchoolType.UNKNOWN,
    val sort: Int = 0,
    private val upgrade: Float = 0f,
    @JsonProperty("min_wizard") private val minWizard: Int = 0,
    @JsonProperty("resource") private val requiredResource: ResourceType = ResourceType.GOLD,
    @JsonProperty("stamina_cost") private val staminaCost: Int = 0,
    private val range: Int = 0,
    @JsonProperty("number_of_targets") private val numberOfTargets: NumberOfTargets = NumberOfTargets.ONE,
    private val target: Target = Target.EVERYONE,
    private val damage: Int = 0,
    private val description: List<String> = emptyList()
) : PersonalityItem {

    lateinit var id: String
    var rank: Int = 0
    private var bonus: Int = 0

    fun createCopy(rank: Int): SpellItem {
        val spellCopy = SpellItem(
            name, school, sort, upgrade, minWizard, requiredResource,
            staminaCost, range, numberOfTargets, target, damage, description)
        spellCopy.id = id
        spellCopy.rank = rank
        return spellCopy
    }

    override fun getDescription(totalLoremaster: Int): String {
        return (description.joinToString(System.lineSeparator()) + System.lineSeparator()
                + System.lineSeparator()
                + "School: " + school.title + System.lineSeparator()
                + "Requires: " + requiredResource.title + System.lineSeparator()
                + "Stamina cost: " + staminaCost + System.lineSeparator()
                + System.lineSeparator()
                + getNeededXpForNextLevel(totalLoremaster) + System.lineSeparator()
                + getNeededGoldForNextLevel())
    }

    private fun getNeededXpForNextLevel(totalLoremaster: Int): String {
        val xpNeeded = getXpCostForNextLevel(totalLoremaster).toString().takeIf { it != "0" } ?: "Max"
        return "XP needed for next level: $xpNeeded"
    }

    private fun getNeededGoldForNextLevel(): String {
        val goldNeeded = getGoldCostForNextLevel().toString().takeIf { it != "0" } ?: "Max"
        return "Gold needed for next level: $goldNeeded"
    }

    private fun getXpCostForNextLevel(totalLoremaster: Int): Int {
        return when {
            rank >= MAXIMUM -> 0
            else -> (getUpgradeFormula() - ((getUpgradeFormula() / 100) * totalLoremaster)).roundToInt()
        }
    }

    private fun getGoldCostForNextLevel(): Int {
        val nextLevel = rank + 1
        return TRAINING_COSTS[nextLevel - 1]
    }

    private fun getUpgradeFormula(): Float {
        val nextLevel = rank + 1
        return upgrade * (nextLevel * nextLevel)
    }

}
