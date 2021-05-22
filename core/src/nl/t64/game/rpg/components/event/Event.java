package nl.t64.game.rpg.components.event;

import com.badlogic.gdx.utils.GdxRuntimeException;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import nl.t64.game.rpg.Utils;
import nl.t64.game.rpg.components.condition.ConditionDatabase;

import java.util.List;


@Getter
public class Event {

    private boolean hasPlayed;
    private String type;
    @JsonProperty(value = "condition")
    private List<String> conditionIds;
    private String conversationId;
    private String entityId;
    private String text;

    private Event() {
        this.hasPlayed = false;
        this.conditionIds = List.of();
    }

    public void possibleStart() {
        if (!hasPlayed && isMeetingCondition()) {
            hasPlayed = true;
            start();
        }
    }

    private boolean isMeetingCondition() {
        return ConditionDatabase.getInstance().isMeetingConditions(conditionIds);
    }

    private void start() {
        if (type.equals("conversation")) {
            Utils.getBrokerManager().componentObservers.notifyShowConversationDialog(conversationId, entityId);
        } else if (type.equals("messagebox")) {
            Utils.getBrokerManager().componentObservers.notifyShowMessageDialog(replaceText());
        } else {
            throw new GdxRuntimeException("Event must contain 'type'.");
        }
    }

    private String replaceText() {
        String substr = text.substring(text.indexOf("%"));
        substr = substr.substring(0, substr.indexOf("%", 1) + 1);
        boolean hasGamePad = Utils.isGamepadConnected();
        return switch (substr) {
            case "%action%" -> text.replace(substr, hasGamePad ? "'A' button" : "'A' key");
            case "%inventory%" -> text.replace(substr, hasGamePad ? "'Y' button" : "'I' key");
            case "%slow%" -> text.replace(substr, hasGamePad ? "'L1' button" : "'Ctrl' key");
            default -> throw new IllegalStateException("Unexpected value: " + substr);
        };
    }

}
