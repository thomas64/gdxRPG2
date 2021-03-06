package nl.t64.game.rpg.screens.inventory.itemslot;

import nl.t64.game.rpg.Utils;
import nl.t64.game.rpg.audio.AudioCommand;
import nl.t64.game.rpg.audio.AudioEvent;
import nl.t64.game.rpg.components.party.InventoryDatabase;
import nl.t64.game.rpg.components.party.InventoryGroup;
import nl.t64.game.rpg.components.party.InventoryItem;
import nl.t64.game.rpg.components.party.SkillItemId;
import nl.t64.game.rpg.screens.inventory.InventoryUtils;
import nl.t64.game.rpg.screens.inventory.messagedialog.MessageDialog;

import java.util.Optional;


public class ItemSlotsExchanger {

    private final InventoryImage draggedItem;
    private final ItemSlot sourceSlot;
    private final ItemSlot targetSlot;
    private boolean isSuccessfullyExchanged;

    public ItemSlotsExchanger(InventoryImage draggedItem, ItemSlot sourceSlot, ItemSlot targetSlot) {
        this.draggedItem = InventoryImage.copyOf(draggedItem);
        this.sourceSlot = sourceSlot;
        this.targetSlot = targetSlot;
        this.isSuccessfullyExchanged = false;
    }

    public void exchange() {
        if (targetSlot.doesAcceptItem(draggedItem)) {
            handleExchange();
        } else {
            sourceSlot.putItemBack(draggedItem);
        }
    }

    private void handleExchange() {
        if (isShopPurchase()) {
            handlePurchase();
        } else if (isShopBarter()) {
            handleBarter();
        } else {
            handlePossibleExchange();
        }
    }

    private void handlePurchase() {
        final int totalMerchant = Utils.getGameData().getParty().getSumOfSkill(SkillItemId.MERCHANT);
        final int totalPrice = draggedItem.inventoryItem.getBuyPriceTotal(totalMerchant);
        if (Utils.getGameData().getInventory().hasEnoughOfItem("gold", totalPrice)) {
            handlePossibleExchange();
            if (isSuccessfullyExchanged) {
                InventoryUtils.getScreenUI().getInventorySlotsTable().removeResource("gold", totalPrice);
            }
        } else {
            String errorMessage = "I'm sorry. You don't seem to have enough gold.";
            new MessageDialog(errorMessage).show(sourceSlot.getStage(), AudioEvent.SE_MENU_ERROR);
            sourceSlot.putItemBack(draggedItem);
        }
    }

    private void handleBarter() {
        final int totalMerchant = Utils.getGameData().getParty().getSumOfSkill(SkillItemId.MERCHANT);
        final int totalValue = draggedItem.inventoryItem.getSellValueTotal(totalMerchant);
        if (totalValue == 0) {
            String errorMessage = "I'm sorry. I can't accept that.";
            new MessageDialog(errorMessage).show(sourceSlot.getStage(), AudioEvent.SE_MENU_ERROR);
            sourceSlot.putItemBack(draggedItem);
            return;
        }

        if (Utils.getGameData().getInventory().hasRoomForResource("gold")) {
            handlePossibleExchange();
            if (isSuccessfullyExchanged) {
                InventoryItem gold = InventoryDatabase.getInstance().createInventoryItem("gold", totalValue);
                InventoryUtils.getScreenUI().getInventorySlotsTable().addResource(gold);
            }
        } else {
            String errorMessage = "I'm sorry. You don't seem to have room for gold.";
            new MessageDialog(errorMessage).show(sourceSlot.getStage(), AudioEvent.SE_MENU_ERROR);
            sourceSlot.putItemBack(draggedItem);
        }
    }

    private void handlePossibleExchange() {
        Optional<InventoryImage> possibleItemAtTarget = targetSlot.getPossibleInventoryImage();
        possibleItemAtTarget.ifPresentOrElse(this::putItemInFilledSlot,
                                             this::putItemInEmptySlot);
    }

    private void putItemInFilledSlot(InventoryImage itemAtTarget) {
        if (targetSlot.equals(sourceSlot)
            || (draggedItem.isSameItemAs(itemAtTarget)
                && draggedItem.isStackable())) {
            doAudio();
            targetSlot.incrementAmountBy(draggedItem.getAmount());
            isSuccessfullyExchanged = true;
        } else {
            swapStacks();
        }
    }

    private void swapStacks() {
        if (doTargetAndSourceAcceptEachOther()
            && !sourceSlot.hasItem()) {
            doAudio();
            sourceSlot.addToStack(targetSlot.getCertainInventoryImage());
            targetSlot.addToStack(draggedItem);
            isSuccessfullyExchanged = true;
        } else {
            sourceSlot.putItemBack(draggedItem);
            isSuccessfullyExchanged = false;
        }
    }

    private void putItemInEmptySlot() {
        doAudio();
        targetSlot.putInSlot(draggedItem);
        isSuccessfullyExchanged = true;
    }

    private void doAudio() {
        if (isShopPurchase()) {
            Utils.getAudioManager().handle(AudioCommand.SE_PLAY_ONCE, AudioEvent.SE_COINS_BUY);
        } else if (isShopBarter()) {
            Utils.getAudioManager().handle(AudioCommand.SE_PLAY_ONCE, AudioEvent.SE_COINS_SELL);
        } else if (isEquipingOrDequiping()) {
            Utils.getAudioManager().handle(AudioCommand.SE_PLAY_ONCE, AudioEvent.SE_EQUIP);
        } else if (isSameSlotOrBox()) {
            // do nothing
        } else {
            Utils.getAudioManager().handle(AudioCommand.SE_PLAY_ONCE, AudioEvent.SE_TAKE);
        }
    }

    private boolean isShopPurchase() {
        return sourceSlot.filterGroup.equals(InventoryGroup.SHOP_ITEM)
               && !targetSlot.filterGroup.equals(InventoryGroup.SHOP_ITEM);
    }

    private boolean isShopBarter() {
        return !sourceSlot.filterGroup.equals(InventoryGroup.SHOP_ITEM)
               && targetSlot.filterGroup.equals(InventoryGroup.SHOP_ITEM);
    }

    private boolean doTargetAndSourceAcceptEachOther() {
        return !(sourceSlot.filterGroup.equals(InventoryGroup.SHOP_ITEM)
                 || targetSlot.filterGroup.equals(InventoryGroup.SHOP_ITEM))
               && targetSlot.doesAcceptItem(draggedItem)
               && sourceSlot.doesAcceptItem(targetSlot.getCertainInventoryImage());
    }

    private boolean isEquipingOrDequiping() {
        return (!sourceSlot.isOnHero() && targetSlot.isOnHero())
               || (sourceSlot.isOnHero() && !targetSlot.isOnHero());
    }

    private boolean isSameSlotOrBox() {
        return targetSlot.equals(sourceSlot)
               || targetSlot.filterGroup.equals(sourceSlot.filterGroup);
    }

}
