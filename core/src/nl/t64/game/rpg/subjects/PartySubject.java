package nl.t64.game.rpg.subjects;

import java.util.ArrayList;
import java.util.List;


public class PartySubject {

    private final List<PartyObserver> observers = new ArrayList<>();

    public void addObserver(PartyObserver observer) {
        observers.add(observer);
    }

    public void removeObserver(PartyObserver observer) {
        observers.remove(observer);
    }

    public void removeAllObservers() {
        observers.clear();
    }

    public void notifyHeroDismissed() {
        observers.forEach(PartyObserver::onNotifyHeroDismissed);
    }

}
