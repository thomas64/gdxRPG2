package nl.t64.game.rpg.screens.world.entity

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.InputMultiplexer
import com.badlogic.gdx.InputProcessor
import nl.t64.game.rpg.Utils.preferenceManager
import nl.t64.game.rpg.constants.Constant
import nl.t64.game.rpg.screens.world.entity.events.*


private const val TURN_DELAY_TIME = 8f / 60f // of a second
private const val TURN_DELAY_GRACE_PERIOD = 0.3f
private const val TURN_GRACE_MAX_VALUE = 5f

class InputPlayer(multiplexer: InputMultiplexer) : InputComponent(), InputProcessor {

    private lateinit var player: Entity

    private var pressUp = false
    private var pressDown = false
    private var pressLeft = false
    private var pressRight = false

    private var pressAlign = false

    private var timeUp = 0
    private var timeDown = 0
    private var timeLeft = 0
    private var timeRight = 0
    private var turnDelay = 0f
    private var turnGrace = 0f

    private var pressCtrl = false
    private var pressShift = false
    private var pressAction = false

    init {
        multiplexer.addProcessor(this)
    }

    override fun receive(event: Event) {
        if (event is LoadEntityEvent) {
            direction = event.direction!!
        }
    }

    override fun dispose() {
        Gdx.input.inputProcessor = null
    }

    override fun keyDown(keycode: Int): Boolean {
        return keyPressed(keycode, true)
    }

    override fun keyUp(keycode: Int): Boolean {
        return keyPressed(keycode, false)
    }

    private fun keyPressed(keycode: Int, isPressed: Boolean): Boolean {
        if (keycode == Input.Keys.CONTROL_LEFT
            || keycode == Input.Keys.CONTROL_RIGHT
            || keycode == Constant.KEYCODE_L1
        ) {
            pressCtrl = isPressed
        }
        if (keycode == Input.Keys.SHIFT_LEFT
            || keycode == Input.Keys.SHIFT_RIGHT
            || keycode == Constant.KEYCODE_R1
        ) {
            pressShift = isPressed
        }

        if (keycode == Input.Keys.A
            || keycode == Constant.KEYCODE_BOTTOM
        ) {
            pressAction = isPressed
        }

        if (keycode == Input.Keys.UP) {
            pressUp = isPressed
        }
        if (keycode == Input.Keys.DOWN) {
            pressDown = isPressed
        }
        if (keycode == Input.Keys.LEFT) {
            pressLeft = isPressed
        }
        if (keycode == Input.Keys.RIGHT) {
            pressRight = isPressed
        }

        if (keycode == Input.Keys.SPACE) {
            pressAlign = isPressed
        }
        return false
    }

    override fun keyTyped(character: Char): Boolean {
        return false
    }

    override fun touchDown(screenX: Int, screenY: Int, pointer: Int, button: Int): Boolean {
        return false
    }

    override fun touchUp(screenX: Int, screenY: Int, pointer: Int, button: Int): Boolean {
        return false
    }

    override fun touchDragged(screenX: Int, screenY: Int, pointer: Int): Boolean {
        return false
    }

    override fun mouseMoved(screenX: Int, screenY: Int): Boolean {
        return false
    }

    override fun scrolled(amountX: Float, amountY: Float): Boolean {
        return false
    }

    override fun update(player: Entity, dt: Float) {
        this.player = player
        processMoveInput(dt)
        processOtherInput()
    }

    override fun reset() {
        pressUp = false
        pressDown = false
        pressLeft = false
        pressRight = false

        pressAlign = false

        timeUp = 0
        timeDown = 0
        timeLeft = 0
        timeRight = 0
        turnDelay = 0f
        turnGrace = 0f

        pressCtrl = false
        pressShift = false
        pressAction = false
    }

    private fun processMoveInput(dt: Float) {
        processPlayerSpeedInput()
        countKeyDownTime()
        ifNoMoveKeys_SetGracePeriod(dt)
        ifNoMoveKeys_SetPlayerIdle()
        setPossibleTurnDelay()
        setPlayerDirection()
        ifMoveKeys_SetPlayerWalking(dt)
    }

