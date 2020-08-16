package nl.t64.game.rpg.components.party;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

/* todo:
    misschien wil ik alle spells, stats en skills ipv in individuele classes in json hebben zoals items.
    maar dat weet ik nog niet zeker. wellicht komt er nog een hoop verschillende logica in de verschillende classes.
    en dan is json niet handig. misschien gaat het wel gebeuren dat alleen de spells in json moeten komen, en niet
    de stats en de skills. */
@Getter
@NoArgsConstructor
public abstract class SpellItem implements PersonalityItem {
    private static final List<Integer> TRAINING_COSTS = List.of(20, 8, 12, 16, 20, 24, 28, 32, 36, 40, 0);
    private static final int MAXIMUM = 10;

    @Setter
    String id;                      // Constant value
    String name;                    // Constant value
    int sort;                       // Constant value for sorting in list.
    float upgrade;                  // Constant value for upgrading formula.
    int minWizard;                  // Constant value for minimal Wizard rank necessary to learn a spell.
    SchoolType school;              // Constant value
    ResourceType requiredResource;  // Constant value
    int staminaCost;                // Constant value
    int rank;
    int bonus;

    SpellItem(int rank) {
        this.rank = rank;
        this.bonus = 0;
    }

    static Class<?> getSpellClass(String spellClassName) throws ClassNotFoundException {
        final String fullPath = SpellItem.class.getPackage().getName() + "." + spellClassName;
        return Class.forName(fullPath);
    }

    @Override
    public String getDescription(int totalLoremaster) {
        return getDescription() + System.lineSeparator() + System.lineSeparator()
               + "School: " + school.title + System.lineSeparator()
               + "Requires: " + requiredResource.title + System.lineSeparator()
               + "Stamina cost: " + staminaCost + System.lineSeparator() + System.lineSeparator()
               + getNeededXpForNextLevel(totalLoremaster) + System.lineSeparator()
               + getNeededGoldForNextLevel();
    }

    abstract String getDescription();

    private String getNeededXpForNextLevel(int totalLoremaster) {
        String xpNeeded = String.valueOf(getXpCostForNextLevel(totalLoremaster));
        if (xpNeeded.equals("0")) {
            xpNeeded = "Max";
        }
        return "XP needed for next level: " + xpNeeded;
    }

    private String getNeededGoldForNextLevel() {
        String goldNeeded = String.valueOf(getGoldCostForNextLevel());
        if (goldNeeded.equals("0")) {
            goldNeeded = "Max";
        }
        return "Gold needed for next level: " + goldNeeded;
    }

    private int getXpCostForNextLevel(int totalLoremaster) {
        if (rank >= MAXIMUM) {
            return 0;
        }
        return Math.round(getUpgradeFormula() - ((getUpgradeFormula() / 100) * totalLoremaster));
    }

    private int getGoldCostForNextLevel() {
        final int nextLevel = rank + 1;
        return TRAINING_COSTS.get(nextLevel - 1);
    }

    private float getUpgradeFormula() {
        final int nextLevel = rank + 1;
        return upgrade * (nextLevel * nextLevel);
    }

}

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

@NoArgsConstructor
class DispelElemental extends SpellItem {
    DispelElemental(int rank) {
        super(rank);
        this.name = "Dispel Elemental";
        this.sort = 10;
        this.upgrade = 3.2f;
        this.minWizard = 2;
        this.school = SchoolType.NEUTRAL;
        this.requiredResource = ResourceType.GEMSTONES;
        this.staminaCost = 7;
    }

    @Override
    String getDescription() {
        return "Tekst en uitleg over " + name + ".";
    }
}

@NoArgsConstructor
class DispelNaming extends SpellItem {
    DispelNaming(int rank) {
        super(rank);
        this.name = "Dispel Naming";
        this.sort = 15;
        this.upgrade = 3.2f;
        this.minWizard = 2;
        this.school = SchoolType.NEUTRAL;
        this.requiredResource = ResourceType.GEMSTONES;
        this.staminaCost = 6;
    }

    @Override
    String getDescription() {
        return "Tekst en uitleg over " + name + ".";
    }
}

