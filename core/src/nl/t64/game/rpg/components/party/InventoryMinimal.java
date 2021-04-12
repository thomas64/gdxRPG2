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
                           .map(skillItemId -> String.format("""
                                                                     %s needs the %s skill
                                                                     to equip that %s.""",
                                                             hero.name, skillItemId.getTitle(), item.name));
        }
    },

    MIN_INTELLIGENCE("Min. Intelligence") {
        @Override
        Optional<String> createMessageIfHeroHasNotEnoughFor(InventoryItem item, HeroItem hero) {
            return InventoryMinimal.createMessage(item, StatItemId.INTELLIGENCE, hero);
        }
    },

    MIN_WILLPOWER("Min. Willpower") {
        @Override
        Optional<String> createMessageIfHeroHasNotEnoughFor(InventoryItem item, HeroItem hero) {
            return InventoryMinimal.createMessage(item, StatItemId.WILLPOWER, hero);
        }
    },

    MIN_STRENGTH("Min. Strength") {
        @Override
        Optional<String> createMessageIfHeroHasNotEnoughFor(InventoryItem item, HeroItem hero) {
            return InventoryMinimal.createMessage(item, StatItemId.STRENGTH, hero);
        }
    },

    MIN_DEXTERITY("Min. Dexterity") {
        @Override
        Optional<String> createMessageIfHeroHasNotEnoughFor(InventoryItem item, HeroItem hero) {
            return InventoryMinimal.createMessage(item, StatItemId.DEXTERITY, hero);
        }
    };

    private final String title;

    abstract Optional<String> createMessageIfHeroHasNotEnoughFor(InventoryItem item, HeroItem hero);

    @Override
    public String getTitle() {
        return title;
    }

    private static Optional<String> createMessage(InventoryItem item, StatItemId statItemId, HeroItem hero) {
        int minimalAttribute = item.getMinimalAttributeOfStatItemId(statItemId);
        if (hero.getCalculatedTotalStatOf(statItemId) < minimalAttribute) {
            return Optional.of(String.format("""
                                                     %s needs %s %s
                                                     to equip that %s.""",
                                             hero.name, minimalAttribute, statItemId.getTitle(), item.name));
        } else {
            return Optional.empty();
        }
    }

}
