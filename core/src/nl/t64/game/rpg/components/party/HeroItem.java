package nl.t64.game.rpg.components.party;

import lombok.Getter;
import lombok.Setter;
import nl.t64.game.rpg.constants.Constant;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;


@Setter
public class HeroItem {

    @Getter
    String id;
    @Getter
    String name;
    @Getter
    SchoolType school;
    @Getter
    boolean hasBeenRecruited = false;
    private StatContainer stats;
    private SkillContainer skills;
    private SpellContainer spells;
    private EquipContainer inventory;

    public boolean isPlayer() {
        return id.equals(Constant.PLAYER_ID);
    }

    public boolean hasSameIdAs(HeroItem candidateHero) {
        return id.equals(candidateHero.id);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public int getXpNeededForNextLevel() {
        return stats.getXpNeededForNextLevel();
    }

    public int getXpDeltaBetweenLevels() {
        return stats.getXpDeltaBetweenLevels();
    }

    public void gainXp(int amount, StringBuilder levelUpMessage) {
        stats.gainXp(amount, hasGainedLevel -> append(levelUpMessage));
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

    StatItem getStatById(StatItemId statItemId) {
        return stats.getById(statItemId);
    }

    SkillItem getSkillById(SkillItemId skillItemId) {
        return skills.getById(skillItemId);
    }

    public List<StatItem> getAllStats() {
        return stats.getAll();
    }

    public List<SkillItem> getAllSkillsAboveZero() {
        return skills.getAllAboveZero();
    }

    public List<SpellItem> getAllSpells() {
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

    public void clearInventoryItemFor(InventoryGroup inventoryGroup) {
        forceSetInventoryItemFor(inventoryGroup, null);
    }

    public void forceSetInventoryItemFor(InventoryGroup inventoryGroup, InventoryItem inventoryItem) {
        inventory.forceSetInventoryItem(inventoryGroup, inventoryItem);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public Optional<String> createMessageIfNotAbleToEquip(InventoryItem inventoryItem) {
        return createMessageIfHeroHasNotEnoughFor(inventoryItem)
                .or(() -> createMessageIfWeaponAndShieldAreNotCompatible(inventoryItem))
                .or(() -> createMessageIfNotAbleToDequip(getInventoryItem(inventoryItem.group)
                                                                 .orElseGet(InventoryItem::new)));  // does nothing
    }

    public Optional<String> createMessageIfNotAbleToDequip(InventoryItem enhancerItem) {
        return Arrays.stream(StatItemId.values())
                     .filter(statItemId -> enhancerItem.getAttributeOfStatItemId(statItemId) > 0)
                     .flatMap(statItemId -> createMessageIfNotAbleToDequip(enhancerItem, statItemId))
                     .findAny();
    }

    private Stream<String> createMessageIfNotAbleToDequip(InventoryItem enhancerItem, StatItemId statItemId) {
        return inventory.getItemsWithMinimalOf(statItemId)
                        .filter(dependantItem -> isMinimalToHigh(statItemId, dependantItem, enhancerItem))
                        .map(enhancerItem::createMessageFailToDequip);
    }

    private boolean isMinimalToHigh(StatItemId statItemId, InventoryItem dependantItem, InventoryItem enhancerItem) {
        return dependantItem.getMinimalAttributeOfStatItemId(statItemId) >
               getCalculatedTotalStatOf(statItemId) - enhancerItem.getAttributeOfStatItemId(statItemId);
    }

    private Optional<String> createMessageIfWeaponAndShieldAreNotCompatible(InventoryItem inventoryItem) {
        if (inventoryItem.isTwoHanded) {
            return inventory.getInventoryItem(InventoryGroup.SHIELD)
                            .map(inventoryItem::createMessageFailToEquipTwoHanded);

        } else if (inventoryItem.group.equals(InventoryGroup.SHIELD)) {
            return inventory.getInventoryItem(InventoryGroup.WEAPON)
                            .filter(otherItem -> otherItem.isTwoHanded)
                            .map(inventoryItem::createMessageFailToEquipTwoHanded);

        } else {
            return Optional.empty();
        }
    }

    public Optional<String> createMessageIfHeroHasNotEnoughFor(InventoryItem inventoryItem) {
        return Arrays.stream(InventoryMinimal.values())
                     .flatMap(minimal -> minimal.createMessageIfHeroHasNotEnoughFor(inventoryItem, this).stream())
                     .findFirst();
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public int getExtraStatForVisualOf(StatItem statItem) {
        final int extra = inventory.getSumOfStat(statItem.getId()) + statItem.bonus;
        if (extra < 0 && extra < -statItem.rank) {
            return -statItem.rank;
        }
        return extra;
    }

    public int getExtraSkillForVisualOf(SkillItem skillItem) {
        final int extra = inventory.getSumOfSkill(skillItem.getId()) + skillItem.bonus;
        if (extra < 0 && extra < -skillItem.rank) {
            return -skillItem.rank;
        }
        return extra;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    int getCalculatedTotalStatOf(StatItemId statItemId) {
        final StatItem statItem = stats.getById(statItemId);
        final int totalStat = getRealTotalStatOf(statItem);
        if (totalStat <= 0) {
            return 1;
        }
        return totalStat;
    }

    public int getCalculatedTotalSkillOf(SkillItemId skillItemId) {
        final SkillItem skillItem = skills.getById(skillItemId);
        if (skillItem.rank <= 0) {
            return 0;
        }
        final int totalSkill = getRealTotalSkillOf(skillItem);
        if (totalSkill <= 0) {
            return 0;
        }
        return totalSkill;
    }

    private int getRealTotalStatOf(StatItem statItem) {
        return statItem.rank + inventory.getSumOfStat(statItem.getId()) + statItem.bonus;
    }

    private int getRealTotalSkillOf(SkillItem skillItem) {
        return skillItem.rank + inventory.getSumOfSkill(skillItem.getId()) + skillItem.bonus;
    }

    public int getTotalCalcOf(CalcAttributeId calcAttributeId) {
        // todo, er moet nog wel een bonus komen voor protection en etc. bijv met een protection spell.
        // of hieronder
        return inventory.getSumOfCalc(calcAttributeId);
    }

    public int getPossibleExtraProtection() {
        // todo, er moet nog wel een bonus komen voor protection en etc. bijv met een protection spell.
        // of hierboven
        return inventory.getBonusProtectionWhenArmorSetIsComplete();
    }

    public int getCalculatedMovepoints() {
        float movepoints = 10f
                           + (stats.getById(StatItemId.STAMINA).variable / 10f)
                           - (getTotalCalcOf(CalcAttributeId.WEIGHT) / 3f);
        // todo,           + bonus movepoints van equipment
        if (movepoints <= 0f) {
            return 1;
        }
        return Math.round(movepoints);
    }

    public int getCalculatedTotalDamage() {
        // todo, is nu alleen nog maar voor hand to hand wapens.
        return inventory.getWeaponSkill()
                        .filter(SkillItemId::isHandToHandWeaponSkill)
                        .map(this::getCalculatedTotalDamage)
                        .orElse(0);
    }

    private int getCalculatedTotalDamage(SkillItemId weaponSkill) {
        return getTotalCalcOf(CalcAttributeId.DAMAGE)
               // + getLevel() todo, this one shouldn't be shown in calculation but should be calculated in battle.
               + (getCalculatedTotalStatOf(StatItemId.STRENGTH) / stats.getInflictDamageStaminaPenalty())
               + getCalculatedTotalSkillOf(SkillItemId.WARRIOR)
               + (getCalculatedTotalSkillOf(weaponSkill) * 2);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    private void append(StringBuilder levelUpMessage) {
        levelUpMessage.append(name).append(" gained a level!").append(System.lineSeparator());
    }

}
