package nl.t64.game.rpg.components.party;

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
    int sort;
    @Getter
    List<String> description;
    @Getter
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
        this.sort = item.sort;
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

    public boolean hasSameIdAs(InventoryItem candidateItem) {
        return hasSameIdAs(candidateItem.id);
    }

    boolean hasSameIdAs(String candidateId) {
        return id.equalsIgnoreCase(candidateId);
    }

    public boolean isStackable() {
        return group.isStackable();
    }

    public int getBuyPrice(int totalMerchant) {
        int salePrice = Math.round(price - ((price / 100f) * totalMerchant));
        if (salePrice == 0) {
            salePrice = 1;
        }
        return salePrice * amount;
    }

    public int getSellValue(int totalMerchant) {
        int value = (int) Math.floor(price / 3f);
        int saleValue = Math.round(value + ((value / 300f) * totalMerchant));
        return saleValue * amount;
    }

    void increaseAmountWith(InventoryItem sourceItem) {
        increaseAmountWith(sourceItem.amount);
    }

    void increaseAmountWith(int amount) {
        this.amount += amount;
    }

    void decreaseAmountWith(int amount) {
        this.amount -= amount;
        if (this.amount < 1) {
            throw new IllegalStateException("Amount cannot be below 1.");
        }
    }

}
