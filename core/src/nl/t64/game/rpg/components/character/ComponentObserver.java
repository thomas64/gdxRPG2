package nl.t64.game.rpg.components.character;

import nl.t64.game.rpg.audio.AudioEvent;
import nl.t64.game.rpg.components.loot.Loot;


public interface ComponentObserver {

    void onNotifyShowConversationDialog(String conversationId, Character npcCharacter);

    void onNotifyShowNoteDialog(String noteId);

    void onNotifyShowFindDialog(Loot loot, AudioEvent event, String message);

    void onNotifyShowFindDialog(Loot loot, AudioEvent event);

    void onNotifyShowMessageDialog(String message);

}
