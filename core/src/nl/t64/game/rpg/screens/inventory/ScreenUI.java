package nl.t64.game.rpg.screens.inventory;

import com.badlogic.gdx.scenes.scene2d.ui.Window;

import java.util.Map;


public interface ScreenUI {

    Window getEquipWindow();

    Map<String, EquipSlotsTable> getEquipSlotsTables();

    InventorySlotsTable getInventorySlotsTable();

}
