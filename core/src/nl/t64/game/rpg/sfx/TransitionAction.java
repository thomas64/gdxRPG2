package nl.t64.game.rpg.sfx;

import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import lombok.Setter;
import nl.t64.game.rpg.constants.Constant;


@Setter
public class TransitionAction extends Action {

    private TransitionType type;
    private float duration;

    public static TransitionAction transition(TransitionType type) {
        TransitionAction action = Actions.action(TransitionAction.class);
        action.setType(type);
        action.setDuration(Constant.FADE_DURATION);
        return action;
    }

    @Override
    public boolean act(float dt) {
        Actor actor = super.getTarget();
        switch (type) {
            case FADE_IN -> actor.addAction(Actions.fadeOut(duration));
            case FADE_OUT -> actor.addAction(Actions.fadeIn(duration));
        }
        return true;
    }

}
