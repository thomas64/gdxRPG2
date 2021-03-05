package nl.t64.game.rpg.screens.world.entity;

import nl.t64.game.rpg.screens.world.entity.events.Event;


interface Component {

    void receive(Event event);

    void dispose();

}
