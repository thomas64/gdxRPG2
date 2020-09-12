package nl.t64.game.rpg.components.party;

import lombok.AllArgsConstructor;
import nl.t64.game.rpg.constants.StatItemId;
import nl.t64.game.rpg.constants.SuperEnum;

import java.util.Optional;


@AllArgsConstructor
enum InventoryMinimal implements SuperEnum {

    SKILL("Skill") {
        @Override
        Optional<String> createMessageIfHeroHasNotEnoughFor(InventoryItem item, HeroItem hero) {
            if (item.skill == null) {
                return Optional.empty();
            }
            if (hero.getSkillById(item.skill).rank <= 0) {
                return Optional.of(String.format("%s needs the %s skill%nto equip that %s.",
                                                 hero.name, item.skill.getTitle(), item.name));
            }
            return Optional.empty();
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
