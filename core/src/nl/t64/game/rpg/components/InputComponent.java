package nl.t64.game.rpg.components;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.math.Vector3;
import lombok.Getter;
import nl.t64.game.rpg.constants.Direction;
import nl.t64.game.rpg.constants.EntityState;
import nl.t64.game.rpg.constants.Mouse;
import nl.t64.game.rpg.entities.Entity;
import nl.t64.game.rpg.screens.WorldScreen;

import java.util.HashMap;
import java.util.Map;

public class InputComponent implements InputProcessor {

    private static final String TAG = InputComponent.class.getSimpleName();
    private static final float TURN_DELAY = 8f / 60f; // of a second

    private static Map<Mouse, Boolean> mouseButtons = new HashMap<>();

    static {
        mouseHide();
    }

    private static void mouseHide() {
        mouseButtons.put(Mouse.SELECT, false);
        mouseButtons.put(Mouse.DO_ACTION, false);
    }

    private Vector3 lastMouseCoordinates;

    private boolean pressUp = false;
    private boolean pressDown = false;
    private boolean pressLeft = false;
    private boolean pressRight = false;
    private boolean pressQuit = false;
    private boolean pressAlign = false;

    @Getter
    private int timeUp = 0;
    @Getter
    private int timeDown = 0;
    @Getter
    private int timeLeft = 0;
    @Getter
    private int timeRight = 0;

    @Getter
    private float timeDelay = 0f;

    public InputComponent() {
        this.lastMouseCoordinates = new Vector3();
        Gdx.input.setInputProcessor(this);
    }

    @Override
    public boolean keyDown(int keycode) {
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
        if (keycode == Input.Keys.Q) {
            pressQuit = true;
        }
        if (keycode == Input.Keys.SPACE) {
            pressAlign = true;
        }
        if (keycode == Input.Keys.F10) {
            WorldScreen.showGrid = !WorldScreen.showGrid;
        }
        if (keycode == Input.Keys.F11) {
            WorldScreen.showObjects = !WorldScreen.showObjects;
        }
        if (keycode == Input.Keys.F12) {
            WorldScreen.showDebug = !WorldScreen.showDebug;
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
        if (keycode == Input.Keys.LEFT) {
            pressLeft = false;
        }
        if (keycode == Input.Keys.RIGHT) {
            pressRight = false;
        }
        if (keycode == Input.Keys.Q) {
            pressQuit = false;
        }
        if (keycode == Input.Keys.SPACE) {
            pressAlign = false;
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

    private void setClickedMouseCoordinates(int x, int y) {
        lastMouseCoordinates.set(x, y, 0);
    }

    public void update(Entity player, float dt) {
        processPlayerMoveInput(player, dt);

        if (pressQuit) Gdx.app.exit();
        if (pressAlign) player.alignToGrid();

        if (mouseButtons.get(Mouse.SELECT)) {
            mouseButtons.put(Mouse.SELECT, false);
        }
    }

    private void processPlayerMoveInput(Entity player, float dt) {
        countKeyDownTime();
        ifNoMoveKeys_SetPlayerStill(player);
        setPossibleTurnDelay(player);
        setPlayerDirection(player);
        player.setFrame();
        ifMoveKeys_SetPlayerMoving(player, dt);
    }

    private void countKeyDownTime() {
        if (pressUp) {
            timeUp += 1;
        } else {
            timeUp = 0;
        }
        if (pressDown) {
            timeDown += 1;
        } else {
            timeDown = 0;
        }
        if (pressLeft) {
            timeLeft += 1;
        } else {
            timeLeft = 0;
        }
        if (pressRight) {
            timeRight += 1;
        } else {
            timeRight = 0;
        }
    }

    private void ifNoMoveKeys_SetPlayerStill(Entity player) {
        if (!areMoveKeysPressed()) {
            timeDelay = 0f;
            player.setState(EntityState.IDLE);
            player.setStandingStillFrame();
        }
    }

    private void setPossibleTurnDelay(Entity player) {
        if (player.getState() == EntityState.IDLE) {
            if ((pressUp && player.getDirection() != Direction.NORTH) ||
                    (pressDown && player.getDirection() != Direction.SOUTH) ||
                    (pressLeft && player.getDirection() != Direction.WEST) ||
                    (pressRight && player.getDirection() != Direction.EAST)) {
                timeDelay = TURN_DELAY;
            }
        }
    }

    private void setPlayerDirection(Entity player) {
        if (pressUp &&
                (timeUp <= timeDown || timeUp <= timeLeft || timeUp <= timeRight)) {
            player.setDirection(Direction.NORTH);
        } else if (pressDown &&
                (timeDown <= timeUp || timeDown <= timeLeft || timeDown <= timeRight)) {
            player.setDirection(Direction.SOUTH);
        } else if (pressLeft &&
                (timeLeft <= timeUp || timeLeft <= timeDown || timeLeft <= timeRight)) {
            player.setDirection(Direction.WEST);
        } else if (pressRight &&
                (timeRight <= timeUp || timeRight <= timeDown || timeRight <= timeLeft)) {
            player.setDirection(Direction.EAST);

        } else if (pressUp) {
            player.setDirection(Direction.NORTH);
        } else if (pressDown) {
            player.setDirection(Direction.SOUTH);
        } else if (pressLeft) {
            player.setDirection(Direction.WEST);
        } else if (pressRight) {
            player.setDirection(Direction.EAST);
        }
    }

    private void ifMoveKeys_SetPlayerMoving(Entity player, float dt) {
        if (areMoveKeysPressed()) {
            if (timeDelay > 0f) {
                timeDelay -= dt;
            } else {
                player.setState(EntityState.WALKING);
                player.move(dt);
            }
        }
    }

    private boolean areMoveKeysPressed() {
        return pressUp || pressDown || pressLeft || pressRight;
    }

    private void selectMouseButtonPressed(int x, int y) {
        mouseButtons.put(Mouse.SELECT, true);
    }

    private void doActionMouseButtonPressed(int x, int y) {
        mouseButtons.put(Mouse.DO_ACTION, true);
    }

    private void selectMouseButtonReleased(int x, int y) {
        mouseButtons.put(Mouse.SELECT, false);
    }

    private void doActionMouseButtonReleased(int x, int y) {
        mouseButtons.put(Mouse.DO_ACTION, false);
    }

}
