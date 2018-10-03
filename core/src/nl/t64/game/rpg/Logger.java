package nl.t64.game.rpg;

import com.badlogic.gdx.Gdx;


public class Logger {

    public static void assetUnloadingFailed(String tag, String assetFilenamePath) {
        Gdx.app.debug(tag, "Asset is not loaded; Nothing to unload: " + assetFilenamePath);
    }

    public static void mapLoaded(String tag, String mapFilenamePath) {
        Gdx.app.debug(tag, "Map loaded: " + mapFilenamePath);
    }

    public static void mapNotLoaded(String tag, String mapFilenamePath) {
        Gdx.app.debug(tag, "Map is not loaded: " + mapFilenamePath);
    }

    public static void mapLoadingFailed(String tag, String mapFilenamePath) {
        Gdx.app.debug(tag, "Map doesn't exist: " + mapFilenamePath);
    }

    public static void textureLoaded(String tag, String textureFilenamePath) {
        Gdx.app.debug(tag, "Texture loaded: " + textureFilenamePath);
    }

    public static void textureNotLoaded(String tag, String textureFilenamePath) {
        Gdx.app.debug(tag, "Texture is not loaded: " + textureFilenamePath);
    }

    public static void textureLoadingFailed(String tag, String textureFilenamePath) {
        Gdx.app.debug(tag, "Texture doesn't exist: " + textureFilenamePath);
    }

}
