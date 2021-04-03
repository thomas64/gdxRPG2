package nl.t64.game.rpg.components.party;

import nl.t64.game.rpg.GameTest;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;


class DuilioTest extends GameTest {

    @Test
    void whenHeroesAreCreated_DuilioShouldHaveRightStats() {
        final HeroItem duilio = new HeroContainer().getHero("duilio");

        assertThat(duilio.school).isEqualTo(SchoolType.ELEMENTAL);
        assertThat(duilio.getLevel()).isEqualTo(22);
        assertThat(duilio.getTotalXp()).isEqualTo(18975);
        assertThat(duilio.getXpNeededForNextLevel()).isEqualTo(2645);
        assertThat(duilio.getCurrentHp()).isEqualTo(122);
        assertThat(duilio.getMaximumHp()).isEqualTo(122);

        assertThat(duilio.getStatById(StatItemId.INTELLIGENCE).rank).isEqualTo(25);
        assertThat(duilio.getCalculatedTotalStatOf(StatItemId.INTELLIGENCE)).isEqualTo(25);
        assertThat(duilio.getStatById(StatItemId.WILLPOWER).rank).isEqualTo(25);
        assertThat(duilio.getCalculatedTotalStatOf(StatItemId.WILLPOWER)).isEqualTo(25);
        assertThat(duilio.getStatById(StatItemId.DEXTERITY).rank).isEqualTo(30);
        assertThat(duilio.getCalculatedTotalStatOf(StatItemId.DEXTERITY)).isEqualTo(30);
        assertThat(duilio.getStatById(StatItemId.AGILITY).rank).isEqualTo(20);
        assertThat(duilio.getCalculatedTotalStatOf(StatItemId.AGILITY)).isEqualTo(19);
        assertThat(duilio.getStatById(StatItemId.ENDURANCE).rank).isEqualTo(25);
        assertThat(duilio.getCalculatedTotalStatOf(StatItemId.ENDURANCE)).isEqualTo(25);
        assertThat(duilio.getStatById(StatItemId.STRENGTH).rank).isEqualTo(25);
        assertThat(duilio.getCalculatedTotalStatOf(StatItemId.STRENGTH)).isEqualTo(25);
        assertThat(duilio.getStatById(StatItemId.STAMINA).rank).isEqualTo(75);
        assertThat(duilio.getCalculatedTotalStatOf(StatItemId.STAMINA)).isEqualTo(75);

        assertThat(duilio.getSkillById(SkillItemId.ALCHEMIST).rank).isEqualTo(5);
        assertThat(duilio.getCalculatedTotalSkillOf(SkillItemId.ALCHEMIST)).isEqualTo(5);
        assertThat(duilio.getSkillById(SkillItemId.DIPLOMAT).rank).isEqualTo(10);
        assertThat(duilio.getCalculatedTotalSkillOf(SkillItemId.DIPLOMAT)).isEqualTo(10);
        assertThat(duilio.getSkillById(SkillItemId.HEALER).rank).isZero();
        assertThat(duilio.getCalculatedTotalSkillOf(SkillItemId.HEALER)).isZero();
        assertThat(duilio.getSkillById(SkillItemId.LOREMASTER).rank).isEqualTo(5);
        assertThat(duilio.getCalculatedTotalSkillOf(SkillItemId.LOREMASTER)).isEqualTo(5);
        assertThat(duilio.getSkillById(SkillItemId.MECHANIC).rank).isZero();
        assertThat(duilio.getCalculatedTotalSkillOf(SkillItemId.MECHANIC)).isZero();
        assertThat(duilio.getSkillById(SkillItemId.MERCHANT).rank).isEqualTo(5);
        assertThat(duilio.getCalculatedTotalSkillOf(SkillItemId.MERCHANT)).isEqualTo(5);
        assertThat(duilio.getSkillById(SkillItemId.RANGER).rank).isEqualTo(5);
        assertThat(duilio.getCalculatedTotalSkillOf(SkillItemId.RANGER)).isEqualTo(5);
        assertThat(duilio.getSkillById(SkillItemId.STEALTH).rank).isEqualTo(5);
        assertThat(duilio.getCalculatedTotalSkillOf(SkillItemId.STEALTH)).isZero();
        assertThat(duilio.getSkillById(SkillItemId.THIEF).rank).isEqualTo(5);
        assertThat(duilio.getCalculatedTotalSkillOf(SkillItemId.THIEF)).isEqualTo(5);
        assertThat(duilio.getSkillById(SkillItemId.TROUBADOUR).rank).isEqualTo(10);
        assertThat(duilio.getCalculatedTotalSkillOf(SkillItemId.TROUBADOUR)).isEqualTo(10);
        assertThat(duilio.getSkillById(SkillItemId.WARRIOR).rank).isEqualTo(10);
        assertThat(duilio.getCalculatedTotalSkillOf(SkillItemId.WARRIOR)).isEqualTo(10);
        assertThat(duilio.getSkillById(SkillItemId.WIZARD).rank).isEqualTo(10);
        assertThat(duilio.getCalculatedTotalSkillOf(SkillItemId.WIZARD)).isEqualTo(10);

        assertThat(duilio.getSkillById(SkillItemId.HAFTED).rank).isEqualTo(10);
        assertThat(duilio.getCalculatedTotalSkillOf(SkillItemId.HAFTED)).isEqualTo(10);
        assertThat(duilio.getSkillById(SkillItemId.MISSILE).rank).isEqualTo(-1);
        assertThat(duilio.getCalculatedTotalSkillOf(SkillItemId.MISSILE)).isZero();
        assertThat(duilio.getSkillById(SkillItemId.POLE).rank).isEqualTo(10);
        assertThat(duilio.getCalculatedTotalSkillOf(SkillItemId.POLE)).isEqualTo(10);
        assertThat(duilio.getSkillById(SkillItemId.SHIELD).rank).isEqualTo(10);
        assertThat(duilio.getCalculatedTotalSkillOf(SkillItemId.SHIELD)).isEqualTo(10);
        assertThat(duilio.getSkillById(SkillItemId.SWORD).rank).isEqualTo(10);
        assertThat(duilio.getCalculatedTotalSkillOf(SkillItemId.SWORD)).isEqualTo(10);
        assertThat(duilio.getSkillById(SkillItemId.THROWN).rank).isEqualTo(10);
        assertThat(duilio.getCalculatedTotalSkillOf(SkillItemId.THROWN)).isEqualTo(10);

        assertThat(duilio.getCalcValueOf(InventoryGroup.WEAPON, CalcAttributeId.BASE_HIT)).isEqualTo(60);
        assertThat(duilio.getCalcValueOf(InventoryGroup.WEAPON, CalcAttributeId.DAMAGE)).isEqualTo(17);
        assertThat(duilio.getCalcValueOf(InventoryGroup.SHIELD, CalcAttributeId.DEFENSE)).isEqualTo(15);
        assertThat(duilio.getTotalCalcOf(CalcAttributeId.PROTECTION)).isEqualTo(10);
    }

}
