package nl.t64.game.rpg.subjects;

import nl.t64.game.rpg.audio.AudioEvent;
import nl.t64.game.rpg.components.loot.Loot;
import nl.t64.game.rpg.screens.world.entity.Entity;


public interface ComponentObserver {

    void onNotifyShowConversationDialog(String conversationId, Entity npcEntity);

    void onNotifyShowConversationDialog(String conversationId, String entityId);

    void onNotifyShowNoteDialog(String noteId);

    void onNotifyShowFindDialog(Loot loot, AudioEvent event, String message);

    void onNotifyShowFindDialog(Loot loot, AudioEvent event);

    void onNotifyShowMessageDialog(String message);

}
