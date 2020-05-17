package nl.t64.game.rpg.components.conversation;

import nl.t64.game.rpg.GameTest;
import nl.t64.game.rpg.constants.ConversationCommand;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;


class NoteTest extends GameTest {

    @Test
    void whenNoteDatabaseIsCreated_ShouldContainNotes() {
        NoteDatabase noteDatabase = NoteDatabase.getInstance();
        ConversationGraph graph = noteDatabase.getNoteById("note_statue_hero_s_hometown_hero_s_chamber");
        assertThat(graph.getCurrentPhraseId()).isEqualTo("1");
        assertThat(graph.getPhrases()).hasSize(1);
        assertThat(graph.getPhraseById("1").getText()).containsOnly("What a beautiful statue. Somehow it looks a little bit like you. That's strange...");
        assertThat(graph.getChoices()).hasSize(1);
        assertThat(graph.getChoices().get("1")).hasSize(1);
        assertThat(graph.getAssociatedChoices().get(0).getText()).isEqualTo("->");
        assertThat(graph.getAssociatedChoices().get(0).toString()).isEqualTo("->");
        assertThat(graph.getAssociatedChoices().get(0).getDestinationId()).isEqualTo("1");
        assertThat(graph.getAssociatedChoices().get(0).getConversationCommand()).isEqualTo(ConversationCommand.EXIT_CONVERSATION);
    }

}
