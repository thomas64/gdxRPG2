package nl.t64.game.rpg.components.quest;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import nl.t64.game.rpg.GameData;
import nl.t64.game.rpg.GameTest;
import nl.t64.game.rpg.Utils;
import nl.t64.game.rpg.components.party.InventoryDatabase;
import nl.t64.game.rpg.constants.Constant;
import nl.t64.game.rpg.profile.ProfileManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.assertj.core.api.Assertions.*;


class QuestTest extends GameTest {

    private QuestContainer quests;

    @BeforeEach
    private void setup() {
        quests = new QuestContainer();
    }

    @Test
    void whenQuestsAreCreated_ShouldHaveAllQuestsStartedAtStateUnknown() {
        StateContainer stateContainer = quests.createStateContainer();
        assertThat(stateContainer.getState("quest0001")).isEqualTo(QuestState.UNKNOWN);
    }

    @Test
    void whenGameIsSaved_ShouldStoreAllCurrentStatesInContainer() {
        StateContainer stateContainer = quests.createStateContainer();
        FileHandle[] configDirList = Gdx.files.local("configs/quests/").list(".json");
        int containerSize = stateContainer.getSize();
        assertThat(configDirList).hasSizeLessThan(containerSize);
    }

    @Test
    void whenGameIsLoaded_ShouldFillQuestContainerFromStateContainer() {
        StateContainer stateContainer = quests.createStateContainer();
        stateContainer.setState("quest0001", QuestState.ACCEPTED);
        quests.setCurrentStates(stateContainer);
        QuestGraph questGraph = quests.getQuestById("quest0001");
        assertThat(questGraph.getCurrentState()).isEqualTo(QuestState.ACCEPTED);
    }

    @Test
    void whenQuestGraphIsLoaded_ShouldHaveCorrectData() {
        QuestGraph quest0001 = quests.getQuestById("quest0001");
        assertThat(quest0001.getId()).isEqualTo("quest0001");
        assertThat(quest0001.getCurrentState()).isEqualTo(QuestState.UNKNOWN);
        assertThat(quest0001.getTitle()).isEqualTo("Gemstones for boy");
        assertThat(quest0001).hasToString("Gemstones for boy");
        Map<String, QuestTask> tasks = quest0001.getTasks();
        assertThat(tasks).hasSize(3);
        assertThat(tasks.get("1").getTaskPhrase()).isEqualTo("Collect 5 gemstones");
        assertThat(tasks.get("1").getType()).isEqualTo(QuestType.FETCH);
        assertThat(tasks.get("1").getTarget()).containsOnly(entry("gemstone", 5));
        assertThat(tasks.get("1")).hasToString("Collect 5 gemstones");
    }

    @Test
    void whenQuestGraphIsLoaded_ShouldHandleFlow() {
        final GameData gameData = Utils.getGameData();
        gameData.onNotifyCreateProfile(new ProfileManager());

        assertThat(gameData.getParty().getHero(0).getXpToInvest()).isEqualTo(0);
        QuestGraph quest0001 = quests.getQuestById("quest0001");
        assertThatExceptionOfType(IllegalCallerException.class).isThrownBy(() -> quest0001.setTaskComplete("1"));
        assertThat(quest0001.isFinished()).isFalse();
        quest0001.handleAccept(s -> assertThat(s).isEqualTo(Constant.PHRASE_ID_QUEST_ACCEPT));
        assertThat(quest0001.getCurrentState()).isEqualTo(QuestState.ACCEPTED);
        quest0001.handleReturn(s -> assertThat(s).isEqualTo(Constant.PHRASE_ID_QUEST_FAILURE));
        assertThatExceptionOfType(IllegalStateException.class)
                .isThrownBy(() -> quest0001.handleReward(null, null, null));
        gameData.getInventory().autoSetItem(InventoryDatabase.getInstance().createInventoryItem("gemstone", 5));
        gameData.getInventory().autoSetItem(InventoryDatabase.getInstance().createInventoryItem("herb", 3));
        quest0001.handleReturn(s -> assertThat(s).isEqualTo(Constant.PHRASE_ID_QUEST_SUCCESS));
        quest0001.handleReward(s -> assertThat(s).isNull(),
                               l -> assertThat(l.isTaken()).isFalse(),
                               null);
        assertThat(quest0001.getCurrentState()).isEqualTo(QuestState.UNCLAIMED);
        gameData.getLoot().getLoot("quest0001").clearContent();
        quest0001.handleReward(s -> assertThat(s).isNull(),
                               l -> assertThat(l.isTaken()).isTrue(),
                               s -> assertThat(s).isEqualTo(Constant.PHRASE_ID_QUEST_FINISHED));
        assertThat(quest0001.getCurrentState()).isEqualTo(QuestState.FINISHED);
        assertThat(quest0001.isFinished()).isTrue();
        assertThat(gameData.getParty().getHero(0).getXpToInvest()).isEqualTo(2);
        assertThat(gameData.getInventory().hasEnoughOfResource("gemstone", 1)).isFalse();
        assertThat(gameData.getInventory().hasEnoughOfResource("herb", 1)).isFalse();
    }

