package nl.t64.game.rpg.components.party;

import lombok.Getter;
import lombok.Setter;

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

    public int getStatValueOf(InventoryGroup inventoryGroup, StatType statType) {
        return inventory.getStatValueOf(inventoryGroup, statType);
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

    public int getTotalStatOf(StatType statType) {
        return getOwnStatOf(statType) + getTotalInventoryStatOf(statType) + stats.getBonusStatOf(statType);
    }

    int getTotalSkillOf(SkillType skillType) {
        int ownskill = getOwnSkillOf(skillType);
        if (ownskill <= 0) {
            return 0;
        }
        return ownskill + getTotalInventorySkillOf(skillType) + skills.getBonusSkillOf(skillType);
    }

    public int getOwnStatOf(StatType statType) {
        return stats.getOwnStatOf(statType);
    }

    public int getTotalInventoryStatOf(StatType statType) {
        return inventory.getSumOfStat(statType);
    }

    public int getOwnSkillOf(SkillType skillType) {
        return skills.getOwnSkillOf(skillType);
    }

    public int getTotalInventorySkillOf(SkillType skillType) {
        return inventory.getSumOfSkill(skillType);
    }

}
