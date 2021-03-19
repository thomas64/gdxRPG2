package nl.t64.game.rpg.subjects;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;


public interface BumpObserver {

    void onNotifyBump(Rectangle biggerBoundingBox, Rectangle checkRect, Vector2 playerPosition);

}
