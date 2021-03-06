package nl.t64.game.rpg.components.loot;

import nl.t64.game.rpg.GameTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.entry;


class LootTest extends GameTest {

    private LootContainer lootContainer;

    @BeforeEach
    private void setup() {
        lootContainer = new LootContainer();
    }

    @Test
    void whenSparklesAreCreated_ShouldHaveCorrectDataInside() {
        Loot sparkle = lootContainer.getLoot("sparkle0001");
        Map<String, Integer> expected = Map.of("gemstone", 1);
        assertThat(sparkle.isTaken()).isFalse();
        assertThat(sparkle.getContent()).hasSize(1);
        Map.Entry<String, Integer> entry = expected.entrySet().iterator().next();
        assertThat(sparkle.getContent()).containsOnly(entry(entry.getKey(), entry.getValue()));
    }

    @Test
    void whenChestsAreCreated_ShouldHaveCorrectDataInside() {
        Loot chest = lootContainer.getLoot("chest0001");
        Map<String, Integer> expected = Map.of("healing_potion", 3);
        assertThat(chest.isTaken()).isFalse();
        assertThat(chest.isTrapped()).isFalse();
        assertThat(chest.isLocked()).isFalse();
        assertThat(chest.getContent()).hasSize(1);
        Map.Entry<String, Integer> entry = expected.entrySet().iterator().next();
        assertThat(chest.getContent()).containsOnly(entry(entry.getKey(), entry.getValue()));
    }

    @Test
    void whenQuestsAreCreated_ShouldHaveCorrectDataInside() {
        Loot reward = lootContainer.getLoot("quest0001");
        assertThat(reward.isTaken()).isFalse();
        assertThat(reward.getContent()).hasSize(1);
        assertThat(reward.getXp()).isEqualTo(2);
        reward.clearXp();
        assertThat(reward.getContent()).hasSize(1);
        assertThat(reward.getXp()).isZero();
    }

    @Test
    void whenLootIdDoesNotExistLikeInQuestWithNoReward_ShouldReturnEmptyReward() {
        Loot reward = lootContainer.getLoot("doesNotExist");
        assertThat(reward.getContent()).isEmpty();
        assertThat(reward.getXp()).isZero();
        assertThat(reward.isTaken()).isTrue();
        assertThat(reward.isTrapped()).isFalse();
        assertThat(reward.canDisarmTrap(0)).isTrue();
        assertThat(reward.isLocked()).isFalse();
        assertThat(reward.canPickLock(0)).isTrue();
    }

    @Test
    void whenChestsHasTrapOrLock_ShouldNeedToDisarmOrPick() {
        int thieflevel = 8;
        int mechaniclevel = 8;
        Loot chest = lootContainer.getLoot("chest0005");
        assertThat(chest.isTaken()).isFalse();
        assertThat(chest.isTrapped()).isTrue();
        assertThat(chest.getTrapLevel()).isEqualTo(6);
        assertThat(chest.canDisarmTrap(mechaniclevel)).isTrue();
        assertThat(chest.isLocked()).isTrue();
        assertThat(chest.getLockLevel()).isEqualTo(10);
        assertThat(chest.canPickLock(thieflevel)).isFalse();
        assertThat(chest.getContent()).hasSize(1);
        assertThat(chest.getContent()).isEqualTo(Map.of("gold", 1000));
        chest.disarmTrap();
        chest.pickLock();
        chest.clearContent();
        assertThat(chest.isTaken()).isTrue();
        assertThat(chest.isTrapped()).isFalse();
        assertThat(chest.getTrapLevel()).isZero();
        assertThat(chest.canDisarmTrap(mechaniclevel)).isTrue();
        assertThat(chest.isLocked()).isFalse();
        assertThat(chest.getLockLevel()).isZero();
        assertThat(chest.canPickLock(thieflevel)).isTrue();
        assertThat(chest.getContent()).isEmpty();
        chest.updateContent(Map.of("herb", 10));
        assertThat(chest.isTaken()).isFalse();
        assertThat(chest.isTrapped()).isFalse();
        assertThat(chest.getTrapLevel()).isZero();
        assertThat(chest.canDisarmTrap(mechaniclevel)).isTrue();
        assertThat(chest.isLocked()).isFalse();
        assertThat(chest.getLockLevel()).isZero();
        assertThat(chest.canPickLock(thieflevel)).isTrue();
        assertThat(chest.getContent()).hasSize(1);
    }

    @Test
    void whenQuestHasBonusRemoved_ShouldRemoveBonus() {
        Loot reward = lootContainer.getLoot("quest0006");
        assertThat(reward.isTaken()).isFalse();
        assertThat(reward.getContent()).hasSize(3);
        assertThat(reward.getContent()).isEqualTo(Map.of("gold", 1, "bonus_gold", 1, "bonus_herb", 1));
        assertThat(reward.getXp()).isEqualTo(2);
        reward.removeBonus();
        assertThat(reward.getContent()).hasSize(1);
        assertThat(reward.getContent()).isEqualTo(Map.of("gold", 1));
    }

    @Test
    void whenQuestHasBonusHandled_ShouldMergeAndRemoveBonus() {
        Loot reward = lootContainer.getLoot("quest0006");
        assertThat(reward.isTaken()).isFalse();
        assertThat(reward.getContent()).hasSize(3);
        assertThat(reward.getContent()).isEqualTo(Map.of("gold", 1, "bonus_gold", 1, "bonus_herb", 1));
        assertThat(reward.getXp()).isEqualTo(2);
        reward.handleBonus();
        assertThat(reward.getContent()).hasSize(2);
        assertThat(reward.getContent()).isEqualTo(Map.of("gold", 2, "herb", 1));
    }

}
