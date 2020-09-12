package nl.t64.game.rpg.constants;

import lombok.AllArgsConstructor;


@AllArgsConstructor
public enum CalcAttributeId implements SuperEnum {

    WEIGHT("Weight"),

    BASE_HIT("Base Hit"),
    DAMAGE("Damage"),

    PROTECTION("Protection"),
    DEFENSE("Defense");

    private final String title;

    @Override
    public String getTitle() {
        return title;
    }

}
