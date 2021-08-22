package nl.t64.game.rpg.components.party.stats


// no super.bonus for Endurance
class Endurance(rank: Int = 0) : StatItem(
    StatItemId.ENDURANCE, StatItemId.ENDURANCE.title, 40, 0.12f, rank, variable = rank
) {
    override fun getDescription(): String {
        return "Tekst en uitleg over $name."
    }

    override fun doUpgrade() {
        rank += 1
        variable += 1
    }

    // todo, speciale bonus toepassen inventoryItem: epic_ring_of_healing, die 1 edu geeft om je leven 1malig te redden.
    fun takeDamage(damage: Int) {
        variable -= damage
        if (variable < 0) {
            variable = 0
        }
    }

    fun restore() {
        variable = rank
    }

}
