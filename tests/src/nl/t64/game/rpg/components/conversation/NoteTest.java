package nl.t64.game.rpg.components.conversation;

import nl.t64.game.rpg.GameTest;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;


class NoteTest extends GameTest {

    private final NoteDatabase noteDB = NoteDatabase.getInstance();

    @Test
    void whenNoteDatabaseIsCreated_ShouldContainNotes() {
        ConversationGraph graph = noteDB.getNoteById("note_statue_hero_s_hometown_hero_s_chamber");
        assertThat(graph.getCurrentPhraseId()).isEqualTo("1");
        assertThat(graph.getPhrases()).hasSize(2);
        assertThat(graph.getCurrentPhrase())
                .containsOnly("What a beautiful statue.", "Somehow it looks a little bit like you.");
        assertThat(graph.getAssociatedChoices()).hasSize(1);
        graph.setCurrentPhraseId("2");
        assertThat(graph.getCurrentPhrase()).containsOnly("That's strange...");
        assertThat(graph.getAssociatedChoices()).hasSize(1);
        assertThat(graph.getAssociatedChoices()[0].getText()).isEqualTo("->");
        assertThat(graph.getAssociatedChoices()[0]).hasToString("->");
        assertThat(graph.getAssociatedChoices()[0].getDestinationId()).isEqualTo("1");
        assertThat(graph.getAssociatedChoices()[0].getConversationCommand()).isEqualTo(ConversationCommand.EXIT_CONVERSATION);
    }

}
