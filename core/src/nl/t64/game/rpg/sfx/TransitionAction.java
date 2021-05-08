package nl.t64.game.rpg.sfx;

import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
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
        switch (type) {
            case FADE_IN -> fadeIn();
            case FADE_OUT -> fadeOut();
        }
        return true;
    }

    private void fadeIn() {
        Actor actor = super.getTarget();
        SequenceAction fadeIn = Actions.sequence(
                Actions.alpha(1f),
                Actions.fadeOut(duration)
        );
        actor.addAction(fadeIn);
    }

    private void fadeOut() {
        Actor actor = super.getTarget();
        SequenceAction fadeOut = Actions.sequence(
                Actions.alpha(0f),
                Actions.fadeIn(duration)
        );
        actor.addAction(fadeOut);
    }

}
