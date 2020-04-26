package nl.t64.game.rpg.components.party;

import lombok.AllArgsConstructor;
import nl.t64.game.rpg.constants.Constant;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;
import java.util.stream.Collectors;


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
            BiFunction<Object, Object, InventoryDescription> functionToExecute) {

        final List<InventoryDescription> attributes = new ArrayList<>();

        attributes.add(functionToExecute.apply(inventoryItem.group, inventoryItem.name));
        attributes.add(functionToExecute.apply(Constant.DESCRIPTION_KEY_BUY, inventoryItem.getBuyPrice()));
        attributes.add(functionToExecute.apply(Constant.DESCRIPTION_KEY_SELL, inventoryItem.getSellValue()));

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
        return attributes.stream()
                         .filter(this::mustBeAdded)
                         .collect(Collectors.toList());
    }

    private boolean mustBeAdded(InventoryDescription attribute) {
        if (attribute.key.equals(Constant.DESCRIPTION_KEY_BUY)
            || attribute.key.equals(Constant.DESCRIPTION_KEY_SELL)) {
            return true;
        }
        if (attribute.key instanceof InventoryGroup) {
            return true;
        }
        if (attribute.value instanceof SkillItemId) {
            return true;
        }
        if (attribute.key.equals(StatItemId.DEXTERITY) && inventoryItem.group.equals(InventoryGroup.SHIELD)) {
            return true;
        }
        if (attribute.key.equals(SkillItemId.STEALTH) && inventoryItem.group.equals(InventoryGroup.CHEST)) {
            return true;
        }
        if (attribute.key.equals(CalcAttributeId.WEIGHT)
            && (inventoryItem.group.equals(InventoryGroup.SHIELD)
                || inventoryItem.group.equals(InventoryGroup.WEAPON)
                || inventoryItem.group.equals(InventoryGroup.RESOURCE)
                || inventoryItem.group.equals(InventoryGroup.POTION))) {
            return false;
        }
        if (attribute.key.equals(CalcAttributeId.WEIGHT)) {
            return true;
        }
        if (attribute.value.equals(0)) {
            return false;
        }
        return true;
    }

}
