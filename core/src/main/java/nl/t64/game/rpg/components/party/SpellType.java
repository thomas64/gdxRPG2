package nl.t64.game.rpg.components.party;

import lombok.AllArgsConstructor;


@AllArgsConstructor
public enum SpellType implements SuperEnum {

    DISPEL_NAMING(DispelNaming.class, "Dispel Naming"),
    DISPEL_NECROMANCY(DispelNecromancy.class, "Dispel Necromancy"),
    DISPEL_STAR(DispelStar.class, "Dispel Star"),
    MIRROR(Mirror.class, "Mirror"),
    VS_ELEMENTAL(VsElemental.class, "vs. Elemental"),
    VS_NECROMANCY(VsNecromancy.class, "vs. Necromancy"),
    VS_STAR(VsStar.class, "vs. Star"),

    AIR_SHIELD(AirShield.class, "Air Shield"),
    DEBILITATION(Debilitation.class, "Debilitation"),
    DRAGON_FLAMES(DragonFlames.class, "Dragon Flames"),
    FIREBALL(Fireball.class, "Fireball"),
    REMOVE_POISON(RemovePoison.class, "Remove Poison"),
    STRENGTH(StrengthPlus.class, "Strength"),
    WIND(Wind.class, "Wind"),

    BANISHING(Banishing.class, "Banishing"),
    ENDURANCE(EndurancePlus.class, "Endurance"),
    SENSE_AURA(SenseAura.class, "Sense Aura"),
    TELEPORTATION(Teleportation.class, "Teleportation"),
    WEAKNESS(Weakness.class, "Weakness"),

    SOLAR_WRATH(SolarWrath.class, "Solar Wrath"),
    STELLAR_GRAVITY(StellarGravity.class, "Stellar Gravity"),
    WEB_OF_STARLIGHT(WebOfStarlight.class, "Web of Starlight");

    final Class<? extends SpellItem> spellClass;
    final String title;

    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public String getDescription(HeroItem hero) {
        return title;
    }

}
