package nl.t64.game.rpg.components.party;

import lombok.AllArgsConstructor;


@AllArgsConstructor
public enum SkillType implements SuperEnum {

    ALCHEMIST("Alchemist") {
        @Override
        public String getDescription(HeroItem hero) {
            return "Tekst en uitleg over " + title
                    + getNeededXpForNextLevel(this, hero)
                    + getNeededGoldForNextLevel(this, hero);
        }
    },
    DIPLOMAT("Diplomat") {
        @Override
        public String getDescription(HeroItem hero) {
            return "Tekst en uitleg over " + title
                    + getNeededXpForNextLevel(this, hero)
                    + getNeededGoldForNextLevel(this, hero);
        }
    },
    HEALER("Healer") {
        @Override
        public String getDescription(HeroItem hero) {
            return "Tekst en uitleg over " + title
                    + getNeededXpForNextLevel(this, hero)
                    + getNeededGoldForNextLevel(this, hero);
        }
    },
    LOREMASTER("Loremaster") {
        @Override
        public String getDescription(HeroItem hero) {
            return "Tekst en uitleg over " + title
                    + getNeededXpForNextLevel(this, hero)
                    + getNeededGoldForNextLevel(this, hero);
        }
    },
    MECHANIC("Mechanic") {
        @Override
        public String getDescription(HeroItem hero) {
            return "Tekst en uitleg over " + title
                    + getNeededXpForNextLevel(this, hero)
                    + getNeededGoldForNextLevel(this, hero);
        }
    },
    MERCHANT("Merchant") {
        @Override
        public String getDescription(HeroItem hero) {
            return "Tekst en uitleg over " + title
                    + getNeededXpForNextLevel(this, hero)
                    + getNeededGoldForNextLevel(this, hero);
        }
    },
    RANGER("Ranger") {
        @Override
        public String getDescription(HeroItem hero) {
            return "Tekst en uitleg over " + title
                    + getNeededXpForNextLevel(this, hero)
                    + getNeededGoldForNextLevel(this, hero);
        }
    },
    STEALTH("Stealth") {
        @Override
        public String getDescription(HeroItem hero) {
            return "Tekst en uitleg over " + title
                    + getNeededXpForNextLevel(this, hero)
                    + getNeededGoldForNextLevel(this, hero);
        }
    },
    THIEF("Thief") {
        @Override
        public String getDescription(HeroItem hero) {
            return "Tekst en uitleg over " + title
                    + getNeededXpForNextLevel(this, hero)
                    + getNeededGoldForNextLevel(this, hero);
        }
    },
    TROUBADOUR("Troubadour") {
        @Override
        public String getDescription(HeroItem hero) {
            return "Tekst en uitleg over " + title
                    + getNeededXpForNextLevel(this, hero)
                    + getNeededGoldForNextLevel(this, hero);
        }
    },
    WARRIOR("Warrior") {
        @Override
        public String getDescription(HeroItem hero) {
            return "Tekst en uitleg over " + title
                    + getNeededXpForNextLevel(this, hero)
                    + getNeededGoldForNextLevel(this, hero);
        }
    },
    WIZARD("Wizard") {
        @Override
        public String getDescription(HeroItem hero) {
            return "Tekst en uitleg over " + title
                    + getNeededXpForNextLevel(this, hero)
                    + getNeededGoldForNextLevel(this, hero);
        }
    },

    HAFTED("Hafted") {
        @Override
        public String getDescription(HeroItem hero) {
            return "Tekst en uitleg over " + title
                    + getNeededXpForNextLevel(this, hero)
                    + getNeededGoldForNextLevel(this, hero);
        }
    },
    MISSILE("Missile") {
        @Override
        public String getDescription(HeroItem hero) {
            return "Tekst en uitleg over " + title
                    + getNeededXpForNextLevel(this, hero)
                    + getNeededGoldForNextLevel(this, hero);
        }
    },
    POLE("Pole") {
        @Override
        public String getDescription(HeroItem hero) {
            return "Tekst en uitleg over " + title
                    + getNeededXpForNextLevel(this, hero)
                    + getNeededGoldForNextLevel(this, hero);
        }
    },
    SHIELD("Shield") {
        @Override
        public String getDescription(HeroItem hero) {
            return "Tekst en uitleg over " + title
                    + getNeededXpForNextLevel(this, hero)
                    + getNeededGoldForNextLevel(this, hero);
        }
    },
    SWORD("Sword") {
        @Override
        public String getDescription(HeroItem hero) {
            return "Tekst en uitleg over " + title
                    + getNeededXpForNextLevel(this, hero)
                    + getNeededGoldForNextLevel(this, hero);
        }
    },
    THROWN("Thrown") {
        @Override
        public String getDescription(HeroItem hero) {
            return "Tekst en uitleg over " + title
                    + getNeededXpForNextLevel(this, hero)
                    + getNeededGoldForNextLevel(this, hero);
        }
    };

    final String title;

    @Override
    public String getTitle() {
        return title;
    }

    String getNeededXpForNextLevel(SkillType skillType, HeroItem hero) {
        String xpNeeded = String.valueOf(hero.getXpCostForNextLevelOf(skillType));
        if (xpNeeded.equals("0")) {
            xpNeeded = "Max";
        }
        return "\n\nXP needed for next level: " + xpNeeded;
    }

    String getNeededGoldForNextLevel(SkillType skillType, HeroItem hero) {
        String goldNeeded = String.valueOf(hero.getGoldCostForNextLevelOf(skillType));
        if (goldNeeded.equals("0")) {
            goldNeeded = "Max";
        }
        return "\nGold needed for next level: " + goldNeeded;
    }

}
