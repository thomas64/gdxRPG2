package nl.t64.game.rpg.constants;

import lombok.AllArgsConstructor;


@AllArgsConstructor
public enum StatItemId implements SuperEnum {

    INTELLIGENCE("Intelligence"),
    WILLPOWER("Willpower"),
    DEXTERITY("Dexterity"),
    AGILITY("Agility"),
    ENDURANCE("Endurance"),
    STRENGTH("Strength"),
    STAMINA("Stamina");

    private final String title;

    @Override
    public String getTitle() {
        return title;
    }

}
