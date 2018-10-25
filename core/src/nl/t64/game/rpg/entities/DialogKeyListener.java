package nl.t64.game.rpg.entities;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;

import java.util.function.Consumer;


public class DialogKeyListener extends InputListener {

    private final Consumer<Integer> updateIndexFunction;
    private final Runnable selectItemFunction;
    private final int numberOfItems;
    private final int exitIndex;

    private int selectedIndex;

    public DialogKeyListener(Consumer<Integer> updateIndexFunction,
                             Runnable selectItemFunction,
                             int numberOfItems, int exitIndex) {
        this.updateIndexFunction = updateIndexFunction;
        this.selectItemFunction = selectItemFunction;
        this.numberOfItems = numberOfItems;
        this.exitIndex = exitIndex;
    }

    @Override
    public boolean keyDown(InputEvent event, int keycode) {

        switch (keycode) {
            case Input.Keys.LEFT:
                if (selectedIndex <= 0) {
                    selectedIndex = 0;
                } else {
                    selectedIndex -= 1;
                }
                break;
            case Input.Keys.RIGHT:
                if (selectedIndex >= numberOfItems - 1) {
                    selectedIndex = numberOfItems - 1;
                } else {
                    selectedIndex += 1;
                }
                break;
            case Input.Keys.ENTER:
                selectItemFunction.run();
                break;
            case Input.Keys.ESCAPE:
                selectedIndex = exitIndex;
                updateIndexFunction.accept(selectedIndex);
                selectItemFunction.run();
                break;
            default:
        }
        updateIndexFunction.accept(selectedIndex);
        return true;
    }

}
