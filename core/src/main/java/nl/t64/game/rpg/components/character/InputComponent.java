package nl.t64.game.rpg.components.character;


abstract class InputComponent implements Component {

    public abstract void update(Character character, float dt);

    public void reset() {
        // empty
    }

}
