package nl.t64.game.rpg.components.party.stats

import nl.t64.game.rpg.components.party.SuperEnum


enum class StatItemId(override val title: String) : SuperEnum {

    INTELLIGENCE("Intelligence"),
    WILLPOWER("Willpower"),
    STRENGTH("Strength"),
    DEXTERITY("Dexterity"),
    AGILITY("Agility"),
    ENDURANCE("Endurance"),
    STAMINA("Stamina");

}
