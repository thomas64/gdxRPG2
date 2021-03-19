package nl.t64.game.rpg.screens.world.entity.events;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import lombok.AllArgsConstructor;


@AllArgsConstructor
public class OnBumpEvent implements Event {
    public final Rectangle biggerBoundingBox;
    public final Rectangle checkRect;
    public final Vector2 playerPosition;
}
