package nl.t64.game.rpg.components.event;

import lombok.Getter;
import nl.t64.game.rpg.Utils;
import nl.t64.game.rpg.components.quest.QuestGraph;
import nl.t64.game.rpg.components.quest.QuestState;

import java.util.Map;
import java.util.function.BiConsumer;


@Getter
public class Event {

    private boolean hasPlayed;
    private Map<String, String> condition;
    private String conversationId;
    private String characterId;

    private Event() {
        this.hasPlayed = false;
    }

    public void startConversation(BiConsumer<String, String> notifyShowConversationDialog) {
        if (!hasPlayed && isMeetingCondition()) {
            hasPlayed = true;
            notifyShowConversationDialog.accept(conversationId, characterId);
        }
    }

    // todo, een condition is ook niet altijd op een andere quest gebaseerd. ook aanpassen wanneer dat zich aandient.
    private boolean isMeetingCondition() {
        return condition.entrySet()
                        .stream()
                        .noneMatch(entry -> isFailingCondition(entry.getKey(), entry.getValue()));
    }

    private boolean isFailingCondition(String key, String value) {
        QuestGraph quest = Utils.getGameData().getQuests().getQuestById(key);
        if (value.startsWith("!")) {
            QuestState state = QuestState.valueOf(value.substring(1));
            return quest.getCurrentState().equals(state);
        } else {
            QuestState state = QuestState.valueOf(value);
            return !quest.getCurrentState().equals(state);
        }
    }

}
