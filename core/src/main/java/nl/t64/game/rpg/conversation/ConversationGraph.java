package nl.t64.game.rpg.conversation;

import lombok.Getter;
import lombok.Setter;

import java.util.Collections;
import java.util.List;
import java.util.Map;


@Getter
public class ConversationGraph {

    static final String DEFAULT_STARTING_PHRASE_ID = "1";

    @Setter
    private String currentPhraseId;
    private Map<String, ConversationPhrase> phrases;
    private Map<String, List<ConversationChoice>> choices;

    public ConversationGraph() {
        this.currentPhraseId = DEFAULT_STARTING_PHRASE_ID;
    }

    public ConversationPhrase getPhraseById(String id) {
        return phrases.get(id);
    }

    public List<ConversationChoice> getAssociatedChoices() {
        if (!choices.containsKey(currentPhraseId)) {
            String nextPhraseId = String.valueOf(Integer.parseInt(currentPhraseId) + 1);
            return Collections.singletonList(new ConversationChoice(nextPhraseId));
        } else {
            return choices.get(currentPhraseId);
        }
    }

}
