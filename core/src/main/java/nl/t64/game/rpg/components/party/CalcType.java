package nl.t64.game.rpg.components.party;

import lombok.AllArgsConstructor;


@AllArgsConstructor
public enum CalcType implements SuperEnum {

    WEIGHT("Weight"),

    BASE_HIT("Base Hit"),
    DAMAGE("Damage"),

    PROTECTION("Protection"),
    DEFENSE("Defense");

    final String title;

    @Override
    public String getTitle() {
        return title;
    }

}
