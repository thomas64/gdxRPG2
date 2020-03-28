package nl.t64.game.rpg.components.party;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;


public class IellwenTest extends DataProvider {

    @Test
    void whenHeroesAreCreated_IellwenShouldHaveRightStats() {
        final HeroItem iellwen = heroes.getHero("iellwen");
        assertThat(party.containsExactlyEqualTo(iellwen)).isFalse();

        assertThat(iellwen.school).isEqualTo(SchoolType.STAR);
        assertThat(iellwen.getLevel()).isEqualTo(20);
        assertThat(iellwen.getTotalXp()).isEqualTo(14350);
        assertThat(iellwen.getXpNeededForNextLevel()).isEqualTo(2205);
        assertThat(iellwen.getCurrentHp()).isEqualTo(110);
        assertThat(iellwen.getMaximumHp()).isEqualTo(110);

        assertThat(iellwen.getStatById(StatItemId.INTELLIGENCE).rank).isEqualTo(35);
        assertThat(iellwen.getCalculatedTotalStatOf(StatItemId.INTELLIGENCE)).isEqualTo(35);
        assertThat(iellwen.getStatById(StatItemId.WILLPOWER).rank).isEqualTo(25);
        assertThat(iellwen.getCalculatedTotalStatOf(StatItemId.WILLPOWER)).isEqualTo(25);
        assertThat(iellwen.getStatById(StatItemId.DEXTERITY).rank).isEqualTo(30);
        assertThat(iellwen.getCalculatedTotalStatOf(StatItemId.DEXTERITY)).isEqualTo(30);
        assertThat(iellwen.getStatById(StatItemId.AGILITY).rank).isEqualTo(25);
        assertThat(iellwen.getCalculatedTotalStatOf(StatItemId.AGILITY)).isEqualTo(25);
        assertThat(iellwen.getStatById(StatItemId.ENDURANCE).rank).isEqualTo(30);
        assertThat(iellwen.getCalculatedTotalStatOf(StatItemId.ENDURANCE)).isEqualTo(30);
        assertThat(iellwen.getStatById(StatItemId.STRENGTH).rank).isEqualTo(20);
        assertThat(iellwen.getCalculatedTotalStatOf(StatItemId.STRENGTH)).isEqualTo(20);
        assertThat(iellwen.getStatById(StatItemId.STAMINA).rank).isEqualTo(60);
        assertThat(iellwen.getCalculatedTotalStatOf(StatItemId.STAMINA)).isEqualTo(60);

        assertThat(iellwen.getSkillById(SkillItemId.ALCHEMIST).rank).isEqualTo(0);
        assertThat(iellwen.getCalculatedTotalSkillOf(SkillItemId.ALCHEMIST)).isEqualTo(0);
        assertThat(iellwen.getSkillById(SkillItemId.DIPLOMAT).rank).isEqualTo(0);
        assertThat(iellwen.getCalculatedTotalSkillOf(SkillItemId.DIPLOMAT)).isEqualTo(0);
        assertThat(iellwen.getSkillById(SkillItemId.HEALER).rank).isEqualTo(10);
        assertThat(iellwen.getCalculatedTotalSkillOf(SkillItemId.HEALER)).isEqualTo(10);
        assertThat(iellwen.getSkillById(SkillItemId.LOREMASTER).rank).isEqualTo(0);
        assertThat(iellwen.getCalculatedTotalSkillOf(SkillItemId.LOREMASTER)).isEqualTo(0);
        assertThat(iellwen.getSkillById(SkillItemId.MECHANIC).rank).isEqualTo(0);
        assertThat(iellwen.getCalculatedTotalSkillOf(SkillItemId.MECHANIC)).isEqualTo(0);
        assertThat(iellwen.getSkillById(SkillItemId.MERCHANT).rank).isEqualTo(0);
        assertThat(iellwen.getCalculatedTotalSkillOf(SkillItemId.MERCHANT)).isEqualTo(0);
        assertThat(iellwen.getSkillById(SkillItemId.RANGER).rank).isEqualTo(6);
        assertThat(iellwen.getCalculatedTotalSkillOf(SkillItemId.RANGER)).isEqualTo(6);
        assertThat(iellwen.getSkillById(SkillItemId.STEALTH).rank).isEqualTo(6);
        assertThat(iellwen.getCalculatedTotalSkillOf(SkillItemId.STEALTH)).isEqualTo(5);
        assertThat(iellwen.getSkillById(SkillItemId.THIEF).rank).isEqualTo(0);
        assertThat(iellwen.getCalculatedTotalSkillOf(SkillItemId.THIEF)).isEqualTo(0);
        assertThat(iellwen.getSkillById(SkillItemId.TROUBADOUR).rank).isEqualTo(0);
        assertThat(iellwen.getCalculatedTotalSkillOf(SkillItemId.TROUBADOUR)).isEqualTo(0);
        assertThat(iellwen.getSkillById(SkillItemId.WARRIOR).rank).isEqualTo(10);
        assertThat(iellwen.getCalculatedTotalSkillOf(SkillItemId.WARRIOR)).isEqualTo(10);
        assertThat(iellwen.getSkillById(SkillItemId.WIZARD).rank).isEqualTo(8);
        assertThat(iellwen.getCalculatedTotalSkillOf(SkillItemId.WIZARD)).isEqualTo(8);

        assertThat(iellwen.getSkillById(SkillItemId.HAFTED).rank).isEqualTo(5);
        assertThat(iellwen.getCalculatedTotalSkillOf(SkillItemId.HAFTED)).isEqualTo(5);
        assertThat(iellwen.getSkillById(SkillItemId.MISSILE).rank).isEqualTo(7);
        assertThat(iellwen.getCalculatedTotalSkillOf(SkillItemId.MISSILE)).isEqualTo(7);
        assertThat(iellwen.getSkillById(SkillItemId.POLE).rank).isEqualTo(0);
        assertThat(iellwen.getCalculatedTotalSkillOf(SkillItemId.POLE)).isEqualTo(0);
        assertThat(iellwen.getSkillById(SkillItemId.SHIELD).rank).isEqualTo(-1);
        assertThat(iellwen.getCalculatedTotalSkillOf(SkillItemId.SHIELD)).isEqualTo(0);
        assertThat(iellwen.getSkillById(SkillItemId.SWORD).rank).isEqualTo(10);
        assertThat(iellwen.getCalculatedTotalSkillOf(SkillItemId.SWORD)).isEqualTo(10);
        assertThat(iellwen.getSkillById(SkillItemId.THROWN).rank).isEqualTo(-1);
        assertThat(iellwen.getCalculatedTotalSkillOf(SkillItemId.THROWN)).isEqualTo(0);

        assertThat(iellwen.getCalcValueOf(InventoryGroup.WEAPON, CalcAttributeId.BASE_HIT)).isEqualTo(60);
        assertThat(iellwen.getCalcValueOf(InventoryGroup.WEAPON, CalcAttributeId.DAMAGE)).isEqualTo(17);
        assertThat(iellwen.getCalcValueOf(InventoryGroup.SHIELD, CalcAttributeId.DEFENSE)).isEqualTo(0);
        assertThat(iellwen.getTotalCalcOf(CalcAttributeId.PROTECTION)).isEqualTo(7);
    }

}
