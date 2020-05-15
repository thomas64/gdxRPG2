package nl.t64.game.rpg.screens.menu;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;

import java.util.function.IntConsumer;


class ListenerKeyVertical extends InputListener {

    private final IntConsumer updateIndexFunction;
    private int numberOfItems;
    private int selectedIndex;

    ListenerKeyVertical(IntConsumer updateIndexFunction, int numberOfItems) {
        this.updateIndexFunction = updateIndexFunction;
        this.numberOfItems = numberOfItems;
    }

    void updateNumberOfItems(int listItemsSize) {
        numberOfItems = listItemsSize;
    }

    void updateSelectedIndex(int newSelectedIndex) {
        selectedIndex = newSelectedIndex;
    }

    @Override
    public boolean keyDown(InputEvent event, int keycode) {
        if (numberOfItems == 0) {
            return false;
        }
        switch (keycode) {
            case Input.Keys.UP -> inputUp();
            case Input.Keys.DOWN -> inputDown();
        }
        return true;
    }

    private void inputUp() {
        if (selectedIndex <= 0) {
            selectedIndex = 0;
        } else {
            selectedIndex -= 1;
        }
        updateIndexFunction.accept(selectedIndex);
    }

    private void inputDown() {
        if (selectedIndex >= numberOfItems - 1) {
            selectedIndex = numberOfItems - 1;
        } else {
            selectedIndex += 1;
        }
        updateIndexFunction.accept(selectedIndex);
    }

}
