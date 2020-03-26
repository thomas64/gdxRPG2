package nl.t64.game.rpg.conversation;

import lombok.Getter;


@Getter
public class ConversationChoice {

    private static final String DEFAULT_ANSWER_TEXT = "->";

    private String text;
    private String destinationId;

    public ConversationChoice() {
        this.text = DEFAULT_ANSWER_TEXT;
    }

    public ConversationChoice(String destinationId) {
        this.text = DEFAULT_ANSWER_TEXT;
        this.destinationId = destinationId;
    }

    public String toString() {
        return text;
    }

}
