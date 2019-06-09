package nl.t64.game.rpg.constants;

import lombok.AllArgsConstructor;


@AllArgsConstructor
public enum InventoryAttribute {

    SKILL("Skill"),
    WEIGHT("Weight"),
    MIN_STRENGTH("Min. Strength"),
    BASE_HIT("Base Hit"),
    DAMAGE("Damage"),
    PROTECTION("Protection"),
    DEFENSE("Defense"),
    DEXTERITY("Dexterity"),
    STEALTH("Stealth");

    public final String title;

}
