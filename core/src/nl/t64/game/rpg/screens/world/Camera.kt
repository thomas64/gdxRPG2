package nl.t64.game.rpg.screens.world

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.utils.viewport.ScreenViewport
import com.badlogic.gdx.utils.viewport.Viewport
import nl.t64.game.rpg.sfx.ShakeCamera
import kotlin.math.max


class Camera : OrthographicCamera() {

    val viewport: Viewport = ScreenViewport(this)
    private val shakeCam: ShakeCamera = ShakeCamera()
    private var mapWidth = 0f
    private var mapHeight = 0f

    init {
        viewport.update(Gdx.graphics.width, Gdx.graphics.height)
        reset()
    }

    fun zoom(): Boolean {
        return if (mapWidth / zoom > Gdx.graphics.width && mapHeight / zoom > Gdx.graphics.height) {
            val zoomNumberWidth = mapWidth / Gdx.graphics.width
            val zoomNumberHeight = mapHeight / Gdx.graphics.height
            zoom = max(zoomNumberWidth, zoomNumberHeight)
            true
        } else false
    }

    fun reset() {
        zoom = 0.5f
    }

    fun startShaking() {
        shakeCam.startShaking()
    }

    fun setPosition(x: Float, y: Float) {
        setPosition(Vector2(x, y))
    }

    fun setPosition(playerPosition: Vector2) {
        val newPosition = getNewPosition(playerPosition)
        focusCameraOn(newPosition)
        update()
    }

    private fun getNewPosition(playerPosition: Vector2): Vector2 {
        return if (shakeCam.isShaking) {
            val newPlayerPosition = getPositionOnMapEdges(playerPosition)
            shakeCam.getNewShakePosition().add(newPlayerPosition)
        } else {
            getPositionOnMapEdges(playerPosition)
        }
    }

    fun setNewMapSize(mapWidth: Float, mapHeight: Float) {
        this.mapWidth = mapWidth
        this.mapHeight = mapHeight
    }

    private fun focusCameraOn(newPosition: Vector2) {
        position[newPosition] = 0f
    }

    private fun getPositionOnMapEdges(playerPosition: Vector2): Vector2 {
        val halfCameraWidth = zoomedCameraWidth / 2 - getHorizontalSpaceBetweenCameraAndMapEdge()
        val halfCameraHeight = zoomedCameraHeight / 2 - getVerticalSpaceBetweenCameraAndMapEdge()
        return Vector2(
            MathUtils.clamp(playerPosition.x, halfCameraWidth, mapWidth - halfCameraWidth),
            MathUtils.clamp(playerPosition.y, halfCameraHeight, mapHeight - halfCameraHeight)
        )
    }

    fun getHorizontalSpaceBetweenCameraAndMapEdge(): Float {
        return if (mapWidth < zoomedCameraWidth) {
            (zoomedCameraWidth - mapWidth) / 2f
        } else 0f
    }

    private fun getVerticalSpaceBetweenCameraAndMapEdge(): Float {
        return if (mapHeight < zoomedCameraHeight) {
            (zoomedCameraHeight - mapHeight) / 2f
        } else 0f
    }

    val zoomedCameraHeight: Float get() = viewportHeight * zoom
    val zoomedCameraWidth: Float get() = viewportWidth * zoom

}
