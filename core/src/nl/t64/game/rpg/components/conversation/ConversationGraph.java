package nl.t64.game.rpg.components.conversation;

import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Map;


@Getter
public class ConversationGraph {

    private static final String DEFAULT_STARTING_PHRASE_ID = "1";

    @Setter
    private String currentPhraseId;
    private Map<String, ConversationPhrase> phrases;
    private Map<String, List<ConversationChoice>> choices;

    private ConversationGraph() {
        this.currentPhraseId = DEFAULT_STARTING_PHRASE_ID;
    }

    public ConversationPhrase getPhraseById(String id) {
        return phrases.get(id);
    }

    public List<ConversationChoice> getAssociatedChoices() {
        if (!choices.containsKey(currentPhraseId)) {
            String nextDestinationId = String.valueOf(Integer.parseInt(currentPhraseId) + 1);
            return List.of(new ConversationChoice(nextDestinationId));
        } else {
            return choices.get(currentPhraseId);
        }
    }

}
