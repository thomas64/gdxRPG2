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

    public static void unitScaleValue(String tag, float unitScale) {
        Gdx.app.debug(tag, "UnitScale value is: " + unitScale);
    }

    public static void worldRenderer(String tag, float vw, float vh, float w, float h, float pw, float ph) {
        Gdx.app.debug(tag, "WorldRenderer: virtual: (" + vw + "," + vh + ")");
        Gdx.app.debug(tag, "WorldRenderer: viewport: (" + w + "," + h + ")");
        Gdx.app.debug(tag, "WorldRenderer: physical: (" + pw + "," + ph + ")");
    }

    public static void portalActivated(String tag) {
        Gdx.app.debug(tag, "Portal Activated");
    }

    public static void playerSpawnLocation(String tag, float x, float y) {
        Gdx.app.debug(tag, "Player Spawn Location: (" + x + "," + y + ")");
    }

    public static void boundingBoxIsZero(String tag, float width, float height) {
        Gdx.app.debug(tag, "Width or Height is 0!! " + width + ":" + height);
    }


}
