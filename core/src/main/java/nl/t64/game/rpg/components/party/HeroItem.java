package nl.t64.game.rpg.components.party;

import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Map;
import java.util.Optional;


@Setter
public class HeroItem {

    @Getter
    String id;
    @Getter
    String name;
    private StatContainer stats;
    private SkillContainer skills;
    private EquipContainer inventory;

    public boolean equalsHero(HeroItem otherHero) {
        return id.equals(otherHero.id);
    }

    public int getNeededXpForNextLevel() {
        return stats.getNeededXpForNextLevel();
    }

    public int getXpDeltaBetweenLevels() {
        return stats.getXpDeltaBetweenLevels();
    }

    public int getTotalXp() {
        return stats.getTotalXp();
    }

    public int getXpRemaining() {
        return stats.getXpRemaining();
    }

    public int getLevel() {
        return stats.getLevel();
    }

    public Map<String, Integer> getAllHpStats() {
        return stats.getAllHpStats();
    }

    public int getMaximumHp() {
        return stats.getMaximumHp();
    }

    public int getCurrentHp() {
        return stats.getCurrentHp();
    }

    public List<SkillType> getAllSkillsAboveZero() {
        return skills.getAllAboveZero();
    }

    public int getStatValueOf(InventoryGroup inventoryGroup, StatType statType) {
        return inventory.getStatValueOf(inventoryGroup, statType);
    }

    public int getSkillValueOf(InventoryGroup inventoryGroup, SkillType skillType) {
        return inventory.getSkillValueOf(inventoryGroup, skillType);
    }

    public int getCalcValueOf(InventoryGroup inventoryGroup, CalcType calcType) {
        return inventory.getCalcValueOf(inventoryGroup, calcType);
    }

    public Optional<InventoryItem> getInventoryItem(InventoryGroup inventoryGroup) {
        return inventory.getInventoryItem(inventoryGroup);
    }

    public void forceSetInventoryItem(InventoryGroup inventoryGroup, InventoryItem inventoryItem) {
        inventory.forceSetInventoryItem(inventoryGroup, inventoryItem);
    }

    public Optional<String> isAbleToEquip(InventoryItem inventoryItem) {
        for (InventoryMinimal minimal : InventoryMinimal.values()) {
            Optional<String> message = minimal.createMessageIfHeroHasNotEnoughFor(inventoryItem, this);
            if (message.isPresent()) {
                return message;
            }
        }
        return Optional.empty();
    }

    int getTotalStatOf(StatType statType) {
        int totalStat = getOwnStatOf(statType) + getSumInventoryStatOf(statType) + stats.getBonusStatOf(statType);
        if (totalStat <= 0) {
            return 1;
        }
        return totalStat;
    }

    int getTotalSkillOf(SkillType skillType) {
        int ownSkill = getOwnSkillOf(skillType);
        if (ownSkill <= 0) {
            return 0;
        }
        int totalSkill = ownSkill + getSumInventorySkillOf(skillType) + skills.getBonusSkillOf(skillType);
        if (totalSkill <= 0) {
            return 0;
        }
        return totalSkill;
    }

    public int getTotalCalcOf(CalcType calcType) {
        // todo, er moet nog wel een bonus komen voor protection en etc. bijv met een protection spell.
        return getSumInventoryCalcOf(calcType);
    }

    public int getOwnStatOf(StatType statType) {
        return stats.getOwnStatOf(statType);
    }

    public int getOwnSkillOf(SkillType skillType) {
        return skills.getOwnSkillOf(skillType);
    }

    public int getOwnCalcOf(CalcType calcType) {
        return 0;
    }

    public int getExtraStatForVisualOf(StatType statType) {
        int extra = getSumInventoryStatOf(statType) + stats.getBonusStatOf(statType);
        if (extra < 0 && extra < -getOwnStatOf(statType)) {
            return -getOwnStatOf(statType);
        }
        return extra;
    }

    public int getExtraSkillForVisualOf(SkillType skillType) {
        int extra = getSumInventorySkillOf(skillType) + skills.getBonusSkillOf(skillType);
        if (extra < 0 && extra < -getOwnSkillOf(skillType)) {
            return -getOwnSkillOf(skillType);
        }
        return extra;
    }

    public int getExtraCalcForVisualOf(CalcType calcType) {
        return 0;
    }

    public int getPreviewStatForVisualOf(StatType statType, InventoryItem hoveredItem) {
        if (hoveredItem == null) {
            return 0;
        } else {
            int equippedValue = getStatValueOf(hoveredItem.getGroup(), statType);
            int hoveredValue = hoveredItem.getAttributeOfStatType(statType);
            int difference = hoveredValue - equippedValue;
            int extra = getOwnStatOf(statType) + getExtraStatForVisualOf(statType);
            return getPreviewForVisual(difference, extra);
        }
    }

    public int getPreviewSkillForVisualOf(SkillType skillType, InventoryItem hoveredItem) {
        if (hoveredItem == null) {
            return 0;
        } else {
            int equippedValue = getSkillValueOf(hoveredItem.getGroup(), skillType);
            int hoveredValue = hoveredItem.getAttributeOfSkillType(skillType);
            int difference = hoveredValue - equippedValue;
            int extra = getOwnSkillOf(skillType) + getExtraSkillForVisualOf(skillType);
            return getPreviewForVisual(difference, extra);
        }
    }

    public int getPreviewCalcForVisualOf(CalcType calcType, InventoryItem hoveredItem) {
        if (hoveredItem == null) {
            return 0;
        } else {
            int equippedValue = getCalcValueOf(hoveredItem.getGroup(), calcType);
            int hoveredValue = hoveredItem.getAttributeOfCalcType(calcType);
            int difference = hoveredValue - equippedValue;
            int extra = getOwnCalcOf(calcType) + getExtraCalcForVisualOf(calcType);
            return getPreviewForVisual(difference, extra);
        }
    }

    private int getPreviewForVisual(int difference, int extra) {
        if (difference < 0 && difference < -extra) {
            return -extra;
        } else if (difference > 0 && extra == 0) {
            return 0;
        }
        return difference;
    }

    private int getSumInventoryStatOf(StatType statType) {
        return inventory.getSumOfStat(statType);
    }

    private int getSumInventorySkillOf(SkillType skillType) {
        return inventory.getSumOfSkill(skillType);
    }

    private int getSumInventoryCalcOf(CalcType calcType) {
        return inventory.getSumOfCalc(calcType);
    }

}
