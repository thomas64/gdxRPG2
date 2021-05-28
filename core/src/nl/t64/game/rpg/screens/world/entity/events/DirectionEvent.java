package nl.t64.game.rpg.screens.world.entity.events;

import nl.t64.game.rpg.screens.world.entity.Direction;


public record DirectionEvent(Direction direction) implements Event {
}
