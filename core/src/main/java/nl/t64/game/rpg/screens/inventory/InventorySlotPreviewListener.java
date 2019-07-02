package nl.t64.game.rpg.screens.inventory;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import java.util.function.Consumer;


class InventorySlotPreviewListener extends ClickListener {

    private final Consumer<InventorySlot> functionToExecute;
    private boolean touchDown;
    private boolean touchUp;

    InventorySlotPreviewListener(Consumer<InventorySlot> functionToExecute) {
        this.functionToExecute = functionToExecute;
        this.touchDown = false;
        this.touchUp = false;
    }

    @Override
    public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
        touchDown = true;
        return true;
    }

    @Override
    public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
        touchUp = true;
    }

    @Override
    public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
        // preview is cleared after doubleClick, and touchUp makes sure preview doesn't immediately returns.
        if (!touchUp) {
            InventorySlot inventorySlot = (InventorySlot) event.getListenerActor();
            functionToExecute.accept(inventorySlot);
        }
        touchUp = false;
    }

    @Override
    public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
        // touchDown makes sure that preview doesn't disappear after singleClick.
        if (!touchDown) {
            functionToExecute.accept(new InventorySlot());
        }
        touchDown = false;
    }

}
