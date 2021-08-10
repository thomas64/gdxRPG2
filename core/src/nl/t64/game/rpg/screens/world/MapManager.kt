package nl.t64.game.rpg.screens.world

import com.badlogic.gdx.ai.pfa.DefaultGraphPath
import com.badlogic.gdx.graphics.g2d.Sprite
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.maps.tiled.TiledMap
import com.badlogic.gdx.math.Vector2
import ktx.tiled.property
import nl.t64.game.rpg.ProfileManager
import nl.t64.game.rpg.Utils.audioManager
import nl.t64.game.rpg.Utils.brokerManager
import nl.t64.game.rpg.Utils.resourceManager
import nl.t64.game.rpg.audio.AudioCommand
import nl.t64.game.rpg.audio.AudioEvent
import nl.t64.game.rpg.audio.toAudioEvent
import nl.t64.game.rpg.constants.Constant
import nl.t64.game.rpg.screens.world.entity.Direction
import nl.t64.game.rpg.screens.world.mapobjects.*
import nl.t64.game.rpg.screens.world.pathfinding.TiledNode
import nl.t64.game.rpg.subjects.ProfileObserver
import kotlin.math.min


private const val MAP_PATH = "maps/"
private const val MAPFILE_SUFFIX = ".tmx"
private const val BGM_PROPERTY = "bgm"
private const val BGS_PROPERTY = "bgs"
private const val DEFAULT_BG = "NONE"

class MapManager : ProfileObserver {

    lateinit var currentMap: GameMap
    private var isMapLoaded: Boolean = false
    private var nextMapTitle: String? = null
    private lateinit var fogOfWar: FogOfWar
    private var timer = 0f

    override fun onNotifyCreateProfile(profileManager: ProfileManager) {
        fogOfWar = FogOfWar()
        loadMap(Constant.STARTING_MAP)
        currentMap.setPlayerSpawnLocationForNewLoad(Constant.STARTING_MAP)
        onNotifySaveProfile(profileManager)
        brokerManager.mapObservers.notifyMapChanged(currentMap)
    }

    override fun onNotifySaveProfile(profileManager: ProfileManager) {
        profileManager.setProperty("mapTitle", currentMap.mapTitle)
        profileManager.setProperty("fogOfWar", fogOfWar)
    }

    override fun onNotifyLoadProfile(profileManager: ProfileManager) {
        val mapTitle = profileManager.getProperty<String>("mapTitle")
        fogOfWar = profileManager.getProperty("fogOfWar")
        loadMap(mapTitle)
        currentMap.setPlayerSpawnLocationForNewLoad(mapTitle)
        brokerManager.mapObservers.notifyMapChanged(currentMap)
    }

    fun loadMapAfterCutscene(mapTitle: String, cutsceneId: String) {
        loadMap(mapTitle)
        currentMap.setPlayerSpawnLocationForNewLoad(cutsceneId)
        brokerManager.mapObservers.notifyMapChanged(currentMap)
    }

    fun getParallaxBackground(camera: Camera): Sprite {
        val cameraWidth = camera.zoomedCameraWidth
        val cameraHeight = camera.zoomedCameraHeight
        val halfWidth = cameraWidth / 2f
        val halfHeight = cameraHeight / 2f

        return Sprite(currentMap.parallaxBackground).apply {
            setSize(cameraWidth, cameraHeight)
            setPosition(-halfWidth, -halfHeight)
        }
    }

    fun getLightmapCamera(camera: Camera): List<Sprite> {
        val cameraWidth = camera.viewportWidth
        val cameraHeight = camera.viewportHeight
        val mapWidth = currentMap.pixelWidth / camera.zoom
        val mapHeight = currentMap.pixelHeight / camera.zoom
        val minWidth = min(cameraWidth, mapWidth)
        val minHeight = min(cameraHeight, mapHeight)
        val halfWidth = minWidth * camera.zoom
        val halfHeight = minHeight * camera.zoom
        val quarterWidth = minWidth * (camera.zoom / 2f)
        val quarterHeight = minHeight * (camera.zoom / 2f)

        return currentMap.lightmapCamera
            .map { Sprite(it) }
            .onEach { it.setSize(halfWidth, halfHeight) }
            .onEach { it.setPosition(-quarterWidth, -quarterHeight) }
    }

    fun getTiledMap(): TiledMap = currentMap.tiledMap
    fun getLightmapMap(): Sprite = currentMap.lightmapMap
    fun getGameMapLights(): List<GameMapLight> = currentMap.lights
    fun getLightmapPlayer(): Sprite? = currentMap.lightmapPlayer
    fun getLowerMapTextures(): List<GameMapQuestTexture> = currentMap.lowerTextures
    fun getUpperMapTextures(): List<GameMapQuestTexture> = currentMap.upperTextures