    @Test
    void whenDemandsAreCompleteFromTheStart_ShouldHandleQuickFlow() {
        final GameData gameData = Utils.getGameData();
        gameData.onNotifyCreateProfile(new ProfileManager());
        gameData.getInventory().autoSetItem(InventoryDatabase.getInstance().createInventoryItem("gemstone", 5));
        gameData.getInventory().autoSetItem(InventoryDatabase.getInstance().createInventoryItem("herb", 3));
        QuestGraph quest0001 = quests.getQuestById("quest0001");
        quest0001.handleAccept(s -> assertThat(s).isEqualTo(Constant.PHRASE_ID_QUEST_IMMEDIATE_SUCCESS));
    }

    //@formatter:off
    @Test
    void whenSearchingForEdgeCases_ShouldThrowExceptions() {
        final GameData gameData = Utils.getGameData();
        gameData.onNotifyCreateProfile(new ProfileManager());

        QuestGraph quest0001 = quests.getQuestById("quest0001");
        assertThat(quest0001.getCurrentState()).isEqualTo(QuestState.UNKNOWN);
        quest0001.know();
        assertThat(quest0001.getCurrentState()).isEqualTo(QuestState.KNOWN);
        quest0001.know();
        assertThat(quest0001.getCurrentState()).isEqualTo(QuestState.KNOWN);
        quest0001.handleAccept(s -> { });
        assertThat(quest0001.getCurrentState()).isEqualTo(QuestState.ACCEPTED);
        assertThatExceptionOfType(IllegalStateException.class).isThrownBy(quest0001::know);
        assertThatExceptionOfType(IllegalStateException.class).isThrownBy(quest0001::accept);
        gameData.getInventory().autoSetItem(InventoryDatabase.getInstance().createInventoryItem("gemstone", 5));
        gameData.getInventory().autoSetItem(InventoryDatabase.getInstance().createInventoryItem("herb", 3));
        quest0001.handleReward(s -> { }, l -> { }, null);
        assertThat(quest0001.getCurrentState()).isEqualTo(QuestState.UNCLAIMED);
        assertThatExceptionOfType(IllegalStateException.class).isThrownBy(quest0001::unclaim);
        gameData.getLoot().getLoot("quest0001").clearContent();
        quest0001.handleReward(s -> { }, l -> { }, s -> { });
        assertThat(quest0001.getCurrentState()).isEqualTo(QuestState.FINISHED);
        assertThatExceptionOfType(IllegalStateException.class).isThrownBy(quest0001::finish);
    }

    @Test
    void whenPartyGainsEnoughXp_ShouldShowMessageThatMemberGainedLevel() {
        final GameData gameData = Utils.getGameData();
        gameData.onNotifyCreateProfile(new ProfileManager());
        gameData.getInventory().autoSetItem(InventoryDatabase.getInstance().createInventoryItem("gemstone", 5));
        gameData.getInventory().autoSetItem(InventoryDatabase.getInstance().createInventoryItem("herb", 3));
        gameData.getParty().getHero(0).gainXp(19, new StringBuilder());
        QuestGraph quest0001 = quests.getQuestById("quest0001");
        quest0001.handleAccept(s -> { });
        quest0001.handleReturn(s -> { });
        gameData.getLoot().getLoot("quest0001").clearContent();
        quest0001.handleReward(s -> assertThat(s).isEqualTo("Mozes gained a level!"), l -> { }, s -> { });
    }
    //@formatter:on

    @Test
    void whenSubTaskIsCheckOrDiscover_ShouldSucceedToSetTaskComplete() {
        QuestGraph quest0003 = quests.getQuestById("quest0003");
        quest0003.setTaskComplete("1");
        quest0003.setTaskComplete("2");
        quest0003.handleReturn(s -> assertThat(s).isEqualTo(Constant.PHRASE_ID_QUEST_SUCCESS));
    }

}
