package nl.t64.game.rpg.components.party;

import lombok.AllArgsConstructor;
import nl.t64.game.rpg.constants.Constant;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.stream.Collectors;
import java.util.stream.IntStream;


@AllArgsConstructor
public class DescriptionCreator {

    private final List<InventoryDescription> descriptionList = new ArrayList<>();
    private final InventoryItem inventoryItem;
    private final int partySumOfMerchantSkill;

    public List<InventoryDescription> createItemDescription() {
        return createDescriptionList(InventoryDescription::new, Set.of(), Set.of(), Set.of(), Set.of(), null);
    }

    public List<InventoryDescription> createItemDescriptionComparingToHero(HeroItem hero) {
        return createDescriptionList((key, value) -> new InventoryDescription(key, value, inventoryItem, hero),
                                     Set.of(), Set.of(), Set.of(), Set.of(), null);
    }

    public List<InventoryDescription> createItemDescriptionComparingToItem(InventoryItem otherItem) {
        return createDescriptionList((key, value) -> new InventoryDescription(key, value, inventoryItem, otherItem),
                                     inventoryItem.getMinimalsOtherItemHasAndYouDont(otherItem),
                                     inventoryItem.getCalcsOtherItemHasAndYouDont(otherItem),
                                     inventoryItem.getStatsOtherItemHasAndYouDont(otherItem),
                                     inventoryItem.getSkillsOtherItemHasAndYouDont(otherItem),
                                     otherItem);
    }

    private List<InventoryDescription> createDescriptionList(BiFunction<Object, Object, InventoryDescription> createDescription,
                                                             Set<InventoryMinimal> minimalsYouDontHave,
                                                             Set<CalcAttributeId> calcsYouDontHave,
                                                             Set<StatItemId> statsYouDontHave,
                                                             Set<SkillItemId> skillsYouDontHave,
                                                             InventoryItem otherItem) {
        descriptionList.add(createDescription.apply(inventoryItem.group, inventoryItem.name));
        addHandiness(createDescription);
        addPrices(createDescription);
        addMinimals(createDescription, minimalsYouDontHave);
        addCalcs(createDescription, calcsYouDontHave);
        addStats(createDescription, statsYouDontHave);
        addSkills(createDescription, skillsYouDontHave);
        addPossibleEmptyLines(createDescription, otherItem);
        return createFilter();
    }

    private void addHandiness(BiFunction<Object, Object, InventoryDescription> createDescription) {
        if (inventoryItem.group.equals(InventoryGroup.WEAPON)) {
            if (inventoryItem.isTwoHanded) {
                descriptionList.add(createDescription.apply("(Two-handed)", ""));
            } else {
                descriptionList.add(createDescription.apply("(One-handed)", ""));
            }
        }
    }

    private void addPrices(BiFunction<Object, Object, InventoryDescription> createDescription) {
        if (inventoryItem.amount == 1) {
            descriptionList.add(createDescription.apply(Constant.DESCRIPTION_KEY_BUY, inventoryItem.getBuyPriceTotal(partySumOfMerchantSkill)));
            descriptionList.add(createDescription.apply(Constant.DESCRIPTION_KEY_SELL, inventoryItem.getSellValueTotal(partySumOfMerchantSkill)));
        } else if (inventoryItem.amount > 1) {
            descriptionList.add(createDescription.apply(Constant.DESCRIPTION_KEY_BUY_PIECE, inventoryItem.getBuyPricePiece(partySumOfMerchantSkill)));
            descriptionList.add(createDescription.apply(Constant.DESCRIPTION_KEY_BUY_TOTAL, inventoryItem.getBuyPriceTotal(partySumOfMerchantSkill)));
            descriptionList.add(createDescription.apply(Constant.DESCRIPTION_KEY_SELL_PIECE, inventoryItem.getSellValuePiece(partySumOfMerchantSkill)));
            descriptionList.add(createDescription.apply(Constant.DESCRIPTION_KEY_SELL_TOTAL, inventoryItem.getSellValueTotal(partySumOfMerchantSkill)));
        } else {
            throw new IllegalStateException("Amount cannot be below 1.");
        }
    }

