package nl.t64.game.rpg.components.party;

import lombok.Getter;


@Getter
public class InventoryDescription {

    final Object key;       // Object can be different kind of Enums.
    final Object value;     // Object can be Integer or String.
    final boolean isEnough;

    InventoryDescription(Object key, Object value, InventoryItem item, HeroItem hero) {
        this.key = key;
        this.value = value;
        this.isEnough = isEnough(item, hero);
    }

    private boolean isEnough(InventoryItem item, HeroItem hero) {
        if (value.equals(0)) {
            return true;
        }
        if (key instanceof InventoryMinimal) {
            return ((InventoryMinimal) key).createMessageIfHeroHasNotEnoughFor(item, hero)
                                           .isEmpty();
            // this means: return true if optionalIsEmpty.
        }
        return true;
    }

}
