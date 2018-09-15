package nl.t64.game.rpg;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.math.Vector3;
import nl.t64.game.rpg.constants.Direction;
import nl.t64.game.rpg.constants.EntityState;
import nl.t64.game.rpg.constants.Key;
import nl.t64.game.rpg.constants.Mouse;
import nl.t64.game.rpg.entities.Entity;

import java.util.HashMap;
import java.util.Map;

public class PlayerController implements InputProcessor {

    private static final String TAG = PlayerController.class.getSimpleName();
    private static Map<Key, Boolean> keys = new HashMap<>();
    private static Map<Mouse, Boolean> mouseButtons = new HashMap<>();

    static {
        keyHide();
        mouseHide();
    }

    private static void keyHide() {
        keys.put(Key.UP, false);
        keys.put(Key.DOWN, false);
        keys.put(Key.LEFT, false);
        keys.put(Key.RIGHT, false);
        keys.put(Key.QUIT, false);
    }

    private static void mouseHide() {
        mouseButtons.put(Mouse.SELECT, false);
        mouseButtons.put(Mouse.DO_ACTION, false);
    }

    private Vector3 lastMouseCoordinates;
    private Entity player;

    public PlayerController(Entity player) {
        this.lastMouseCoordinates = new Vector3();
        this.player = player;
    }

    @Override
    public boolean keyDown(int keycode) {
        if (keycode == Input.Keys.UP) {
            upPressed();
        }
        if (keycode == Input.Keys.DOWN) {
            downPressed();
        }
        if (keycode == Input.Keys.LEFT) {
            leftPressed();
        }
        if (keycode == Input.Keys.RIGHT) {
            rightPressed();
        }
        if (keycode == Input.Keys.Q) {
            quitPressed();
        }
        return true;
    }

    @Override
    public boolean keyUp(int keycode) {
        if (keycode == Input.Keys.UP) {
            upReleased();
        }
        if (keycode == Input.Keys.DOWN) {
            downReleased();
        }
        if (keycode == Input.Keys.LEFT) {
            leftReleased();
        }
        if (keycode == Input.Keys.RIGHT) {
            rightReleased();
        }
        if (keycode == Input.Keys.Q) {
            quitReleased();
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
            setClickedMouseCoordinates(screenX, screenY);
        }
        if (button == Input.Buttons.LEFT) {
            selectMouseButtonPressed(screenX, screenY);
        }
        if (button == Input.Buttons.RIGHT) {
            doActionMouseButtonPressed(screenX, screenY);
        }
        return true;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        if (button == Input.Buttons.LEFT) {
            selectMouseButtonReleased(screenX, screenY);
        }
        if (button == Input.Buttons.RIGHT) {
            doActionMouseButtonReleased(screenX, screenY);
        }
        return true;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }

    public void dispose() {
    }

    public void update(float delta) {
        processInput(delta);
    }

    private void setClickedMouseCoordinates(int x, int y) {
        lastMouseCoordinates.set(x, y, 0);
    }

    private void processInput(float delta) {
        if (keys.get(Key.UP)) {
            playerMove(Direction.NORTH, delta);
        } else if (keys.get(Key.DOWN)) {
            playerMove(Direction.SOUTH, delta);
        } else if (keys.get(Key.LEFT)) {
            playerMove(Direction.WEST, delta);
        } else if (keys.get(Key.RIGHT)) {
            playerMove(Direction.EAST, delta);
        } else if (keys.get(Key.QUIT)) {
            Gdx.app.exit();
        } else {
            player.setState(EntityState.IDLE);
        }

        if (mouseButtons.get(Mouse.SELECT)) {
            mouseButtons.put(Mouse.SELECT, false);
        }
    }

    private void playerMove(Direction direction, float delta) {
        player.calculateNextPosition(direction, delta);
        player.setState(EntityState.WALKING);
        player.setDirection(direction, delta);
    }

    private void upPressed() {
        keys.put(Key.UP, true);
    }

    private void downPressed() {
        keys.put(Key.DOWN, true);
    }

//region Key presses

    private void leftPressed() {
        keys.put(Key.LEFT, true);
    }

    private void rightPressed() {
        keys.put(Key.RIGHT, true);
    }

    private void quitPressed() {
        keys.put(Key.QUIT, true);
    }

    private void selectMouseButtonPressed(int x, int y) {
        mouseButtons.put(Mouse.SELECT, true);
    }

    private void doActionMouseButtonPressed(int x, int y) {
        mouseButtons.put(Mouse.DO_ACTION, true);
    }

    private void upReleased() {
        keys.put(Key.UP, false);
    }

    private void downReleased() {
        keys.put(Key.DOWN, false);
    }

//endregion

//region Key releases

    private void leftReleased() {
        keys.put(Key.LEFT, false);
    }

    private void rightReleased() {
        keys.put(Key.RIGHT, false);
    }

    private void quitReleased() {
        keys.put(Key.QUIT, false);
    }

    private void selectMouseButtonReleased(int x, int y) {
        mouseButtons.put(Mouse.SELECT, false);
    }

    private void doActionMouseButtonReleased(int x, int y) {
        mouseButtons.put(Mouse.DO_ACTION, false);
    }

//endregion

}
