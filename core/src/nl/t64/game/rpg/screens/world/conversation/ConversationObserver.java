package nl.t64.game.rpg.screens.world.conversation;

import nl.t64.game.rpg.components.loot.Loot;


public interface ConversationObserver {

    void onNotifyExitConversation();

    void onNotifyShowMessageDialog(String message);

    void onNotifyLoadShop();

    void onNotifyShowRewardDialog(Loot reward);

    void onNotifyHeroJoined();

    void onNotifyHeroDismiss();

}
