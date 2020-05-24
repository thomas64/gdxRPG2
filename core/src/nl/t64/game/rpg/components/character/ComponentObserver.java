package nl.t64.game.rpg.components.character;

import nl.t64.game.rpg.components.loot.Loot;


public interface ComponentObserver {

    void onNotifyShowConversationDialog(String conversationId, String characterId, Character npcCharacter);

    void onNotifyShowNoteDialog(String noteId);

    void onNotifyShowSparkleDialog(Loot sparkle);

}
