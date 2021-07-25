package nl.t64.game.rpg.components.event

import nl.t64.game.rpg.GameTest
import nl.t64.game.rpg.ProfileManager
import nl.t64.game.rpg.Utils.gameData
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test


internal class EventTest : GameTest() {

    private lateinit var eventContainer: EventContainer

    @BeforeEach
    private fun setup() {
        gameData.onNotifyCreateProfile(ProfileManager())
        eventContainer = EventContainer()
    }

    @Test
    fun whenEventMeetsConditions_ShouldBeAbleToPlay() {
        val event = eventContainer.getEventById("event0001")
        assertThat(event.conversationId).isEqualTo("event0001")
        assertThat(event.entityId).isEqualTo("man02")

        assertThat(event.conditionIds).containsExactly("quest6_known", "quest7_unknown")
        assertThat(event.hasPlayed).isFalse

        event.possibleStart()
        assertThat(event.hasPlayed).isFalse

        gameData.quests.getQuestById("quest0006").know()
        event.possibleStart()
        assertThat(event.hasPlayed).isTrue
    }

}
