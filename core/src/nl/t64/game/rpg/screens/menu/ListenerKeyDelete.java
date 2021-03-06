package nl.t64.game.rpg.screens.menu;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;

import java.util.function.IntConsumer;


class ListenerKeyDelete extends InputListener {

    private final ListenerInput listenerInput;

    ListenerKeyDelete(IntConsumer updateIndexFunction, Runnable selectItemFunction, int deleteIndex) {
        this.listenerInput = new ListenerInput(updateIndexFunction, selectItemFunction, deleteIndex);
    }

    @Override
    public boolean keyDown(InputEvent event, int keycode) {
        if (keycode == Input.Keys.FORWARD_DEL) {
            listenerInput.inputConfirmDefinedIndex();
        }
        return true;
    }

}
