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
        var dragImage = sourceSlot.getCertainInventoryImage();
        int startAmount = sourceSlot.getAmount();
        sourceSlot.clearStack();
        if (startAmount > 1) {
            handlePartOfStack(dragImage, startAmount);
        }
        var payload = new Payload();
        payload.setDragActor(dragImage);
        dragAndDrop.setDragActorPosition(payload.getDragActor().getWidth() / 2,
                                         -payload.getDragActor().getHeight() / 2);
        return payload;
    }

    private void handlePartOfStack(InventoryImage dragImage, int startAmount) {
        if (InventoryUtils.isShiftPressed() && InventoryUtils.isCtrlPressed()) {
            // do nothing
        } else if (InventoryUtils.isCtrlPressed()) {
            duplicateInSource(dragImage, startAmount);
            dragImage.setAmount(sourceSlot.getHalfOfAmount());
            sourceSlot.decrementAmountBy(sourceSlot.getHalfOfAmount());
        } else if (!InventoryUtils.isShiftPressed()) {
            duplicateInSource(dragImage, startAmount);
            dragImage.setAmount(1);
            sourceSlot.decrementAmountBy(1);
        }
    }

    @Override
    public void dragStop(InputEvent event, float x, float y, int pointer, Payload payload, Target target) {
        boolean targetExist = target != null;
        if (!targetExist) {
            var draggedItem = (InventoryImage) payload.getDragActor();
            sourceSlot.putItemBack(draggedItem);
        }
    }

    private void duplicateInSource(InventoryImage dragImage, int startAmount) {
        var inventoryItem = new InventoryItem(dragImage.inventoryItem);
        inventoryItem.setAmount(startAmount);
        var inventoryImage = new InventoryImage(inventoryItem);
        sourceSlot.addToStack(inventoryImage);
        var itemSlotSource = new ItemSlotSource(inventoryImage, dragAndDrop);
        dragAndDrop.addSource(itemSlotSource);
    }

}
