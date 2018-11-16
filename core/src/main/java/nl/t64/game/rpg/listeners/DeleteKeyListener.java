package nl.t64.game.rpg.listeners;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;

import java.util.function.Consumer;


public class DeleteKeyListener extends InputListener {

    private final Consumer<Integer> updateIndexFunction;
    private final Runnable selectItemFunction;
    private final int deleteIndex;

    public DeleteKeyListener(Consumer<Integer> updateIndexFunction, Runnable selectItemFunction, int deleteIndex) {
        this.updateIndexFunction = updateIndexFunction;
        this.selectItemFunction = selectItemFunction;
        this.deleteIndex = deleteIndex;
    }

    @Override
    public boolean keyDown(InputEvent event, int keycode) {
        if (keycode == Input.Keys.FORWARD_DEL) {
            updateIndexFunction.accept(deleteIndex);
            selectItemFunction.run();
        }
        return true;
    }

}
