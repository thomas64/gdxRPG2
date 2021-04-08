package nl.t64.game.rpg.components.party;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;


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
    @JsonProperty("is_two_handed")
    boolean isTwoHanded;
    SkillItemId skill;
    private int price;
    private int weight;
    @JsonProperty("min_intelligence")
    private int minIntelligence;
    @JsonProperty("min_dexterity")
    private int minDexterity;
    @JsonProperty("min_strength")
    private int minStrength;
    @JsonProperty("base_hit")
    private int baseHit;
    private int damage;
    int protection;
    private int defense;
    @JsonProperty("spell_battery")
    private int spellBattery;
    private int intelligence;
    private int willpower;
    private int dexterity;
    private int agility;
    private int diplomat;
    private int loremaster;
    private int stealth;
    private int thief;
    private int warrior;
    private int wizard;

    InventoryItem(InventoryItem item, int amount) {
        this.id = item.id;
        this.name = item.name;
        this.sort = item.sort;
        this.description = item.description;
        this.amount = amount;
        this.group = item.group;
        this.skill = item.skill;
        this.isTwoHanded = item.isTwoHanded;
        this.price = item.price;
        this.weight = item.weight;
        this.minIntelligence = item.minIntelligence;
        this.minDexterity = item.minDexterity;
        this.minStrength = item.minStrength;
        this.baseHit = item.baseHit;
        this.damage = item.damage;
        this.protection = item.protection;
        this.defense = item.defense;
        this.spellBattery = item.spellBattery;
        this.intelligence = item.intelligence;
        this.willpower = item.willpower;
        this.dexterity = item.dexterity;
        this.agility = item.agility;
        this.diplomat = item.diplomat;
        this.loremaster = item.loremaster;
        this.stealth = item.stealth;
        this.thief = item.thief;
        this.warrior = item.warrior;
        this.wizard = item.wizard;
    }

    public static InventoryItem copyOf(InventoryItem inventoryItem, int amount) {
        return new InventoryItem(inventoryItem, amount);
    }

    Object getAttributeOfMinimal(InventoryMinimal minimal) {
        return switch (minimal) {
            case SKILL -> Objects.requireNonNullElse(skill, 0);
            case MIN_INTELLIGENCE -> minIntelligence;
            case MIN_DEXTERITY -> minDexterity;
            case MIN_STRENGTH -> minStrength;
        };
    }

    int getMinimalAttributeOfStatItemId(StatItemId statItemId) {
        return switch (statItemId) {
            case INTELLIGENCE -> minIntelligence;
            case DEXTERITY -> minDexterity;
            case STRENGTH -> minStrength;
            case WILLPOWER, AGILITY, ENDURANCE, STAMINA -> 0;
        };
    }

    int getAttributeOfStatItemId(StatItemId statItemId) {
        return switch (statItemId) {
            case INTELLIGENCE -> intelligence;
            case WILLPOWER -> willpower;
            case DEXTERITY -> dexterity;
            case AGILITY -> agility;
            case ENDURANCE, STRENGTH, STAMINA -> 0;
        };
    }

    int getAttributeOfSkillItemId(SkillItemId skillItemId) {
        return switch (skillItemId) {
            case DIPLOMAT -> diplomat;
            case LOREMASTER -> loremaster;
            case STEALTH -> stealth;
            case THIEF -> thief;
            case WARRIOR -> warrior;
            case WIZARD -> wizard;
            case ALCHEMIST, HEALER, MECHANIC, MERCHANT, RANGER, TROUBADOUR,
                    HAFTED, MISSILE, POLE, SHIELD, SWORD, THROWN -> 0;
        };
    }

    int getAttributeOfCalcAttributeId(CalcAttributeId calcAttributeId) {
        return switch (calcAttributeId) {
            case WEIGHT -> weight;
            case MOVEPOINTS -> 0;
            case BASE_HIT -> baseHit;
            case DAMAGE -> damage;
            case PROTECTION -> protection;
            case DEFENSE -> defense;
            case SPELL_BATTERY -> spellBattery;
        };
    }

    public Set<InventoryMinimal> getMinimalsOtherItemHasAndYouDont(InventoryItem otherItem) {
        if (hasOnlyOneMinimal() && otherItem.hasOnlyOneMinimal()) {
            return Set.of();
        } else {
            return Arrays.stream(InventoryMinimal.values())
                         .filter(minimal -> getAttributeOfMinimal(minimal) instanceof Integer)
                         .filter(minimal -> (int) getAttributeOfMinimal(minimal) == 0)
                         .filter(minimal -> (int) otherItem.getAttributeOfMinimal(minimal) > 0)
                         .collect(Collectors.toUnmodifiableSet());
        }
    }

    private boolean hasOnlyOneMinimal() {
        return Arrays.stream(InventoryMinimal.values())
                     .filter(minimal -> getAttributeOfMinimal(minimal) instanceof Integer)
                     .filter(minimal -> (int) getAttributeOfMinimal(minimal) > 0)
                     .count() == 1L;
    }

    public Set<CalcAttributeId> getCalcsOtherItemHasAndYouDont(InventoryItem otherItem) {
        return Arrays.stream(CalcAttributeId.values())
                     .filter(calcAttributeId -> getAttributeOfCalcAttributeId(calcAttributeId) == 0)
                     .filter(calcAttributeId -> otherItem.getAttributeOfCalcAttributeId(calcAttributeId) > 0)
                     .collect(Collectors.toUnmodifiableSet());
    }

    public Set<StatItemId> getStatsOtherItemHasAndYouDont(InventoryItem otherItem) {
        return Arrays.stream(StatItemId.values())
                     .filter(statItemId -> getAttributeOfStatItemId(statItemId) == 0)
                     .filter(statItemId -> otherItem.getAttributeOfStatItemId(statItemId) > 0)
                     .collect(Collectors.toUnmodifiableSet());
    }

    public Set<SkillItemId> getSkillsOtherItemHasAndYouDont(InventoryItem otherItem) {
        return Arrays.stream(SkillItemId.values())
                     .filter(skillItemId -> getAttributeOfSkillItemId(skillItemId) == 0)
                     .filter(skillItemId -> otherItem.getAttributeOfSkillItemId(skillItemId) > 0)
                     .collect(Collectors.toUnmodifiableSet());
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
