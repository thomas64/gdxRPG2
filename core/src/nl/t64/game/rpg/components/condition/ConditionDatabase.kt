package nl.t64.game.rpg.components.condition

import nl.t64.game.rpg.Utils.gameData
import nl.t64.game.rpg.components.party.skills.SkillItemId
import nl.t64.game.rpg.components.quest.QuestState


object ConditionDatabase {

    private val conditions: Map<String, () -> Boolean> = mapOf(
        Pair("diplomat2") { diplomat2 },
        Pair("diplomat3") { diplomat3 },
        Pair("key_mysterious_tunnel") { keyMysteriousTunnel },
        Pair("3_starting_potions") { startingPotions },
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

    private val diplomat2 get() = hasEnoughOfSkill(SkillItemId.DIPLOMAT, 2)
    private val diplomat3 get() = hasEnoughOfSkill(SkillItemId.DIPLOMAT, 3)
    private val keyMysteriousTunnel get() = hasEnoughOfItem("key_mysterious_tunnel", 1)
    private val startingPotions get() = hasEnoughOfItem("healing_potion", 3)
    private val quest4Known get() = isQuestStateAtLeast("quest0004", QuestState.KNOWN)
    private val quest6Task3 get() = isQuestTaskNumberComplete("quest0006", 3)
    private val quest6Known get() = isQuestStateAtLeast("quest0006", QuestState.KNOWN)
    private val quest6Unclaimed get() = isQuestStateAtLeast("quest0006", QuestState.UNCLAIMED)
    private val quest7Task3 get() = isQuestTaskNumberComplete("quest0007", 3)
    private val quest7Unknown get() = isQuestStateAtMost("quest0007", QuestState.UNKNOWN)
    private val quest7Unclaimed get() = isQuestStateAtLeast("quest0007", QuestState.UNCLAIMED)

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    private fun hasEnoughOfSkill(skillItemId: SkillItemId, rank: Int): Boolean {
        return gameData.party.hasEnoughOfSkill(skillItemId, rank)
    }

    private fun hasEnoughOfItem(inventoryItemId: String, amount: Int): Boolean {
        return gameData.inventory.hasEnoughOfItem(inventoryItemId, amount)
    }

    private fun isQuestTaskNumberComplete(questId: String, taskNumber: Int): Boolean {
        val quest = gameData.quests.getQuestById(questId)
        val questTask = quest.getAllTasks()[taskNumber - 1]
        return questTask.isComplete
    }

    private fun isQuestStateAtLeast(questId: String, questState: QuestState): Boolean {
        val quest = gameData.quests.getQuestById(questId)
        return quest.currentState.isAtLeast(questState)
    }

    private fun isQuestStateAtMost(questId: String, questState: QuestState): Boolean {
        val quest = gameData.quests.getQuestById(questId)
        return quest.currentState.isAtMost(questState)
    }

}