    fun findPath(startPoint: Vector2, endPoint: Vector2): DefaultGraphPath<TiledNode> {
        return currentMap.tiledGraph.findPath(startPoint, endPoint)
    }

    fun updateFogOfWar(playerPosition: Vector2, dt: Float) {
        timer += dt
        if (timer > 1f) {
            timer -= 1f
            fogOfWar.update(playerPosition, currentMap.mapTitle)
        }
    }

    fun drawFogOfWar(shapeRenderer: ShapeRenderer) {
        fogOfWar.draw(shapeRenderer, currentMap.mapTitle)
    }

    fun updateQuestLayers() {
        currentMap.questBlockers.forEach { it.update() }
        currentMap.upperTextures.forEach { it.update() }
        currentMap.lowerTextures.forEach { it.update() }
    }

    fun getGroundSound(x: Float, y: Float): AudioEvent {
        return getGroundSound(Vector2(x, y))
    }

    fun getGroundSound(playerFeetPosition: Vector2): AudioEvent {
        return currentMap.getUnderground(playerFeetPosition).toAudioEvent()
    }

    fun prepareForBattle() {
        nextMapTitle = null
    }

    fun checkWarpPoint(warpPoint: GameMapWarpPoint, playerDirection: Direction) {
        audioManager.handle(AudioCommand.SE_PLAY_ONCE, AudioEvent.SE_WARP)
        nextMapTitle = warpPoint.toMapName
        brokerManager.mapObservers.notifyFadeOut(
            { changeMapWithCameraShake(warpPoint, playerDirection) }, warpPoint.fadeColor
        )
    }

    fun collisionPortal(portal: GameMapPortal, playerDirection: Direction) {
        nextMapTitle = portal.toMapName
        brokerManager.mapObservers.notifyFadeOut(
            { changeMap(portal, playerDirection) }, portal.fadeColor
        )
    }

    private fun changeMapWithCameraShake(warpPoint: GameMapRelocator, direction: Direction) {
        changeMap(warpPoint, direction)
        brokerManager.mapObservers.notifyShakeCamera()
    }

    private fun changeMap(portal: GameMapRelocator, direction: Direction) {
        portal.enterDirection = direction
        loadMap(portal.toMapName)
        currentMap.setPlayerSpawnLocation(portal)
        brokerManager.mapObservers.notifyMapChanged(currentMap)
    }

    fun setTiledGraph() {
        currentMap.setTiledGraph()
    }

    fun continueAudio() {
        if (isMapLoaded) {
            audioManager.handle(AudioCommand.BGM_PLAY_LOOP, currentMap.bgm)
            audioManager.handle(AudioCommand.BGS_PLAY_LOOP, currentMap.bgs)
        }
    }

    fun fadeAudio() {
        audioManager.possibleBgmFade(currentMap.bgm, getBgmOfMap(getTiledMap(nextMapTitle)))
        audioManager.possibleBgsFade(currentMap.bgs, getBgsOfMap(getTiledMap(nextMapTitle)))
    }

    fun loadMap(mapTitle: String) {
        val prevBgm = if (isMapLoaded) currentMap.bgm else AudioEvent.NONE
        val prevBgs = if (isMapLoaded) currentMap.bgs else listOf(AudioEvent.NONE)
        disposeOldMaps()
        currentMap = GameMap(mapTitle)
        isMapLoaded = true
        fogOfWar.putIfAbsent(currentMap)
        val nextBgm = currentMap.bgm
        val nextBgs = currentMap.bgs
        audioManager.possibleBgmSwitch(prevBgm, nextBgm)
        audioManager.possibleBgsSwitch(prevBgs, nextBgs)
    }

    fun disposeOldMaps() {
        brokerManager.actionObservers.removeAllObservers()
        brokerManager.blockObservers.removeAllObservers()
        brokerManager.bumpObservers.removeAllObservers()
        brokerManager.detectionObservers.removeAllObservers()
        brokerManager.collisionObservers.removeAllObservers()
        if (isMapLoaded) {
            currentMap.dispose()
            isMapLoaded = false
        }
    }

    companion object {

        fun getBgmOfMap(tiledMap: TiledMap): AudioEvent {
            val audioEventString = tiledMap.property(BGM_PROPERTY, DEFAULT_BG)
            return AudioEvent.valueOf(audioEventString.uppercase())
        }

        fun getBgsOfMap(tiledMap: TiledMap): List<AudioEvent> {
            val audioEventStrings = tiledMap.property(BGS_PROPERTY, DEFAULT_BG)
            return audioEventStrings.uppercase().split(",").map { it.trim() }
                .map { AudioEvent.valueOf(it) }
        }

        fun getTiledMap(mapTitle: String?): TiledMap {
            return mapTitle?.let {
                resourceManager.getMapAsset("$MAP_PATH$it$MAPFILE_SUFFIX")
            } ?: TiledMap()
        }
    }

}
