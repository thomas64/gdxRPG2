package nl.t64.game.rpg.screens.world.entity;


abstract class InputComponent implements Component {

    Direction direction;

    public abstract void update(Entity entity, float dt);

    public void reset() {
        // empty
    }

}
