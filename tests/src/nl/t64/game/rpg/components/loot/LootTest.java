package nl.t64.game.rpg.components.loot;

import nl.t64.game.rpg.GameData;
import nl.t64.game.rpg.GameTest;
import nl.t64.game.rpg.profile.ProfileManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;


class LootTest extends GameTest {

    private SparkleContainer sparkles;

    @BeforeEach
    private void setup() {
        final var profileManager = new ProfileManager();
        final var gameData = new GameData();
        gameData.onNotifyCreateProfile(profileManager);
        sparkles = gameData.getSparkles();
    }

    @Test
    void whenSparklesAreCreated_ShouldHaveCorrectDataInside() {
        Loot sparkle = sparkles.getSparkle("sparkle0001");
        Content expected = new Content("gemstones", 1);
        assertThat(sparkle.isTaken()).isFalse();
        assertThat(sparkle.getContent()).hasSize(1);
        assertThat(sparkle.getContent()).extracting("itemId").containsOnly(expected.getItemId());
        assertThat(sparkle.getContent()).extracting("amount").containsOnly(expected.getAmount());
    }

}
