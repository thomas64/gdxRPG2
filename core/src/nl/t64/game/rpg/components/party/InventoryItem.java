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
    private int price;
    private int weight;
    @JsonProperty("min_intelligence")
    int minIntelligence;
    @JsonProperty("min_strength")
    int minStrength;
    @JsonProperty("base_hit")
    private int baseHit;
    private int damage;
    private int protection;
    private int defense;
    private int dexterity;
    private int stealth;

    InventoryItem(InventoryItem item, int amount) {
        this.id = item.id;
        this.name = item.name;
        this.sort = item.sort;
        this.description = item.description;
        this.amount = amount;
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

    public static InventoryItem copyOf(InventoryItem inventoryItem, int amount) {
        return new InventoryItem(inventoryItem, amount);
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

    public int getBuyPriceTotal(int totalMerchant) {
        return getBuyPricePiece(totalMerchant) * amount;
    }

    public int getBuyPricePiece(int totalMerchant) {
        int salePrice = Math.round(price - ((price / 100f) * totalMerchant));
        if (salePrice == 0) {
            salePrice = 1;
        }
        return salePrice;
    }

    public int getSellValueTotal(int totalMerchant) {
        return getSellValuePiece(totalMerchant) * amount;
    }

    public int getSellValuePiece(int totalMerchant) {
        int value = (int) Math.floor(price / 3f);
        return Math.round(value + ((value / 300f) * totalMerchant));
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
