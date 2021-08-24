package nl.t64.game.rpg.components.quest

import com.badlogic.gdx.Gdx
import nl.t64.game.rpg.Utils
import nl.t64.game.rpg.screens.world.conversation.ConversationSubject


private const val QUEST_CONFIGS = "configs/quests/"
private const val FILE_LIST = QUEST_CONFIGS + "_files.txt"

class QuestContainer {

    private val quests: Map<String, QuestGraph> = Gdx.files.internal(FILE_LIST).readString()
        .split(System.lineSeparator())
        .map { Gdx.files.internal(QUEST_CONFIGS + it).readString() }
        .map { Utils.readValue<QuestGraph>(it) }
        .flatMap { it.toList() }
        .toMap()
        .onEach { (questId: String, quest: QuestGraph) -> quest.id = questId }

    fun getAllKnownQuests(): Array<QuestGraph> =
        quests.values
            .filter { !it.isHidden }
            .filter { it.currentState != QuestState.UNKNOWN }
            .sortedWith(compareBy({ it.isFailed }, { it.currentState }, { it.id }))
            .toTypedArray()

    fun isTaskNumberComplete(questId: String, taskNumber: Int): Boolean =
        getQuestById(questId).isTaskComplete(taskNumber.toString())

    fun isTaskComplete(questId: String, taskId: String?): Boolean =
        getQuestById(questId).isTaskComplete(taskId)

    fun setTaskComplete(questId: String, taskId: String) =
        getQuestById(questId).setTaskComplete(taskId)

    fun isCurrentStateEqualOrHigherThan(questId: String, questState: QuestState): Boolean =
        getQuestById(questId).isCurrentStateEqualOrHigherThan(questState)

    fun isCurrentStateEqualOrLowerThan(questId: String, questState: QuestState): Boolean =
        getQuestById(questId).isCurrentStateEqualOrLowerThan(questState)

    fun know(questId: String) =
        getQuestById(questId).know()

    fun accept(questId: String, observers: ConversationSubject) =
        getQuestById(questId).accept(observers)

    fun finish(questId: String) =
        getQuestById(questId).finish()

    fun handleTolerate(questId: String, observers: ConversationSubject) =
        getQuestById(questId).handleTolerate(observers)

    fun handleAccept(questId: String, continueConversation: (String) -> Unit, observers: ConversationSubject) =
        getQuestById(questId).handleAccept(continueConversation, observers)

    fun handleReceive(questId: String, observers: ConversationSubject) =
        getQuestById(questId).handleReceive(observers)

    fun handleReturn(questId: String, continueConversation: (String) -> Unit) =
        getQuestById(questId).handleReturn(continueConversation)

    fun handleAcceptOrReturn(questId: String, continueConversation: (String) -> Unit, observers: ConversationSubject) =
        getQuestById(questId).handleAcceptOrReturn(continueConversation, observers)

    fun handleReward(questId: String, endConversation: (String) -> Unit, observers: ConversationSubject) =
        getQuestById(questId).handleReward(endConversation, observers)

    fun handleFail(questId: String, observers: ConversationSubject) =
        getQuestById(questId).handleFail(observers)

    fun handleCheckIfLinkedIsKnown(questId: String, phraseId: String, continueConversation: (String) -> Unit) =
        getQuestById(questId).handleCheckIfLinkedIsKnown(phraseId, continueConversation)

    fun handleCheckIfAccepted(questId: String,
                              phraseId: String,
                              continueConversation: (String) -> Unit,
                              endConversation: (String) -> Unit) =
        getQuestById(questId).handleCheckIfAccepted(phraseId, continueConversation, endConversation)

    fun handleCheckIfAcceptedInventory(questId: String,
                                       taskId: String,
                                       phraseId: String,
                                       continueConversation: (String) -> Unit,
                                       endConversation: (String) -> Unit) =
        getQuestById(questId).handleCheckIfAcceptedInventory(taskId, phraseId, continueConversation, endConversation)


    fun contains(questId: String): Boolean = quests.containsKey(questId)
    fun getQuestById(questId: String): QuestGraph = quests[questId]!!

}
