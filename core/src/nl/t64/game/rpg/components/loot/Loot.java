package nl.t64.game.rpg.components.loot;

import lombok.Getter;
import lombok.Setter;

import java.util.List;


@Getter
@Setter
public class Loot {

    private List<Content> content;

    public boolean isTaken() {
        return content.isEmpty();
    }

}
