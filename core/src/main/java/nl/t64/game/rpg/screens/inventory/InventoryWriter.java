package nl.t64.game.rpg.screens.inventory;


import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import nl.t64.game.rpg.Utils;
import nl.t64.game.rpg.components.party.HeroItem;
import nl.t64.game.rpg.components.party.InventoryItem;
import nl.t64.game.rpg.constants.ScreenType;

import java.util.function.Consumer;


final class InventoryWriter {

    private InventoryWriter() {
        throw new IllegalStateException("InventoryWriter class");
    }

    static void storeToGameData() {
        InventoryScreen inventoryScreen = ((InventoryScreen) Utils.getScreenManager().getScreen(ScreenType.INVENTORY));
        Table inventorySlotsTable = inventoryScreen.inventoryUI.inventorySlotsTable.inventorySlots;
        Table equipSlotsTable = inventoryScreen.inventoryUI.equipSlotsTables.get(DynamicVars.heroIndex).equipSlots;

        setGlobalInventory(inventorySlotsTable);
        setPersonalInventory(equipSlotsTable);
    }

    private static void setGlobalInventory(Table inventorySlotsTable) {
        for (int i = 0; i <= inventorySlotsTable.getChildren().size - 1; i++) {
            InventorySlot slot = (InventorySlot) inventorySlotsTable.getChildren().get(i);
            slot.getPossibleInventoryImage().ifPresentOrElse(
                    addItemToGlobalInventory(i, slot),
                    addNullToGlobalInventory(i));
        }
    }

    private static Consumer<InventoryImage> addItemToGlobalInventory(int index, InventorySlot slot) {
        return inventoryImage -> {
            InventoryItem item = new InventoryItem(inventoryImage.inventoryItem);
            item.setAmount(slot.amount);
            Utils.getGameData().getInventory().forceSetItemAt(index, item);
        };
    }

    private static Runnable addNullToGlobalInventory(int index) {
        return () -> Utils.getGameData().getInventory().forceSetItemAt(index, null);
    }

    private static void setPersonalInventory(Table equipSlotsTable) {
        HeroItem hero = Utils.getGameData().getParty().getHero(DynamicVars.heroIndex);
        for (Actor actor : equipSlotsTable.getChildren()) {
            if (actor instanceof Table) {
                for (Actor deepActor : ((Table) actor).getChildren()) {
                    addToPersonalInventory(hero, (InventorySlot) deepActor);
                }
            } else {
                addToPersonalInventory(hero, (InventorySlot) actor);
            }
        }
    }

    private static void addToPersonalInventory(HeroItem hero, InventorySlot slot) {
        slot.getPossibleInventoryImage().ifPresentOrElse(
                addItemToPersonalInventory(hero, slot),
                addNullToPersonalInventory(hero, slot));
    }

    private static Consumer<InventoryImage> addItemToPersonalInventory(HeroItem hero, InventorySlot slot) {
        return inventoryImage -> {
            InventoryItem item = inventoryImage.inventoryItem;
            hero.forceSetInventoryItem(slot.filterGroup, item);
        };
    }

    private static Runnable addNullToPersonalInventory(HeroItem hero, InventorySlot slot) {
        return () -> hero.forceSetInventoryItem(slot.filterGroup, null);
    }

}
