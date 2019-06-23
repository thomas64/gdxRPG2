package nl.t64.game.rpg.screens.inventory;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import java.util.function.Consumer;


class InventorySlotPreviewListener extends ClickListener {

    private final Consumer<InventorySlot> functionToExecute;
    private boolean touchDown;

    InventorySlotPreviewListener(Consumer<InventorySlot> functionToExecute) {
        this.functionToExecute = functionToExecute;
        this.touchDown = false;
    }

    @Override
    public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
        touchDown = true;
        return true;
    }

    @Override
    public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
        InventorySlot inventorySlot = (InventorySlot) event.getListenerActor();
        functionToExecute.accept(inventorySlot);
    }

    @Override
    public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
        if (!touchDown) {
            functionToExecute.accept(new InventorySlot());
        }
        touchDown = false;
    }

}
