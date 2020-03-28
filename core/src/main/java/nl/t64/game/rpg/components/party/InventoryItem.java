package nl.t64.game.rpg.components.party;

import com.badlogic.gdx.math.MathUtils;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.Objects;


@NoArgsConstructor
@Setter
public class InventoryItem {

    @Getter
    String id;
    String name;
    @Getter
    List<String> description;
    int amount;
    @Getter
    InventoryGroup group;
    SkillItemId skill;
    int price;
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
        this.price = item.price;
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
        return switch (minimal) {
            case SKILL -> Objects.requireNonNullElse(skill, 0);
            case MIN_INTELLIGENCE -> minIntelligence;
            case MIN_STRENGTH -> minStrength;
        };
    }

    int getAttributeOfStatItemId(StatItemId statItemId) {
        return switch (statItemId) {
            case DEXTERITY -> dexterity;
            case INTELLIGENCE, WILLPOWER, AGILITY, ENDURANCE, STRENGTH, STAMINA -> 0;
        };
    }

    int getAttributeOfSkillItemId(SkillItemId skillItemId) {
        return switch (skillItemId) {
            case STEALTH -> stealth;
            case ALCHEMIST, DIPLOMAT, HEALER, LOREMASTER, MECHANIC, MERCHANT, RANGER,
                    THIEF, TROUBADOUR, WARRIOR, WIZARD,
                    HAFTED, MISSILE, POLE, SHIELD, SWORD, THROWN -> 0;
        };
    }

    int getAttributeOfCalcAttributeId(CalcAttributeId calcAttributeId) {
        return switch (calcAttributeId) {
            case WEIGHT -> weight;
            case BASE_HIT -> baseHit;
            case DAMAGE -> damage;
            case PROTECTION -> protection;
            case DEFENSE -> defense;
        };
    }

    public boolean hasSameIdAs(String candidateId) {
        return id.equalsIgnoreCase(candidateId);
    }

    public boolean isStackable() {
        return group.equals(InventoryGroup.RESOURCE)
               || group.equals(InventoryGroup.POTION);
    }

    int getSellValue() {
        return MathUtils.floor(price / 3f);
    }

    void increaseAmountWith(InventoryItem sourceItem) {
        amount += sourceItem.amount;
    }

}
