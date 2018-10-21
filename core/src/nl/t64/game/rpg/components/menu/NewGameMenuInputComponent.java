package nl.t64.game.rpg.components.menu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.math.Vector3;
import nl.t64.game.rpg.entities.Menu;
import nl.t64.game.rpg.events.Event;
import nl.t64.game.rpg.events.menu.*;


public class NewGameMenuInputComponent extends InputComponent implements InputProcessor {

    private Vector3 lastMouseCoordinates;
    private boolean mouseMoved = false;
    private boolean clickSelect = false;

    private Character pressChar = null;
    private boolean pressBackspace = false;

    private boolean pressLeft = false;
    private boolean pressRight = false;
    private boolean pressEnter = false;
    private boolean pressEscape = false;

    public NewGameMenuInputComponent() {
        this.lastMouseCoordinates = new Vector3();
    }

    @Override
    public void receive(Event event) {
        if (event instanceof RefreshInputEvent) {
            Gdx.input.setInputProcessor(this);
        }
    }

    @Override
    public void dispose() {
        Gdx.input.setInputProcessor(null);
    }

    @Override
    public boolean keyDown(int keycode) {
        if (keycode == Input.Keys.BACKSPACE) {
            pressBackspace = true;
        }
        if (keycode == Input.Keys.LEFT) {
            pressLeft = true;
        }
        if (keycode == Input.Keys.RIGHT) {
            pressRight = true;
        }
        if (keycode == Input.Keys.ENTER) {
            pressEnter = true;
        }
        if (keycode == Input.Keys.ESCAPE) {
            pressEscape = true;
        }
        return true;
    }

    @Override
    public boolean keyUp(int keycode) {
        if (keycode == Input.Keys.BACKSPACE) {
            pressBackspace = false;
        }
        if (keycode == Input.Keys.LEFT) {
            pressLeft = false;
        }
        if (keycode == Input.Keys.RIGHT) {
            pressRight = false;
        }
        if (keycode == Input.Keys.ENTER) {
            pressEnter = false;
        }
        if (keycode == Input.Keys.ESCAPE) {
            pressEscape = false;
        }
        return true;
    }

    @Override
    public boolean keyTyped(char character) {
        String validCharacters = "1234567890abcdefghijklmnopqrstuvwxyz";
        for (char c : validCharacters.toCharArray()) {
            if (character == c) {
                pressChar = character;
                break;
            }
        }
        return true;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        if (button == Input.Buttons.LEFT || button == Input.Buttons.RIGHT) {
            lastMouseCoordinates.set(screenX, screenY, 0);
        }
        if (button == Input.Buttons.LEFT) {
            clickSelect = true;
        }
        return true;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        if (button == Input.Buttons.LEFT) {
            clickSelect = false;
        }
        return true;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        mouseMoved = true;
        lastMouseCoordinates.set(screenX, screenY, 0);
        return true;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }

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
            newGameMenu.send(new PressUpEvent());
            pressLeft = false;
        }
        if (pressRight) {
            newGameMenu.send(new PressDownEvent());
            pressRight = false;
        }
        if (pressEnter) {
            newGameMenu.send(new PressEnterEvent());
            pressEnter = false;
        }
        if (pressEscape) {
            newGameMenu.send(new PressEscapeEvent());
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