    private fun processOtherInput() {
        if (pressAlign && preferenceManager.isInDebugMode) {
            player.send(StateEvent(EntityState.ALIGNING))
        }
        if (pressAction) {
            player.send(ActionEvent())
            pressAction = false
        }
    }

    private fun processPlayerSpeedInput() {
        var moveSpeed = Constant.MOVE_SPEED_2
        when {
            pressCtrl && pressShift && preferenceManager.isInDebugMode -> moveSpeed = Constant.MOVE_SPEED_4
            pressShift -> moveSpeed = Constant.MOVE_SPEED_3
            pressCtrl -> moveSpeed = Constant.MOVE_SPEED_1
        }
        player.send(SpeedEvent(moveSpeed))
    }

    private fun countKeyDownTime() {
        when {
            pressUp -> timeUp += 1
            else -> timeUp = 0
        }
        when {
            pressDown -> timeDown += 1
            else -> timeDown = 0
        }
        when {
            pressLeft -> timeLeft += 1
            else -> timeLeft = 0
        }
        when {
            pressRight -> timeRight += 1
            else -> timeRight = 0
        }
    }

    private fun ifNoMoveKeys_SetGracePeriod(dt: Float) {
        if (!areMoveKeysPressed()
            && turnGrace < TURN_GRACE_MAX_VALUE
        ) {
            turnGrace += dt
        }
    }

    private fun ifNoMoveKeys_SetPlayerIdle() {
        if (!areMoveKeysPressed()) {
            turnDelay = 0f
            player.send(StateEvent(EntityState.IDLE))
        }
    }

    private fun setPossibleTurnDelay() {
        if (turnDelay <= 0f
            && turnGrace > TURN_DELAY_GRACE_PERIOD
            && (pressUpButDirectionisNotYetNorth()
                    || pressDownButDirectionIsNotYetSouth()
                    || pressLeftButDirectionIsNotYetWest()
                    || pressRightButDirectionIsNotYetEast())
        ) {
            turnDelay = TURN_DELAY_TIME
        }
    }

    private fun setPlayerDirection() {
        when {
            pressUp && timeUpIsLess() -> direction = Direction.NORTH
            pressDown && timeDownIsLess() -> direction = Direction.SOUTH
            pressLeft && timeLeftIsLess() -> direction = Direction.WEST
            pressRight && timeRightIsLess() -> direction = Direction.EAST

            pressUp -> direction = Direction.NORTH
            pressDown -> direction = Direction.SOUTH
            pressLeft -> direction = Direction.WEST
            pressRight -> direction = Direction.EAST
        }
    }

    private fun ifMoveKeys_SetPlayerWalking(dt: Float) {
        if (areMoveKeysPressed()) {
            player.send(DirectionEvent(direction))

            if (turnDelay > 0f) {
                turnDelay -= dt
            } else {
                turnGrace = 0f
                player.send(StateEvent(EntityState.WALKING))
            }
        }
    }

    private fun areMoveKeysPressed(): Boolean {
        return pressUp || pressDown || pressLeft || pressRight
    }

    private fun pressUpButDirectionisNotYetNorth(): Boolean {
        return pressUp && direction != Direction.NORTH
    }

    private fun pressDownButDirectionIsNotYetSouth(): Boolean {
        return pressDown && direction != Direction.SOUTH
    }

    private fun pressLeftButDirectionIsNotYetWest(): Boolean {
        return pressLeft && direction != Direction.WEST
    }

    private fun pressRightButDirectionIsNotYetEast(): Boolean {
        return pressRight && direction != Direction.EAST
    }

    private fun timeRightIsLess(): Boolean {
        return timeRight <= timeUp || timeRight <= timeDown || timeRight <= timeLeft
    }

    private fun timeLeftIsLess(): Boolean {
        return timeLeft <= timeUp || timeLeft <= timeDown || timeLeft <= timeRight
    }

    private fun timeDownIsLess(): Boolean {
        return timeDown <= timeUp || timeDown <= timeLeft || timeDown <= timeRight
    }

    private fun timeUpIsLess(): Boolean {
        return timeUp <= timeDown || timeUp <= timeLeft || timeUp <= timeRight
    }

}
