package nl.t64.game.rpg;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.TextureLoader;
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


public class ResourceManager {

    private final AssetManager assetManager;
    private final ObjectMap<String, SpriteConfig> spriteConfigs;

    public ResourceManager() {
        this.assetManager = new AssetManager();
        this.spriteConfigs = new ObjectMap<>();
    }

    public void unloadAsset(String assetFilenamePath) {
        if (assetManager.isLoaded(assetFilenamePath)) {
            assetManager.unload(assetFilenamePath);
        }
    }

    public TiledMap getMapAsset(String mapFilenamePath) {
        if (!assetManager.isLoaded(mapFilenamePath)) {
            loadMapAsset(mapFilenamePath);
        }
        return assetManager.get(mapFilenamePath, TiledMap.class);
    }

    private void loadMapAsset(String mapFilenamePath) {
        assetManager.setLoader(TiledMap.class, new TmxMapLoader(assetManager.getFileHandleResolver()));
        assetManager.load(mapFilenamePath, TiledMap.class);
        assetManager.finishLoadingAsset(mapFilenamePath);
    }

    public BitmapFont getTrueTypeAsset(String trueTypeFilenamePath, int fontSize) {
        if (!assetManager.isLoaded(trueTypeFilenamePath)) {
            loadTrueTypeAsset(trueTypeFilenamePath, fontSize);
        }
        return assetManager.get(trueTypeFilenamePath, BitmapFont.class);
    }

    private void loadTrueTypeAsset(String trueTypeFilenamePath, int fontSize) {
        assetManager.setLoader(FreeTypeFontGenerator.class, new FreeTypeFontGeneratorLoader(assetManager.getFileHandleResolver()));
        assetManager.setLoader(BitmapFont.class, ".ttf", new FreetypeFontLoader(assetManager.getFileHandleResolver()));
        var parameter = new FreetypeFontLoader.FreeTypeFontLoaderParameter();
        parameter.fontFileName = trueTypeFilenamePath;
        parameter.fontParameters.size = fontSize;
        assetManager.load(trueTypeFilenamePath, BitmapFont.class, parameter);
        assetManager.finishLoadingAsset(trueTypeFilenamePath);
    }

    public Texture getTextureAsset(String textureFilenamePath) {
        if (!assetManager.isLoaded(textureFilenamePath)) {
            loadTextureAsset(textureFilenamePath);
        }
        return assetManager.get(textureFilenamePath, Texture.class);
    }

    private void loadTextureAsset(String textureFilenamePath) {
        assetManager.setLoader(Texture.class, new TextureLoader(assetManager.getFileHandleResolver()));
        assetManager.load(textureFilenamePath, Texture.class);
        assetManager.finishLoadingAsset(textureFilenamePath);
    }

    public SpriteConfig getSpriteConfig(String spriteId) {
        if (spriteConfigs.size == 0) {
            loadSpriteConfigs();
        }
        return spriteConfigs.get(spriteId);
    }

    @SuppressWarnings("unchecked")
    private void loadSpriteConfigs() {
        Json json = new Json();
        FileHandle[] configFiles = Gdx.files.local("configs/sprites").list(".json");
        for (FileHandle file : configFiles) {
            ObjectMap<String, SpriteConfig> configs = json.fromJson(ObjectMap.class, SpriteConfig.class, file);
            spriteConfigs.putAll(configs);
        }
    }

}
