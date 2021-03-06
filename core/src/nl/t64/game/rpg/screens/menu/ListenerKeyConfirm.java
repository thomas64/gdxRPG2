package nl.t64.game.rpg.screens.menu;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import nl.t64.game.rpg.constants.Constant;


class ListenerKeyConfirm extends InputListener {

    private final ListenerInput listenerInput;

    ListenerKeyConfirm(Runnable selectItemFunction) {
        this.listenerInput = new ListenerInput(selectItemFunction);
    }

    @Override
    public boolean keyDown(InputEvent event, int keycode) {
        switch (keycode) {
            case Constant.KEYCODE_BOTTOM,
                    Input.Keys.ENTER -> listenerInput.inputConfirm();
        }
        return true;
    }

}
