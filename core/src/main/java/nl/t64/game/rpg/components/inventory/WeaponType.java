package nl.t64.game.rpg.components.inventory;

import lombok.AllArgsConstructor;


@AllArgsConstructor
public enum WeaponType {

    SWORD("Sword"),
    HAFTED("Hafted"),
    POLE("Pole"),
    MISSILE("Missile"),
    THROWN("Thrown");

    final String title;

}
