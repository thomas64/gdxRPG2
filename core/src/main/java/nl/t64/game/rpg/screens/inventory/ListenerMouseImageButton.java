package nl.t64.game.rpg.screens.inventory;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;


public class ListenerMouseImageButton extends ClickListener {

    private final Runnable functionToExecute;
    private boolean hasFocus;

    public ListenerMouseImageButton(Runnable functionToExecute) {
        this.functionToExecute = functionToExecute;
        this.hasFocus = false;
    }

    @Override
    public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
        hasFocus = true;
        return true;
    }

    @Override
    public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
        if (hasFocus) {
            functionToExecute.run();
        }
    }

    @Override
    public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
        hasFocus = false;
    }

}