@NoArgsConstructor
class DispelNecromancy extends SpellItem {
    DispelNecromancy(int rank) {
        super(rank);
        this.name = "Dispel Necromancy";
        this.sort = 20;
        this.upgrade = 3.2f;
        this.minWizard = 2;
        this.school = SchoolType.NEUTRAL;
        this.requiredResource = ResourceType.GEMSTONES;
        this.staminaCost = 6;
    }

    @Override
    String getDescription() {
        return "Tekst en uitleg over " + name + ".";
    }
}

@NoArgsConstructor
class DispelStar extends SpellItem {
    DispelStar(int rank) {
        super(rank);
        this.name = "Dispel Star";
        this.sort = 25;
        this.upgrade = 3.2f;
        this.minWizard = 2;
        this.school = SchoolType.NEUTRAL;
        this.requiredResource = ResourceType.GEMSTONES;
        this.staminaCost = 6;
    }

    @Override
    String getDescription() {
        return "Tekst en uitleg over " + name + ".";
    }
}

@NoArgsConstructor
class Mirror extends SpellItem {
    Mirror(int rank) {
        super(rank);
        this.name = "Mirror";
        this.sort = 30;
        this.upgrade = 2f;
        this.minWizard = 6;
        this.school = SchoolType.NEUTRAL;
        this.requiredResource = ResourceType.GEMSTONES;
        this.staminaCost = 10;
    }

    @Override
    String getDescription() {
        return "Tekst en uitleg over " + name + ".";
    }
}

@NoArgsConstructor
class VsElemental extends SpellItem {
    VsElemental(int rank) {
        super(rank);
        this.name = "vs. Elemental";
        this.sort = 35;
        this.upgrade = 2.4f;
        this.minWizard = 1;
        this.school = SchoolType.NEUTRAL;
        this.requiredResource = ResourceType.GEMSTONES;
        this.staminaCost = 5;
    }

    @Override
    String getDescription() {
        return "Tekst en uitleg over " + name + ".";
    }
}

@NoArgsConstructor
class VsNaming extends SpellItem {
    VsNaming(int rank) {
        super(rank);
        this.name = "vs. Naming";
        this.sort = 40;
        this.upgrade = 2.4f;
        this.minWizard = 1;
        this.school = SchoolType.NEUTRAL;
        this.requiredResource = ResourceType.GEMSTONES;
        this.staminaCost = 5;
    }

    @Override
    String getDescription() {
        return "Tekst en uitleg over " + name + ".";
    }
}

@NoArgsConstructor
class VsNecromancy extends SpellItem {
    VsNecromancy(int rank) {
        super(rank);
        this.name = "vs. Necromancy";
        this.sort = 45;
        this.upgrade = 2.4f;
        this.minWizard = 1;
        this.school = SchoolType.NEUTRAL;
        this.requiredResource = ResourceType.GEMSTONES;
        this.staminaCost = 3;
    }

    @Override
    String getDescription() {
        return "Tekst en uitleg over " + name + ".";
    }
}

@NoArgsConstructor
class VsStar extends SpellItem {
    VsStar(int rank) {
        super(rank);
        this.name = "vs. Star";
        this.sort = 50;
        this.upgrade = 2.4f;
        this.minWizard = 1;
        this.school = SchoolType.NEUTRAL;
        this.requiredResource = ResourceType.GEMSTONES;
        this.staminaCost = 5;
    }

    @Override
    String getDescription() {
        return "Tekst en uitleg over " + name + ".";
    }
}

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

@NoArgsConstructor
class AirShield extends SpellItem {
    AirShield(int rank) {
        super(rank);
        this.name = "Air Shield";
        this.sort = 110;
        this.upgrade = 3.2f;
        this.minWizard = 1;
        this.school = SchoolType.ELEMENTAL;
        this.requiredResource = ResourceType.GEMSTONES;
        this.staminaCost = 6;
    }

    @Override
    String getDescription() {
        return "Tekst en uitleg over " + name + ".";
    }
}

