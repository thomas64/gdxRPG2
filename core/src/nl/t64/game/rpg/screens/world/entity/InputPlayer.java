package nl.t64.game.rpg.screens.world.entity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import nl.t64.game.rpg.Utils;
import nl.t64.game.rpg.constants.Constant;
import nl.t64.game.rpg.screens.world.entity.events.*;


public class InputPlayer extends InputComponent implements InputProcessor {

    private static final float TURN_DELAY_TIME = 8f / 60f; // of a second
    private static final float TURN_DELAY_GRACE_PERIOD = 0.3f;
    private static final float TURN_GRACE_MAX_VALUE = 5f;

    private Entity player;

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
    private float turnGrace;

    private boolean pressCtrl;
    private boolean pressShift;
    private boolean pressAction;

    public InputPlayer(InputMultiplexer multiplexer) {
        this.reset();
        multiplexer.addProcessor(this);
    }

    @Override
    public void receive(Event event) {
        if (event instanceof LoadEntityEvent loadEvent) {
            direction = loadEvent.direction;
        }
    }

    @Override
    public void dispose() {
        Gdx.input.setInputProcessor(null);
    }

    @Override
    public boolean keyDown(int keycode) {
        if (keycode == Input.Keys.CONTROL_LEFT || keycode == Input.Keys.CONTROL_RIGHT
            || keycode == Constant.KEYCODE_L1) {
            pressCtrl = true;
        }
        if (keycode == Input.Keys.SHIFT_LEFT || keycode == Input.Keys.SHIFT_RIGHT
            || keycode == Constant.KEYCODE_R1) {
            pressShift = true;
        }

        if (keycode == Input.Keys.A || keycode == Constant.KEYCODE_BOTTOM) {
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
        if (keycode == Input.Keys.CONTROL_LEFT || keycode == Input.Keys.CONTROL_RIGHT
            || keycode == Constant.KEYCODE_L1) {
            pressCtrl = false;
        }
        if (keycode == Input.Keys.SHIFT_LEFT || keycode == Input.Keys.SHIFT_RIGHT
            || keycode == Constant.KEYCODE_R1) {
            pressShift = false;
        }

        if (keycode == Input.Keys.A || keycode == Constant.KEYCODE_BOTTOM) {
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
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
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
    public boolean scrolled(float amountX, float amountY) {
        return false;
    }

    @Override
    public void update(Entity player, float dt) {
        this.player = player;
        processMoveInput(dt);
        processOtherInput();
    }

    @Override
    public void reset() {
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
        turnGrace = 0f;

        pressCtrl = false;
        pressShift = false;
        pressAction = false;
    }

    private void processMoveInput(float dt) {
        processPlayerSpeedInput();
        countKeyDownTime();
        ifNoMoveKeys_SetGracePeriod(dt);
        ifNoMoveKeys_SetPlayerIdle();
        setPossibleTurnDelay();
        setPlayerDirection();
        ifMoveKeys_SetPlayerWalking(dt);
    }

    private void processOtherInput() {
        if (pressAlign && Utils.getPreferenceManager().isInDebugMode()) {
            player.send(new StateEvent(EntityState.ALIGNING));
        }
        if (pressAction) {
            player.send(new ActionEvent());
            pressAction = false;
        }
    }

    private void processPlayerSpeedInput() {
        float moveSpeed = Constant.MOVE_SPEED_2;
        if (pressCtrl && pressShift && Utils.getPreferenceManager().isInDebugMode()) {
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

    private void ifNoMoveKeys_SetGracePeriod(float dt) {
        if (!areMoveKeysPressed()
            && turnGrace < TURN_GRACE_MAX_VALUE) {
            turnGrace += dt;
        }
    }

    private void ifNoMoveKeys_SetPlayerIdle() {
        if (!areMoveKeysPressed()) {
            turnDelay = 0f;
            player.send(new StateEvent(EntityState.IDLE));
        }
    }

    private void setPossibleTurnDelay() {
        if (turnDelay <= 0f
            && turnGrace > TURN_DELAY_GRACE_PERIOD
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

    private void ifMoveKeys_SetPlayerWalking(float dt) {
        if (areMoveKeysPressed()) {
            player.send(new DirectionEvent(direction));

            if (turnDelay > 0f) {
                turnDelay -= dt;
            } else {
                turnGrace = 0f;
                player.send(new StateEvent(EntityState.WALKING));
            }
        }
    }

    private boolean areMoveKeysPressed() {
        return pressUp || pressDown || pressLeft || pressRight;
    }

    private boolean pressUpButDirectionisNotYetNorth() {
        return pressUp && !direction.equals(Direction.NORTH);
    }

    private boolean pressDownButDirectionIsNotYetSouth() {
        return pressDown && !direction.equals(Direction.SOUTH);
    }

    private boolean pressLeftButDirectionIsNotYetWest() {
        return pressLeft && !direction.equals(Direction.WEST);
    }

    private boolean pressRightButDirectionIsNotYetEast() {
        return pressRight && !direction.equals(Direction.EAST);
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
