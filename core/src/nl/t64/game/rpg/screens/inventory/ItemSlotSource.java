package nl.t64.game.rpg.screens.inventory;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.DragAndDrop;
import com.badlogic.gdx.scenes.scene2d.utils.DragAndDrop.Payload;
import com.badlogic.gdx.scenes.scene2d.utils.DragAndDrop.Source;
import com.badlogic.gdx.scenes.scene2d.utils.DragAndDrop.Target;


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
        var imageToDrag = sourceSlot.getCertainInventoryImage();
        int startAmount = sourceSlot.getAmount();
        sourceSlot.clearStack();
        if (startAmount > 1) {
            handlePartOfStack(imageToDrag, startAmount);
        } else {
            unmakeCurrentDragImageDraggable();
        }
        return createPayload(imageToDrag);
    }

    @Override
    public void dragStop(InputEvent event, float x, float y, int pointer, Payload payload, Target target) {
        boolean targetExist = target != null;
        if (!targetExist) {
            var draggedItem = (InventoryImage) payload.getDragActor();
            sourceSlot.putItemBack(draggedItem);
        }
    }

    private void handlePartOfStack(InventoryImage imageToDrag, int startAmount) {
        if (InventoryUtils.isShiftPressed() && InventoryUtils.isCtrlPressed()) {
            // do nothing
        } else if (InventoryUtils.isCtrlPressed()) {
            duplicateInSource(imageToDrag, startAmount);
            imageToDrag.setAmount(sourceSlot.getHalfOfAmount());
            sourceSlot.decrementAmountBy(sourceSlot.getHalfOfAmount());
        } else if (!InventoryUtils.isShiftPressed()) {
            duplicateInSource(imageToDrag, startAmount);
            imageToDrag.setAmount(1);
            sourceSlot.decrementAmountBy(1);
        }
    }

    private Payload createPayload(InventoryImage imageToDrag) {
        var copyImage = InventoryImage.copyOf(imageToDrag);
        var payload = new Payload();
        payload.setDragActor(copyImage);
        makeSingleItemDraggable(copyImage);
        dragAndDrop.setDragActorPosition(copyImage.getWidth() / 2, -copyImage.getHeight() / 2);
        return payload;
    }

    private void duplicateInSource(InventoryImage imageToDrag, int startAmount) {
        var copyImage = InventoryImage.copyOf(imageToDrag, startAmount);
        makeLastItemDraggable(copyImage);
        sourceSlot.addToStack(copyImage);
    }

    private void makeSingleItemDraggable(InventoryImage copyImage) {
        if (copyImage.getAmount() == 1) {
            makeDraggable(copyImage);
        }
    }

    private void makeLastItemDraggable(InventoryImage copyImage) {
        if (copyImage.getAmount() == 2) {
            makeDraggable(copyImage);
        }
    }

    private void makeDraggable(InventoryImage copyImage) {
        var itemSlotSource = new ItemSlotSource(copyImage, dragAndDrop);
        dragAndDrop.addSource(itemSlotSource);
    }

    private void unmakeCurrentDragImageDraggable() {
        Source dragSource = dragAndDrop.getDragSource();
        dragAndDrop.removeSource(dragSource);
    }

}
