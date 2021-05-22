package nl.t64.game.rpg.components.quest;

import lombok.AllArgsConstructor;


@AllArgsConstructor
public enum QuestState {
    UNKNOWN(0),
    KNOWN(1),
    ACCEPTED(2),
    UNCLAIMED(3),
    FINISHED(4);

    private final int index;

    public boolean isAtLeast(QuestState otherState) {
        return index >= otherState.index;
    }

    public boolean isAtMost(QuestState otherState) {
        return index <= otherState.index;
    }

}
