package nl.t64.game.rpg.screens.inventory;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.DragAndDrop;
import com.badlogic.gdx.scenes.scene2d.utils.DragAndDrop.Payload;
import com.badlogic.gdx.scenes.scene2d.utils.DragAndDrop.Source;
import com.badlogic.gdx.scenes.scene2d.utils.DragAndDrop.Target;
import nl.t64.game.rpg.components.party.InventoryItem;


public class ItemSlotSource extends Source {

    ItemSlot sourceSlot;
    private final DragAndDrop dragAndDrop;

    public ItemSlotSource(InventoryImage inventoryImage, DragAndDrop dragAndDrop) {
        super(inventoryImage); // = setSourceActor
        this.dragAndDrop = dragAndDrop;
        this.sourceSlot = null;
    }

    public ItemSlotSource(Label amountLabel, DragAndDrop dragAndDrop) {
        super(amountLabel);  // = setSourceActor
        this.dragAndDrop = dragAndDrop;
        this.sourceSlot = null;
    }

    @Override
    public Payload dragStart(InputEvent event, float x, float y, int pointer) {
        sourceSlot = (ItemSlot) super.getActor().getParent();
        sourceSlot.decrementAmount();
        int newAmount = sourceSlot.getAmount();
        var payload = new Payload();
        var dragImage = sourceSlot.getCertainInventoryImage();
        dragImage.inventoryItem.setAmount(1);
        payload.setDragActor(dragImage);
        dragAndDrop.setDragActorPosition(payload.getDragActor().getWidth() / 2,
                                         -payload.getDragActor().getHeight() / 2);
        if (newAmount > 0) {
            duplicateInSource(dragImage, newAmount);
        }
        return payload;
    }

    @Override
    public void dragStop(InputEvent event, float x, float y, int pointer, Payload payload, Target target) {
        boolean targetExist = target != null;
        if (!targetExist) {
            sourceSlot.putItemBack(payload.getDragActor());
        }
    }

    private void duplicateInSource(InventoryImage dragImage, int newAmount) {
        var inventoryItem = new InventoryItem(dragImage.inventoryItem);
        inventoryItem.setAmount(newAmount);
        var inventoryImage = new InventoryImage(inventoryItem);
        sourceSlot.addToStack(inventoryImage);
        var itemSlotSource = new ItemSlotSource(inventoryImage, dragAndDrop);
        dragAndDrop.addSource(itemSlotSource);
    }

}
