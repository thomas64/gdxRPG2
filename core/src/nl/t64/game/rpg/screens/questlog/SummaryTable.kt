package nl.t64.game.rpg.screens.questlog

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.utils.Align
import nl.t64.game.rpg.Utils
import nl.t64.game.rpg.Utils.resourceManager
import nl.t64.game.rpg.components.quest.QuestGraph
import nl.t64.game.rpg.constants.Constant


private const val TEXT_FONT = "fonts/spectral_extra_bold_20.ttf"
private const val TEXT_SIZE = 20
private const val WIDTH = -102f
private const val PAD = 20f

internal class SummaryTable {

    private val summary: Label = createLabel()
    val container: Table = createContainer()

    fun populateSummary(quest: QuestGraph) {
        summary.setText(quest.summary)
        container.cells.peek().setActor<Image>(Utils.getFaceImage(quest.entityId))
    }

    private fun createLabel(): Label {
        val font = resourceManager.getTrueTypeAsset(TEXT_FONT, TEXT_SIZE)
        val labelStyle = LabelStyle(font, Color.BLACK)
        return Label("", labelStyle).apply {
            wrap = true
        }
    }

    private fun createContainer(): Table {
        return Table().apply {
            defaults().align(Align.topLeft)
            columnDefaults(0).width(Gdx.graphics.width / 2f + WIDTH - Constant.FACE_SIZE - PAD).pad(PAD)
            columnDefaults(1).size(Constant.FACE_SIZE)
            background = Utils.createTopBorder()
            add(summary)
            add()
        }
    }

}
