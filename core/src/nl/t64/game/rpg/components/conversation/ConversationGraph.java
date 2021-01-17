package nl.t64.game.rpg.components.conversation;

import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Map;


@Getter
public class ConversationGraph {

    private static final String DEFAULT_STARTING_PHRASE_ID = "1";

    @Setter
    String currentPhraseId;
    private Map<String, ConversationPhrase> phrases;

    private ConversationGraph() {
        this.currentPhraseId = DEFAULT_STARTING_PHRASE_ID;
    }

    public List<String> getCurrentPhrase() {
        return phrases.get(currentPhraseId).getText();
    }

    public List<ConversationChoice> getAssociatedChoices() {
        return phrases.get(currentPhraseId).getChoices(currentPhraseId);
    }

}
