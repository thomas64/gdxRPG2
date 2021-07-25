package nl.t64.game.rpg.subjects

import nl.t64.game.rpg.audio.AudioEvent
import nl.t64.game.rpg.components.loot.Loot
import nl.t64.game.rpg.screens.world.entity.Entity


interface ComponentObserver {

    fun onNotifyShowConversationDialog(conversationId: String, npcEntity: Entity)
    fun onNotifyShowConversationDialog(conversationId: String, entityId: String)
    fun onNotifyShowNoteDialog(noteId: String)
    fun onNotifyShowFindDialog(loot: Loot, event: AudioEvent, message: String)
    fun onNotifyShowFindDialog(loot: Loot, event: AudioEvent)
    fun onNotifyShowMessageDialog(message: String)
    fun onNotifyShowBattleScreen(battleId: String)

}
