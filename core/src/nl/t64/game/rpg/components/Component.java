package nl.t64.game.rpg.components;

import nl.t64.game.rpg.events.Event;


public interface Component {

    void receive(Event event);

    void dispose();

}
