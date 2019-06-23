package nl.t64.game.rpg.components.party;

import lombok.Getter;
import lombok.Setter;
import nl.t64.game.rpg.constants.InventoryAttribute;

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
    private Strength strength;
    private Endurance endurance;
    private Stamina stamina;

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

    public int getAttributeValueOf(InventoryGroup inventoryGroup, InventoryAttribute inventoryAttribute) {
        return getInventoryItem(inventoryGroup).map(inventoryItem -> inventoryItem.getAttribute(inventoryAttribute))
                                               .orElse(0);
    }

    public Optional<InventoryItem> getInventoryItem(InventoryGroup inventoryGroup) {
        return inventory.getInventoryItem(inventoryGroup);
    }

    public void forceSetInventoryItem(InventoryGroup inventoryGroup, InventoryItem inventoryItem) {
        inventory.forceSetInventoryItem(inventoryGroup, inventoryItem);
    }

    public InventoryMessage isAbleToEquip(InventoryItem inventoryItem) {
        String message = String.format("%s needs %s %s%nto equip that %s.", name, "%s", "%s", inventoryItem.name);
        if (inventoryItem.minIntelligence > getOwnIntelligence()) {
            String intelligenceMessage = String.format(message, inventoryItem.minIntelligence, "Intelligence");
            return new InventoryMessage(false, intelligenceMessage);
        }
        if (inventoryItem.minStrength > getOwnStrength()) {
            String strengthMessage = String.format(message, inventoryItem.minStrength, "Strength");
            return new InventoryMessage(false, strengthMessage);
        }
        return new InventoryMessage(true, null);
    }

    int getProtectionWithShield() {
        return inventory.getSumOfProtectionWithShield();
    }

    int getIntelligence() {
        return intelligence.current + intelligence.bonus;
    }

    int getStrength() {
        return strength.current + strength.bonus;
    }

    public int getOwnIntelligence() {
        return intelligence.current;
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
