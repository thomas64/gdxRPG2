package nl.t64.game.rpg.components.quest;

import nl.t64.game.rpg.GameData;
import nl.t64.game.rpg.GameTest;
import nl.t64.game.rpg.Utils;
import nl.t64.game.rpg.components.loot.Loot;
import nl.t64.game.rpg.components.loot.LootContainer;
import nl.t64.game.rpg.components.party.InventoryContainer;
import nl.t64.game.rpg.components.party.InventoryDatabase;
import nl.t64.game.rpg.components.party.InventoryItem;
import nl.t64.game.rpg.components.party.PartyContainer;
import nl.t64.game.rpg.constants.Constant;
import nl.t64.game.rpg.profile.ProfileManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

import static org.assertj.core.api.Assertions.*;


class QuestTest extends GameTest {

    private QuestContainer quests;
    private InventoryContainer inventory;
    private PartyContainer party;
    private LootContainer loot;

    @BeforeEach
    private void setup() {
        final GameData gameData = Utils.getGameData();
        gameData.onNotifyCreateProfile(new ProfileManager());
        quests = gameData.getQuests();
        inventory = gameData.getInventory();
        party = gameData.getParty();
        loot = gameData.getLoot();
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
        assertThat(party.getHero(0).getXpToInvest()).isZero();
        QuestGraph quest0001 = quests.getQuestById("quest0001");
        assertThatExceptionOfType(IllegalCallerException.class).isThrownBy(() -> quest0001.setTaskComplete("1"));
        assertThat(quest0001.isFinished()).isFalse();
        quest0001.handleAccept(s -> assertThat(s).isEqualTo("New quest:"
                                                            + System.lineSeparator() + System.lineSeparator()
                                                            + "Herbs for boy"),
                               s -> assertThat(s).isEqualTo(Constant.PHRASE_ID_QUEST_ACCEPT));
        assertThat(quest0001.getCurrentState()).isEqualTo(QuestState.ACCEPTED);
        quest0001.handleReturn(s -> assertThat(s).isEqualTo(Constant.PHRASE_ID_QUEST_NO_SUCCESS));
        assertThatExceptionOfType(IllegalStateException.class)
                .isThrownBy(() -> quest0001.handleReward(s -> {}, null, null, null));
        inventory.autoSetItem(InventoryDatabase.getInstance().createInventoryItem("gemstone", 5));
        inventory.autoSetItem(InventoryDatabase.getInstance().createInventoryItem("herb", 3));
        quest0001.handleReturn(s -> assertThat(s).isEqualTo(Constant.PHRASE_ID_QUEST_SUCCESS));
        quest0001.handleReward(s -> assertThat(s).isIn("Quest completed:"
                                                       + System.lineSeparator() + System.lineSeparator()
                                                       + "Herbs for boy",
                                                       "+ 2 XP"),
                               s -> assertThat(s).isNull(),
                               (l, s) -> assertThat(l.isTaken()).isFalse(),
                               null);
        assertThat(quest0001.getCurrentState()).isEqualTo(QuestState.UNCLAIMED);
        loot.getLoot("quest0001").clearContent();
        quest0001.handleReward(s -> assertThat(s).isNull(),
                               s -> assertThat(s).isNull(),
                               (l, s) -> assertThat(l.isTaken()).isTrue(),
                               s -> assertThat(s).isEqualTo(Constant.PHRASE_ID_QUEST_FINISHED));
        assertThat(quest0001.getCurrentState()).isEqualTo(QuestState.FINISHED);
        assertThat(quest0001.isFinished()).isTrue();
        assertThat(party.getHero(0).getXpToInvest()).isEqualTo(2);
        assertThat(inventory.hasEnoughOfItem("gemstone", 1)).isFalse();
        assertThat(inventory.hasEnoughOfItem("herb", 1)).isFalse();
    }

