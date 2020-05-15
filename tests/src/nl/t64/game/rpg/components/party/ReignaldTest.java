package nl.t64.game.rpg.components.party;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;


class ReignaldTest extends DataProvider {

    @Test
    void whenHeroesAreCreated_ReignaldShouldHaveRightStats() {
        final HeroItem reignald = heroes.getHero("reignald");
        assertThat(party.containsExactlyEqualTo(reignald)).isFalse();

        assertThat(reignald.school).isEqualTo(SchoolType.NONE);
        assertThat(reignald.getLevel()).isEqualTo(8);
        assertThat(reignald.getTotalXp()).isEqualTo(1020);
        assertThat(reignald.getXpNeededForNextLevel()).isEqualTo(405);
        assertThat(reignald.getCurrentHp()).isEqualTo(68);
        assertThat(reignald.getMaximumHp()).isEqualTo(68);

        assertThat(reignald.getStatById(StatItemId.INTELLIGENCE).rank).isEqualTo(10);
        assertThat(reignald.getCalculatedTotalStatOf(StatItemId.INTELLIGENCE)).isEqualTo(10);
        assertThat(reignald.getStatById(StatItemId.WILLPOWER).rank).isEqualTo(8);
        assertThat(reignald.getCalculatedTotalStatOf(StatItemId.WILLPOWER)).isEqualTo(8);
        assertThat(reignald.getStatById(StatItemId.DEXTERITY).rank).isEqualTo(25);
        assertThat(reignald.getCalculatedTotalStatOf(StatItemId.DEXTERITY)).isEqualTo(23);
        assertThat(reignald.getStatById(StatItemId.AGILITY).rank).isEqualTo(10);
        assertThat(reignald.getCalculatedTotalStatOf(StatItemId.AGILITY)).isEqualTo(10);
        assertThat(reignald.getStatById(StatItemId.ENDURANCE).rank).isEqualTo(20);
        assertThat(reignald.getCalculatedTotalStatOf(StatItemId.ENDURANCE)).isEqualTo(20);
        assertThat(reignald.getStatById(StatItemId.STRENGTH).rank).isEqualTo(20);
        assertThat(reignald.getCalculatedTotalStatOf(StatItemId.STRENGTH)).isEqualTo(20);
        assertThat(reignald.getStatById(StatItemId.STAMINA).rank).isEqualTo(40);
        assertThat(reignald.getCalculatedTotalStatOf(StatItemId.STAMINA)).isEqualTo(40);

        assertThat(reignald.getSkillById(SkillItemId.ALCHEMIST).rank).isEqualTo(-1);
        assertThat(reignald.getCalculatedTotalSkillOf(SkillItemId.ALCHEMIST)).isEqualTo(0);
        assertThat(reignald.getSkillById(SkillItemId.DIPLOMAT).rank).isEqualTo(0);
        assertThat(reignald.getCalculatedTotalSkillOf(SkillItemId.DIPLOMAT)).isEqualTo(0);
        assertThat(reignald.getSkillById(SkillItemId.HEALER).rank).isEqualTo(0);
        assertThat(reignald.getCalculatedTotalSkillOf(SkillItemId.HEALER)).isEqualTo(0);
        assertThat(reignald.getSkillById(SkillItemId.LOREMASTER).rank).isEqualTo(0);
        assertThat(reignald.getCalculatedTotalSkillOf(SkillItemId.LOREMASTER)).isEqualTo(0);
        assertThat(reignald.getSkillById(SkillItemId.MECHANIC).rank).isEqualTo(0);
        assertThat(reignald.getCalculatedTotalSkillOf(SkillItemId.MECHANIC)).isEqualTo(0);
        assertThat(reignald.getSkillById(SkillItemId.MERCHANT).rank).isEqualTo(0);
        assertThat(reignald.getCalculatedTotalSkillOf(SkillItemId.MERCHANT)).isEqualTo(0);
        assertThat(reignald.getSkillById(SkillItemId.RANGER).rank).isEqualTo(0);
        assertThat(reignald.getCalculatedTotalSkillOf(SkillItemId.RANGER)).isEqualTo(0);
        assertThat(reignald.getSkillById(SkillItemId.STEALTH).rank).isEqualTo(1);
        assertThat(reignald.getCalculatedTotalSkillOf(SkillItemId.STEALTH)).isEqualTo(0);
        assertThat(reignald.getSkillById(SkillItemId.THIEF).rank).isEqualTo(-1);
        assertThat(reignald.getCalculatedTotalSkillOf(SkillItemId.THIEF)).isEqualTo(0);
        assertThat(reignald.getSkillById(SkillItemId.TROUBADOUR).rank).isEqualTo(0);
        assertThat(reignald.getCalculatedTotalSkillOf(SkillItemId.TROUBADOUR)).isEqualTo(0);
        assertThat(reignald.getSkillById(SkillItemId.WARRIOR).rank).isEqualTo(4);
        assertThat(reignald.getCalculatedTotalSkillOf(SkillItemId.WARRIOR)).isEqualTo(4);
        assertThat(reignald.getSkillById(SkillItemId.WIZARD).rank).isEqualTo(-1);
        assertThat(reignald.getCalculatedTotalSkillOf(SkillItemId.WIZARD)).isEqualTo(0);

        assertThat(reignald.getSkillById(SkillItemId.HAFTED).rank).isEqualTo(0);
        assertThat(reignald.getCalculatedTotalSkillOf(SkillItemId.HAFTED)).isEqualTo(0);
        assertThat(reignald.getSkillById(SkillItemId.MISSILE).rank).isEqualTo(-1);
        assertThat(reignald.getCalculatedTotalSkillOf(SkillItemId.MISSILE)).isEqualTo(0);
        assertThat(reignald.getSkillById(SkillItemId.POLE).rank).isEqualTo(0);
        assertThat(reignald.getCalculatedTotalSkillOf(SkillItemId.POLE)).isEqualTo(0);
        assertThat(reignald.getSkillById(SkillItemId.SHIELD).rank).isEqualTo(2);
        assertThat(reignald.getCalculatedTotalSkillOf(SkillItemId.SHIELD)).isEqualTo(2);
        assertThat(reignald.getSkillById(SkillItemId.SWORD).rank).isEqualTo(4);
        assertThat(reignald.getCalculatedTotalSkillOf(SkillItemId.SWORD)).isEqualTo(4);
        assertThat(reignald.getSkillById(SkillItemId.THROWN).rank).isEqualTo(2);
        assertThat(reignald.getCalculatedTotalSkillOf(SkillItemId.THROWN)).isEqualTo(2);

        assertThat(reignald.getCalcValueOf(InventoryGroup.WEAPON, CalcAttributeId.BASE_HIT)).isEqualTo(50);
        assertThat(reignald.getCalcValueOf(InventoryGroup.WEAPON, CalcAttributeId.DAMAGE)).isEqualTo(15);
        assertThat(reignald.getCalcValueOf(InventoryGroup.SHIELD, CalcAttributeId.DEFENSE)).isEqualTo(10);
        assertThat(reignald.getTotalCalcOf(CalcAttributeId.PROTECTION)).isEqualTo(8);
    }

}
