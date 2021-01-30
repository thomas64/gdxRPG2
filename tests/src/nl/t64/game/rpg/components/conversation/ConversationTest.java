package nl.t64.game.rpg.components.conversation;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import nl.t64.game.rpg.GameData;
import nl.t64.game.rpg.GameTest;
import nl.t64.game.rpg.Utils;
import nl.t64.game.rpg.components.party.InventoryDatabase;
import nl.t64.game.rpg.components.quest.QuestGraph;
import nl.t64.game.rpg.profile.ProfileManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

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
        assertThat(graph.getCurrentPhrase()).containsOnly("Hi Mozes!");
        assertThat(graph.getPhrases().get("1").getText()).containsOnly("Hi Mozes!");
        assertThat(graph.getPhrases().get("1").getChoices()).isEmpty();
        assertThat(graph.getAssociatedChoices()).hasSize(1);
        assertThat(graph.getAssociatedChoices()[0].getText()).isEqualTo("->");
        assertThat(graph.getAssociatedChoices()[0]).hasToString("->");
        assertThat(graph.getAssociatedChoices()[0].getDestinationId()).isEqualTo("2");
        assertThat(graph.getAssociatedChoices()[0].getConversationCommand()).isEqualTo(ConversationCommand.NONE);
        assertThat(graph.getAssociatedChoices()[0].getConditionId()).isEmpty();
        assertThat(graph.getAssociatedChoices()[0].isMeetingCondition()).isTrue();
    }

    @Test
    void whenConversationGraphIsLoaded_ShouldGetGivenChoices() {
        ConversationGraph graph = conversations.getConversationById("luana01");
        graph.setCurrentPhraseId("8");
        assertThat(graph.getCurrentPhraseId()).isEqualTo("8");
        assertThat(graph.getPhrases()).hasSize(15);
        assertThat(graph.getCurrentPhrase())
                .containsOnly("Okay, here's the deal. You are taking me with you and MAYBE I'll forgive you.");
        assertThat(graph.getAssociatedChoices()).hasSize(2);
        assertThat(graph.getAssociatedChoices()[0].getText())
                .isEqualTo("Alright Luana, I will endure your presence.");
        assertThat(graph.getAssociatedChoices()[0].getDestinationId()).isEqualTo("9");
        assertThat(graph.getAssociatedChoices()[0].getConversationCommand()).isEqualTo(ConversationCommand.NONE);
        assertThat(graph.getAssociatedChoices()[1].getText()).isEqualTo("No, I don't WANT your protection.");
        assertThat(graph.getAssociatedChoices()[1].getDestinationId()).isEqualTo("10");
        assertThat(graph.getAssociatedChoices()[1].getConversationCommand()).isEqualTo(ConversationCommand.NONE);
    }

    @Test
    void whenConversationGraphIsLoaded_ShouldGetOtherChoices() {
        ConversationGraph graph = conversations.getConversationById("luana01");
        graph.setCurrentPhraseId("9");
        assertThat(graph.getCurrentPhraseId()).isEqualTo("9");
        assertThat(graph.getPhrases()).hasSize(15);
        assertThat(graph.getCurrentPhrase())
                .containsOnly("Alright! Now were talking!", "You are so not gonna regret this.");
        assertThat(graph.getAssociatedChoices()).hasSize(1);
        assertThat(graph.getAssociatedChoices()[0].getText()).isEqualTo("->");
        assertThat(graph.getAssociatedChoices()[0].getDestinationId()).isEqualTo("301");
        assertThat(graph.getAssociatedChoices()[0].getConversationCommand())
                .isEqualTo(ConversationCommand.HERO_JOIN);
    }

    @Test
    void whenConversationHasSkillCondition_ShouldMeetConditionWhenMet() {
        final GameData gameData = Utils.getGameData();
        gameData.onNotifyCreateProfile(new ProfileManager());

        ConversationGraph graph = conversations.getConversationById("quest0006");
        assertThat(graph.getCurrentPhraseId()).isEqualTo("1");
        assertThat(graph.getAssociatedChoices()).hasSize(3);
        assertThat(graph.getAssociatedChoices()[1].getText()).startsWith("[Diplomat 2] W");
        assertThat(graph.getAssociatedChoices()[1].isMeetingCondition()).isFalse();

        gameData.getParty().addHero(gameData.getHeroes().getHero("elias"));
        assertThat(graph.getAssociatedChoices()[1].isMeetingCondition()).isTrue();
    }

    @Test
    void whenConversationHasItemCondition_ShouldMeetConditionWhenMet() {
        final GameData gameData = Utils.getGameData();
        gameData.onNotifyCreateProfile(new ProfileManager());

        ConversationGraph graph = conversations.getConversationById("quest0006");
        graph.setCurrentPhraseId("11");
        assertThat(graph.getAssociatedChoices()).hasSize(3);
        assertThat(graph.getAssociatedChoices()[1].getText()).startsWith("[Mysterious Tunnel Key] Y");
        assertThat(graph.getAssociatedChoices()[1].isMeetingCondition()).isFalse();

        gameData.getInventory().autoSetItem(InventoryDatabase.getInstance().createInventoryItem("key_mysterious_tunnel"));
        assertThat(graph.getAssociatedChoices()[1].isMeetingCondition()).isTrue();
    }

    @Test
    void whenConversationHasInvisibleQuestCondition_ShouldShowChoiceWhenMet() {
        final GameData gameData = Utils.getGameData();
        gameData.onNotifyCreateProfile(new ProfileManager());
        ConversationGraph graph = conversations.getConversationById("quest0006");
        graph.setCurrentPhraseId("37");

        assertThat(graph.getAssociatedChoices()).hasSize(1);
        assertThat(graph.getAssociatedChoices()[0].getText()).startsWith("I was just kidding.");
        assertThat(graph.getAssociatedChoices()[0].getConditionId()).isEqualTo("");
        assertThat(graph.getAssociatedChoices()[0].isMeetingCondition()).isTrue();

        QuestGraph quest7 = gameData.getQuests().getQuestById("quest0007");
        quest7.know();
        quest7.accept(s -> {});
        quest7.unclaim();

        assertThat(graph.getAssociatedChoices()).hasSize(2);
        assertThat(graph.getAssociatedChoices()[1].getText()).startsWith("I gave it to someone else. (Fails ");
        assertThat(graph.getAssociatedChoices()[1].getConditionId()).isEqualTo("i_quest7_unclaimed");
        assertThat(graph.getAssociatedChoices()[1].isMeetingCondition()).isTrue();
    }

    @Test
    void whenConversationHasInvisibleTaskCondition_ShouldShowChoiceWhenMet() {
        final GameData gameData = Utils.getGameData();
        gameData.onNotifyCreateProfile(new ProfileManager());
        ConversationGraph graph = conversations.getConversationById("quest0006");
        graph.setCurrentPhraseId("37");
        List<ConversationChoice> choices = graph.getPhrases().get("37").getChoices();

        assertThat(graph.getAssociatedChoices()).hasSize(1);
        assertThat(graph.getAssociatedChoices()[0].getText()).startsWith("I was just kidding.");
        assertThat(graph.getAssociatedChoices()[0].getConditionId()).isEqualTo("");
        assertThat(graph.getAssociatedChoices()[0].isMeetingCondition()).isTrue();

        assertThat(choices).hasSize(3);
        assertThat(choices.get(2).getText()).startsWith("I already used it myself. (Fails ");
        assertThat(choices.get(2).getConditionId()).isEqualTo("i_quest6_task3");
        assertThat(choices.get(2).isMeetingCondition()).isFalse();
        assertThat(choices.get(2).isVisible()).isFalse();

        QuestGraph quest6 = gameData.getQuests().getQuestById("quest0006");
        gameData.getInventory().autoSetItem(InventoryDatabase.getInstance().createInventoryItem("key_mysterious_tunnel"));
        quest6.setTaskComplete("3");

        assertThat(graph.getAssociatedChoices()).hasSize(2);
        assertThat(graph.getAssociatedChoices()[1].getText()).startsWith("I already used it myself. (Fails ");
        assertThat(graph.getAssociatedChoices()[1].getConditionId()).isEqualTo("i_quest6_task3");
        assertThat(graph.getAssociatedChoices()[1].isMeetingCondition()).isTrue();

        assertThat(choices).hasSize(3);
        assertThat(choices.get(2).getText()).startsWith("I already used it myself. (Fails ");
        assertThat(choices.get(2).getConditionId()).isEqualTo("i_quest6_task3");
        assertThat(choices.get(2).isMeetingCondition()).isTrue();
        assertThat(choices.get(2).isVisible()).isTrue();
    }

}