@NoArgsConstructor
class ControlElementals extends SpellItem {
    ControlElementals(int rank) {
        super(rank);
        this.name = "Control Elementals";
        this.sort = 115;
        this.upgrade = 5.2f;
        this.minWizard = 4;
        this.school = SchoolType.ELEMENTAL;
        this.requiredResource = ResourceType.SPICES;
        this.staminaCost = 20;
    }

    @Override
    String getDescription() {
        return "Tekst en uitleg over " + name + ".";
    }
}

@NoArgsConstructor
class Debilitation extends SpellItem {
    Debilitation(int rank) {
        super(rank);
        this.name = "Debilitation";
        this.sort = 120;
        this.upgrade = 3.2f;
        this.minWizard = 2;
        this.school = SchoolType.ELEMENTAL;
        this.requiredResource = ResourceType.HERBS;
        this.staminaCost = 6;
    }

    @Override
    String getDescription() {
        return "Tekst en uitleg over " + name + ".";
    }
}

@NoArgsConstructor
class DragonFlames extends SpellItem {
    DragonFlames(int rank) {
        super(rank);
        this.name = "Dragon Flames";
        this.sort = 125;
        this.upgrade = 5.2f;
        this.minWizard = 2;
        this.school = SchoolType.ELEMENTAL;
        this.requiredResource = ResourceType.GEMSTONES;
        this.staminaCost = 10;
    }

    @Override
    String getDescription() {
        return "Tekst en uitleg over " + name + ".";
    }
}

@NoArgsConstructor
class EarthSmite extends SpellItem {
    EarthSmite(int rank) {
        super(rank);
        this.name = "Earth Smite";
        this.sort = 130;
        this.upgrade = 6f;
        this.minWizard = 4;
        this.school = SchoolType.ELEMENTAL;
        this.requiredResource = ResourceType.GEMSTONES;
        this.staminaCost = 10;
    }

    @Override
    String getDescription() {
        return "Tekst en uitleg over " + name + ".";
    }
}

@NoArgsConstructor
class Fireball extends SpellItem {
    Fireball(int rank) {
        super(rank);
        this.name = "Fireball";
        this.sort = 135;
        this.upgrade = 4f;
        this.minWizard = 5;
        this.school = SchoolType.ELEMENTAL;
        this.requiredResource = ResourceType.SPICES;
        this.staminaCost = 15;
    }

    @Override
    String getDescription() {
        return "Tekst en uitleg over " + name + ".";
    }
}

@NoArgsConstructor
class Immolation extends SpellItem {
    Immolation(int rank) {
        super(rank);
        this.name = "Immolation";
        this.sort = 140;
        this.upgrade = 1.6f;
        this.minWizard = 4;
        this.school = SchoolType.ELEMENTAL;
        this.requiredResource = ResourceType.SPICES;
        this.staminaCost = 8;
    }

    @Override
    String getDescription() {
        return "Tekst en uitleg over " + name + ".";
    }
}

@NoArgsConstructor
class Lightning extends SpellItem {
    Lightning(int rank) {
        super(rank);
        this.name = "Lightning";
        this.sort = 145;
        this.upgrade = 4f;
        this.minWizard = 3;
        this.school = SchoolType.ELEMENTAL;
        this.requiredResource = ResourceType.GEMSTONES;
        this.staminaCost = 3;
    }

    @Override
    String getDescription() {
        return "Tekst en uitleg over " + name + ".";
    }
}

@NoArgsConstructor
class RemovePoison extends SpellItem {
    RemovePoison(int rank) {
        super(rank);
        this.name = "Remove Poison";
        this.sort = 150;
        this.upgrade = 0.8f;
        this.minWizard = 2;
        this.school = SchoolType.ELEMENTAL;
        this.requiredResource = ResourceType.HERBS;
        this.staminaCost = 2;
    }

    @Override
    String getDescription() {
        return "Tekst en uitleg over " + name + ".";
    }
}

@NoArgsConstructor
class StrengthPlus extends SpellItem {
    StrengthPlus(int rank) {
        super(rank);
        this.name = "Strength";
        this.sort = 155;
        this.upgrade = 1.6f;
        this.minWizard = 1;
        this.school = SchoolType.ELEMENTAL;
        this.requiredResource = ResourceType.HERBS;
        this.staminaCost = 7;
    }

