package nl.t64.game.rpg.screens.inventory;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;


class InventoryScreenListener extends InputListener {

    private final Runnable closeScreenFunction;

    InventoryScreenListener(Runnable closeScreenFunction) {
        this.closeScreenFunction = closeScreenFunction;
    }

    @Override
    public boolean keyDown(InputEvent event, int keycode) {
        switch (keycode) {
            case Input.Keys.I:
            case Input.Keys.ESCAPE:
                closeScreenFunction.run();
                break;
            default:
                break;
        }
        return true;
    }

}
