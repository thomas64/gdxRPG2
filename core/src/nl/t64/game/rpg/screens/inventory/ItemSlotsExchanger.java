package nl.t64.game.rpg.screens.inventory;

import nl.t64.game.rpg.Utils;
import nl.t64.game.rpg.components.party.InventoryDatabase;
import nl.t64.game.rpg.components.party.InventoryGroup;
import nl.t64.game.rpg.components.party.InventoryItem;
import nl.t64.game.rpg.components.party.SkillItemId;

import java.util.Optional;


class ItemSlotsExchanger {

    private final InventoryImage draggedItem;
    private final ItemSlot sourceSlot;
    private final ItemSlot targetSlot;
    private boolean isSuccessfullyExchanged;

    ItemSlotsExchanger(InventoryImage draggedItem, ItemSlot sourceSlot, ItemSlot targetSlot) {
        this.draggedItem = draggedItem;
        this.sourceSlot = sourceSlot;
        this.targetSlot = targetSlot;
        this.isSuccessfullyExchanged = false;
    }

    void exchange() {
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
        if (Utils.getGameData().getInventory().hasEnoughOfResource("gold", totalPrice)) {
            handlePossibleExchange();
            if (isSuccessfullyExchanged) {
                InventoryUtils.getScreenUI().getInventorySlotsTable().removeResource("gold", totalPrice);
            }
        } else {
            new MessageDialog("I'm sorry. You don't seem to have enough gold.").show(sourceSlot.getStage());
            sourceSlot.putItemBack(draggedItem);
        }
    }

    private void handleBarter() {
        final int totalMerchant = Utils.getGameData().getParty().getSumOfSkill(SkillItemId.MERCHANT);
        final int totalValue = draggedItem.inventoryItem.getSellValueTotal(totalMerchant);
        if (totalValue == 0) {
            new MessageDialog("I'm sorry. I can't accept that.").show(sourceSlot.getStage());
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
            new MessageDialog("I'm sorry. You don't seem to have room for gold.").show(sourceSlot.getStage());
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
            targetSlot.incrementAmountBy(draggedItem.getAmount());
            isSuccessfullyExchanged = true;
        } else {
            swapStacks();
        }
    }

    private void swapStacks() {
        if (doTargetAndSourceAcceptEachOther()
            && !sourceSlot.hasItem()) {
            sourceSlot.addToStack(targetSlot.getCertainInventoryImage());
            targetSlot.addToStack(draggedItem);
            isSuccessfullyExchanged = true;
        } else {
            sourceSlot.putItemBack(draggedItem);
            isSuccessfullyExchanged = false;
        }
    }

    private void putItemInEmptySlot() {
        targetSlot.putInSlot(draggedItem);
        isSuccessfullyExchanged = true;
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

}
