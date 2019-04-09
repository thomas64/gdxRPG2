package nl.t64.game.rpg.components.character;

import nl.t64.game.rpg.events.Event;


interface Component {

    void receive(Event event);

    void dispose();

}
