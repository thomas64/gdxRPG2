package nl.t64.game.rpg.screens.inventory;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.DragAndDrop;
import com.badlogic.gdx.scenes.scene2d.utils.DragAndDrop.Payload;
import com.badlogic.gdx.scenes.scene2d.utils.DragAndDrop.Source;
import com.badlogic.gdx.scenes.scene2d.utils.DragAndDrop.Target;


class InventorySlotSource extends Source {

    InventorySlot sourceSlot;
    private final DragAndDrop dragAndDrop;

    InventorySlotSource(InventoryImage inventoryImage, DragAndDrop dragAndDrop) {
        super(inventoryImage); // = setSourceActor
        this.dragAndDrop = dragAndDrop;
        this.sourceSlot = null;
    }

    InventorySlotSource(Label amountLabel, DragAndDrop dragAndDrop) {
        super(amountLabel);  // = setSourceActor
        this.dragAndDrop = dragAndDrop;
        this.sourceSlot = null;
    }

    @Override
    public Payload dragStart(InputEvent event, float x, float y, int pointer) {
        sourceSlot = (InventorySlot) super.getActor().getParent();
        sourceSlot.decrementAmount();
        Payload payload = new Payload();
        payload.setDragActor(sourceSlot.getCertainInventoryImage());
        dragAndDrop.setDragActorPosition(payload.getDragActor().getWidth() / 2,
                                         -payload.getDragActor().getHeight() / 2);
        if (sourceSlot.amount > 0) {
            sourceSlot.addToStack(new InventoryImage(sourceSlot.getCertainInventoryImage().inventoryItem));
            dragAndDrop.addSource(new InventorySlotSource(sourceSlot.getCertainInventoryImage(), dragAndDrop));
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

}
