package nl.t64.game.rpg.components.conversation;

import lombok.Getter;


@Getter
public class ConversationChoice {

    private static final String DEFAULT_ANSWER_TEXT = "->";
    private static final String DEFAULT_DESTINATION_ID = "1";
    private static final ConversationCommand DEFAULT_CONVERSATION_COMMAND = ConversationCommand.NONE;

    private String text;
    private String destinationId;
    private ConversationCommand conversationCommand;

    private ConversationChoice() {
        this.text = DEFAULT_ANSWER_TEXT;
        this.destinationId = DEFAULT_DESTINATION_ID;
        this.conversationCommand = DEFAULT_CONVERSATION_COMMAND;
    }

    ConversationChoice(String destinationId) {
        this.text = DEFAULT_ANSWER_TEXT;
        this.destinationId = destinationId;
        this.conversationCommand = DEFAULT_CONVERSATION_COMMAND;
    }

    public String toString() {
        return text;
    }

}