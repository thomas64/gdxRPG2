package nl.t64.game.rpg.components.party;

import lombok.Getter;
import nl.t64.game.rpg.constants.ThreeState;


@Getter
public class InventoryDescription {

    final Object key;       // Object can be different kind of Enums.
    final Object value;     // Object can be Integer or String.
    final ThreeState compare;

    InventoryDescription(Object key, Object value, InventoryItem item, HeroItem hero) {
        this.key = key;
        this.value = value;
        this.compare = isEnough(item, hero);
    }

    InventoryDescription(Object key, Object value, InventoryItem item1, InventoryItem item2) {
        this.key = key;
        this.value = value;
        this.compare = compare(item1, item2);
    }

    private ThreeState isEnough(InventoryItem item, HeroItem hero) {
        if (value.equals(0)) {
            return ThreeState.SAME;
        }
        if (key instanceof InventoryMinimal) {
            return ((InventoryMinimal) key).createMessageIfHeroHasNotEnoughFor(item, hero)
                                           .isEmpty() ? ThreeState.SAME : ThreeState.LESS;
        }
        return ThreeState.SAME;
    }

    private ThreeState compare(InventoryItem item1, InventoryItem item2) {
        if (value instanceof Integer) {

            if (key instanceof StatType) {
                StatType statType = (StatType) key;
                if (item1.getAttributeOfStatType(statType) < item2.getAttributeOfStatType(statType)) {
                    return ThreeState.LESS;
                } else if (item1.getAttributeOfStatType(statType) > item2.getAttributeOfStatType(statType)) {
                    return ThreeState.MORE;
                }

            } else if (key instanceof SkillType) {
                SkillType skillType = (SkillType) key;
                if (item1.getAttributeOfSkillType(skillType) < item2.getAttributeOfSkillType(skillType)) {
                    return ThreeState.LESS;
                } else if (item1.getAttributeOfSkillType(skillType) > item2.getAttributeOfSkillType(skillType)) {
                    return ThreeState.MORE;
                }

            } else if (key instanceof CalcType) {
                CalcType calcType = (CalcType) key;
                if (calcType.equals(CalcType.WEIGHT)) {
                    return ThreeState.SAME;
                }
                if (item1.getAttributeOfCalcType(calcType) < item2.getAttributeOfCalcType(calcType)) {
                    return ThreeState.LESS;
                } else if (item1.getAttributeOfCalcType(calcType) > item2.getAttributeOfCalcType(calcType)) {
                    return ThreeState.MORE;
                }
            }
        }
        return ThreeState.SAME;
    }

}
