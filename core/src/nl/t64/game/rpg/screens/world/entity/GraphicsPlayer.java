package nl.t64.game.rpg.screens.world.entity;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import nl.t64.game.rpg.Utils;
import nl.t64.game.rpg.audio.AudioCommand;
import nl.t64.game.rpg.constants.Constant;
import nl.t64.game.rpg.screens.world.entity.events.*;


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
        if (event instanceof LoadEntityEvent loadEvent) {
            direction = loadEvent.direction;
        }
        if (event instanceof StateEvent stateEvent) {
            state = stateEvent.state();
        }
        if (event instanceof DirectionEvent directionEvent) {
            direction = directionEvent.direction();
        }
        if (event instanceof PositionEvent positionEvent) {
            position = positionEvent.position();
            feetPosition = new Vector2(position.x + Constant.HALF_TILE_SIZE, position.y);
        }
        if (event instanceof SpeedEvent speedEvent) {
            setFrameDuration(speedEvent.moveSpeed());
            moveSpeed = speedEvent.moveSpeed();
        }
    }

    @Override
    public void update(float dt) {
        setFrame(dt);
        playStepSoundWhenWalking(dt);
    }

    @Override
    public void render(Batch batch) {
        batch.draw(currentFrame, position.x, position.y, Constant.TILE_SIZE, Constant.TILE_SIZE);
    }

    @Override
    public void renderOnMiniMap(Entity entity, Batch batch, ShapeRenderer shapeRenderer) {
        shapeRenderer.setColor(Color.BLUE);
        switch (direction) {
            case NORTH -> shapeRenderer.triangle(position.x,
                                                 position.y,
                                                 position.x + Constant.HALF_TILE_SIZE,
                                                 position.y + Constant.TILE_SIZE,
                                                 position.x + Constant.TILE_SIZE,
                                                 position.y);
            case SOUTH -> shapeRenderer.triangle(position.x,
                                                 position.y + Constant.TILE_SIZE,
                                                 position.x + Constant.TILE_SIZE,
                                                 position.y + Constant.TILE_SIZE,
                                                 position.x + Constant.HALF_TILE_SIZE,
                                                 position.y);
            case WEST -> shapeRenderer.triangle(position.x,
                                                position.y + Constant.HALF_TILE_SIZE,
                                                position.x + Constant.TILE_SIZE,
                                                position.y + Constant.TILE_SIZE,
                                                position.x + Constant.TILE_SIZE,
                                                position.y);
            case EAST -> shapeRenderer.triangle(position.x,
                                                position.y,
                                                position.x,
                                                position.y + Constant.TILE_SIZE,
                                                position.x + Constant.TILE_SIZE,
                                                position.y + Constant.HALF_TILE_SIZE);
            case NONE -> throw new IllegalArgumentException("Direction 'NONE' is not usable.");
        }
    }

    private void playStepSoundWhenWalking(float dt) {
        if (state.equals(EntityState.WALKING) && moveSpeed != Constant.MOVE_SPEED_4) {
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

}
