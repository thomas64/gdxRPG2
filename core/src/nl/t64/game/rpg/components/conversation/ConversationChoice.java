package nl.t64.game.rpg.components.conversation;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;


@Getter
public class ConversationChoice {

    private static final String DEFAULT_ANSWER_TEXT = "->";
    private static final String DEFAULT_DESTINATION_ID = "1";
    private static final ConversationCommand DEFAULT_CONVERSATION_COMMAND = ConversationCommand.NONE;
    private static final String DEFAULT_CONDITION = "";
    private static final String INVISIBLE_PREFIX = "i_";

    private String text;
    private String destinationId;
    private ConversationCommand conversationCommand;
    @JsonProperty("condition")
    private String conditionId;

    private ConversationChoice() {
        this.text = DEFAULT_ANSWER_TEXT;
        this.destinationId = DEFAULT_DESTINATION_ID;
        this.conversationCommand = DEFAULT_CONVERSATION_COMMAND;
        this.conditionId = DEFAULT_CONDITION;
    }

    ConversationChoice(String destinationId) {
        this.text = DEFAULT_ANSWER_TEXT;
        this.destinationId = destinationId;
        this.conversationCommand = DEFAULT_CONVERSATION_COMMAND;
        this.conditionId = DEFAULT_CONDITION;
    }

    public String toString() {
        return text;
    }

    boolean isVisible() {
        return !conditionId.startsWith(INVISIBLE_PREFIX) || isMeetingCondition();
    }

    public boolean isMeetingCondition() {
        if (conditionId.isEmpty()) {
            return true;
        } else {
            return ConditionDatabase.getInstance().isMeetingCondition(conditionId);
        }
    }

}
