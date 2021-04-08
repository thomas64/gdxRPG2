package nl.t64.game.rpg.components.party;

import lombok.AllArgsConstructor;


@AllArgsConstructor
public enum InventoryGroup implements SuperEnum {

    EVERYTHING(""),
    SHOP_ITEM(""),
    LOOT_ITEM(""),

    WEAPON("Weapon"),
    SHIELD("Shield"),               // 12 prt / 30 def
    ACCESSORY("Accessory"),

    HELMET("Helmet"),               // 12 prt
    NECKLACE("Necklace"),
    SHOULDERS("Shoulders"),         // 12 prt
    CHEST("Chest"),                 // 12 prt
    CLOAK("Cloak"),                 // 2 prt    extra stealth
    BRACERS("Bracers"),             // 12 prt
    GLOVES("Gloves"),               // 12 prt
    RING("Ring"),
    BELT("Belt"),                   // 2 prt    extra agility
    PANTS("Pants"),                 // 12 prt
    BOOTS("Boots"),                 // 12 prt   bij complete set prt omhoog

    EMPTY(""),

    POTION("Potion"),
    ITEM("Item"),
    RESOURCE("Resource");

    private final String title;

    @Override
    public String getTitle() {
        return title;
    }

    boolean hasImpactOnPrtAgiStl() {
        return switch (this) {
            case SHIELD, HELMET, SHOULDERS, CHEST, CLOAK, BRACERS, GLOVES, BELT, PANTS, BOOTS -> true;
            default -> false;
        };
    }

    boolean isPartArmorOfSet() {
        return switch (this) {
            case HELMET, SHOULDERS, CHEST, CLOAK, BRACERS, GLOVES, BELT, PANTS, BOOTS -> true;
            default -> false;
        };
    }

    boolean isStackable() {
        return switch (this) {
            case POTION, RESOURCE -> true;
            default -> false;
        };
    }

}
