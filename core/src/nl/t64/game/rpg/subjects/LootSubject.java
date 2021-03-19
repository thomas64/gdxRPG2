package nl.t64.game.rpg.subjects;

import java.util.ArrayList;
import java.util.List;


public class LootSubject {

    private final List<LootObserver> observers = new ArrayList<>();

    public void addObserver(LootObserver observer) {
        observers.add(observer);
    }

    public void removeObserver(LootObserver observer) {
        observers.remove(observer);
    }

    public void removeAllObservers() {
        observers.clear();
    }

    public void notifyLootTaken() {
        observers.forEach(LootObserver::onNotifyLootTaken);
    }

    public void notifyRewardTaken() {
        observers.forEach(LootObserver::onNotifyRewardTaken);
    }

    public void notifyReceiveTaken() {
        observers.forEach(LootObserver::onNotifyReceiveTaken);
    }

}
