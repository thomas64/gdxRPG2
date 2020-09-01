package nl.t64.game.rpg.components.party;

import nl.t64.game.rpg.GameTest;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;


class MagicTest extends GameTest {

    private final SpellDatabase spellDB = SpellDatabase.getInstance();

    @Test
    void whenSpellIsCreatedAtRank1_ShouldHaveVariables() {
        SpellItem airShield = spellDB.createSpellItem("air_shield", 1);
        assertThat(airShield.getId()).isEqualTo("air_shield");
        assertThat(airShield.getName()).isEqualTo("Air Shield");
        assertThat(airShield.getSort()).isEqualTo(110);
        assertThat(airShield.getRank()).isEqualTo(1);
        assertThat(airShield.getDescription(0))
                .isEqualToIgnoringWhitespace("""
                                                     All friendly entities within the range of the spell
                                                     add 1 per Rank to their Protection value.
                                                                                                                      
                                                     School: Elemental
                                                     Requires: Gemstone
                                                     Stamina cost: 6
                                                                                                                      
                                                     XP needed for next level: 13
                                                     Gold needed for next level: 8
                                                     """);
    }

    @Test
    void whenSpellIsCreatedAtRank10_ShouldHaveVariables() {
        SpellItem airShield = spellDB.createSpellItem("air_shield", 10);
        assertThat(airShield.getId()).isEqualTo("air_shield");
        assertThat(airShield.getName()).isEqualTo("Air Shield");
        assertThat(airShield.getSort()).isEqualTo(110);
        assertThat(airShield.getRank()).isEqualTo(10);
        assertThat(airShield.getDescription(0))
                .isEqualToIgnoringWhitespace("""
                                                     All friendly entities within the range of the spell
                                                     add 1 per Rank to their Protection value.
                                                                                                                      
                                                     School: Elemental
                                                     Requires: Gemstone
                                                     Stamina cost: 6
                                                                                                                      
                                                     XP needed for next level: Max
                                                     Gold needed for next level: Max
                                                     """);
    }

}
