package nl.t64.game.rpg.screens.inventory;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ActorGestureListener;
import com.badlogic.gdx.scenes.scene2d.utils.DragAndDrop;

import java.util.function.BiConsumer;


public class ItemSlotClickListener extends ActorGestureListener {

    private final BiConsumer<ItemSlot, DragAndDrop> handleDoubleClickFunction;
    private final DragAndDrop dragAndDrop;

    ItemSlotClickListener(BiConsumer<ItemSlot, DragAndDrop> handleDoubleClickFunction) {
        this(handleDoubleClickFunction, null);
    }

    public ItemSlotClickListener(BiConsumer<ItemSlot, DragAndDrop> handleDoubleClickFunction, DragAndDrop dragAndDrop) {
        this.handleDoubleClickFunction = handleDoubleClickFunction;
        this.dragAndDrop = dragAndDrop;
    }

    @Override
    public void tap(InputEvent event, float x, float y, int count, int button) {
        if (count == 2) {
            ItemSlot itemSlot = (ItemSlot) event.getListenerActor();
            handleDoubleClickFunction.accept(itemSlot, dragAndDrop);
        }
    }

}
