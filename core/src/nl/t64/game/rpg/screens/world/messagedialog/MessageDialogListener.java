package nl.t64.game.rpg.screens.world.messagedialog;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import lombok.AllArgsConstructor;
import nl.t64.game.rpg.constants.Constant;


@AllArgsConstructor
class MessageDialogListener extends InputListener {

    private final Runnable closeDialogFunction;

    @Override
    public boolean keyDown(InputEvent event, int keycode) {
        switch (keycode) {
            case Constant.KEYCODE_BOTTOM,
                    Input.Keys.ENTER,
                    Input.Keys.A -> closeDialogFunction.run();
        }
        return true;
    }

}
