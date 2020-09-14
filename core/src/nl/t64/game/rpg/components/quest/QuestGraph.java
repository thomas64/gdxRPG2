package nl.t64.game.rpg.components.quest;

import lombok.Getter;
import nl.t64.game.rpg.Utils;
import nl.t64.game.rpg.components.loot.Loot;
import nl.t64.game.rpg.constants.Constant;
import nl.t64.game.rpg.constants.QuestState;

import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.stream.Collectors;


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
        if (currentState.equals(QuestState.FINISHED)) {
            return "v  " + title;
        } else if (currentState.equals(QuestState.UNCLAIMED)) {
            return "o   " + title;
        } else {
            return "     " + title;
        }
    }

    public List<QuestTask> getAllTasks() {
        return tasks.entrySet().stream()
                    .sorted(Map.Entry.comparingByKey())
                    .map(Map.Entry::getValue)
                    .collect(Collectors.toList());
    }

    public void setTaskComplete(String taskId) {
        tasks.get(taskId).setComplete();
    }

    public boolean isTaskComplete(String taskId) {
        return tasks.get(taskId).isComplete();
    }

    public boolean isFinished() {
        return currentState.equals(QuestState.FINISHED);
    }

    public void handleAccept(Consumer<String> continueConversation) {
        know();
        accept();
        if (doesReturnMeetDemand()) {
            continueConversation.accept(Constant.PHRASE_ID_QUEST_IMMEDIATE_SUCCESS);
        } else {
            continueConversation.accept(Constant.PHRASE_ID_QUEST_ACCEPT);
        }
    }

    public void handleReceive(Consumer<Loot> notifyShowReceiveDialog) {
        know();
        Loot receive = getAllQuestTasks().stream()
                                         .filter(questTask -> questTask.type.equals(QuestType.ITEM_DELIVERY))
                                         .findFirst()
                                         .map(questTask -> new Loot(questTask.getTarget()))
                                         .orElseThrow();
        notifyShowReceiveDialog.accept(receive);
    }

    public void handleCheck(String phraseId, Consumer<String> continueConversation, Consumer<String> endConversation) {
        if (currentState.equals(QuestState.ACCEPTED)) {
            continueConversation.accept(Constant.PHRASE_ID_QUEST_DELIVERY);
        } else {
            endConversation.accept(phraseId);
        }
    }

    public void handleInventory(String taskId, String phraseId,
                                Consumer<String> continueConversation, Consumer<String> endConversation) {
        if (currentState.equals(QuestState.ACCEPTED)
            && tasks.get(taskId).hasTargetInInventory()) {
            continueConversation.accept(Constant.PHRASE_ID_QUEST_DELIVERY);
        } else {
            endConversation.accept(phraseId);
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
            getAllQuestTasks().forEach(QuestTask::forceFinished);
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

    public void accept() {
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
        return getAllQuestTasks().stream().allMatch(QuestTask::isCompleteForReturn);
    }

    private List<QuestTask> getAllQuestTasks() {
        return List.copyOf(tasks.values());
    }

}
