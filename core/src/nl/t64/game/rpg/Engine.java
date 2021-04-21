package nl.t64.game.rpg;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.controllers.Controllers;
import nl.t64.game.rpg.audio.AudioManager;
import nl.t64.game.rpg.constants.ScreenType;
import nl.t64.game.rpg.screens.ScreenManager;
import nl.t64.game.rpg.screens.world.MapManager;


public class Engine extends Game {

    private static float runTime = 0f;

    PreferenceManager preferenceManager;
    final ResourceManager resourceManager;
    final ProfileManager profileManager;
    final GameData gameData;
    final ScreenManager screenManager;
    final AudioManager audioManager;
    final MapManager mapManager;
    final BrokerManager brokerManager;
    final GamepadMapping gamepadMapping;

    public Engine() {
        this.resourceManager = new ResourceManager();
        this.profileManager = new ProfileManager();
        this.gameData = new GameData();
        this.screenManager = new ScreenManager();
        this.audioManager = new AudioManager();
        this.mapManager = new MapManager();
        this.brokerManager = new BrokerManager();
        this.gamepadMapping = new GamepadMapping();
    }

    private static void updateRunTime() {
        runTime += Gdx.graphics.getDeltaTime();
    }

    public static float getRunTime() {
        return runTime;
    }

    PreferenceManager getPreferenceManager() {
        if (preferenceManager == null) {
            preferenceManager = new PreferenceManager();
        }
        return preferenceManager;
    }

    @Override
    public void create() {
        brokerManager.profileObservers.addObserver(gameData);
        brokerManager.profileObservers.addObserver(mapManager);

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
