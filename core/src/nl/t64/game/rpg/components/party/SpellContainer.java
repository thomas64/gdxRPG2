package nl.t64.game.rpg.components.party;

import com.fasterxml.jackson.annotation.JsonCreator;

import java.lang.reflect.Constructor;
import java.util.Comparator;
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

    private static Class<?> getClassOf(String spellName) throws ClassNotFoundException {
        return SpellItem.getSpellClass(getCamelCaseOf(spellName));
    }

    private static String getCamelCaseOf(String spellName) {
        spellName = Character.toUpperCase(spellName.charAt(0)) + spellName.substring(1);
        while (spellName.contains("_")) {
            spellName = spellName.replaceFirst(
                    "_[a-z]",
                    String.valueOf(Character.toUpperCase(spellName.charAt(spellName.indexOf('_') + 1)))
            );
        }
        return spellName;
    }

    private void putInContainer(Map.Entry<String, Integer> spellSet) throws ReflectiveOperationException {
        final Constructor<?>[] constructors = getClassOf(spellSet.getKey()).getDeclaredConstructors();
        final SpellItem spellItem = (SpellItem) constructors[0].newInstance(spellSet.getValue());
        spellItem.setId(spellSet.getKey());
        spells.put(spellItem.id, spellItem);
    }

    List<SpellItem> getAll() {
        return spells.values()
                     .stream()
                     .sorted(Comparator.comparing(SpellItem::getSort))
                     .collect(Collectors.toList());
    }

}
