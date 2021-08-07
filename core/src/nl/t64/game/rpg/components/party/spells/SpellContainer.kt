package nl.t64.game.rpg.components.party.spells

import com.fasterxml.jackson.annotation.JsonCreator


class SpellContainer() {

    private val spells: MutableMap<String, SpellItem> = HashMap()

    @JsonCreator
    constructor(startingSpells: Map<String, Int>) : this() {
        startingSpells.entries
            .map { SpellDatabase.createSpellItem(it.key, it.value) }
            .forEach { this.spells[it.id] = it }
    }

    fun getAll(): List<SpellItem> {
        return spells.values.sortedBy { it.sort }
    }

}
