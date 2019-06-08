package nl.t64.game.rpg.components.party;

import lombok.AllArgsConstructor;


@AllArgsConstructor
enum InventoryAttribute {

    SKILL("Skill"),
    WEIGHT("Weight"),
    MIN_STRENGTH("Min. Strength"),
    BASE_HIT("Base Hit"),
    DAMAGE("Damage"),
    PROTECTION("Protection"),
    DEFENSE("Defense"),
    DEXTERITY("Dexterity"),
    STEALTH("Stealth");

    final String title;

}
