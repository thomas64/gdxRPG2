package nl.t64.game.rpg.components.party;

import lombok.AllArgsConstructor;

import java.util.Optional;


@AllArgsConstructor
public enum InventoryMinimal implements SuperEnum {

    SKILL("Skill") {
        @Override
        Optional<String> createMessageIfHeroHasNotEnoughFor(InventoryItem item, HeroItem hero) {
            if (item.skill == null) {
                return Optional.empty();
            }
            if (hero.getSkillRankOf(item.skill) <= 0) {
                return Optional.of(String.format("%s needs the %s skill%nto equip that %s.",
                                                 hero.name, item.skill.title, item.name));
            }
            return Optional.empty();
        }
    },

    MIN_INTELLIGENCE("Min. Intelligence") {
        @Override
        Optional<String> createMessageIfHeroHasNotEnoughFor(InventoryItem item, HeroItem hero) {
            if (hero.getStatRankOf(StatItemId.INTELLIGENCE) < item.minIntelligence) {
                return Optional.of(
                        String.format("%s needs %s %s%nto equip that %s.",
                                      hero.name, item.minIntelligence, StatItemId.INTELLIGENCE.title, item.name));
            }
            return Optional.empty();
        }
    },

    MIN_STRENGTH("Min. Strength") {
        @Override
        Optional<String> createMessageIfHeroHasNotEnoughFor(InventoryItem item, HeroItem hero) {
            if (hero.getStatRankOf(StatItemId.STRENGTH) < item.minStrength) {
                return Optional.of(String.format("%s needs %s %s%nto equip that %s.",
                                                 hero.name, item.minStrength, StatItemId.STRENGTH.title, item.name));
            }
            return Optional.empty();
        }
    };

    private final String title;

    abstract Optional<String> createMessageIfHeroHasNotEnoughFor(InventoryItem item, HeroItem hero);

    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public String getDescription(HeroItem hero) {
        return title;
    }

}
