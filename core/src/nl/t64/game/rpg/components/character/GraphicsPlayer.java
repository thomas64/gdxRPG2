package nl.t64.game.rpg.components.character;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import nl.t64.game.rpg.Utils;
import nl.t64.game.rpg.audio.AudioCommand;
import nl.t64.game.rpg.constants.Constant;
import nl.t64.game.rpg.events.Event;
import nl.t64.game.rpg.events.character.*;


public class GraphicsPlayer extends GraphicsComponent {

    private Vector2 feetPosition;
    private float moveSpeed;
    private float stepCount;

    public GraphicsPlayer() {
        super.loadWalkingAnimation(Constant.PLAYER_ID);
        this.feetPosition = new Vector2();
        this.moveSpeed = Constant.MOVE_SPEED_2;
        this.stepCount = 0f;
    }

    @Override
    public void receive(Event event) {
        if (event instanceof LoadCharacterEvent loadEvent) {
            direction = loadEvent.direction;
        }
        if (event instanceof StateEvent stateEvent) {
            state = stateEvent.state;
        }
        if (event instanceof DirectionEvent directionEvent) {
            direction = directionEvent.direction;
        }
        if (event instanceof PositionEvent positionEvent) {
            position = positionEvent.position;
            feetPosition = getFeetPosition();
        }
        if (event instanceof SpeedEvent speedEvent) {
            setFrameDuration(speedEvent.moveSpeed);
            moveSpeed = speedEvent.moveSpeed;
        }
    }

    @Override
    public void update(float dt) {
        setFrame(dt);
        playStepSoundWhenWalking(dt);
    }

    @Override
    public void render(Character player, Batch batch, ShapeRenderer shapeRenderer) {
        batch.draw(currentFrame, position.x, position.y, Constant.TILE_SIZE, Constant.TILE_SIZE);
    }

    private void playStepSoundWhenWalking(float dt) {
        if (state.equals(CharacterState.WALKING) && moveSpeed != Constant.MOVE_SPEED_4) {
            stepCount = stepCount - dt;
            if (stepCount < 0f) {
                stepCount = frameDuration * 2f;
                Utils.getAudioManager().handle(AudioCommand.SE_PLAY_ONCE,
                                               Utils.getMapManager().getGroundSound(feetPosition));
            }
        } else {
            stepCount = 0f;
        }
    }

    private Vector2 getFeetPosition() {
        return switch (direction) {
            case NORTH -> new Vector2(position.x + (Constant.TILE_SIZE / 2f), position.y + (Constant.TILE_SIZE * 0.2f));
            case SOUTH -> new Vector2(position.x + (Constant.TILE_SIZE / 2f), position.y - (Constant.TILE_SIZE * 0.2f));
            case WEST -> new Vector2(position.x + (Constant.TILE_SIZE * 0.2f), position.y);
            case EAST -> new Vector2(position.x + (Constant.TILE_SIZE * 0.7f), position.y);
            case NONE -> throw new IllegalArgumentException("Direction 'NONE' is not usable.");
        };
    }

}
