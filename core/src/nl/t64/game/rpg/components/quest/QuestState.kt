package nl.t64.game.rpg.components.quest


enum class QuestState(val index: Int) {
    UNKNOWN(0),
    KNOWN(1),
    ACCEPTED(2),
    UNCLAIMED(3),
    FINISHED(4);

    fun isAtLeast(otherState: QuestState): Boolean = index >= otherState.index
    fun isAtMost(otherState: QuestState): Boolean = index <= otherState.index

}
