package nl.t64.game.rpg.components.party;

import lombok.AllArgsConstructor;

import java.util.Arrays;
import java.util.Optional;


@AllArgsConstructor
public enum SkillItemId implements SuperEnum {

    ALCHEMIST("Alchemist"),
    DIPLOMAT("Diplomat"),
    HEALER("Healer"),
    LOREMASTER("Loremaster"),
    MECHANIC("Mechanic"),
    MERCHANT("Merchant"),
    RANGER("Ranger"),
    STEALTH("Stealth"),
    THIEF("Thief"),
    TROUBADOUR("Troubadour"),
    WARRIOR("Warrior"),
    WIZARD("Wizard"),

    HAFTED("Hafted"),
    MISSILE("Missile"),
    POLE("Pole"),
    SHIELD("Shield"),
    SWORD("Sword"),
    THROWN("Thrown");

    private final String title;

    @Override
    public String getTitle() {
        return title;
    }

    boolean isHandToHandWeaponSkill() {
        return switch (this) {
            case SWORD, HAFTED, POLE -> true;
            case MISSILE, THROWN -> false;
            default -> throw new IllegalCallerException("Only possible to ask a Weapon Skill.");
        };
    }

    public static Optional<SkillItemId> from(String possibleSkillItemId) {
        return Arrays.stream(SkillItemId.values())
                     .filter(skillItemId -> possibleSkillItemId.equalsIgnoreCase(skillItemId.name()))
                     .findFirst();
    }

}
