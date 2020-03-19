package nl.t64.game.rpg.components.party;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;


public class ValterTest extends DataProvider {

    @Test
    void whenHeroesAreCreated_ValterShouldHaveRightStats() {
        final HeroItem valter = heroes.getHero("valter");
        assertThat(party.contains(valter)).isFalse();

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
        assertThat(valter.getSkillById(SkillItemId.DIPLOMAT).rank).isEqualTo(0);
        assertThat(valter.getCalculatedTotalSkillOf(SkillItemId.DIPLOMAT)).isEqualTo(0);
        assertThat(valter.getSkillById(SkillItemId.HEALER).rank).isEqualTo(0);
        assertThat(valter.getCalculatedTotalSkillOf(SkillItemId.HEALER)).isEqualTo(0);
        assertThat(valter.getSkillById(SkillItemId.LOREMASTER).rank).isEqualTo(2);
        assertThat(valter.getCalculatedTotalSkillOf(SkillItemId.LOREMASTER)).isEqualTo(2);
        assertThat(valter.getSkillById(SkillItemId.MECHANIC).rank).isEqualTo(2);
        assertThat(valter.getCalculatedTotalSkillOf(SkillItemId.MECHANIC)).isEqualTo(2);
        assertThat(valter.getSkillById(SkillItemId.MERCHANT).rank).isEqualTo(0);
        assertThat(valter.getCalculatedTotalSkillOf(SkillItemId.MERCHANT)).isEqualTo(0);
        assertThat(valter.getSkillById(SkillItemId.RANGER).rank).isEqualTo(0);
        assertThat(valter.getCalculatedTotalSkillOf(SkillItemId.RANGER)).isEqualTo(0);
        assertThat(valter.getSkillById(SkillItemId.STEALTH).rank).isEqualTo(0);
        assertThat(valter.getCalculatedTotalSkillOf(SkillItemId.STEALTH)).isEqualTo(0);
        assertThat(valter.getSkillById(SkillItemId.THIEF).rank).isEqualTo(0);
        assertThat(valter.getCalculatedTotalSkillOf(SkillItemId.THIEF)).isEqualTo(0);
        assertThat(valter.getSkillById(SkillItemId.TROUBADOUR).rank).isEqualTo(0);
        assertThat(valter.getCalculatedTotalSkillOf(SkillItemId.TROUBADOUR)).isEqualTo(0);
        assertThat(valter.getSkillById(SkillItemId.WARRIOR).rank).isEqualTo(0);
        assertThat(valter.getCalculatedTotalSkillOf(SkillItemId.WARRIOR)).isEqualTo(0);
        assertThat(valter.getSkillById(SkillItemId.WIZARD).rank).isEqualTo(2);
        assertThat(valter.getCalculatedTotalSkillOf(SkillItemId.WIZARD)).isEqualTo(2);

        assertThat(valter.getSkillById(SkillItemId.HAFTED).rank).isEqualTo(-1);
        assertThat(valter.getCalculatedTotalSkillOf(SkillItemId.HAFTED)).isEqualTo(0);
        assertThat(valter.getSkillById(SkillItemId.MISSILE).rank).isEqualTo(-1);
        assertThat(valter.getCalculatedTotalSkillOf(SkillItemId.MISSILE)).isEqualTo(0);
        assertThat(valter.getSkillById(SkillItemId.POLE).rank).isEqualTo(0);
        assertThat(valter.getCalculatedTotalSkillOf(SkillItemId.POLE)).isEqualTo(0);
        assertThat(valter.getSkillById(SkillItemId.SHIELD).rank).isEqualTo(-1);
        assertThat(valter.getCalculatedTotalSkillOf(SkillItemId.SHIELD)).isEqualTo(0);
        assertThat(valter.getSkillById(SkillItemId.SWORD).rank).isEqualTo(1);
        assertThat(valter.getCalculatedTotalSkillOf(SkillItemId.SWORD)).isEqualTo(1);
        assertThat(valter.getSkillById(SkillItemId.THROWN).rank).isEqualTo(1);
        assertThat(valter.getCalculatedTotalSkillOf(SkillItemId.THROWN)).isEqualTo(1);

        assertThat(valter.getCalcValueOf(InventoryGroup.WEAPON, CalcAttributeId.BASE_HIT)).isEqualTo(40);
        assertThat(valter.getCalcValueOf(InventoryGroup.WEAPON, CalcAttributeId.DAMAGE)).isEqualTo(11);
        assertThat(valter.getCalcValueOf(InventoryGroup.SHIELD, CalcAttributeId.DEFENSE)).isEqualTo(0);
        assertThat(valter.getTotalCalcOf(CalcAttributeId.PROTECTION)).isEqualTo(1);
    }

}
