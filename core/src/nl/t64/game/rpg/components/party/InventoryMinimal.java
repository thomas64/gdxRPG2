package nl.t64.game.rpg.components.party;

import lombok.AllArgsConstructor;

import java.util.Optional;


@AllArgsConstructor
enum InventoryMinimal implements SuperEnum {

    SKILL("Skill") {
        @Override
        Optional<String> createMessageIfHeroHasNotEnoughFor(InventoryItem item, HeroItem hero) {
            return Optional.ofNullable(item.skill)
                           .filter(skillItemId -> hero.getSkillById(skillItemId).rank <= 0)
                           .map(skillItemId -> String.format("%s needs the %s skill%nto equip that %s.",
                                                             hero.name, skillItemId.getTitle(), item.name));
        }
    },

    MIN_INTELLIGENCE("Min. Intelligence") {
        @Override
        Optional<String> createMessageIfHeroHasNotEnoughFor(InventoryItem item, HeroItem hero) {
            if (hero.getStatById(StatItemId.INTELLIGENCE).rank < item.minIntelligence) {
                return Optional.of(
                        String.format("%s needs %s %s%nto equip that %s.",
                                      hero.name, item.minIntelligence, StatItemId.INTELLIGENCE.getTitle(), item.name));
            }
            return Optional.empty();
        }
    },

    MIN_STRENGTH("Min. Strength") {
        @Override
        Optional<String> createMessageIfHeroHasNotEnoughFor(InventoryItem item, HeroItem hero) {
            if (hero.getStatById(StatItemId.STRENGTH).rank < item.minStrength) {
                return Optional.of(String.format("%s needs %s %s%nto equip that %s.",
                                                 hero.name, item.minStrength, StatItemId.STRENGTH.getTitle(), item.name));
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

}
