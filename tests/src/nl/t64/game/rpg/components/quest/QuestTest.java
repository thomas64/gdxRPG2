package nl.t64.game.rpg.components.quest;

import nl.t64.game.rpg.GameData;
import nl.t64.game.rpg.GameTest;
import nl.t64.game.rpg.Utils;
import nl.t64.game.rpg.components.party.InventoryDatabase;
import nl.t64.game.rpg.components.party.InventoryItem;
import nl.t64.game.rpg.constants.Constant;
import nl.t64.game.rpg.constants.QuestState;
import nl.t64.game.rpg.profile.ProfileManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Map;
import java.util.NoSuchElementException;

import static org.assertj.core.api.Assertions.*;


class QuestTest extends GameTest {

    private QuestContainer quests;

    @BeforeEach
    private void setup() {
        quests = new QuestContainer();
    }

    @Test
    void whenQuestsAreCreated_ShouldHaveAllQuestsStartedAtStateUnknown() {
        assertThat(quests.getQuestById("quest0001").getCurrentState()).isEqualTo(QuestState.UNKNOWN);
    }

    @Test
    void whenQuestGraphIsLoaded_ShouldHaveCorrectData() {
        QuestGraph quest0001 = quests.getQuestById("quest0001");
        assertThat(quest0001.getId()).isEqualTo("quest0001");
        assertThat(quest0001.getCurrentState()).isEqualTo(QuestState.UNKNOWN);
        assertThat(quest0001.getTitle()).isEqualTo("Herbs for boy");
        assertThat(quest0001.getCharacter()).isEqualTo("boy01");
        assertThat(quest0001.getSummary()).contains("Johnny's mother is ill and ");
        assertThat(quest0001).hasToString("     Herbs for boy");
        Map<String, QuestTask> tasks = quest0001.getTasks();
        assertThat(tasks).hasSize(3);
        assertThat(tasks.get("1").getTaskPhrase()).isEqualTo("Collect 3 herbs");
        assertThat(tasks.get("1").getType()).isEqualTo(QuestType.FETCH);
        assertThat(tasks.get("1").getTarget()).containsOnly(entry("herb", 3));
        assertThat(tasks.get("1")).hasToString("     Collect 3 herbs");
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
        assertThat(gameData.getInventory().hasEnoughOfItem("gemstone", 1)).isFalse();
        assertThat(gameData.getInventory().hasEnoughOfItem("herb", 1)).isFalse();
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

    @Test
    void whenQuestIsTolerated_ShouldNotHandleQuickFlow() {
        QuestGraph quest0002 = quests.getQuestById("quest0002");
        assertThat(quest0002.getCurrentState()).isEqualTo(QuestState.UNKNOWN);
        quest0002.handleTolerate(s -> assertThat(s).isEqualTo(Constant.PHRASE_ID_QUEST_TOLERATE));
        assertThat(quest0002.getCurrentState()).isEqualTo(QuestState.ACCEPTED);
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
        assertThatExceptionOfType(NoSuchElementException.class).isThrownBy(() -> quest0001.handleReceive(loot -> { }));
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
    void whenQuestReceiveItemQuestIsHandled_ShouldBeHandled() {
        final GameData gameData = Utils.getGameData();
        gameData.onNotifyCreateProfile(new ProfileManager());

        QuestGraph quest0005 = quests.getQuestById("quest0005");
        quest0005.handleReceive(l -> { });
        assertThat(quest0005.getCurrentState()).isEqualTo(QuestState.KNOWN);

        quest0005.handleInventory("1", "xxx",
                                  s -> assertThat(s).isNull(),
                                  s -> assertThat(s).isEqualTo("xxx"));

        String targetId = quest0005.getTasks().get("1").getTarget().entrySet().iterator().next().getKey();
        InventoryItem targetItem = InventoryDatabase.getInstance().createInventoryItem(targetId);
        Utils.getGameData().getInventory().autoSetItem(targetItem);

        quest0005.handleInventory("1", "xxx",
                                  s -> assertThat(s).isNull(),
                                  s -> assertThat(s).isEqualTo("xxx"));

        quest0005.accept();

        quest0005.handleInventory("1", "xxx",
                                  s -> assertThat(s).isEqualTo(Constant.PHRASE_ID_QUEST_DELIVERY),
                                  s -> assertThat(s).isNull());

        assertThat(Utils.getGameData().getInventory().contains(Map.of(targetId, 1))).isTrue();
        quest0005.setTaskComplete("1");
        assertThat(Utils.getGameData().getInventory().contains(Map.of(targetId, 1))).isFalse();
    }

    @Test
    void whenQuestReceiveMessageQuestIsHandled_ShouldBeHandled() {
        QuestGraph quest0004 = quests.getQuestById("quest0004");

        quest0004.handleCheck("xxx",
                              s -> assertThat(s).isNull(),
                              s -> assertThat(s).isEqualTo("xxx"));

        quest0004.handleAccept(s -> assertThat(s).isEqualTo(Constant.PHRASE_ID_QUEST_ACCEPT));

        quest0004.handleCheck("xxx",
                              s -> assertThat(s).isEqualTo(Constant.PHRASE_ID_QUEST_DELIVERY),
                              s -> assertThat(s).isNull());
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
    void whenQuestTaskIsCheckOrDiscover_ShouldSucceedToSetTaskComplete() {
        QuestGraph quest0003 = quests.getQuestById("quest0003");
        quest0003.setTaskComplete("1");
        quest0003.setTaskComplete("2");
        quest0003.handleReturn(s -> assertThat(s).isEqualTo(Constant.PHRASE_ID_QUEST_SUCCESS));
    }

    @Test
    void whenQuestsAreNotUnknown_ShouldBecomeVisibleInQuestLogInCorrectOrder() {
        assertThat(quests.getAllKnownQuests()).isEmpty();
        quests.getQuestById("quest0001").know();
        assertThat(quests.getAllKnownQuests()).extracting("id").containsExactly("quest0001");
        quests.getQuestById("quest0003").know();
        quests.getQuestById("quest0003").accept();
        assertThat(quests.getAllKnownQuests()).extracting("id").containsExactly("quest0001", "quest0003");
        quests.getQuestById("quest0001").accept();
        quests.getQuestById("quest0001").unclaim();
        assertThat(quests.getAllKnownQuests()).extracting("id").containsExactly("quest0003", "quest0001");
    }

    @Test
    void whenStatesOfQuestChanges_ShouldChangeToString() {
        QuestGraph quest0001 = quests.getQuestById("quest0001");
        assertThat(quest0001).hasToString("     Herbs for boy");
        quest0001.know();
        assertThat(quest0001).hasToString("     Herbs for boy");
        quest0001.accept();
        assertThat(quest0001).hasToString("     Herbs for boy");
        quest0001.unclaim();
        assertThat(quest0001).hasToString("o   Herbs for boy");
        quest0001.finish();
        assertThat(quest0001).hasToString("v  Herbs for boy");
    }

    @Test
    void whenQuestTaskIsCompleted_ShouldChangeToString() {
        final GameData gameData = Utils.getGameData();
        gameData.onNotifyCreateProfile(new ProfileManager());

        QuestGraph quest0001 = quests.getQuestById("quest0001");
        assertThat(quest0001.getTasks().get("1").isQuestFinished()).isFalse();
        assertThat(quest0001.getTasks().get("1")).hasToString("     Collect 3 herbs");

        gameData.getInventory().autoSetItem(InventoryDatabase.getInstance().createInventoryItem("herb", 3));
        assertThat(quest0001.getTasks().get("1")).hasToString("v  Collect 3 herbs");

        gameData.getInventory().autoRemoveItem("herb", 3);
        assertThat(quest0001.getTasks().get("1")).hasToString("     Collect 3 herbs");

        quest0001.getTasks().get("1").forceFinished();
        assertThat(quest0001.getTasks().get("1")).hasToString("v  Collect 3 herbs");

        assertThat(quest0001.getTasks().get("3")).hasToString("     Give them to Johnny at Starter Path");
        assertThat(quest0001.getTasks().get("1").isQuestFinished()).isTrue();
    }

    @Test
    void whenQuestIsShownInQuestLog_ShouldShowTasksInCorrectOrder() {
        QuestGraph quest0001 = quests.getQuestById("quest0001");
        assertThat(quest0001.getAllTasks()).extracting("taskPhrase").containsExactly(
                "Collect 3 herbs", "Collect 5 gemstones for the programmer", "Give them to Johnny at Starter Path");
    }

    @Test
    void whenSetCompletableTaskIsSetComplete_ShouldBeComplete() {
        QuestGraph quest0003 = quests.getQuestById("quest0003");
        assertThat(quest0003.isTaskComplete("1")).isFalse();
        quest0003.setTaskComplete("1");
        assertThat(quest0003.isTaskComplete("1")).isTrue();
    }

}
