package nl.t64.game.rpg.components.menu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.math.Vector3;
import nl.t64.game.rpg.entities.Menu;
import nl.t64.game.rpg.events.Event;
import nl.t64.game.rpg.events.menu.*;


public class MainMenuInputComponent extends InputComponent implements InputProcessor {

    private Vector3 lastMouseCoordinates;
    private boolean mouseMoved = false;
    private boolean clickSelect = false;

    private boolean pressUp = false;
    private boolean pressDown = false;
    private boolean pressEnter = false;

    public MainMenuInputComponent() {
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
        if (keycode == Input.Keys.UP) {
            pressUp = true;
        }
        if (keycode == Input.Keys.DOWN) {
            pressDown = true;
        }
        if (keycode == Input.Keys.ENTER) {
            pressEnter = true;
        }
        return true;
    }

    @Override
    public boolean keyUp(int keycode) {
        if (keycode == Input.Keys.UP) {
            pressUp = false;
        }
        if (keycode == Input.Keys.DOWN) {
            pressDown = false;
        }
        if (keycode == Input.Keys.ENTER) {
            pressEnter = false;
        }
        return true;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
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
    public void update(Menu mainMenu) {
        if (pressUp) {
            mainMenu.send(new PressUpEvent());
            pressUp = false;
        }
        if (pressDown) {
            mainMenu.send(new PressDownEvent());
            pressDown = false;
        }
        if (pressEnter) {
            mainMenu.send(new PressEnterEvent());
            pressEnter = false;
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
