package nl.t64.game.rpg.events.character;

import com.badlogic.gdx.math.Vector2;
import lombok.AllArgsConstructor;
import nl.t64.game.rpg.constants.CharacterState;
import nl.t64.game.rpg.constants.Direction;
import nl.t64.game.rpg.events.Event;


@AllArgsConstructor
public class LoadNpcEvent implements Event {
    public final CharacterState state;
    public final Direction direction;
    public final Vector2 position;
}
