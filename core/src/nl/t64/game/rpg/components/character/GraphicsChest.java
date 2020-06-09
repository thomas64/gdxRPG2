package nl.t64.game.rpg.components.character;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import nl.t64.game.rpg.Utils;
import nl.t64.game.rpg.constants.CharacterState;
import nl.t64.game.rpg.constants.Constant;
import nl.t64.game.rpg.events.Event;
import nl.t64.game.rpg.events.character.LoadCharacterEvent;
import nl.t64.game.rpg.events.character.StateEvent;

import java.util.List;


public class GraphicsChest extends GraphicsComponent {

    private final List<TextureRegion> chestImage;

    public GraphicsChest() {
        this.chestImage = Utils.getChestImage();
    }

    @Override
    public void receive(Event event) {
        if (event instanceof LoadCharacterEvent loadEvent) {
            position = loadEvent.position;
            state = loadEvent.state;
        }
        if (event instanceof StateEvent stateEvent) {
            state = stateEvent.state;
        }
    }

    @Override
    public void update(float dt) {
        setFrame(dt);
    }

    @Override
    public void render(Character chest, Batch batch, ShapeRenderer shapeRenderer) {
        batch.draw(currentFrame, position.x, position.y, Constant.TILE_SIZE, Constant.TILE_SIZE);
    }

    @Override
    void setFrame(float dt) {
        if (state.equals(CharacterState.OPENED)) {
            currentFrame = chestImage.get(1);
        } else {
            currentFrame = chestImage.get(0);
        }
    }

}
