package nl.t64.game.rpg.components.party;

import lombok.Setter;

import java.util.Map;
import java.util.Optional;


@Setter
public class HeroItem {

    String name;
    private Level level;
    private PersonalContainer inventory;
    private Strength strength;
    private Endurance endurance;
    private Stamina stamina;

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

    int getLevel() {
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

    public Optional<InventoryItem> getInventoryItem(InventoryGroup inventoryGroup) {
        return inventory.getInventoryItem(inventoryGroup);
    }

    public void forceSetInventoryItem(InventoryGroup inventoryGroup, InventoryItem inventoryItem) {
        inventory.forceSetInventoryItem(inventoryGroup, inventoryItem);
    }

    int getProtectionWithShield() {
        return inventory.getSumOfProtectionWithShield();
    }

    int getStrength() {
        return strength.current + strength.bonus;
    }

    public int getWeaponBaseHit() {
        return inventory.getWeaponBaseHit();
    }

    public int getWeaponDamage() {
        return inventory.getWeaponDamage();
    }

    public int getShieldDefense() {
        return inventory.getShieldDefense();
    }

    public int getOwnEndurance() {
        return endurance.current;
    }

    public int getOwnStrength() {
        return strength.current;
    }

    public int getOwnStamina() {
        return stamina.current;
    }

}
