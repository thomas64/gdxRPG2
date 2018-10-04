package nl.t64.game.rpg.events;

import lombok.Getter;


@Getter
public class LoadSpriteEvent extends Event {
    private String path;
    private int col;
    private int row;
}
