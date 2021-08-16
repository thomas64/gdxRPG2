package nl.t64.game.rpg.components.battle

import com.badlogic.gdx.Gdx
import nl.t64.game.rpg.Utils


private const val BATTLES_CONFIGS = "configs/battles/"
private const val FILE_LIST = BATTLES_CONFIGS + "_files.txt"

class BattleContainer {

    private val battles: Map<String, Battle> = Gdx.files.internal(FILE_LIST).readString()
        .split(System.lineSeparator())
        .filter { it.isNotBlank() }
        .map { Gdx.files.internal(BATTLES_CONFIGS + it).readString() }
        .map { Utils.readValue<Battle>(it) }
        .flatMap { it.toList() }
        .toMap()

    fun getBattlers(battleId: String): List<Battler> {
        return battles[battleId]!!.battlers
    }

    fun isBattleWon(battleId: String): Boolean {
        return battles[battleId]!!.hasWon
    }

    fun setBattleWon(battleId: String) {
        battles[battleId]!!.hasWon = true
    }

}
