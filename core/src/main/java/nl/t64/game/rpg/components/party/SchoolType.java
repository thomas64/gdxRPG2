package nl.t64.game.rpg.components.party;

import lombok.AllArgsConstructor;


@AllArgsConstructor
enum SchoolType implements SuperEnum {

    NONE(""),
    SPECIAL("Special"),
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
