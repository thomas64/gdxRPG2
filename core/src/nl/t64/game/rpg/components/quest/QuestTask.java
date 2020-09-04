package nl.t64.game.rpg.components.quest;

import com.badlogic.gdx.utils.GdxRuntimeException;
import lombok.Getter;
import nl.t64.game.rpg.Utils;

import java.util.Collections;
import java.util.Map;


@Getter
class QuestTask {

    private String taskPhrase;
    QuestType type;
    private Map<String, Integer> target;
    private boolean isComplete;

    private QuestTask() {
        this.target = Collections.emptyMap();
        this.isComplete = false;
    }

    public String toString() {
        return taskPhrase;
    }

    void setTaskComplete() {
        if (type.equals(QuestType.DISCOVER)
            || type.equals(QuestType.CHECK)
            || type.equals(QuestType.MESSAGE_DELIVERY)) {
            isComplete = true;
        } else {
            throw new IllegalCallerException("Only possible to complete a DISCOVER or CHECK task.");
        }
    }

    void removeTargetFromInventory() {
        Utils.getGameData().getInventory().autoRemoveResource(getTargetEntry().getKey(),
                                                              getTargetEntry().getValue());
    }

    boolean isComplete() {
        return switch (type) {
            case RETURN -> checkReturn();
            case FETCH -> checkFetch();
            case DISCOVER, CHECK, MESSAGE_DELIVERY -> checkTaskComplete();
            default -> throw new GdxRuntimeException(String.format("No %s task for now.", type));
        };
    }

    private boolean checkReturn() {
        return true;
    }

    private boolean checkFetch() {
        return Utils.getGameData().getInventory().hasEnoughOfResource(getTargetEntry().getKey(),
                                                                      getTargetEntry().getValue());
    }

    private boolean checkTaskComplete() {
        return isComplete;
    }

    private Map.Entry<String, Integer> getTargetEntry() {
        return target.entrySet().iterator().next();
    }

}
