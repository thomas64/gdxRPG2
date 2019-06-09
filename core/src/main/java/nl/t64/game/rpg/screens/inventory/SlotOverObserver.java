package nl.t64.game.rpg.screens.inventory;

import nl.t64.game.rpg.components.party.InventoryItem;


public interface SlotOverObserver {

    void receive(InventoryItem inventoryItem);

}
