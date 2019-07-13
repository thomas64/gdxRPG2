package nl.t64.game.rpg.screens.inventory;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Array;

import java.util.function.Consumer;


class InventorySlotPreviewListener extends ClickListener {

    private final Consumer<InventorySlot> updateHoveredItemFunction;
    private boolean touchDown;

    InventorySlotPreviewListener(Consumer<InventorySlot> updateHoveredItemFunction) {
        this.updateHoveredItemFunction = updateHoveredItemFunction;
        this.touchDown = false;
    }

    @Override
    public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
        touchDown = true;
        return true;
    }

    @Override
    public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
        // this makes sure preview doesn't immediately returns after doubleClick.
        if (!getClickListener(event).isDoubleClicked()) {
            InventorySlot inventorySlot = (InventorySlot) event.getListenerActor();
            updateHoveredItemFunction.accept(inventorySlot);
        }
    }

    @Override
    public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
        // touchDown makes sure that preview doesn't disappear after singleClick.
        if (!touchDown) {
            updateHoveredItemFunction.accept(new InventorySlot());
        }
        touchDown = false;
        getClickListener(event).setDoubleClickedToFalse();
    }

    private InventorySlotClickListener getClickListener(InputEvent event) {
        final Array<EventListener> listeners = event.getListenerActor().getListeners();
        for (EventListener listener : new Array.ArrayIterator<>(listeners)) {
            if (listener instanceof InventorySlotClickListener) {
                return (InventorySlotClickListener) listener;
            }
        }
        throw new IllegalStateException("There should be an InventorySlotClickListener in Array of EventListeners");
    }

}
