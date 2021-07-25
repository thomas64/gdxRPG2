package nl.t64.game.rpg.components.condition

import nl.t64.game.rpg.Utils.gameData
import nl.t64.game.rpg.components.party.SkillItemId
import nl.t64.game.rpg.components.quest.QuestState


object ConditionDatabase {

    private val conditions: Map<String, () -> Boolean> = mapOf(
        Pair("diplomat2") { diplomat2() },
        Pair("diplomat3") { diplomat3() },
        Pair("key_mysterious_tunnel") { keyMysteriousTunnel() },
        Pair("3_starting_potions") { startingPotions() },
        Pair("quest4_known") { quest4Known() },
        Pair("i_quest6_task3") { quest6Task3() },
        Pair("quest6_known") { quest6Known() },
        Pair("i_quest6_unclaimed") { quest6Unclaimed() },
        Pair("i_quest7_task3") { quest7Task3() },
        Pair("quest7_unknown") { quest7Unknown() },
        Pair("i_quest7_unclaimed") { quest7Unclaimed() }
    )

    @JvmStatic
    fun isMeetingConditions(conditionIds: List<String?>): Boolean {
        return if (conditionIds.isEmpty()) {
            true
        } else {
            conditionIds.all { conditions[it]!!.invoke() }
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    private fun diplomat2(): Boolean {
        return hasEnoughOfSkill(SkillItemId.DIPLOMAT, 2)
    }

    private fun diplomat3(): Boolean {
        return hasEnoughOfSkill(SkillItemId.DIPLOMAT, 3)
    }

    private fun keyMysteriousTunnel(): Boolean {
        return hasEnoughOfItem("key_mysterious_tunnel", 1)
    }

    private fun startingPotions(): Boolean {
        return hasEnoughOfItem("healing_potion", 3)
    }

    private fun quest4Known(): Boolean {
        return isQuestStateAtLeast("quest0004", QuestState.KNOWN)
    }

    private fun quest6Task3(): Boolean {
        return isQuestTaskNumberComplete("quest0006", 3)
    }

    private fun quest6Known(): Boolean {
        return isQuestStateAtLeast("quest0006", QuestState.KNOWN)
    }

    private fun quest6Unclaimed(): Boolean {
        return isQuestStateAtLeast("quest0006", QuestState.UNCLAIMED)
    }

    private fun quest7Task3(): Boolean {
        return isQuestTaskNumberComplete("quest0007", 3)
    }

    private fun quest7Unknown(): Boolean {
        return isQuestStateAtMost("quest0007", QuestState.UNKNOWN)
    }

    private fun quest7Unclaimed(): Boolean {
        return isQuestStateAtLeast("quest0007", QuestState.UNCLAIMED)
    }

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
