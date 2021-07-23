package nl.t64.game.rpg

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.assets.loaders.MusicLoader
import com.badlogic.gdx.assets.loaders.SoundLoader
import com.badlogic.gdx.assets.loaders.TextureLoader
import com.badlogic.gdx.audio.Music
import com.badlogic.gdx.audio.Sound
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGeneratorLoader
import com.badlogic.gdx.graphics.g2d.freetype.FreetypeFontLoader
import com.badlogic.gdx.graphics.g2d.freetype.FreetypeFontLoader.FreeTypeFontLoaderParameter
import com.badlogic.gdx.maps.tiled.TiledMap
import com.badlogic.gdx.maps.tiled.TmxMapLoader
import com.badlogic.gdx.utils.Json
import com.badlogic.gdx.utils.ObjectMap


private const val FILE_LIST = "_files.txt"
private const val SPRITE_CONFIGS = "configs/sprites/"
private const val FILE_LIST_SPRITE_CONFIGS = SPRITE_CONFIGS + FILE_LIST
private const val SHOP_CONFIGS = "configs/shops/"
private const val CONFIG_SUFFIX = ".json"
private const val ATLAS_FILES = "sprites/"
private const val FILE_LIST_ATLAS_FILES = ATLAS_FILES + FILE_LIST
private const val ATLAS_FILES2 = "sprites/inventory/"
private const val FILE_LIST_ATLAS_FILES2 = ATLAS_FILES2 + FILE_LIST
private const val ATLAS_FILES3 = "sprites/spells/"
private const val FILE_LIST_ATLAS_FILES3 = ATLAS_FILES3 + FILE_LIST

class ResourceManager {

    private val assetManager: AssetManager = AssetManager()
    private val spriteConfigs: ObjectMap<String, SpriteConfig> = ObjectMap()
    private val atlasList: MutableList<TextureAtlas> = ArrayList()
    private val json: Json = Json()

    fun unloadAsset(assetFilenamePath: String) {
        if (assetManager.isLoaded(assetFilenamePath)) {
            assetManager.unload(assetFilenamePath)
        }
    }

    fun getMapAsset(mapFilenamePath: String): TiledMap {
        if (!assetManager.isLoaded(mapFilenamePath)) {
            loadMapAsset(mapFilenamePath)
        }
        return assetManager.get(mapFilenamePath, TiledMap::class.java)
    }

    private fun loadMapAsset(mapFilenamePath: String) {
        assetManager.setLoader(TiledMap::class.java, TmxMapLoader(assetManager.fileHandleResolver))
        assetManager.load(mapFilenamePath, TiledMap::class.java)
        assetManager.finishLoadingAsset<Any>(mapFilenamePath)
    }

    fun getTrueTypeAsset(trueTypeFilenamePath: String, fontSize: Int): BitmapFont {
        if (!assetManager.isLoaded(trueTypeFilenamePath)) {
            loadTrueTypeAsset(trueTypeFilenamePath, fontSize)
        }
        return assetManager.get(trueTypeFilenamePath, BitmapFont::class.java)
    }

    private fun loadTrueTypeAsset(trueTypeFilenamePath: String, fontSize: Int) {
        assetManager.setLoader(
            FreeTypeFontGenerator::class.java,
            FreeTypeFontGeneratorLoader(assetManager.fileHandleResolver)
        )
        assetManager.setLoader(BitmapFont::class.java, ".ttf", FreetypeFontLoader(assetManager.fileHandleResolver))
        val parameter = FreeTypeFontLoaderParameter()
        parameter.fontFileName = trueTypeFilenamePath
        parameter.fontParameters.size = fontSize
        assetManager.load(trueTypeFilenamePath, BitmapFont::class.java, parameter)
        assetManager.finishLoadingAsset<Any>(trueTypeFilenamePath)
    }

    fun getTextureAsset(textureFilenamePath: String): Texture {
        if (!assetManager.isLoaded(textureFilenamePath)) {
            loadTextureAsset(textureFilenamePath)
        }
        return assetManager.get(textureFilenamePath, Texture::class.java)
    }

    private fun loadTextureAsset(textureFilenamePath: String) {
        assetManager.setLoader(Texture::class.java, TextureLoader(assetManager.fileHandleResolver))
        assetManager.load(textureFilenamePath, Texture::class.java)
        assetManager.finishLoadingAsset<Any>(textureFilenamePath)
    }

    fun getSoundAsset(soundFilenamePath: String): Sound {
        if (!assetManager.isLoaded(soundFilenamePath)) {
            loadSoundAsset(soundFilenamePath)
        }
        return assetManager.get(soundFilenamePath, Sound::class.java)
    }

    private fun loadSoundAsset(soundFilenamePath: String) {
        assetManager.setLoader(Sound::class.java, SoundLoader(assetManager.fileHandleResolver))
        assetManager.load(soundFilenamePath, Sound::class.java)
        assetManager.finishLoadingAsset<Any>(soundFilenamePath)
    }

    fun getMusicAsset(musicFilenamePath: String): Music {
        if (!assetManager.isLoaded(musicFilenamePath)) {
            loadMusicAsset(musicFilenamePath)
        }
        return assetManager.get(musicFilenamePath, Music::class.java)
    }

    private fun loadMusicAsset(musicFilenamePath: String) {
        assetManager.setLoader(Music::class.java, MusicLoader(assetManager.fileHandleResolver))
        assetManager.load(musicFilenamePath, Music::class.java)
        assetManager.finishLoadingAsset<Any>(musicFilenamePath)
    }

    fun getSpriteConfig(spriteId: String): SpriteConfig {
        if (spriteConfigs.isEmpty) {
            loadSpriteConfigs()
        }
        return spriteConfigs.get(spriteId)
    }

    private fun loadSpriteConfigs() {
        Gdx.files.internal(FILE_LIST_SPRITE_CONFIGS).readString().split(System.lineSeparator())
            .map { Gdx.files.internal(SPRITE_CONFIGS + it) }
            .map { json.fromJson(ObjectMap::class.java, SpriteConfig::class.java, it) }
            .forEach { spriteConfigs.putAll(it as ObjectMap<String, SpriteConfig>) }
    }

    fun getAtlasTexture(atlasId: String): TextureRegion {
        if (atlasList.isEmpty()) {
            loadAtlasTexture(FILE_LIST_ATLAS_FILES, ATLAS_FILES)
            loadAtlasTexture(FILE_LIST_ATLAS_FILES2, ATLAS_FILES2)
            loadAtlasTexture(FILE_LIST_ATLAS_FILES3, ATLAS_FILES3)
        }
        return atlasList.mapNotNull { it.findRegion(atlasId) }.first()
    }

    private fun loadAtlasTexture(fileName: String, directory: String) {
        Gdx.files.internal(fileName).readString().split(System.lineSeparator())
            .filter { it.isNotBlank() }
            .map { Gdx.files.internal(directory + it) }
            .map { TextureAtlas(it) }
            .forEach { atlasList.add(it) }
    }

    fun getShopInventory(shopId: String): List<String> {
        val fullFilenamePath = SHOP_CONFIGS + shopId + CONFIG_SUFFIX
        return json.fromJson(List::class.java, String::class.java, Gdx.files.internal(fullFilenamePath)) as List<String>
    }

}
