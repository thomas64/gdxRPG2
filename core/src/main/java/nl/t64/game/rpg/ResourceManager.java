package nl.t64.game.rpg;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.TextureLoader;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGeneratorLoader;
import com.badlogic.gdx.graphics.g2d.freetype.FreetypeFontLoader;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.ObjectMap;
import nl.t64.game.rpg.conversation.ConversationGraph;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class ResourceManager {

    private static final String SPRITE_CONFIGS = "configs/sprites";
    private static final String CONVERSATION_CONFIGS = "configs/conversations/";
    private static final String SHOP_CONFIGS = "configs/shops/";
    private static final String CONFIG_SUFFIX = ".json";
    private static final String ATLAS_FILES = "sprites";
    private static final String ATLAS_FILES2 = "sprites/inventory";
    private static final String ATLAS_SUFFIX = ".atlas";

    private final AssetManager assetManager;
    private final ObjectMap<String, SpriteConfig> spriteConfigs;
    private final List<TextureAtlas> atlasList;
    private final Json json;

    public ResourceManager() {
        this.assetManager = new AssetManager();
        this.spriteConfigs = new ObjectMap<>();
        this.atlasList = new ArrayList<>();
        this.json = new Json();
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
        FileHandle[] configFiles = Gdx.files.local(SPRITE_CONFIGS).list(CONFIG_SUFFIX);
        for (FileHandle file : configFiles) {
            ObjectMap<String, SpriteConfig> configs = json.fromJson(ObjectMap.class, SpriteConfig.class, file);
            spriteConfigs.putAll(configs);
        }
    }

    public TextureRegion getAtlasTexture(String atlasId) {
        if (atlasList.isEmpty()) {
            loadAtlasTexture(ATLAS_FILES);
            loadAtlasTexture(ATLAS_FILES2);
        }
        TextureRegion textureRegion = null;
        for (TextureAtlas atlas : atlasList) {
            textureRegion = atlas.findRegion(atlasId);
            if (textureRegion != null) {
                break;
            }
        }
        return textureRegion;
    }

    private void loadAtlasTexture(String path) {
        FileHandle[] atlasFiles = Gdx.files.local(path).list(ATLAS_SUFFIX);
        Arrays.stream(atlasFiles)
              .map(TextureAtlas::new)
              .forEach(atlasList::add);
    }

    public ConversationGraph getConversation(String conversationId) {
        String fullFilenamePath = CONVERSATION_CONFIGS + conversationId + CONFIG_SUFFIX;
        return json.fromJson(ConversationGraph.class, Gdx.files.local(fullFilenamePath));
    }

    @SuppressWarnings("unchecked")
    public List<String> getShopInventory(String shopId) {
        String fullFilenamePath = SHOP_CONFIGS + shopId + CONFIG_SUFFIX;
        return json.fromJson(List.class, String.class, Gdx.files.local(fullFilenamePath));
    }

}
