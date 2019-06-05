package nl.t64.game.rpg.screens.inventory;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;


class InventorySlotTooltipListener extends InputListener {

    private static final int OFFSET_X = 20;
    private static final int OFFSET_Y = 10;

    private InventorySlotTooltip toolTip;
    private boolean isInside;
    private Vector2 currentCoords;
    private Vector2 offset;
    private boolean touchDown;

    InventorySlotTooltipListener(InventorySlotTooltip toolTip) {
        this.toolTip = toolTip;
        this.isInside = false;
        this.currentCoords = new Vector2(0, 0);
        this.offset = new Vector2(OFFSET_X, OFFSET_Y);
    }

    @Override
    public boolean mouseMoved(InputEvent event, float x, float y) {
        InventorySlot inventorySlot = (InventorySlot) event.getListenerActor();
        if (isInside) {
            currentCoords.set(x, y);
            inventorySlot.localToStageCoordinates(currentCoords);
            toolTip.window.setPosition(currentCoords.x + offset.x, currentCoords.y + offset.y);
        }
        return false;
    }

    @Override
    public void touchDragged(InputEvent event, float x, float y, int pointer) {
        touchDown = true;
        InventorySlot inventorySlot = (InventorySlot) event.getListenerActor();
        toolTip.setVisible(inventorySlot, false);
    }

    @Override
    public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
        touchDown = true;
        InventorySlot inventorySlot = (InventorySlot) event.getListenerActor();
        toolTip.setVisible(inventorySlot, false);
        return true;
    }

    @Override
    public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
        isInside = true;

        if (!touchDown) {
            InventorySlot inventorySlot = (InventorySlot) event.getListenerActor();

            currentCoords.set(x, y);
            inventorySlot.localToStageCoordinates(currentCoords);

            toolTip.updateDescription(inventorySlot);
            toolTip.window.setPosition(currentCoords.x + offset.x, currentCoords.y + offset.y);
            toolTip.window.toFront();
            toolTip.setVisible(inventorySlot, true);
        }
    }

    @Override
    public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
        InventorySlot inventorySlot = (InventorySlot) event.getListenerActor();
        toolTip.setVisible(inventorySlot, false);
        isInside = false;

        currentCoords.set(x, y);
        inventorySlot.localToStageCoordinates(currentCoords);
        touchDown = false;
    }

}
