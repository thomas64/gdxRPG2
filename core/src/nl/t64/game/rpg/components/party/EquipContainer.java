package nl.t64.game.rpg.components.party;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;


class EquipContainer {

    private static final int NUMBER_OF_EQUIPMENT_SLOTS = 14;

    private final Map<String, InventoryItem> equipment;

    private EquipContainer() {
        this.equipment = new HashMap<>(NUMBER_OF_EQUIPMENT_SLOTS);
        this.equipment.put(InventoryGroup.WEAPON.name(), null);
        this.equipment.put(InventoryGroup.SHIELD.name(), null);
        this.equipment.put(InventoryGroup.ACCESSORY.name(), null);
        this.equipment.put(InventoryGroup.HELMET.name(), null);
        this.equipment.put(InventoryGroup.NECKLACE.name(), null);
        this.equipment.put(InventoryGroup.SHOULDERS.name(), null);
        this.equipment.put(InventoryGroup.CHEST.name(), null);
        this.equipment.put(InventoryGroup.CLOAK.name(), null);
        this.equipment.put(InventoryGroup.BRACERS.name(), null);
        this.equipment.put(InventoryGroup.GLOVES.name(), null);
        this.equipment.put(InventoryGroup.RING.name(), null);
        this.equipment.put(InventoryGroup.BELT.name(), null);
        this.equipment.put(InventoryGroup.PANTS.name(), null);
        this.equipment.put(InventoryGroup.BOOTS.name(), null);
    }

    @JsonCreator
    private EquipContainer(@JsonProperty("weapon") String weaponId,
                           @JsonProperty("shield") String shieldId,
                           @JsonProperty("chest") String chestId) {
        this();
        var database = InventoryDatabase.getInstance();
        this.equipment.put(InventoryGroup.WEAPON.name(), database.createInventoryItem(weaponId));
        if (shieldId != null) {
            this.equipment.put(InventoryGroup.SHIELD.name(), database.createInventoryItem(shieldId));
        }
        this.equipment.put(InventoryGroup.CHEST.name(), database.createInventoryItem(chestId));
    }

    Optional<InventoryItem> getInventoryItem(InventoryGroup inventoryGroup) {
        return Optional.ofNullable(equipment.get(inventoryGroup.name()));
    }

    void forceSetInventoryItem(InventoryGroup inventoryGroup, InventoryItem inventoryItem) {
        equipment.replace(inventoryGroup.name(), inventoryItem);
    }

    int getStatValueOf(InventoryGroup inventoryGroup, StatItemId statItemId) {
        return getInventoryItem(inventoryGroup)
                .map(inventoryItem -> inventoryItem.getAttributeOfStatItemId(statItemId))
                .orElse(0);
    }

    int getSkillValueOf(InventoryGroup inventoryGroup, SkillItemId skillItemId) {
        return getInventoryItem(inventoryGroup)
                .map(inventoryItem -> inventoryItem.getAttributeOfSkillItemId(skillItemId))
                .orElse(0);
    }

    int getCalcValueOf(InventoryGroup inventoryGroup, CalcAttributeId calcAttributeId) {
        return getInventoryItem(inventoryGroup)
                .map(inventoryItem -> inventoryItem.getAttributeOfCalcAttributeId(calcAttributeId))
                .orElse(0);
    }

    Stream<InventoryItem> getItemsWithMinimalOf(StatItemId statItemId) {
        return equipment.values()
                        .stream()
                        .filter(Objects::nonNull)
                        .filter(inventoryItem -> inventoryItem.getMinimalAttributeOfStatItemId(statItemId) > 0);
    }

    int getSumOfStat(StatItemId statItemId) {
        return equipment.values()
                        .stream()
                        .filter(Objects::nonNull)
                        .mapToInt(inventoryItem -> inventoryItem.getAttributeOfStatItemId(statItemId))
                        .sum();
    }

    int getSumOfSkill(SkillItemId skillItemId) {
        return equipment.values()
                        .stream()
                        .filter(Objects::nonNull)
                        .mapToInt(inventoryItem -> inventoryItem.getAttributeOfSkillItemId(skillItemId))
                        .sum();
    }

    int getSumOfCalc(CalcAttributeId calcAttributeId) {
        return equipment.values()
                        .stream()
                        .filter(Objects::nonNull)
                        .mapToInt(inventoryItem -> inventoryItem.getAttributeOfCalcAttributeId(calcAttributeId))
                        .sum();
    }

    Optional<SkillItemId> getWeaponSkill() {
        return getInventoryItem(InventoryGroup.WEAPON)
                .map(item -> item.getAttributeOfMinimal(InventoryMinimal.SKILL))
                .map(SkillItemId.class::cast);
    }

    int getBonusProtectionWhenArmorSetIsComplete() {
        return getInventoryItem(InventoryGroup.HELMET)
                .map(inventoryItem -> inventoryItem.id.split("_"))
                .map(parts -> parts[0] + "_" + parts[1])
                .map(this::doesEquippedArmorAllHaveSamePrefix)
                .filter(isCompleteSet -> isCompleteSet)
                .flatMap(isCompleteSet -> getProtectionBonus())
                .orElse(0);
    }

    private boolean doesEquippedArmorAllHaveSamePrefix(String prefix) {
        return equipment.values()
                        .stream()
                        .filter(Objects::nonNull)
                        .filter(inventoryItem -> inventoryItem.group.isPartArmorOfSet())
                        .filter(inventoryItem -> inventoryItem.id.startsWith(prefix))
                        .count() == 9L;
    }

    private Optional<Integer> getProtectionBonus() {
        return getInventoryItem(InventoryGroup.HELMET)
                .map(inventoryItem -> inventoryItem.getAttributeOfCalcAttributeId(CalcAttributeId.PROTECTION));
    }

}
