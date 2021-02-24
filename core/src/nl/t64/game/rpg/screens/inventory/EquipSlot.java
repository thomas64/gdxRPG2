package nl.t64.game.rpg.screens.inventory;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import nl.t64.game.rpg.Utils;
import nl.t64.game.rpg.audio.AudioEvent;
import nl.t64.game.rpg.components.party.HeroItem;
import nl.t64.game.rpg.components.party.InventoryGroup;


class EquipSlot extends ItemSlot {

    private static final Color TRANSPARENT = new Color(1f, 1f, 1f, 0.3f);

    private final HeroItem heroItem;

    EquipSlot(InventoryGroup filterGroup, HeroItem heroItem) {
        super(filterGroup);
        this.heroItem = heroItem;
        this.imagesBackground.addActorBefore(this.imagesBackground.getChildren().peek(),
                                             createShadowImage(this.filterGroup));
    }

    private static Image createShadowImage(InventoryGroup inventoryGroup) {
        String groupId = inventoryGroup.name().toLowerCase();
        TextureRegion textureRegion = Utils.getResourceManager().getAtlasTexture(groupId);
        Image shadow = new Image(textureRegion);
        shadow.setColor(TRANSPARENT);
        shadow.setName("shadow");
        return shadow;
    }

    @Override
    public int getIndex() {
        throw new IllegalCallerException("EquipSlot does not contain index.");
    }

    @Override
    public InventoryImage getCertainInventoryImage() {
        return (InventoryImage) super.getChildren().peek();
    }

    @Override
    public void addToStack(Actor actor) {
        super.add(actor);
        if (actor instanceof InventoryImage inventoryImage) {
            heroItem.forceSetInventoryItemFor(filterGroup, inventoryImage.inventoryItem);
            refreshSlot();
        }
    }

    @Override
    public boolean hasItem() {
        return heroItem.getInventoryItem(filterGroup).isPresent();
    }

    @Override
    public boolean isOnHero() {
        return true;
    }

    @Override
    public boolean doesAcceptItem(InventoryImage draggedItem) {
        if (filterGroup.equals(draggedItem.inventoryGroup)) {
            return doesHeroAcceptItem(draggedItem);
        }
        return false;
    }

    @Override
    public void clearStack() {
        if (hasItem()) {
            super.getChildren().pop();
            heroItem.clearInventoryItemFor(filterGroup);
            refreshSlot();
        }
    }

    @Override
    void putInSlot(InventoryImage draggedItem) {
        addToStack(draggedItem);
    }

    @Override
    int getAmount() {
        return hasItem() ? 1 : 0;
    }

    @Override
    void incrementAmountBy(int amount) {
        throw new IllegalCallerException("EquipSlot amount cannot be incremented.");
    }

    @Override
    void decrementAmountBy(int amount) {
        heroItem.clearInventoryItemFor(filterGroup);
        refreshSlot();
    }

    private boolean doesHeroAcceptItem(InventoryImage draggedItem) {
        return heroItem.createMessageIfNotAbleToEquip(draggedItem.inventoryItem)
                       .map(this::showDialogHeroDoesNotAccept)
                       .orElse(true);
    }

    private boolean showDialogHeroDoesNotAccept(String message) {
        new MessageDialog(message).show(getStage(), AudioEvent.SE_MENU_ERROR);
        return false;
    }

    private void refreshSlot() {
        setVisibilityOfShadow();
        setItemColor();
    }

    private void setVisibilityOfShadow() {
        final boolean shouldShadowBeVisible = !hasItem();
        setShadowVisible(shouldShadowBeVisible);
    }

    private void setShadowVisible(boolean visible) {
        imagesBackground.getChild(1).setVisible(visible);  // 1 is index of actor named shadow
    }

}
