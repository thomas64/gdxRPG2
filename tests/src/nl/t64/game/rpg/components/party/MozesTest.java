package nl.t64.game.rpg.components.party;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;


class MozesTest extends DataProvider {

    @Test
    void whenHeroesAreCreated_MozesShouldHaveRightStats() {
        final HeroItem mozes = party.getHero("mozes");
        assertThat(party.containsExactlyEqualTo(mozes)).isTrue();

        assertThat(mozes.school).isEqualTo(SchoolType.UNKNOWN);
        assertThat(mozes.getLevel()).isEqualTo(1);
        assertThat(mozes.getTotalXp()).isEqualTo(5);
        assertThat(mozes.getXpNeededForNextLevel()).isEqualTo(20);
        assertThat(mozes.getCurrentHp()).isEqualTo(46);
        assertThat(mozes.getMaximumHp()).isEqualTo(46);

        assertThat(mozes.getStatById(StatItemId.INTELLIGENCE).rank).isEqualTo(18);
        assertThat(mozes.getCalculatedTotalStatOf(StatItemId.INTELLIGENCE)).isEqualTo(18);
        assertThat(mozes.getStatById(StatItemId.WILLPOWER).rank).isEqualTo(12);
        assertThat(mozes.getCalculatedTotalStatOf(StatItemId.WILLPOWER)).isEqualTo(12);
        assertThat(mozes.getStatById(StatItemId.DEXTERITY).rank).isEqualTo(15);
        assertThat(mozes.getCalculatedTotalStatOf(StatItemId.DEXTERITY)).isEqualTo(13);
        assertThat(mozes.getStatById(StatItemId.AGILITY).rank).isEqualTo(15);
        assertThat(mozes.getCalculatedTotalStatOf(StatItemId.AGILITY)).isEqualTo(15);
        assertThat(mozes.getStatById(StatItemId.ENDURANCE).rank).isEqualTo(15);
        assertThat(mozes.getCalculatedTotalStatOf(StatItemId.ENDURANCE)).isEqualTo(15);
        assertThat(mozes.getStatById(StatItemId.STRENGTH).rank).isEqualTo(15);
        assertThat(mozes.getCalculatedTotalStatOf(StatItemId.STRENGTH)).isEqualTo(15);
        assertThat(mozes.getStatById(StatItemId.STAMINA).rank).isEqualTo(30);
        assertThat(mozes.getCalculatedTotalStatOf(StatItemId.STAMINA)).isEqualTo(30);

        assertThat(mozes.getSkillById(SkillItemId.ALCHEMIST).rank).isZero();
        assertThat(mozes.getCalculatedTotalSkillOf(SkillItemId.ALCHEMIST)).isZero();
        assertThat(mozes.getSkillById(SkillItemId.DIPLOMAT).rank).isZero();
        assertThat(mozes.getCalculatedTotalSkillOf(SkillItemId.DIPLOMAT)).isZero();
        assertThat(mozes.getSkillById(SkillItemId.HEALER).rank).isZero();
        assertThat(mozes.getCalculatedTotalSkillOf(SkillItemId.HEALER)).isZero();
        assertThat(mozes.getSkillById(SkillItemId.LOREMASTER).rank).isZero();
        assertThat(mozes.getCalculatedTotalSkillOf(SkillItemId.LOREMASTER)).isZero();
        assertThat(mozes.getSkillById(SkillItemId.MECHANIC).rank).isZero();
        assertThat(mozes.getCalculatedTotalSkillOf(SkillItemId.MECHANIC)).isZero();
        assertThat(mozes.getSkillById(SkillItemId.MERCHANT).rank).isZero();
        assertThat(mozes.getCalculatedTotalSkillOf(SkillItemId.MERCHANT)).isZero();
        assertThat(mozes.getSkillById(SkillItemId.RANGER).rank).isZero();
        assertThat(mozes.getCalculatedTotalSkillOf(SkillItemId.RANGER)).isZero();
        assertThat(mozes.getSkillById(SkillItemId.STEALTH).rank).isEqualTo(1);
        assertThat(mozes.getCalculatedTotalSkillOf(SkillItemId.STEALTH)).isZero();
        assertThat(mozes.getSkillById(SkillItemId.THIEF).rank).isZero();
        assertThat(mozes.getCalculatedTotalSkillOf(SkillItemId.THIEF)).isZero();
        assertThat(mozes.getSkillById(SkillItemId.TROUBADOUR).rank).isEqualTo(1);
        assertThat(mozes.getCalculatedTotalSkillOf(SkillItemId.TROUBADOUR)).isEqualTo(1);
        assertThat(mozes.getSkillById(SkillItemId.WARRIOR).rank).isEqualTo(3);
        assertThat(mozes.getCalculatedTotalSkillOf(SkillItemId.WARRIOR)).isEqualTo(3);
        assertThat(mozes.getSkillById(SkillItemId.WIZARD).rank).isEqualTo(1);
        assertThat(mozes.getCalculatedTotalSkillOf(SkillItemId.WIZARD)).isEqualTo(1);

        assertThat(mozes.getSkillById(SkillItemId.HAFTED).rank).isEqualTo(1);
        assertThat(mozes.getCalculatedTotalSkillOf(SkillItemId.HAFTED)).isEqualTo(1);
        assertThat(mozes.getSkillById(SkillItemId.MISSILE).rank).isEqualTo(3);
        assertThat(mozes.getCalculatedTotalSkillOf(SkillItemId.MISSILE)).isEqualTo(3);
        assertThat(mozes.getSkillById(SkillItemId.POLE).rank).isZero();
        assertThat(mozes.getCalculatedTotalSkillOf(SkillItemId.POLE)).isZero();
        assertThat(mozes.getSkillById(SkillItemId.SHIELD).rank).isEqualTo(3);
        assertThat(mozes.getCalculatedTotalSkillOf(SkillItemId.SHIELD)).isEqualTo(3);
        assertThat(mozes.getSkillById(SkillItemId.SWORD).rank).isEqualTo(3);
        assertThat(mozes.getCalculatedTotalSkillOf(SkillItemId.SWORD)).isEqualTo(3);
        assertThat(mozes.getSkillById(SkillItemId.THROWN).rank).isZero();
        assertThat(mozes.getCalculatedTotalSkillOf(SkillItemId.THROWN)).isZero();

        assertThat(mozes.getCalcValueOf(InventoryGroup.WEAPON, CalcAttributeId.BASE_HIT)).isEqualTo(40);
        assertThat(mozes.getCalcValueOf(InventoryGroup.WEAPON, CalcAttributeId.DAMAGE)).isEqualTo(13);
        assertThat(mozes.getCalcValueOf(InventoryGroup.SHIELD, CalcAttributeId.DEFENSE)).isEqualTo(5);
        assertThat(mozes.getTotalCalcOf(CalcAttributeId.PROTECTION)).isEqualTo(6);
    }

}
