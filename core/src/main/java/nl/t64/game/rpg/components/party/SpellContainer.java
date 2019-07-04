package nl.t64.game.rpg.components.party;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Setter;

@Setter
class SpellContainer {

    @JsonProperty("vs_necromancy")
    private int vsNecromancy;
    private int endurance;
    @JsonProperty("sense_aura")
    private int senseAura;
    private int weakness;

    @JsonProperty("dragon_flames")
    private int dragonFlames;

    @JsonProperty("vs_elemental")
    private int vsElemental;
    @JsonProperty("remove_poision")
    private int removePoision;
    private int wind;

    @JsonProperty("vs_star")
    private int vsStar;
    private int fireball;
    private int mirror;
    @JsonProperty("dispel_necromancy")
    private int dispelNecromancy;

    private int banishing;
    private int teleportation;

    @JsonProperty("air_shield")
    private int airShield;
    private int strength;
    private int debilitation;

    @JsonProperty("web_of_starlight")
    private int webOfStarlight;
    @JsonProperty("dispel_star")
    private int dispelStar;
    @JsonProperty("stellar_gravity")
    private int stellarGravity;
    @JsonProperty("dispel_naming")
    private int dispelNaming;
    @JsonProperty("solar_wrath")
    private int solarWrath;

}
