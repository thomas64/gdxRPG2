package nl.t64.game.rpg.subjects;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import nl.t64.game.rpg.screens.world.entity.Direction;


public interface ActionObserver {

    void onNotifyActionPressed(Rectangle checkRect, Direction playerDirection, Vector2 playerPosition);

}