    @Override
    String getDescription() {
        return "Tekst en uitleg over " + name + ".";
    }
}

@NoArgsConstructor
class Wind extends SpellItem {
    Wind(int rank) {
        super(rank);
        this.name = "Wind";
        this.sort = 160;
        this.upgrade = 5.2f;
        this.minWizard = 2;
        this.school = SchoolType.ELEMENTAL;
        this.requiredResource = ResourceType.HERBS;
        this.staminaCost = 7;
    }

    @Override
    String getDescription() {
        return "Tekst en uitleg over " + name + ".";
    }
}

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

@NoArgsConstructor
class Banishing extends SpellItem {
    Banishing(int rank) {
        super(rank);
        this.name = "Banishing";
        this.sort = 210;
        this.upgrade = 5.2f;
        this.minWizard = 4;
        this.school = SchoolType.NAMING;
        this.requiredResource = ResourceType.GEMSTONES;
        this.staminaCost = 15;
    }

    @Override
    String getDescription() {
        return "Tekst en uitleg over " + name + ".";
    }
}

@NoArgsConstructor
class Brilliance extends SpellItem {
    Brilliance(int rank) {
        super(rank);
        this.name = "Brilliance";
        this.sort = 215;
        this.upgrade = 1.2f;
        this.minWizard = 2;
        this.school = SchoolType.NAMING;
        this.requiredResource = ResourceType.GEMSTONES;
        this.staminaCost = 6;
    }

    @Override
    String getDescription() {
        return "Tekst en uitleg over " + name + ".";
    }
}

@NoArgsConstructor
class Charming extends SpellItem {
    Charming(int rank) {
        super(rank);
        this.name = "Charming";
        this.sort = 220;
        this.upgrade = 4f;
        this.minWizard = 3;
        this.school = SchoolType.NAMING;
        this.requiredResource = ResourceType.GEMSTONES;
        this.staminaCost = 6;
    }

    @Override
    String getDescription() {
        return "Tekst en uitleg over " + name + ".";
    }
}

@NoArgsConstructor
class ControlMarquis extends SpellItem {
    ControlMarquis(int rank) {
        super(rank);
        this.name = "Control Marquis";
        this.sort = 222;
        this.upgrade = 4f;
        this.minWizard = 3;
        this.school = SchoolType.NAMING;
        this.requiredResource = ResourceType.GEMSTONES;
        this.staminaCost = 10;
    }

    @Override
    String getDescription() {
        return "Tekst en uitleg over " + name + ".";
    }
}

@NoArgsConstructor
class EndurancePlus extends SpellItem {
    EndurancePlus(int rank) {
        super(rank);
        this.name = "Endurance";
        this.sort = 225;
        this.upgrade = 3.2f;
        this.minWizard = 2;
        this.school = SchoolType.NAMING;
        this.requiredResource = ResourceType.SPICES;
        this.staminaCost = 6;
    }

    @Override
    String getDescription() {
        return "Tekst en uitleg over " + name + ".";
    }
}

@NoArgsConstructor
class SenseAura extends SpellItem {
    SenseAura(int rank) {
        super(rank);
        this.name = "Sense Aura";
        this.sort = 230;
        this.upgrade = 4f;
        this.minWizard = 3;
        this.school = SchoolType.NAMING;
        this.requiredResource = ResourceType.HERBS;
        this.staminaCost = 2;
    }

    @Override
    String getDescription() {
        return "Tekst en uitleg over " + name + ".";
    }
}

@NoArgsConstructor
class Stupidity extends SpellItem {
    Stupidity(int rank) {
        super(rank);
        this.name = "Stupidity";
        this.sort = 235;
        this.upgrade = 1.2f;
        this.minWizard = 2;
        this.school = SchoolType.NAMING;
        this.requiredResource = ResourceType.HERBS;
        this.staminaCost = 6;
    }

    @Override
    String getDescription() {
        return "Tekst en uitleg over " + name + ".";
    }
}

