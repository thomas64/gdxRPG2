package nl.t64.game.rpg.components.conversation;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import nl.t64.game.rpg.GameTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;


class ConversationTest extends GameTest {

    private ConversationContainer conversations;

    @BeforeEach
    private void setup() {
        conversations = new ConversationContainer();
    }

    @Test
    void whenConversationsAreCreated_ShouldHaveAllConversationsStartedAtPhrase1() {
        PhraseIdContainer phraseIdContainer = conversations.createPhraseIdContainer();
        assertThat(phraseIdContainer.getPhraseId("luana01")).isEqualTo("1");
    }

    @Test
    void whenGameIsSaved_ShouldStoreAllCurrentPhraseIdsInContainer() {
        PhraseIdContainer phraseIdContainer = conversations.createPhraseIdContainer();
        FileHandle[] configDirList = Gdx.files.local("configs/conversations/").list(".json");
        int containerSize = phraseIdContainer.getSize();
        assertThat(configDirList).hasSizeLessThan(containerSize);
    }

    @Test
    void whenGameIsLoaded_ShouldFillConversationContainerFromPhraseIdContainer() {
        PhraseIdContainer phraseIdContainer = conversations.createPhraseIdContainer();
        phraseIdContainer.setPhraseId("luana01", "2");
        conversations.setCurrentPhraseIds(phraseIdContainer);
        ConversationGraph conversationGraph = conversations.getConversationById("luana01");
        assertThat(conversationGraph.getCurrentPhraseId()).isEqualTo("2");
    }

    @Test
    void whenConversationGraphIsLoaded_ShouldGetDefaultChoices() {
        ConversationGraph graph = conversations.getConversationById("luana01");
        assertThat(graph.getCurrentPhraseId()).isEqualTo("1");
        assertThat(graph.getPhrases()).hasSize(15);
        assertThat(graph.getPhraseById("1").getText()).containsOnly("Hi Mozes!");
        assertThat(graph.getChoices()).hasSize(7);
        assertThat(graph.getChoices().get("1")).isNull();
        assertThat(graph.getAssociatedChoices().get(0).getText()).isEqualTo("->");
        assertThat(graph.getAssociatedChoices().get(0)).hasToString("->");
        assertThat(graph.getAssociatedChoices().get(0).getDestinationId()).isEqualTo("2");
        assertThat(graph.getAssociatedChoices().get(0).getConversationCommand()).isEqualTo(ConversationCommand.NONE);
    }

    @Test
    void whenConversationGraphIsLoaded_ShouldGetGivenChoices() {
        ConversationGraph graph = conversations.getConversationById("luana01");
        graph.setCurrentPhraseId("8");
        assertThat(graph.getCurrentPhraseId()).isEqualTo("8");
        assertThat(graph.getPhrases()).hasSize(15);
        assertThat(graph.getPhraseById("8").getText())
                .containsOnly("Okay, here's the deal. You are taking me with you and MAYBE I'll forgive you.");
        assertThat(graph.getChoices()).hasSize(7);
        assertThat(graph.getChoices().get("8")).hasSize(2);
        assertThat(graph.getAssociatedChoices().get(0).getText())
                .isEqualTo("Alright Luana, I will endure your presence.");
        assertThat(graph.getAssociatedChoices().get(0).getDestinationId()).isEqualTo("9");
        assertThat(graph.getAssociatedChoices().get(0).getConversationCommand()).isEqualTo(ConversationCommand.NONE);
        assertThat(graph.getAssociatedChoices().get(1).getText()).isEqualTo("No, I don't WANT your protection.");
        assertThat(graph.getAssociatedChoices().get(1).getDestinationId()).isEqualTo("10");
        assertThat(graph.getAssociatedChoices().get(1).getConversationCommand()).isEqualTo(ConversationCommand.NONE);
    }

    @Test
    void whenConversationGraphIsLoaded_ShouldGetOtherChoices() {
        ConversationGraph graph = conversations.getConversationById("luana01");
        graph.setCurrentPhraseId("9");
        assertThat(graph.getCurrentPhraseId()).isEqualTo("9");
        assertThat(graph.getPhrases()).hasSize(15);
        assertThat(graph.getPhraseById("9").getText())
                .containsOnly("Alright! Now were talking!", "You are so not gonna regret this.");
        assertThat(graph.getChoices()).hasSize(7);
        assertThat(graph.getChoices().get("9")).hasSize(1);
        assertThat(graph.getAssociatedChoices().get(0).getText()).isEqualTo("->");
        assertThat(graph.getAssociatedChoices().get(0).getDestinationId()).isEqualTo("301");
        assertThat(graph.getAssociatedChoices().get(0).getConversationCommand())
                .isEqualTo(ConversationCommand.HERO_JOIN);
    }

}
