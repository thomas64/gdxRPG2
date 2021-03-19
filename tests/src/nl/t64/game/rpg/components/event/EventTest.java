package nl.t64.game.rpg.components.event;

import nl.t64.game.rpg.GameData;
import nl.t64.game.rpg.GameTest;
import nl.t64.game.rpg.ProfileManager;
import nl.t64.game.rpg.Utils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Map;

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
        assertThat(event.getCharacterId()).isEqualTo("man02");

        assertThat(event.getCondition()).isEqualTo(Map.of("quest0006", "!UNKNOWN",
                                                          "quest0007", "UNKNOWN"));
        assertThat(event.isHasPlayed()).isFalse();

        event.startConversation();
        assertThat(event.isHasPlayed()).isFalse();

        Utils.getGameData().getQuests().getQuestById("quest0006").know();
        event.startConversation();
        assertThat(event.isHasPlayed()).isTrue();
    }

}