package nl.t64.game.rpg;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.TextureLoader;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;

public final class Utility {

    private static final AssetManager ASSET_MANAGER = new AssetManager();
    private static final String TAG = Utility.class.getSimpleName();
    private static InternalFileHandleResolver filePathResolver = new InternalFileHandleResolver();

    public static void unloadAsset(String assetFilenamePath) {
        if (ASSET_MANAGER.isLoaded(assetFilenamePath)) {
            ASSET_MANAGER.unload(assetFilenamePath);
        } else {
            Logger.assetUnloadingFailed(TAG, assetFilenamePath);
        }
    }

    public static float getProgress() {
        return ASSET_MANAGER.getProgress();
    }

    public static int getNumberQueuedAssets() {
        return ASSET_MANAGER.getQueuedAssets();
    }

    public static boolean updateAssetLoading() {
        return ASSET_MANAGER.update();
    }

    public static boolean isAssetLoaded(String filename) {
        return ASSET_MANAGER.isLoaded(filename);
    }

    public static void loadMapAsset(String mapFilenamePath) {
        if (filePathResolver.resolve(mapFilenamePath).exists()) {
            ASSET_MANAGER.setLoader(TiledMap.class, new TmxMapLoader(filePathResolver));
            ASSET_MANAGER.load(mapFilenamePath, TiledMap.class);
            // todo, loading screen. for now, just block until map is loaded
            ASSET_MANAGER.finishLoadingAsset(mapFilenamePath);
            Logger.mapLoaded(TAG, mapFilenamePath);
        } else {
            Logger.mapLoadingFailed(TAG, mapFilenamePath);
        }
    }

    public static TiledMap getMapAsset(String mapFilenamePath) {
        TiledMap map = null;
        if (isAssetLoaded(mapFilenamePath)) {
            map = ASSET_MANAGER.get(mapFilenamePath, TiledMap.class);
        } else {
            Logger.mapNotLoaded(TAG, mapFilenamePath);
        }
        return map;
    }

    public static void loadTextureAsset(String textureFilenamePath) {
        if (filePathResolver.resolve(textureFilenamePath).exists()) {
            ASSET_MANAGER.setLoader(Texture.class, new TextureLoader(filePathResolver));
            ASSET_MANAGER.load(textureFilenamePath, Texture.class);
            // todo, loading screen. for now, just block until texture is loaded
            ASSET_MANAGER.finishLoadingAsset(textureFilenamePath);
            Logger.textureLoaded(TAG, textureFilenamePath);
        } else {
            Logger.textureLoadingFailed(TAG, textureFilenamePath);
        }
    }

    public static Texture getTextureAsset(String textureFilenamePath) {
        Texture texture = null;
        if (isAssetLoaded(textureFilenamePath)) {
            texture = ASSET_MANAGER.get(textureFilenamePath, Texture.class);
        } else {
            Logger.textureNotLoaded(TAG, textureFilenamePath);
        }
        return texture;
    }

}
