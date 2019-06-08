package nl.t64.game.rpg.components.party;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static nl.t64.game.rpg.components.party.InventoryAttribute.*;
import static nl.t64.game.rpg.components.party.InventoryGroup.*;


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
    int damage;
    int protection;
    int defense;
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
        this.baseHit = item.baseHit;
        this.damage = item.damage;
        this.protection = item.protection;
        this.defense = item.defense;
        this.dexterity = item.dexterity;
        this.stealth = item.stealth;
    }

    public boolean hasSameIdAs(String candidateId) {
        return id.equalsIgnoreCase(candidateId);
    }

    public boolean isStackable() {
        return group.equals(RESOURCE);
    }

    void increaseAmountWith(InventoryItem sourceItem) {
        amount += sourceItem.amount;
    }

    public List<Map.Entry<String, String>> createDescription() {
        List<Map.Entry<String, String>> attributes = createListOfPairsWithTitlesAndValues();
        return createFilter(attributes);
    }

    private List<Map.Entry<String, String>> createListOfPairsWithTitlesAndValues() {
        return Arrays.asList(
                Map.entry(group.title, name),
                Map.entry(SKILL.title, skill == null ? "0" : skill.title),
                Map.entry(WEIGHT.title, String.valueOf(weight)),
                Map.entry(MIN_STRENGTH.title, String.valueOf(minStrength)),
                Map.entry(BASE_HIT.title, String.valueOf(baseHit) + "%"),
                Map.entry(DAMAGE.title, String.valueOf(damage)),
                Map.entry(PROTECTION.title, String.valueOf(protection)),
                Map.entry(DEFENSE.title, String.valueOf(defense)),
                Map.entry(DEXTERITY.title, String.valueOf(dexterity)),
                Map.entry(STEALTH.title, String.valueOf(stealth))
        );
    }

    private List<Map.Entry<String, String>> createFilter(List<Map.Entry<String, String>> attributes) {
        List<Map.Entry<String, String>> filtered = new ArrayList<>();
        attributes.forEach(attribute -> {
            if (attribute.getKey().equals(DEXTERITY.title) &&
                    group.equals(SHIELD)) {
                filtered.add(attribute);
                return;
            }
            if (attribute.getKey().equals(STEALTH.title) &&
                    group.equals(CHEST)) {
                filtered.add(attribute);
                return;
            }
            if (attribute.getKey().equals(WEIGHT.title) &&
                    (group.equals(SHIELD) ||
                            group.equals(WEAPON) ||
                            group.equals(RESOURCE))) {
                return;
            }
            if (attribute.getKey().equals(WEIGHT.title)) {
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

