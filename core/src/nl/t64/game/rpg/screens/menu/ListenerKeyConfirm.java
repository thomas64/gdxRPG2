package nl.t64.game.rpg.screens.menu;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import nl.t64.game.rpg.Utils;
import nl.t64.game.rpg.audio.AudioCommand;
import nl.t64.game.rpg.audio.AudioEvent;

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
            case Input.Keys.ENTER -> inputEnter();
            case Input.Keys.ESCAPE -> inputEscape();
        }
        return true;
    }

    private void inputEnter() {
        Utils.getAudioManager().handle(AudioCommand.SE_PLAY_ONCE, AudioEvent.SE_MENU_CONFIRM);
        selectItemFunction.run();
    }

    private void inputEscape() {
        Utils.getAudioManager().handle(AudioCommand.SE_PLAY_ONCE, AudioEvent.SE_MENU_BACK);
        updateIndexFunction.accept(exitIndex);
        selectItemFunction.run();
    }

}
