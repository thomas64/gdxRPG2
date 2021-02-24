package nl.t64.game.rpg.screens.menu;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;

import java.util.function.IntConsumer;


class ListenerKeyHorizontal extends InputListener {

    private final ListenerInput listenerInput;

    ListenerKeyHorizontal(IntConsumer updateIndexFunction, int numberOfItems) {
        this.listenerInput = new ListenerInput(updateIndexFunction, numberOfItems);
    }

    void updateSelectedIndex(int newSelectedIndex) {
        listenerInput.updateSelectedIndex(newSelectedIndex);
    }

    @Override
    public boolean keyDown(InputEvent event, int keycode) {
        switch (keycode) {
            case Input.Keys.LEFT -> listenerInput.inputPrev();
            case Input.Keys.RIGHT -> listenerInput.inputNext();
        }
        return true;
    }

}
