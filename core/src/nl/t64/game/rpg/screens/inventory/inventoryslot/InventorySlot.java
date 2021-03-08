package nl.t64.game.rpg.screens.inventory.inventoryslot;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.Align;
import nl.t64.game.rpg.components.party.InventoryContainer;
import nl.t64.game.rpg.components.party.InventoryGroup;
import nl.t64.game.rpg.screens.inventory.itemslot.InventoryImage;
import nl.t64.game.rpg.screens.inventory.itemslot.ItemSlot;
import nl.t64.game.rpg.screens.inventory.tooltip.ItemSlotTooltip;


public class InventorySlot extends ItemSlot {

    private final Label amountLabel;
    private final InventoryContainer inventory;

    public InventorySlot(int index, InventoryGroup filterGroup, ItemSlotTooltip tooltip, InventoryContainer inventory) {
        super(index, filterGroup, tooltip);
        this.inventory = inventory;
        this.amountLabel = createAmountLabel();
        addToStack(this.amountLabel);
    }

    private Label createAmountLabel() {
        var labelStyle = new Label.LabelStyle(new BitmapFont(), Color.WHITE);
        var label = new Label(String.valueOf(getAmount()), labelStyle);
        label.setAlignment(Align.bottomRight);
        label.setVisible(false);
        return label;
    }

    @Override
    public void addToStack(Actor actor) {
        if (actor instanceof InventoryImage inventoryImage) {
            super.addActorBefore(super.getChildren().peek(), actor);
            inventory.forceSetItemAt(index, inventoryImage.inventoryItem);
            refreshSlot();
        } else {
            super.add(actor);
        }
    }

    @Override
    public boolean hasItem() {
        return inventory.getItemAt(index).isPresent();
    }

    @Override
    public boolean isOnHero() {
        return false;
    }

    @Override
    public boolean doesAcceptItem(InventoryImage draggedItem) {
        return !filterGroup.equals(InventoryGroup.LOOT_ITEM);
    }

    @Override
    public void clearStack() {
        if (hasItem()) {
            super.getChildren().removeIndex(1);
            inventory.clearItemAt(index);
            refreshSlot();
        }
    }

    @Override
    public void putInSlot(InventoryImage draggedItem) {
        if (hasItem()) {
            incrementAmountBy(draggedItem.getAmount());
        } else {
            addToStack(draggedItem);
        }
    }

    @Override
    public int getAmount() {
        return inventory.getAmountOfItemAt(index);
    }

    @Override
    public void incrementAmountBy(int amount) {
        inventory.incrementAmountAt(index, amount);
        refreshSlot();
    }

    @Override
    public void decrementAmountBy(int amount) {
        inventory.decrementAmountAt(index, amount);
        refreshSlot();
    }

    private void refreshSlot() {
        final int amount = getAmount();
        amountLabel.setText(String.valueOf(amount));
        setVisibilityOfAmountLabel(amount);
        setItemColor();
    }

    private void setVisibilityOfAmountLabel(int amount) {
        final boolean shouldAmountLabelBeVisible = amount >= 2;
        amountLabel.setVisible(shouldAmountLabelBeVisible);
    }

}
