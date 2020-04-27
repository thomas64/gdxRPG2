package nl.t64.game.rpg.screens.inventory.tooltip;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import nl.t64.game.rpg.screens.inventory.ItemSlot;


public class ItemSlotTooltipListener extends ClickListener {

    private static final float OFFSET_X = 20f;
    private static final float OFFSET_Y = 10f;

    private final ItemSlotTooltip toolTip;
    private final Vector2 currentCoords;
    private final Vector2 offset;
    private boolean isInside;
    private boolean touchDown;

    public ItemSlotTooltipListener(ItemSlotTooltip toolTip) {
        this.toolTip = toolTip;
        this.isInside = false;
        this.touchDown = false;
        this.currentCoords = new Vector2();
        this.offset = new Vector2(OFFSET_X, OFFSET_Y);
    }

    @Override
    public boolean mouseMoved(InputEvent event, float x, float y) {
        ItemSlot itemSlot = (ItemSlot) event.getListenerActor();
        if (isInside) {
            currentCoords.set(x, y);
            itemSlot.localToStageCoordinates(currentCoords);
            toolTip.setPosition(currentCoords.x + offset.x, currentCoords.y + offset.y);
        }
        return false;
    }

    @Override
    public void touchDragged(InputEvent event, float x, float y, int pointer) {
        ItemSlot itemSlot = (ItemSlot) event.getListenerActor();
        toolTip.setVisible(itemSlot, false);
        touchDown = false;
    }

    @Override
    public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
        touchDown = true;
        ItemSlot itemSlot = (ItemSlot) event.getListenerActor();
        toolTip.setVisible(itemSlot, false);
        return true;
    }

    @Override
    public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
        isInside = true;

        if (!touchDown) {
            ItemSlot itemSlot = (ItemSlot) event.getListenerActor();

            currentCoords.set(x, y);
            itemSlot.localToStageCoordinates(currentCoords);

            toolTip.updateDescription(itemSlot);
            toolTip.setPosition(currentCoords.x + offset.x, currentCoords.y + offset.y);
            toolTip.toFront();
            toolTip.setVisible(itemSlot, true);
        }
    }

    @Override
    public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
        ItemSlot itemSlot = (ItemSlot) event.getListenerActor();
        toolTip.setVisible(itemSlot, false);
        isInside = false;

        currentCoords.set(x, y);
        itemSlot.localToStageCoordinates(currentCoords);
        touchDown = false;
    }

}
