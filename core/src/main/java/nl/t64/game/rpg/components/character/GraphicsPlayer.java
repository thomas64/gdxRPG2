package nl.t64.game.rpg.components.character;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import nl.t64.game.rpg.constants.Constant;
import nl.t64.game.rpg.events.Event;
import nl.t64.game.rpg.events.character.*;


public class GraphicsPlayer extends GraphicsComponent {

    public GraphicsPlayer() {
        loadWalkingAnimation(Constant.PLAYER_ID);
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
        }
        if (event instanceof SpeedEvent speedEvent) {
            setFrameDuration(speedEvent.moveSpeed);
        }
    }

    @Override
    public void update(float dt) {
        setFrame(dt);
    }

    @Override
    public void render(Character player, Batch batch, ShapeRenderer shapeRenderer) {
        batch.draw(currentFrame, position.x, position.y, Constant.TILE_SIZE, Constant.TILE_SIZE);
    }

}
