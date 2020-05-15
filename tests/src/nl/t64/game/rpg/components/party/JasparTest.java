package nl.t64.game.rpg.components.party;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;


class JasparTest extends DataProvider {

    @Test
    void whenHeroesAreCreated_JasparShouldHaveRightStats() {
        final HeroItem jaspar = heroes.getHero("jaspar");
        assertThat(party.containsExactlyEqualTo(jaspar)).isFalse();

        assertThat(jaspar.school).isEqualTo(SchoolType.NONE);
        assertThat(jaspar.getLevel()).isEqualTo(12);
        assertThat(jaspar.getTotalXp()).isEqualTo(3250);
        assertThat(jaspar.getXpNeededForNextLevel()).isEqualTo(845);
        assertThat(jaspar.getCurrentHp()).isEqualTo(102);
        assertThat(jaspar.getMaximumHp()).isEqualTo(102);

        assertThat(jaspar.getStatById(StatItemId.INTELLIGENCE).rank).isEqualTo(6);
        assertThat(jaspar.getCalculatedTotalStatOf(StatItemId.INTELLIGENCE)).isEqualTo(6);
        assertThat(jaspar.getStatById(StatItemId.WILLPOWER).rank).isEqualTo(11);
        assertThat(jaspar.getCalculatedTotalStatOf(StatItemId.WILLPOWER)).isEqualTo(11);
        assertThat(jaspar.getStatById(StatItemId.DEXTERITY).rank).isEqualTo(14);
        assertThat(jaspar.getCalculatedTotalStatOf(StatItemId.DEXTERITY)).isEqualTo(4);
        assertThat(jaspar.getStatById(StatItemId.AGILITY).rank).isEqualTo(8);
        assertThat(jaspar.getCalculatedTotalStatOf(StatItemId.AGILITY)).isEqualTo(8);
        assertThat(jaspar.getStatById(StatItemId.ENDURANCE).rank).isEqualTo(30);
        assertThat(jaspar.getCalculatedTotalStatOf(StatItemId.ENDURANCE)).isEqualTo(30);
        assertThat(jaspar.getStatById(StatItemId.STRENGTH).rank).isEqualTo(30);
        assertThat(jaspar.getCalculatedTotalStatOf(StatItemId.STRENGTH)).isEqualTo(30);
        assertThat(jaspar.getStatById(StatItemId.STAMINA).rank).isEqualTo(60);
        assertThat(jaspar.getCalculatedTotalStatOf(StatItemId.STAMINA)).isEqualTo(60);

        assertThat(jaspar.getSkillById(SkillItemId.ALCHEMIST).rank).isEqualTo(-1);
        assertThat(jaspar.getCalculatedTotalSkillOf(SkillItemId.ALCHEMIST)).isEqualTo(0);
        assertThat(jaspar.getSkillById(SkillItemId.DIPLOMAT).rank).isEqualTo(0);
        assertThat(jaspar.getCalculatedTotalSkillOf(SkillItemId.DIPLOMAT)).isEqualTo(0);
        assertThat(jaspar.getSkillById(SkillItemId.HEALER).rank).isEqualTo(0);
        assertThat(jaspar.getCalculatedTotalSkillOf(SkillItemId.HEALER)).isEqualTo(0);
        assertThat(jaspar.getSkillById(SkillItemId.LOREMASTER).rank).isEqualTo(-1);
        assertThat(jaspar.getCalculatedTotalSkillOf(SkillItemId.LOREMASTER)).isEqualTo(0);
        assertThat(jaspar.getSkillById(SkillItemId.MECHANIC).rank).isEqualTo(0);
        assertThat(jaspar.getCalculatedTotalSkillOf(SkillItemId.MECHANIC)).isEqualTo(0);
        assertThat(jaspar.getSkillById(SkillItemId.MERCHANT).rank).isEqualTo(0);
        assertThat(jaspar.getCalculatedTotalSkillOf(SkillItemId.MERCHANT)).isEqualTo(0);
        assertThat(jaspar.getSkillById(SkillItemId.RANGER).rank).isEqualTo(0);
        assertThat(jaspar.getCalculatedTotalSkillOf(SkillItemId.RANGER)).isEqualTo(0);
        assertThat(jaspar.getSkillById(SkillItemId.STEALTH).rank).isEqualTo(1);
        assertThat(jaspar.getCalculatedTotalSkillOf(SkillItemId.STEALTH)).isEqualTo(0);
        assertThat(jaspar.getSkillById(SkillItemId.THIEF).rank).isEqualTo(-1);
        assertThat(jaspar.getCalculatedTotalSkillOf(SkillItemId.THIEF)).isEqualTo(0);
        assertThat(jaspar.getSkillById(SkillItemId.TROUBADOUR).rank).isEqualTo(-1);
        assertThat(jaspar.getCalculatedTotalSkillOf(SkillItemId.TROUBADOUR)).isEqualTo(0);
        assertThat(jaspar.getSkillById(SkillItemId.WARRIOR).rank).isEqualTo(6);
        assertThat(jaspar.getCalculatedTotalSkillOf(SkillItemId.WARRIOR)).isEqualTo(6);
        assertThat(jaspar.getSkillById(SkillItemId.WIZARD).rank).isEqualTo(-1);
        assertThat(jaspar.getCalculatedTotalSkillOf(SkillItemId.WIZARD)).isEqualTo(0);

        assertThat(jaspar.getSkillById(SkillItemId.HAFTED).rank).isEqualTo(0);
        assertThat(jaspar.getCalculatedTotalSkillOf(SkillItemId.HAFTED)).isEqualTo(0);
        assertThat(jaspar.getSkillById(SkillItemId.MISSILE).rank).isEqualTo(-1);
        assertThat(jaspar.getCalculatedTotalSkillOf(SkillItemId.MISSILE)).isEqualTo(0);
        assertThat(jaspar.getSkillById(SkillItemId.POLE).rank).isEqualTo(6);
        assertThat(jaspar.getCalculatedTotalSkillOf(SkillItemId.POLE)).isEqualTo(6);
        assertThat(jaspar.getSkillById(SkillItemId.SHIELD).rank).isEqualTo(4);
        assertThat(jaspar.getCalculatedTotalSkillOf(SkillItemId.SHIELD)).isEqualTo(4);
        assertThat(jaspar.getSkillById(SkillItemId.SWORD).rank).isEqualTo(6);
        assertThat(jaspar.getCalculatedTotalSkillOf(SkillItemId.SWORD)).isEqualTo(6);
        assertThat(jaspar.getSkillById(SkillItemId.THROWN).rank).isEqualTo(-1);
        assertThat(jaspar.getCalculatedTotalSkillOf(SkillItemId.THROWN)).isEqualTo(0);

        assertThat(jaspar.getCalcValueOf(InventoryGroup.WEAPON, CalcAttributeId.BASE_HIT)).isEqualTo(40);
        assertThat(jaspar.getCalcValueOf(InventoryGroup.WEAPON, CalcAttributeId.DAMAGE)).isEqualTo(17);
        assertThat(jaspar.getCalcValueOf(InventoryGroup.SHIELD, CalcAttributeId.DEFENSE)).isEqualTo(15);
        assertThat(jaspar.getTotalCalcOf(CalcAttributeId.PROTECTION)).isEqualTo(18);
    }

}
