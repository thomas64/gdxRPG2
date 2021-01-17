package nl.t64.game.rpg.screens.world.conversation;

import nl.t64.game.rpg.components.loot.Loot;


public interface ConversationObserver {

    void onNotifyExitConversation();

    void onNotifyShowMessageTooltip(String message);

    void onNotifyShowLevelUpDialog(String message);

    void onNotifyLoadShop();

    void onNotifyShowRewardDialog(Loot reward, String levelUpMessage);

    void onNotifyShowReceiveDialog(Loot receive);

    void onNotifyHeroJoined();

    void onNotifyHeroDismiss();

}
