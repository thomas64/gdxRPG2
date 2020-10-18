package nl.t64.game.rpg.screens.menu;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import nl.t64.game.rpg.Utils;
import nl.t64.game.rpg.audio.AudioCommand;
import nl.t64.game.rpg.audio.AudioEvent;

import java.util.function.IntConsumer;


class ListenerKeyHorizontal extends InputListener {

    private final IntConsumer updateIndexFunction;
    private final int numberOfItems;

    private int selectedIndex;

    ListenerKeyHorizontal(IntConsumer updateIndexFunction, int numberOfItems) {
        this.updateIndexFunction = updateIndexFunction;
        this.numberOfItems = numberOfItems;
    }

    void updateSelectedIndex(int newSelectedIndex) {
        selectedIndex = newSelectedIndex;
    }

    @Override
    public boolean keyDown(InputEvent event, int keycode) {
        switch (keycode) {
            case Input.Keys.LEFT -> inputLeft();
            case Input.Keys.RIGHT -> inputRight();
        }
        return true;
    }

    private void inputLeft() {
        if (selectedIndex <= 0) {
            Utils.getAudioManager().handle(AudioCommand.SE_PLAY_ONCE, AudioEvent.SE_MENU_ERROR);
            selectedIndex = 0;
        } else {
            Utils.getAudioManager().handle(AudioCommand.SE_PLAY_ONCE, AudioEvent.SE_MENU_CURSOR);
            selectedIndex -= 1;
        }
        updateIndexFunction.accept(selectedIndex);
    }

    private void inputRight() {
        if (selectedIndex >= numberOfItems - 1) {
            Utils.getAudioManager().handle(AudioCommand.SE_PLAY_ONCE, AudioEvent.SE_MENU_ERROR);
            selectedIndex = numberOfItems - 1;
        } else {
            Utils.getAudioManager().handle(AudioCommand.SE_PLAY_ONCE, AudioEvent.SE_MENU_CURSOR);
            selectedIndex += 1;
        }
        updateIndexFunction.accept(selectedIndex);
    }

}
