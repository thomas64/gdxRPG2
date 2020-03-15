package nl.t64.game.rpg.conversation;

import lombok.Getter;


@SuppressWarnings("unused")
@Getter
public class ConversationChoice {

    private String sourceId;
    private String destinationId;
    private String text;

    public String toString() {
        return text;
    }

}
