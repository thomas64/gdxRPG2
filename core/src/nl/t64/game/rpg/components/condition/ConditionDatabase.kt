package nl.t64.game.rpg.components.condition

import nl.t64.game.rpg.Utils.gameData
import nl.t64.game.rpg.components.party.skills.SkillItemId
import nl.t64.game.rpg.components.quest.QuestState


object ConditionDatabase {

    private val conditions: Map<String, () -> Boolean> = mapOf(
        Pair("quest_intro_not_finished") { questIntroNotFinished },
        Pair("3_starting_potions") { startingPotions },
        Pair("grace_ribbon") { graceRibbon },
        Pair("first_equipment_item") { firstEquipmentItem },
        Pair("diplomat2") { diplomat2 },
        Pair("i_mask_of_ardor") { maskOfArdor },
        Pair("defeated_orc_guards") { defeatedOrcGuards },

        Pair("diplomat3") { diplomat3 },
        Pair("key_mysterious_tunnel") { keyMysteriousTunnel },
        Pair("quest4_known") { quest4Known },
        Pair("i_quest6_task3") { quest6Task3 },
        Pair("quest6_known") { quest6Known },
        Pair("i_quest6_unclaimed") { quest6Unclaimed },
        Pair("i_quest7_task3") { quest7Task3 },
        Pair("quest7_unknown") { quest7Unknown },
        Pair("i_quest7_unclaimed") { quest7Unclaimed }
    )

    fun isMeetingConditions(conditionIds: List<String?>): Boolean {
        return when {
            conditionIds.isEmpty() -> true
            else -> conditionIds.all { conditions[it]!!.invoke() }
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    private val questIntroNotFinished get() = !isQuestTaskNumberComplete("quest_intro", 1)
    private val startingPotions get() = hasEnoughOfItem("healing_potion", 3)
    private val graceRibbon get() = hasEnoughOfItem("grace_ribbon", 1)
    private val firstEquipmentItem get() = hasEnoughOfOneOfTheseItems("basic_light_helmet", "basic_light_boots")
    private val diplomat2 get() = hasEnoughOfSkill(SkillItemId.DIPLOMAT, 2)
    private val maskOfArdor get() = hasEnoughOfItem("mask_of_ardor", 1)
    private val defeatedOrcGuards get() = isBattleWon("quest_orc_guards")

    private val diplomat3 get() = hasEnoughOfSkill(SkillItemId.DIPLOMAT, 3)
    private val keyMysteriousTunnel get() = hasEnoughOfItem("key_mysterious_tunnel", 1)
    private val quest4Known get() = isQuestStateAtLeast("quest0004", QuestState.KNOWN)
    private val quest6Task3 get() = isQuestTaskNumberComplete("quest0006", 3)
    private val quest6Known get() = isQuestStateAtLeast("quest0006", QuestState.KNOWN)
    private val quest6Unclaimed get() = isQuestStateAtLeast("quest0006", QuestState.UNCLAIMED)
    private val quest7Task3 get() = isQuestTaskNumberComplete("quest0007", 3)
    private val quest7Unknown get() = isQuestStateAtMost("quest0007", QuestState.UNKNOWN)
    private val quest7Unclaimed get() = isQuestStateAtLeast("quest0007", QuestState.UNCLAIMED)

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    private fun hasEnoughOfSkill(skillItemId: SkillItemId, rank: Int): Boolean =
        gameData.party.hasEnoughOfSkill(skillItemId, rank)

    private fun hasEnoughOfOneOfTheseItems(vararg inventoryItemIds: String): Boolean =
        inventoryItemIds.any { hasEnoughOfItem(it, 1) }

    private fun hasEnoughOfItem(inventoryItemId: String, amount: Int): Boolean =
        gameData.inventory.hasEnoughOfItem(inventoryItemId, amount)
                || gameData.party.getAllHeroes().count { it.hasInventoryItem(inventoryItemId) } >= amount

    private fun isQuestTaskNumberComplete(questId: String, taskNumber: Int): Boolean =
        gameData.quests.isTaskNumberComplete(questId, taskNumber)

    private fun isQuestStateAtLeast(questId: String, questState: QuestState): Boolean =
        gameData.quests.isCurrentStateEqualOrHigherThan(questId, questState)

    private fun isQuestStateAtMost(questId: String, questState: QuestState): Boolean =
        gameData.quests.isCurrentStateEqualOrLowerThan(questId, questState)

    private fun isBattleWon(battleId: String): Boolean =
        gameData.battles.isBattleWon(battleId)


}
