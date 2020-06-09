package nl.t64.game.rpg.components.character;

import nl.t64.game.rpg.components.loot.Loot;


public interface ComponentObserver {

    void onNotifyShowConversationDialog(String conversationId, Character npcCharacter);

    void onNotifyShowNoteDialog(String noteId);

    void onNotifyShowLootDialog(Loot loot);

    void onNotifyShowLootDialog(Loot loot, String message);

    void onNotifyShowMessageDialog(String message);

}
