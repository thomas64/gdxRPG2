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
    private Level level;
    private PersonalContainer inventory;

    private Intelligence intelligence;
    private Dexterity dexterity;
    private Strength strength;
    private Endurance endurance;
    private Stamina stamina;

    private Stealth stealth;
    private Hafted hafted;
    private Pole pole;
    private Shield shield;
    private Sword sword;

    public boolean equalsHero(HeroItem otherHero) {
        return id.equals(otherHero.id);
    }

    public int getNeededXpForNextLevel() {
        return level.getNeededXpForNextLevel();
    }

    public int getXpDeltaBetweenLevels() {
        return level.getXpDeltaBetweenLevels();
    }

    public int getTotalXp() {
        return level.totalXp;
    }

    public int getXpRemaining() {
        return level.xpForUpgrades;
    }

    public int getLevel() {
        return level.current;
    }

    public Map<String, Integer> getAllHpStats() {
        return Map.of("lvlCur", level.current,
                      "lvlHp", level.hitpoints,
                      "staCur", stamina.current,
                      "staHp", stamina.hitpoints,
                      "eduCur", endurance.current,
                      "eduHp", endurance.hitpoints,
                      "eduBon", endurance.bonus);
    }

    public int getMaximumHp() {
        return level.current + stamina.current + endurance.current + endurance.bonus;
    }

    public int getCurrentHp() {
        return level.hitpoints + stamina.hitpoints + endurance.hitpoints + endurance.bonus;
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

//    int getProtectionWithShield() { // todo, protection without shield.
//        return inventory.getSumOfProtectionWithShield();
//    }

    int getTotalStatOf(StatType statType) {
        return getOwnStatOf(statType) + getTotalInventoryStatOf(statType) + getBonusStatOf(statType);
    }

    int getTotalSkillOf(SkillType skillType) {
        int ownskill = getOwnSkillOf(skillType);
        if (ownskill <= 0) {
            return 0;
        }
        return ownskill + getTotalInventorySkillOf(skillType) + getBonusSkillOf(skillType);
    }

    public int getOwnStatOf(StatType statType) {
        switch (statType) {
            case PROTECTION:
                return 0;
            case INTELLIGENCE:
                return intelligence.current;
            case DEXTERITY:
                return dexterity.current;
            case ENDURANCE:
                return endurance.current;
            case STRENGTH:
                return strength.current;
            case STAMINA:
                return stamina.current;
            default:
                throw new IllegalArgumentException(String.format("StatType '%s' not usable.", statType));
        }
    }

    public int getTotalInventoryStatOf(StatType statType) {
        return inventory.getSumOfStat(statType);
    }

    private int getBonusStatOf(StatType statType) {
        switch (statType) {
            case PROTECTION:
                return 0; // todo, er moet nog wel een bonus komen voor protection. bijv met een protection spell.
            case INTELLIGENCE:
                return intelligence.bonus;
            case DEXTERITY:
                return dexterity.bonus;
            case ENDURANCE:
                return endurance.bonus;
            case STRENGTH:
                return strength.bonus;
            case STAMINA:
                return 0;
            default:
                throw new IllegalArgumentException(String.format("StatType '%s' not usable.", statType));
        }
    }

    public int getOwnSkillOf(SkillType skillType) {
        switch (skillType) {
            case STEALTH:
                return stealth.current;
            case HAFTED:
                return hafted.current;
            case POLE:
                return pole.current;
            case SHIELD:
                return shield.current;
            case SWORD:
                return sword.current;
            default:
                throw new IllegalArgumentException(String.format("SkillType '%s' not usable.", skillType));
        }
    }

    public int getTotalInventorySkillOf(SkillType skillType) {
        return inventory.getSumOfSkill(skillType);
    }

    public int getBonusSkillOf(SkillType skillType) {
        switch (skillType) {
            case STEALTH:
                return stealth.bonus;
            default:
                return 0;
        }
    }

}
