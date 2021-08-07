package nl.t64.game.rpg.components.party.spells

import nl.t64.game.rpg.components.party.SuperEnum


enum class ResourceType(override val title: String) : SuperEnum {

    GOLD("Gold"),
    HERB("Herb"),
    SPICE("Spice"),
    GEMSTONE("Gemstone");

}
