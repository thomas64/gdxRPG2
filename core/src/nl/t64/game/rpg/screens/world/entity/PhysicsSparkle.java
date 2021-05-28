package nl.t64.game.rpg.screens.world.entity;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import nl.t64.game.rpg.Utils;
import nl.t64.game.rpg.audio.AudioEvent;
import nl.t64.game.rpg.components.loot.Loot;
import nl.t64.game.rpg.screens.world.entity.events.Event;
import nl.t64.game.rpg.screens.world.entity.events.LoadEntityEvent;
import nl.t64.game.rpg.screens.world.entity.events.OnActionEvent;


public class PhysicsSparkle extends PhysicsComponent {

    private final Loot sparkle;
    private boolean isSelected;

    public PhysicsSparkle(Loot sparkle) {
        this.sparkle = sparkle;
        this.isSelected = false;
    }

    @Override
    public void receive(Event event) {
        if (event instanceof LoadEntityEvent loadEvent) {
            currentPosition = loadEvent.position;
        }
        if (event instanceof OnActionEvent onActionEvent) {
            if (onActionEvent.checkRect().overlaps(getRectangle())) {
                isSelected = true;
            }
        }
    }

    @Override
    public void update(Entity entity, float dt) {
        if (isSelected) {
            isSelected = false;
            Utils.getBrokerManager().componentObservers.notifyShowFindDialog(sparkle, AudioEvent.SE_SPARKLE);
        }
    }

    @Override
    public void debug(ShapeRenderer shapeRenderer) {
        // empty
    }

}
