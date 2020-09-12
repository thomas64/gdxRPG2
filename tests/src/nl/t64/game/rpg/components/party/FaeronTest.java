package nl.t64.game.rpg.components.party;

import nl.t64.game.rpg.GameTest;
import nl.t64.game.rpg.constants.*;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;


class FaeronTest extends GameTest {

    @Test
    void whenHeroesAreCreated_FaeronShouldHaveRightStats() {
        final HeroItem faeron = new HeroContainer().getHero("faeron");

        assertThat(faeron.school).isEqualTo(SchoolType.NAMING);
        assertThat(faeron.getLevel()).isEqualTo(25);
        assertThat(faeron.getTotalXp()).isEqualTo(27625);
        assertThat(faeron.getXpNeededForNextLevel()).isEqualTo(3380);
        assertThat(faeron.getCurrentHp()).isEqualTo(130);
        assertThat(faeron.getMaximumHp()).isEqualTo(130);

        assertThat(faeron.getStatById(StatItemId.INTELLIGENCE).rank).isEqualTo(30);
        assertThat(faeron.getCalculatedTotalStatOf(StatItemId.INTELLIGENCE)).isEqualTo(30);
        assertThat(faeron.getStatById(StatItemId.WILLPOWER).rank).isEqualTo(30);
        assertThat(faeron.getCalculatedTotalStatOf(StatItemId.WILLPOWER)).isEqualTo(30);
        assertThat(faeron.getStatById(StatItemId.DEXTERITY).rank).isEqualTo(30);
        assertThat(faeron.getCalculatedTotalStatOf(StatItemId.DEXTERITY)).isEqualTo(30);
        assertThat(faeron.getStatById(StatItemId.AGILITY).rank).isEqualTo(30);
        assertThat(faeron.getCalculatedTotalStatOf(StatItemId.AGILITY)).isEqualTo(30);
        assertThat(faeron.getStatById(StatItemId.ENDURANCE).rank).isEqualTo(25);
        assertThat(faeron.getCalculatedTotalStatOf(StatItemId.ENDURANCE)).isEqualTo(25);
        assertThat(faeron.getStatById(StatItemId.STRENGTH).rank).isEqualTo(15);
        assertThat(faeron.getCalculatedTotalStatOf(StatItemId.STRENGTH)).isEqualTo(15);
        assertThat(faeron.getStatById(StatItemId.STAMINA).rank).isEqualTo(80);
        assertThat(faeron.getCalculatedTotalStatOf(StatItemId.STAMINA)).isEqualTo(80);

        assertThat(faeron.getSkillById(SkillItemId.ALCHEMIST).rank).isEqualTo(10);
        assertThat(faeron.getCalculatedTotalSkillOf(SkillItemId.ALCHEMIST)).isEqualTo(10);
        assertThat(faeron.getSkillById(SkillItemId.DIPLOMAT).rank).isEqualTo(10);
        assertThat(faeron.getCalculatedTotalSkillOf(SkillItemId.DIPLOMAT)).isEqualTo(10);
        assertThat(faeron.getSkillById(SkillItemId.HEALER).rank).isZero();
        assertThat(faeron.getCalculatedTotalSkillOf(SkillItemId.HEALER)).isZero();
        assertThat(faeron.getSkillById(SkillItemId.LOREMASTER).rank).isEqualTo(10);
        assertThat(faeron.getCalculatedTotalSkillOf(SkillItemId.LOREMASTER)).isEqualTo(10);
        assertThat(faeron.getSkillById(SkillItemId.MECHANIC).rank).isZero();
        assertThat(faeron.getCalculatedTotalSkillOf(SkillItemId.MECHANIC)).isZero();
        assertThat(faeron.getSkillById(SkillItemId.MERCHANT).rank).isEqualTo(10);
        assertThat(faeron.getCalculatedTotalSkillOf(SkillItemId.MERCHANT)).isEqualTo(10);
        assertThat(faeron.getSkillById(SkillItemId.RANGER).rank).isEqualTo(10);
        assertThat(faeron.getCalculatedTotalSkillOf(SkillItemId.RANGER)).isEqualTo(10);
        assertThat(faeron.getSkillById(SkillItemId.STEALTH).rank).isEqualTo(10);
        assertThat(faeron.getCalculatedTotalSkillOf(SkillItemId.STEALTH)).isEqualTo(10);
        assertThat(faeron.getSkillById(SkillItemId.THIEF).rank).isEqualTo(10);
        assertThat(faeron.getCalculatedTotalSkillOf(SkillItemId.THIEF)).isEqualTo(10);
        assertThat(faeron.getSkillById(SkillItemId.TROUBADOUR).rank).isEqualTo(10);
        assertThat(faeron.getCalculatedTotalSkillOf(SkillItemId.TROUBADOUR)).isEqualTo(10);
        assertThat(faeron.getSkillById(SkillItemId.WARRIOR).rank).isEqualTo(10);
        assertThat(faeron.getCalculatedTotalSkillOf(SkillItemId.WARRIOR)).isEqualTo(10);
        assertThat(faeron.getSkillById(SkillItemId.WIZARD).rank).isEqualTo(-1);
        assertThat(faeron.getCalculatedTotalSkillOf(SkillItemId.WIZARD)).isZero();

        assertThat(faeron.getSkillById(SkillItemId.HAFTED).rank).isEqualTo(10);
        assertThat(faeron.getCalculatedTotalSkillOf(SkillItemId.HAFTED)).isEqualTo(10);
        assertThat(faeron.getSkillById(SkillItemId.MISSILE).rank).isEqualTo(-1);
        assertThat(faeron.getCalculatedTotalSkillOf(SkillItemId.MISSILE)).isZero();
        assertThat(faeron.getSkillById(SkillItemId.POLE).rank).isEqualTo(-1);
        assertThat(faeron.getCalculatedTotalSkillOf(SkillItemId.POLE)).isZero();
        assertThat(faeron.getSkillById(SkillItemId.SHIELD).rank).isZero();
        assertThat(faeron.getCalculatedTotalSkillOf(SkillItemId.SHIELD)).isZero();
        assertThat(faeron.getSkillById(SkillItemId.SWORD).rank).isEqualTo(10);
        assertThat(faeron.getCalculatedTotalSkillOf(SkillItemId.SWORD)).isEqualTo(10);
        assertThat(faeron.getSkillById(SkillItemId.THROWN).rank).isEqualTo(-1);
        assertThat(faeron.getCalculatedTotalSkillOf(SkillItemId.THROWN)).isZero();

        assertThat(faeron.getCalcValueOf(InventoryGroup.WEAPON, CalcAttributeId.BASE_HIT)).isEqualTo(60);
        assertThat(faeron.getCalcValueOf(InventoryGroup.WEAPON, CalcAttributeId.DAMAGE)).isEqualTo(24);
        assertThat(faeron.getCalcValueOf(InventoryGroup.SHIELD, CalcAttributeId.DEFENSE)).isZero();
        assertThat(faeron.getTotalCalcOf(CalcAttributeId.PROTECTION)).isEqualTo(4);
    }

}
