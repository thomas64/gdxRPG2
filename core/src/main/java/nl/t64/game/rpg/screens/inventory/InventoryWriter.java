package nl.t64.game.rpg.screens.inventory;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import nl.t64.game.rpg.Utils;
import nl.t64.game.rpg.components.party.HeroItem;
import nl.t64.game.rpg.components.party.InventoryItem;

import java.util.function.Consumer;
import java.util.stream.IntStream;


final class InventoryWriter {

    private InventoryWriter() {
        throw new IllegalStateException("InventoryWriter class");
    }

    static void storeToGameData() {
        ScreenUI screenUI = InventoryUtils.getScreenUI();
        Table inventorySlotsTable = screenUI.getInventorySlotsTable().inventorySlots;
        Table equipSlotsTable = screenUI.getEquipSlotsTables().get(InventoryUtils.getSelectedHero().getId()).equipSlots;

        setInventoryContainer(inventorySlotsTable);
        setEquipContainer(equipSlotsTable);
    }

    private static void setInventoryContainer(Table inventorySlotsTable) {
        IntStream.range(0, inventorySlotsTable.getChildren().size)
                 .forEach(i -> {
                     InventorySlot slot = (InventorySlot) inventorySlotsTable.getChildren().get(i);
                     slot.getPossibleInventoryImage().ifPresentOrElse(addItemToInventoryContainer(i, slot),
                                                                      addNullToInventoryContainer(i));
                 });
    }

    private static Consumer<InventoryImage> addItemToInventoryContainer(int index, InventorySlot slot) {
        return inventoryImage -> {
            InventoryItem inventoryItem = new InventoryItem(inventoryImage.inventoryItem);
            inventoryItem.setAmount(slot.amount);
            Utils.getGameData().getInventory().forceSetItemAt(index, inventoryItem);
        };
    }

    private static Runnable addNullToInventoryContainer(int index) {
        return () -> Utils.getGameData().getInventory().forceSetItemAt(index, null);
    }

    private static void setEquipContainer(Table equipSlotsTable) {
        for (Actor actor : equipSlotsTable.getChildren()) {
            if (actor instanceof Table) {
                for (Actor deepActor : ((Table) actor).getChildren()) {
                    addToEquipContainer(InventoryUtils.getSelectedHero(), (InventorySlot) deepActor);
                }
            } else {
                addToEquipContainer(InventoryUtils.getSelectedHero(), (InventorySlot) actor);
            }
        }
    }

    private static void addToEquipContainer(HeroItem hero, InventorySlot slot) {
        slot.getPossibleInventoryImage().ifPresentOrElse(addItemToEquipContainer(hero, slot),
                                                         addNullToEquipContainer(hero, slot));
    }

    private static Consumer<InventoryImage> addItemToEquipContainer(HeroItem hero, InventorySlot slot) {
        return inventoryImage -> {
            InventoryItem inventoryItem = inventoryImage.inventoryItem;
            hero.forceSetInventoryItem(slot.filterGroup, inventoryItem);
        };
    }

    private static Runnable addNullToEquipContainer(HeroItem hero, InventorySlot slot) {
        return () -> hero.forceSetInventoryItem(slot.filterGroup, null);
    }

}