@NoArgsConstructor
class Teleportation extends SpellItem {
    Teleportation(int rank) {
        super(rank);
        this.name = "Teleportation";
        this.sort = 240;
        this.upgrade = 3.2f;
        this.minWizard = 8;
        this.school = SchoolType.NAMING;
        this.requiredResource = ResourceType.GEMSTONES;
        this.staminaCost = 10;
    }

    @Override
    String getDescription() {
        return "Tekst en uitleg over " + name + ".";
    }
}

@NoArgsConstructor
class Weakness extends SpellItem {
    Weakness(int rank) {
        super(rank);
        this.name = "Weakness";
        this.sort = 245;
        this.upgrade = 3.2f;
        this.minWizard = 2;
        this.school = SchoolType.NAMING;
        this.requiredResource = ResourceType.HERBS;
        this.staminaCost = 6;
    }

    @Override
    String getDescription() {
        return "Tekst en uitleg over " + name + ".";
    }
}

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

@NoArgsConstructor
class AcidBolt extends SpellItem {
    AcidBolt(int rank) {
        super(rank);
        this.name = "Acid Bolt";
        this.sort = 310;
        this.upgrade = 1.6f;
        this.minWizard = 4;
        this.school = SchoolType.NECROMANCY;
        this.requiredResource = ResourceType.SPICES;
        this.staminaCost = 8;
    }

    @Override
    String getDescription() {
        return "Tekst en uitleg over " + name + ".";
    }
}

@NoArgsConstructor
class AuraOfDeath extends SpellItem {
    AuraOfDeath(int rank) {
        super(rank);
        this.name = "Aura of Death";
        this.sort = 312;
        this.upgrade = 0.8f;
        this.minWizard = 4;
        this.school = SchoolType.NECROMANCY;
        this.requiredResource = ResourceType.SPICES;
        this.staminaCost = 5;
    }

    @Override
    String getDescription() {
        return "Tekst en uitleg over " + name + ".";
    }
}

@NoArgsConstructor
class CheatDeath extends SpellItem {
    CheatDeath(int rank) {
        super(rank);
        this.name = "Cheat Death";
        this.sort = 314;
        this.upgrade = 2.4f;
        this.minWizard = 6;
        this.school = SchoolType.NECROMANCY;
        this.requiredResource = ResourceType.GEMSTONES;
        this.staminaCost = 10;
    }

    @Override
    String getDescription() {
        return "Tekst en uitleg over " + name + ".";
    }
}

@NoArgsConstructor
class ControlZombies extends SpellItem {
    ControlZombies(int rank) {
        super(rank);
        this.name = "Control Zombies";
        this.sort = 315;
        this.upgrade = 4f;
        this.minWizard = 3;
        this.school = SchoolType.NECROMANCY;
        this.requiredResource = ResourceType.HERBS;
        this.staminaCost = 10;
    }

    @Override
    String getDescription() {
        return "Tekst en uitleg over " + name + ".";
    }
}

@NoArgsConstructor
class CrushingDeath extends SpellItem {
    CrushingDeath(int rank) {
        super(rank);
        this.name = "Crushing Death";
        this.sort = 317;
        this.upgrade = 6f;
        this.minWizard = 7;
        this.school = SchoolType.NECROMANCY;
        this.requiredResource = ResourceType.SPICES;
        this.staminaCost = 10;
    }

    @Override
    String getDescription() {
        return "Tekst en uitleg over " + name + ".";
    }
}

@NoArgsConstructor
class Darkness extends SpellItem {
    Darkness(int rank) {
        super(rank);
        this.name = "Darkness";
        this.sort = 320;
        this.upgrade = 3.2f;
        this.minWizard = 2;
        this.school = SchoolType.NECROMANCY;
        this.requiredResource = ResourceType.GEMSTONES;
        this.staminaCost = 6;
    }

    @Override
    String getDescription() {
        return "Tekst en uitleg over " + name + ".";
    }
}

