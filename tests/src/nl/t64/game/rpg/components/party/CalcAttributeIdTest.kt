package nl.t64.game.rpg.components.party

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test


internal class CalcAttributeIdTest {

    @Test
    fun test() {
        assertThat(CalcAttributeId.from("something")).isNull()
        assertThat(CalcAttributeId.from("weapon damage")).isSameAs(CalcAttributeId.DAMAGE)
    }

}
