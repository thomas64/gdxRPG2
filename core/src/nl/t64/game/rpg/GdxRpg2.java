package nl.t64.game.rpg;

import com.badlogic.gdx.Game;
import lombok.Getter;
import nl.t64.game.rpg.screens.WorldScreen;
import nl.t64.game.rpg.screens.menu.*;


@Getter
public class GdxRpg2 extends Game {

    private static final GdxRpg2 INSTANCE = new GdxRpg2();

    private MainMenu mainMenuScreen;
    private NewMenu newGameMenuScreen;
    private LoadMenu loadGameMenuScreen;
    private SettingsMenu settingsMenuScreen;
    private WorldScreen worldScreen;
    private PauseMenu pauseMenuScreen;

    private GdxRpg2() {
    }

    public static GdxRpg2 getInstance() {
        return INSTANCE;
    }

    @Override
    public void create() {
        mainMenuScreen = new MainMenu();
        newGameMenuScreen = new NewMenu();
        loadGameMenuScreen = new LoadMenu();
        settingsMenuScreen = new SettingsMenu();
        worldScreen = new WorldScreen();
        pauseMenuScreen = new PauseMenu();
        setScreen(mainMenuScreen);
    }

    @Override
    public void dispose() {
        mainMenuScreen.dispose();
        newGameMenuScreen.dispose();
        loadGameMenuScreen.dispose();
        settingsMenuScreen.dispose();
        worldScreen.dispose();
        pauseMenuScreen.dispose();
    }

}
