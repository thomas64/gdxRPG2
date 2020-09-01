package nl.t64.game.rpg.components.party;

import nl.t64.game.rpg.GameTest;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;


class LuanaTest extends GameTest {

    @Test
    void whenHeroesAreCreated_LuanaShouldHaveRightStats() {
        final HeroItem luana = new HeroContainer().getHero("luana");

        assertThat(luana.school).isEqualTo(SchoolType.ELEMENTAL);
        assertThat(luana.getLevel()).isEqualTo(1);
        assertThat(luana.getTotalXp()).isEqualTo(5);
        assertThat(luana.getXpNeededForNextLevel()).isEqualTo(20);
        assertThat(luana.getCurrentHp()).isEqualTo(31);
        assertThat(luana.getMaximumHp()).isEqualTo(31);

        assertThat(luana.getStatById(StatItemId.INTELLIGENCE).rank).isEqualTo(14);
        assertThat(luana.getCalculatedTotalStatOf(StatItemId.INTELLIGENCE)).isEqualTo(14);
        assertThat(luana.getStatById(StatItemId.WILLPOWER).rank).isEqualTo(10);
        assertThat(luana.getCalculatedTotalStatOf(StatItemId.WILLPOWER)).isEqualTo(10);
        assertThat(luana.getStatById(StatItemId.DEXTERITY).rank).isEqualTo(22);
        assertThat(luana.getCalculatedTotalStatOf(StatItemId.DEXTERITY)).isEqualTo(22);
        assertThat(luana.getStatById(StatItemId.AGILITY).rank).isEqualTo(20);
        assertThat(luana.getCalculatedTotalStatOf(StatItemId.AGILITY)).isEqualTo(20);
        assertThat(luana.getStatById(StatItemId.ENDURANCE).rank).isEqualTo(10);
        assertThat(luana.getCalculatedTotalStatOf(StatItemId.ENDURANCE)).isEqualTo(10);
        assertThat(luana.getStatById(StatItemId.STRENGTH).rank).isEqualTo(8);
        assertThat(luana.getCalculatedTotalStatOf(StatItemId.STRENGTH)).isEqualTo(8);
        assertThat(luana.getStatById(StatItemId.STAMINA).rank).isEqualTo(20);
        assertThat(luana.getCalculatedTotalStatOf(StatItemId.STAMINA)).isEqualTo(20);

        assertThat(luana.getSkillById(SkillItemId.ALCHEMIST).rank).isZero();
        assertThat(luana.getCalculatedTotalSkillOf(SkillItemId.ALCHEMIST)).isZero();
        assertThat(luana.getSkillById(SkillItemId.DIPLOMAT).rank).isZero();
        assertThat(luana.getCalculatedTotalSkillOf(SkillItemId.DIPLOMAT)).isZero();
        assertThat(luana.getSkillById(SkillItemId.HEALER).rank).isZero();
        assertThat(luana.getCalculatedTotalSkillOf(SkillItemId.HEALER)).isZero();
        assertThat(luana.getSkillById(SkillItemId.LOREMASTER).rank).isZero();
        assertThat(luana.getCalculatedTotalSkillOf(SkillItemId.LOREMASTER)).isZero();
        assertThat(luana.getSkillById(SkillItemId.MECHANIC).rank).isEqualTo(1);
        assertThat(luana.getCalculatedTotalSkillOf(SkillItemId.MECHANIC)).isEqualTo(1);
        assertThat(luana.getSkillById(SkillItemId.MERCHANT).rank).isZero();
        assertThat(luana.getCalculatedTotalSkillOf(SkillItemId.MERCHANT)).isZero();
        assertThat(luana.getSkillById(SkillItemId.RANGER).rank).isZero();
        assertThat(luana.getCalculatedTotalSkillOf(SkillItemId.RANGER)).isZero();
        assertThat(luana.getSkillById(SkillItemId.STEALTH).rank).isEqualTo(3);
        assertThat(luana.getCalculatedTotalSkillOf(SkillItemId.STEALTH)).isEqualTo(3);
        assertThat(luana.getSkillById(SkillItemId.THIEF).rank).isEqualTo(3);
        assertThat(luana.getCalculatedTotalSkillOf(SkillItemId.THIEF)).isEqualTo(3);
        assertThat(luana.getSkillById(SkillItemId.TROUBADOUR).rank).isZero();
        assertThat(luana.getCalculatedTotalSkillOf(SkillItemId.TROUBADOUR)).isZero();
        assertThat(luana.getSkillById(SkillItemId.WARRIOR).rank).isZero();
        assertThat(luana.getCalculatedTotalSkillOf(SkillItemId.WARRIOR)).isZero();
        assertThat(luana.getSkillById(SkillItemId.WIZARD).rank).isZero();
        assertThat(luana.getCalculatedTotalSkillOf(SkillItemId.WIZARD)).isZero();

        assertThat(luana.getSkillById(SkillItemId.HAFTED).rank).isEqualTo(-1);
        assertThat(luana.getCalculatedTotalSkillOf(SkillItemId.HAFTED)).isZero();
        assertThat(luana.getSkillById(SkillItemId.MISSILE).rank).isEqualTo(-1);
        assertThat(luana.getCalculatedTotalSkillOf(SkillItemId.MISSILE)).isZero();
        assertThat(luana.getSkillById(SkillItemId.POLE).rank).isZero();
        assertThat(luana.getCalculatedTotalSkillOf(SkillItemId.POLE)).isZero();
        assertThat(luana.getSkillById(SkillItemId.SHIELD).rank).isEqualTo(-1);
        assertThat(luana.getCalculatedTotalSkillOf(SkillItemId.SHIELD)).isZero();
        assertThat(luana.getSkillById(SkillItemId.SWORD).rank).isEqualTo(1);
        assertThat(luana.getCalculatedTotalSkillOf(SkillItemId.SWORD)).isEqualTo(1);
        assertThat(luana.getSkillById(SkillItemId.THROWN).rank).isEqualTo(2);
        assertThat(luana.getCalculatedTotalSkillOf(SkillItemId.THROWN)).isEqualTo(2);

        assertThat(luana.getCalcValueOf(InventoryGroup.WEAPON, CalcAttributeId.BASE_HIT)).isEqualTo(40);
        assertThat(luana.getCalcValueOf(InventoryGroup.WEAPON, CalcAttributeId.DAMAGE)).isEqualTo(11);
        assertThat(luana.getCalcValueOf(InventoryGroup.SHIELD, CalcAttributeId.DEFENSE)).isZero();
        assertThat(luana.getTotalCalcOf(CalcAttributeId.PROTECTION)).isEqualTo(1);
    }

}
