package nl.t64.game.rpg;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.TextureLoader;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGeneratorLoader;
import com.badlogic.gdx.graphics.g2d.freetype.FreetypeFontLoader;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.ObjectMap;
import nl.t64.game.rpg.events.character.LoadSpriteEvent;


public final class Utility {

    private Utility() {
        throw new IllegalStateException("Utility class");
    }

    private static final String TAG = Utility.class.getSimpleName();
    private static final AssetManager ASSET_MANAGER = new AssetManager();
    private static final InternalFileHandleResolver FILE_PATH_RESOLVER = new InternalFileHandleResolver();

    public static void unloadAsset(String assetFilenamePath) {
        if (ASSET_MANAGER.isLoaded(assetFilenamePath)) {
            ASSET_MANAGER.unload(assetFilenamePath);
        } else {
            Logger.assetUnloadingFailed(TAG, assetFilenamePath);
        }
    }

    public static TiledMap getMapAsset(String mapFilenamePath) {
        if (!ASSET_MANAGER.isLoaded(mapFilenamePath)) {
            ASSET_MANAGER.setLoader(TiledMap.class, new TmxMapLoader(FILE_PATH_RESOLVER));
            ASSET_MANAGER.load(mapFilenamePath, TiledMap.class);
            ASSET_MANAGER.finishLoadingAsset(mapFilenamePath);
        }
        return ASSET_MANAGER.get(mapFilenamePath, TiledMap.class);
    }

    public static BitmapFont getTrueTypeAsset(String trueTypeFilenamePath, int fontSize) {
        if (!ASSET_MANAGER.isLoaded(trueTypeFilenamePath)) {
            ASSET_MANAGER.setLoader(FreeTypeFontGenerator.class, new FreeTypeFontGeneratorLoader(FILE_PATH_RESOLVER));
            ASSET_MANAGER.setLoader(BitmapFont.class, ".ttf", new FreetypeFontLoader(FILE_PATH_RESOLVER));
            var parameter = new FreetypeFontLoader.FreeTypeFontLoaderParameter();
            parameter.fontFileName = trueTypeFilenamePath;
            parameter.fontParameters.size = fontSize;
            ASSET_MANAGER.load(trueTypeFilenamePath, BitmapFont.class, parameter);
            ASSET_MANAGER.finishLoadingAsset(trueTypeFilenamePath);
        }
        return ASSET_MANAGER.get(trueTypeFilenamePath, BitmapFont.class);
    }

    public static Texture getTextureAsset(String textureFilenamePath) {
        if (!ASSET_MANAGER.isLoaded(textureFilenamePath)) {
            ASSET_MANAGER.setLoader(Texture.class, new TextureLoader(FILE_PATH_RESOLVER));
            ASSET_MANAGER.load(textureFilenamePath, Texture.class);
            ASSET_MANAGER.finishLoadingAsset(textureFilenamePath);
        }
        return ASSET_MANAGER.get(textureFilenamePath, Texture.class);
    }

    @SuppressWarnings("unchecked")
    public static ObjectMap<String, LoadSpriteEvent> getAllSpriteConfigsFromJson(String configFilenamePath) {
        Json json = new Json();
        return json.fromJson(ObjectMap.class, LoadSpriteEvent.class, FILE_PATH_RESOLVER.resolve(configFilenamePath));
    }

}
