package nl.t64.game.rpg.screens.battle

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Screen
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.actions.Actions
import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.utils.ScreenUtils
import nl.t64.game.rpg.Utils
import nl.t64.game.rpg.Utils.audioManager
import nl.t64.game.rpg.Utils.brokerManager
import nl.t64.game.rpg.Utils.gameData
import nl.t64.game.rpg.Utils.mapManager
import nl.t64.game.rpg.Utils.profileManager
import nl.t64.game.rpg.Utils.screenManager
import nl.t64.game.rpg.audio.AudioCommand
import nl.t64.game.rpg.audio.AudioEvent
import nl.t64.game.rpg.components.battle.EnemyContainer
import nl.t64.game.rpg.constants.Constant
import nl.t64.game.rpg.constants.ScreenType
import nl.t64.game.rpg.screens.inventory.messagedialog.MessageDialog
import nl.t64.game.rpg.screens.menu.DialogQuestion
import nl.t64.game.rpg.screens.world.Camera


class BattleScreen : Screen {

    private lateinit var battleId: String
    private lateinit var enemies: EnemyContainer
    private lateinit var stage: Stage
    private lateinit var listener: BattleScreenListener

    companion object {
        fun load(battleId: String) {
            val screen = screenManager.getScreen(ScreenType.BATTLE) as BattleScreen
            screen.battleId = battleId
            screen.enemies = EnemyContainer(battleId)
            screenManager.setScreen(ScreenType.BATTLE)
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
        val enemyTable = BattleScreenBuilder.createEnemyTable(enemies.getAll())
        stage.addActor(enemyTable)
        listener = BattleScreenListener({ winBattle() }, { fleeBattle() }, { killHero(it) })

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
            Actions.addListener(listener, false)
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
        stage.clear()
    }

    override fun dispose() {
        stage.dispose()
    }

    private fun winBattle() {
        gameData.battles.setBattleWon(battleId)

        val levelUpMessage = StringBuilder()
        val totalXpWon = enemies.getTotalXp()
        gameData.party.gainXp(totalXpWon, levelUpMessage)
        val finalLevelUpMessage = levelUpMessage.toString().trim().ifEmpty { null }

        val xpMessage = """
            The enemy is defeated!
            Party gained $totalXpWon XP.""".trimIndent()
        val messageDialog = MessageDialog(xpMessage)
        messageDialog.setActionAfterHide { battleWonExitScreen(finalLevelUpMessage) }
        messageDialog.show(stage, AudioEvent.SE_CONVERSATION_NEXT)
    }

    private fun battleWonExitScreen(levelUpMessage: String?) {
        exitScreen {
            screenManager.setScreen(ScreenType.WORLD)
            brokerManager.battleObservers.notifyBattleWon(battleId, enemies.getSpoils(), levelUpMessage)
        }
    }

    private fun fleeBattle() {
        val message = """
            Fleeing will return you to the location of 
            your last save with all the progress intact.
            
            Do you want to flee?""".trimIndent()
        DialogQuestion({ battleFledExitScreen() }, message)
            .show(stage, AudioEvent.SE_CONVERSATION_NEXT, 0)
    }

    private fun battleFledExitScreen() {
        exitScreen {
            val mapTitle = profileManager.getLastSaveLocation()
            mapManager.loadMapAfterFleeing(mapTitle)
            screenManager.setScreen(ScreenType.WORLD)
        }
    }

    private fun killHero(index: Int) {
        if (index == 1) {
            gameOver()
        } else {
            killPartyMember(index)
        }
    }

    private fun gameOver() {
        val message = """
            Mozes took a fatal blow.
            
                       Game Over.""".trimIndent()
        val messageDialog = MessageDialog(message)
        messageDialog.setActionAfterHide { gameOverExitScreen() }
        messageDialog.show(stage, AudioEvent.SE_CONVERSATION_NEXT)
    }

    private fun gameOverExitScreen() {
        exitScreen {
            screenManager.setScreen(ScreenType.SCENE_DEATH)
        }
    }

    private fun killPartyMember(index: Int) {
        if (gameData.party.contains(index - 1)) {
            audioManager.handle(AudioCommand.SE_PLAY_ONCE, AudioEvent.SE_CONVERSATION_NEXT)
            gameData.party.getHero(index - 1).takeDamage(1000)
            val heroTable = stage.actors[1] as Table
            val heroFace = heroTable.cells[(index * 3) - 2].actor as Image
            heroFace.color = Color.DARK_GRAY
        }
    }

    private fun exitScreen(actionAfterExit: () -> Unit) {
        stage.addAction(Actions.sequence(
            Actions.run {
                Gdx.input.inputProcessor = null
                Utils.setGamepadInputProcessor(null)
            },
            Actions.fadeOut(Constant.FADE_DURATION),
            Actions.run { actionAfterExit.invoke() }
        ))
    }

}
