package nl.t64.game.rpg.screens.inventory;


import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import nl.t64.game.rpg.Utils;
import nl.t64.game.rpg.components.party.HeroItem;
import nl.t64.game.rpg.components.party.InventoryItem;

import java.util.function.Consumer;


final class InventoryWriter {

    private InventoryWriter() {
        throw new IllegalStateException("InventoryWriter class");
    }

    static void storeToGameData(Group group) {
        if (group instanceof Window) {
            setGlobalInventory((Table) group);
        } else if (group.getParent() instanceof Window) {
            setPersonalInventory((Table) group);
        } else if (group.getParent().getParent() instanceof Window) {
            setPersonalInventory((Table) group.getParent());
        } else {
            throw new IllegalStateException("Cannot store inventory to GameData.");
        }
    }

    private static void setGlobalInventory(Table inventorySlotsWindow) {
        for (int i = 0; i < inventorySlotsWindow.getChildren().size - 1; i++) {
            InventorySlot slot = (InventorySlot) inventorySlotsWindow.getChildren().get(i + 1); // zero is the label
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
        HeroItem heroItem = Utils.getGameData().getParty().getHero(0); // todo, fix index.
        for (Actor actor : equipSlotsTable.getChildren()) {
            if (actor instanceof Table) {
                for (Actor deepActor : ((Table) actor).getChildren()) {
                    addToPersonalInventory(heroItem, (InventorySlot) deepActor);
                }
            } else {
                addToPersonalInventory(heroItem, (InventorySlot) actor);
            }
        }
    }

    private static void addToPersonalInventory(HeroItem heroItem, InventorySlot slot) {
        slot.getPossibleInventoryImage().ifPresentOrElse(
                addItemToPersonalInventory(heroItem, slot),
                addNullToPersonalInventory(heroItem, slot));
    }

    private static Consumer<InventoryImage> addItemToPersonalInventory(HeroItem heroItem, InventorySlot slot) {
        return inventoryImage -> {
            InventoryItem item = inventoryImage.inventoryItem;
            heroItem.forceSetInventoryItem(slot.filterGroup, item);
        };
    }

    private static Runnable addNullToPersonalInventory(HeroItem heroItem, InventorySlot slot) {
        return () -> heroItem.forceSetInventoryItem(slot.filterGroup, null);
    }

}
