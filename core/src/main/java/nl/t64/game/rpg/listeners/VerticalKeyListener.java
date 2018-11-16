package nl.t64.game.rpg.listeners;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import lombok.Setter;

import java.util.function.Consumer;


public class VerticalKeyListener extends InputListener {

    private final Consumer<Integer> updateIndexFunction;
    @Setter
    private int numberOfItems;

    private int selectedIndex;

    public VerticalKeyListener(Consumer<Integer> updateIndexFunction, int numberOfItems) {
        this.updateIndexFunction = updateIndexFunction;
        this.numberOfItems = numberOfItems;
    }

    public void updateSelectedIndex(int newSelectedIndex) {
        selectedIndex = newSelectedIndex;
    }

    @Override
    public boolean keyDown(InputEvent event, int keycode) {
        if (numberOfItems == 0) {
            return false;
        }
        switch (keycode) {
            case Input.Keys.UP:
                if (selectedIndex <= 0) {
                    selectedIndex = 0;
                } else {
                    selectedIndex -= 1;
                }
                updateIndexFunction.accept(selectedIndex);
                break;
            case Input.Keys.DOWN:
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
