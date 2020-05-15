package nl.t64.game.rpg.screens.inventory;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.Align;
import nl.t64.game.rpg.components.party.InventoryContainer;
import nl.t64.game.rpg.components.party.InventoryGroup;


public class InventorySlot extends ItemSlot {

    public final Label amountLabel;
    private final int index;
    private final InventoryContainer inventory;

    public InventorySlot(int index, InventoryGroup filterGroup, InventoryContainer inventory) {
        super(filterGroup);
        this.index = index;
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
    public InventoryImage getCertainInventoryImage() {
        return (InventoryImage) super.getChildren().get(1);
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
        return true;
    }

    @Override
    void clearStack() {
        if (hasItem()) {
            super.getChildren().removeIndex(1);
            inventory.clearItemAt(index);
            refreshSlot();
        }
    }

    @Override
    void putInSlot(Actor actor) {
        if (hasItem()) {
            incrementAmountBy(1);
        } else {
            addToStack(actor);
        }
    }

    @Override
    int getAmount() {
        return inventory.getAmountOfItemAt(index);
    }

    @Override
    void incrementAmountBy(int amount) {
        inventory.incrementAmountAt(index, amount);
        refreshSlot();
    }

    @Override
    void decrementAmountBy(int amount) {
        if (amount == 0) {
            // Do nothing, because this only happens when decremented by half when there are only 2 items left.
            // 2 minus start decrement is 1. 1 divided by 2 is 0.
            // So it is already halved and doesn't need be decremented again.
        } else if (getAmount() > 1) {
            inventory.decrementAmountAt(index, amount);
        } else {
            inventory.clearItemAt(index);
        }
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