    @Test
    void whenDemandsAreCompleteFromTheStart_ShouldHandleQuickFlow() {
        inventory.autoSetItem(InventoryDatabase.getInstance().createInventoryItem("gemstone", 5));
        inventory.autoSetItem(InventoryDatabase.getInstance().createInventoryItem("herb", 3));
        QuestGraph quest0001 = quests.getQuestById("quest0001");
        quest0001.handleAccept(s -> {}, s -> assertThat(s).isEqualTo(Constant.PHRASE_ID_QUEST_IMMEDIATE_SUCCESS));
    }

    @Test
    void whenQuestIsTolerated_ShouldNotHandleQuickFlow() {
        QuestGraph quest0002 = quests.getQuestById("quest0002");
        assertThat(quest0002.getCurrentState()).isEqualTo(QuestState.UNKNOWN);
        quest0002.handleTolerate(s -> assertThat(s).isEqualTo("New quest:"
                                                              + System.lineSeparator() + System.lineSeparator()
                                                              + "Get through the gatekeeper"));
        assertThat(quest0002.getCurrentState()).isEqualTo(QuestState.ACCEPTED);
    }

    @Test
    void whenSearchingForEdgeCases_ShouldThrowExceptions() {
        QuestGraph quest0001 = quests.getQuestById("quest0001");
        assertThat(quest0001.getCurrentState()).isEqualTo(QuestState.UNKNOWN);
        quest0001.know();
        assertThat(quest0001.getCurrentState()).isEqualTo(QuestState.KNOWN);
        quest0001.know();
        assertThat(quest0001.getCurrentState()).isEqualTo(QuestState.KNOWN);
        assertThatExceptionOfType(NoSuchElementException.class).isThrownBy(() -> quest0001.handleReceive(loot -> { }));
        quest0001.handleAccept(s -> {}, s -> {});
        assertThat(quest0001.getCurrentState()).isEqualTo(QuestState.ACCEPTED);
        assertThatExceptionOfType(IllegalStateException.class).isThrownBy(quest0001::know);
        assertThatExceptionOfType(IllegalStateException.class).isThrownBy(() -> quest0001.accept(s -> {}));
        inventory.autoSetItem(InventoryDatabase.getInstance().createInventoryItem("gemstone", 5));
        inventory.autoSetItem(InventoryDatabase.getInstance().createInventoryItem("herb", 3));
        quest0001.handleReward(s -> {}, s -> {}, (l, s) -> {}, null);
        assertThat(quest0001.getCurrentState()).isEqualTo(QuestState.UNCLAIMED);
        assertThatExceptionOfType(IllegalStateException.class).isThrownBy(quest0001::unclaim);
        loot.getLoot("quest0001").clearContent();
        quest0001.handleReward(s -> {}, s -> {}, (l, s) -> {}, s -> {});
        assertThat(quest0001.getCurrentState()).isEqualTo(QuestState.FINISHED);
        assertThatExceptionOfType(IllegalStateException.class).isThrownBy(quest0001::finish);
    }

    @Test
    void whenQuestReceiveItemQuestIsHandled_ShouldBeHandled() {
        QuestGraph quest0005 = quests.getQuestById("quest0005");
        quest0005.handleReceive(l -> { });
        assertThat(quest0005.getCurrentState()).isEqualTo(QuestState.KNOWN);

        quest0005.handleCheckIfAcceptedInventory("1", "xxx",
                                                 s -> assertThat(s).isNull(),
                                                 s -> assertThat(s).isEqualTo("xxx"));

        String targetId = quest0005.getTasks().get("1").getTarget().entrySet().iterator().next().getKey();
        InventoryItem targetItem = InventoryDatabase.getInstance().createInventoryItem(targetId);
        inventory.autoSetItem(targetItem);

        quest0005.handleCheckIfAcceptedInventory("1", "xxx",
                                                 s -> assertThat(s).isNull(),
                                                 s -> assertThat(s).isEqualTo("xxx"));

        quest0005.accept(s -> {});

        quest0005.handleCheckIfAcceptedInventory("1", "xxx",
                                                 s -> assertThat(s).isEqualTo(Constant.PHRASE_ID_QUEST_DELIVERY),
                                                 s -> assertThat(s).isNull());

        assertThat(inventory.contains(Map.of(targetId, 1))).isTrue();
        quest0005.setTaskComplete("1");
        assertThat(inventory.contains(Map.of(targetId, 1))).isFalse();
    }

