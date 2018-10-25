package nl.t64.game.rpg.components.menu;

import nl.t64.game.rpg.constants.KeyDown;
import nl.t64.game.rpg.entities.Menu;
import nl.t64.game.rpg.events.menu.*;


public class NewGameMenuInputComponent extends InputComponent {

    @Override
    public void update(Menu newGameMenu) {
        if (pressChar != null) {
            newGameMenu.send(new PressCharEvent(pressChar));
            pressChar = null;
        }
        if (pressBackspace) {
            newGameMenu.send(new PressBackspaceEvent());
            pressBackspace = false;
        }
        if (pressLeft) {
            newGameMenu.send(new KeyDownEvent(KeyDown.LEFT));
            pressLeft = false;
        }
        if (pressRight) {
            newGameMenu.send(new KeyDownEvent(KeyDown.RIGHT));
            pressRight = false;
        }
        if (pressEnter) {
            newGameMenu.send(new KeyDownEvent(KeyDown.ENTER));
            pressEnter = false;
        }
        if (pressEscape) {
            newGameMenu.send(new KeyDownEvent(KeyDown.ESCAPE));
            pressEscape = false;
        }
        if (mouseMoved) {
            newGameMenu.send(new MouseMoveEvent(lastMouseCoordinates));
            mouseMoved = false;
        }
        if (clickSelect) {
            newGameMenu.send(new MouseClickEvent());
            clickSelect = false;
        }
    }

}
