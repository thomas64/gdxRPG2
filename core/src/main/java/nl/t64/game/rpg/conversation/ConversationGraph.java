package nl.t64.game.rpg.conversation;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashMap;
import java.util.List;


@NoArgsConstructor
public class ConversationGraph {

    @Getter
    @Setter
    private String currentConversationId;
    private HashMap<String, Conversation> conversations;
    private HashMap<String, List<ConversationChoice>> associatedChoices;

    public Conversation getConversationById(String id) {
        return conversations.get(id);
    }

    public List<ConversationChoice> getCurrentChoices() {
        return associatedChoices.get(currentConversationId);
    }

}
