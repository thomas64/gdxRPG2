package nl.t64.game.rpg.subjects;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import nl.t64.game.rpg.constants.Constant;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


public class BlockSubject {

    private final List<BlockObserver> observers = new ArrayList<>();

    public void addObserver(BlockObserver observer) {
        observers.add(observer);
    }

    public void removeObserver(BlockObserver observer) {
        observers.remove(observer);
    }

    public void removeAllObservers() {
        observers.clear();
    }

    public List<Rectangle> getCurrentBlockersFor(Rectangle boundingBox) {
        return List.copyOf(observers).stream()
                   .map(observer -> observer.getBlockerFor(boundingBox))
                   .flatMap(Optional::stream)
                   .collect(Collectors.toUnmodifiableList());
    }

    public boolean isCurrentlyBlocking(float x, float y) {
        return List.copyOf(observers).stream()
                   .anyMatch(observer -> observer.isBlocking(getFormattedPoint(x, y)));
    }

    private Vector2 getFormattedPoint(float x, float y) {
        return new Vector2(x * Constant.HALF_TILE_SIZE + 1f,
                           y * Constant.HALF_TILE_SIZE + 1f);
    }

}
