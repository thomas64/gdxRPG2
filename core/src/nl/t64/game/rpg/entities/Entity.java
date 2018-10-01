package nl.t64.game.rpg.entities;

import com.badlogic.gdx.graphics.g2d.Batch;
import lombok.Getter;
import lombok.Setter;
import nl.t64.game.rpg.MapManager;
import nl.t64.game.rpg.components.GraphicsComponent;
import nl.t64.game.rpg.components.InputComponent;
import nl.t64.game.rpg.components.PhysicsComponent;
import nl.t64.game.rpg.constants.EntityState;


public class Entity {

    private static final String TAG = Entity.class.getSimpleName();

    @Getter
    @Setter
    private EntityState state = EntityState.IDLE;

    @Getter
    private InputComponent inputComponent;
    @Getter
    private PhysicsComponent physicsComponent;
    private GraphicsComponent graphicsComponent;

    public Entity() {
        inputComponent = new InputComponent();
        physicsComponent = new PhysicsComponent();
        graphicsComponent = new GraphicsComponent();
    }

    public void update(MapManager mapManager, float dt) {
        inputComponent.update(this, dt);
        physicsComponent.update(mapManager);
        graphicsComponent.update(this);
    }

    public void render(Batch batch) {
        graphicsComponent.render(batch, this);
    }

    public void dispose() {
        inputComponent.dispose();
        graphicsComponent.dispose();
        physicsComponent.dispose();
    }

}
