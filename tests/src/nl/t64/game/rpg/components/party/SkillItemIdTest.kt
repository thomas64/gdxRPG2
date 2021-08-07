package nl.t64.game.rpg.components.party

import nl.t64.game.rpg.components.party.skills.SkillItemId
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatExceptionOfType
import org.junit.jupiter.api.Test


internal class SkillItemIdTest {

    @Test
    fun test() {
        assertThat(SkillItemId.from("diplomat")).isSameAs(SkillItemId.DIPLOMAT)
        assertThatExceptionOfType(NoSuchElementException::class.java).isThrownBy {
            SkillItemId.from("pipo")
        }
    }

}
