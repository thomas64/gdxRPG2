package nl.t64.game.rpg.screens.world.entity.events;

import com.badlogic.gdx.math.Vector2;
import nl.t64.game.rpg.screens.world.entity.Direction;
import nl.t64.game.rpg.screens.world.entity.EntityState;


public class LoadEntityEvent implements Event {

    public final EntityState state;
    public final Direction direction;
    public final Vector2 position;
    public final String conversationOrBattleId;

    public LoadEntityEvent(Vector2 position) {
        this(null, null, position, null);
    }

    public LoadEntityEvent(Direction direction, Vector2 position) {
        this(null, direction, position, null);
    }

    public LoadEntityEvent(EntityState state, Vector2 position) {
        this(state, null, position, null);
    }

    public LoadEntityEvent(EntityState state, Direction direction, Vector2 position) {
        this(state, direction, position, null);
    }

    public LoadEntityEvent(EntityState state, Direction direction, Vector2 position, String conversationOrBattleId) {
        this.state = state;
        this.direction = direction;
        this.position = position;
        this.conversationOrBattleId = conversationOrBattleId;
    }

}
