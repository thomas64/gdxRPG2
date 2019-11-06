package nl.t64.game.rpg.components.party;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Objects;


@NoArgsConstructor
@Setter
public class InventoryItem {

    @Getter
    String id;
    String name;
    @Getter
    String description;
    int amount;
    @Getter
    InventoryGroup group;
    SkillItemId skill;
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
        this.description = item.description;
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

    Object getAttributeOfMinimal(InventoryMinimal minimal) {
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

    int getAttributeOfStatItemId(StatItemId statItemId) {
        switch (statItemId) {
            case DEXTERITY:
                return dexterity;
            default:
                return 0;
        }
    }

    int getAttributeOfSkillItemId(SkillItemId skillItemId) {
        switch (skillItemId) {
            case STEALTH:
                return stealth;
            default:
                return 0;
        }
    }

    int getAttributeOfCalcAttributeId(CalcAttributeId calcAttributeId) {
        switch (calcAttributeId) {
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
            default:
                throw new IllegalArgumentException(String.format("CalcAttributeId '%s' not usable.", calcAttributeId));
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

}
