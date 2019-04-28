package nl.t64.game.rpg;

import com.badlogic.gdx.Gdx;
import nl.t64.game.rpg.profile.ProfileManager;
import nl.t64.game.rpg.screens.ScreenManager;
import nl.t64.game.rpg.screens.world.MapManager;


public final class Utils {

    private Utils() {
        throw new IllegalStateException("Utils class");
    }

    public static ResourceManager getResourceManager() {
        return getEngine().resourceManager;
    }

    public static Settings getSettings() {
        return getEngine().settings;
    }

    public static ProfileManager getProfileManager() {
        return getEngine().profileManager;
    }

    public static GameData getGameData() {
        return getEngine().gameData;
    }

    public static ScreenManager getScreenManager() {
        return getEngine().screenManager;
    }

    public static MapManager getMapManager() {
        return getEngine().mapManager;
    }

    private static Engine getEngine() {
        return (Engine) Gdx.app.getApplicationListener();
    }

}
