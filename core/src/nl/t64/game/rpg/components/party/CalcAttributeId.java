package nl.t64.game.rpg.components.party;

import lombok.AllArgsConstructor;

import java.util.Arrays;
import java.util.Optional;


@AllArgsConstructor
public enum CalcAttributeId implements SuperEnum {

    WEIGHT("Weight") {
        @Override
        public String getDescription() {
            return """
                    Decreases amount of movepoints in combat.
                    Allows the possibility of shoving a less weighted character in combat.""";
        }
    },

    MOVEPOINTS("Movepoints") {
        @Override
        public String getDescription() {
            return """
                    Defines how many steps this character is able to take in combat.
                    Movepoints are calculated from Stamina and Weight.
                    More stamina, more movepoints. More weight, less movepoints.""";
        }
    },

    BASE_HIT("Base Hit") {
        @Override
        public String getDescription() {
            return "ToDo";
        }
    },

    DAMAGE("Damage") {
        @Override
        public String getDescription() {
            return """
                    Defines the amount of damage-to-inflict to an enemy with physical weapons in combat.
                    Damage is the counterpart of Protection.
                    An enemy's protection decreases the damage you inflict.""";
        }
    },

    PROTECTION("Protection") {
        @Override
        public String getDescription() {
            return """
                    Decreases the enemy's damage-to-inflict with physical weapons in combat.
                    Protection is the counterpart of Damage.
                    A complete armor set from the same type results into bonus protection.""";
        }
    },

    DEFENSE("Defense") {
        @Override
        public String getDescription() {
            return "ToDo";
        }
    },

    SPELL_BATTERY("Spell Battery") {
        @Override
        public String getDescription() {
            return "ToDo";
        }
    };

    private final String title;

    public static Optional<CalcAttributeId> from(String label) {
        return Arrays.stream(CalcAttributeId.values())
                     .filter(calcAttributeId -> label.toLowerCase().contains(calcAttributeId.title.toLowerCase()))
                     .findFirst();
    }

    @Override
    public String getTitle() {
        return title;
    }

    public abstract String getDescription();

}
