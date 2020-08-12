package nl.t64.game.rpg.screens.loot;

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

    void notifyLootTaken() {
        observers.forEach(LootObserver::onNotifyLootTaken);
    }

    void notifyRewardTaken() {
        observers.forEach(LootObserver::onNotifyRewardTaken);
    }

}