@NoArgsConstructor
class Exhaustion extends SpellItem {
    Exhaustion(int rank) {
        super(rank);
        this.name = "Exhaustion";
        this.sort = 325;
        this.upgrade = 3.2f;
        this.minWizard = 2;
        this.school = SchoolType.NECROMANCY;
        this.requiredResource = ResourceType.GEMSTONES;
        this.staminaCost = 6;
    }

    @Override
    String getDescription() {
        return "Tekst en uitleg over " + name + ".";
    }
}

@NoArgsConstructor
class Haste extends SpellItem {
    Haste(int rank) {
        super(rank);
        this.name = "Haste";
        this.sort = 330;
        this.upgrade = 5.2f;
        this.minWizard = 4;
        this.school = SchoolType.NECROMANCY;
        this.requiredResource = ResourceType.SPICES;
        this.staminaCost = 5;
    }

    @Override
    String getDescription() {
        return "Tekst en uitleg over " + name + ".";
    }

}

@NoArgsConstructor
class SpiritShield extends SpellItem {
    SpiritShield(int rank) {
        super(rank);
        this.name = "Spirit Shield";
        this.sort = 335;
        this.upgrade = 4f;
        this.minWizard = 3;
        this.school = SchoolType.NECROMANCY;
        this.requiredResource = ResourceType.GEMSTONES;
        this.staminaCost = 6;
    }

    @Override
    String getDescription() {
        return "Tekst en uitleg over " + name + ".";
    }
}

@NoArgsConstructor
class StaminaPlus extends SpellItem {
    StaminaPlus(int rank) {
        super(rank);
        this.name = "Stamina";
        this.sort = 340;
        this.upgrade = 3.2f;
        this.minWizard = 2;
        this.school = SchoolType.NECROMANCY;
        this.requiredResource = ResourceType.GEMSTONES;
        this.staminaCost = 6;
    }

    @Override
    String getDescription() {
        return "Tekst en uitleg over " + name + ".";
    }
}

@NoArgsConstructor
class TapStamina extends SpellItem {
    TapStamina(int rank) {
        super(rank);
        this.name = "Tap Stamina";
        this.sort = 342;
        this.upgrade = 4f;
        this.minWizard = 3;
        this.school = SchoolType.NECROMANCY;
        this.requiredResource = ResourceType.SPICES;
        this.staminaCost = 5;
    }

    @Override
    String getDescription() {
        return "Tekst en uitleg over " + name + ".";
    }
}

@NoArgsConstructor
class WallOfBones extends SpellItem {
    WallOfBones(int rank) {
        super(rank);
        this.name = "Wall of Bones";
        this.sort = 345;
        this.upgrade = 5.2f;
        this.minWizard = 5;
        this.school = SchoolType.NECROMANCY;
        this.requiredResource = ResourceType.HERBS;
        this.staminaCost = 15;
    }

    @Override
    String getDescription() {
        return "Tekst en uitleg over " + name + ".";
    }
}

@NoArgsConstructor
class WraithTouch extends SpellItem {
    WraithTouch(int rank) {
        super(rank);
        this.name = "Wraith Touch";
        this.sort = 350;
        this.upgrade = 1.6f;
        this.minWizard = 8;
        this.school = SchoolType.NECROMANCY;
        this.requiredResource = ResourceType.GEMSTONES;
        this.staminaCost = 2;
    }

    @Override
    String getDescription() {
        return "Tekst en uitleg over " + name + ".";
    }
}

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

@NoArgsConstructor
class Clumsiness extends SpellItem {
    Clumsiness(int rank) {
        super(rank);
        this.name = "Clumsiness";
        this.sort = 406;
        this.upgrade = 3.2f;
        this.minWizard = 2;
        this.school = SchoolType.STAR;
        this.requiredResource = ResourceType.SPICES;
        this.staminaCost = 6;
    }

    @Override
    String getDescription() {
        return "Tekst en uitleg over " + name + ".";
    }
}

@NoArgsConstructor
class DexterityPlus extends SpellItem {
    DexterityPlus(int rank) {
        super(rank);
        this.name = "Dexterity";
        this.sort = 408;
        this.upgrade = 3.2f;
        this.minWizard = 2;
        this.school = SchoolType.STAR;
        this.requiredResource = ResourceType.SPICES;
        this.staminaCost = 7;
    }

