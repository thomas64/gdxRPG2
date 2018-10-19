package nl.t64.game.rpg;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Screen;
import nl.t64.game.rpg.constants.ScreenType;
import nl.t64.game.rpg.screens.MainMenuScreen;
import nl.t64.game.rpg.screens.WorldScreen;


public class GdxRpg2 extends Game {

    private WorldScreen worldScreen;
    private MainMenuScreen mainMenuScreen;

    public Screen getScreenType(ScreenType screenType) {
        switch (screenType) {
            case MAIN_MENU:
                return mainMenuScreen;
            case WORLD:
                return worldScreen;
            default:
                return mainMenuScreen;
        }
    }

    @Override
    public void create() {
        mainMenuScreen = new MainMenuScreen(this);
        worldScreen = new WorldScreen();
        setScreen(mainMenuScreen);
    }

    @Override
    public void dispose() {
        mainMenuScreen.dispose();
        worldScreen.dispose();
    }
}
