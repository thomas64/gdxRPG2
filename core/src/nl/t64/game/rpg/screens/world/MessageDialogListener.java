package nl.t64.game.rpg.screens.world;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;


class MessageDialogListener extends InputListener {

    private final Runnable closeDialogFunction;

    MessageDialogListener(Runnable closeDialogFunction) {
        this.closeDialogFunction = closeDialogFunction;
    }

    @Override
    public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
        closeDialogFunction.run();
        return true;
    }

    @Override
    public boolean keyDown(InputEvent event, int keycode) {
        switch (keycode) {
            case Input.Keys.ENTER,
                    Input.Keys.A -> closeDialogFunction.run();
        }
        return true;
    }

}
