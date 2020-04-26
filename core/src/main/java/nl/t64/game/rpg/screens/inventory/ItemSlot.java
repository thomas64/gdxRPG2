package nl.t64.game.rpg.screens.inventory;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import nl.t64.game.rpg.Utils;
import nl.t64.game.rpg.components.party.InventoryGroup;

import java.util.Optional;


public abstract class ItemSlot extends Stack {

    private static final String SPRITE_BACKGROUND = "sprites/inventoryslot.png";

    final InventoryGroup filterGroup;
    final Stack imagesBackground;

    ItemSlot(InventoryGroup filterGroup) {
        this.filterGroup = filterGroup;
        this.imagesBackground = createImageBackground();
        addToStack(this.imagesBackground);
    }

    private Stack createImageBackground() {
        var stack = new Stack();
        var texture = Utils.getResourceManager().getTextureAsset(SPRITE_BACKGROUND);
        var background = new Image(texture);
        stack.add(background);
        return stack;
    }

    Optional<InventoryImage> getPossibleInventoryImage() {
        if (hasItem()) {
            return Optional.of(getCertainInventoryImage());
        }
        return Optional.empty();
    }

    public abstract InventoryImage getCertainInventoryImage();

    public abstract void addToStack(Actor actor);

    public abstract boolean hasItem();

    public abstract boolean isOnHero();

    public abstract boolean doesAcceptItem(InventoryImage draggedItem);

    abstract void clearStack();

    abstract void putInSlot(Actor actor);

    abstract int getAmount();

    abstract void incrementAmountBy(int amount);

    abstract void decrementAmountBy(int amount);

    void putSingleItemInEmptySlot(Actor actor) {
        putInSlot(actor);
    }

    void putItemBack(Actor actor) {
        putInSlot(actor);
    }

    void incrementAmount() {
        incrementAmountBy(1);
    }

    void decrementAmount() {
        decrementAmountBy(1);
    }

}
