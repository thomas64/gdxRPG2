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
        for (CalcAttributeId calcAttributeId : CalcAttributeId.values()) {
            attributes.add(functionToExecute.apply(calcAttributeId, inventoryItem.getAttributeOfCalcAttributeId(calcAttributeId)));
        }
        for (StatItemId statItemId : StatItemId.values()) {
            attributes.add(functionToExecute.apply(statItemId, inventoryItem.getAttributeOfStatItemId(statItemId)));
        }
        for (SkillItemId skillItemId : SkillItemId.values()) {
            attributes.add(functionToExecute.apply(skillItemId, inventoryItem.getAttributeOfSkillItemId(skillItemId)));
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
            if (attribute.value instanceof SkillItemId) {
                filtered.add(attribute);
                return;
            }
            if (attribute.key.equals(StatItemId.DEXTERITY) && inventoryItem.group.equals(InventoryGroup.SHIELD)) {
                filtered.add(attribute);
                return;
            }
            if (attribute.key.equals(SkillItemId.STEALTH) && inventoryItem.group.equals(InventoryGroup.CHEST)) {
                filtered.add(attribute);
                return;
            }
            if (attribute.key.equals(CalcAttributeId.WEIGHT)
                    && (inventoryItem.group.equals(InventoryGroup.SHIELD)
                    || inventoryItem.group.equals(InventoryGroup.WEAPON)
                    || inventoryItem.group.equals(InventoryGroup.RESOURCE))) {
                return;
            }
            if (attribute.key.equals(CalcAttributeId.WEIGHT)) {
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
