package nl.t64.game.rpg.screens.menu;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;

import java.util.function.Consumer;


public class ConfirmKeyListener extends InputListener {

    private final Consumer<Integer> updateIndexFunction;
    private final Runnable selectItemFunction;
    private final int exitIndex;

    ConfirmKeyListener(Consumer<Integer> updateIndexFunction, Runnable selectItemFunction, int exitIndex) {
        this.updateIndexFunction = updateIndexFunction;
        this.selectItemFunction = selectItemFunction;
        this.exitIndex = exitIndex;
    }

    @Override
    public boolean keyDown(InputEvent event, int keycode) {
        switch (keycode) {
            case Input.Keys.ENTER:
                selectItemFunction.run();
                break;
            case Input.Keys.ESCAPE:
                updateIndexFunction.accept(exitIndex);
                selectItemFunction.run();
                break;
            default:
                break;
        }
        return true;
    }

}
