package nl.t64.game.rpg;

import com.badlogic.gdx.Game;
import nl.t64.game.rpg.screens.AdventureScreen;

public class GdxRpg2 extends Game {

    private static final AdventureScreen ADVENTURE_SCREEN = new AdventureScreen();

    @Override
    public void create() {
        this.setScreen(ADVENTURE_SCREEN);
    }

    @Override
    public void dispose() {
        ADVENTURE_SCREEN.dispose();
    }
}
