package nl.t64.game.rpg.components.party;

import com.fasterxml.jackson.annotation.JsonCreator;

import java.lang.reflect.Constructor;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


class SpellContainer {

    private final Map<String, SpellItem> spells;

    private SpellContainer() {
        this.spells = new HashMap<>();
    }

    @JsonCreator
    private SpellContainer(Map<String, Integer> startingSpells) {
        this();
        for (Map.Entry<String, Integer> spellSet : startingSpells.entrySet()) {
            try {
                putInContainer(spellSet);
            } catch (ReflectiveOperationException e) {
                throw new IllegalArgumentException(String.format("SpellType '%s' not usable.", spellSet.getKey()));
            }
        }
    }

    private void putInContainer(Map.Entry<String, Integer> spellSet) throws ReflectiveOperationException {
        SpellType spellType = SpellType.valueOf(spellSet.getKey().toUpperCase());
        Constructor<?>[] constructors = spellType.spellClass.getDeclaredConstructors();
        SpellItem spellItem = (SpellItem) constructors[0].newInstance(spellSet.getValue());
        spells.put(spellType.name(), spellItem);
    }

    int getRankOf(SpellType spellType) {
        return spells.get(spellType.name()).rank;
    }

    List<SpellType> getAll() {
        return Arrays.stream(SpellType.values())
                     .filter(this::contains)
                     .collect(Collectors.toList());
    }

    private boolean contains(SpellType spellType) {
        return spells.containsKey(spellType.name());
    }


}
