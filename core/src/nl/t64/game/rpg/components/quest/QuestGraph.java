package nl.t64.game.rpg.components.quest;

import lombok.Getter;
import nl.t64.game.rpg.Utils;
import nl.t64.game.rpg.components.loot.Loot;
import nl.t64.game.rpg.constants.Constant;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.stream.Collectors;


@Getter
public class QuestGraph {

    private static final QuestState DEFAULT_STATE = QuestState.UNKNOWN;

    String id;
    QuestState currentState;
    boolean isFailed;
    private String title;
    private String character;
    private String summary;
    private String linkedWith;
    private Map<String, QuestTask> tasks;

    private QuestGraph() {
        this.currentState = DEFAULT_STATE;
        this.isFailed = false;
    }

    public String toString() {
        if (isFailed) {
            return "x  " + title;
        } else if (currentState.equals(QuestState.FINISHED)) {
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
                    .collect(Collectors.toUnmodifiableList());
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

    public void handleAccept(Consumer<String> notifyShowMessageTooltip, Consumer<String> continueConversation) {
        know();
        accept(notifyShowMessageTooltip);
        if (doesReturnMeetDemand()) {
            continueConversation.accept(Constant.PHRASE_ID_QUEST_IMMEDIATE_SUCCESS);
        } else {
            continueConversation.accept(Constant.PHRASE_ID_QUEST_ACCEPT);
        }
    }

    public void handleTolerate(Consumer<String> notifyShowMessageTooltip) {
        know();
        accept(notifyShowMessageTooltip);
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

    public void handleCheckIfLinkedIsKnown(String phraseId, Consumer<String> continueConversation) {
        if (linkedWith != null
            && !Utils.getGameData().getQuests().getQuestById(linkedWith).getCurrentState().equals(QuestState.UNKNOWN)) {
            continueConversation.accept(Constant.PHRASE_ID_QUEST_LINKED);
        } else {
            continueConversation.accept(phraseId);
        }
    }

    public void handleCheckIfAccepted(String phraseId,
                                      Consumer<String> continueConversation, Consumer<String> endConversation) {
        if (currentState.equals(QuestState.ACCEPTED)) {
            continueConversation.accept(Constant.PHRASE_ID_QUEST_DELIVERY);
        } else {
            endConversation.accept(phraseId);
        }
    }

    public void handleCheckIfAcceptedInventory(String taskId, String phraseId,
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
            continueConversation.accept(Constant.PHRASE_ID_QUEST_NO_SUCCESS);
        }
    }

    public void handleReward(Consumer<String> notifyShowMessageTooltip,
                             Consumer<String> notifyShowLevelUpDialog,
                             BiConsumer<Loot, String> notifyShowRewardDialog,
                             Consumer<String> endConversation) {
        if (currentState.equals(QuestState.ACCEPTED)) {
            takeDemands();
            unclaim();
            getAllQuestTasks().forEach(QuestTask::forceFinished);
            notifyShowMessageTooltip.accept("Quest completed:"
                                            + System.lineSeparator() + System.lineSeparator() + title);
        }
        Loot reward = Utils.getGameData().getLoot().getLoot(id);
        reward.removeBonus();
        Optional<String> levelUpMessage = partyGainXp(reward, notifyShowMessageTooltip);
        if (reward.isTaken()) {
            finish();
            endConversation.accept(Constant.PHRASE_ID_QUEST_FINISHED);
            levelUpMessage.ifPresent(notifyShowLevelUpDialog);
        } else {
            notifyShowRewardDialog.accept(reward, levelUpMessage.orElse(null));
        }
    }

    public void handleFail(Consumer<String> notifyShowMessageTooltip) {
        isFailed = true;
        notifyShowMessageTooltip.accept("Quest failed:" + System.lineSeparator() + System.lineSeparator() + title);
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

    public void accept(Consumer<String> notifyShowMessageTooltip) {
        if (currentState.equals(QuestState.KNOWN)) {
            currentState = QuestState.ACCEPTED;
            notifyShowMessageTooltip.accept("New quest:" + System.lineSeparator() + System.lineSeparator() + title);
        } else {
            throw new IllegalStateException("Only quest KNOWN can be ACCEPTED.");
        }
    }

    public void unclaim() {
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

    private Optional<String> partyGainXp(Loot reward, Consumer<String> notifyShowMessageTooltip) {
        if (reward.isXpGained()) {
            return Optional.empty();
        }
        StringBuilder levelUpMessage = new StringBuilder();
        Utils.getGameData().getParty().getAllHeroes().forEach(hero -> hero.gainXp(reward.getXp(), levelUpMessage));
        notifyShowMessageTooltip.accept(String.format("+ %s XP", reward.getXp()));
        reward.clearXp();
        String finalMessage = levelUpMessage.toString().strip();
        if (finalMessage.isEmpty()) {
            return Optional.empty();
        } else {
            return Optional.of(finalMessage);
        }
    }

    private boolean doesReturnMeetDemand() {
        return getAllQuestTasks().stream()
                                 .filter(task -> !task.isOptional())
                                 .allMatch(QuestTask::isCompleteForReturn);
    }

    private List<QuestTask> getAllQuestTasks() {
        return List.copyOf(tasks.values());
    }

}
