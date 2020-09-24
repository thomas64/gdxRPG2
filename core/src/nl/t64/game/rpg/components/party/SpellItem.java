package nl.t64.game.rpg.components.party;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

/* todo: moeten alle stats en skills ipv in individuele classes in json komen net zoals spells?
    wellicht komt er nog een hoop verschillende logica in de verschillende classes. dan is json niet handig. */

@NoArgsConstructor
@Setter
public class SpellItem implements PersonalityItem {

    private static final List<Integer> TRAINING_COSTS = List.of(20, 8, 12, 16, 20, 24, 28, 32, 36, 40, 0);
    private static final int MAXIMUM = 10;

    @Getter
    String id;
    @Getter
    private String name;
    private SchoolType school;
    @Getter
    private int sort;
    private float upgrade;
    @JsonProperty("min_wizard")
    private int minWizard;
    @JsonProperty("resource")
    private ResourceType requiredResource;
    @JsonProperty("stamina_cost")
    private int staminaCost;
    private int range;
    @JsonProperty("number_of_targets")
    private NumberOfTargets numberOfTargets;
    private Target target;
    private int damage;
    private List<String> description;

    @Getter
    private int rank;
    private int bonus;

    SpellItem(SpellItem item, int rank) {
        this.id = item.id;
        this.name = item.name;
        this.school = item.school;
        this.sort = item.sort;
        this.upgrade = item.upgrade;
        this.minWizard = item.minWizard;
        this.requiredResource = item.requiredResource;
        this.staminaCost = item.staminaCost;
        this.range = item.range;
        this.numberOfTargets = item.numberOfTargets;
        this.target = item.target;
        this.damage = item.damage;
        this.description = item.description;

        this.rank = rank;
        this.bonus = 0;
    }

    @Override
    public String getDescription(int totalLoremaster) {
        return String.join(System.lineSeparator(), description) + System.lineSeparator()
               + System.lineSeparator()
               + "School: " + school.getTitle() + System.lineSeparator()
               + "Requires: " + requiredResource.title + System.lineSeparator()
               + "Stamina cost: " + staminaCost + System.lineSeparator()
               + System.lineSeparator()
               + getNeededXpForNextLevel(totalLoremaster) + System.lineSeparator()
               + getNeededGoldForNextLevel();
    }

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
