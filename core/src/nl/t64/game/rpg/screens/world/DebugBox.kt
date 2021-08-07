package nl.t64.game.rpg.screens.world

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.Pixmap
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.scenes.scene2d.utils.Drawable
import com.badlogic.gdx.utils.Align
import nl.t64.game.rpg.Engine
import nl.t64.game.rpg.Utils.gameData
import nl.t64.game.rpg.constants.Constant
import nl.t64.game.rpg.screens.world.entity.Entity


private val TRANSPARENT = Color(0f, 0f, 0f, 0.5f)
private const val TABLE_WIDTH = 200f
private const val SECOND_COLUMN_WIDTH = 100f

internal class DebugBox(private val player: Entity) {

    private val table: Table = createTable()
    private val stage: Stage = Stage().apply { addActor(table) }

    fun dispose() {
        stage.dispose()
    }

    fun update(dt: Float) {
        table.clear()

        table.add("FPS:")
        table.add(Gdx.graphics.framesPerSecond.toString())
        table.row()
        table.add("dt:")
        table.add(dt.toString())
        table.row()
        table.add("runTime:")
        val runTime = Engine.getRunTime().toString()
        table.add(runTime.substring(0, runTime.length - 4))
        table.row()
        table.add("").row()

//        table.add("timeUp:");
//        table.add(player.getInputComponent().getTimeUp());
//        table.row();
//        table.add("timeDown:");
//        table.add(player.getInputComponent().getTimeDown());
//        table.row();
//        table.add("timeLeft:");
//        table.add(player.getInputComponent().getTimeLeft());
//        table.row();
//        table.add("timeRight:");
//        table.add(player.getInputComponent().getTimeRight());
//        table.row();
//        table.add("turnDelay:");
//        table.add(String.valueOf(player.getPlayerInput().getTurnDelay()));
//        table.row();
//        table.add("turnGrace:");
//        table.add(String.valueOf(player.getPlayerInput().getTurnGrace()));
//        table.row();
//        table.add("").row();
//
//        table.add("oldPositionX:");
//        table.add(String.valueOf(player.getOldPosition().x));
//        table.row();
//        table.add("oldPositionY:");
//        table.add(String.valueOf(player.getOldPosition().y));
//        table.row();
//        table.add("").row();

        table.add("currentPositionX:")
        table.add(player.position.x.toString())
        table.row()
        table.add("currentPositionY:")
        table.add(player.position.y.toString())
        table.row()
        table.add("").row()

        table.add("currentPositionGridX:")
        table.add(player.getPositionInGrid().x.toString())
        table.row()
        table.add("currentPositionGridY:")
        table.add(player.getPositionInGrid().y.toString())
        table.row()
        table.add("").row()

        table.add("currentPositionTiledX:")
        table.add((player.position.x / Constant.TILE_SIZE).toInt().toString())
        table.row()
        table.add("currentPositionTiledY:")
        table.add((player.position.y / Constant.TILE_SIZE).toInt().toString())
        table.row()
        table.add("").row()

        table.add("direction:")
        table.add(player.direction.toString())
        table.row()
        table.add("state:")
        table.add(player.state.toString())
        table.row()
        table.add("").row()

        table.add("quest0001:")
        table.add(gameData.quests.getQuestById("quest0001").currentState.toString())
        table.add("").row()
        table.add("quest0002:")
        table.add(gameData.quests.getQuestById("quest0002").currentState.toString())
        table.add("").row()
        table.add("quest0003:")
        table.add(gameData.quests.getQuestById("quest0003").currentState.toString())
        table.add("").row()
        table.add("quest0004:")
        table.add(gameData.quests.getQuestById("quest0004").currentState.toString())
        table.add("").row()
        table.add("quest0005:")
        table.add(gameData.quests.getQuestById("quest0005").currentState.toString())
        table.add("").row()

        table.pack()
        table.setPosition(0f, Gdx.graphics.height - table.height)

        stage.act(dt)
        stage.draw()
    }

    private fun createTable(): Table {
        val debugSkin = Skin()
        debugSkin.add("default", LabelStyle(BitmapFont(), Color.WHITE))

        return Table(debugSkin).apply {
            defaults().width(TABLE_WIDTH).align(Align.left)
            columnDefaults(1).width(SECOND_COLUMN_WIDTH)
            background = createTransparency()
        }
    }

    private fun createTransparency(): Drawable {
        val pixmap = Pixmap(1, 1, Pixmap.Format.Alpha)
        pixmap.setColor(TRANSPARENT)
        pixmap.fill()
        return Image(Texture(pixmap)).drawable
    }

}
