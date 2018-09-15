package nl.t64.game.rpg.tiled;

import com.badlogic.gdx.math.Rectangle;
import lombok.AllArgsConstructor;
import lombok.Getter;


@AllArgsConstructor
@Getter
public class Portal {

    private final Rectangle rectangle;
    private final String fromMapName;
    private final String toMapName;
    private final String toMapLocation;

}
