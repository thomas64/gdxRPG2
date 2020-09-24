package nl.t64.game.rpg.components.party;

import lombok.AllArgsConstructor;


@AllArgsConstructor
enum ResourceType implements SuperEnum {

    GOLD("Gold"),
    HERB("Herb"),
    SPICE("Spice"),
    GEMSTONE("Gemstone");

    final String title;

    @Override
    public String getTitle() {
        return title;
    }

}