    @Test
    void whenQuestReceiveMessageQuestIsHandled_ShouldBeHandled() {
        QuestGraph quest0004 = quests.getQuestById("quest0004");

        quest0004.handleCheckIfAccepted("xxx",
                                        s -> assertThat(s).isNull(),
                                        s -> assertThat(s).isEqualTo("xxx"));

        quest0004.handleAccept(s -> {}, s -> assertThat(s).isEqualTo(Constant.PHRASE_ID_QUEST_ACCEPT));

        quest0004.handleCheckIfAccepted("xxx",
                                        s -> assertThat(s).isEqualTo(Constant.PHRASE_ID_QUEST_DELIVERY),
                                        s -> assertThat(s).isNull());
    }

    @Test
    void whenPartyGainsEnoughXp_ShouldShowMessageThatMemberGainedLevel() {
        inventory.autoSetItem(InventoryDatabase.getInstance().createInventoryItem("gemstone", 5));
        inventory.autoSetItem(InventoryDatabase.getInstance().createInventoryItem("herb", 3));
        party.getHero(0).gainXp(19, new StringBuilder());
        QuestGraph quest0001 = quests.getQuestById("quest0001");
        Loot questLoot = loot.getLoot("quest0001");
        assertThat(questLoot.isXpGained()).isFalse();
        quest0001.handleAccept(s -> {}, s -> {});
        quest0001.handleReturn(s -> {});
        questLoot.clearContent();
        quest0001.handleReward(s -> assertThat(s).isIn("Quest completed:"
                                                       + System.lineSeparator() + System.lineSeparator()
                                                       + "Herbs for boy",
                                                       "+ 2 XP"),
                               s -> assertThat(s).isEqualTo("Mozes gained a level!"),
                               (l, s) -> {},
                               s -> {});
        assertThat(questLoot.isXpGained()).isTrue();
    }

    @Test
    void whenQuestFails_ShouldSetSubtasksAccordingly() {
        inventory.autoSetItem(InventoryDatabase.getInstance()
                                               .createInventoryItem("key_mysterious_tunnel", 1));

        QuestGraph quest0006 = quests.getQuestById("quest0006");
        assertThat(quest0006.getAllTasks()).extracting("isOptional").containsExactly(
                true, false, true, false);
        quest0006.handleAccept(s -> { }, s -> { });
        List<QuestTask> allTasks = quest0006.getAllTasks();
        assertThat(allTasks.get(0).isFailed()).isFalse();
        assertThat(allTasks.get(1).isFailed()).isFalse();
        assertThat(allTasks.get(2).isFailed()).isFalse();
        assertThat(allTasks.get(3).isFailed()).isFalse();
        quest0006.handleReward(s -> { }, null, (l, s) -> { }, null);
        assertThat(allTasks.get(0).isFailed()).isTrue();
        assertThat(allTasks.get(0)).hasToString("x  [Optional] Search for clues about the key");
        assertThat(allTasks.get(1).isFailed()).isFalse();
        assertThat(allTasks.get(2).isFailed()).isTrue();
        assertThat(allTasks.get(3).isFailed()).isFalse();
    }

    @Test
    void whenQuestTaskIsCheckOrDiscover_ShouldSucceedToSetTaskComplete() {
        QuestGraph quest0003 = quests.getQuestById("quest0003");
        quest0003.setTaskComplete("1");
        quest0003.setTaskComplete("2");
        quest0003.handleReturn(s -> assertThat(s).isEqualTo(Constant.PHRASE_ID_QUEST_SUCCESS));
    }

