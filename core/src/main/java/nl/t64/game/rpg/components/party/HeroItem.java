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
    @Getter
    SchoolType school;
    private StatContainer stats;
    private SkillContainer skills;
    private SpellContainer spells;
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

    public List<SpellType> getAllSpells() {
        return spells.getAll();
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

    int getCalculatedTotalStatOf(StatType statType) {
        int totalStat = getRealTotalStatOf(statType);
        if (totalStat <= 0) {
            return 1;
        }
        return totalStat;
    }

    int getCalculatedTotalSkillOf(SkillType skillType) {
        if (getOwnSkillOf(skillType) <= 0) {
            return 0;
        }
        int totalSkill = getRealTotalSkillOf(skillType);
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

    public int getSpellRankOf(SpellType spellType) {
        return spells.getRankOf(spellType);
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
            int visualTotal = getOwnStatOf(statType) + getExtraStatForVisualOf(statType);
            int realTotal = getRealTotalStatOf(statType);
            return getPreviewForVisual(difference, visualTotal, realTotal);
        }
    }

    public int getPreviewSkillForVisualOf(SkillType skillType, InventoryItem hoveredItem) {
        if (hoveredItem == null) {
            return 0;
        } else {
            int equippedValue = getSkillValueOf(hoveredItem.getGroup(), skillType);
            int hoveredValue = hoveredItem.getAttributeOfSkillType(skillType);
            int difference = hoveredValue - equippedValue;
            int visualTotal = getOwnSkillOf(skillType) + getExtraSkillForVisualOf(skillType);
            int realTotal = getRealTotalSkillOf(skillType);
            return getPreviewForVisual(difference, visualTotal, realTotal);
        }
    }

    public int getPreviewCalcForVisualOf(CalcType calcType, InventoryItem hoveredItem) {
        if (hoveredItem == null) {
            return 0;
        } else {
            int equippedValue = getCalcValueOf(hoveredItem.getGroup(), calcType);
            int hoveredValue = hoveredItem.getAttributeOfCalcType(calcType);
            return hoveredValue - equippedValue;
        }
    }

    private int getPreviewForVisual(int difference, int visualTotal, int realTotal) {
        if (difference < 0 && difference < -visualTotal) {
            return -visualTotal;
        } else if (difference > 0 && difference + realTotal < 0) {
            return 0;
        } else if (difference > 0 && difference + realTotal == 0) {
            return 0;
        } else if (difference > 0 && difference + realTotal > 0 && realTotal < 0) {
            return realTotal + difference;
        }
        return difference;
    }

    private int getRealTotalStatOf(StatType statType) {
        return getOwnStatOf(statType) + getSumInventoryStatOf(statType) + stats.getBonusStatOf(statType);
    }

    private int getRealTotalSkillOf(SkillType skillType) {
        return getOwnSkillOf(skillType) + getSumInventorySkillOf(skillType) + skills.getBonusSkillOf(skillType);
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
