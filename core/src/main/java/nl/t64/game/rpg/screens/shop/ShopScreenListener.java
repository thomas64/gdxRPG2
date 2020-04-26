package nl.t64.game.rpg.screens.shop;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import nl.t64.game.rpg.screens.inventory.InventoryUtils;


class ShopScreenListener extends InputListener {

    private final Runnable closeScreenFunction;

    ShopScreenListener(Runnable closeScreenFunction) {
        this.closeScreenFunction = closeScreenFunction;
    }

    @Override
    public boolean keyDown(InputEvent event, int keycode) {
        switch (keycode) {
            case Input.Keys.ESCAPE -> closeScreenFunction.run();
            case Input.Keys.SHIFT_LEFT,
                    Input.Keys.SHIFT_RIGHT -> InventoryUtils.setShiftPressed(true);
            case Input.Keys.CONTROL_LEFT,
                    Input.Keys.CONTROL_RIGHT -> InventoryUtils.setCtrlPressed(true);
        }
        return true;
    }

    @Override
    public boolean keyUp(InputEvent event, int keycode) {
        InventoryUtils.setShiftPressed(false);
        InventoryUtils.setCtrlPressed(false);
        return true;
    }

}
