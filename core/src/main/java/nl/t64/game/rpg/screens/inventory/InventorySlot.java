package nl.t64.game.rpg.screens.inventory;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.utils.Align;
import nl.t64.game.rpg.Utils;
import nl.t64.game.rpg.components.inventory.InventoryGroup;

import java.util.Optional;


class InventorySlot extends Stack {

    private static final String SPRITE_BACKGROUND = "sprites/inventoryslot.png";
    private static final int STANDARD_STACK_SIZE = 2;
    private static final Color TRANSPARENT = new Color(1f, 1f, 1f, 0.3f);

    private Stack imagesBackground;
    Label amountLabel;
    int amount = 0;
    InventoryGroup filterGroup;

    InventorySlot(InventoryGroup filterGroup) {
        this();
        this.filterGroup = filterGroup;
        this.imagesBackground.add(createShadowImage(filterGroup));
    }

    InventorySlot() {
        this.filterGroup = InventoryGroup.EVERYTHING;
        createImageBackground();
        createNumberOfItemsLabel();
    }

    private static Image createShadowImage(InventoryGroup inventoryGroup) {
        String groupId = inventoryGroup.name().toLowerCase();
        TextureRegion textureRegion = Utils.getResourceManager().getAtlasTexture(groupId);
        Image image = new Image(textureRegion);
        image.setColor(TRANSPARENT);
        return image;
    }

    private void createImageBackground() {
        imagesBackground = new Stack();
        var texture = Utils.getResourceManager().getTextureAsset(SPRITE_BACKGROUND);
        var background = new Image(texture);
        imagesBackground.add(background);
        addToStack(imagesBackground);
    }

    private void createNumberOfItemsLabel() {
        var labelSkin = new Skin();
        labelSkin.add("default", new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        amountLabel = new Label(String.valueOf(amount), labelSkin);
        amountLabel.setAlignment(Align.bottomRight);
        amountLabel.setVisible(false);
        addToStack(amountLabel);
    }

    boolean hasItem() {
        return super.getChildren().size > STANDARD_STACK_SIZE;
    }

    void addToStack(Actor actor) {
        if (isActorAnInventoryItem(actor)) {
            super.addActorBefore(super.getChildren().peek(), actor);
            refreshSlot();
        } else {
            super.add(actor);
        }
    }

    void clearStack() {
        if (hasItem()) {
            super.getChildren().removeIndex(1);
        }
        amount = 0;
    }

    Optional<InventoryImage> getPossibleInventoryImage() {
        if (hasItem()) {
            return Optional.of(getCertainInventoryImage());
        }
        return Optional.empty();
    }

    InventoryImage getCertainInventoryImage() {
        return (InventoryImage) super.getChildren().items[super.getChildren().size - 2];
    }

    boolean doesAcceptInventoryGroup(InventoryGroup itemGroup) {
        if (filterGroup.equals(InventoryGroup.EVERYTHING)) {
            return true;
        } else {
            return filterGroup == itemGroup;
        }
    }

    void putItemInEmptySlot(Actor actor) {
        putInSlot(actor);
    }

    void putItemBack(Actor actor) {
        putInSlot(actor);
    }

    void incrementAmount() {
        amount++;
        refreshSlot();
    }

    void decrementAmount() {
        amount--;
        refreshSlot();
    }

    private void putInSlot(Actor actor) {
        amount++;
        if (amount > 1) {
            refreshSlot();
        } else {
            addToStack(actor);
        }
    }

    private void refreshSlot() {
        amountLabel.setText(String.valueOf(amount));
        setVisibilityOfAmountLabel();
        setVisibilityOfShadow();
    }

    private void setVisibilityOfAmountLabel() {
        if (amount < STANDARD_STACK_SIZE) {
            amountLabel.setVisible(false);
        } else {
            amountLabel.setVisible(true);
        }
    }

    private void setVisibilityOfShadow() {
        if (hasShadow()) {
            if (amount >= 1) {
                setShadowVisible(false);
            } else {
                setShadowVisible(true);
            }
        }
    }

    private void setShadowVisible(boolean visible) {
        imagesBackground.getChildren().get(1).setVisible(visible);
    }

    private boolean hasShadow() {
        return imagesBackground.getChildren().size > 1;
    }

    private boolean isActorAnInventoryItem(Actor actor) {
        return !actor.equals(imagesBackground) && !actor.equals(amountLabel);
    }

}