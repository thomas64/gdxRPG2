package nl.t64.game.rpg.components.quest;

import lombok.Getter;
import nl.t64.game.rpg.constants.Constant;

import java.util.List;
import java.util.Map;
import java.util.function.Consumer;


@Getter
public class QuestGraph {

    private static final QuestState DEFAULT_STATE = QuestState.UNKNOWN;

    QuestState currentState;
    private String title;
    private Map<String, QuestTask> tasks;

    private QuestGraph() {
        this.currentState = DEFAULT_STATE;
    }

    public String toString() {
        return title;
    }

    public boolean isFinished() {
        return currentState.equals(QuestState.FINISHED);
    }

    public void tryToFulfill(Consumer<String> continueConversation) {
        if (doesMeetDemand()) {
            continueConversation.accept(Constant.PHRASE_ID_QUEST_SUCCESS);
        } else {
            continueConversation.accept(Constant.PHRASE_ID_QUEST_FAILURE);
        }
    }

    public void takeDemands() {
        getAllQuestTasks().stream()
                          .filter(questTask -> questTask.type.equals(QuestType.FETCH))
                          .forEach(QuestTask::removeTargetFromInventory);
    }

    public void accept() {
        if (currentState.equals(QuestState.UNKNOWN)) {
            currentState = QuestState.ACCEPTED;
        } else {
            throw new IllegalStateException("Only quest UNKNOWN can be ACCEPTED.");
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

    private boolean doesMeetDemand() {
        return getAllQuestTasks().stream().allMatch(QuestTask::isComplete);
    }

    private List<QuestTask> getAllQuestTasks() {
        return List.copyOf(tasks.values());
    }

}
