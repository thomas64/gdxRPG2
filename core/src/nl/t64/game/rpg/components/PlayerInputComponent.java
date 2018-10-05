package nl.t64.game.rpg.components;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.math.Vector3;
import nl.t64.game.rpg.constants.Direction;
import nl.t64.game.rpg.constants.EntityState;
import nl.t64.game.rpg.entities.Entity;
import nl.t64.game.rpg.events.DirectionEvent;
import nl.t64.game.rpg.events.Event;
import nl.t64.game.rpg.events.StartDirectionEvent;
import nl.t64.game.rpg.events.StateEvent;
import nl.t64.game.rpg.screens.WorldScreen;


public class PlayerInputComponent extends InputComponent implements InputProcessor {

    private static final String TAG = PlayerInputComponent.class.getSimpleName();
    private static final float TURN_DELAY_TIME = 8f / 60f; // of a second

    private Vector3 lastMouseCoordinates;
    private boolean clickSelect = false;
    private boolean clickDoAction = false;

    private boolean pressUp = false;
    private boolean pressDown = false;
    private boolean pressLeft = false;
    private boolean pressRight = false;
    private boolean pressQuit = false;
    private boolean pressAlign = false;

    private int timeUp = 0;
    private int timeDown = 0;
    private int timeLeft = 0;
    private int timeRight = 0;
    private float turnDelay = 0f;

    private Direction direction;

    public PlayerInputComponent() {
        this.lastMouseCoordinates = new Vector3();
        Gdx.input.setInputProcessor(this);
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
    public void update(Entity player, float dt) {
        processPlayerMoveInput(player, dt);

        if (pressQuit) Gdx.app.exit();
        if (pressAlign) player.send(new StateEvent(EntityState.ALIGNING));
        if (clickSelect) clickSelect = false;
    }

    private void processPlayerMoveInput(Entity player, float dt) {
        countKeyDownTime();
        ifNoMoveKeys_SetPlayerIdle(player);
        setPossibleTurnDelay();
        setPlayerDirection();
        ifMoveKeys_SetPlayerWalking(player, dt);
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

    private void ifNoMoveKeys_SetPlayerIdle(Entity player) {
        if (!areMoveKeysPressed()) {
            turnDelay = 0f;
            player.send(new StateEvent(EntityState.IDLE));
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

    private void ifMoveKeys_SetPlayerWalking(Entity player, float dt) {
        if (areMoveKeysPressed()) {
            player.send(new DirectionEvent(direction));

            if (turnDelay > 0f) {
                turnDelay -= dt;
            } else {
                player.send(new StateEvent(EntityState.WALKING));
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
