package nl.t64.game.rpg.components.inventory;

import lombok.AllArgsConstructor;


@AllArgsConstructor
public enum InventoryGroup {

    EVERYTHING(""),

    WEAPON("Weapon"),
    SHIELD("Shield"),               // 12 prt / 30 def
    ACCESSORY("Accessory"),

    HELMET("Helmet"),               // 12 prt
    NECKLACE("Necklace"),
    SHOULDERS("Shoulders"),         // 12 prt
    CHEST("Chest"),                 // 12 prt
    CLOAK("Cloak"),                 // 2 prt
    BRACERS("Bracers"),             // 12 prt
    GLOVES("Gloves"),               // 12 prt
    RING("Ring"),
    BELT("Belt"),                   // 1 prt
    PANTS("Pants"),                 // 12 prt   sum of totaal: prt / 3 ?
    BOOTS("Boots"),                 // 12 prt   bij complete set agi omhoog.

    RESOURCE("");

    final String title;

}
