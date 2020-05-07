package nl.t64.game.rpg.events.character;

import com.badlogic.gdx.math.Vector2;
import nl.t64.game.rpg.constants.CharacterState;
import nl.t64.game.rpg.constants.Direction;
import nl.t64.game.rpg.events.Event;


public class LoadCharacterEvent implements Event {

    public final CharacterState state;
    public final Direction direction;
    public final Vector2 position;
    public final String conversationId;

    public LoadCharacterEvent(Direction direction, Vector2 position) {
        this(null, direction, position, null);
    }

    public LoadCharacterEvent(CharacterState state, Direction direction, Vector2 position) {
        this(state, direction, position, null);
    }

    public LoadCharacterEvent(CharacterState state, Direction direction, Vector2 position, String conversationId) {
        this.state = state;
        this.direction = direction;
        this.position = position;
        this.conversationId = conversationId;
    }

}
