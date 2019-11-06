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

    int getXpCostForNextLevelOf(StatItemId statItemId) {
        return stats.getXpCostForNextLevelOf(statItemId);
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

    int getXpCostForNextLevelOf(SkillItemId skillItemId) {
        return skills.getXpCostForNextLevelOf(skillItemId);
    }

    int getGoldCostForNextLevelOf(SkillItemId skillItemId) {
        return skills.getGoldCostForNextLevelOf(skillItemId);
    }

    public List<SkillItemId> getAllSkillsAboveZero() {
        return skills.getAllAboveZero();
    }

    public List<SpellType> getAllSpells() {
        return spells.getAll();
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public int getStatValueOf(InventoryGroup inventoryGroup, StatItemId statItemId) {
        return inventory.getStatValueOf(inventoryGroup, statItemId);
    }

    public int getSkillValueOf(InventoryGroup inventoryGroup, SkillItemId skillItemId) {
        return inventory.getSkillValueOf(inventoryGroup, skillItemId);
    }

    public int getCalcValueOf(InventoryGroup inventoryGroup, CalcAttributeId calcAttributeId) {
        return inventory.getCalcValueOf(inventoryGroup, calcAttributeId);
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

    public int getStatRankOf(StatItemId statItemId) {
        return stats.getRankOf(statItemId);
    }

    public int getSkillRankOf(SkillItemId skillItemId) {
        return skills.getRankOf(skillItemId);
    }

    public int getCalcRankOf(CalcAttributeId calcAttributeId) {
        return 0;
    }

    public int getSpellRankOf(SpellType spellType) {
        return spells.getRankOf(spellType);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public int getExtraStatForVisualOf(StatItemId statItemId) {
        int extra = inventory.getSumOfStat(statItemId) + stats.getBonusStatOf(statItemId);
        if (extra < 0 && extra < -getStatRankOf(statItemId)) {
            return -getStatRankOf(statItemId);
        }
        return extra;
    }

    public int getExtraSkillForVisualOf(SkillItemId skillItemId) {
        int extra = inventory.getSumOfSkill(skillItemId) + skills.getBonusSkillOf(skillItemId);
        if (extra < 0 && extra < -getSkillRankOf(skillItemId)) {
            return -getSkillRankOf(skillItemId);
        }
        return extra;
    }

    public int getExtraCalcForVisualOf(CalcAttributeId calcAttributeId) {
        return 0;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    int getCalculatedTotalStatOf(StatItemId statItemId) {
        int totalStat = getRealTotalStatOf(statItemId);
        if (totalStat <= 0) {
            return 1;
        }
        return totalStat;
    }

    int getCalculatedTotalSkillOf(SkillItemId skillItemId) {
        if (getSkillRankOf(skillItemId) <= 0) {
            return 0;
        }
        int totalSkill = getRealTotalSkillOf(skillItemId);
        if (totalSkill <= 0) {
            return 0;
        }
        return totalSkill;
    }

    private int getRealTotalStatOf(StatItemId statItemId) {
        return getStatRankOf(statItemId) + inventory.getSumOfStat(statItemId) + stats.getBonusStatOf(statItemId);
    }

    private int getRealTotalSkillOf(SkillItemId skillItemId) {
        return getSkillRankOf(skillItemId) + inventory.getSumOfSkill(skillItemId) + skills.getBonusSkillOf(skillItemId);
    }

    public int getTotalCalcOf(CalcAttributeId calcAttributeId) {
        // todo, er moet nog wel een bonus komen voor protection en etc. bijv met een protection spell.
        return inventory.getSumOfCalc(calcAttributeId);
    }

}
