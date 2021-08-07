package nl.t64.game.rpg.screens.inventory

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle
import nl.t64.game.rpg.Utils


open class ButtonLabels(private val stage: Stage) {

    fun create() {
        val labelStyle = LabelStyle(BitmapFont(), Color.BLACK)

        val buttonTopLeftLabel = Label(createTopLeftText(), labelStyle)
        buttonTopLeftLabel.setPosition(35f, 996f)
        stage.addActor(buttonTopLeftLabel)

        val buttonTopRightLabel = Label(createTopRightText(), labelStyle)
        buttonTopRightLabel.setPosition(Gdx.graphics.width - 52f, 996f)
        stage.addActor(buttonTopRightLabel)

        val buttonMiddleLeftLabel = Label(createMiddleLeftText(), labelStyle)
        buttonMiddleLeftLabel.setPosition(35f, 772f)
        stage.addActor(buttonMiddleLeftLabel)

        val buttonMiddleRightLabel = Label(createMiddleRightText(), labelStyle)
        buttonMiddleRightLabel.setPosition(Gdx.graphics.width - 52f, 772f)
        stage.addActor(buttonMiddleRightLabel)

        val buttonBottomLeftLabel = Label(createBottomLeftText(), labelStyle)
        buttonBottomLeftLabel.setPosition(300f, 26f)
        stage.addActor(buttonBottomLeftLabel)

        val buttonBottomRightLabel = Label(createBottomRightText(), labelStyle)
        buttonBottomRightLabel.setPosition(890f, 26f)
        stage.addActor(buttonBottomRightLabel)
    }

    private fun createTopLeftText(): String {
        return if (Utils.isGamepadConnected()) {
            "[L1]"
        } else {
            "[Q]"
        }
    }

    private fun createTopRightText(): String {
        return if (Utils.isGamepadConnected()) {
            "[R1]"
        } else {
            "[W]"
        }
    }

    private fun createMiddleLeftText(): String {
        return if (Utils.isGamepadConnected()) {
            "[R3]"
        } else {
            "[Z]"
        }
    }

    private fun createMiddleRightText(): String {
        return if (Utils.isGamepadConnected()) {
            "[R3]"
        } else {
            "[X]"
        }
    }

    open fun createBottomLeftText(): String? {
        return if (Utils.isGamepadConnected()) {
            "[A] De/Equip      [Y] Dismiss hero      [Start] Sort inventory"
        } else {
            "[A] De/Equip      [D] Dismiss hero      [Space] Sort inventory"
        }
    }

    open fun createBottomRightText(): String? {
        return if (Utils.isGamepadConnected()) {
            "[Select] Toggle tooltip      [L3] Toggle compare      [B] Back"
        } else {
            "[T] Toggle tooltip      [C] Toggle compare      [ I ] Back"
        }
    }

}