    private void addMinimals(BiFunction<Object, Object, InventoryDescription> createDescription,
                             Set<InventoryMinimal> minimalsYouDontHave) {
        for (InventoryMinimal minimal : InventoryMinimal.values()) {
            if (minimalsYouDontHave.contains(minimal)) {
                descriptionList.add(createDescription.apply("", ""));
            } else {
                descriptionList.add(createDescription.apply(minimal, inventoryItem.getAttributeOfMinimal(minimal)));
            }
        }
    }

    private void addCalcs(BiFunction<Object, Object, InventoryDescription> createDescription,
                          Set<CalcAttributeId> calcsYouDontHave) {
        for (CalcAttributeId calcId : CalcAttributeId.values()) {
            if (calcsYouDontHave.contains(calcId)) {
                descriptionList.add(createDescription.apply(calcId, "0"));
            } else {
                descriptionList.add(createDescription.apply(calcId, inventoryItem.getAttributeOfCalcAttributeId(calcId)));
            }
        }
    }

    private void addStats(BiFunction<Object, Object, InventoryDescription> createDescription,
                          Set<StatItemId> statsYouDontHave) {
        for (StatItemId statId : StatItemId.values()) {
            if (statsYouDontHave.contains(statId)) {
                descriptionList.add(createDescription.apply(statId, "0"));
            } else {
                descriptionList.add(createDescription.apply(statId, inventoryItem.getAttributeOfStatItemId(statId)));
            }
        }
    }

    private void addSkills(BiFunction<Object, Object, InventoryDescription> createDescription,
                           Set<SkillItemId> skillsYouDontHave) {
        for (SkillItemId skillId : SkillItemId.values()) {
            if (skillsYouDontHave.contains(skillId)) {
                descriptionList.add(createDescription.apply(skillId, "0"));
            } else {
                descriptionList.add(createDescription.apply(skillId, inventoryItem.getAttributeOfSkillItemId(skillId)));
            }
        }
    }

    private void addPossibleEmptyLines(BiFunction<Object, Object, InventoryDescription> createDescription,
                                       InventoryItem otherItem) {
        if (otherItem != null && otherItem.description != null) {
            IntStream.range(0, otherItem.description.size())
                     .mapToObj(i -> createDescription.apply("", ""))
                     .forEach(descriptionList::add);
        }
    }

    private List<InventoryDescription> createFilter() {
        return descriptionList.stream()
                              .filter(this::mustBeAdded)
                              .collect(Collectors.toList());
    }

    private boolean mustBeAdded(InventoryDescription description) {
        if (description.key.equals(Constant.DESCRIPTION_KEY_BUY_TOTAL)
            || description.key.equals(Constant.DESCRIPTION_KEY_SELL_TOTAL)
            || description.key.equals(Constant.DESCRIPTION_KEY_BUY_PIECE)
            || description.key.equals(Constant.DESCRIPTION_KEY_SELL_PIECE)
            || description.key.equals(Constant.DESCRIPTION_KEY_BUY)
            || description.key.equals(Constant.DESCRIPTION_KEY_SELL)) {
            return true;
        }
        if (description.key instanceof InventoryGroup) {
            return true;
        }
        if (description.value instanceof SkillItemId) {
            return true;
        }
        if ((description.key.equals(CalcAttributeId.PROTECTION)
             || description.key.equals(StatItemId.AGILITY)
             || description.key.equals(SkillItemId.STEALTH))
            && inventoryItem.group.hasImpactOnPrtAgiStl()) {
            return true;
        }
        if (description.key.equals(CalcAttributeId.WEIGHT)
            && (inventoryItem.group.equals(InventoryGroup.SHIELD)
                || inventoryItem.group.equals(InventoryGroup.WEAPON)
                || inventoryItem.group.equals(InventoryGroup.RESOURCE)
                || inventoryItem.group.equals(InventoryGroup.POTION)
                || inventoryItem.group.equals(InventoryGroup.ITEM))) {
            return false;
        }
        if (description.key.equals(CalcAttributeId.WEIGHT)) {
            return true;
        }
        if (description.value.equals(0)) {
            return false;
        }
        return true;
    }

}
