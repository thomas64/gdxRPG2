package nl.t64.game.rpg.components.inventory;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@NoArgsConstructor
@Setter
public class InventoryItem {

    @Getter
    String id;
    String name;
    int amount;
    @Getter
    InventoryGroup group;
    WeaponType skill;
    int weight;
    @JsonProperty("min_strength")
    int minStrength;
    @JsonProperty("hit_chance")
    int hitChance;
    int protection;
    int defense;
    int damage;
    int dexterity;
    int stealth;

    public InventoryItem(InventoryItem item) {
        this.id = item.id;
        this.name = item.name;
        this.amount = 1;
        this.group = item.group;
        this.skill = item.skill;
        this.weight = item.weight;
        this.minStrength = item.minStrength;
        this.hitChance = item.hitChance;
        this.protection = item.protection;
        this.defense = item.defense;
        this.damage = item.damage;
        this.dexterity = item.dexterity;
        this.stealth = item.stealth;
    }

    public boolean hasSameIdAs(String candidateId) {
        return id.equalsIgnoreCase(candidateId);
    }

    public boolean isStackable() {
        return group.equals(InventoryGroup.RESOURCE);
    }

    public String getDescription() {
        return name;
    }

    void increaseAmountWith(InventoryItem sourceItem) {
        amount += sourceItem.amount;
    }

}

