package nl.t64.game.rpg.subjects;

import com.badlogic.gdx.math.Rectangle;
import nl.t64.game.rpg.screens.world.entity.Direction;

import java.util.ArrayList;
import java.util.List;


public class CollisionSubject {

    private final List<CollisionObserver> observers = new ArrayList<>();

    public void addObserver(CollisionObserver observer) {
        observers.add(observer);
    }

    public void removeObserver(CollisionObserver observer) {
        observers.remove(observer);
    }

    public void removeAllObservers() {
        observers.clear();
    }

    public void notifyCollision(Rectangle playerBoundingBox, Direction playerDirection) {
        List.copyOf(observers).forEach(observer -> observer.onNotifyCollision(playerBoundingBox, playerDirection));
    }

}
