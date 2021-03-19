package nl.t64.game.rpg.screens.world;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import nl.t64.game.rpg.Utils;
import nl.t64.game.rpg.subjects.BlockObserver;

import java.util.Optional;


class GameMapBlocker extends GameMapObject implements BlockObserver {

    GameMapBlocker(Rectangle rectangle) {
        this.rectangle = rectangle;
        Utils.getBrokerManager().blockObservers.addObserver(this);
    }

    @Override
    public Optional<Rectangle> getBlockerFor(Rectangle boundingBox) {
        if (boundingBox.overlaps(rectangle)) {
            return Optional.of(rectangle);
        } else {
            return Optional.empty();
        }
    }

    @Override
    public boolean isBlocking(Vector2 point) {
        return rectangle.contains(point);
    }

}
