package nl.t64.game.rpg.components.party;

import lombok.AllArgsConstructor;


@AllArgsConstructor
public enum ResourceType implements SuperEnum {

    GOLD("Gold"),
    HERBS("Herbs"),
    SPICES("Spices"),
    GEMSTONES("Gemstones");

    final String title;

    @Override
    public String getTitle() {
        return title;
    }

}
