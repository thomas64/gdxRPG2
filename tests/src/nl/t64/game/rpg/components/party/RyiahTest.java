package nl.t64.game.rpg.components.party;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;


class RyiahTest extends DataProvider {

    @Test
    void whenHeroesAreCreated_RyiahShouldHaveRightStats() {
        final HeroItem ryiah = heroes.getHero("ryiah");
        assertThat(party.containsExactlyEqualTo(ryiah)).isFalse();

        assertThat(ryiah.school).isEqualTo(SchoolType.NAMING);
        assertThat(ryiah.getLevel()).isEqualTo(3);
        assertThat(ryiah.getTotalXp()).isEqualTo(70);
        assertThat(ryiah.getXpNeededForNextLevel()).isEqualTo(80);
        assertThat(ryiah.getCurrentHp()).isEqualTo(50);
        assertThat(ryiah.getMaximumHp()).isEqualTo(50);

        assertThat(ryiah.getStatById(StatItemId.INTELLIGENCE).rank).isEqualTo(22);
        assertThat(ryiah.getCalculatedTotalStatOf(StatItemId.INTELLIGENCE)).isEqualTo(22);
        assertThat(ryiah.getStatById(StatItemId.WILLPOWER).rank).isEqualTo(16);
        assertThat(ryiah.getCalculatedTotalStatOf(StatItemId.WILLPOWER)).isEqualTo(16);
        assertThat(ryiah.getStatById(StatItemId.DEXTERITY).rank).isEqualTo(20);
        assertThat(ryiah.getCalculatedTotalStatOf(StatItemId.DEXTERITY)).isEqualTo(20);
        assertThat(ryiah.getStatById(StatItemId.AGILITY).rank).isEqualTo(15);
        assertThat(ryiah.getCalculatedTotalStatOf(StatItemId.AGILITY)).isEqualTo(15);
        assertThat(ryiah.getStatById(StatItemId.ENDURANCE).rank).isEqualTo(16);
        assertThat(ryiah.getCalculatedTotalStatOf(StatItemId.ENDURANCE)).isEqualTo(16);
        assertThat(ryiah.getStatById(StatItemId.STRENGTH).rank).isEqualTo(10);
        assertThat(ryiah.getCalculatedTotalStatOf(StatItemId.STRENGTH)).isEqualTo(10);
        assertThat(ryiah.getStatById(StatItemId.STAMINA).rank).isEqualTo(31);
        assertThat(ryiah.getCalculatedTotalStatOf(StatItemId.STAMINA)).isEqualTo(31);

        assertThat(ryiah.getSkillById(SkillItemId.ALCHEMIST).rank).isEqualTo(0);
        assertThat(ryiah.getCalculatedTotalSkillOf(SkillItemId.ALCHEMIST)).isEqualTo(0);
        assertThat(ryiah.getSkillById(SkillItemId.DIPLOMAT).rank).isEqualTo(0);
        assertThat(ryiah.getCalculatedTotalSkillOf(SkillItemId.DIPLOMAT)).isEqualTo(0);
        assertThat(ryiah.getSkillById(SkillItemId.HEALER).rank).isEqualTo(0);
        assertThat(ryiah.getCalculatedTotalSkillOf(SkillItemId.HEALER)).isEqualTo(0);
        assertThat(ryiah.getSkillById(SkillItemId.LOREMASTER).rank).isEqualTo(1);
        assertThat(ryiah.getCalculatedTotalSkillOf(SkillItemId.LOREMASTER)).isEqualTo(1);
        assertThat(ryiah.getSkillById(SkillItemId.MECHANIC).rank).isEqualTo(0);
        assertThat(ryiah.getCalculatedTotalSkillOf(SkillItemId.MECHANIC)).isEqualTo(0);
        assertThat(ryiah.getSkillById(SkillItemId.MERCHANT).rank).isEqualTo(1);
        assertThat(ryiah.getCalculatedTotalSkillOf(SkillItemId.MERCHANT)).isEqualTo(1);
        assertThat(ryiah.getSkillById(SkillItemId.RANGER).rank).isEqualTo(0);
        assertThat(ryiah.getCalculatedTotalSkillOf(SkillItemId.RANGER)).isEqualTo(0);
        assertThat(ryiah.getSkillById(SkillItemId.STEALTH).rank).isEqualTo(0);
        assertThat(ryiah.getCalculatedTotalSkillOf(SkillItemId.STEALTH)).isEqualTo(0);
        assertThat(ryiah.getSkillById(SkillItemId.THIEF).rank).isEqualTo(0);
        assertThat(ryiah.getCalculatedTotalSkillOf(SkillItemId.THIEF)).isEqualTo(0);
        assertThat(ryiah.getSkillById(SkillItemId.TROUBADOUR).rank).isEqualTo(0);
        assertThat(ryiah.getCalculatedTotalSkillOf(SkillItemId.TROUBADOUR)).isEqualTo(0);
        assertThat(ryiah.getSkillById(SkillItemId.WARRIOR).rank).isEqualTo(0);
        assertThat(ryiah.getCalculatedTotalSkillOf(SkillItemId.WARRIOR)).isEqualTo(0);
        assertThat(ryiah.getSkillById(SkillItemId.WIZARD).rank).isEqualTo(4);
        assertThat(ryiah.getCalculatedTotalSkillOf(SkillItemId.WIZARD)).isEqualTo(4);

        assertThat(ryiah.getSkillById(SkillItemId.HAFTED).rank).isEqualTo(0);
        assertThat(ryiah.getCalculatedTotalSkillOf(SkillItemId.HAFTED)).isEqualTo(0);
        assertThat(ryiah.getSkillById(SkillItemId.MISSILE).rank).isEqualTo(-1);
        assertThat(ryiah.getCalculatedTotalSkillOf(SkillItemId.MISSILE)).isEqualTo(0);
        assertThat(ryiah.getSkillById(SkillItemId.POLE).rank).isEqualTo(3);
        assertThat(ryiah.getCalculatedTotalSkillOf(SkillItemId.POLE)).isEqualTo(3);
        assertThat(ryiah.getSkillById(SkillItemId.SHIELD).rank).isEqualTo(0);
        assertThat(ryiah.getCalculatedTotalSkillOf(SkillItemId.SHIELD)).isEqualTo(0);
        assertThat(ryiah.getSkillById(SkillItemId.SWORD).rank).isEqualTo(3);
        assertThat(ryiah.getCalculatedTotalSkillOf(SkillItemId.SWORD)).isEqualTo(3);
        assertThat(ryiah.getSkillById(SkillItemId.THROWN).rank).isEqualTo(-1);
        assertThat(ryiah.getCalculatedTotalSkillOf(SkillItemId.THROWN)).isEqualTo(0);

        assertThat(ryiah.getCalcValueOf(InventoryGroup.WEAPON, CalcAttributeId.BASE_HIT)).isEqualTo(50);
        assertThat(ryiah.getCalcValueOf(InventoryGroup.WEAPON, CalcAttributeId.DAMAGE)).isEqualTo(4);
        assertThat(ryiah.getCalcValueOf(InventoryGroup.SHIELD, CalcAttributeId.DEFENSE)).isEqualTo(0);
        assertThat(ryiah.getTotalCalcOf(CalcAttributeId.PROTECTION)).isEqualTo(1);
    }

}
