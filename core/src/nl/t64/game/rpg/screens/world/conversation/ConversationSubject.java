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

    public void notifyExitConversation() {
        observers.forEach(ConversationObserver::onNotifyExitConversation);
    }

    public void notifyShowMessageTooltip(String message) {
        observers.forEach(observer -> observer.onNotifyShowMessageTooltip(message));
    }

    public void notifyShowLevelUpDialog(String message) {
        observers.forEach(observer -> observer.onNotifyShowLevelUpDialog(message));
    }

    public void notifyLoadShop() {
        observers.forEach(ConversationObserver::onNotifyLoadShop);
    }

    public void notifyShowRewardDialog(Loot reward, String levelUpMessage) {
        observers.forEach(observer -> observer.onNotifyShowRewardDialog(reward, levelUpMessage));
    }

    public void notifyShowReceiveDialog(Loot receive) {
        observers.forEach(observer -> observer.onNotifyShowReceiveDialog(receive));
    }

    public void notifyHeroJoined() {
        observers.forEach(ConversationObserver::onNotifyHeroJoined);
    }

    public void notifyHeroDismiss() {
        observers.forEach(ConversationObserver::onNotifyHeroDismiss);
    }

}
