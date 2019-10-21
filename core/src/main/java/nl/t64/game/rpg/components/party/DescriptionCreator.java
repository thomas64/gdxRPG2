package nl.t64.game.rpg.components.party;

import lombok.AllArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;


@AllArgsConstructor
public class DescriptionCreator {

    private final InventoryItem inventoryItem;

    public List<InventoryDescription> createItemDescriptionComparingToHero(HeroItem hero) {
        return createDescriptionList((key, value) -> new InventoryDescription(key, value, inventoryItem, hero));
    }

    public List<InventoryDescription> createItemDescriptionComparingToItem(InventoryItem otherItem) {
        return createDescriptionList((key, value) -> new InventoryDescription(key, value, inventoryItem, otherItem));
    }

    private List<InventoryDescription> createDescriptionList(
            BiFunction<SuperEnum, Object, InventoryDescription> functionToExecute) {

        final List<InventoryDescription> attributes = new ArrayList<>();

        attributes.add(functionToExecute.apply(inventoryItem.group, inventoryItem.name));

        for (InventoryMinimal minimal : InventoryMinimal.values()) {
            attributes.add(functionToExecute.apply(minimal, inventoryItem.getAttributeOfMinimal(minimal)));
        }
        for (CalcType calcType : CalcType.values()) {
            attributes.add(functionToExecute.apply(calcType, inventoryItem.getAttributeOfCalcType(calcType)));
        }
        for (StatType statType : StatType.values()) {
            attributes.add(functionToExecute.apply(statType, inventoryItem.getAttributeOfStatType(statType)));
        }
        for (SkillType skillType : SkillType.values()) {
            attributes.add(functionToExecute.apply(skillType, inventoryItem.getAttributeOfSkillType(skillType)));
        }
        return createFilter(attributes);
    }

    private List<InventoryDescription> createFilter(List<InventoryDescription> attributes) {
        final List<InventoryDescription> filtered = new ArrayList<>();
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
