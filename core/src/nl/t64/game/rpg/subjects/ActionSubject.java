package nl.t64.game.rpg.subjects;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import nl.t64.game.rpg.screens.world.entity.Direction;

import java.util.ArrayList;
import java.util.List;


public class ActionSubject {

    private final List<ActionObserver> observers = new ArrayList<>();

    public void addObserver(ActionObserver observer) {
        observers.add(observer);
    }

    public void removeObserver(ActionObserver observer) {
        observers.remove(observer);
    }

    public void removeAllObservers() {
        observers.clear();
    }

    public void notifyActionPressed(Rectangle checkRect, Direction playerDirection, Vector2 playerPosition) {
        List.copyOf(observers).forEach(observer -> observer.onNotifyActionPressed(checkRect,
                                                                                  playerDirection,
                                                                                  playerPosition));
    }

}
