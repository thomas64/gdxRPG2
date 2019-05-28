package nl.t64.game.rpg.components.inventory;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static nl.t64.game.rpg.components.inventory.InventoryGroup.*;


public class PersonalContainer {

    private final Map<InventoryGroup, InventoryItem> equipment;

    private PersonalContainer() {
        this.equipment = new HashMap<>(14);
        this.equipment.put(WEAPON, null);
        this.equipment.put(SHIELD, null);
        this.equipment.put(ACCESSORY, null);
        this.equipment.put(HELMET, null);
        this.equipment.put(SHOULDERS, null);
        this.equipment.put(NECKLACE, null);
        this.equipment.put(CLOAK, null);
        this.equipment.put(CHEST, null);
        this.equipment.put(BRACERS, null);
        this.equipment.put(GLOVES, null);
        this.equipment.put(RING, null);
        this.equipment.put(BELT, null);
        this.equipment.put(PANTS, null);
        this.equipment.put(BOOTS, null);
    }

    @JsonCreator
    private PersonalContainer(@JsonProperty("weapon") String weaponId,
                              @JsonProperty("shield") String shieldId,
                              @JsonProperty("chest") String chestId) {
        this();
        var database = InventoryDatabase.getInstance();
        this.equipment.put(WEAPON, database.getInventoryItem(weaponId));
        if (shieldId != null) {
            this.equipment.put(SHIELD, database.getInventoryItem(shieldId));
        }
        this.equipment.put(CHEST, database.getInventoryItem(chestId));
    }

    public int getSumOfProtectionWithShield() {
        return equipment.values()
                        .stream()
                        .filter(Objects::nonNull)
                        .mapToInt(item -> item.protection)
                        .sum();
    }

}
