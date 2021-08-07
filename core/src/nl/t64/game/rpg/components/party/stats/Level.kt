package nl.t64.game.rpg.components.party.stats

import kotlin.math.roundToInt


private const val MAXIMUM = 40

class Level(var rank: Int = 1) {

    var variable: Int = rank
    var totalXp: Int = getTotalXpForLevel(rank.toFloat())
    var xpToInvest: Int = 0

    fun getXpDeltaBetweenLevels(): Int {
        val nextLevel = rank + 1
        return getTotalXpForLevel(nextLevel.toFloat()) - getTotalXpForLevel(rank.toFloat())
    }

    fun getXpNeededForNextLevel(): Int {
        return when {
            rank >= MAXIMUM -> 1
            else -> {
                val nextLevel = rank + 1
                getTotalXpForLevel(nextLevel.toFloat()) - totalXp
            }
        }
    }

    fun gainXp(amount: Int, hasGainedLevel: (Boolean) -> Unit) {
        if (rank < MAXIMUM) {
            xpToInvest += amount
            totalXp += amount
        }
        while (getXpNeededForNextLevel() <= 0) {
            rank += 1
            variable += 1
            hasGainedLevel.invoke(true)
        }
    }

    private fun getTotalXpForLevel(level: Float): Int {
        return ((5f / 6f) * ((2f * level) + 1f) * ((level * level) + level)).roundToInt()
    }

}
