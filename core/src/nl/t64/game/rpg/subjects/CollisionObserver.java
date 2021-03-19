package nl.t64.game.rpg.subjects;

import com.badlogic.gdx.math.Rectangle;
import nl.t64.game.rpg.screens.world.entity.Direction;


public interface CollisionObserver {

    void onNotifyCollision(Rectangle playerBoundingBox, Direction playerDirection);

}
