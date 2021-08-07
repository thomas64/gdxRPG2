package nl.t64.game.rpg

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.InputProcessor
import com.badlogic.gdx.controllers.Controllers
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.NinePatch
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.scenes.scene2d.ui.Window
import com.badlogic.gdx.scenes.scene2d.ui.Window.WindowStyle
import com.badlogic.gdx.scenes.scene2d.utils.Drawable
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable
import com.badlogic.gdx.utils.Align
import com.badlogic.gdx.utils.ScreenUtils
import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import nl.t64.game.rpg.audio.AudioManager
import nl.t64.game.rpg.constants.Constant
import nl.t64.game.rpg.screens.ScreenManager
import nl.t64.game.rpg.screens.world.MapManager


private const val TITLE_PADDING = 50f
private const val TITLE_FONT = "fonts/fff_tusj.ttf"
private const val TITLE_SIZE = 30
private const val SPRITE_BORDER = "sprites/border.png"
private const val SPRITE_TOP_BORDER = "sprites/top_border.png"
private const val CHAR_PATH = "sprites/characters/%s.png"
private const val FACE_PATH = "sprites/faces/%s.png"
private const val DOOR_PATH = "sprites/objects/%s.png"
private const val CHEST_PATH = "sprites/objects/chest.png"
private const val SPRITE_PARCHMENT = "sprites/parchment.png"
private const val SMALL_PARCHMENT_WIDTH = 400f
private const val SMALL_PARCHMENT_HEIGHT = 280f
private const val LIGHTMAP_PATH = "sprites/lightmaps/%s.png"

object Utils {

    @JvmStatic
    val resourceManager: ResourceManager
        get() = engine.resourceManager

    @JvmStatic
    val preferenceManager: PreferenceManager
        get() = engine.preferenceManager

    @JvmStatic
    val profileManager: ProfileManager
        get() = engine.profileManager

    @JvmStatic
    val gameData: GameData
        get() = engine.gameData

    @JvmStatic
    val screenManager: ScreenManager
        get() = engine.screenManager

    @JvmStatic
    val audioManager: AudioManager
        get() = engine.audioManager

    @JvmStatic
    val mapManager: MapManager
        get() = engine.mapManager

    @JvmStatic
    val brokerManager: BrokerManager
        get() = engine.brokerManager

    @JvmStatic
    fun setGamepadInputProcessor(inputProcessor: InputProcessor?) {
        engine.gamepadMapping.setInputProcessor(inputProcessor)
    }

    private val engine: Engine
        get() = Gdx.app.applicationListener as Engine

    @JvmStatic
    fun isGamepadConnected(): Boolean {
        return Controllers.getCurrent()?.isConnected ?: false
    }

    @JvmStatic
    fun createDefaultWindow(title: String, table: Table): Window {
        val window = Window(title, createDefaultWindowStyle())
        window.add(table)
        window.padTop(TITLE_PADDING)
        window.titleLabel.setAlignment(Align.left)
        window.isMovable = false
        window.pack()
        return window
    }

    private fun createDefaultWindowStyle(): WindowStyle {
        val texture = resourceManager.getTextureAsset(SPRITE_BORDER)
        val ninepatch = NinePatch(texture, 1, 1, 1, 1)
        val drawable = NinePatchDrawable(ninepatch)
        val font = resourceManager.getTrueTypeAsset(TITLE_FONT, TITLE_SIZE)
        return WindowStyle(font, Color.BLACK, drawable)
    }

    @JvmStatic
    fun createTopBorder(): Drawable {
        val texture = resourceManager.getTextureAsset(SPRITE_TOP_BORDER)
        val ninepatch = NinePatch(texture, 0, 0, 1, 0)
        return NinePatchDrawable(ninepatch)
    }

    fun createLightmap(lightmapId: String): Texture {
        return resourceManager.getTextureAsset(String.format(LIGHTMAP_PATH, lightmapId))
    }

    @JvmStatic
    fun getCharImage(spriteId: String): Array<Array<TextureRegion>> {
        val charConfig = resourceManager.getSpriteConfig(spriteId)
        val path = String.format(CHAR_PATH, charConfig.source)
        val row = charConfig.row - 1
        val col = charConfig.col - 1
        val splitOfEight = getSplitTexture(path, Constant.SPRITE_GROUP_WIDTH, Constant.SPRITE_GROUP_HEIGHT)
        val personSprite = splitOfEight[row][col]
        return personSprite.split(Constant.TILE_SIZE.toInt(), Constant.TILE_SIZE.toInt())
    }

