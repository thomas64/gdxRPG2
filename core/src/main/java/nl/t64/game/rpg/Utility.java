package nl.t64.game.rpg;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.TextureLoader;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGeneratorLoader;
import com.badlogic.gdx.graphics.g2d.freetype.FreetypeFontLoader;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.ObjectMap;
import nl.t64.game.rpg.components.character.SpriteConfig;


public final class Utility {

    private Utility() {
        throw new IllegalStateException("Utility class");
    }

    private static final String TAG = Utility.class.getSimpleName();
    private static final AssetManager ASSET_MANAGER = new AssetManager();
    private static final InternalFileHandleResolver FILE_PATH_RESOLVER = new InternalFileHandleResolver();
    private static final ObjectMap<String, SpriteConfig> SPRITE_CONFIGS = new ObjectMap<>();

    public static void unloadAsset(String assetFilenamePath) {
        if (ASSET_MANAGER.isLoaded(assetFilenamePath)) {
            ASSET_MANAGER.unload(assetFilenamePath);
        } else {
            Logger.assetUnloadingFailed(TAG, assetFilenamePath);
        }
    }

    public static TiledMap getMapAsset(String mapFilenamePath) {
        if (!ASSET_MANAGER.isLoaded(mapFilenamePath)) {
            loadMapAsset(mapFilenamePath);
        }
        return ASSET_MANAGER.get(mapFilenamePath, TiledMap.class);
    }

    private static void loadMapAsset(String mapFilenamePath) {
        ASSET_MANAGER.setLoader(TiledMap.class, new TmxMapLoader(FILE_PATH_RESOLVER));
        ASSET_MANAGER.load(mapFilenamePath, TiledMap.class);
        ASSET_MANAGER.finishLoadingAsset(mapFilenamePath);
    }

    public static BitmapFont getTrueTypeAsset(String trueTypeFilenamePath, int fontSize) {
        if (!ASSET_MANAGER.isLoaded(trueTypeFilenamePath)) {
            loadTrueTypeAsset(trueTypeFilenamePath, fontSize);
        }
        return ASSET_MANAGER.get(trueTypeFilenamePath, BitmapFont.class);
    }

    private static void loadTrueTypeAsset(String trueTypeFilenamePath, int fontSize) {
        ASSET_MANAGER.setLoader(FreeTypeFontGenerator.class, new FreeTypeFontGeneratorLoader(FILE_PATH_RESOLVER));
        ASSET_MANAGER.setLoader(BitmapFont.class, ".ttf", new FreetypeFontLoader(FILE_PATH_RESOLVER));
        var parameter = new FreetypeFontLoader.FreeTypeFontLoaderParameter();
        parameter.fontFileName = trueTypeFilenamePath;
        parameter.fontParameters.size = fontSize;
        ASSET_MANAGER.load(trueTypeFilenamePath, BitmapFont.class, parameter);
        ASSET_MANAGER.finishLoadingAsset(trueTypeFilenamePath);
    }

    public static Texture getTextureAsset(String textureFilenamePath) {
        if (!ASSET_MANAGER.isLoaded(textureFilenamePath)) {
            loadTextureAsset(textureFilenamePath);
        }
        return ASSET_MANAGER.get(textureFilenamePath, Texture.class);
    }

    private static void loadTextureAsset(String textureFilenamePath) {
        ASSET_MANAGER.setLoader(Texture.class, new TextureLoader(FILE_PATH_RESOLVER));
        ASSET_MANAGER.load(textureFilenamePath, Texture.class);
        ASSET_MANAGER.finishLoadingAsset(textureFilenamePath);
    }

    public static SpriteConfig getSpriteConfig(String spriteId) {
        if (SPRITE_CONFIGS.isEmpty()) {
            loadSpriteConfigs();
        }
        return SPRITE_CONFIGS.get(spriteId);
    }

    @SuppressWarnings("unchecked")
    private static void loadSpriteConfigs() {
        Json json = new Json();
        FileHandle[] configFiles = FILE_PATH_RESOLVER.resolve("configs/").list(".json");
        for (FileHandle file : configFiles) {
            ObjectMap<String, SpriteConfig> spriteConfigs = json.fromJson(ObjectMap.class, SpriteConfig.class, file);
            SPRITE_CONFIGS.putAll(spriteConfigs);
        }
    }

}
