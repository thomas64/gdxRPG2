package nl.t64.game.rpg.screens.inventory;

import com.badlogic.gdx.scenes.scene2d.utils.DragAndDrop.Payload;
import com.badlogic.gdx.scenes.scene2d.utils.DragAndDrop.Source;
import com.badlogic.gdx.scenes.scene2d.utils.DragAndDrop.Target;


public class ItemSlotTarget extends Target {

    private final ItemSlot targetSlot;

    public ItemSlotTarget(ItemSlot targetSlot) {
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
        ItemSlot sourceSlot = ((ItemSlotSource) source).sourceSlot;
        new ItemSlotsExchanger(draggedItem, sourceSlot, targetSlot).exchange();
    }

}
