package nl.t64.game.rpg.components.party;

import lombok.AllArgsConstructor;


@AllArgsConstructor
public enum StatItemId implements SuperEnum {

    INTELLIGENCE("Intelligence") {
        @Override
        public String getDescription(HeroItem hero) {
            return "Tekst en uitleg over " + title + getNeededXpForNextLevel(this, hero);
        }
    },
    WILLPOWER("Willpower") {
        @Override
        public String getDescription(HeroItem hero) {
            return "Tekst en uitleg over " + title + getNeededXpForNextLevel(this, hero);
        }
    },
    DEXTERITY("Dexterity") {
        @Override
        public String getDescription(HeroItem hero) {
            return "Tekst en uitleg over " + title + getNeededXpForNextLevel(this, hero);
        }
    },
    AGILITY("Agility") {
        @Override
        public String getDescription(HeroItem hero) {
            return "Tekst en uitleg over " + title + getNeededXpForNextLevel(this, hero);
        }
    },
    ENDURANCE("Endurance") {
        @Override
        public String getDescription(HeroItem hero) {
            return "Tekst en uitleg over " + title + getNeededXpForNextLevel(this, hero);
        }
    },
    STRENGTH("Strength") {
        @Override
        public String getDescription(HeroItem hero) {
            return "Tekst en uitleg over " + title + getNeededXpForNextLevel(this, hero);
        }
    },
    STAMINA("Stamina") {
        @Override
        public String getDescription(HeroItem hero) {
            return "Tekst en uitleg over " + title + getNeededXpForNextLevel(this, hero);
        }
    };

    final String title;

    @Override
    public String getTitle() {
        return title;
    }

    String getNeededXpForNextLevel(StatItemId statItemId, HeroItem hero) {
        String xpNeeded = String.valueOf(hero.getXpCostForNextLevelOf(statItemId));
        if (xpNeeded.equals("0")) {
            xpNeeded = "Max";
        }
        return "\n\nXP needed for next level: " + xpNeeded;
    }

}
