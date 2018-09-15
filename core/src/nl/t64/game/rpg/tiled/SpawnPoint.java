package nl.t64.game.rpg.tiled;

import com.badlogic.gdx.math.Rectangle;
import lombok.AllArgsConstructor;
import lombok.Getter;


@AllArgsConstructor
@Getter
public class SpawnPoint {

    private final Rectangle rectangle;
    private final String fromMapName;
    private final String fromMapLocation;
    private final String direction;

}
