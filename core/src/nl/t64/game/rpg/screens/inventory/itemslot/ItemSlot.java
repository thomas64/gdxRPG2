package nl.t64.game.rpg.screens.inventory.itemslot;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.badlogic.gdx.utils.Scaling;
import nl.t64.game.rpg.Utils;
import nl.t64.game.rpg.components.party.InventoryGroup;
import nl.t64.game.rpg.screens.inventory.tooltip.ItemSlotTooltip;

import java.util.Optional;


public abstract class ItemSlot extends Stack {

    private static final String SPRITE_BACKGROUND = "sprites/inventoryslot.png";
    private static final String SPRITE_SELECTED = "sprites/selected.png";
    private static final String NOTHING = "nothing";
    private static final String BASIC = "basic";
    private static final String FINE = "fine";
    private static final String SPECIALIST = "specialist";
    private static final String MASTERWORK = "masterwork";
    private static final String EPIC = "epic";
    private static final String LEGENDARY = "legendary";

    public final int index;
    public final InventoryGroup filterGroup;
    public final ItemSlotTooltip tooltip;
    public final Stack imagesBackground;

    public ItemSlot(int index, InventoryGroup filterGroup, ItemSlotTooltip tooltip) {
        this.index = index;
        this.filterGroup = filterGroup;
        this.tooltip = tooltip;
        this.imagesBackground = createImageBackground();
        addToStack(this.imagesBackground);
    }

    private Stack createImageBackground() {
        var stack = new Stack();
        var texture = Utils.getResourceManager().getTextureAsset(SPRITE_BACKGROUND);
        var background = new Image(texture);
        background.setName("background");
        stack.add(background);
        stack.add(createTierColor(NOTHING));
        return stack;
    }

    public void select() {
        var texture = Utils.getResourceManager().getTextureAsset(SPRITE_SELECTED);
        var selector = new Image(texture);
        selector.setName("selector");
        super.add(selector);
        tooltip.refresh(this);
    }

    public void deselect() {
        if (isSelected()) {
            super.getChildren().pop();
        } else {
            throw new GdxRuntimeException("Tried to deselect an unselected ItemSlot.");
        }
    }

    public boolean isSelected() {
        String name = super.getChildren().peek().getName();
        return name != null && name.equals("selector");
    }

    public Optional<InventoryImage> getPossibleInventoryImage() {
        if (hasItem()) {
            return Optional.of(getCertainInventoryImage());
        }
        return Optional.empty();
    }

    public InventoryImage getCertainInventoryImage() {
        return (InventoryImage) super.getChild(1);
    }

    public abstract void addToStack(Actor actor);

    public abstract boolean hasItem();

    public abstract boolean isOnHero();

    public abstract boolean doesAcceptItem(InventoryImage draggedItem);

    public abstract void clearStack();

    public abstract void putInSlot(InventoryImage draggedItem);

    public abstract int getAmount();

    public abstract void incrementAmountBy(int amount);

    public abstract void decrementAmountBy(int amount);

    void putItemBack(InventoryImage draggedItem) {
        putInSlot(draggedItem);
    }

    int getHalfOfAmount() {
        return (int) Math.floor(getAmount() / 2f);
    }

    public void setItemColor() {
        imagesBackground.getChildren().pop();
        if (hasItem()) {
            imagesBackground.add(createTierColor());
        } else {
            imagesBackground.add(createTierColor(NOTHING));
        }
    }

    private Image createTierColor() {
        String itemId = getCertainInventoryImage().inventoryItem.getId();
        if (itemId.contains(BASIC)) {
            return createTierColor(BASIC);
        } else if (itemId.contains(FINE)) {
            return createTierColor(FINE);
        } else if (itemId.contains(SPECIALIST)) {
            return createTierColor(SPECIALIST);
        } else if (itemId.contains(MASTERWORK)) {
            return createTierColor(MASTERWORK);
        } else if (itemId.contains(EPIC)) {
            return createTierColor(EPIC);
        } else if (itemId.contains(LEGENDARY)) {
            return createTierColor(LEGENDARY);
        } else {
            return createTierColor(NOTHING);
        }
    }

    private Image createTierColor(String tierColor) {
        var textureRegion = Utils.getResourceManager().getAtlasTexture(tierColor);
        var image = new Image(textureRegion);
        image.setScaling(Scaling.none);
        image.setPosition(1f, 1f);
        image.setName("tier color");
        return image;
    }

}
