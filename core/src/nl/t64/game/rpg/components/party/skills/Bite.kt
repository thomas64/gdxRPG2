package nl.t64.game.rpg.components.party.skills


class Bite(rank: Int = 0) : SkillItem(
    SkillItemId.BITE, SkillItemId.BITE.title, 0f, rank
) {
    override fun getDescription(): String = ""
}
