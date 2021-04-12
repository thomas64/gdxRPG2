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
    @JsonProperty("min_willpower")
    private int minWillpower;
    @JsonProperty("min_dexterity")
    private int minDexterity;
    @JsonProperty("min_strength")
    private int minStrength;
    private int movepoints;
    @JsonProperty("base_hit")
    private int baseHit;
    private int damage;
    private int protection;
    private int defense;
    @JsonProperty("spell_battery")
    private int spellBattery;
    private int intelligence;
    private int willpower;
    private int dexterity;
    private int strength;
    private int agility;
    private int endurance;
    private int alchemist;
    private int diplomat;
    private int healer;
    private int loremaster;
    private int mechanic;
    private int ranger;
    private int stealth;
    private int thief;
    private int troubadour;
    private int warrior;
    private int wizard;
    @JsonProperty("cheat_death")
    private boolean cheatDeath;
    @JsonProperty("quick_switch")
    private boolean quickSwitch;
    @JsonProperty("spell_boost")
    private boolean spellBoost;

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
        this.minWillpower = item.minWillpower;
        this.minDexterity = item.minDexterity;
        this.minStrength = item.minStrength;
        this.movepoints = item.movepoints;
        this.baseHit = item.baseHit;
        this.damage = item.damage;
        this.protection = item.protection;
        this.defense = item.defense;
        this.spellBattery = item.spellBattery;
        this.intelligence = item.intelligence;
        this.willpower = item.willpower;
        this.dexterity = item.dexterity;
        this.strength = item.strength;
        this.agility = item.agility;
        this.endurance = item.endurance;
        this.alchemist = item.alchemist;
        this.diplomat = item.diplomat;
        this.healer = item.healer;
        this.loremaster = item.loremaster;
        this.mechanic = item.mechanic;
        this.ranger = item.ranger;
        this.stealth = item.stealth;
        this.thief = item.thief;
        this.troubadour = item.troubadour;
        this.warrior = item.warrior;
        this.wizard = item.wizard;
        this.cheatDeath = item.cheatDeath;
        this.quickSwitch = item.quickSwitch;
        this.spellBoost = item.spellBoost;
    }

    public static InventoryItem copyOf(InventoryItem inventoryItem, int amount) {
        return new InventoryItem(inventoryItem, amount);
    }

    Object getAttributeOfMinimal(InventoryMinimal minimal) {
        return switch (minimal) {
            case SKILL -> Objects.requireNonNullElse(skill, 0);
            case MIN_INTELLIGENCE -> minIntelligence;
            case MIN_WILLPOWER -> minWillpower;
            case MIN_DEXTERITY -> minDexterity;
            case MIN_STRENGTH -> minStrength;
        };
    }

    int getMinimalAttributeOfStatItemId(StatItemId statItemId) {
        return switch (statItemId) {
            case INTELLIGENCE -> minIntelligence;
            case WILLPOWER -> minWillpower;
            case DEXTERITY -> minDexterity;
            case STRENGTH -> minStrength;
            case AGILITY, ENDURANCE, STAMINA -> 0;
        };
    }

    int getAttributeOfStatItemId(StatItemId statItemId) {
        return switch (statItemId) {
            case INTELLIGENCE -> intelligence;
            case WILLPOWER -> willpower;
            case DEXTERITY -> dexterity;
            case STRENGTH -> strength;
            case AGILITY -> agility;
            case ENDURANCE -> endurance;
            case STAMINA -> 0;
        };
    }

    int getAttributeOfSkillItemId(SkillItemId skillItemId) {
        return switch (skillItemId) {
            case ALCHEMIST -> alchemist;
            case DIPLOMAT -> diplomat;
            case HEALER -> healer;
            case LOREMASTER -> loremaster;
            case MECHANIC -> mechanic;
            case STEALTH -> stealth;
            case RANGER -> ranger;
            case THIEF -> thief;
            case TROUBADOUR -> troubadour;
            case WARRIOR -> warrior;
            case WIZARD -> wizard;
            case MERCHANT, HAFTED, MISSILE, POLE, SHIELD, SWORD, THROWN -> 0;
        };
    }

    int getAttributeOfCalcAttributeId(CalcAttributeId calcAttributeId) {
        return switch (calcAttributeId) {
            case WEIGHT -> weight;
            case MOVEPOINTS -> movepoints;
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

    String createMessageFailToDequip(InventoryItem dependantItem) {
        return String.format("""
                                     Cannot unequip the %s.
                                     The %s depends on it.""", name, dependantItem.name);
    }

    String createMessageFailToEquipTwoHanded(InventoryItem otherItem) {
        return String.format("""
                                     Cannot equip the %s.
                                     First unequip the %s.""", name, otherItem.name);
    }

}
