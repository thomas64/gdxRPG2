package nl.t64.game.rpg.screens.world;

import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

import java.util.Optional;


class GameMapLight {

    final String id;
    final Vector2 center;

    GameMapLight(MapObject mapObject) {
        this.id = Optional.ofNullable(mapObject.getName()).orElse("default");
        Rectangle rectangle = ((RectangleMapObject) mapObject).getRectangle();
        this.center = rectangle.getCenter(new Vector2(rectangle.x, rectangle.y));
    }

}
