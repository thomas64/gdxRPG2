package nl.t64.game.rpg.components.character;

import nl.t64.game.rpg.constants.Direction;


abstract class InputComponent implements Component {

    Direction direction;

    public abstract void update(Character character, float dt);

    public void reset() {
        // empty
    }

}