    @JvmStatic
    fun getFaceImage(spriteId: String): Image {
        val faceConfig = resourceManager.getSpriteConfig(spriteId)
        val path = String.format(FACE_PATH, faceConfig.source)
        val row = faceConfig.row - 1
        val col = faceConfig.col - 1
        val splitOfEight = getSplitTexture(path, Constant.FACE_SIZE.toInt())
        val characterFace = splitOfEight[row][col]
        return Image(characterFace)
    }

    @JvmStatic
    fun getDoorImage(spriteId: String, width: Int): Array<Array<TextureRegion>> {
        val doorConfig = resourceManager.getSpriteConfig(spriteId)
        val path = String.format(DOOR_PATH, doorConfig.source)
        val row = doorConfig.row - 1
        val col = doorConfig.col - 1
        val allDoors = getSplitTexture(path, width, Constant.SPRITE_GROUP_HEIGHT * 2)
        val doorSprite = allDoors[row][col]
        return doorSprite.split(width, Constant.TILE_SIZE.toInt() * 2)
    }

    @JvmStatic
    fun getChestImage(): List<TextureRegion> {
        val splitOfEight = getSplitTexture(CHEST_PATH, Constant.SPRITE_GROUP_WIDTH, Constant.SPRITE_GROUP_HEIGHT)
        val firstRedChest = splitOfEight[0][0]
        val twelveRedChestTextures = firstRedChest.split(Constant.TILE_SIZE.toInt(), Constant.TILE_SIZE.toInt())
        return listOf(twelveRedChestTextures[0][0], twelveRedChestTextures[3][0])
    }

    @JvmStatic
    fun getSplitTexture(texturePath: String, size: Int): Array<Array<TextureRegion>> {
        return getSplitTexture(texturePath, size, size)
    }

    private fun getSplitTexture(texturePath: String, width: Int, height: Int): Array<Array<TextureRegion>> {
        val completeTexture = resourceManager.getTextureAsset(texturePath)
        return TextureRegion.split(completeTexture, width, height)
    }

    @JvmStatic
    fun getHpColor(hpStats: Map<String, Int>): Color {
        var color = Color.ROYAL
        if (hpStats["lvlVari"]!! < hpStats["lvlRank"]!!) {
            color = Color.LIME
        }
        if (hpStats["staVari"]!! < hpStats["staRank"]!!) {
            color = Color.GOLD
        }
        if (hpStats["eduVari"]!! < hpStats["eduRank"]!!) {
            color = Color.FIREBRICK
            if (hpStats["staVari"]!! > 0) {
                color = Color.ORANGE
            }
        }
        return color
    }

    @JvmStatic
    fun createScreenshot(withBlur: Boolean): Image {
        val screenshot = Image(ScreenUtils.getFrameBufferTexture())
        if (withBlur) {
            screenshot.color = Color.DARK_GRAY
        }
        return screenshot
    }

    @JvmStatic
    fun createLargeParchment(): Image {
        val parchment = Image(resourceManager.getTextureAsset(SPRITE_PARCHMENT))
        parchment.setSize(Gdx.graphics.width.toFloat(), Gdx.graphics.height.toFloat())
        return parchment
    }

    @JvmStatic
    fun createSmallParchment(): Image {
        val parchment = Image(resourceManager.getTextureAsset(SPRITE_PARCHMENT))
        parchment.setSize(SMALL_PARCHMENT_WIDTH, SMALL_PARCHMENT_HEIGHT)
        parchment.setPosition(
            Gdx.graphics.width / 2f - SMALL_PARCHMENT_WIDTH / 2f,
            Gdx.graphics.height / 2f - SMALL_PARCHMENT_HEIGHT / 2f
        )
        return parchment
    }

    @JvmStatic
    fun <T> readValue(json: String, clazz: Class<T>): HashMap<String, T> {
        val mapper = jacksonObjectMapper()
        val valueType = mapper.typeFactory.constructMapType(HashMap::class.java, String::class.java, clazz)
        return mapper.readValue(json, valueType)
    }

    inline fun <reified T> readValue(json: String): HashMap<String, T> {
        val mapper = jacksonObjectMapper()
        val valueType = mapper.typeFactory.constructMapType(HashMap::class.java, String::class.java, T::class.java)
        return mapper.readValue(json, valueType)
    }

    @JvmStatic
    fun <T> readListValue(json: String, clazz: Class<T>): Map<String, List<T>> {
        val mapper = jacksonObjectMapper()
        val valueType: TypeReference<Map<String, List<T>>> = object : TypeReference<Map<String, List<T>>>() {}
        return mapper.readValue(json, valueType)
    }

}
