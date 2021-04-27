package nl.t64.game.rpg.screens.menu;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;

import java.util.function.IntConsumer;


class ListenerKeyVertical extends InputListener {

    private final ListenerInput listenerInput;

    ListenerKeyVertical(IntConsumer updateIndexFunction, int numberOfItems) {
        this.listenerInput = new ListenerInput(updateIndexFunction, numberOfItems);
    }

    void updateSelectedIndex(int newSelectedIndex) {
        listenerInput.updateSelectedIndex(newSelectedIndex);
    }

    @Override
    public boolean keyDown(InputEvent event, int keycode) {
        switch (keycode) {
            case Input.Keys.UP -> listenerInput.inputPrev();
            case Input.Keys.DOWN -> listenerInput.inputNext();
        }
        return true;
    }

}
