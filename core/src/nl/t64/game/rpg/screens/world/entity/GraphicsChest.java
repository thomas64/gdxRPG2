package nl.t64.game.rpg.screens.world.entity;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import nl.t64.game.rpg.Utils;
import nl.t64.game.rpg.constants.Constant;
import nl.t64.game.rpg.screens.world.entity.events.Event;
import nl.t64.game.rpg.screens.world.entity.events.LoadEntityEvent;
import nl.t64.game.rpg.screens.world.entity.events.StateEvent;

import java.util.List;


public class GraphicsChest extends GraphicsComponent {

    private final List<TextureRegion> chestImage;

    public GraphicsChest() {
        this.chestImage = Utils.getChestImage();
    }

    @Override
    public void receive(Event event) {
        if (event instanceof LoadEntityEvent loadEvent) {
            position = loadEvent.position;
            state = loadEvent.state;
        }
        if (event instanceof StateEvent stateEvent) {
            state = stateEvent.state();
        }
    }

    @Override
    public void update(float dt) {
        setFrame(dt);
    }

    @Override
    public void render(Batch batch) {
        batch.draw(currentFrame, position.x, position.y, Constant.TILE_SIZE, Constant.TILE_SIZE);
    }

    @Override
    public void renderOnMiniMap(Entity entity, Batch batch, ShapeRenderer shapeRenderer) {
        // empty
    }

    @Override
    void setFrame(float dt) {
        if (state.equals(EntityState.OPENED)) {
            currentFrame = getOpenFrame();
        } else {
            currentFrame = getClosedFrame();
        }
    }

    private TextureRegion getOpenFrame() {
        return chestImage.get(1);
    }

    private TextureRegion getClosedFrame() {
        return chestImage.get(0);
    }

}
