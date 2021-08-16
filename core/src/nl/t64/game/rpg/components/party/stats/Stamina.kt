package nl.t64.game.rpg.components.party.stats

import kotlin.math.abs


// no super.bonus for Stamina
class Stamina(rank: Int = 0) : StatItem(
    StatItemId.STAMINA, StatItemId.STAMINA.title, 90, 0.04f, rank, variable = rank
) {
    override fun getDescription(): String {
        return "Tekst en uitleg over $name."
    }

    fun takeDamage(damage: Int): Int? {
        variable -= damage
        if (variable < 0) {
            val remainingDamage = abs(variable)
            variable = 0
            return remainingDamage
        }
        return null
    }

    fun getInflictDamagePenalty(): Int {
        return if (variable <= 0) 5 else 1
    }
}
