package nl.t64.game.rpg.components.menu;

import nl.t64.game.rpg.GdxRpg2;
import nl.t64.game.rpg.components.Component;
import nl.t64.game.rpg.entities.Menu;


public abstract class PhysicsComponent implements Component {

    public abstract void update(Menu menu, GdxRpg2 game);

}
