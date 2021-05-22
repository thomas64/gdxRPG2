package nl.t64.game.rpg.components.condition;

import nl.t64.game.rpg.Utils;
import nl.t64.game.rpg.components.party.SkillItemId;
import nl.t64.game.rpg.components.quest.QuestGraph;
import nl.t64.game.rpg.components.quest.QuestState;
import nl.t64.game.rpg.components.quest.QuestTask;

import java.util.AbstractMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;


public final class ConditionDatabase {

    private static ConditionDatabase instance;

    private final Map<String, Supplier<Boolean>> conditions;

    private ConditionDatabase() {
        this.conditions = Map.ofEntries(create("diplomat2", ConditionDatabase::diplomat2),
                                        create("diplomat3", ConditionDatabase::diplomat3),
                                        create("key_mysterious_tunnel", ConditionDatabase::keyMysteriousTunnel),
                                        create("3_starting_potions", ConditionDatabase::startingPotions),
                                        create("quest4_known", ConditionDatabase::quest4Known),
                                        create("i_quest6_task3", ConditionDatabase::quest6Task3),
                                        create("quest6_known", ConditionDatabase::quest6Known),
                                        create("i_quest6_unclaimed", ConditionDatabase::quest6Unclaimed),
                                        create("i_quest7_task3", ConditionDatabase::quest7Task3),
                                        create("quest7_unknown", ConditionDatabase::quest7Unknown),
                                        create("i_quest7_unclaimed", ConditionDatabase::quest7Unclaimed)
        );
    }

    private static Map.Entry<String, Supplier<Boolean>> create(String key, Supplier<Boolean> value) {
        return new AbstractMap.SimpleImmutableEntry<>(key, value);
    }

    public static ConditionDatabase getInstance() {
        if (instance == null) {
            instance = new ConditionDatabase();
        }
        return instance;
    }

    public boolean isMeetingConditions(List<String> conditionIds) {
        if (conditionIds.isEmpty()) {
            return true;
        } else {
            return conditionIds.stream().allMatch(id -> conditions.get(id).get());
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    private static boolean diplomat2() {
        return hasEnoughOfSkill(SkillItemId.DIPLOMAT, 2);
    }

    private static boolean diplomat3() {
        return hasEnoughOfSkill(SkillItemId.DIPLOMAT, 3);
    }

    private static boolean keyMysteriousTunnel() {
        return hasEnoughOfItem("key_mysterious_tunnel", 1);
    }

    private static boolean startingPotions() {
        return hasEnoughOfItem("healing_potion", 3);
    }

    private static boolean quest4Known() {
        return isQuestStateAtLeast("quest0004", QuestState.KNOWN);
    }

    private static boolean quest6Task3() {
        return isQuestTaskNumberComplete("quest0006", 3);
    }

    private static boolean quest6Known() {
        return isQuestStateAtLeast("quest0006", QuestState.KNOWN);
    }

    private static boolean quest6Unclaimed() {
        return isQuestStateAtLeast("quest0006", QuestState.UNCLAIMED);
    }

    private static boolean quest7Task3() {
        return isQuestTaskNumberComplete("quest0007", 3);
    }

    private static boolean quest7Unknown() {
        return isQuestStateAtMost("quest0007", QuestState.UNKNOWN);
    }

    private static boolean quest7Unclaimed() {
        return isQuestStateAtLeast("quest0007", QuestState.UNCLAIMED);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    private static boolean hasEnoughOfSkill(SkillItemId skillItemId, int rank) {
        return Utils.getGameData().getParty().hasEnoughOfSkill(skillItemId, rank);
    }

    private static boolean hasEnoughOfItem(String inventoryItemId, int amount) {
        return Utils.getGameData().getInventory().hasEnoughOfItem(inventoryItemId, amount);
    }

    private static boolean isQuestTaskNumberComplete(String questId, int taskNumber) {
        QuestGraph quest = Utils.getGameData().getQuests().getQuestById(questId);
        QuestTask questTask = quest.getAllTasks().get(taskNumber - 1);
        return questTask.isComplete();
    }

    private static boolean isQuestStateAtLeast(String questId, QuestState questState) {
        QuestGraph quest = Utils.getGameData().getQuests().getQuestById(questId);
        return quest.getCurrentState().isAtLeast(questState);
    }

    private static boolean isQuestStateAtMost(String questId, QuestState questState) {
        QuestGraph quest = Utils.getGameData().getQuests().getQuestById(questId);
        return quest.getCurrentState().isAtMost(questState);
    }

}
