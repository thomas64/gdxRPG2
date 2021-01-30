package nl.t64.game.rpg.components.conversation;

import nl.t64.game.rpg.Utils;
import nl.t64.game.rpg.components.party.SkillItemId;
import nl.t64.game.rpg.components.quest.QuestGraph;
import nl.t64.game.rpg.components.quest.QuestState;
import nl.t64.game.rpg.components.quest.QuestTask;

import java.util.Map;
import java.util.function.Supplier;


final class ConditionDatabase {

    private static ConditionDatabase instance;

    private final Map<String, Supplier<Boolean>> conditions;

    private ConditionDatabase() {
        this.conditions = Map.of("diplomat2", ConditionDatabase::diplomat2,
                                 "diplomat3", ConditionDatabase::diplomat3,
                                 "key_mysterious_tunnel", ConditionDatabase::keyMysteriousTunnel,
                                 "i_quest6_task3", ConditionDatabase::quest6Task3,
                                 "i_quest7_task3", ConditionDatabase::quest7Task3,
                                 "i_quest6_unclaimed", ConditionDatabase::quest6Unclaimed,
                                 "i_quest7_unclaimed", ConditionDatabase::quest7Unclaimed);
    }

    static ConditionDatabase getInstance() {
        if (instance == null) {
            instance = new ConditionDatabase();
        }
        return instance;
    }

    boolean isMeetingCondition(String conditionId) {
        return conditions.get(conditionId).get();
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    private static boolean diplomat2() {
        return isMeetingSkillCondition(SkillItemId.DIPLOMAT, 2);
    }

    private static boolean diplomat3() {
        return isMeetingSkillCondition(SkillItemId.DIPLOMAT, 3);
    }

    private static boolean keyMysteriousTunnel() {
        return isMeetingInventoryCondition("key_mysterious_tunnel", 1);
    }

    private static boolean quest6Task3() {
        return isMeetingTaskCondition("quest0006", 3);
    }

    private static boolean quest7Task3() {
        return isMeetingTaskCondition("quest0007", 3);
    }

    private static boolean quest6Unclaimed() {
        return isMeetingQuestCondition("quest0006", QuestState.UNCLAIMED);
    }

    private static boolean quest7Unclaimed() {
        return isMeetingQuestCondition("quest0007", QuestState.UNCLAIMED);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    private static boolean isMeetingSkillCondition(SkillItemId skillItemId, int rank) {
        return Utils.getGameData().getParty().hasEnoughOfSkill(skillItemId, rank);
    }

    private static boolean isMeetingInventoryCondition(String inventoryItemId, int amount) {
        return Utils.getGameData().getInventory().hasEnoughOfItem(inventoryItemId, amount);
    }

    private static boolean isMeetingTaskCondition(String questId, int taskNumber) {
        QuestGraph quest = Utils.getGameData().getQuests().getQuestById(questId);
        QuestTask questTask = quest.getAllTasks().get(taskNumber - 1);
        return questTask.isComplete();
    }

    private static boolean isMeetingQuestCondition(String questId, QuestState questState) {
        QuestGraph quest = Utils.getGameData().getQuests().getQuestById(questId);
        return quest.getCurrentState().isAtLeast(questState);
    }

}
