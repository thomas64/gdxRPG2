package nl.t64.game.rpg.screens.world.conversation;

import nl.t64.game.rpg.components.loot.Loot;

import java.util.ArrayList;
import java.util.List;


public class ConversationSubject {

    private final List<ConversationObserver> observers = new ArrayList<>();

    public void addObserver(ConversationObserver observer) {
        observers.add(observer);
    }

    public void removeObserver(ConversationObserver observer) {
        observers.remove(observer);
    }

    public void removeAllObservers() {
        observers.clear();
    }

    void notifyExitConversation() {
        observers.forEach(ConversationObserver::onNotifyExitConversation);
    }

    void notifyShowLevelUpDialog(String message) {
        observers.forEach(conversationObserver -> conversationObserver.onNotifyShowLevelUpDialog(message));
    }

    void notifyLoadShop() {
        observers.forEach(ConversationObserver::onNotifyLoadShop);
    }

    void notifyShowRewardDialog(Loot reward, String levelUpMessage) {
        observers.forEach(conversationObserver -> conversationObserver.onNotifyShowRewardDialog(reward,
                                                                                                levelUpMessage));
    }

    void notifyShowReceiveDialog(Loot receive) {
        observers.forEach(conversationObserver -> conversationObserver.onNotifyShowReceiveDialog(receive));
    }

    void notifyHeroJoined() {
        observers.forEach(ConversationObserver::onNotifyHeroJoined);
    }

    void notifyHeroDismiss() {
        observers.forEach(ConversationObserver::onNotifyHeroDismiss);
    }

}
