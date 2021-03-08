package nl.t64.game.rpg;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.controllers.Controllers;
import nl.t64.game.rpg.audio.AudioManager;
import nl.t64.game.rpg.constants.ScreenType;
import nl.t64.game.rpg.profile.ProfileManager;
import nl.t64.game.rpg.screens.ScreenManager;
import nl.t64.game.rpg.screens.world.MapManager;


public class Engine extends Game {

    private static float runTime = 0f;

    final ResourceManager resourceManager;
    final Settings settings;
    final ProfileManager profileManager;
    final GameData gameData;
    final ScreenManager screenManager;
    final AudioManager audioManager;
    final MapManager mapManager;
    final GamepadMapping gamepadMapping;

    public Engine(Settings settings) {
        this.resourceManager = new ResourceManager();
        this.settings = settings;
        this.profileManager = new ProfileManager();
        this.gameData = new GameData();
        this.screenManager = new ScreenManager();
        this.audioManager = new AudioManager();
        this.mapManager = new MapManager();
        this.gamepadMapping = new GamepadMapping();
    }

    private static void updateRunTime() {
        runTime += Gdx.graphics.getDeltaTime();
    }

    public static float getRunTime() {
        return runTime;
    }

    @Override
    public void create() {
        profileManager.profileSubject.addObserver(gameData);
        profileManager.profileSubject.addObserver(mapManager);

        Controllers.addListener(gamepadMapping.controllerToInputAdapter);

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
