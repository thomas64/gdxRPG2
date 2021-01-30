package nl.t64.game.rpg.components.quest;

import com.badlogic.gdx.utils.GdxRuntimeException;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import nl.t64.game.rpg.Utils;

import java.util.Map;


@Getter
public class QuestTask {

    QuestType type;
    private String taskPhrase;
    private Map<String, Integer> target;
    @JsonProperty("isOptional")
    private boolean isOptional;
    private boolean isComplete;
    private boolean isFailed;
    private boolean isQuestFinished;

    public QuestTask(String taskPhrase) {
        this();
        this.type = QuestType.NONE;
        this.taskPhrase = taskPhrase;
    }

    private QuestTask() {
        this.target = Map.of();
        this.isOptional = false;
        this.isComplete = false;
        this.isFailed = false;
        this.isQuestFinished = false;
    }

    public String toString() {
        if (type.equals(QuestType.NONE)) {
            return System.lineSeparator() + System.lineSeparator() + System.lineSeparator() + taskPhrase;
        } else if (isFailed) {
            return "x  " + taskPhrase;
        } else if (isQuestFinished) {
            return "v  " + taskPhrase;
        } else if (type.equals(QuestType.RETURN)) {
            return "     " + taskPhrase;
        } else if (isCompleteForReturn()) {
            return "v  " + taskPhrase;
        } else {
            return "     " + taskPhrase;
        }
    }

    void forceFinished() {
        isQuestFinished = true;
        if (isOptional && !isComplete) {
            isFailed = true;
        }
    }

    void setComplete() {
        switch (type) {
            case ITEM_DELIVERY -> {
                target.forEach((itemId, amount) -> Utils.getGameData().getInventory().autoRemoveItem(itemId, amount));
                isComplete = true;
            }
            case DISCOVER, MESSAGE_DELIVERY -> isComplete = true;
            case CHECK -> setCheckTypePossibleComplete();
            default -> throw new IllegalCallerException("Only possible to complete a DISCOVER, CHECK or DELIVERY task.");
        }
    }

    private void setCheckTypePossibleComplete() {
        if (target.isEmpty()) {
            isComplete = true;
            return;
        }

        if (doesInventoryContainsTarget()) {
            isComplete = true;
        }
    }

    void removeTargetFromInventory() {
        Utils.getGameData().getInventory().autoRemoveItem(getTargetEntry().getKey(),
                                                          getTargetEntry().getValue());
    }

    boolean hasTargetInInventory() {
        return Utils.getGameData().getInventory().contains(target);
    }

    boolean isCompleteForReturn() {
        return switch (type) {
            case RETURN -> true;
            case FETCH -> doesInventoryContainsTarget();
            case DISCOVER, CHECK, MESSAGE_DELIVERY, ITEM_DELIVERY -> isComplete;
            default -> throw new GdxRuntimeException(String.format("No %s task for now.", type));
        };
    }

    private boolean doesInventoryContainsTarget() {
        return Utils.getGameData().getInventory().hasEnoughOfItem(getTargetEntry().getKey(),
                                                                  getTargetEntry().getValue());
    }

    private Map.Entry<String, Integer> getTargetEntry() {
        return target.entrySet().iterator().next();
    }

}
