package nl.t64.game.rpg.screens.world.mapobjects;

import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import nl.t64.game.rpg.Utils;
import nl.t64.game.rpg.subjects.BlockObserver;

import java.util.Optional;


public class GameMapBlocker extends GameMapObject implements BlockObserver {

    public GameMapBlocker(RectangleMapObject rectObject) {
        super.rectangle = rectObject.getRectangle();

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
