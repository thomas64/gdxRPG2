package nl.t64.game.rpg.subjects;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;
import java.util.List;


public class BumpSubject {

    private final List<BumpObserver> observers = new ArrayList<>();

    public void addObserver(BumpObserver observer) {
        observers.add(observer);
    }

    public void removeObserver(BumpObserver observer) {
        observers.remove(observer);
    }

    public void removeAllObservers() {
        observers.clear();
    }

    public void notifyBump(Rectangle biggerBoundingBox, Rectangle checkRect, Vector2 playerPosition) {
        List.copyOf(observers).forEach(observer -> observer.onNotifyBump(biggerBoundingBox, checkRect, playerPosition));
    }

}
