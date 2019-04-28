package nl.t64.game.rpg;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import nl.t64.game.rpg.constants.ScreenType;
import nl.t64.game.rpg.profile.ProfileManager;
import nl.t64.game.rpg.screens.ScreenManager;
import nl.t64.game.rpg.screens.world.MapManager;


public class Engine extends Game {

    public static float runTime = 0f;

    final ResourceManager resourceManager;
    final Settings settings;
    final ProfileManager profileManager;
    final GameData gameData;
    final ScreenManager screenManager;
    final MapManager mapManager;

    public Engine(Settings settings) {
        this.resourceManager = new ResourceManager();
        this.settings = settings;
        this.profileManager = new ProfileManager();
        this.gameData = new GameData();
        this.screenManager = new ScreenManager();
        this.mapManager = new MapManager();
    }

    private static void updateRunTime() {
        runTime += Gdx.graphics.getDeltaTime();
    }

    @Override
    public void create() {
        profileManager.addObserver(gameData);
        profileManager.addObserver(mapManager);

        screenManager.setScreen(ScreenType.MENU_MAIN);
    }

    @Override
    public void render() {
        updateRunTime();
        super.render();
    }

    @Override
    public void dispose() {
        screenManager.dispose();
    }

}
