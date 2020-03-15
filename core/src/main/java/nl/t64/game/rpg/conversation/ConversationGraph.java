package nl.t64.game.rpg.conversation;

import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.List;


@SuppressWarnings({"unused", "MismatchedQueryAndUpdateOfCollection"})
@NoArgsConstructor
public class ConversationGraph {

    private String currentConversationId;
    private HashMap<String, Conversation> conversations;
    private HashMap<String, List<ConversationChoice>> associatedChoices;

    public List<ConversationChoice> getCurrentChoices() {
        return associatedChoices.get(currentConversationId);
    }

    public String getCurrentConversationId() {
        return currentConversationId;
    }

    public void setCurrentConversation(String id) {
        currentConversationId = id;
    }

    public Conversation getConversationByID(String id) {
        return conversations.get(id);
    }

}
