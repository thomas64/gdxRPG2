package nl.t64.game.rpg.screens.menu;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import nl.t64.game.rpg.constants.Constant;

import java.util.function.IntConsumer;


class ListenerKeyStart extends InputListener {

    private final ListenerInput listenerInput;

    ListenerKeyStart(IntConsumer updateIndexFunction, Runnable selectItemFunction, int exitIndex) {
        this.listenerInput = new ListenerInput(updateIndexFunction, selectItemFunction, exitIndex);
    }

    @Override
    public boolean keyDown(InputEvent event, int keycode) {
        if (keycode == Constant.KEYCODE_START) {
            listenerInput.inputConfirmDefinedIndex();
        }
        return true;
    }

}
