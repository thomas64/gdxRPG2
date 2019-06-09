package nl.t64.game.rpg.screens.inventory;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;

import java.util.function.Consumer;


public class InventorySlotPreviewListener extends InputListener {

    private final Consumer<InventorySlot> functionToExecute;

    InventorySlotPreviewListener(Consumer<InventorySlot> functionToExecute) {
        this.functionToExecute = functionToExecute;
    }

    @Override
    public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
        InventorySlot inventorySlot = (InventorySlot) event.getListenerActor();
        functionToExecute.accept(inventorySlot);
    }

    @Override
    public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
        functionToExecute.accept(new InventorySlot());
    }

}
