package nl.t64.game.rpg.components.character;

import java.util.List;


public interface ComponentObserver {

    void onNotifyShowConversationDialog(List<String> conversationIds, String characterId, Character npcCharacter);

}
