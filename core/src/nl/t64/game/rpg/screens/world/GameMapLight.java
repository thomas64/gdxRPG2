package nl.t64.game.rpg.screens.world;

import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

import java.util.Optional;


class GameMapLight {

    final String id;
    final Vector2 center;

    GameMapLight(RectangleMapObject rectObject) {
        this.id = Optional.ofNullable(rectObject.getName()).orElse("default");
        Rectangle rectangle = rectObject.getRectangle();
        this.center = rectangle.getCenter(new Vector2(rectangle.x, rectangle.y));
    }

}
