package nl.t64.game.rpg.components.party.stats


class Endurance(rank: Int = 0) : StatItem(
    StatItemId.ENDURANCE, StatItemId.ENDURANCE.title, 40, 0.12f, rank, variable = rank, bonus = 0
) {
    override fun getDescription(): String {
        return "Tekst en uitleg over $name."
    }
}
