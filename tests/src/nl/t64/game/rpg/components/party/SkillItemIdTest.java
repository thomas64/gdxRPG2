package nl.t64.game.rpg.components.party;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;


class SkillItemIdTest {

    @Test
    void test() {
        Assertions.assertThat(SkillItemId.from("diplomat")).containsSame(SkillItemId.DIPLOMAT);
        Assertions.assertThat(SkillItemId.from("pipo")).isEmpty();
    }

}
