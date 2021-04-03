package nl.t64.game.rpg.components.party;

import lombok.Getter;
import lombok.Setter;
import nl.t64.game.rpg.constants.Constant;

import java.util.Arrays;
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

    public Optional<String> createMessageIfNotAbleToEquip(InventoryItem inventoryItem) {
        return createMessageIfHeroHasNotEnoughFor(inventoryItem)
                .or(() -> createMessageIfWeaponAndShieldAreNotCompatible(inventoryItem));
    }

    private Optional<String> createMessageIfWeaponAndShieldAreNotCompatible(InventoryItem inventoryItem) {
        if (inventoryItem.isTwoHanded
            && inventory.getInventoryItem(InventoryGroup.SHIELD).isPresent()) {
            return createMessage(inventory.getInventoryItem(InventoryGroup.SHIELD).get().name, inventoryItem.name);
        } else if (inventoryItem.group.equals(InventoryGroup.SHIELD)
                   && inventory.getInventoryItem(InventoryGroup.WEAPON).isPresent()
                   && inventory.getInventoryItem(InventoryGroup.WEAPON).get().isTwoHanded) {
            return createMessage(inventory.getInventoryItem(InventoryGroup.WEAPON).get().name, inventoryItem.name);
        } else {
            return Optional.empty();
        }
    }

    private Optional<String> createMessage(String itemName1, String itemName2) {
        return Optional.of(String.format("""
                                                 %s needs to unequip the %s
                                                 to equip that %s.""", name, itemName1, itemName2));
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
        return inventory.getSumOfCalc(calcAttributeId);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    private void append(StringBuilder levelUpMessage) {
        levelUpMessage.append(name).append(" gained a level!").append(System.lineSeparator());
    }

}
