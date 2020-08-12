package nl.t64.game.rpg.components.quest;

import java.util.HashMap;
import java.util.Map;


public class StateContainer {

    private final Map<String, QuestState> currentStates;

    StateContainer() {
        this.currentStates = new HashMap<>();
    }

    QuestState getState(String questId) {
        return currentStates.get(questId);
    }

    void setState(String questId, QuestState state) {
        currentStates.put(questId, state);
    }

    int getSize() {
        return currentStates.size();
    }

}
