package nl.t64.game.rpg.screens.world.mapobjects;

import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import lombok.Getter;

import java.util.Optional;


@Getter
public class GameMapLight {

    private final String id;
    private final Vector2 center;

    public GameMapLight(RectangleMapObject rectObject) {
        this.id = Optional.ofNullable(rectObject.getName()).orElse("default");
        Rectangle rectangle = rectObject.getRectangle();
        this.center = rectangle.getCenter(new Vector2(rectangle.x, rectangle.y));
    }

}
