package nl.t64.game.rpg.components.character;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.math.Vector3;
import nl.t64.game.rpg.constants.CharacterState;
import nl.t64.game.rpg.constants.Constant;
import nl.t64.game.rpg.constants.Direction;
import nl.t64.game.rpg.events.Event;
import nl.t64.game.rpg.events.character.*;


public class PlayerInput extends InputComponent implements InputProcessor {

    private static final String TAG = PlayerInput.class.getSimpleName();
    private static final float TURN_DELAY_TIME = 8f / 60f; // of a second

    private Vector3 lastMouseCoordinates;
    private boolean clickSelect;
    private boolean clickDoAction;

    private boolean pressUp;
    private boolean pressDown;
    private boolean pressLeft;
    private boolean pressRight;

    private boolean pressAlign;

    private int timeUp;
    private int timeDown;
    private int timeLeft;
    private int timeRight;
    private float turnDelay;

    private boolean pressCtrl;
    private boolean pressShift;
    private boolean pressAction;

    private Direction direction;

    public PlayerInput(InputMultiplexer multiplexer) {
        this.lastMouseCoordinates = new Vector3();
        this.reset();
        multiplexer.addProcessor(this);
    }

    @Override
    public void receive(Event event) {
        if (event instanceof StartDirectionEvent) {
            direction = ((StartDirectionEvent) event).getDirection();
        }
    }

    @Override
    public void dispose() {
        Gdx.input.setInputProcessor(null);
    }

    @Override
    public boolean keyDown(int keycode) {
        if (keycode == Input.Keys.CONTROL_LEFT || keycode == Input.Keys.CONTROL_RIGHT) {
            pressCtrl = true;
        }
        if (keycode == Input.Keys.SHIFT_LEFT || keycode == Input.Keys.SHIFT_RIGHT) {
            pressShift = true;
        }

        if (keycode == Input.Keys.A) {
            pressAction = true;
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

        if (keycode == Input.Keys.SPACE) {
            pressAlign = true;
        }
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        if (keycode == Input.Keys.CONTROL_LEFT || keycode == Input.Keys.CONTROL_RIGHT) {
            pressCtrl = false;
        }
        if (keycode == Input.Keys.SHIFT_LEFT || keycode == Input.Keys.SHIFT_RIGHT) {
            pressShift = false;
        }

        if (keycode == Input.Keys.A) {
            pressAction = false;
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

        if (keycode == Input.Keys.SPACE) {
            pressAlign = false;
        }
        return false;
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
        if (button == Input.Buttons.RIGHT) {
            clickDoAction = true;
        }
        return true;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        if (button == Input.Buttons.LEFT) {
            clickSelect = false;
        }
        if (button == Input.Buttons.RIGHT) {
            clickDoAction = false;
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

    @Override
    public void update(Character player, float dt) {
        processPlayerMoveInput(player, dt);

        if (pressAlign) {
            player.send(new StateEvent(CharacterState.ALIGNING));
        }
        if (clickSelect) {
            player.send(new StartSelectEvent(lastMouseCoordinates));
            clickSelect = false;
        }
        if (pressAction) {
            player.send(new ActionEvent());
            pressAction = false;
        }

    }

    @Override
    public void reset() {
        clickSelect = false;
        clickDoAction = false;

        pressUp = false;
        pressDown = false;
        pressLeft = false;
        pressRight = false;

        pressAlign = false;

        timeUp = 0;
        timeDown = 0;
        timeLeft = 0;
        timeRight = 0;
        turnDelay = 0f;

        pressCtrl = false;
        pressShift = false;
        pressAction = false;
    }

    private void processPlayerMoveInput(Character player, float dt) {
        processPlayerSpeedInput(player);
        countKeyDownTime();
        ifNoMoveKeys_SetPlayerIdle(player);
        setPossibleTurnDelay();
        setPlayerDirection();
        ifMoveKeys_SetPlayerWalking(player, dt);
    }

    private void processPlayerSpeedInput(Character player) {
        float moveSpeed = Constant.MOVE_SPEED_2;
        if (pressCtrl && pressShift) {
            moveSpeed = Constant.MOVE_SPEED_4;
        } else if (pressShift) {
            moveSpeed = Constant.MOVE_SPEED_3;
        } else if (pressCtrl) {
            moveSpeed = Constant.MOVE_SPEED_1;
        }
        player.send(new SpeedEvent(moveSpeed));
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

    private void ifNoMoveKeys_SetPlayerIdle(Character player) {
        if (!areMoveKeysPressed()) {
            turnDelay = 0f;
            player.send(new StateEvent(CharacterState.IDLE));
        }
    }

    private void setPossibleTurnDelay() {
        if (turnDelay <= 0f
                && (pressUpButDirectionisNotYetNorth() ||
                pressDownButDirectionIsNotYetSouth() ||
                pressLeftButDirectionIsNotYetWest() ||
                pressRightButDirectionIsNotYetEast())
        ) {
            turnDelay = TURN_DELAY_TIME;
        }
    }

    private void setPlayerDirection() {
        if (pressUp && timeUpIsLess()) {
            direction = Direction.NORTH;
        } else if (pressDown && timeDownIsLess()) {
            direction = Direction.SOUTH;
        } else if (pressLeft && timeLeftIsLess()) {
            direction = Direction.WEST;
        } else if (pressRight && timeRightIsLess()) {
            direction = Direction.EAST;

        } else if (pressUp) {
            direction = Direction.NORTH;
        } else if (pressDown) {
            direction = Direction.SOUTH;
        } else if (pressLeft) {
            direction = Direction.WEST;
        } else if (pressRight) {
            direction = Direction.EAST;
        }
    }

    private void ifMoveKeys_SetPlayerWalking(Character player, float dt) {
        if (areMoveKeysPressed()) {
            player.send(new DirectionEvent(direction));

            if (turnDelay > 0f) {
                turnDelay -= dt;
            } else {
                player.send(new StateEvent(CharacterState.WALKING));
            }
        }
    }

    private boolean areMoveKeysPressed() {
        return pressUp || pressDown || pressLeft || pressRight;
    }

    private boolean pressUpButDirectionisNotYetNorth() {
        return pressUp && direction != Direction.NORTH;
    }

    private boolean pressDownButDirectionIsNotYetSouth() {
        return pressDown && direction != Direction.SOUTH;
    }

    private boolean pressLeftButDirectionIsNotYetWest() {
        return pressLeft && direction != Direction.WEST;
    }

    private boolean pressRightButDirectionIsNotYetEast() {
        return pressRight && direction != Direction.EAST;
    }

    private boolean timeRightIsLess() {
        return timeRight <= timeUp || timeRight <= timeDown || timeRight <= timeLeft;
    }

    private boolean timeLeftIsLess() {
        return timeLeft <= timeUp || timeLeft <= timeDown || timeLeft <= timeRight;
    }

    private boolean timeDownIsLess() {
        return timeDown <= timeUp || timeDown <= timeLeft || timeDown <= timeRight;
    }

    private boolean timeUpIsLess() {
        return timeUp <= timeDown || timeUp <= timeLeft || timeUp <= timeRight;
    }

}