package nl.t64.game.rpg.components.party;

import lombok.AllArgsConstructor;


@AllArgsConstructor
public enum SkillItemId implements SuperEnum {

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

    final String title;

    @Override
    public String getTitle() {
        return title;
    }

}
