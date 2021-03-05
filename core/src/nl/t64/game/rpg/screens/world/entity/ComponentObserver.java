package nl.t64.game.rpg.screens.world.entity;

import nl.t64.game.rpg.audio.AudioEvent;
import nl.t64.game.rpg.components.loot.Loot;


public interface ComponentObserver {

    void onNotifyShowConversationDialog(String conversationId, Entity npcCharacter);

    void onNotifyShowConversationDialog(String conversationId, String characterId);

    void onNotifyShowNoteDialog(String noteId);

    void onNotifyShowFindDialog(Loot loot, AudioEvent event, String message);

    void onNotifyShowFindDialog(Loot loot, AudioEvent event);

    void onNotifyShowMessageDialog(String message);

}
