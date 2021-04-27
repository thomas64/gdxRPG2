package nl.t64.game.rpg.screens.menu;

import nl.t64.game.rpg.Utils;
import nl.t64.game.rpg.audio.AudioCommand;
import nl.t64.game.rpg.audio.AudioEvent;

import java.util.function.IntConsumer;


class ListenerInput {

    private static final int NO_MENU_INDEX = 0;
    private static final IntConsumer NO_MENU_TO_UPDATE = i -> {};

    private final IntConsumer updateIndexFunction;
    private int numberOfItems;

    private Runnable selectItemFunction;
    private int definedIndex;

    private int selectedIndex;

    ListenerInput(IntConsumer updateIndexFunction, int numberOfItems) {
        this.updateIndexFunction = updateIndexFunction;
        this.numberOfItems = numberOfItems;
    }

    ListenerInput(Runnable selectItemFunction) {
        this.updateIndexFunction = NO_MENU_TO_UPDATE;
        this.selectItemFunction = selectItemFunction;
        this.definedIndex = NO_MENU_INDEX;
    }

    ListenerInput(IntConsumer updateIndexFunction, Runnable selectItemFunction, int definedIndex) {
        this.updateIndexFunction = updateIndexFunction;
        this.selectItemFunction = selectItemFunction;
        this.definedIndex = definedIndex;
    }

    void updateSelectedIndex(int newSelectedIndex) {
        selectedIndex = newSelectedIndex;
    }

    void inputPrev() {
        if (numberOfItems == 0) {
            return;
        }

        if (selectedIndex <= 0) {
            Utils.getAudioManager().handle(AudioCommand.SE_PLAY_ONCE, AudioEvent.SE_MENU_ERROR);
            selectedIndex = 0;
        } else {
            Utils.getAudioManager().handle(AudioCommand.SE_PLAY_ONCE, AudioEvent.SE_MENU_CURSOR);
            selectedIndex -= 1;
        }
        updateIndexFunction.accept(selectedIndex);
    }

    void inputNext() {
        if (numberOfItems == 0) {
            return;
        }

        if (selectedIndex >= numberOfItems - 1) {
            Utils.getAudioManager().handle(AudioCommand.SE_PLAY_ONCE, AudioEvent.SE_MENU_ERROR);
            selectedIndex = numberOfItems - 1;
        } else {
            Utils.getAudioManager().handle(AudioCommand.SE_PLAY_ONCE, AudioEvent.SE_MENU_CURSOR);
            selectedIndex += 1;
        }
        updateIndexFunction.accept(selectedIndex);
    }

    void inputConfirm() {
        Utils.getAudioManager().handle(AudioCommand.SE_PLAY_ONCE, AudioEvent.SE_MENU_CONFIRM);
        selectItemFunction.run();
    }

    void inputCancel() {
        Utils.getAudioManager().handle(AudioCommand.SE_PLAY_ONCE, AudioEvent.SE_MENU_BACK);
        updateIndexFunction.accept(definedIndex);
        selectItemFunction.run();
    }

    void inputConfirmDefinedIndex() {
        Utils.getAudioManager().handle(AudioCommand.SE_PLAY_ONCE, AudioEvent.SE_MENU_CONFIRM);
        updateIndexFunction.accept(definedIndex);
        selectItemFunction.run();
    }

}
