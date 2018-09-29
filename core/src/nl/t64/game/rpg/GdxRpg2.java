package nl.t64.game.rpg;

import com.badlogic.gdx.Game;
import nl.t64.game.rpg.screens.WorldScreen;

public class GdxRpg2 extends Game {

    public static final int WIDTH = 1280;
    public static final int HEIGHT = 720;
    public static final String TITLE = "gdxRPG2";

    private static final WorldScreen WORLD_SCREEN = new WorldScreen();

    @Override
    public void create() {
        setScreen(WORLD_SCREEN);
    }

    @Override
    public void dispose() {
        WORLD_SCREEN.dispose();
    }
}
