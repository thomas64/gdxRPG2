package nl.t64.game.rpg.screens.world;

import com.badlogic.gdx.graphics.Color;

import java.util.ArrayList;
import java.util.List;


public class MapSubject {

    private final List<MapObserver> observers = new ArrayList<>();

    public void addObserver(MapObserver observer) {
        observers.add(observer);
    }

    public void removeObserver(MapObserver observer) {
        observers.remove(observer);
    }

    public void removeAllObservers() {
        observers.clear();
    }

    public void notifyMapWillChange(Runnable changeMap, Color transitionColor) {
        observers.forEach(mapObserver -> mapObserver.onNotifyMapWillChange(changeMap, transitionColor));
    }

    public void notifyMapChanged(GameMap currentMap) {
        observers.forEach(mapObserver -> mapObserver.onNotifyMapChanged(currentMap));
    }

}
