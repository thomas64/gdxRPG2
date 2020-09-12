package nl.t64.game.rpg.components.party;

import nl.t64.game.rpg.GameTest;
import nl.t64.game.rpg.constants.*;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;


class RyiahTest extends GameTest {

    @Test
    void whenHeroesAreCreated_RyiahShouldHaveRightStats() {
        final HeroItem ryiah = new HeroContainer().getHero("ryiah");

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

        assertThat(ryiah.getSkillById(SkillItemId.ALCHEMIST).rank).isZero();
        assertThat(ryiah.getCalculatedTotalSkillOf(SkillItemId.ALCHEMIST)).isZero();
        assertThat(ryiah.getSkillById(SkillItemId.DIPLOMAT).rank).isZero();
        assertThat(ryiah.getCalculatedTotalSkillOf(SkillItemId.DIPLOMAT)).isZero();
        assertThat(ryiah.getSkillById(SkillItemId.HEALER).rank).isZero();
        assertThat(ryiah.getCalculatedTotalSkillOf(SkillItemId.HEALER)).isZero();
        assertThat(ryiah.getSkillById(SkillItemId.LOREMASTER).rank).isEqualTo(1);
        assertThat(ryiah.getCalculatedTotalSkillOf(SkillItemId.LOREMASTER)).isEqualTo(1);
        assertThat(ryiah.getSkillById(SkillItemId.MECHANIC).rank).isZero();
        assertThat(ryiah.getCalculatedTotalSkillOf(SkillItemId.MECHANIC)).isZero();
        assertThat(ryiah.getSkillById(SkillItemId.MERCHANT).rank).isEqualTo(1);
        assertThat(ryiah.getCalculatedTotalSkillOf(SkillItemId.MERCHANT)).isEqualTo(1);
        assertThat(ryiah.getSkillById(SkillItemId.RANGER).rank).isZero();
        assertThat(ryiah.getCalculatedTotalSkillOf(SkillItemId.RANGER)).isZero();
        assertThat(ryiah.getSkillById(SkillItemId.STEALTH).rank).isZero();
        assertThat(ryiah.getCalculatedTotalSkillOf(SkillItemId.STEALTH)).isZero();
        assertThat(ryiah.getSkillById(SkillItemId.THIEF).rank).isZero();
        assertThat(ryiah.getCalculatedTotalSkillOf(SkillItemId.THIEF)).isZero();
        assertThat(ryiah.getSkillById(SkillItemId.TROUBADOUR).rank).isZero();
        assertThat(ryiah.getCalculatedTotalSkillOf(SkillItemId.TROUBADOUR)).isZero();
        assertThat(ryiah.getSkillById(SkillItemId.WARRIOR).rank).isZero();
        assertThat(ryiah.getCalculatedTotalSkillOf(SkillItemId.WARRIOR)).isZero();
        assertThat(ryiah.getSkillById(SkillItemId.WIZARD).rank).isEqualTo(4);
        assertThat(ryiah.getCalculatedTotalSkillOf(SkillItemId.WIZARD)).isEqualTo(4);

        assertThat(ryiah.getSkillById(SkillItemId.HAFTED).rank).isZero();
        assertThat(ryiah.getCalculatedTotalSkillOf(SkillItemId.HAFTED)).isZero();
        assertThat(ryiah.getSkillById(SkillItemId.MISSILE).rank).isEqualTo(-1);
        assertThat(ryiah.getCalculatedTotalSkillOf(SkillItemId.MISSILE)).isZero();
        assertThat(ryiah.getSkillById(SkillItemId.POLE).rank).isEqualTo(3);
        assertThat(ryiah.getCalculatedTotalSkillOf(SkillItemId.POLE)).isEqualTo(3);
        assertThat(ryiah.getSkillById(SkillItemId.SHIELD).rank).isZero();
        assertThat(ryiah.getCalculatedTotalSkillOf(SkillItemId.SHIELD)).isZero();
        assertThat(ryiah.getSkillById(SkillItemId.SWORD).rank).isEqualTo(3);
        assertThat(ryiah.getCalculatedTotalSkillOf(SkillItemId.SWORD)).isEqualTo(3);
        assertThat(ryiah.getSkillById(SkillItemId.THROWN).rank).isEqualTo(-1);
        assertThat(ryiah.getCalculatedTotalSkillOf(SkillItemId.THROWN)).isZero();

        assertThat(ryiah.getCalcValueOf(InventoryGroup.WEAPON, CalcAttributeId.BASE_HIT)).isEqualTo(50);
        assertThat(ryiah.getCalcValueOf(InventoryGroup.WEAPON, CalcAttributeId.DAMAGE)).isEqualTo(4);
        assertThat(ryiah.getCalcValueOf(InventoryGroup.SHIELD, CalcAttributeId.DEFENSE)).isZero();
        assertThat(ryiah.getTotalCalcOf(CalcAttributeId.PROTECTION)).isEqualTo(1);
    }

}
