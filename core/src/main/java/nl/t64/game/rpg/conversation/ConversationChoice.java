package nl.t64.game.rpg.conversation;

import lombok.Getter;


public class ConversationChoice {

    private String text;
    @Getter
    private String destinationId;

    public String toString() {
        return text;
    }

}
