package nl.t64.game.rpg.screens.inventory;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ActorGestureListener;

import java.util.function.Consumer;


class InventorySlotClickListener extends ActorGestureListener {

    private final Consumer<InventorySlot> functionToExecute;

    InventorySlotClickListener(Consumer<InventorySlot> functionToExecute) {
        this.functionToExecute = functionToExecute;
    }

    @Override
    public void tap(InputEvent event, float x, float y, int count, int button) {
        if (count == 2) {
            InventorySlot inventorySlot = (InventorySlot) event.getListenerActor();
            functionToExecute.accept(inventorySlot);
        }
    }

}