    @Override
    String getDescription() {
        return "Tekst en uitleg over " + name + ".";
    }
}

@NoArgsConstructor
class FrozenDoom extends SpellItem {
    FrozenDoom(int rank) {
        super(rank);
        this.name = "Frozen Doom";
        this.sort = 410;
        this.upgrade = 6f;
        this.minWizard = 6;
        this.school = SchoolType.STAR;
        this.requiredResource = ResourceType.HERBS;
        this.staminaCost = 10;
    }

    @Override
    String getDescription() {
        return "Tekst en uitleg over " + name + ".";
    }
}

@NoArgsConstructor
class Light extends SpellItem {
    Light(int rank) {
        super(rank);
        this.name = "Light";
        this.sort = 412;
        this.upgrade = 3.2f;
        this.minWizard = 2;
        this.school = SchoolType.STAR;
        this.requiredResource = ResourceType.SPICES;
        this.staminaCost = 5;
    }

    @Override
    String getDescription() {
        return "Tekst en uitleg over " + name + ".";
    }
}

@NoArgsConstructor
class Photosynthesis extends SpellItem {
    Photosynthesis(int rank) {
        super(rank);
        this.name = "Photosynthesis";
        this.sort = 414;
        this.upgrade = 1.6f;
        this.minWizard = 3;
        this.school = SchoolType.STAR;
        this.requiredResource = ResourceType.HERBS;
        this.staminaCost = 8;
    }

    @Override
    String getDescription() {
        return "Tekst en uitleg over " + name + ".";
    }
}

@NoArgsConstructor
class SolarWrath extends SpellItem {
    SolarWrath(int rank) {
        super(rank);
        this.name = "Solar Wrath";
        this.sort = 415;
        this.upgrade = 0.8f;
        this.minWizard = 4;
        this.school = SchoolType.STAR;
        this.requiredResource = ResourceType.GEMSTONES;
        this.staminaCost = 5;
    }

    @Override
    String getDescription() {
        return "Tekst en uitleg over " + name + ".";
    }
}

@NoArgsConstructor
class StarlightShield extends SpellItem {
    StarlightShield(int rank) {
        super(rank);
        this.name = "Starlight Shield";
        this.sort = 417;
        this.upgrade = 4f;
        this.minWizard = 3;
        this.school = SchoolType.STAR;
        this.requiredResource = ResourceType.GEMSTONES;
        this.staminaCost = 8;
    }

    @Override
    String getDescription() {
        return "Tekst en uitleg over " + name + ".";
    }
}

@NoArgsConstructor
class StellarGravity extends SpellItem {
    StellarGravity(int rank) {
        super(rank);
        this.name = "Stellar Gravity";
        this.sort = 420;
        this.upgrade = 3.2f;
        this.minWizard = 2;
        this.school = SchoolType.STAR;
        this.requiredResource = ResourceType.SPICES;
        this.staminaCost = 6;
    }

    @Override
    String getDescription() {
        return "Tekst en uitleg over " + name + ".";
    }
}

@NoArgsConstructor
class WebOfStarlight extends SpellItem {
    WebOfStarlight(int rank) {
        super(rank);
        this.name = "Web of Starlight";
        this.sort = 425;
        this.upgrade = 4f;
        this.minWizard = 3;
        this.school = SchoolType.STAR;
        this.requiredResource = ResourceType.GEMSTONES;
        this.staminaCost = 10;
    }

    @Override
    String getDescription() {
        return "Tekst en uitleg over " + name + ".";
    }
}

@NoArgsConstructor
class WhiteFire extends SpellItem {
    WhiteFire(int rank) {
        super(rank);
        this.name = "White Fire";
        this.sort = 430;
        this.upgrade = 6f;
        this.minWizard = 6;
        this.school = SchoolType.STAR;
        this.requiredResource = ResourceType.SPICES;
        this.staminaCost = 15;
    }

    @Override
    String getDescription() {
        return "Tekst en uitleg over " + name + ".";
    }
}
