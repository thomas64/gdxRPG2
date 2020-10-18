package nl.t64.game.rpg.screens.world.conversation;

import nl.t64.game.rpg.audio.AudioEvent;
import nl.t64.game.rpg.components.loot.Loot;


public interface ConversationObserver {

    void onNotifyExitConversation();

    void onNotifyShowMessageDialog(String message, AudioEvent event);

    void onNotifyLoadShop();

    void onNotifyShowRewardDialog(Loot reward);

    void onNotifyShowReceiveDialog(Loot receive);

    void onNotifyHeroJoined();

    void onNotifyHeroDismiss();

}
