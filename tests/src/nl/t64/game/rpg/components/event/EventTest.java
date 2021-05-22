package nl.t64.game.rpg.components.event;

import nl.t64.game.rpg.GameData;
import nl.t64.game.rpg.GameTest;
import nl.t64.game.rpg.ProfileManager;
import nl.t64.game.rpg.Utils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;


class EventTest extends GameTest {

    private EventContainer eventContainer;

    @BeforeEach
    private void setup() {
        final GameData gameData = Utils.getGameData();
        gameData.onNotifyCreateProfile(new ProfileManager());
        eventContainer = new EventContainer();
    }

    @Test
    void whenEventMeetsConditions_ShouldBeAbleToPlay() {
        Event event = eventContainer.getEventById("event0001");
        assertThat(event.getConversationId()).isEqualTo("event0001");
        assertThat(event.getEntityId()).isEqualTo("man02");

        assertThat(event.getConditionIds()).containsExactly("quest6_known", "quest7_unknown");
        assertThat(event.isHasPlayed()).isFalse();

        event.possibleStart();
        assertThat(event.isHasPlayed()).isFalse();

        Utils.getGameData().getQuests().getQuestById("quest0006").know();
        event.possibleStart();
        assertThat(event.isHasPlayed()).isTrue();
    }

}
