package nl.t64.game.rpg.components.party;

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

    final String title;

    @Override
    public String getTitle() {
        return title;
    }

}
