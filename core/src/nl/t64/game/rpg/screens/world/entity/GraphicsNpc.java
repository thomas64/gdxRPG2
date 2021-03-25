package nl.t64.game.rpg.screens.world.entity;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import nl.t64.game.rpg.constants.Constant;
import nl.t64.game.rpg.screens.world.entity.events.*;


public class GraphicsNpc extends GraphicsComponent {

    public GraphicsNpc(String spriteId) {
        this.frameDuration = Constant.NORMAL_FRAMES;
        loadWalkingAnimation(spriteId);
    }

    @Override
    public void receive(Event event) {
        if (event instanceof LoadEntityEvent loadEvent) {
            state = loadEvent.state;
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
    }

    @Override
    public void update(float dt) {
        setFrame(dt);
    }

    @Override
    public void render(Batch batch) {
        if (state.equals(EntityState.INVISIBLE)) {
            return;
        }
        batch.draw(currentFrame, position.x, position.y, Constant.TILE_SIZE, Constant.TILE_SIZE);
    }

    @Override
    public void renderOnMiniMap(Entity entity, Batch batch, ShapeRenderer shapeRenderer) {
        if (entity.getConversationId().contains("shop") && !state.equals(EntityState.INVISIBLE)) {
            shapeRenderer.setColor(Color.YELLOW);
            shapeRenderer.circle(position.x + Constant.HALF_TILE_SIZE,
                                 position.y + Constant.HALF_TILE_SIZE,
                                 Constant.HALF_TILE_SIZE);
        }
    }

}
