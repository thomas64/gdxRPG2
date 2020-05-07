package nl.t64.game.rpg.components.character;


public interface ComponentObserver {

    void onNotifyShowConversationDialog(String conversationId, String characterId, Character npcCharacter);

}
