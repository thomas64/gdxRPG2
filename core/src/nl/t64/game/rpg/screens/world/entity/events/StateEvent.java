package nl.t64.game.rpg.screens.world.entity.events;

import nl.t64.game.rpg.screens.world.entity.EntityState;


public record StateEvent(EntityState state) implements Event {
}
