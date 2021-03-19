package nl.t64.game.rpg.subjects;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

import java.util.Optional;


public interface BlockObserver {

    Optional<Rectangle> getBlockerFor(Rectangle boundingBox);

    boolean isBlocking(Vector2 point);

}
