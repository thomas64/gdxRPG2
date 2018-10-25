package nl.t64.game.rpg.components.menu;

import nl.t64.game.rpg.constants.KeyDown;
import nl.t64.game.rpg.entities.Menu;
import nl.t64.game.rpg.events.menu.KeyDownEvent;
import nl.t64.game.rpg.events.menu.MouseClickEvent;
import nl.t64.game.rpg.events.menu.MouseMoveEvent;


public class MainMenuInputComponent extends InputComponent {

    @Override
    public void update(Menu mainMenu) {
        if (pressUp) {
            mainMenu.send(new KeyDownEvent(KeyDown.UP));
            pressUp = false;
        }
        if (pressDown) {
            mainMenu.send(new KeyDownEvent(KeyDown.DOWN));
            pressDown = false;
        }
        if (pressEnter) {
            mainMenu.send(new KeyDownEvent(KeyDown.ENTER));
            pressEnter = false;
        }
        if (pressEscape) {
            mainMenu.send(new KeyDownEvent(KeyDown.ESCAPE));
            pressEscape = false;
        }
        if (mouseMoved) {
            mainMenu.send(new MouseMoveEvent(lastMouseCoordinates));
            mouseMoved = false;
        }
        if (clickSelect) {
            mainMenu.send(new MouseClickEvent());
            clickSelect = false;
        }
    }

}
