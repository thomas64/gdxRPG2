package nl.t64.game.rpg.components.party.skills

import nl.t64.game.rpg.components.party.SuperEnum


enum class SkillItemId(override val title: String) : SuperEnum {

    ALCHEMIST("Alchemist"),
    DIPLOMAT("Diplomat"),
    HEALER("Healer"),
    LOREMASTER("Loremaster"),
    MECHANIC("Mechanic"),
    MERCHANT("Merchant"),
    RANGER("Ranger"),
    STEALTH("Stealth"),
    THIEF("Thief"),
    TROUBADOUR("Troubadour"),
    WARRIOR("Warrior"),
    WIZARD("Wizard"),

    HAFTED("Hafted"),
    MISSILE("Missile"),
    POLE("Pole"),
    SHIELD("Shield"),
    SWORD("Sword"),
    THROWN("Thrown");

    fun isHandToHandWeaponSkill(): Boolean {
        return when (this) {
            SWORD, HAFTED, POLE -> true
            MISSILE, THROWN -> false
            else -> throw IllegalArgumentException("Only possible to ask a Weapon Skill.")
        }
    }

    companion object {
        fun from(possibleSkillItemId: String): SkillItemId {
            return values().first { possibleSkillItemId.equals(it.name, true) }
        }
    }

}
