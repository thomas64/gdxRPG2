package nl.t64.game.rpg.components.menu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.math.Vector3;
import nl.t64.game.rpg.components.Component;
import nl.t64.game.rpg.entities.Menu;
import nl.t64.game.rpg.events.Event;
import nl.t64.game.rpg.events.menu.RefreshInputEvent;


public abstract class InputComponent implements Component, InputProcessor {

    Vector3 lastMouseCoordinates;
    boolean mouseMoved = false;
    boolean clickSelect = false;

    Character pressChar = null;
    boolean pressBackspace = false;

    boolean pressUp = false;
    boolean pressDown = false;
    boolean pressLeft = false;
    boolean pressRight = false;

    boolean pressEnter = false;
    boolean pressEscape = false;

    InputComponent() {
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

    public abstract void update(Menu menu);

    @Override
    public boolean keyDown(int keycode) {
        if (keycode == Input.Keys.BACKSPACE) {
            pressBackspace = true;
        }
        if (keycode == Input.Keys.UP) {
            pressUp = true;
        }
        if (keycode == Input.Keys.DOWN) {
            pressDown = true;
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
        if (keycode == Input.Keys.UP) {
            pressUp = false;
        }
        if (keycode == Input.Keys.DOWN) {
            pressDown = false;
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

}
