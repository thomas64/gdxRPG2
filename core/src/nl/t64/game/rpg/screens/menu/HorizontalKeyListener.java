package nl.t64.game.rpg.screens.menu;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;

import java.util.function.Consumer;


public class HorizontalKeyListener extends InputListener {

    private final Consumer<Integer> updateIndexFunction;
    private final int numberOfItems;

    private int selectedIndex;

    HorizontalKeyListener(Consumer<Integer> updateIndexFunction, int numberOfItems) {
        this.updateIndexFunction = updateIndexFunction;
        this.numberOfItems = numberOfItems;
    }

    void updateSelectedIndex(int newSelectedIndex) {
        selectedIndex = newSelectedIndex;
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
                updateIndexFunction.accept(selectedIndex);
                break;
            case Input.Keys.RIGHT:
                if (selectedIndex >= numberOfItems - 1) {
                    selectedIndex = numberOfItems - 1;
                } else {
                    selectedIndex += 1;
                }
                updateIndexFunction.accept(selectedIndex);
                break;
            default:
                break;
        }
        return true;
    }

}
