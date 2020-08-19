package nl.t64.game.rpg.components.loot;

import nl.t64.game.rpg.GameData;
import nl.t64.game.rpg.GameTest;
import nl.t64.game.rpg.profile.ProfileManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.entry;


class LootTest extends GameTest {

    private LootContainer lootContainer;

    @BeforeEach
    private void setup() {
        final var profileManager = new ProfileManager();
        final var gameData = new GameData();
        gameData.onNotifyCreateProfile(profileManager);
        lootContainer = gameData.getLoot();
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
    void whenChestsHasTrapOrLock_ShouldNeedToDisarmOrPick() {
        Loot chest = lootContainer.getLoot("chest0005");
        Map<String, Integer> expected = Map.of("gold", 1000);
        assertThat(chest.isTaken()).isFalse();
        assertThat(chest.isTrapped()).isTrue();
        assertThat(chest.getTrapLevel()).isEqualTo(6);
        assertThat(chest.isLocked()).isTrue();
        assertThat(chest.getLockLevel()).isEqualTo(10);
        assertThat(chest.getContent()).hasSize(1);
        Map.Entry<String, Integer> entry = expected.entrySet().iterator().next();
        assertThat(chest.getContent()).containsOnly(entry(entry.getKey(), entry.getValue()));
        chest.disarmTrap();
        chest.pickLock();
        chest.clearContent();
        assertThat(chest.isTaken()).isTrue();
        assertThat(chest.isTrapped()).isFalse();
        assertThat(chest.getTrapLevel()).isEqualTo(0);
        assertThat(chest.isLocked()).isFalse();
        assertThat(chest.getLockLevel()).isEqualTo(0);
        assertThat(chest.getContent()).hasSize(0);
        chest.updateContent(Map.of("herb", 10));
        assertThat(chest.isTaken()).isFalse();
        assertThat(chest.isTrapped()).isFalse();
        assertThat(chest.getTrapLevel()).isEqualTo(0);
        assertThat(chest.isLocked()).isFalse();
        assertThat(chest.getLockLevel()).isEqualTo(0);
        assertThat(chest.getContent()).hasSize(1);
    }

}
