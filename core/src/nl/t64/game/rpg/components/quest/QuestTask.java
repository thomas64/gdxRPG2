package nl.t64.game.rpg.components.quest;

import com.badlogic.gdx.utils.GdxRuntimeException;
import lombok.Getter;
import nl.t64.game.rpg.Utils;

import java.util.Collections;
import java.util.Map;


@Getter
public class QuestTask {

    QuestType type;
    private String taskPhrase;
    private Map<String, Integer> target;
    private boolean isComplete;
    private boolean isQuestFinished;

    private QuestTask() {
        this.target = Collections.emptyMap();
        this.isComplete = false;
    }

    public String toString() {
        if (isQuestFinished) {
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
    }

    void setComplete() {
        switch (type) {
            case ITEM_DELIVERY -> {
                target.forEach((itemId, amount) -> Utils.getGameData().getInventory().autoRemoveItem(itemId, amount));
                isComplete = true;
            }
            case DISCOVER, CHECK, MESSAGE_DELIVERY -> isComplete = true;
            default -> throw new IllegalCallerException("Only possible to complete a DISCOVER, CHECK or DELIVERY task.");
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
            case RETURN -> checkReturn();
            case FETCH -> checkFetch();
            case DISCOVER, CHECK, MESSAGE_DELIVERY, ITEM_DELIVERY -> checkComplete();
            default -> throw new GdxRuntimeException(String.format("No %s task for now.", type));
        };
    }

    private boolean checkReturn() {
        return true;
    }

    private boolean checkFetch() {
        return Utils.getGameData().getInventory().hasEnoughOfItem(getTargetEntry().getKey(),
                                                                  getTargetEntry().getValue());
    }

    private boolean checkComplete() {
        return isComplete;
    }

    private Map.Entry<String, Integer> getTargetEntry() {
        return target.entrySet().iterator().next();
    }

}
