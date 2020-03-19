package nl.t64.game.rpg.components.party;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;


public class GalenTest extends DataProvider {

    @Test
    void whenHeroesAreCreated_GalenShouldHaveRightStats() {
        final HeroItem galen = heroes.getHero("galen");
        assertThat(party.contains(galen)).isFalse();

        assertThat(galen.school).isEqualTo(SchoolType.NONE);
        assertThat(galen.getLevel()).isEqualTo(4);
        assertThat(galen.getTotalXp()).isEqualTo(150);
        assertThat(galen.getXpNeededForNextLevel()).isEqualTo(125);
        assertThat(galen.getCurrentHp()).isEqualTo(64);
        assertThat(galen.getMaximumHp()).isEqualTo(64);

        assertThat(galen.getStatById(StatItemId.INTELLIGENCE).rank).isEqualTo(15);
        assertThat(galen.getCalculatedTotalStatOf(StatItemId.INTELLIGENCE)).isEqualTo(15);
        assertThat(galen.getStatById(StatItemId.WILLPOWER).rank).isEqualTo(15);
        assertThat(galen.getCalculatedTotalStatOf(StatItemId.WILLPOWER)).isEqualTo(15);
        assertThat(galen.getStatById(StatItemId.DEXTERITY).rank).isEqualTo(18);
        assertThat(galen.getCalculatedTotalStatOf(StatItemId.DEXTERITY)).isEqualTo(12);
        assertThat(galen.getStatById(StatItemId.AGILITY).rank).isEqualTo(10);
        assertThat(galen.getCalculatedTotalStatOf(StatItemId.AGILITY)).isEqualTo(10);
        assertThat(galen.getStatById(StatItemId.ENDURANCE).rank).isEqualTo(20);
        assertThat(galen.getCalculatedTotalStatOf(StatItemId.ENDURANCE)).isEqualTo(20);
        assertThat(galen.getStatById(StatItemId.STRENGTH).rank).isEqualTo(25);
        assertThat(galen.getCalculatedTotalStatOf(StatItemId.STRENGTH)).isEqualTo(25);
        assertThat(galen.getStatById(StatItemId.STAMINA).rank).isEqualTo(40);
        assertThat(galen.getCalculatedTotalStatOf(StatItemId.STAMINA)).isEqualTo(40);

        assertThat(galen.getSkillById(SkillItemId.ALCHEMIST).rank).isEqualTo(-1);
        assertThat(galen.getCalculatedTotalSkillOf(SkillItemId.ALCHEMIST)).isEqualTo(0);
        assertThat(galen.getSkillById(SkillItemId.DIPLOMAT).rank).isEqualTo(0);
        assertThat(galen.getCalculatedTotalSkillOf(SkillItemId.DIPLOMAT)).isEqualTo(0);
        assertThat(galen.getSkillById(SkillItemId.HEALER).rank).isEqualTo(0);
        assertThat(galen.getCalculatedTotalSkillOf(SkillItemId.HEALER)).isEqualTo(0);
        assertThat(galen.getSkillById(SkillItemId.LOREMASTER).rank).isEqualTo(0);
        assertThat(galen.getCalculatedTotalSkillOf(SkillItemId.LOREMASTER)).isEqualTo(0);
        assertThat(galen.getSkillById(SkillItemId.MECHANIC).rank).isEqualTo(0);
        assertThat(galen.getCalculatedTotalSkillOf(SkillItemId.MECHANIC)).isEqualTo(0);
        assertThat(galen.getSkillById(SkillItemId.MERCHANT).rank).isEqualTo(0);
        assertThat(galen.getCalculatedTotalSkillOf(SkillItemId.MERCHANT)).isEqualTo(0);
        assertThat(galen.getSkillById(SkillItemId.RANGER).rank).isEqualTo(4);
        assertThat(galen.getCalculatedTotalSkillOf(SkillItemId.RANGER)).isEqualTo(4);
        assertThat(galen.getSkillById(SkillItemId.STEALTH).rank).isEqualTo(3);
        assertThat(galen.getCalculatedTotalSkillOf(SkillItemId.STEALTH)).isEqualTo(0);
        assertThat(galen.getSkillById(SkillItemId.THIEF).rank).isEqualTo(0);
        assertThat(galen.getCalculatedTotalSkillOf(SkillItemId.THIEF)).isEqualTo(0);
        assertThat(galen.getSkillById(SkillItemId.TROUBADOUR).rank).isEqualTo(0);
        assertThat(galen.getCalculatedTotalSkillOf(SkillItemId.TROUBADOUR)).isEqualTo(0);
        assertThat(galen.getSkillById(SkillItemId.WARRIOR).rank).isEqualTo(5);
        assertThat(galen.getCalculatedTotalSkillOf(SkillItemId.WARRIOR)).isEqualTo(5);
        assertThat(galen.getSkillById(SkillItemId.WIZARD).rank).isEqualTo(-1);
        assertThat(galen.getCalculatedTotalSkillOf(SkillItemId.WIZARD)).isEqualTo(0);

        assertThat(galen.getSkillById(SkillItemId.HAFTED).rank).isEqualTo(5);
        assertThat(galen.getCalculatedTotalSkillOf(SkillItemId.HAFTED)).isEqualTo(5);
        assertThat(galen.getSkillById(SkillItemId.MISSILE).rank).isEqualTo(3);
        assertThat(galen.getCalculatedTotalSkillOf(SkillItemId.MISSILE)).isEqualTo(3);
        assertThat(galen.getSkillById(SkillItemId.POLE).rank).isEqualTo(0);
        assertThat(galen.getCalculatedTotalSkillOf(SkillItemId.POLE)).isEqualTo(0);
        assertThat(galen.getSkillById(SkillItemId.SHIELD).rank).isEqualTo(3);
        assertThat(galen.getCalculatedTotalSkillOf(SkillItemId.SHIELD)).isEqualTo(3);
        assertThat(galen.getSkillById(SkillItemId.SWORD).rank).isEqualTo(-1);
        assertThat(galen.getCalculatedTotalSkillOf(SkillItemId.SWORD)).isEqualTo(0);
        assertThat(galen.getSkillById(SkillItemId.THROWN).rank).isEqualTo(-1);
        assertThat(galen.getCalculatedTotalSkillOf(SkillItemId.THROWN)).isEqualTo(0);

        assertThat(galen.getCalcValueOf(InventoryGroup.WEAPON, CalcAttributeId.BASE_HIT)).isEqualTo(30);
        assertThat(galen.getCalcValueOf(InventoryGroup.WEAPON, CalcAttributeId.DAMAGE)).isEqualTo(20);
        assertThat(galen.getCalcValueOf(InventoryGroup.SHIELD, CalcAttributeId.DEFENSE)).isEqualTo(10);
        assertThat(galen.getTotalCalcOf(CalcAttributeId.PROTECTION)).isEqualTo(10);
    }

}
