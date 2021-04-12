package nl.t64.game.rpg.components.party;

import com.fasterxml.jackson.annotation.JsonCreator;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


class SpellContainer {

    private final Map<String, SpellItem> spells;

    private SpellContainer() {
        this.spells = new HashMap<>();
    }

    @JsonCreator
    private SpellContainer(Map<String, Integer> startingSpells) {
        this();
        putInContainer(startingSpells);
    }

    private void putInContainer(Map<String, Integer> startingSpells) {
        var database = SpellDatabase.getInstance();
        startingSpells.entrySet()
                      .stream()
                      .map(spellSet -> database.createSpellItem(spellSet.getKey(), spellSet.getValue()))
                      .forEach(spell -> spells.put(spell.id, spell));
    }

    List<SpellItem> getAll() {
        return spells.values()
                     .stream()
                     .sorted(Comparator.comparing(SpellItem::getSort))
                     .toList();
    }

}
