package nl.t64.game.rpg.screens.world.conversation;

import nl.t64.game.rpg.components.loot.Loot;


public interface ConversationObserver {

    default void onNotifyExitConversation() {
        throw new IllegalCallerException("Implement this method in child.");
    }

    default void onNotifyShowMessageTooltip(String message) {
        throw new IllegalCallerException("Implement this method in child.");
    }

    default void onNotifyShowLevelUpDialog(String message) {
        throw new IllegalCallerException("Implement this method in child.");
    }

    default void onNotifyLoadShop() {
        throw new IllegalCallerException("Implement this method in child.");
    }

    default void onNotifyShowRewardDialog(Loot reward, String levelUpMessage) {
        throw new IllegalCallerException("Implement this method in child.");
    }

    default void onNotifyShowReceiveDialog(Loot receive) {
        throw new IllegalCallerException("Implement this method in child.");
    }

    default void onNotifyHeroJoined() {
        throw new IllegalCallerException("Implement this method in child.");
    }

    default void onNotifyHeroDismiss() {
        throw new IllegalCallerException("Implement this method in child.");
    }

}
