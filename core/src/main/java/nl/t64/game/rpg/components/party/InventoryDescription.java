package nl.t64.game.rpg.components.party;

import lombok.Getter;
import nl.t64.game.rpg.constants.ThreeState;


@Getter
public class InventoryDescription {

    final SuperEnum key;
    final Object value;     // Object can be Integer or String.
    final ThreeState compare;

    InventoryDescription(SuperEnum key, Object value, InventoryItem item, HeroItem hero) {
        this.key = key;
        this.value = value;
        this.compare = isEnough(item, hero);
    }

    InventoryDescription(SuperEnum key, Object value, InventoryItem item1, InventoryItem item2) {
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

            if (key instanceof StatItemId) {
                StatItemId statItemId = (StatItemId) key;
                if (item1.getAttributeOfStatItemId(statItemId) < item2.getAttributeOfStatItemId(statItemId)) {
                    return ThreeState.LESS;
                } else if (item1.getAttributeOfStatItemId(statItemId) > item2.getAttributeOfStatItemId(statItemId)) {
                    return ThreeState.MORE;
                }

            } else if (key instanceof SkillItemId) {
                SkillItemId skillItemId = (SkillItemId) key;
                if (item1.getAttributeOfSkillItemId(skillItemId) < item2.getAttributeOfSkillItemId(skillItemId)) {
                    return ThreeState.LESS;
                } else if (item1.getAttributeOfSkillItemId(skillItemId) > item2.getAttributeOfSkillItemId(skillItemId)) {
                    return ThreeState.MORE;
                }

            } else if (key instanceof CalcAttributeId) {
                CalcAttributeId calcAttributeId = (CalcAttributeId) key;
                if (calcAttributeId.equals(CalcAttributeId.WEIGHT)) {
                    return ThreeState.SAME;
                }
                if (item1.getAttributeOfCalcAttributeId(calcAttributeId) < item2.getAttributeOfCalcAttributeId(calcAttributeId)) {
                    return ThreeState.LESS;
                } else if (item1.getAttributeOfCalcAttributeId(calcAttributeId) > item2.getAttributeOfCalcAttributeId(calcAttributeId)) {
                    return ThreeState.MORE;
                }
            }
        }
        return ThreeState.SAME;
    }

}
