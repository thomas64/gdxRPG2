package nl.t64.game.rpg.screens.menu;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import nl.t64.game.rpg.Utils;
import nl.t64.game.rpg.audio.AudioCommand;
import nl.t64.game.rpg.audio.AudioEvent;

import java.util.function.IntConsumer;


class ListenerKeyDelete extends InputListener {

    private final IntConsumer updateIndexFunction;
    private final Runnable selectItemFunction;
    private final int deleteIndex;

    ListenerKeyDelete(IntConsumer updateIndexFunction, Runnable selectItemFunction, int deleteIndex) {
        this.updateIndexFunction = updateIndexFunction;
        this.selectItemFunction = selectItemFunction;
        this.deleteIndex = deleteIndex;
    }

    @Override
    public boolean keyDown(InputEvent event, int keycode) {
        if (keycode == Input.Keys.FORWARD_DEL) {
            inputDelete();
        }
        return true;
    }

    private void inputDelete() {
        Utils.getAudioManager().handle(AudioCommand.SE_PLAY_ONCE, AudioEvent.SE_MENU_CONFIRM);
        updateIndexFunction.accept(deleteIndex);
        selectItemFunction.run();
    }

}
