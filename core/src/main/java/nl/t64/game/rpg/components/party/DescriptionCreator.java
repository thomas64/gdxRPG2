package nl.t64.game.rpg.components.party;

import lombok.AllArgsConstructor;

import java.util.ArrayList;
import java.util.List;


@AllArgsConstructor
public class DescriptionCreator {

    private InventoryItem inventoryItem;

    public List<InventoryDescription> createItemDescriptionComparingToHero(HeroItem hero) {
        List<InventoryDescription> attributes = new ArrayList<>();
        attributes.add(new InventoryDescription(inventoryItem.group,
                                                inventoryItem.name,
                                                inventoryItem,
                                                hero));

        for (InventoryMinimal minimal : InventoryMinimal.values()) {
            attributes.add(new InventoryDescription(minimal,
                                                    inventoryItem.getAttributeOfMinimal(minimal),
                                                    inventoryItem,
                                                    hero));
        }
        for (CalcType calcType : CalcType.values()) {
            attributes.add(new InventoryDescription(calcType,
                                                    inventoryItem.getAttributeOfCalcType(calcType),
                                                    inventoryItem,
                                                    hero));
        }
        for (StatType statType : StatType.values()) {
            attributes.add(new InventoryDescription(statType,
                                                    inventoryItem.getAttributeOfStatType(statType),
                                                    inventoryItem,
                                                    hero));
        }
        for (SkillType skillType : SkillType.values()) {
            attributes.add(new InventoryDescription(skillType,
                                                    inventoryItem.getAttributeOfSkillType(skillType),
                                                    inventoryItem,
                                                    hero));
        }
        return createFilter(attributes);
    }

    public List<InventoryDescription> createItemDescriptionComparingToItem(InventoryItem otherItem) {
        List<InventoryDescription> attributes = new ArrayList<>();
        attributes.add(new InventoryDescription(inventoryItem.group,
                                                inventoryItem.name,
                                                inventoryItem,
                                                otherItem));

        for (InventoryMinimal minimal : InventoryMinimal.values()) {
            attributes.add(new InventoryDescription(minimal,
                                                    inventoryItem.getAttributeOfMinimal(minimal),
                                                    inventoryItem,
                                                    otherItem));
        }
        for (CalcType calcType : CalcType.values()) {
            attributes.add(new InventoryDescription(calcType,
                                                    inventoryItem.getAttributeOfCalcType(calcType),
                                                    inventoryItem,
                                                    otherItem));
        }
        for (StatType statType : StatType.values()) {
            attributes.add(new InventoryDescription(statType,
                                                    inventoryItem.getAttributeOfStatType(statType),
                                                    inventoryItem,
                                                    otherItem));
        }
        for (SkillType skillType : SkillType.values()) {
            attributes.add(new InventoryDescription(skillType,
                                                    inventoryItem.getAttributeOfSkillType(skillType),
                                                    inventoryItem,
                                                    otherItem));
        }
        return createFilter(attributes);
    }

    private List<InventoryDescription> createFilter(List<InventoryDescription> attributes) {
        List<InventoryDescription> filtered = new ArrayList<>();
        attributes.forEach(attribute -> {
            if (attribute.key instanceof InventoryGroup) {
                filtered.add(attribute);
                return;
            }
            if (attribute.value instanceof SkillType) {
                filtered.add(attribute);
                return;
            }
            if (attribute.key.equals(StatType.DEXTERITY) && inventoryItem.group.equals(InventoryGroup.SHIELD)) {
                filtered.add(attribute);
                return;
            }
            if (attribute.key.equals(SkillType.STEALTH) && inventoryItem.group.equals(InventoryGroup.CHEST)) {
                filtered.add(attribute);
                return;
            }
            if (attribute.key.equals(CalcType.WEIGHT)
                    && (inventoryItem.group.equals(InventoryGroup.SHIELD)
                    || inventoryItem.group.equals(InventoryGroup.WEAPON)
                    || inventoryItem.group.equals(InventoryGroup.RESOURCE))) {
                return;
            }
            if (attribute.key.equals(CalcType.WEIGHT)) {
                filtered.add(attribute);
                return;
            }
            if (attribute.value.equals(0)) {
                return;
            }
            filtered.add(attribute);
        });
        return filtered;
    }


}
