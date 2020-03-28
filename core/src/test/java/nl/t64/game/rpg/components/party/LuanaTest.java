package nl.t64.game.rpg.components.party;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;


public class LuanaTest extends DataProvider {

    @Test
    void whenHeroesAreCreated_LuanaShouldHaveRightStats() {
        final HeroItem luana = heroes.getHero("luana");
        assertThat(party.containsExactlyEqualTo(luana)).isFalse();

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

        assertThat(luana.getSkillById(SkillItemId.ALCHEMIST).rank).isEqualTo(0);
        assertThat(luana.getCalculatedTotalSkillOf(SkillItemId.ALCHEMIST)).isEqualTo(0);
        assertThat(luana.getSkillById(SkillItemId.DIPLOMAT).rank).isEqualTo(0);
        assertThat(luana.getCalculatedTotalSkillOf(SkillItemId.DIPLOMAT)).isEqualTo(0);
        assertThat(luana.getSkillById(SkillItemId.HEALER).rank).isEqualTo(0);
        assertThat(luana.getCalculatedTotalSkillOf(SkillItemId.HEALER)).isEqualTo(0);
        assertThat(luana.getSkillById(SkillItemId.LOREMASTER).rank).isEqualTo(0);
        assertThat(luana.getCalculatedTotalSkillOf(SkillItemId.LOREMASTER)).isEqualTo(0);
        assertThat(luana.getSkillById(SkillItemId.MECHANIC).rank).isEqualTo(1);
        assertThat(luana.getCalculatedTotalSkillOf(SkillItemId.MECHANIC)).isEqualTo(1);
        assertThat(luana.getSkillById(SkillItemId.MERCHANT).rank).isEqualTo(0);
        assertThat(luana.getCalculatedTotalSkillOf(SkillItemId.MERCHANT)).isEqualTo(0);
        assertThat(luana.getSkillById(SkillItemId.RANGER).rank).isEqualTo(0);
        assertThat(luana.getCalculatedTotalSkillOf(SkillItemId.RANGER)).isEqualTo(0);
        assertThat(luana.getSkillById(SkillItemId.STEALTH).rank).isEqualTo(3);
        assertThat(luana.getCalculatedTotalSkillOf(SkillItemId.STEALTH)).isEqualTo(3);
        assertThat(luana.getSkillById(SkillItemId.THIEF).rank).isEqualTo(3);
        assertThat(luana.getCalculatedTotalSkillOf(SkillItemId.THIEF)).isEqualTo(3);
        assertThat(luana.getSkillById(SkillItemId.TROUBADOUR).rank).isEqualTo(0);
        assertThat(luana.getCalculatedTotalSkillOf(SkillItemId.TROUBADOUR)).isEqualTo(0);
        assertThat(luana.getSkillById(SkillItemId.WARRIOR).rank).isEqualTo(0);
        assertThat(luana.getCalculatedTotalSkillOf(SkillItemId.WARRIOR)).isEqualTo(0);
        assertThat(luana.getSkillById(SkillItemId.WIZARD).rank).isEqualTo(0);
        assertThat(luana.getCalculatedTotalSkillOf(SkillItemId.WIZARD)).isEqualTo(0);

        assertThat(luana.getSkillById(SkillItemId.HAFTED).rank).isEqualTo(-1);
        assertThat(luana.getCalculatedTotalSkillOf(SkillItemId.HAFTED)).isEqualTo(0);
        assertThat(luana.getSkillById(SkillItemId.MISSILE).rank).isEqualTo(-1);
        assertThat(luana.getCalculatedTotalSkillOf(SkillItemId.MISSILE)).isEqualTo(0);
        assertThat(luana.getSkillById(SkillItemId.POLE).rank).isEqualTo(0);
        assertThat(luana.getCalculatedTotalSkillOf(SkillItemId.POLE)).isEqualTo(0);
        assertThat(luana.getSkillById(SkillItemId.SHIELD).rank).isEqualTo(-1);
        assertThat(luana.getCalculatedTotalSkillOf(SkillItemId.SHIELD)).isEqualTo(0);
        assertThat(luana.getSkillById(SkillItemId.SWORD).rank).isEqualTo(1);
        assertThat(luana.getCalculatedTotalSkillOf(SkillItemId.SWORD)).isEqualTo(1);
        assertThat(luana.getSkillById(SkillItemId.THROWN).rank).isEqualTo(2);
        assertThat(luana.getCalculatedTotalSkillOf(SkillItemId.THROWN)).isEqualTo(2);

        assertThat(luana.getCalcValueOf(InventoryGroup.WEAPON, CalcAttributeId.BASE_HIT)).isEqualTo(40);
        assertThat(luana.getCalcValueOf(InventoryGroup.WEAPON, CalcAttributeId.DAMAGE)).isEqualTo(11);
        assertThat(luana.getCalcValueOf(InventoryGroup.SHIELD, CalcAttributeId.DEFENSE)).isEqualTo(0);
        assertThat(luana.getTotalCalcOf(CalcAttributeId.PROTECTION)).isEqualTo(1);
    }

}
