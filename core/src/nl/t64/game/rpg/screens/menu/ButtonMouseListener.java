package nl.t64.game.rpg.screens.menu;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;

import java.util.function.Consumer;


public class ButtonMouseListener extends InputListener {

    private final Consumer<Integer> updateIndexFunction;
    private final Runnable selectItemFunction;
    private final int index;

    ButtonMouseListener(Consumer<Integer> updateIndexFunction, Runnable selectItemFunction, int index) {
        this.updateIndexFunction = updateIndexFunction;
        this.selectItemFunction = selectItemFunction;
        this.index = index;
    }

    @Override
    public boolean mouseMoved(InputEvent event, float x, float y) {
        updateIndexFunction.accept(index);
        return true;
    }

    @Override
    public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
        updateIndexFunction.accept(index);
        selectItemFunction.run();
        return true;
    }

}
