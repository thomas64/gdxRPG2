package nl.t64.game.rpg.components.party;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;


class EliasTest extends DataProvider {

    @Test
    void whenHeroesAreCreated_EliasShouldHaveRightStats() {
        final HeroItem elias = heroes.getHero("elias");
        assertThat(party.containsExactlyEqualTo(elias)).isFalse();

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

        assertThat(elias.getSkillById(SkillItemId.ALCHEMIST).rank).isEqualTo(0);
        assertThat(elias.getCalculatedTotalSkillOf(SkillItemId.ALCHEMIST)).isEqualTo(0);
        assertThat(elias.getSkillById(SkillItemId.DIPLOMAT).rank).isEqualTo(8);
        assertThat(elias.getCalculatedTotalSkillOf(SkillItemId.DIPLOMAT)).isEqualTo(8);
        assertThat(elias.getSkillById(SkillItemId.HEALER).rank).isEqualTo(0);
        assertThat(elias.getCalculatedTotalSkillOf(SkillItemId.HEALER)).isEqualTo(0);
        assertThat(elias.getSkillById(SkillItemId.LOREMASTER).rank).isEqualTo(0);
        assertThat(elias.getCalculatedTotalSkillOf(SkillItemId.LOREMASTER)).isEqualTo(0);
        assertThat(elias.getSkillById(SkillItemId.MECHANIC).rank).isEqualTo(0);
        assertThat(elias.getCalculatedTotalSkillOf(SkillItemId.MECHANIC)).isEqualTo(0);
        assertThat(elias.getSkillById(SkillItemId.MERCHANT).rank).isEqualTo(0);
        assertThat(elias.getCalculatedTotalSkillOf(SkillItemId.MERCHANT)).isEqualTo(0);
        assertThat(elias.getSkillById(SkillItemId.RANGER).rank).isEqualTo(0);
        assertThat(elias.getCalculatedTotalSkillOf(SkillItemId.RANGER)).isEqualTo(0);
        assertThat(elias.getSkillById(SkillItemId.STEALTH).rank).isEqualTo(0);
        assertThat(elias.getCalculatedTotalSkillOf(SkillItemId.STEALTH)).isEqualTo(0);
        assertThat(elias.getSkillById(SkillItemId.THIEF).rank).isEqualTo(0);
        assertThat(elias.getCalculatedTotalSkillOf(SkillItemId.THIEF)).isEqualTo(0);
        assertThat(elias.getSkillById(SkillItemId.TROUBADOUR).rank).isEqualTo(0);
        assertThat(elias.getCalculatedTotalSkillOf(SkillItemId.TROUBADOUR)).isEqualTo(0);
        assertThat(elias.getSkillById(SkillItemId.WARRIOR).rank).isEqualTo(7);
        assertThat(elias.getCalculatedTotalSkillOf(SkillItemId.WARRIOR)).isEqualTo(7);
        assertThat(elias.getSkillById(SkillItemId.WIZARD).rank).isEqualTo(7);
        assertThat(elias.getCalculatedTotalSkillOf(SkillItemId.WIZARD)).isEqualTo(7);

        assertThat(elias.getSkillById(SkillItemId.HAFTED).rank).isEqualTo(5);
        assertThat(elias.getCalculatedTotalSkillOf(SkillItemId.HAFTED)).isEqualTo(5);
        assertThat(elias.getSkillById(SkillItemId.MISSILE).rank).isEqualTo(-1);
        assertThat(elias.getCalculatedTotalSkillOf(SkillItemId.MISSILE)).isEqualTo(0);
        assertThat(elias.getSkillById(SkillItemId.POLE).rank).isEqualTo(5);
        assertThat(elias.getCalculatedTotalSkillOf(SkillItemId.POLE)).isEqualTo(5);
        assertThat(elias.getSkillById(SkillItemId.SHIELD).rank).isEqualTo(-1);
        assertThat(elias.getCalculatedTotalSkillOf(SkillItemId.SHIELD)).isEqualTo(0);
        assertThat(elias.getSkillById(SkillItemId.SWORD).rank).isEqualTo(7);
        assertThat(elias.getCalculatedTotalSkillOf(SkillItemId.SWORD)).isEqualTo(7);
        assertThat(elias.getSkillById(SkillItemId.THROWN).rank).isEqualTo(-1);
        assertThat(elias.getCalculatedTotalSkillOf(SkillItemId.THROWN)).isEqualTo(0);

        assertThat(elias.getCalcValueOf(InventoryGroup.WEAPON, CalcAttributeId.BASE_HIT)).isEqualTo(50);
        assertThat(elias.getCalcValueOf(InventoryGroup.WEAPON, CalcAttributeId.DAMAGE)).isEqualTo(17);
        assertThat(elias.getCalcValueOf(InventoryGroup.SHIELD, CalcAttributeId.DEFENSE)).isEqualTo(0);
        assertThat(elias.getTotalCalcOf(CalcAttributeId.PROTECTION)).isEqualTo(6);
    }

}
