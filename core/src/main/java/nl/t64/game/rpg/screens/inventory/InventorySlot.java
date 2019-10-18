package nl.t64.game.rpg.screens.inventory;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.utils.Align;
import nl.t64.game.rpg.Utils;
import nl.t64.game.rpg.components.party.InventoryGroup;

import java.util.Optional;


class InventorySlot extends Stack {

    private static final String SPRITE_BACKGROUND = "sprites/inventoryslot.png";
    private static final int STANDARD_STACK_SIZE = 2;
    private static final Color TRANSPARENT = new Color(1f, 1f, 1f, 0.3f);

    final Label amountLabel;
    final InventoryGroup filterGroup;
    private final Stack imagesBackground;
    int amount = 0;

    InventorySlot(InventoryGroup filterGroup) {
        this.filterGroup = filterGroup;
        this.imagesBackground = createImageBackground();
        this.imagesBackground.add(createShadowImage(this.filterGroup));
        addToStack(this.imagesBackground);
        this.amountLabel = createNumberOfItemsLabel();
        addToStack(this.amountLabel);
    }

    InventorySlot() {
        this.filterGroup = InventoryGroup.EVERYTHING;
        this.imagesBackground = createImageBackground();
        addToStack(this.imagesBackground);
        this.amountLabel = createNumberOfItemsLabel();
        addToStack(this.amountLabel);
    }

    private static Image createShadowImage(InventoryGroup inventoryGroup) {
        String groupId = inventoryGroup.name().toLowerCase();
        TextureRegion textureRegion = Utils.getResourceManager().getAtlasTexture(groupId);
        Image image = new Image(textureRegion);
        image.setColor(TRANSPARENT);
        return image;
    }

    private Stack createImageBackground() {
        var stack = new Stack();
        var texture = Utils.getResourceManager().getTextureAsset(SPRITE_BACKGROUND);
        var background = new Image(texture);
        stack.add(background);
        return stack;
    }

    private Label createNumberOfItemsLabel() {
        var labelStyle = new Label.LabelStyle(new BitmapFont(), Color.WHITE);
        var label = new Label(String.valueOf(amount), labelStyle);
        label.setAlignment(Align.bottomRight);
        label.setVisible(false);
        return label;
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
        refreshSlot();
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

    boolean isOnHero() {
        return !filterGroup.equals(InventoryGroup.EVERYTHING);
    }

    boolean doesAcceptItem(InventoryImage draggedItem) {
        if (filterGroup.equals(InventoryGroup.EVERYTHING)) {
            return true;
        }
        if (filterGroup.equals(draggedItem.inventoryGroup)) {
            return doesHeroAcceptItem(draggedItem);
        }
        return false;
    }

    private boolean doesHeroAcceptItem(InventoryImage draggedItem) {
        Optional<String> message = InventoryUtils.selectedHero.isAbleToEquip(draggedItem.inventoryItem);
        if (message.isPresent()) {
            new MessageDialog(message.get()).show(getStage());
            return false;
        } else {
            return true;
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
        final boolean shouldAmountLabelBeVisible = amount >= STANDARD_STACK_SIZE;
        amountLabel.setVisible(shouldAmountLabelBeVisible);
    }

    private void setVisibilityOfShadow() {
        if (hasShadow()) {
            final boolean shouldShadowBeVisible = amount < 1;
            setShadowVisible(shouldShadowBeVisible);
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
