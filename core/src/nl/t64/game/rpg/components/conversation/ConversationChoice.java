package nl.t64.game.rpg.components.conversation;

import lombok.Getter;
import nl.t64.game.rpg.GameData;
import nl.t64.game.rpg.Utils;
import nl.t64.game.rpg.components.quest.QuestGraph;
import nl.t64.game.rpg.components.quest.QuestTask;

import java.util.Map;


@Getter
public class ConversationChoice {

    private static final String DEFAULT_ANSWER_TEXT = "->";
    private static final String DEFAULT_DESTINATION_ID = "1";
    private static final ConversationCommand DEFAULT_CONVERSATION_COMMAND = ConversationCommand.NONE;
    private static final Map<String, Integer> DEFAULT_CONDITION = Map.of();

    private String text;
    private String destinationId;
    private ConversationCommand conversationCommand;
    private Map<String, Integer> condition;

    private ConversationChoice() {
        this.text = DEFAULT_ANSWER_TEXT;
        this.destinationId = DEFAULT_DESTINATION_ID;
        this.conversationCommand = DEFAULT_CONVERSATION_COMMAND;
        this.condition = DEFAULT_CONDITION;
    }

    ConversationChoice(String destinationId) {
        this.text = DEFAULT_ANSWER_TEXT;
        this.destinationId = destinationId;
        this.conversationCommand = DEFAULT_CONVERSATION_COMMAND;
        this.condition = DEFAULT_CONDITION;
    }

    public String toString() {
        return text;
    }

    public boolean isMeetingCondition(String conversationId) {
        if (condition.isEmpty()) {
            return true;
        } else {
            return isMeetingQuestOrTaskOrInventoryOrSkillCondition(conversationId);
        }
    }

    private boolean isMeetingQuestOrTaskOrInventoryOrSkillCondition(String conversationId) {
        GameData gameData = Utils.getGameData();
        String key = getConditionEntry().getKey();
        int value = getConditionEntry().getValue();
        if (key.startsWith("quest")) {
            QuestGraph quest = gameData.getQuests().getQuestById(key);
            return quest.getCurrentState().isStateAtLeastOfIndex(value);
        }
        if (key.equals("task")) {
            QuestGraph quest = gameData.getQuests().getQuestById(conversationId);
            QuestTask questTask = quest.getAllTasks().get(value - 1);
            return questTask.isComplete();
        }
        if (gameData.getInventory().contains(condition)) {
            return true;
        }
        int bestSkillLevel = gameData.getParty().getBestSkillLevel(key);
        return bestSkillLevel >= value;
    }

    private Map.Entry<String, Integer> getConditionEntry() {
        return condition.entrySet().iterator().next();
    }

}
