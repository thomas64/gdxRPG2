package nl.t64.game.rpg.events.character;

import lombok.Getter;
import nl.t64.game.rpg.events.Event;


@Getter
public class LoadSpriteEvent implements Event {
    private String path;
    private int col;
    private int row;
}
