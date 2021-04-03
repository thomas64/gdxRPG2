package nl.t64.game.rpg.components.party;

import nl.t64.game.rpg.GameTest;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;


class OnarrTest extends GameTest {

    @Test
    void whenHeroesAreCreated_OnarrShouldHaveRightStats() {
        final HeroItem onarr = new HeroContainer().getHero("onarr");

        assertThat(onarr.school).isEqualTo(SchoolType.NONE);
        assertThat(onarr.getLevel()).isEqualTo(18);
        assertThat(onarr.getTotalXp()).isEqualTo(10545);
        assertThat(onarr.getXpNeededForNextLevel()).isEqualTo(1805);
        assertThat(onarr.getCurrentHp()).isEqualTo(108);
        assertThat(onarr.getMaximumHp()).isEqualTo(108);

        assertThat(onarr.getStatById(StatItemId.INTELLIGENCE).rank).isEqualTo(30);
        assertThat(onarr.getCalculatedTotalStatOf(StatItemId.INTELLIGENCE)).isEqualTo(30);
        assertThat(onarr.getStatById(StatItemId.WILLPOWER).rank).isEqualTo(25);
        assertThat(onarr.getCalculatedTotalStatOf(StatItemId.WILLPOWER)).isEqualTo(25);
        assertThat(onarr.getStatById(StatItemId.DEXTERITY).rank).isEqualTo(23);
        assertThat(onarr.getCalculatedTotalStatOf(StatItemId.DEXTERITY)).isEqualTo(23);
        assertThat(onarr.getStatById(StatItemId.AGILITY).rank).isEqualTo(15);
        assertThat(onarr.getCalculatedTotalStatOf(StatItemId.AGILITY)).isEqualTo(11);
        assertThat(onarr.getStatById(StatItemId.ENDURANCE).rank).isEqualTo(30);
        assertThat(onarr.getCalculatedTotalStatOf(StatItemId.ENDURANCE)).isEqualTo(30);
        assertThat(onarr.getStatById(StatItemId.STRENGTH).rank).isEqualTo(25);
        assertThat(onarr.getCalculatedTotalStatOf(StatItemId.STRENGTH)).isEqualTo(25);
        assertThat(onarr.getStatById(StatItemId.STAMINA).rank).isEqualTo(60);
        assertThat(onarr.getCalculatedTotalStatOf(StatItemId.STAMINA)).isEqualTo(60);

        assertThat(onarr.getSkillById(SkillItemId.ALCHEMIST).rank).isEqualTo(-1);
        assertThat(onarr.getCalculatedTotalSkillOf(SkillItemId.ALCHEMIST)).isZero();
        assertThat(onarr.getSkillById(SkillItemId.DIPLOMAT).rank).isZero();
        assertThat(onarr.getCalculatedTotalSkillOf(SkillItemId.DIPLOMAT)).isZero();
        assertThat(onarr.getSkillById(SkillItemId.HEALER).rank).isEqualTo(4);
        assertThat(onarr.getCalculatedTotalSkillOf(SkillItemId.HEALER)).isEqualTo(4);
        assertThat(onarr.getSkillById(SkillItemId.LOREMASTER).rank).isEqualTo(6);
        assertThat(onarr.getCalculatedTotalSkillOf(SkillItemId.LOREMASTER)).isEqualTo(6);
        assertThat(onarr.getSkillById(SkillItemId.MECHANIC).rank).isZero();
        assertThat(onarr.getCalculatedTotalSkillOf(SkillItemId.MECHANIC)).isZero();
        assertThat(onarr.getSkillById(SkillItemId.MERCHANT).rank).isZero();
        assertThat(onarr.getCalculatedTotalSkillOf(SkillItemId.MERCHANT)).isZero();
        assertThat(onarr.getSkillById(SkillItemId.RANGER).rank).isZero();
        assertThat(onarr.getCalculatedTotalSkillOf(SkillItemId.RANGER)).isZero();
        assertThat(onarr.getSkillById(SkillItemId.STEALTH).rank).isZero();
        assertThat(onarr.getCalculatedTotalSkillOf(SkillItemId.STEALTH)).isZero();
        assertThat(onarr.getSkillById(SkillItemId.THIEF).rank).isZero();
        assertThat(onarr.getCalculatedTotalSkillOf(SkillItemId.THIEF)).isZero();
        assertThat(onarr.getSkillById(SkillItemId.TROUBADOUR).rank).isEqualTo(7);
        assertThat(onarr.getCalculatedTotalSkillOf(SkillItemId.TROUBADOUR)).isEqualTo(7);
        assertThat(onarr.getSkillById(SkillItemId.WARRIOR).rank).isEqualTo(9);
        assertThat(onarr.getCalculatedTotalSkillOf(SkillItemId.WARRIOR)).isEqualTo(9);
        assertThat(onarr.getSkillById(SkillItemId.WIZARD).rank).isEqualTo(-1);
        assertThat(onarr.getCalculatedTotalSkillOf(SkillItemId.WIZARD)).isZero();

        assertThat(onarr.getSkillById(SkillItemId.HAFTED).rank).isEqualTo(8);
        assertThat(onarr.getCalculatedTotalSkillOf(SkillItemId.HAFTED)).isEqualTo(8);
        assertThat(onarr.getSkillById(SkillItemId.MISSILE).rank).isEqualTo(-1);
        assertThat(onarr.getCalculatedTotalSkillOf(SkillItemId.MISSILE)).isZero();
        assertThat(onarr.getSkillById(SkillItemId.POLE).rank).isEqualTo(8);
        assertThat(onarr.getCalculatedTotalSkillOf(SkillItemId.POLE)).isEqualTo(8);
        assertThat(onarr.getSkillById(SkillItemId.SHIELD).rank).isEqualTo(9);
        assertThat(onarr.getCalculatedTotalSkillOf(SkillItemId.SHIELD)).isEqualTo(9);
        assertThat(onarr.getSkillById(SkillItemId.SWORD).rank).isEqualTo(5);
        assertThat(onarr.getCalculatedTotalSkillOf(SkillItemId.SWORD)).isEqualTo(5);
        assertThat(onarr.getSkillById(SkillItemId.THROWN).rank).isEqualTo(8);
        assertThat(onarr.getCalculatedTotalSkillOf(SkillItemId.THROWN)).isEqualTo(8);

        assertThat(onarr.getCalcValueOf(InventoryGroup.WEAPON, CalcAttributeId.BASE_HIT)).isEqualTo(40);
        assertThat(onarr.getCalcValueOf(InventoryGroup.WEAPON, CalcAttributeId.DAMAGE)).isEqualTo(24);
        assertThat(onarr.getCalcValueOf(InventoryGroup.SHIELD, CalcAttributeId.DEFENSE)).isEqualTo(15);
        assertThat(onarr.getTotalCalcOf(CalcAttributeId.PROTECTION)).isEqualTo(16);
    }

}
