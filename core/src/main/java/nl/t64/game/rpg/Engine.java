package nl.t64.game.rpg;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import lombok.Getter;
import nl.t64.game.rpg.profile.ProfileManager;
import nl.t64.game.rpg.screens.WorldScreen;
import nl.t64.game.rpg.screens.menu.*;


@Getter
public class Engine extends Game {

    @Getter
    private static float runTime = 0f;

    private Settings settings;
    private ProfileManager profileManager;
    private GameData gameData;

    private MainMenu mainMenuScreen;
    private NewMenu newGameMenuScreen;
    private LoadMenu loadGameMenuScreen;
    private SettingsMenu settingsMenuScreen;
    private WorldScreen worldScreen;
    private PauseMenu pauseMenuScreen;

    public Engine(Settings settings) {
        this.settings = settings;
    }

    private static void updateRunTime() {
        runTime += Gdx.graphics.getDeltaTime();
    }

    @Override
    public void create() {
        profileManager = new ProfileManager();
        gameData = new GameData();

        mainMenuScreen = new MainMenu(this);
        newGameMenuScreen = new NewMenu(this);
        loadGameMenuScreen = new LoadMenu(this);
        settingsMenuScreen = new SettingsMenu(this);
        worldScreen = new WorldScreen(this);
        pauseMenuScreen = new PauseMenu(this);

        profileManager.addObserver(gameData);
        profileManager.addObserver(worldScreen);

        setScreen(mainMenuScreen);
    }

    @Override
    public void render() {
        updateRunTime();
        super.render();
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
