package nl.t64.game.rpg.screens.menu;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import nl.t64.game.rpg.constants.Constant;

import java.util.function.IntConsumer;


class ListenerKeyCancel extends InputListener {

    private final ListenerInput listenerInput;

    ListenerKeyCancel(Runnable selectItemFunction) {
        this.listenerInput = new ListenerInput(selectItemFunction);
    }

    ListenerKeyCancel(IntConsumer updateIndexFunction, Runnable selectItemFunction, int exitIndex) {
        this.listenerInput = new ListenerInput(updateIndexFunction, selectItemFunction, exitIndex);
    }

    @Override
    public boolean keyDown(InputEvent event, int keycode) {
        switch (keycode) {
            case Constant.KEYCODE_RIGHT,
                    Input.Keys.ESCAPE -> listenerInput.inputCancel();
        }
        return true;
    }

}
