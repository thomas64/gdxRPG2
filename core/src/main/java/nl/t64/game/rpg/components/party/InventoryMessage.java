package nl.t64.game.rpg.components.party;

import lombok.AllArgsConstructor;
import lombok.Getter;


@AllArgsConstructor
@Getter
public class InventoryMessage {

    private boolean isSuccessful;
    private String message;

}
