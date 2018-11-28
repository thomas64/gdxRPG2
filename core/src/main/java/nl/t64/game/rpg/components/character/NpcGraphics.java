package nl.t64.game.rpg.components.character;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import nl.t64.game.rpg.Utility;
import nl.t64.game.rpg.constants.Constant;
import nl.t64.game.rpg.events.Event;
import nl.t64.game.rpg.events.character.*;


public class NpcGraphics extends GraphicsComponent {

    private static final String TAG = NpcGraphics.class.getSimpleName();

    private boolean isSelected = false;

    public NpcGraphics(String spriteId) {
        this.frameDuration = 0.25f;
        SpriteConfig spriteConfig = Utility.getSpriteConfig(spriteId);
        loadWalkingAnimation(spriteConfig);
    }

    @Override
    public void receive(Event event) {
        if (event instanceof StartStateEvent) {
            state = ((StartStateEvent) event).getState();
        }
        if (event instanceof StateEvent) {
            state = ((StateEvent) event).getState();
        }
        if (event instanceof StartDirectionEvent) {
            direction = ((StartDirectionEvent) event).getDirection();
        }
        if (event instanceof DirectionEvent) {
            direction = ((DirectionEvent) event).getDirection();
        }
        if (event instanceof PositionEvent) {
            position = ((PositionEvent) event).getPosition();
        }
        if (event instanceof SelectEvent) {
            isSelected = true;
        }
        if (event instanceof DeselectEvent) {
            isSelected = false;
        }

    }

    @Override
    public void dispose() {
        // empty
    }

    @Override
    public void update() {
        setFrame();
    }

    @Override
    public void render(Character npcCharacter, Batch batch, ShapeRenderer shapeRenderer) {
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
        shapeRenderer.setColor(0f, 1f, 1f, 0.5f);
        float width = boundingBox.getWidth();
        float height = boundingBox.getHeight() / 3f;
        float x = boundingBox.x;
        float y = boundingBox.y - height / 2f;
        shapeRenderer.ellipse(x, y, width, height);
        shapeRenderer.end();
        Gdx.gl.glDisable(GL20.GL_BLEND);
    }

}
