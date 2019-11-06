package nl.t64.game.rpg.components.party;

import lombok.AllArgsConstructor;


@AllArgsConstructor
public enum SchoolType implements SuperEnum {

    NONE("No"),
    UNKNOWN("Unknown"),
    NEUTRAL("Neutral"),
    ELEMENTAL("Elemental"),
    NAMING("Naming"),
    NECROMANCY("Necromancy"),
    STAR("Star");

    final String title;

    @Override
    public String getTitle() {
        return title;
    }

}
