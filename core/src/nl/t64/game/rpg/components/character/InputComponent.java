package nl.t64.game.rpg.components.character;


abstract class InputComponent extends ComponentSubject implements Component {

    Direction direction;

    public abstract void update(Character character, float dt);

    public void reset() {
        // empty
    }

}