    @Test
    void whenQuestsAreNotUnknown_ShouldBecomeVisibleInQuestLogInCorrectOrder() {
        QuestGraph quest0001 = quests.getQuestById("quest0001");
        QuestGraph quest0003 = quests.getQuestById("quest0003");
        assertThat(quests.getAllKnownQuests()).isEmpty();
        quest0001.know();
        assertThat(quests.getAllKnownQuests()).extracting("id").containsExactly("quest0001");
        quest0003.know();
        quest0003.accept(s -> {});
        assertThat(quests.getAllKnownQuests()).extracting("id").containsExactly("quest0001", "quest0003");
        quest0001.accept(s -> {});
        quest0001.unclaim();
        assertThat(quests.getAllKnownQuests()).extracting("id").containsExactly("quest0003", "quest0001");
        quest0003.handleFail(s -> { });
        assertThat(quests.getAllKnownQuests()).extracting("id").containsExactly("quest0001", "quest0003");
    }

    @Test
    void whenStatesOfQuestChanges_ShouldChangeToString() {
        QuestGraph quest0001 = quests.getQuestById("quest0001");
        assertThat(quest0001).hasToString("     Herbs for boy");
        quest0001.know();
        assertThat(quest0001).hasToString("     Herbs for boy");
        quest0001.accept(s -> {});
        assertThat(quest0001).hasToString("     Herbs for boy");
        quest0001.unclaim();
        assertThat(quest0001).hasToString("o   Herbs for boy");
        quest0001.finish();
        assertThat(quest0001).hasToString("v  Herbs for boy");

        assertThat(quest0001.isFailed()).isFalse();
        quest0001.handleFail(s -> assertThat(s).isEqualTo("Quest failed:"
                                                          + System.lineSeparator() + System.lineSeparator()
                                                          + "Herbs for boy"));
        assertThat(quest0001).hasToString("x  Herbs for boy");
        assertThat(quest0001.isFailed()).isTrue();
    }

    @Test
    void whenQuestTaskIsCompleted_ShouldChangeToString() {
        QuestGraph quest0001 = quests.getQuestById("quest0001");
        assertThat(quest0001.getTasks().get("1").isQuestFinished()).isFalse();
        assertThat(quest0001.getTasks().get("1")).hasToString("     Collect 3 herbs");

        inventory.autoSetItem(InventoryDatabase.getInstance().createInventoryItem("herb", 3));
        assertThat(quest0001.getTasks().get("1")).hasToString("v  Collect 3 herbs");

        inventory.autoRemoveItem("herb", 3);
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

    @Test
    void whenTwoQuestsAreLinked_ShouldChangeConversationIfLinkedQuestIsKnown() {
        QuestGraph quest0006 = quests.getQuestById("quest0006");
        QuestGraph quest0007 = quests.getQuestById("quest0007");
        assertThat(quest0006.getLinkedWith()).isEqualTo("quest0007");
        assertThat(quest0007.getLinkedWith()).isEqualTo("quest0006");

        quest0006.handleCheckIfLinkedIsKnown("xxx",
                                             s -> assertThat(s).isEqualTo("xxx"));
        quest0007.know();
        quest0006.handleCheckIfLinkedIsKnown("xxx",
                                             s -> assertThat(s).isEqualTo(Constant.PHRASE_ID_QUEST_LINKED));
    }

    @Test
    void whenQuestTaskWithMessageIsCreated_ShouldShowThatMessage() {
        String message = "(No tasks visible until this quest is accepted)";
        var questTask = new QuestTask(message);
        assertThat(questTask.getType()).isEqualTo(QuestType.NONE);
        assertThat(questTask).hasToString(System.lineSeparator() + System.lineSeparator()
                                          + System.lineSeparator() + message);
    }

}
