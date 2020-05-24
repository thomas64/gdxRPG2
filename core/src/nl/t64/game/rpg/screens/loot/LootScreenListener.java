package nl.t64.game.rpg.screens.loot;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import nl.t64.game.rpg.screens.inventory.InventoryUtils;


class LootScreenListener extends InputListener {

    private final Runnable closeScreenFunction;
    private final Runnable takeItemFunction;

    LootScreenListener(Runnable closeScreenFunction, Runnable takeItemFunction) {
        this.closeScreenFunction = closeScreenFunction;
        this.takeItemFunction = takeItemFunction;
    }

    @Override
    public boolean keyDown(InputEvent event, int keycode) {
        switch (keycode) {
            case Input.Keys.ESCAPE -> closeScreenFunction.run();
            case Input.Keys.ENTER,
                    Input.Keys.A -> takeStackOfItems();
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

    private void takeStackOfItems() {
        InventoryUtils.setShiftPressed(true);
        takeItemFunction.run();
        InventoryUtils.setShiftPressed(false);
    }

}
