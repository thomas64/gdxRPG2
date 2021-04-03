package nl.t64.game.rpg.components.party;

import nl.t64.game.rpg.GameTest;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;


class ValterTest extends GameTest {

    @Test
    void whenHeroesAreCreated_ValterShouldHaveRightStats() {
        final HeroItem valter = new HeroContainer().getHero("valter");

        assertThat(valter.school).isEqualTo(SchoolType.ELEMENTAL);
        assertThat(valter.getLevel()).isEqualTo(2);
        assertThat(valter.getTotalXp()).isEqualTo(25);
        assertThat(valter.getXpNeededForNextLevel()).isEqualTo(45);
        assertThat(valter.getCurrentHp()).isEqualTo(37);
        assertThat(valter.getMaximumHp()).isEqualTo(37);

        assertThat(valter.getStatById(StatItemId.INTELLIGENCE).rank).isEqualTo(22);
        assertThat(valter.getCalculatedTotalStatOf(StatItemId.INTELLIGENCE)).isEqualTo(22);
        assertThat(valter.getStatById(StatItemId.WILLPOWER).rank).isEqualTo(18);
        assertThat(valter.getCalculatedTotalStatOf(StatItemId.WILLPOWER)).isEqualTo(18);
        assertThat(valter.getStatById(StatItemId.DEXTERITY).rank).isEqualTo(15);
        assertThat(valter.getCalculatedTotalStatOf(StatItemId.DEXTERITY)).isEqualTo(15);
        assertThat(valter.getStatById(StatItemId.AGILITY).rank).isEqualTo(12);
        assertThat(valter.getCalculatedTotalStatOf(StatItemId.AGILITY)).isEqualTo(12);
        assertThat(valter.getStatById(StatItemId.ENDURANCE).rank).isEqualTo(15);
        assertThat(valter.getCalculatedTotalStatOf(StatItemId.ENDURANCE)).isEqualTo(15);
        assertThat(valter.getStatById(StatItemId.STRENGTH).rank).isEqualTo(10);
        assertThat(valter.getCalculatedTotalStatOf(StatItemId.STRENGTH)).isEqualTo(10);
        assertThat(valter.getStatById(StatItemId.STAMINA).rank).isEqualTo(20);
        assertThat(valter.getCalculatedTotalStatOf(StatItemId.STAMINA)).isEqualTo(20);

        assertThat(valter.getSkillById(SkillItemId.ALCHEMIST).rank).isEqualTo(3);
        assertThat(valter.getCalculatedTotalSkillOf(SkillItemId.ALCHEMIST)).isEqualTo(3);
        assertThat(valter.getSkillById(SkillItemId.DIPLOMAT).rank).isZero();
        assertThat(valter.getCalculatedTotalSkillOf(SkillItemId.DIPLOMAT)).isZero();
        assertThat(valter.getSkillById(SkillItemId.HEALER).rank).isZero();
        assertThat(valter.getCalculatedTotalSkillOf(SkillItemId.HEALER)).isZero();
        assertThat(valter.getSkillById(SkillItemId.LOREMASTER).rank).isEqualTo(2);
        assertThat(valter.getCalculatedTotalSkillOf(SkillItemId.LOREMASTER)).isEqualTo(2);
        assertThat(valter.getSkillById(SkillItemId.MECHANIC).rank).isEqualTo(2);
        assertThat(valter.getCalculatedTotalSkillOf(SkillItemId.MECHANIC)).isEqualTo(2);
        assertThat(valter.getSkillById(SkillItemId.MERCHANT).rank).isZero();
        assertThat(valter.getCalculatedTotalSkillOf(SkillItemId.MERCHANT)).isZero();
        assertThat(valter.getSkillById(SkillItemId.RANGER).rank).isZero();
        assertThat(valter.getCalculatedTotalSkillOf(SkillItemId.RANGER)).isZero();
        assertThat(valter.getSkillById(SkillItemId.STEALTH).rank).isZero();
        assertThat(valter.getCalculatedTotalSkillOf(SkillItemId.STEALTH)).isZero();
        assertThat(valter.getSkillById(SkillItemId.THIEF).rank).isZero();
        assertThat(valter.getCalculatedTotalSkillOf(SkillItemId.THIEF)).isZero();
        assertThat(valter.getSkillById(SkillItemId.TROUBADOUR).rank).isZero();
        assertThat(valter.getCalculatedTotalSkillOf(SkillItemId.TROUBADOUR)).isZero();
        assertThat(valter.getSkillById(SkillItemId.WARRIOR).rank).isZero();
        assertThat(valter.getCalculatedTotalSkillOf(SkillItemId.WARRIOR)).isZero();
        assertThat(valter.getSkillById(SkillItemId.WIZARD).rank).isEqualTo(2);
        assertThat(valter.getCalculatedTotalSkillOf(SkillItemId.WIZARD)).isEqualTo(2);

        assertThat(valter.getSkillById(SkillItemId.HAFTED).rank).isEqualTo(-1);
        assertThat(valter.getCalculatedTotalSkillOf(SkillItemId.HAFTED)).isZero();
        assertThat(valter.getSkillById(SkillItemId.MISSILE).rank).isEqualTo(-1);
        assertThat(valter.getCalculatedTotalSkillOf(SkillItemId.MISSILE)).isZero();
        assertThat(valter.getSkillById(SkillItemId.POLE).rank).isZero();
        assertThat(valter.getCalculatedTotalSkillOf(SkillItemId.POLE)).isZero();
        assertThat(valter.getSkillById(SkillItemId.SHIELD).rank).isEqualTo(-1);
        assertThat(valter.getCalculatedTotalSkillOf(SkillItemId.SHIELD)).isZero();
        assertThat(valter.getSkillById(SkillItemId.SWORD).rank).isEqualTo(1);
        assertThat(valter.getCalculatedTotalSkillOf(SkillItemId.SWORD)).isEqualTo(1);
        assertThat(valter.getSkillById(SkillItemId.THROWN).rank).isEqualTo(1);
        assertThat(valter.getCalculatedTotalSkillOf(SkillItemId.THROWN)).isEqualTo(1);

        assertThat(valter.getCalcValueOf(InventoryGroup.WEAPON, CalcAttributeId.BASE_HIT)).isEqualTo(35);
        assertThat(valter.getCalcValueOf(InventoryGroup.WEAPON, CalcAttributeId.DAMAGE)).isEqualTo(8);
        assertThat(valter.getCalcValueOf(InventoryGroup.SHIELD, CalcAttributeId.DEFENSE)).isZero();
        assertThat(valter.getTotalCalcOf(CalcAttributeId.PROTECTION)).isEqualTo(1);
    }

}
