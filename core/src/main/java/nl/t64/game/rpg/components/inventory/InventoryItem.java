package nl.t64.game.rpg.components.inventory;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;


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
    @JsonProperty("base_hit")
    int baseHit;
    int protection;
    int defense;
    int damage;
    int dexterity;
    int stealth;

    @Getter
    private List<Map.Entry<String, String>> description;

    public InventoryItem(InventoryItem item) {
        this.id = item.id;
        this.name = item.name;
        this.amount = 1;
        this.group = item.group;
        this.skill = item.skill;
        this.weight = item.weight;
        this.minStrength = item.minStrength;
        this.baseHit = item.baseHit;
        this.protection = item.protection;
        this.defense = item.defense;
        this.damage = item.damage;
        this.dexterity = item.dexterity;
        this.stealth = item.stealth;

        this.description = createDescription();
    }

    public boolean hasSameIdAs(String candidateId) {
        return id.equalsIgnoreCase(candidateId);
    }

    public boolean isStackable() {
        return group.equals(InventoryGroup.RESOURCE);
    }

    void increaseAmountWith(InventoryItem sourceItem) {
        amount += sourceItem.amount;
    }

    private List<Map.Entry<String, String>> createDescription() {
        List<Map.Entry<String, String>> attributes = createListOfPairsWithTitlesAndValues();
        return createFilter(attributes);
    }

    private List<Map.Entry<String, String>> createListOfPairsWithTitlesAndValues() {
        return Arrays.asList(
                Map.entry(group.title, name),
                Map.entry("Skill", skill == null ? "0" : skill.title),
                Map.entry("Weight", String.valueOf(weight)),
                Map.entry("Min. Strength", String.valueOf(minStrength)),
                Map.entry("Base Hit", String.valueOf(baseHit) + "%"),
                Map.entry("Protection", String.valueOf(protection)),
                Map.entry("Defense", String.valueOf(defense)),
                Map.entry("Damage", String.valueOf(damage)),
                Map.entry("Dexterity", String.valueOf(dexterity)),
                Map.entry("Stealth", String.valueOf(stealth))
        );
    }

    private List<Map.Entry<String, String>> createFilter(List<Map.Entry<String, String>> attributes) {
        List<Map.Entry<String, String>> filtered = new ArrayList<>();
        attributes.forEach(attribute -> {
            if (attribute.getKey().equals("Dexterity") &&
                    group.equals(InventoryGroup.SHIELD)) {
                filtered.add(attribute);
                return;
            }
            if (attribute.getKey().equals("Stealth") &&
                    group.equals(InventoryGroup.CHEST)) {
                filtered.add(attribute);
                return;
            }
            if (attribute.getKey().equals("Weight") &&
                    (group.equals(InventoryGroup.SHIELD) ||
                            group.equals(InventoryGroup.WEAPON) ||
                            group.equals(InventoryGroup.RESOURCE))) {
                return;
            }
            if (attribute.getKey().equals("Weight")) {
                filtered.add(attribute);
                return;
            }
            if (attribute.getValue().startsWith("0")) {
                return;
            }
            filtered.add(attribute);
        });
        return filtered;
    }

}

