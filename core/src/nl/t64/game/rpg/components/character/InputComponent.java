package nl.t64.game.rpg.components.character;

import nl.t64.game.rpg.components.Component;
import nl.t64.game.rpg.entities.Character;


public abstract class InputComponent implements Component {

    public abstract void update(Character character, float dt);

}
