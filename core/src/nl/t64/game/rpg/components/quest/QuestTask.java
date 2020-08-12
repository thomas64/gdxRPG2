package nl.t64.game.rpg.components.quest;

import com.badlogic.gdx.utils.GdxRuntimeException;
import lombok.Getter;
import nl.t64.game.rpg.Utils;

import java.util.Collections;
import java.util.Map;


@Getter
public class QuestTask {

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

    void setLocationDiscovered() {
        isComplete = true;
    }

    void removeTargetFromInventory() {
        Utils.getGameData().getInventory().autoRemoveResource(getTargetEntry().getKey(),
                                                              getTargetEntry().getValue());
    }

    boolean isComplete() {
        return switch (type) {
            case RETURN -> checkReturn();
            case FETCH -> checkFetch();
            case DISCOVER -> checkDiscover();
            default -> throw new GdxRuntimeException(String.format("No %s quests for now.", type));
        };
    }

    private boolean checkReturn() {
        return true;
    }

    private boolean checkFetch() {
        return Utils.getGameData().getInventory().hasEnoughOfResource(getTargetEntry().getKey(),
                                                                      getTargetEntry().getValue());
    }

    private boolean checkDiscover() {
        return isComplete;
    }

    private Map.Entry<String, Integer> getTargetEntry() {
        return target.entrySet().iterator().next();
    }

}
