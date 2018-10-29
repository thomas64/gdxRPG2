package nl.t64.game.rpg;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Screen;
import nl.t64.game.rpg.constants.ScreenType;
import nl.t64.game.rpg.screens.WorldScreen;
import nl.t64.game.rpg.screens.menu.LoadMenu;
import nl.t64.game.rpg.screens.menu.MainMenu;
import nl.t64.game.rpg.screens.menu.NewMenu;


public class GdxRpg2 extends Game {

    private MainMenu mainMenuScreen;
    private NewMenu newGameMenuScreen;
    private LoadMenu loadGameMenuScreen;
    private WorldScreen worldScreen;

    public Screen getScreenType(ScreenType screenType) {
        switch (screenType) {
            case MAIN_MENU:
                return mainMenuScreen;
            case NEW_GAME_MENU:
                return newGameMenuScreen;
            case LOAD_GAME_MENU:
                return loadGameMenuScreen;
            case WORLD:
                return worldScreen;

            default:
                return mainMenuScreen;
        }
    }

    @Override
    public void create() {
        mainMenuScreen = new MainMenu(this);
        newGameMenuScreen = new NewMenu(this);
        loadGameMenuScreen = new LoadMenu(this);
        worldScreen = new WorldScreen();
        setScreen(mainMenuScreen);
    }

    @Override
    public void dispose() {
        mainMenuScreen.dispose();
        newGameMenuScreen.dispose();
        loadGameMenuScreen.dispose();
        worldScreen.dispose();
    }

}
