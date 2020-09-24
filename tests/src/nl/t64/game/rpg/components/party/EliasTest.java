package nl.t64.game.rpg.components.party;

import nl.t64.game.rpg.GameTest;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;


class EliasTest extends GameTest {

    @Test
    void whenHeroesAreCreated_EliasShouldHaveRightStats() {
        final HeroItem elias = new HeroContainer().getHero("elias");

        assertThat(elias.school).isEqualTo(SchoolType.NAMING);
        assertThat(elias.getLevel()).isEqualTo(18);
        assertThat(elias.getTotalXp()).isEqualTo(10545);
        assertThat(elias.getXpNeededForNextLevel()).isEqualTo(1805);
        assertThat(elias.getCurrentHp()).isEqualTo(108);
        assertThat(elias.getMaximumHp()).isEqualTo(108);

        assertThat(elias.getStatById(StatItemId.INTELLIGENCE).rank).isEqualTo(30);
        assertThat(elias.getCalculatedTotalStatOf(StatItemId.INTELLIGENCE)).isEqualTo(30);
        assertThat(elias.getStatById(StatItemId.WILLPOWER).rank).isEqualTo(30);
        assertThat(elias.getCalculatedTotalStatOf(StatItemId.WILLPOWER)).isEqualTo(30);
        assertThat(elias.getStatById(StatItemId.DEXTERITY).rank).isEqualTo(25);
        assertThat(elias.getCalculatedTotalStatOf(StatItemId.DEXTERITY)).isEqualTo(25);
        assertThat(elias.getStatById(StatItemId.AGILITY).rank).isEqualTo(18);
        assertThat(elias.getCalculatedTotalStatOf(StatItemId.AGILITY)).isEqualTo(18);
        assertThat(elias.getStatById(StatItemId.ENDURANCE).rank).isEqualTo(30);
        assertThat(elias.getCalculatedTotalStatOf(StatItemId.ENDURANCE)).isEqualTo(30);
        assertThat(elias.getStatById(StatItemId.STRENGTH).rank).isEqualTo(20);
        assertThat(elias.getCalculatedTotalStatOf(StatItemId.STRENGTH)).isEqualTo(20);
        assertThat(elias.getStatById(StatItemId.STAMINA).rank).isEqualTo(60);
        assertThat(elias.getCalculatedTotalStatOf(StatItemId.STAMINA)).isEqualTo(60);

        assertThat(elias.getSkillById(SkillItemId.ALCHEMIST).rank).isZero();
        assertThat(elias.getCalculatedTotalSkillOf(SkillItemId.ALCHEMIST)).isZero();
        assertThat(elias.getSkillById(SkillItemId.DIPLOMAT).rank).isEqualTo(8);
        assertThat(elias.getCalculatedTotalSkillOf(SkillItemId.DIPLOMAT)).isEqualTo(8);
        assertThat(elias.getSkillById(SkillItemId.HEALER).rank).isZero();
        assertThat(elias.getCalculatedTotalSkillOf(SkillItemId.HEALER)).isZero();
        assertThat(elias.getSkillById(SkillItemId.LOREMASTER).rank).isZero();
        assertThat(elias.getCalculatedTotalSkillOf(SkillItemId.LOREMASTER)).isZero();
        assertThat(elias.getSkillById(SkillItemId.MECHANIC).rank).isZero();
        assertThat(elias.getCalculatedTotalSkillOf(SkillItemId.MECHANIC)).isZero();
        assertThat(elias.getSkillById(SkillItemId.MERCHANT).rank).isZero();
        assertThat(elias.getCalculatedTotalSkillOf(SkillItemId.MERCHANT)).isZero();
        assertThat(elias.getSkillById(SkillItemId.RANGER).rank).isZero();
        assertThat(elias.getCalculatedTotalSkillOf(SkillItemId.RANGER)).isZero();
        assertThat(elias.getSkillById(SkillItemId.STEALTH).rank).isZero();
        assertThat(elias.getCalculatedTotalSkillOf(SkillItemId.STEALTH)).isZero();
        assertThat(elias.getSkillById(SkillItemId.THIEF).rank).isZero();
        assertThat(elias.getCalculatedTotalSkillOf(SkillItemId.THIEF)).isZero();
        assertThat(elias.getSkillById(SkillItemId.TROUBADOUR).rank).isZero();
        assertThat(elias.getCalculatedTotalSkillOf(SkillItemId.TROUBADOUR)).isZero();
        assertThat(elias.getSkillById(SkillItemId.WARRIOR).rank).isEqualTo(7);
        assertThat(elias.getCalculatedTotalSkillOf(SkillItemId.WARRIOR)).isEqualTo(7);
        assertThat(elias.getSkillById(SkillItemId.WIZARD).rank).isEqualTo(7);
        assertThat(elias.getCalculatedTotalSkillOf(SkillItemId.WIZARD)).isEqualTo(7);

        assertThat(elias.getSkillById(SkillItemId.HAFTED).rank).isEqualTo(5);
        assertThat(elias.getCalculatedTotalSkillOf(SkillItemId.HAFTED)).isEqualTo(5);
        assertThat(elias.getSkillById(SkillItemId.MISSILE).rank).isEqualTo(-1);
        assertThat(elias.getCalculatedTotalSkillOf(SkillItemId.MISSILE)).isZero();
        assertThat(elias.getSkillById(SkillItemId.POLE).rank).isEqualTo(5);
        assertThat(elias.getCalculatedTotalSkillOf(SkillItemId.POLE)).isEqualTo(5);
        assertThat(elias.getSkillById(SkillItemId.SHIELD).rank).isEqualTo(-1);
        assertThat(elias.getCalculatedTotalSkillOf(SkillItemId.SHIELD)).isZero();
        assertThat(elias.getSkillById(SkillItemId.SWORD).rank).isEqualTo(7);
        assertThat(elias.getCalculatedTotalSkillOf(SkillItemId.SWORD)).isEqualTo(7);
        assertThat(elias.getSkillById(SkillItemId.THROWN).rank).isEqualTo(-1);
        assertThat(elias.getCalculatedTotalSkillOf(SkillItemId.THROWN)).isZero();

        assertThat(elias.getCalcValueOf(InventoryGroup.WEAPON, CalcAttributeId.BASE_HIT)).isEqualTo(50);
        assertThat(elias.getCalcValueOf(InventoryGroup.WEAPON, CalcAttributeId.DAMAGE)).isEqualTo(17);
        assertThat(elias.getCalcValueOf(InventoryGroup.SHIELD, CalcAttributeId.DEFENSE)).isZero();
        assertThat(elias.getTotalCalcOf(CalcAttributeId.PROTECTION)).isEqualTo(6);
    }

}
