package nl.t64.game.rpg;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.math.Vector3;
import lombok.Getter;
import nl.t64.game.rpg.constants.Direction;
import nl.t64.game.rpg.constants.EntityState;
import nl.t64.game.rpg.constants.Mouse;
import nl.t64.game.rpg.entities.Entity;
import nl.t64.game.rpg.screens.AdventureScreen;

import java.util.HashMap;
import java.util.Map;

public class PlayerController implements InputProcessor {

    private static final String TAG = PlayerController.class.getSimpleName();
    private static final float TURN_DELAY = 7f / 60f; // of a second

    private static Map<Mouse, Boolean> mouseButtons = new HashMap<>();

    static {
        mouseHide();
    }

    private static void mouseHide() {
        mouseButtons.put(Mouse.SELECT, false);
        mouseButtons.put(Mouse.DO_ACTION, false);
    }

    private Vector3 lastMouseCoordinates;
    private Entity player;

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

    public PlayerController(Entity player) {
        this.lastMouseCoordinates = new Vector3();
        this.player = player;
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
            AdventureScreen.showGrid = !AdventureScreen.showGrid;
        }
        if (keycode == Input.Keys.F11) {
            AdventureScreen.showObjects = !AdventureScreen.showObjects;
        }
        if (keycode == Input.Keys.F12) {
            AdventureScreen.showDebug = !AdventureScreen.showDebug;
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

    public void update(float delta) {
        processInput(delta);
    }

    private void setClickedMouseCoordinates(int x, int y) {
        lastMouseCoordinates.set(x, y, 0);
    }

    private void processInput(float delta) {
        processPlayerMoveInput(delta);

        if (pressQuit) Gdx.app.exit();
        if (pressAlign) player.alignToGrid();

        if (mouseButtons.get(Mouse.SELECT)) {
            mouseButtons.put(Mouse.SELECT, false);
        }
    }

    private void processPlayerMoveInput(float delta) {
        countKeyDownTime();
        ifNoMoveKeys_SetPlayerStill();
        setPossibleTurnDelay();
        setPlayerDirection();
        ifMoveKeys_SetPlayerMoving(delta);
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

    private void ifNoMoveKeys_SetPlayerStill() {
        if (!areMoveKeysPressed()) {
            timeDelay = 0f;
            player.setState(EntityState.IDLE);
            player.setStandingStillFrame();
        }
    }

    private void setPossibleTurnDelay() {
        if (player.getState() == EntityState.IDLE) {
            if ((timeUp > 0 && player.getCurrentDirection() != Direction.NORTH) ||
                    (timeDown > 0 && player.getCurrentDirection() != Direction.SOUTH) ||
                    (timeLeft > 0 && player.getCurrentDirection() != Direction.WEST) ||
                    (timeRight > 0 && player.getCurrentDirection() != Direction.EAST)) {
                timeDelay = TURN_DELAY;
            }
        }
    }

    private void setPlayerDirection() {
        if (timeUp > 0 &&
                (timeUp <= timeDown || timeUp <= timeLeft || timeUp <= timeRight)) {
            player.setDirection(Direction.NORTH);
        } else if (timeDown > 0 &&
                (timeDown <= timeUp || timeDown <= timeLeft || timeDown <= timeRight)) {
            player.setDirection(Direction.SOUTH);
        } else if (timeLeft > 0 &&
                (timeLeft <= timeUp || timeLeft <= timeDown || timeLeft <= timeRight)) {
            player.setDirection(Direction.WEST);
        } else if (timeRight > 0 &&
                (timeRight <= timeUp || timeRight <= timeDown || timeRight <= timeLeft)) {
            player.setDirection(Direction.EAST);
        } else if (timeUp > 0) {
            player.setDirection(Direction.NORTH);
        } else if (timeDown > 0) {
            player.setDirection(Direction.SOUTH);
        } else if (timeLeft > 0) {
            player.setDirection(Direction.WEST);
        } else if (timeRight > 0) {
            player.setDirection(Direction.EAST);
        }
    }

    private void ifMoveKeys_SetPlayerMoving(float delta) {
        if (areMoveKeysPressed()) {
            if (timeDelay > 0f) {
                timeDelay -= delta;
            } else {
                player.calculateNextPosition(player.getCurrentDirection(), delta);
                player.setState(EntityState.WALKING);
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
