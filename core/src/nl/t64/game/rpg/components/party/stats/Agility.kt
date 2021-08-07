package nl.t64.game.rpg.components.party.stats


// no super.variable for Agility
class Agility(rank: Int = 0) : StatItem(
    StatItemId.AGILITY, StatItemId.AGILITY.title, 30, 0.24f, rank, bonus = 0
) {
    override fun getDescription(): String {
        return "Decreases the enemy's chance-to-hit a physical attack in combat."
    }
}
