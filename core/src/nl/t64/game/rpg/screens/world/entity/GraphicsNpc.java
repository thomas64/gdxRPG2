package nl.t64.game.rpg.screens.world.entity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import nl.t64.game.rpg.constants.Constant;
import nl.t64.game.rpg.screens.world.entity.events.*;


public class GraphicsNpc extends GraphicsComponent {

    private static final Color SELECTION = new Color(0f, 1f, 1f, 0.5f);

    private boolean isSelected;

    public GraphicsNpc(String spriteId) {
        this.isSelected = false;
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
        if (event instanceof SelectEvent) {
            if (!state.equals(EntityState.INVISIBLE)) {
                isSelected = true;
            }
        }
        if (event instanceof DeselectEvent) {
            isSelected = false;
        }
    }

    @Override
    public void update(float dt) {
        setFrame(dt);
    }

    @Override
    public void render(Entity npcCharacter, Batch batch, ShapeRenderer shapeRenderer) {
        if (state.equals(EntityState.INVISIBLE)) {
            return;
        }
        batch.end();
        if (isSelected) {
            drawSelected(npcCharacter.getBoundingBox(), shapeRenderer);
        }
        batch.begin();
        batch.draw(currentFrame, position.x, position.y, Constant.TILE_SIZE, Constant.TILE_SIZE);

    }

    private void drawSelected(Rectangle boundingBox, ShapeRenderer shapeRenderer) {
        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(SELECTION);
        float width = Constant.TILE_SIZE;
        float height = boundingBox.getHeight();
        float x = boundingBox.x + (boundingBox.width / 2f) - (width / 2f);
        float y = boundingBox.y - (height / 2f);
        shapeRenderer.ellipse(x, y, width, height);
        shapeRenderer.end();
        Gdx.gl.glDisable(GL20.GL_BLEND);
    }

}