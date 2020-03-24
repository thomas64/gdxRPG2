package nl.t64.game.rpg.screens.inventory;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ActorGestureListener;

import java.util.function.Consumer;


public class InventorySlotClickListener extends ActorGestureListener {

    private final Consumer<InventorySlot> handleDoubleClickFunction;

    public InventorySlotClickListener(Consumer<InventorySlot> handleDoubleClickFunction) {
        this.handleDoubleClickFunction = handleDoubleClickFunction;
    }

    @Override
    public void tap(InputEvent event, float x, float y, int count, int button) {
        if (count == 2) {
            InventorySlot inventorySlot = (InventorySlot) event.getListenerActor();
            handleDoubleClickFunction.accept(inventorySlot);
        }
    }

}
