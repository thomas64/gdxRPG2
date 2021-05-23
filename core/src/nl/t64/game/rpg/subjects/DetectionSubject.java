package nl.t64.game.rpg.subjects;

import com.badlogic.gdx.math.Circle;

import java.util.ArrayList;
import java.util.List;


public class DetectionSubject {

    private final List<DetectionObserver> observers = new ArrayList<>();

    public void addObserver(DetectionObserver observer) {
        observers.add(observer);
    }

    public void removeObserver(DetectionObserver observer) {
        observers.remove(observer);
    }

    public void removeAllObservers() {
        observers.clear();
    }

    public void notifyDetection(Circle detectionRange) {
        List.copyOf(observers).forEach(observer -> observer.onNotifyDetection(detectionRange));
    }

}
