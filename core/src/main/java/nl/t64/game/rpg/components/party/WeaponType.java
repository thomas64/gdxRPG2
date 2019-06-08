package nl.t64.game.rpg.components.party;

import lombok.AllArgsConstructor;


@AllArgsConstructor
enum WeaponType {

    SWORD("Sword"),
    HAFTED("Hafted"),
    POLE("Pole"),
    MISSILE("Missile"),
    THROWN("Thrown");

    final String title;

}
