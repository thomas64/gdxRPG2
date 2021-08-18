package nl.t64.game.rpg.screens.cutscene

import com.badlogic.gdx.scenes.scene2d.Action
import com.badlogic.gdx.scenes.scene2d.actions.Actions
import nl.t64.game.rpg.Utils.audioManager
import nl.t64.game.rpg.Utils.screenManager
import nl.t64.game.rpg.audio.AudioCommand
import nl.t64.game.rpg.audio.AudioEvent
import nl.t64.game.rpg.constants.Constant
import nl.t64.game.rpg.constants.ScreenType
import nl.t64.game.rpg.screens.world.entity.Direction
import nl.t64.game.rpg.screens.world.entity.EntityState
import kotlin.random.Random


class SceneDeath : CutsceneScreen() {

    private lateinit var mozes: CutsceneActor
    private lateinit var flames: List<CutsceneActor>

    override fun prepare() {
        mozes = CutsceneActor.createCharacter("mozes")
        flames = List(1200) { CutsceneActor.createFlame() }
        actorsStage.addActor(mozes)
        flames.forEach { actorsStage.addActor(it) }
        actions = listOf(killItWithFire())
    }

    private fun killItWithFire(): Action {
        return Actions.sequence(
            Actions.run {
                setMapWithBgsOnly("death_scene")
                setCameraPosition(0f, 0f)
                title.setText("Is this the end of Adan...?")
                title.setPosition(camera.position.x - (title.width / 2f), camera.position.y)
                mozes.setPosition(580f, 300f)
                mozes.direction = Direction.NORTH
                flames.forEachIndexed { i, it ->
                    it.setPosition(i % 12 * 100f - 100f,
                                   Random.nextInt(800, 3000).toFloat())
                }
                mozes.isVisible = true
            },
            Actions.delay(1f),
            Actions.run { audioManager.handle(AudioCommand.BGS_PLAY_LOOP, AudioEvent.BGS_QUAKE) },

            actionFadeIn(),

            Actions.run {
                flames.forEach {
                    val scale = Random.nextInt(2, 5).toFloat()
                    it.addAction(Actions.sequence(
                        Actions.scaleBy(scale, scale),
                        Actions.delay(3f),
                        Actions.moveBy(0f, -4000f, 10f)
                    ))
                }
            },
            Actions.addAction(Actions.sequence(
                Actions.delay(3f),
                Actions.run { mozes.direction = Direction.SOUTH },
                Actions.run { mozes.entityState = EntityState.RUNNING },
                Actions.moveBy(0f, -1000f, 10f),
                Actions.delay(0.5f),
                Actions.visible(false)
            ), mozes),
            Actions.delay(4.5f),
            Actions.run { audioManager.handle(AudioCommand.SE_PLAY_ONCE, AudioEvent.SE_DEATH_SCREAM) },
            Actions.delay(4f),

            actionFadeOutWithoutBgmFading(),

            Actions.delay(2f),
            Actions.addAction(Actions.sequence(
                Actions.alpha(0f),
                Actions.visible(true),
                Actions.fadeIn(Constant.FADE_DURATION),
                Actions.delay(5f),
                Actions.run { isBgmFading = true },
                Actions.fadeOut(Constant.FADE_DURATION),
                Actions.visible(false),
                Actions.alpha(1f)
            ), title),
            Actions.delay(8f),
            Actions.run { isBgmFading = false },
            Actions.run { exitScreen() }
        )
    }

    override fun exitScreen() {
        endCutsceneAnd { screenManager.setScreen(ScreenType.MENU_MAIN) }
    }

}
