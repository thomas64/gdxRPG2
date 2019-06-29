package nl.t64.game.rpg.components.party;

import lombok.AllArgsConstructor;


@AllArgsConstructor
public enum StatType implements SuperEnum {

    WEIGHT("Weight"),

    BASE_HIT("Base Hit"),
    DAMAGE("Damage"),

    PROTECTION("Protection"),
    DEFENSE("Defense"),

    INTELLIGENCE("Intelligence"),
    DEXTERITY("Dexterity"),
    ENDURANCE("Endurance"),
    STRENGTH("Strength"),
    STAMINA("Stamina");

    final String title;

    @Override
    public String getTitle() {
        return title;
    }
}
