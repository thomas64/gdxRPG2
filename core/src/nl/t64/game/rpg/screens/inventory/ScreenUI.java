package nl.t64.game.rpg.screens.inventory;

import nl.t64.game.rpg.screens.inventory.equipslot.EquipSlotsTables;
import nl.t64.game.rpg.screens.inventory.inventoryslot.InventorySlotsTable;


public interface ScreenUI {

    EquipSlotsTables getEquipSlotsTables();

    InventorySlotsTable getInventorySlotsTable();

}
