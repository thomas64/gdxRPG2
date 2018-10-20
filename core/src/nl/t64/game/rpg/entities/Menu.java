package nl.t64.game.rpg.entities;

import nl.t64.game.rpg.GdxRpg2;
import nl.t64.game.rpg.components.Component;
import nl.t64.game.rpg.components.menu.GraphicsComponent;
import nl.t64.game.rpg.components.menu.InputComponent;
import nl.t64.game.rpg.components.menu.PhysicsComponent;
import nl.t64.game.rpg.events.Event;

import java.util.ArrayList;
import java.util.List;


public class Menu {

    private static final String TAG = Menu.class.getSimpleName();

    private static final int MAX_COMPONENTS = 3;
    private List<Component> components;

    private InputComponent inputComponent;
    private PhysicsComponent physicsComponent;
    private GraphicsComponent graphicsComponent;

    public Menu(InputComponent ic, PhysicsComponent pc, GraphicsComponent gc) {
        inputComponent = ic;
        physicsComponent = pc;
        graphicsComponent = gc;

        components = new ArrayList<>(MAX_COMPONENTS);
        components.add(inputComponent);
        components.add(physicsComponent);
        components.add(graphicsComponent);
    }

    public void send(Event event) {
        components.forEach(component -> component.receive(event));
    }

    public void update(GdxRpg2 game) {
        inputComponent.update(this);
        physicsComponent.update(this, game);
        graphicsComponent.update();
    }

    public void dispose() {
        components.forEach(Component::dispose);
    }

}
