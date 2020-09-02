package nl.t64.game.rpg.components.quest;

import lombok.Getter;
import nl.t64.game.rpg.Utils;
import nl.t64.game.rpg.components.loot.Loot;
import nl.t64.game.rpg.constants.Constant;

import java.util.List;
import java.util.Map;
import java.util.function.Consumer;


@Getter
public class QuestGraph {

    private static final QuestState DEFAULT_STATE = QuestState.UNKNOWN;

    String id;
    QuestState currentState;
    private String title;
    private Map<String, QuestTask> tasks;

    private QuestGraph() {
        this.currentState = DEFAULT_STATE;
    }

    public String toString() {
        return title;
    }

    public void setTaskComplete(String taskId) {
        tasks.get(taskId).setTaskComplete();
    }

    public boolean isTaskComplete(String taskId) {
        return false; // todo, yet to implement a quest with a subtask which will change the game map.
    }

    public boolean isFinished() {
        return currentState.equals(QuestState.FINISHED);
    }

    public void handleAccept(Consumer<String> continueConversation) {
        know();
        accept();
        // todo, update logbook
        if (doesReturnMeetDemand()) {
            continueConversation.accept(Constant.PHRASE_ID_QUEST_IMMEDIATE_SUCCESS);
        } else {
            continueConversation.accept(Constant.PHRASE_ID_QUEST_ACCEPT);
        }
    }

    public void handleReturn(Consumer<String> continueConversation) {
        if (doesReturnMeetDemand()) {
            continueConversation.accept(Constant.PHRASE_ID_QUEST_SUCCESS);
        } else {
            continueConversation.accept(Constant.PHRASE_ID_QUEST_FAILURE);
        }
    }

    public void handleReward(Consumer<String> notifyShowMessageDialog,
                             Consumer<Loot> notifyShowRewardDialog,
                             Consumer<String> endConversation) {
        Loot reward = Utils.getGameData().getLoot().getLoot(id);
        if (currentState.equals(QuestState.ACCEPTED)) {
            takeDemands();
            unclaim();
            partyGainXp(reward, notifyShowMessageDialog);
            // todo, set all tasks to complete indefinite
        }
        if (!reward.isTaken()) {
            notifyShowRewardDialog.accept(reward);
        } else {
            finish();
            endConversation.accept(Constant.PHRASE_ID_QUEST_FINISHED);
        }
    }

    public void know() {
        switch (currentState) {
            case KNOWN:
                break;
            case UNKNOWN:
                currentState = QuestState.KNOWN;
                break;
            default:
                throw new IllegalStateException("Only quest UNKNOWN can be KNOWN.");
        }
    }

    void accept() {
        if (currentState.equals(QuestState.KNOWN)) {
            currentState = QuestState.ACCEPTED;
        } else {
            throw new IllegalStateException("Only quest KNOWN can be ACCEPTED.");
        }
    }

    void unclaim() {
        if (currentState.equals(QuestState.ACCEPTED)) {
            currentState = QuestState.UNCLAIMED;
        } else {
            throw new IllegalStateException("Only quest ACCEPTED can be UNCLAIMED.");
        }
    }

    public void finish() {
        if (currentState.equals(QuestState.UNCLAIMED)) {
            currentState = QuestState.FINISHED;
        } else {
            throw new IllegalStateException("Only quest UNCLAIMED can be FINISHED.");
        }
    }

    private void takeDemands() {
        getAllQuestTasks().stream()
                          .filter(questTask -> questTask.type.equals(QuestType.FETCH))
                          .forEach(QuestTask::removeTargetFromInventory);
    }

    private void partyGainXp(Loot reward, Consumer<String> notifyShowMessageDialog) {
        StringBuilder levelUpMessage = new StringBuilder();
        Utils.getGameData().getParty().getAllHeroes().forEach(hero -> hero.gainXp(reward.getXp(), levelUpMessage));
        reward.clearXp();
        String finalMessage = levelUpMessage.toString().strip();
        if (!finalMessage.isEmpty()) {
            notifyShowMessageDialog.accept(finalMessage);
        }
    }

    private boolean doesReturnMeetDemand() {
        return getAllQuestTasks().stream().allMatch(QuestTask::isComplete);
    }

    private List<QuestTask> getAllQuestTasks() {
        return List.copyOf(tasks.values());
    }

}
