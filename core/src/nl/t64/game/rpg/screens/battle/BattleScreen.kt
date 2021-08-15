package nl.t64.game.rpg.screens.battle

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Screen
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.actions.Actions
import com.badlogic.gdx.utils.ScreenUtils
import nl.t64.game.rpg.Utils
import nl.t64.game.rpg.Utils.audioManager
import nl.t64.game.rpg.Utils.gameData
import nl.t64.game.rpg.Utils.screenManager
import nl.t64.game.rpg.audio.AudioCommand
import nl.t64.game.rpg.audio.AudioEvent
import nl.t64.game.rpg.components.battle.EnemyDatabase
import nl.t64.game.rpg.components.battle.EnemyItem
import nl.t64.game.rpg.constants.Constant
import nl.t64.game.rpg.constants.ScreenType
import nl.t64.game.rpg.screens.world.Camera


class BattleScreen : Screen {

    private lateinit var battleId: String
    private lateinit var enemies: List<EnemyItem>
    private lateinit var stage: Stage

    companion object {
        fun load(battleId: String) {
            val screen = screenManager.getScreen(ScreenType.BATTLE) as BattleScreen
            screen.battleId = battleId
            screen.enemies = createEnemies(battleId)
            screenManager.setScreen(ScreenType.BATTLE)
        }

        private fun createEnemies(battleId: String): List<EnemyItem> {
            val enemies: MutableList<EnemyItem> = ArrayList()
            gameData.battles.getBattlers(battleId).forEach {
                (0 until it.amount).forEach { _ ->
                    val enemy = EnemyDatabase.createEnemy(it.id)
                    enemies.add(enemy)
                }
            }
            return enemies
        }
    }

    override fun show() {
        val camera = Camera()
        stage = Stage(camera.viewport)
        val battleTitle = BattleScreenBuilder.createBattleTitle()
        stage.addActor(battleTitle)
        val heroTable = BattleScreenBuilder.createHeroTable()
        stage.addActor(heroTable)
        val buttonTable = BattleScreenBuilder.createButtonTable()
        stage.addActor(buttonTable)
        val enemyTable = BattleScreenBuilder.createEnemyTable(enemies)
        stage.addActor(enemyTable)

        stage.addAction(Actions.sequence(
            Actions.addAction(Actions.sequence(
                Actions.alpha(0f),
                Actions.visible(true),
                Actions.fadeIn(Constant.FADE_DURATION),
                Actions.run { audioManager.handle(AudioCommand.SE_PLAY_ONCE, AudioEvent.SE_FIGHT_ON) },
                Actions.delay(1f),
                Actions.fadeOut(Constant.FADE_DURATION),
                Actions.visible(false)
            ), battleTitle),
            Actions.delay(2.1f),
            Actions.run {
                camera.zoom = 1f
                heroTable.isVisible = true
                buttonTable.isVisible = true
                enemyTable.isVisible = true
                Gdx.input.inputProcessor = stage
                Utils.setGamepadInputProcessor(stage)
            },
            Actions.addListener(BattleScreenListener({ winBattle() },
                                                     { fleeBattle() },
                                                     { killPartyMember(it) }), false)
        ))
    }

    override fun render(dt: Float) {
        ScreenUtils.clear(Color.BLACK)
        stage.act(dt)
        stage.draw()
    }

    override fun resize(width: Int, height: Int) {
        // empty
    }

    override fun pause() {
        // empty
    }

    override fun resume() {
        // empty
    }

    override fun hide() {
        Gdx.input.inputProcessor = null
        Utils.setGamepadInputProcessor(null)
        stage.clear()
    }

    override fun dispose() {
        stage.dispose()
    }

    private fun winBattle() {
        println("winBattle")
    }

    private fun fleeBattle() {
        println("fleeBattle")
    }

    private fun killPartyMember(index: Int) {
        println("killPartyMember $index")
    }

}
