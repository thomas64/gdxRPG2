package nl.t64.game.rpg.components.conversation;

import java.util.HashMap;
import java.util.Map;


public class PhraseIdContainer {

    private final Map<String, String> currentPhraseIds;

    PhraseIdContainer() {
        this.currentPhraseIds = new HashMap<>();
    }

    String getPhraseId(String conversationId) {
        return currentPhraseIds.get(conversationId);
    }

    void setPhraseId(String conversationId, String phraseId) {
        currentPhraseIds.put(conversationId, phraseId);
    }

    int getSize() {
        return currentPhraseIds.size();
    }

}
