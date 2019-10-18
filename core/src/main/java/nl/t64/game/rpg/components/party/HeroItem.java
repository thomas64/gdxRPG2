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

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    int getXpCostForNextLevelOf(StatType statType) {
        return stats.getXpCostForNextLevelOf(statType);
    }

    public int getXpNeededForNextLevel() {
        return stats.getXpNeededForNextLevel();
    }

    public int getXpDeltaBetweenLevels() {
        return stats.getXpDeltaBetweenLevels();
    }

    public int getTotalXp() {
        return stats.getTotalXp();
    }

    public int getXpToInvest() {
        return stats.getXpToInvest();
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

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public List<SkillType> getAllSkillsAboveZero() {
        return skills.getAllAboveZero();
    }

    public List<SpellType> getAllSpells() {
        return spells.getAll();
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public int getStatValueOf(InventoryGroup inventoryGroup, StatType statType) {
        return inventory.getStatValueOf(inventoryGroup, statType);
    }

    public int getSkillValueOf(InventoryGroup inventoryGroup, SkillType skillType) {
        return inventory.getSkillValueOf(inventoryGroup, skillType);
    }

    public int getCalcValueOf(InventoryGroup inventoryGroup, CalcType calcType) {
        return inventory.getCalcValueOf(inventoryGroup, calcType);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

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
        return Optional.empty();    // Yes, is able to equip.
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public int getStatRankOf(StatType statType) {
        return stats.getRankOf(statType);
    }

    public int getSkillRankOf(SkillType skillType) {
        return skills.getRankOf(skillType);
    }

    public int getCalcRankOf(CalcType calcType) {
        return 0;
    }

    public int getSpellRankOf(SpellType spellType) {
        return spells.getRankOf(spellType);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public int getExtraStatForVisualOf(StatType statType) {
        int extra = inventory.getSumOfStat(statType) + stats.getBonusStatOf(statType);
        if (extra < 0 && extra < -getStatRankOf(statType)) {
            return -getStatRankOf(statType);
        }
        return extra;
    }

    public int getExtraSkillForVisualOf(SkillType skillType) {
        int extra = inventory.getSumOfSkill(skillType) + skills.getBonusSkillOf(skillType);
        if (extra < 0 && extra < -getSkillRankOf(skillType)) {
            return -getSkillRankOf(skillType);
        }
        return extra;
    }

    public int getExtraCalcForVisualOf(CalcType calcType) {
        return 0;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    int getCalculatedTotalStatOf(StatType statType) {
        int totalStat = getRealTotalStatOf(statType);
        if (totalStat <= 0) {
            return 1;
        }
        return totalStat;
    }

    int getCalculatedTotalSkillOf(SkillType skillType) {
        if (getSkillRankOf(skillType) <= 0) {
            return 0;
        }
        int totalSkill = getRealTotalSkillOf(skillType);
        if (totalSkill <= 0) {
            return 0;
        }
        return totalSkill;
    }

    private int getRealTotalStatOf(StatType statType) {
        return getStatRankOf(statType) + inventory.getSumOfStat(statType) + stats.getBonusStatOf(statType);
    }

    private int getRealTotalSkillOf(SkillType skillType) {
        return getSkillRankOf(skillType) + inventory.getSumOfSkill(skillType) + skills.getBonusSkillOf(skillType);
    }

    public int getTotalCalcOf(CalcType calcType) {
        // todo, er moet nog wel een bonus komen voor protection en etc. bijv met een protection spell.
        return inventory.getSumOfCalc(calcType);
    }

}
