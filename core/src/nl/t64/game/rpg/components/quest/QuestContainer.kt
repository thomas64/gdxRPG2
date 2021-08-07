package nl.t64.game.rpg.components.quest

import com.badlogic.gdx.Gdx
import nl.t64.game.rpg.Utils


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
            .filter { it.currentState != QuestState.UNKNOWN }
            .sortedWith(compareBy({ it.isFailed }, { it.currentState }, { it.id }))
            .toTypedArray()

    fun getQuestById(questId: String): QuestGraph = quests[questId]!!

}
