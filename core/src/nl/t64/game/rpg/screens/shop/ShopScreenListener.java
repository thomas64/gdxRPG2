package nl.t64.game.rpg.screens.shop;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;


class ShopScreenListener extends InputListener {

    private final Runnable closeScreenFunction;

    ShopScreenListener(Runnable closeScreenFunction) {
        this.closeScreenFunction = closeScreenFunction;
    }

    @Override
    public boolean keyDown(InputEvent event, int keycode) {
        switch (keycode) {
            case Input.Keys.ESCAPE -> closeScreenFunction.run();
        }
        return true;
    }

}
