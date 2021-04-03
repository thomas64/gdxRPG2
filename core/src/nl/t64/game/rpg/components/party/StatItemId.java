package nl.t64.game.rpg.components.party;

import lombok.AllArgsConstructor;


@AllArgsConstructor
public enum StatItemId implements SuperEnum {

    INTELLIGENCE("Intelligence"),
    WILLPOWER("Willpower"),
    STRENGTH("Strength"),
    DEXTERITY("Dexterity"),
    AGILITY("Agility"),
    ENDURANCE("Endurance"),
    STAMINA("Stamina");

    private final String title;

    @Override
    public String getTitle() {
        return title;
    }

}
