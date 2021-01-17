package nl.t64.game.rpg.components.conversation;

import lombok.Getter;

import java.util.List;


@Getter
public class ConversationPhrase {

    private List<String> text;
    private List<ConversationChoice> choices;

    private ConversationPhrase() {
        this.text = null;   // mandatory in Json file
        this.choices = List.of();
    }

    List<ConversationChoice> getChoices(String currentPhraseId) {
        if (choices.isEmpty()) {
            return createArrowChoiceThatPointsToNextPhraseId(currentPhraseId);
        } else {
            return choices;
        }
    }

    private List<ConversationChoice> createArrowChoiceThatPointsToNextPhraseId(String currentPhraseId) {
        String nextDestinationId = String.valueOf(Integer.parseInt(currentPhraseId) + 1);
        return List.of(new ConversationChoice(nextDestinationId));
    }

}
