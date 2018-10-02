package nl.t64.game.rpg.components;

import nl.t64.game.rpg.entities.Entity;


public abstract class InputComponent implements Component {

    public abstract void update(Entity entity, float dt);

}
