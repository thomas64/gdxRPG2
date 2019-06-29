package nl.t64.game.rpg.components.party;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


@NoArgsConstructor
@Setter
public class InventoryItem {

    @Getter
    String id;
    String name;
    int amount;
    @Getter
    InventoryGroup group;
    SkillType skill;
    int weight;
    @JsonProperty("min_intelligence")
    int minIntelligence;
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
        this.minIntelligence = item.minIntelligence;
        this.minStrength = item.minStrength;
        this.baseHit = item.baseHit;
        this.damage = item.damage;
        this.protection = item.protection;
        this.defense = item.defense;
        this.dexterity = item.dexterity;
        this.stealth = item.stealth;
    }

    public Object getAttributeOfMinimal(InventoryMinimal minimal) {
        switch (minimal) {
            case SKILL:
                return Objects.requireNonNullElse(skill, 0);
            case MIN_INTELLIGENCE:
                return minIntelligence;
            case MIN_STRENGTH:
                return minStrength;
            default:
                throw new IllegalArgumentException(String.format("InventoryMinimal '%s' not usable.", minimal));
        }
    }

    public int getAttributeOfStatType(StatType statType) {
        switch (statType) {
            case WEIGHT:
                return weight;
            case BASE_HIT:
                return baseHit;
            case DAMAGE:
                return damage;
            case PROTECTION:
                return protection;
            case DEFENSE:
                return defense;
            case DEXTERITY:
                return dexterity;
            default:
                return 0;
        }
    }

    public int getAttributeOfSkillType(SkillType skillType) {
        switch (skillType) {
            case STEALTH:
                return stealth;
            default:
                return 0;
        }
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

    public List<InventoryDescription> createDescriptionFor(HeroItem hero) {
        List<InventoryDescription> attributes = new ArrayList<>();
        attributes.add(new InventoryDescription(group, name, this, hero));

        for (InventoryMinimal minimal : InventoryMinimal.values()) {
            attributes.add(new InventoryDescription(minimal, getAttributeOfMinimal(minimal), this, hero));
        }
        for (StatType statType : StatType.values()) {
            attributes.add(new InventoryDescription(statType, getAttributeOfStatType(statType), this, hero));
        }
        for (SkillType skillType : SkillType.values()) {
            attributes.add(new InventoryDescription(skillType, getAttributeOfSkillType(skillType), this, hero));
        }
        return createFilter(attributes);
    }

    private List<InventoryDescription> createFilter(List<InventoryDescription> attributes) {
        List<InventoryDescription> filtered = new ArrayList<>();
        attributes.forEach(attribute -> {
            if (attribute.key instanceof InventoryGroup) {
                filtered.add(attribute);
                return;
            }
            if (attribute.value instanceof SkillType) {
                filtered.add(attribute);
                return;
            }
            if (attribute.key.equals(StatType.DEXTERITY) && group.equals(InventoryGroup.SHIELD)) {
                filtered.add(attribute);
                return;
            }
            if (attribute.key.equals(SkillType.STEALTH) && group.equals(InventoryGroup.CHEST)) {
                filtered.add(attribute);
                return;
            }
            if (attribute.key.equals(StatType.WEIGHT)
                    && (group.equals(InventoryGroup.SHIELD)
                    || group.equals(InventoryGroup.WEAPON)
                    || group.equals(InventoryGroup.RESOURCE))) {
                return;
            }
            if (attribute.key.equals(StatType.WEIGHT)) {
                filtered.add(attribute);
                return;
            }
            if (attribute.value.equals(0)) {
                return;
            }
            filtered.add(attribute);
        });
        return filtered;
    }

}
