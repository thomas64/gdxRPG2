package nl.t64.game.rpg;

import com.badlogic.gdx.Gdx;


public class Logger {

    private Logger() {
        throw new IllegalStateException("Logger class");
    }

    public static void assetUnloadingFailed(String tag, String assetFilenamePath) {
        Gdx.app.debug(tag, "Asset is not loaded; Nothing to unload: " + assetFilenamePath);
    }

}
