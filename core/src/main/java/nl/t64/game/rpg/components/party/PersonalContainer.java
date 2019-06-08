package nl.t64.game.rpg.components.party;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import static nl.t64.game.rpg.components.party.InventoryGroup.*;


class PersonalContainer {

    private static final int NUMBER_OF_EQUIPMENT_SLOTS = 14;

    private final Map<String, InventoryItem> equipment;

    private PersonalContainer() {
        this.equipment = new HashMap<>(NUMBER_OF_EQUIPMENT_SLOTS);
        this.equipment.put(WEAPON.name(), null);
        this.equipment.put(SHIELD.name(), null);
        this.equipment.put(ACCESSORY.name(), null);
        this.equipment.put(HELMET.name(), null);
        this.equipment.put(NECKLACE.name(), null);
        this.equipment.put(SHOULDERS.name(), null);
        this.equipment.put(CHEST.name(), null);
        this.equipment.put(CLOAK.name(), null);
        this.equipment.put(BRACERS.name(), null);
        this.equipment.put(GLOVES.name(), null);
        this.equipment.put(RING.name(), null);
        this.equipment.put(BELT.name(), null);
        this.equipment.put(PANTS.name(), null);
        this.equipment.put(BOOTS.name(), null);
    }

    @JsonCreator
    private PersonalContainer(@JsonProperty("weapon") String weaponId,
                              @JsonProperty("shield") String shieldId,
                              @JsonProperty("chest") String chestId) {
        this();
        var database = InventoryDatabase.getInstance();
        this.equipment.put(WEAPON.name(), database.getInventoryItem(weaponId));
        if (shieldId != null) {
            this.equipment.put(SHIELD.name(), database.getInventoryItem(shieldId));
        }
        this.equipment.put(CHEST.name(), database.getInventoryItem(chestId));
    }

    Optional<InventoryItem> getInventoryItem(InventoryGroup inventoryGroup) {
        return Optional.ofNullable(equipment.get(inventoryGroup.name()));
    }

    void forceSetInventoryItem(InventoryGroup inventoryGroup, InventoryItem inventoryItem) {
        equipment.replace(inventoryGroup.name(), inventoryItem);
    }

    int getSumOfProtectionWithShield() {
        return equipment.values()
                        .stream()
                        .filter(Objects::nonNull)
                        .mapToInt(item -> item.protection)
                        .sum();
    }

    int getWeaponBaseHit() {
        return getInventoryItem(WEAPON).map(item -> item.baseHit).orElse(0);
    }

    int getWeaponDamage() {
        return getInventoryItem(WEAPON).map(item -> item.damage).orElse(0);
    }

    int getShieldDefense() {
        return getInventoryItem(SHIELD).map(item -> item.defense).orElse(0);
    }

}
