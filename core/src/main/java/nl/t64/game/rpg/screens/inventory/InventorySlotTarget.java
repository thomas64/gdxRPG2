package nl.t64.game.rpg.screens.inventory;

import com.badlogic.gdx.scenes.scene2d.utils.DragAndDrop.Payload;
import com.badlogic.gdx.scenes.scene2d.utils.DragAndDrop.Source;
import com.badlogic.gdx.scenes.scene2d.utils.DragAndDrop.Target;


public class InventorySlotTarget extends Target {

    private final InventorySlot targetSlot;

    public InventorySlotTarget(InventorySlot targetSlot) {
        super(targetSlot);
        this.targetSlot = targetSlot;
    }

    @Override
    public boolean drag(Source source, Payload payload, float x, float y, int pointer) {
        return true;
    }

    @Override
    public void drop(Source source, Payload payload, float x, float y, int pointer) {
        InventoryImage draggedItem = (InventoryImage) payload.getDragActor();
        InventorySlot sourceSlot = ((InventorySlotSource) source).sourceSlot;
        new InventorySlotsExchanger(draggedItem, sourceSlot, targetSlot).exchange();
    }

}
