package nl.t64.game.rpg.screens.menu;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;

import java.util.function.IntConsumer;


class ListenerKeyConfirm extends InputListener {

    private final IntConsumer updateIndexFunction;
    private final Runnable selectItemFunction;
    private final int exitIndex;

    ListenerKeyConfirm(IntConsumer updateIndexFunction, Runnable selectItemFunction, int exitIndex) {
        this.updateIndexFunction = updateIndexFunction;
        this.selectItemFunction = selectItemFunction;
        this.exitIndex = exitIndex;
    }

    @Override
    public boolean keyDown(InputEvent event, int keycode) {
        switch (keycode) {
            case Input.Keys.ENTER:
                inputEnter();
                break;
            case Input.Keys.ESCAPE:
                inputEscape();
                break;
            default:
                break;
        }
        return true;
    }

    private void inputEnter() {
        selectItemFunction.run();
    }

    private void inputEscape() {
        updateIndexFunction.accept(exitIndex);
        selectItemFunction.run();
    }

}
