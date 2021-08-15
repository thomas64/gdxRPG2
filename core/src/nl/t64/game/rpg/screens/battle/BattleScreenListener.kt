package nl.t64.game.rpg.screens.battle

import com.badlogic.gdx.Input
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.InputListener


internal class BattleScreenListener(
    private val winBattle: () -> Unit,
    private val fleeBattle: () -> Unit,
    private val killPartyMember: (Int) -> Unit
) : InputListener() {

    override fun keyDown(event: InputEvent, keycode: Int): Boolean {
        when (keycode) {
            Input.Keys.W -> winBattle.invoke()
            Input.Keys.F -> fleeBattle.invoke()
            Input.Keys.NUM_1 -> killPartyMember.invoke(1)
            Input.Keys.NUM_2 -> killPartyMember.invoke(2)
            Input.Keys.NUM_3 -> killPartyMember.invoke(3)
            Input.Keys.NUM_4 -> killPartyMember.invoke(4)
            Input.Keys.NUM_5 -> killPartyMember.invoke(5)
            Input.Keys.NUM_6 -> killPartyMember.invoke(6)
        }
        return true
    }

}
