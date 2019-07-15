package nl.t64.game.rpg.components.party;

import nl.t64.game.rpg.GameData;
import nl.t64.game.rpg.GameTest;
import nl.t64.game.rpg.constants.Constant;
import nl.t64.game.rpg.profile.ProfileManager;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;


class HeroTest extends GameTest {

    private HeroContainer heroes;
    private PartyContainer party;

    @BeforeEach
    private void setup() {
        final var profileManager = new ProfileManager();
        final var gameData = new GameData();
        gameData.onNotifyCreate(profileManager);
        heroes = gameData.getHeroes();
        party = gameData.getParty();
    }

    private void addHeroToParty(String heroId) {
        HeroItem hero = heroes.getHero(heroId);
        heroes.removeHero(heroId);
        party.addHero(hero);
    }

    private void removeHeroFromParty(String heroId) {
        HeroItem hero = party.getHero(heroId);
        party.removeHero(heroId);
        heroes.addHero(hero);
    }

    @Test
    void whenDataIsCreated_ShouldContainPlayer() {
        assertThat(heroes.contains(Constant.PLAYER_ID)).isFalse();
        assertThat(heroes.getSize()).isEqualTo(13);
        assertThat(party.contains(Constant.PLAYER_ID)).isTrue();
        assertThat(party.getSize()).isEqualTo(1);
    }

    @Test
    void whenPlayerIsRemovedFromParty_ShouldThrowException() {
        assertThat(party.contains(Constant.PLAYER_ID)).isTrue();
        assertThat(party.getSize()).isEqualTo(1);

        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> removeHeroFromParty(Constant.PLAYER_ID))
                .withMessage("Cannot remove player from party.");
    }

    @Test
    void whenHeroIsAddedToParty_ShouldBeLastInParty() {
        final HeroItem mozes = party.getHero("mozes");
        assertThat(party.isHeroLast(mozes)).isTrue();

        final String luana = "luana";
        addHeroToParty(luana);
        assertThat(party.isHeroLast(mozes)).isFalse();
        assertThat(party.isHeroLast(party.getHero(luana))).isTrue();
    }

    @Test
    void whenHeroIsAddedToParty_ShouldBeRemovedFromHeroContainer() {
        final String luana = "luana";

        assertThat(heroes.getSize()).isEqualTo(13);
        assertThat(heroes.contains(luana)).isTrue();
        assertThat(party.getSize()).isEqualTo(1);
        assertThat(party.contains(luana)).isFalse();

        addHeroToParty(luana);

        assertThat(heroes.getSize()).isEqualTo(12);
        assertThat(heroes.contains(luana)).isFalse();
        assertThat(party.getSize()).isEqualTo(2);
        assertThat(party.contains(luana)).isTrue();
    }

    @Test
    void whenHeroIsRemovedFromParty_ShouldBeAddedToHeroContainer() {
        final String luana = "luana";

        addHeroToParty(luana);

        assertThat(heroes.getSize()).isEqualTo(12);
        assertThat(heroes.contains(luana)).isFalse();
        assertThat(party.getSize()).isEqualTo(2);
        assertThat(party.contains(luana)).isTrue();

        removeHeroFromParty(luana);

        assertThat(heroes.getSize()).isEqualTo(13);
        assertThat(heroes.contains(luana)).isTrue();
        assertThat(party.getSize()).isEqualTo(1);
        assertThat(party.contains(luana)).isFalse();
    }

    @Test
    void whenPartyIsFull_ShouldThrowException() {
        addHeroToParty("luana");
        addHeroToParty("reignald");
        addHeroToParty("ryiah");
        addHeroToParty("valter");
        addHeroToParty("galen");

        assertThat(heroes.getSize()).isEqualTo(8);
        assertThat(party.getSize()).isEqualTo(6);

        assertThatExceptionOfType(IllegalStateException.class)
                .isThrownBy(() -> addHeroToParty("jaspar"))
                .withMessage("Party is full.");
    }

    @Test
    void whenImpossibleItemIsForceSet_ShouldOverwriteExistingItem() {
        final HeroItem mozes = party.getHero("mozes");
        assertThat(mozes.getInventoryItem(InventoryGroup.WEAPON)).get().hasFieldOrPropertyWithValue("id", "basic_shortsword");

        final InventoryItem newWeapon = InventoryDatabase.getInstance().getInventoryItem("masterwork_lance");
        mozes.forceSetInventoryItem(InventoryGroup.WEAPON, newWeapon);
        assertThat(mozes.getInventoryItem(InventoryGroup.WEAPON)).get().hasFieldOrPropertyWithValue("id", "masterwork_lance");
    }

    @SuppressWarnings("OptionalGetWithoutIsPresent")
    @Test
    void whenImpossibleItemIsChecked_ShouldGetMessage() {
        final HeroItem mozes = party.getHero("mozes");
        final HeroItem ryiah = heroes.getHero("ryiah");
        final InventoryItem legendaryStaff = InventoryDatabase.getInstance().getInventoryItem("legendary_staff");
        final InventoryItem masterworkLance = InventoryDatabase.getInstance().getInventoryItem("masterwork_lance");
        final InventoryItem basicDagger = InventoryDatabase.getInstance().getInventoryItem("basic_dagger");

        Optional<String> message1 = mozes.isAbleToEquip(legendaryStaff);
        assertThat(message1.get()).isEqualToIgnoringNewLines("Mozes needs the Pole skill\nto equip that Legendary Staff.");

        Optional<String> message2 = ryiah.isAbleToEquip(legendaryStaff);
        assertThat(message2.get()).isEqualToIgnoringNewLines("Ryiah needs 30 Intelligence\nto equip that Legendary Staff.");

        Optional<String> message3 = ryiah.isAbleToEquip(masterworkLance);
        assertThat(message3.get()).isEqualToIgnoringNewLines("Ryiah needs 20 Strength\nto equip that Masterwork Lance.");

        Optional<String> message4 = mozes.isAbleToEquip(basicDagger);
        assertThat(message4).isEmpty();
    }

    @Test
    void whenHeroesAreCreated_ShouldHaveRightStats() {
        final HeroItem mozes = party.getHero("mozes");
        final HeroItem luana = heroes.getHero("luana");
        final HeroItem reignald = heroes.getHero("reignald");
        final HeroItem ryiah = heroes.getHero("ryiah");
        final HeroItem valter = heroes.getHero("valter");
        final HeroItem galen = heroes.getHero("galen");
        final HeroItem jaspar = heroes.getHero("jaspar");
        final HeroItem kiara = heroes.getHero("kiara");
        final HeroItem luthais = heroes.getHero("luthais");
        final HeroItem elias = heroes.getHero("elias");
        final HeroItem onarr = heroes.getHero("onarr");
        final HeroItem duilio = heroes.getHero("duilio");
        final HeroItem iellwen = heroes.getHero("iellwen");
        final HeroItem faeron = heroes.getHero("faeron");

        SoftAssertions softly = new SoftAssertions();
        softly.assertThat(party.contains(mozes)).isTrue();

        //////

        softly.assertThat(mozes.school).isEqualTo(SchoolType.UNKNOWN);
        softly.assertThat(mozes.getLevel()).isEqualTo(1);
        softly.assertThat(mozes.getTotalXp()).isEqualTo(5);
        softly.assertThat(mozes.getXpNeededForNextLevel()).isEqualTo(20);
        softly.assertThat(mozes.getCurrentHp()).isEqualTo(46);
        softly.assertThat(mozes.getMaximumHp()).isEqualTo(46);

        softly.assertThat(mozes.getStatRankOf(StatType.INTELLIGENCE)).isEqualTo(18);
        softly.assertThat(mozes.getCalculatedTotalStatOf(StatType.INTELLIGENCE)).isEqualTo(18);
        softly.assertThat(mozes.getStatRankOf(StatType.WILLPOWER)).isEqualTo(12);
        softly.assertThat(mozes.getCalculatedTotalStatOf(StatType.WILLPOWER)).isEqualTo(12);
        softly.assertThat(mozes.getStatRankOf(StatType.DEXTERITY)).isEqualTo(15);
        softly.assertThat(mozes.getCalculatedTotalStatOf(StatType.DEXTERITY)).isEqualTo(13);
        softly.assertThat(mozes.getStatRankOf(StatType.AGILITY)).isEqualTo(15);
        softly.assertThat(mozes.getCalculatedTotalStatOf(StatType.AGILITY)).isEqualTo(15);
        softly.assertThat(mozes.getStatRankOf(StatType.ENDURANCE)).isEqualTo(15);
        softly.assertThat(mozes.getCalculatedTotalStatOf(StatType.ENDURANCE)).isEqualTo(15);
        softly.assertThat(mozes.getStatRankOf(StatType.STRENGTH)).isEqualTo(15);
        softly.assertThat(mozes.getCalculatedTotalStatOf(StatType.STRENGTH)).isEqualTo(15);
        softly.assertThat(mozes.getStatRankOf(StatType.STAMINA)).isEqualTo(30);
        softly.assertThat(mozes.getCalculatedTotalStatOf(StatType.STAMINA)).isEqualTo(30);

        softly.assertThat(mozes.getSkillRankOf(SkillType.ALCHEMIST)).isEqualTo(0);
        softly.assertThat(mozes.getCalculatedTotalSkillOf(SkillType.ALCHEMIST)).isEqualTo(0);
        softly.assertThat(mozes.getSkillRankOf(SkillType.DIPLOMAT)).isEqualTo(0);
        softly.assertThat(mozes.getCalculatedTotalSkillOf(SkillType.DIPLOMAT)).isEqualTo(0);
        softly.assertThat(mozes.getSkillRankOf(SkillType.HEALER)).isEqualTo(0);
        softly.assertThat(mozes.getCalculatedTotalSkillOf(SkillType.HEALER)).isEqualTo(0);
        softly.assertThat(mozes.getSkillRankOf(SkillType.LOREMASTER)).isEqualTo(0);
        softly.assertThat(mozes.getCalculatedTotalSkillOf(SkillType.LOREMASTER)).isEqualTo(0);
        softly.assertThat(mozes.getSkillRankOf(SkillType.MECHANIC)).isEqualTo(0);
        softly.assertThat(mozes.getCalculatedTotalSkillOf(SkillType.MECHANIC)).isEqualTo(0);
        softly.assertThat(mozes.getSkillRankOf(SkillType.MERCHANT)).isEqualTo(0);
        softly.assertThat(mozes.getCalculatedTotalSkillOf(SkillType.MERCHANT)).isEqualTo(0);
        softly.assertThat(mozes.getSkillRankOf(SkillType.RANGER)).isEqualTo(0);
        softly.assertThat(mozes.getCalculatedTotalSkillOf(SkillType.RANGER)).isEqualTo(0);
        softly.assertThat(mozes.getSkillRankOf(SkillType.STEALTH)).isEqualTo(1);
        softly.assertThat(mozes.getCalculatedTotalSkillOf(SkillType.STEALTH)).isEqualTo(0);
        softly.assertThat(mozes.getSkillRankOf(SkillType.THIEF)).isEqualTo(0);
        softly.assertThat(mozes.getCalculatedTotalSkillOf(SkillType.THIEF)).isEqualTo(0);
        softly.assertThat(mozes.getSkillRankOf(SkillType.TROUBADOUR)).isEqualTo(1);
        softly.assertThat(mozes.getCalculatedTotalSkillOf(SkillType.TROUBADOUR)).isEqualTo(1);
        softly.assertThat(mozes.getSkillRankOf(SkillType.WARRIOR)).isEqualTo(3);
        softly.assertThat(mozes.getCalculatedTotalSkillOf(SkillType.WARRIOR)).isEqualTo(3);
        softly.assertThat(mozes.getSkillRankOf(SkillType.WIZARD)).isEqualTo(1);
        softly.assertThat(mozes.getCalculatedTotalSkillOf(SkillType.WIZARD)).isEqualTo(1);

        softly.assertThat(mozes.getSkillRankOf(SkillType.HAFTED)).isEqualTo(1);
        softly.assertThat(mozes.getCalculatedTotalSkillOf(SkillType.HAFTED)).isEqualTo(1);
        softly.assertThat(mozes.getSkillRankOf(SkillType.MISSILE)).isEqualTo(3);
        softly.assertThat(mozes.getCalculatedTotalSkillOf(SkillType.MISSILE)).isEqualTo(3);
        softly.assertThat(mozes.getSkillRankOf(SkillType.POLE)).isEqualTo(0);
        softly.assertThat(mozes.getCalculatedTotalSkillOf(SkillType.POLE)).isEqualTo(0);
        softly.assertThat(mozes.getSkillRankOf(SkillType.SHIELD)).isEqualTo(3);
        softly.assertThat(mozes.getCalculatedTotalSkillOf(SkillType.SHIELD)).isEqualTo(3);
        softly.assertThat(mozes.getSkillRankOf(SkillType.SWORD)).isEqualTo(3);
        softly.assertThat(mozes.getCalculatedTotalSkillOf(SkillType.SWORD)).isEqualTo(3);
        softly.assertThat(mozes.getSkillRankOf(SkillType.THROWN)).isEqualTo(0);
        softly.assertThat(mozes.getCalculatedTotalSkillOf(SkillType.THROWN)).isEqualTo(0);

        softly.assertThat(mozes.getCalcValueOf(InventoryGroup.WEAPON, CalcType.BASE_HIT)).isEqualTo(40);
        softly.assertThat(mozes.getCalcValueOf(InventoryGroup.WEAPON, CalcType.DAMAGE)).isEqualTo(13);
        softly.assertThat(mozes.getCalcValueOf(InventoryGroup.SHIELD, CalcType.DEFENSE)).isEqualTo(5);
        softly.assertThat(mozes.getTotalCalcOf(CalcType.PROTECTION)).isEqualTo(6);

        softly.assertThat(mozes.getInventoryItem(InventoryGroup.WEAPON)).get().hasFieldOrPropertyWithValue("id", "basic_shortsword");
        softly.assertThat(mozes.getInventoryItem(InventoryGroup.SHIELD)).get().hasFieldOrPropertyWithValue("id", "basic_light_shield");
        softly.assertThat(mozes.getInventoryItem(InventoryGroup.HELMET)).isEmpty();
        softly.assertThat(mozes.getInventoryItem(InventoryGroup.CHEST)).get().hasFieldOrPropertyWithValue("id", "basic_medium_chest");

        //////

        softly.assertThat(luana.school).isEqualTo(SchoolType.ELEMENTAL);
        softly.assertThat(luana.getLevel()).isEqualTo(1);
        softly.assertThat(luana.getTotalXp()).isEqualTo(5);
        softly.assertThat(luana.getXpNeededForNextLevel()).isEqualTo(20);
        softly.assertThat(luana.getCurrentHp()).isEqualTo(31);
        softly.assertThat(luana.getMaximumHp()).isEqualTo(31);

        softly.assertThat(luana.getStatRankOf(StatType.INTELLIGENCE)).isEqualTo(14);
        softly.assertThat(luana.getCalculatedTotalStatOf(StatType.INTELLIGENCE)).isEqualTo(14);
        softly.assertThat(luana.getStatRankOf(StatType.WILLPOWER)).isEqualTo(10);
        softly.assertThat(luana.getCalculatedTotalStatOf(StatType.WILLPOWER)).isEqualTo(10);
        softly.assertThat(luana.getStatRankOf(StatType.DEXTERITY)).isEqualTo(22);
        softly.assertThat(luana.getCalculatedTotalStatOf(StatType.DEXTERITY)).isEqualTo(22);
        softly.assertThat(luana.getStatRankOf(StatType.AGILITY)).isEqualTo(20);
        softly.assertThat(luana.getCalculatedTotalStatOf(StatType.AGILITY)).isEqualTo(20);
        softly.assertThat(luana.getStatRankOf(StatType.ENDURANCE)).isEqualTo(10);
        softly.assertThat(luana.getCalculatedTotalStatOf(StatType.ENDURANCE)).isEqualTo(10);
        softly.assertThat(luana.getStatRankOf(StatType.STRENGTH)).isEqualTo(8);
        softly.assertThat(luana.getCalculatedTotalStatOf(StatType.STRENGTH)).isEqualTo(8);
        softly.assertThat(luana.getStatRankOf(StatType.STAMINA)).isEqualTo(20);
        softly.assertThat(luana.getCalculatedTotalStatOf(StatType.STAMINA)).isEqualTo(20);

        softly.assertThat(luana.getSkillRankOf(SkillType.ALCHEMIST)).isEqualTo(0);
        softly.assertThat(luana.getCalculatedTotalSkillOf(SkillType.ALCHEMIST)).isEqualTo(0);
        softly.assertThat(luana.getSkillRankOf(SkillType.DIPLOMAT)).isEqualTo(0);
        softly.assertThat(luana.getCalculatedTotalSkillOf(SkillType.DIPLOMAT)).isEqualTo(0);
        softly.assertThat(luana.getSkillRankOf(SkillType.HEALER)).isEqualTo(0);
        softly.assertThat(luana.getCalculatedTotalSkillOf(SkillType.HEALER)).isEqualTo(0);
        softly.assertThat(luana.getSkillRankOf(SkillType.LOREMASTER)).isEqualTo(0);
        softly.assertThat(luana.getCalculatedTotalSkillOf(SkillType.LOREMASTER)).isEqualTo(0);
        softly.assertThat(luana.getSkillRankOf(SkillType.MECHANIC)).isEqualTo(1);
        softly.assertThat(luana.getCalculatedTotalSkillOf(SkillType.MECHANIC)).isEqualTo(1);
        softly.assertThat(luana.getSkillRankOf(SkillType.MERCHANT)).isEqualTo(0);
        softly.assertThat(luana.getCalculatedTotalSkillOf(SkillType.MERCHANT)).isEqualTo(0);
        softly.assertThat(luana.getSkillRankOf(SkillType.RANGER)).isEqualTo(0);
        softly.assertThat(luana.getCalculatedTotalSkillOf(SkillType.RANGER)).isEqualTo(0);
        softly.assertThat(luana.getSkillRankOf(SkillType.STEALTH)).isEqualTo(3);
        softly.assertThat(luana.getCalculatedTotalSkillOf(SkillType.STEALTH)).isEqualTo(3);
        softly.assertThat(luana.getSkillRankOf(SkillType.THIEF)).isEqualTo(3);
        softly.assertThat(luana.getCalculatedTotalSkillOf(SkillType.THIEF)).isEqualTo(3);
        softly.assertThat(luana.getSkillRankOf(SkillType.TROUBADOUR)).isEqualTo(0);
        softly.assertThat(luana.getCalculatedTotalSkillOf(SkillType.TROUBADOUR)).isEqualTo(0);
        softly.assertThat(luana.getSkillRankOf(SkillType.WARRIOR)).isEqualTo(0);
        softly.assertThat(luana.getCalculatedTotalSkillOf(SkillType.WARRIOR)).isEqualTo(0);
        softly.assertThat(luana.getSkillRankOf(SkillType.WIZARD)).isEqualTo(0);
        softly.assertThat(luana.getCalculatedTotalSkillOf(SkillType.WIZARD)).isEqualTo(0);

        softly.assertThat(luana.getSkillRankOf(SkillType.HAFTED)).isEqualTo(-1);
        softly.assertThat(luana.getCalculatedTotalSkillOf(SkillType.HAFTED)).isEqualTo(0);
        softly.assertThat(luana.getSkillRankOf(SkillType.MISSILE)).isEqualTo(-1);
        softly.assertThat(luana.getCalculatedTotalSkillOf(SkillType.MISSILE)).isEqualTo(0);
        softly.assertThat(luana.getSkillRankOf(SkillType.POLE)).isEqualTo(0);
        softly.assertThat(luana.getCalculatedTotalSkillOf(SkillType.POLE)).isEqualTo(0);
        softly.assertThat(luana.getSkillRankOf(SkillType.SHIELD)).isEqualTo(-1);
        softly.assertThat(luana.getCalculatedTotalSkillOf(SkillType.SHIELD)).isEqualTo(0);
        softly.assertThat(luana.getSkillRankOf(SkillType.SWORD)).isEqualTo(1);
        softly.assertThat(luana.getCalculatedTotalSkillOf(SkillType.SWORD)).isEqualTo(1);
        softly.assertThat(luana.getSkillRankOf(SkillType.THROWN)).isEqualTo(2);
        softly.assertThat(luana.getCalculatedTotalSkillOf(SkillType.THROWN)).isEqualTo(2);

        softly.assertThat(luana.getCalcValueOf(InventoryGroup.WEAPON, CalcType.BASE_HIT)).isEqualTo(40);
        softly.assertThat(luana.getCalcValueOf(InventoryGroup.WEAPON, CalcType.DAMAGE)).isEqualTo(11);
        softly.assertThat(luana.getCalcValueOf(InventoryGroup.SHIELD, CalcType.DEFENSE)).isEqualTo(0);
        softly.assertThat(luana.getTotalCalcOf(CalcType.PROTECTION)).isEqualTo(1);

        softly.assertThat(luana.getInventoryItem(InventoryGroup.WEAPON)).get().hasFieldOrPropertyWithValue("id", "basic_dagger");
        softly.assertThat(luana.getInventoryItem(InventoryGroup.SHIELD)).isEmpty();
        softly.assertThat(luana.getInventoryItem(InventoryGroup.HELMET)).isEmpty();
        softly.assertThat(luana.getInventoryItem(InventoryGroup.CHEST)).get().hasFieldOrPropertyWithValue("id", "basic_light_chest");

        //////

        softly.assertThat(reignald.school).isEqualTo(SchoolType.NONE);
        softly.assertThat(reignald.getLevel()).isEqualTo(8);
        softly.assertThat(reignald.getTotalXp()).isEqualTo(1020);
        softly.assertThat(reignald.getXpNeededForNextLevel()).isEqualTo(405);
        softly.assertThat(reignald.getCurrentHp()).isEqualTo(68);
        softly.assertThat(reignald.getMaximumHp()).isEqualTo(68);

        softly.assertThat(reignald.getStatRankOf(StatType.INTELLIGENCE)).isEqualTo(10);
        softly.assertThat(reignald.getCalculatedTotalStatOf(StatType.INTELLIGENCE)).isEqualTo(10);
        softly.assertThat(reignald.getStatRankOf(StatType.WILLPOWER)).isEqualTo(8);
        softly.assertThat(reignald.getCalculatedTotalStatOf(StatType.WILLPOWER)).isEqualTo(8);
        softly.assertThat(reignald.getStatRankOf(StatType.DEXTERITY)).isEqualTo(25);
        softly.assertThat(reignald.getCalculatedTotalStatOf(StatType.DEXTERITY)).isEqualTo(23);
        softly.assertThat(reignald.getStatRankOf(StatType.AGILITY)).isEqualTo(10);
        softly.assertThat(reignald.getCalculatedTotalStatOf(StatType.AGILITY)).isEqualTo(10);
        softly.assertThat(reignald.getStatRankOf(StatType.ENDURANCE)).isEqualTo(20);
        softly.assertThat(reignald.getCalculatedTotalStatOf(StatType.ENDURANCE)).isEqualTo(20);
        softly.assertThat(reignald.getStatRankOf(StatType.STRENGTH)).isEqualTo(20);
        softly.assertThat(reignald.getCalculatedTotalStatOf(StatType.STRENGTH)).isEqualTo(20);
        softly.assertThat(reignald.getStatRankOf(StatType.STAMINA)).isEqualTo(40);
        softly.assertThat(reignald.getCalculatedTotalStatOf(StatType.STAMINA)).isEqualTo(40);

        softly.assertThat(reignald.getSkillRankOf(SkillType.ALCHEMIST)).isEqualTo(-1);
        softly.assertThat(reignald.getCalculatedTotalSkillOf(SkillType.ALCHEMIST)).isEqualTo(0);
        softly.assertThat(reignald.getSkillRankOf(SkillType.DIPLOMAT)).isEqualTo(0);
        softly.assertThat(reignald.getCalculatedTotalSkillOf(SkillType.DIPLOMAT)).isEqualTo(0);
        softly.assertThat(reignald.getSkillRankOf(SkillType.HEALER)).isEqualTo(0);
        softly.assertThat(reignald.getCalculatedTotalSkillOf(SkillType.HEALER)).isEqualTo(0);
        softly.assertThat(reignald.getSkillRankOf(SkillType.LOREMASTER)).isEqualTo(0);
        softly.assertThat(reignald.getCalculatedTotalSkillOf(SkillType.LOREMASTER)).isEqualTo(0);
        softly.assertThat(reignald.getSkillRankOf(SkillType.MECHANIC)).isEqualTo(0);
        softly.assertThat(reignald.getCalculatedTotalSkillOf(SkillType.MECHANIC)).isEqualTo(0);
        softly.assertThat(reignald.getSkillRankOf(SkillType.MERCHANT)).isEqualTo(0);
        softly.assertThat(reignald.getCalculatedTotalSkillOf(SkillType.MERCHANT)).isEqualTo(0);
        softly.assertThat(reignald.getSkillRankOf(SkillType.RANGER)).isEqualTo(0);
        softly.assertThat(reignald.getCalculatedTotalSkillOf(SkillType.RANGER)).isEqualTo(0);
        softly.assertThat(reignald.getSkillRankOf(SkillType.STEALTH)).isEqualTo(1);
        softly.assertThat(reignald.getCalculatedTotalSkillOf(SkillType.STEALTH)).isEqualTo(0);
        softly.assertThat(reignald.getSkillRankOf(SkillType.THIEF)).isEqualTo(-1);
        softly.assertThat(reignald.getCalculatedTotalSkillOf(SkillType.THIEF)).isEqualTo(0);
        softly.assertThat(reignald.getSkillRankOf(SkillType.TROUBADOUR)).isEqualTo(0);
        softly.assertThat(reignald.getCalculatedTotalSkillOf(SkillType.TROUBADOUR)).isEqualTo(0);
        softly.assertThat(reignald.getSkillRankOf(SkillType.WARRIOR)).isEqualTo(4);
        softly.assertThat(reignald.getCalculatedTotalSkillOf(SkillType.WARRIOR)).isEqualTo(4);
        softly.assertThat(reignald.getSkillRankOf(SkillType.WIZARD)).isEqualTo(-1);
        softly.assertThat(reignald.getCalculatedTotalSkillOf(SkillType.WIZARD)).isEqualTo(0);

        softly.assertThat(reignald.getSkillRankOf(SkillType.HAFTED)).isEqualTo(0);
        softly.assertThat(reignald.getCalculatedTotalSkillOf(SkillType.HAFTED)).isEqualTo(0);
        softly.assertThat(reignald.getSkillRankOf(SkillType.MISSILE)).isEqualTo(-1);
        softly.assertThat(reignald.getCalculatedTotalSkillOf(SkillType.MISSILE)).isEqualTo(0);
        softly.assertThat(reignald.getSkillRankOf(SkillType.POLE)).isEqualTo(0);
        softly.assertThat(reignald.getCalculatedTotalSkillOf(SkillType.POLE)).isEqualTo(0);
        softly.assertThat(reignald.getSkillRankOf(SkillType.SHIELD)).isEqualTo(2);
        softly.assertThat(reignald.getCalculatedTotalSkillOf(SkillType.SHIELD)).isEqualTo(2);
        softly.assertThat(reignald.getSkillRankOf(SkillType.SWORD)).isEqualTo(4);
        softly.assertThat(reignald.getCalculatedTotalSkillOf(SkillType.SWORD)).isEqualTo(4);
        softly.assertThat(reignald.getSkillRankOf(SkillType.THROWN)).isEqualTo(2);
        softly.assertThat(reignald.getCalculatedTotalSkillOf(SkillType.THROWN)).isEqualTo(2);

        softly.assertThat(reignald.getCalcValueOf(InventoryGroup.WEAPON, CalcType.BASE_HIT)).isEqualTo(50);
        softly.assertThat(reignald.getCalcValueOf(InventoryGroup.WEAPON, CalcType.DAMAGE)).isEqualTo(15);
        softly.assertThat(reignald.getCalcValueOf(InventoryGroup.SHIELD, CalcType.DEFENSE)).isEqualTo(10);
        softly.assertThat(reignald.getTotalCalcOf(CalcType.PROTECTION)).isEqualTo(8);

        //////

        softly.assertThat(ryiah.school).isEqualTo(SchoolType.NAMING);
        softly.assertThat(ryiah.getLevel()).isEqualTo(3);
        softly.assertThat(ryiah.getTotalXp()).isEqualTo(70);
        softly.assertThat(ryiah.getXpNeededForNextLevel()).isEqualTo(80);
        softly.assertThat(ryiah.getCurrentHp()).isEqualTo(50);
        softly.assertThat(ryiah.getMaximumHp()).isEqualTo(50);

        softly.assertThat(ryiah.getStatRankOf(StatType.INTELLIGENCE)).isEqualTo(22);
        softly.assertThat(ryiah.getCalculatedTotalStatOf(StatType.INTELLIGENCE)).isEqualTo(22);
        softly.assertThat(ryiah.getStatRankOf(StatType.WILLPOWER)).isEqualTo(16);
        softly.assertThat(ryiah.getCalculatedTotalStatOf(StatType.WILLPOWER)).isEqualTo(16);
        softly.assertThat(ryiah.getStatRankOf(StatType.DEXTERITY)).isEqualTo(20);
        softly.assertThat(ryiah.getCalculatedTotalStatOf(StatType.DEXTERITY)).isEqualTo(20);
        softly.assertThat(ryiah.getStatRankOf(StatType.AGILITY)).isEqualTo(15);
        softly.assertThat(ryiah.getCalculatedTotalStatOf(StatType.AGILITY)).isEqualTo(15);
        softly.assertThat(ryiah.getStatRankOf(StatType.ENDURANCE)).isEqualTo(16);
        softly.assertThat(ryiah.getCalculatedTotalStatOf(StatType.ENDURANCE)).isEqualTo(16);
        softly.assertThat(ryiah.getStatRankOf(StatType.STRENGTH)).isEqualTo(10);
        softly.assertThat(ryiah.getCalculatedTotalStatOf(StatType.STRENGTH)).isEqualTo(10);
        softly.assertThat(ryiah.getStatRankOf(StatType.STAMINA)).isEqualTo(31);
        softly.assertThat(ryiah.getCalculatedTotalStatOf(StatType.STAMINA)).isEqualTo(31);

        softly.assertThat(ryiah.getSkillRankOf(SkillType.ALCHEMIST)).isEqualTo(0);
        softly.assertThat(ryiah.getCalculatedTotalSkillOf(SkillType.ALCHEMIST)).isEqualTo(0);
        softly.assertThat(ryiah.getSkillRankOf(SkillType.DIPLOMAT)).isEqualTo(0);
        softly.assertThat(ryiah.getCalculatedTotalSkillOf(SkillType.DIPLOMAT)).isEqualTo(0);
        softly.assertThat(ryiah.getSkillRankOf(SkillType.HEALER)).isEqualTo(0);
        softly.assertThat(ryiah.getCalculatedTotalSkillOf(SkillType.HEALER)).isEqualTo(0);
        softly.assertThat(ryiah.getSkillRankOf(SkillType.LOREMASTER)).isEqualTo(1);
        softly.assertThat(ryiah.getCalculatedTotalSkillOf(SkillType.LOREMASTER)).isEqualTo(1);
        softly.assertThat(ryiah.getSkillRankOf(SkillType.MECHANIC)).isEqualTo(0);
        softly.assertThat(ryiah.getCalculatedTotalSkillOf(SkillType.MECHANIC)).isEqualTo(0);
        softly.assertThat(ryiah.getSkillRankOf(SkillType.MERCHANT)).isEqualTo(1);
        softly.assertThat(ryiah.getCalculatedTotalSkillOf(SkillType.MERCHANT)).isEqualTo(1);
        softly.assertThat(ryiah.getSkillRankOf(SkillType.RANGER)).isEqualTo(0);
        softly.assertThat(ryiah.getCalculatedTotalSkillOf(SkillType.RANGER)).isEqualTo(0);
        softly.assertThat(ryiah.getSkillRankOf(SkillType.STEALTH)).isEqualTo(0);
        softly.assertThat(ryiah.getCalculatedTotalSkillOf(SkillType.STEALTH)).isEqualTo(0);
        softly.assertThat(ryiah.getSkillRankOf(SkillType.THIEF)).isEqualTo(0);
        softly.assertThat(ryiah.getCalculatedTotalSkillOf(SkillType.THIEF)).isEqualTo(0);
        softly.assertThat(ryiah.getSkillRankOf(SkillType.TROUBADOUR)).isEqualTo(0);
        softly.assertThat(ryiah.getCalculatedTotalSkillOf(SkillType.TROUBADOUR)).isEqualTo(0);
        softly.assertThat(ryiah.getSkillRankOf(SkillType.WARRIOR)).isEqualTo(0);
        softly.assertThat(ryiah.getCalculatedTotalSkillOf(SkillType.WARRIOR)).isEqualTo(0);
        softly.assertThat(ryiah.getSkillRankOf(SkillType.WIZARD)).isEqualTo(4);
        softly.assertThat(ryiah.getCalculatedTotalSkillOf(SkillType.WIZARD)).isEqualTo(4);

        softly.assertThat(ryiah.getSkillRankOf(SkillType.HAFTED)).isEqualTo(0);
        softly.assertThat(ryiah.getCalculatedTotalSkillOf(SkillType.HAFTED)).isEqualTo(0);
        softly.assertThat(ryiah.getSkillRankOf(SkillType.MISSILE)).isEqualTo(-1);
        softly.assertThat(ryiah.getCalculatedTotalSkillOf(SkillType.MISSILE)).isEqualTo(0);
        softly.assertThat(ryiah.getSkillRankOf(SkillType.POLE)).isEqualTo(3);
        softly.assertThat(ryiah.getCalculatedTotalSkillOf(SkillType.POLE)).isEqualTo(3);
        softly.assertThat(ryiah.getSkillRankOf(SkillType.SHIELD)).isEqualTo(0);
        softly.assertThat(ryiah.getCalculatedTotalSkillOf(SkillType.SHIELD)).isEqualTo(0);
        softly.assertThat(ryiah.getSkillRankOf(SkillType.SWORD)).isEqualTo(3);
        softly.assertThat(ryiah.getCalculatedTotalSkillOf(SkillType.SWORD)).isEqualTo(3);
        softly.assertThat(ryiah.getSkillRankOf(SkillType.THROWN)).isEqualTo(-1);
        softly.assertThat(ryiah.getCalculatedTotalSkillOf(SkillType.THROWN)).isEqualTo(0);

        softly.assertThat(ryiah.getCalcValueOf(InventoryGroup.WEAPON, CalcType.BASE_HIT)).isEqualTo(50);
        softly.assertThat(ryiah.getCalcValueOf(InventoryGroup.WEAPON, CalcType.DAMAGE)).isEqualTo(4);
        softly.assertThat(ryiah.getCalcValueOf(InventoryGroup.SHIELD, CalcType.DEFENSE)).isEqualTo(0);
        softly.assertThat(ryiah.getTotalCalcOf(CalcType.PROTECTION)).isEqualTo(1);

        //////

        softly.assertThat(valter.school).isEqualTo(SchoolType.ELEMENTAL);
        softly.assertThat(valter.getLevel()).isEqualTo(2);
        softly.assertThat(valter.getTotalXp()).isEqualTo(25);
        softly.assertThat(valter.getXpNeededForNextLevel()).isEqualTo(45);
        softly.assertThat(valter.getCurrentHp()).isEqualTo(37);
        softly.assertThat(valter.getMaximumHp()).isEqualTo(37);

        softly.assertThat(valter.getStatRankOf(StatType.INTELLIGENCE)).isEqualTo(22);
        softly.assertThat(valter.getCalculatedTotalStatOf(StatType.INTELLIGENCE)).isEqualTo(22);
        softly.assertThat(valter.getStatRankOf(StatType.WILLPOWER)).isEqualTo(18);
        softly.assertThat(valter.getCalculatedTotalStatOf(StatType.WILLPOWER)).isEqualTo(18);
        softly.assertThat(valter.getStatRankOf(StatType.DEXTERITY)).isEqualTo(15);
        softly.assertThat(valter.getCalculatedTotalStatOf(StatType.DEXTERITY)).isEqualTo(15);
        softly.assertThat(valter.getStatRankOf(StatType.AGILITY)).isEqualTo(12);
        softly.assertThat(valter.getCalculatedTotalStatOf(StatType.AGILITY)).isEqualTo(12);
        softly.assertThat(valter.getStatRankOf(StatType.ENDURANCE)).isEqualTo(15);
        softly.assertThat(valter.getCalculatedTotalStatOf(StatType.ENDURANCE)).isEqualTo(15);
        softly.assertThat(valter.getStatRankOf(StatType.STRENGTH)).isEqualTo(10);
        softly.assertThat(valter.getCalculatedTotalStatOf(StatType.STRENGTH)).isEqualTo(10);
        softly.assertThat(valter.getStatRankOf(StatType.STAMINA)).isEqualTo(20);
        softly.assertThat(valter.getCalculatedTotalStatOf(StatType.STAMINA)).isEqualTo(20);

        softly.assertThat(valter.getSkillRankOf(SkillType.ALCHEMIST)).isEqualTo(3);
        softly.assertThat(valter.getCalculatedTotalSkillOf(SkillType.ALCHEMIST)).isEqualTo(3);
        softly.assertThat(valter.getSkillRankOf(SkillType.DIPLOMAT)).isEqualTo(0);
        softly.assertThat(valter.getCalculatedTotalSkillOf(SkillType.DIPLOMAT)).isEqualTo(0);
        softly.assertThat(valter.getSkillRankOf(SkillType.HEALER)).isEqualTo(0);
        softly.assertThat(valter.getCalculatedTotalSkillOf(SkillType.HEALER)).isEqualTo(0);
        softly.assertThat(valter.getSkillRankOf(SkillType.LOREMASTER)).isEqualTo(2);
        softly.assertThat(valter.getCalculatedTotalSkillOf(SkillType.LOREMASTER)).isEqualTo(2);
        softly.assertThat(valter.getSkillRankOf(SkillType.MECHANIC)).isEqualTo(2);
        softly.assertThat(valter.getCalculatedTotalSkillOf(SkillType.MECHANIC)).isEqualTo(2);
        softly.assertThat(valter.getSkillRankOf(SkillType.MERCHANT)).isEqualTo(0);
        softly.assertThat(valter.getCalculatedTotalSkillOf(SkillType.MERCHANT)).isEqualTo(0);
        softly.assertThat(valter.getSkillRankOf(SkillType.RANGER)).isEqualTo(0);
        softly.assertThat(valter.getCalculatedTotalSkillOf(SkillType.RANGER)).isEqualTo(0);
        softly.assertThat(valter.getSkillRankOf(SkillType.STEALTH)).isEqualTo(0);
        softly.assertThat(valter.getCalculatedTotalSkillOf(SkillType.STEALTH)).isEqualTo(0);
        softly.assertThat(valter.getSkillRankOf(SkillType.THIEF)).isEqualTo(0);
        softly.assertThat(valter.getCalculatedTotalSkillOf(SkillType.THIEF)).isEqualTo(0);
        softly.assertThat(valter.getSkillRankOf(SkillType.TROUBADOUR)).isEqualTo(0);
        softly.assertThat(valter.getCalculatedTotalSkillOf(SkillType.TROUBADOUR)).isEqualTo(0);
        softly.assertThat(valter.getSkillRankOf(SkillType.WARRIOR)).isEqualTo(0);
        softly.assertThat(valter.getCalculatedTotalSkillOf(SkillType.WARRIOR)).isEqualTo(0);
        softly.assertThat(valter.getSkillRankOf(SkillType.WIZARD)).isEqualTo(2);
        softly.assertThat(valter.getCalculatedTotalSkillOf(SkillType.WIZARD)).isEqualTo(2);

        softly.assertThat(valter.getSkillRankOf(SkillType.HAFTED)).isEqualTo(-1);
        softly.assertThat(valter.getCalculatedTotalSkillOf(SkillType.HAFTED)).isEqualTo(0);
        softly.assertThat(valter.getSkillRankOf(SkillType.MISSILE)).isEqualTo(-1);
        softly.assertThat(valter.getCalculatedTotalSkillOf(SkillType.MISSILE)).isEqualTo(0);
        softly.assertThat(valter.getSkillRankOf(SkillType.POLE)).isEqualTo(0);
        softly.assertThat(valter.getCalculatedTotalSkillOf(SkillType.POLE)).isEqualTo(0);
        softly.assertThat(valter.getSkillRankOf(SkillType.SHIELD)).isEqualTo(-1);
        softly.assertThat(valter.getCalculatedTotalSkillOf(SkillType.SHIELD)).isEqualTo(0);
        softly.assertThat(valter.getSkillRankOf(SkillType.SWORD)).isEqualTo(1);
        softly.assertThat(valter.getCalculatedTotalSkillOf(SkillType.SWORD)).isEqualTo(1);
        softly.assertThat(valter.getSkillRankOf(SkillType.THROWN)).isEqualTo(1);
        softly.assertThat(valter.getCalculatedTotalSkillOf(SkillType.THROWN)).isEqualTo(1);

        softly.assertThat(valter.getCalcValueOf(InventoryGroup.WEAPON, CalcType.BASE_HIT)).isEqualTo(40);
        softly.assertThat(valter.getCalcValueOf(InventoryGroup.WEAPON, CalcType.DAMAGE)).isEqualTo(11);
        softly.assertThat(valter.getCalcValueOf(InventoryGroup.SHIELD, CalcType.DEFENSE)).isEqualTo(0);
        softly.assertThat(valter.getTotalCalcOf(CalcType.PROTECTION)).isEqualTo(1);

        //////
        softly.assertThat(galen.school).isEqualTo(SchoolType.NONE);
        softly.assertThat(galen.getLevel()).isEqualTo(4);
        softly.assertThat(galen.getTotalXp()).isEqualTo(150);
        softly.assertThat(galen.getXpNeededForNextLevel()).isEqualTo(125);
        softly.assertThat(galen.getCurrentHp()).isEqualTo(64);
        softly.assertThat(galen.getMaximumHp()).isEqualTo(64);

        softly.assertThat(galen.getStatRankOf(StatType.INTELLIGENCE)).isEqualTo(15);
        softly.assertThat(galen.getCalculatedTotalStatOf(StatType.INTELLIGENCE)).isEqualTo(15);
        softly.assertThat(galen.getStatRankOf(StatType.WILLPOWER)).isEqualTo(15);
        softly.assertThat(galen.getCalculatedTotalStatOf(StatType.WILLPOWER)).isEqualTo(15);
        softly.assertThat(galen.getStatRankOf(StatType.DEXTERITY)).isEqualTo(18);
        softly.assertThat(galen.getCalculatedTotalStatOf(StatType.DEXTERITY)).isEqualTo(12);
        softly.assertThat(galen.getStatRankOf(StatType.AGILITY)).isEqualTo(10);
        softly.assertThat(galen.getCalculatedTotalStatOf(StatType.AGILITY)).isEqualTo(10);
        softly.assertThat(galen.getStatRankOf(StatType.ENDURANCE)).isEqualTo(20);
        softly.assertThat(galen.getCalculatedTotalStatOf(StatType.ENDURANCE)).isEqualTo(20);
        softly.assertThat(galen.getStatRankOf(StatType.STRENGTH)).isEqualTo(25);
        softly.assertThat(galen.getCalculatedTotalStatOf(StatType.STRENGTH)).isEqualTo(25);
        softly.assertThat(galen.getStatRankOf(StatType.STAMINA)).isEqualTo(40);
        softly.assertThat(galen.getCalculatedTotalStatOf(StatType.STAMINA)).isEqualTo(40);

        softly.assertThat(galen.getSkillRankOf(SkillType.ALCHEMIST)).isEqualTo(-1);
        softly.assertThat(galen.getCalculatedTotalSkillOf(SkillType.ALCHEMIST)).isEqualTo(0);
        softly.assertThat(galen.getSkillRankOf(SkillType.DIPLOMAT)).isEqualTo(0);
        softly.assertThat(galen.getCalculatedTotalSkillOf(SkillType.DIPLOMAT)).isEqualTo(0);
        softly.assertThat(galen.getSkillRankOf(SkillType.HEALER)).isEqualTo(0);
        softly.assertThat(galen.getCalculatedTotalSkillOf(SkillType.HEALER)).isEqualTo(0);
        softly.assertThat(galen.getSkillRankOf(SkillType.LOREMASTER)).isEqualTo(0);
        softly.assertThat(galen.getCalculatedTotalSkillOf(SkillType.LOREMASTER)).isEqualTo(0);
        softly.assertThat(galen.getSkillRankOf(SkillType.MECHANIC)).isEqualTo(0);
        softly.assertThat(galen.getCalculatedTotalSkillOf(SkillType.MECHANIC)).isEqualTo(0);
        softly.assertThat(galen.getSkillRankOf(SkillType.MERCHANT)).isEqualTo(0);
        softly.assertThat(galen.getCalculatedTotalSkillOf(SkillType.MERCHANT)).isEqualTo(0);
        softly.assertThat(galen.getSkillRankOf(SkillType.RANGER)).isEqualTo(4);
        softly.assertThat(galen.getCalculatedTotalSkillOf(SkillType.RANGER)).isEqualTo(4);
        softly.assertThat(galen.getSkillRankOf(SkillType.STEALTH)).isEqualTo(3);
        softly.assertThat(galen.getCalculatedTotalSkillOf(SkillType.STEALTH)).isEqualTo(0);
        softly.assertThat(galen.getSkillRankOf(SkillType.THIEF)).isEqualTo(0);
        softly.assertThat(galen.getCalculatedTotalSkillOf(SkillType.THIEF)).isEqualTo(0);
        softly.assertThat(galen.getSkillRankOf(SkillType.TROUBADOUR)).isEqualTo(0);
        softly.assertThat(galen.getCalculatedTotalSkillOf(SkillType.TROUBADOUR)).isEqualTo(0);
        softly.assertThat(galen.getSkillRankOf(SkillType.WARRIOR)).isEqualTo(5);
        softly.assertThat(galen.getCalculatedTotalSkillOf(SkillType.WARRIOR)).isEqualTo(5);
        softly.assertThat(galen.getSkillRankOf(SkillType.WIZARD)).isEqualTo(-1);
        softly.assertThat(galen.getCalculatedTotalSkillOf(SkillType.WIZARD)).isEqualTo(0);

        softly.assertThat(galen.getSkillRankOf(SkillType.HAFTED)).isEqualTo(5);
        softly.assertThat(galen.getCalculatedTotalSkillOf(SkillType.HAFTED)).isEqualTo(5);
        softly.assertThat(galen.getSkillRankOf(SkillType.MISSILE)).isEqualTo(3);
        softly.assertThat(galen.getCalculatedTotalSkillOf(SkillType.MISSILE)).isEqualTo(3);
        softly.assertThat(galen.getSkillRankOf(SkillType.POLE)).isEqualTo(0);
        softly.assertThat(galen.getCalculatedTotalSkillOf(SkillType.POLE)).isEqualTo(0);
        softly.assertThat(galen.getSkillRankOf(SkillType.SHIELD)).isEqualTo(3);
        softly.assertThat(galen.getCalculatedTotalSkillOf(SkillType.SHIELD)).isEqualTo(3);
        softly.assertThat(galen.getSkillRankOf(SkillType.SWORD)).isEqualTo(-1);
        softly.assertThat(galen.getCalculatedTotalSkillOf(SkillType.SWORD)).isEqualTo(0);
        softly.assertThat(galen.getSkillRankOf(SkillType.THROWN)).isEqualTo(-1);
        softly.assertThat(galen.getCalculatedTotalSkillOf(SkillType.THROWN)).isEqualTo(0);

        softly.assertThat(galen.getCalcValueOf(InventoryGroup.WEAPON, CalcType.BASE_HIT)).isEqualTo(30);
        softly.assertThat(galen.getCalcValueOf(InventoryGroup.WEAPON, CalcType.DAMAGE)).isEqualTo(20);
        softly.assertThat(galen.getCalcValueOf(InventoryGroup.SHIELD, CalcType.DEFENSE)).isEqualTo(10);
        softly.assertThat(galen.getTotalCalcOf(CalcType.PROTECTION)).isEqualTo(10);

        //////

        softly.assertThat(jaspar.school).isEqualTo(SchoolType.NONE);
        softly.assertThat(jaspar.getLevel()).isEqualTo(12);
        softly.assertThat(jaspar.getTotalXp()).isEqualTo(3250);
        softly.assertThat(jaspar.getXpNeededForNextLevel()).isEqualTo(845);
        softly.assertThat(jaspar.getCurrentHp()).isEqualTo(102);
        softly.assertThat(jaspar.getMaximumHp()).isEqualTo(102);

        softly.assertThat(jaspar.getStatRankOf(StatType.INTELLIGENCE)).isEqualTo(6);
        softly.assertThat(jaspar.getCalculatedTotalStatOf(StatType.INTELLIGENCE)).isEqualTo(6);
        softly.assertThat(jaspar.getStatRankOf(StatType.WILLPOWER)).isEqualTo(11);
        softly.assertThat(jaspar.getCalculatedTotalStatOf(StatType.WILLPOWER)).isEqualTo(11);
        softly.assertThat(jaspar.getStatRankOf(StatType.DEXTERITY)).isEqualTo(14);
        softly.assertThat(jaspar.getCalculatedTotalStatOf(StatType.DEXTERITY)).isEqualTo(4);
        softly.assertThat(jaspar.getStatRankOf(StatType.AGILITY)).isEqualTo(8);
        softly.assertThat(jaspar.getCalculatedTotalStatOf(StatType.AGILITY)).isEqualTo(8);
        softly.assertThat(jaspar.getStatRankOf(StatType.ENDURANCE)).isEqualTo(30);
        softly.assertThat(jaspar.getCalculatedTotalStatOf(StatType.ENDURANCE)).isEqualTo(30);
        softly.assertThat(jaspar.getStatRankOf(StatType.STRENGTH)).isEqualTo(30);
        softly.assertThat(jaspar.getCalculatedTotalStatOf(StatType.STRENGTH)).isEqualTo(30);
        softly.assertThat(jaspar.getStatRankOf(StatType.STAMINA)).isEqualTo(60);
        softly.assertThat(jaspar.getCalculatedTotalStatOf(StatType.STAMINA)).isEqualTo(60);

        softly.assertThat(jaspar.getSkillRankOf(SkillType.ALCHEMIST)).isEqualTo(-1);
        softly.assertThat(jaspar.getCalculatedTotalSkillOf(SkillType.ALCHEMIST)).isEqualTo(0);
        softly.assertThat(jaspar.getSkillRankOf(SkillType.DIPLOMAT)).isEqualTo(0);
        softly.assertThat(jaspar.getCalculatedTotalSkillOf(SkillType.DIPLOMAT)).isEqualTo(0);
        softly.assertThat(jaspar.getSkillRankOf(SkillType.HEALER)).isEqualTo(0);
        softly.assertThat(jaspar.getCalculatedTotalSkillOf(SkillType.HEALER)).isEqualTo(0);
        softly.assertThat(jaspar.getSkillRankOf(SkillType.LOREMASTER)).isEqualTo(-1);
        softly.assertThat(jaspar.getCalculatedTotalSkillOf(SkillType.LOREMASTER)).isEqualTo(0);
        softly.assertThat(jaspar.getSkillRankOf(SkillType.MECHANIC)).isEqualTo(0);
        softly.assertThat(jaspar.getCalculatedTotalSkillOf(SkillType.MECHANIC)).isEqualTo(0);
        softly.assertThat(jaspar.getSkillRankOf(SkillType.MERCHANT)).isEqualTo(0);
        softly.assertThat(jaspar.getCalculatedTotalSkillOf(SkillType.MERCHANT)).isEqualTo(0);
        softly.assertThat(jaspar.getSkillRankOf(SkillType.RANGER)).isEqualTo(0);
        softly.assertThat(jaspar.getCalculatedTotalSkillOf(SkillType.RANGER)).isEqualTo(0);
        softly.assertThat(jaspar.getSkillRankOf(SkillType.STEALTH)).isEqualTo(1);
        softly.assertThat(jaspar.getCalculatedTotalSkillOf(SkillType.STEALTH)).isEqualTo(0);
        softly.assertThat(jaspar.getSkillRankOf(SkillType.THIEF)).isEqualTo(-1);
        softly.assertThat(jaspar.getCalculatedTotalSkillOf(SkillType.THIEF)).isEqualTo(0);
        softly.assertThat(jaspar.getSkillRankOf(SkillType.TROUBADOUR)).isEqualTo(-1);
        softly.assertThat(jaspar.getCalculatedTotalSkillOf(SkillType.TROUBADOUR)).isEqualTo(0);
        softly.assertThat(jaspar.getSkillRankOf(SkillType.WARRIOR)).isEqualTo(6);
        softly.assertThat(jaspar.getCalculatedTotalSkillOf(SkillType.WARRIOR)).isEqualTo(6);
        softly.assertThat(jaspar.getSkillRankOf(SkillType.WIZARD)).isEqualTo(-1);
        softly.assertThat(jaspar.getCalculatedTotalSkillOf(SkillType.WIZARD)).isEqualTo(0);

        softly.assertThat(jaspar.getSkillRankOf(SkillType.HAFTED)).isEqualTo(0);
        softly.assertThat(jaspar.getCalculatedTotalSkillOf(SkillType.HAFTED)).isEqualTo(0);
        softly.assertThat(jaspar.getSkillRankOf(SkillType.MISSILE)).isEqualTo(-1);
        softly.assertThat(jaspar.getCalculatedTotalSkillOf(SkillType.MISSILE)).isEqualTo(0);
        softly.assertThat(jaspar.getSkillRankOf(SkillType.POLE)).isEqualTo(6);
        softly.assertThat(jaspar.getCalculatedTotalSkillOf(SkillType.POLE)).isEqualTo(6);
        softly.assertThat(jaspar.getSkillRankOf(SkillType.SHIELD)).isEqualTo(4);
        softly.assertThat(jaspar.getCalculatedTotalSkillOf(SkillType.SHIELD)).isEqualTo(4);
        softly.assertThat(jaspar.getSkillRankOf(SkillType.SWORD)).isEqualTo(6);
        softly.assertThat(jaspar.getCalculatedTotalSkillOf(SkillType.SWORD)).isEqualTo(6);
        softly.assertThat(jaspar.getSkillRankOf(SkillType.THROWN)).isEqualTo(-1);
        softly.assertThat(jaspar.getCalculatedTotalSkillOf(SkillType.THROWN)).isEqualTo(0);

        softly.assertThat(jaspar.getCalcValueOf(InventoryGroup.WEAPON, CalcType.BASE_HIT)).isEqualTo(40);
        softly.assertThat(jaspar.getCalcValueOf(InventoryGroup.WEAPON, CalcType.DAMAGE)).isEqualTo(17);
        softly.assertThat(jaspar.getCalcValueOf(InventoryGroup.SHIELD, CalcType.DEFENSE)).isEqualTo(15);
        softly.assertThat(jaspar.getTotalCalcOf(CalcType.PROTECTION)).isEqualTo(18);

        //////

        softly.assertThat(kiara.school).isEqualTo(SchoolType.ELEMENTAL);
        softly.assertThat(kiara.getLevel()).isEqualTo(12);
        softly.assertThat(kiara.getTotalXp()).isEqualTo(3250);
        softly.assertThat(kiara.getXpNeededForNextLevel()).isEqualTo(845);
        softly.assertThat(kiara.getCurrentHp()).isEqualTo(72);
        softly.assertThat(kiara.getMaximumHp()).isEqualTo(72);

        softly.assertThat(kiara.getStatRankOf(StatType.INTELLIGENCE)).isEqualTo(15);
        softly.assertThat(kiara.getCalculatedTotalStatOf(StatType.INTELLIGENCE)).isEqualTo(15);
        softly.assertThat(kiara.getStatRankOf(StatType.WILLPOWER)).isEqualTo(10);
        softly.assertThat(kiara.getCalculatedTotalStatOf(StatType.WILLPOWER)).isEqualTo(10);
        softly.assertThat(kiara.getStatRankOf(StatType.DEXTERITY)).isEqualTo(30);
        softly.assertThat(kiara.getCalculatedTotalStatOf(StatType.DEXTERITY)).isEqualTo(30);
        softly.assertThat(kiara.getStatRankOf(StatType.AGILITY)).isEqualTo(26);
        softly.assertThat(kiara.getCalculatedTotalStatOf(StatType.AGILITY)).isEqualTo(26);
        softly.assertThat(kiara.getStatRankOf(StatType.ENDURANCE)).isEqualTo(20);
        softly.assertThat(kiara.getCalculatedTotalStatOf(StatType.ENDURANCE)).isEqualTo(20);
        softly.assertThat(kiara.getStatRankOf(StatType.STRENGTH)).isEqualTo(15);
        softly.assertThat(kiara.getCalculatedTotalStatOf(StatType.STRENGTH)).isEqualTo(15);
        softly.assertThat(kiara.getStatRankOf(StatType.STAMINA)).isEqualTo(40);
        softly.assertThat(kiara.getCalculatedTotalStatOf(StatType.STAMINA)).isEqualTo(40);

        softly.assertThat(kiara.getSkillRankOf(SkillType.ALCHEMIST)).isEqualTo(0);
        softly.assertThat(kiara.getCalculatedTotalSkillOf(SkillType.ALCHEMIST)).isEqualTo(0);
        softly.assertThat(kiara.getSkillRankOf(SkillType.DIPLOMAT)).isEqualTo(0);
        softly.assertThat(kiara.getCalculatedTotalSkillOf(SkillType.DIPLOMAT)).isEqualTo(0);
        softly.assertThat(kiara.getSkillRankOf(SkillType.HEALER)).isEqualTo(1);
        softly.assertThat(kiara.getCalculatedTotalSkillOf(SkillType.HEALER)).isEqualTo(1);
        softly.assertThat(kiara.getSkillRankOf(SkillType.LOREMASTER)).isEqualTo(0);
        softly.assertThat(kiara.getCalculatedTotalSkillOf(SkillType.LOREMASTER)).isEqualTo(0);
        softly.assertThat(kiara.getSkillRankOf(SkillType.MECHANIC)).isEqualTo(0);
        softly.assertThat(kiara.getCalculatedTotalSkillOf(SkillType.MECHANIC)).isEqualTo(0);
        softly.assertThat(kiara.getSkillRankOf(SkillType.MERCHANT)).isEqualTo(4);
        softly.assertThat(kiara.getCalculatedTotalSkillOf(SkillType.MERCHANT)).isEqualTo(4);
        softly.assertThat(kiara.getSkillRankOf(SkillType.RANGER)).isEqualTo(0);
        softly.assertThat(kiara.getCalculatedTotalSkillOf(SkillType.RANGER)).isEqualTo(0);
        softly.assertThat(kiara.getSkillRankOf(SkillType.STEALTH)).isEqualTo(5);
        softly.assertThat(kiara.getCalculatedTotalSkillOf(SkillType.STEALTH)).isEqualTo(5);
        softly.assertThat(kiara.getSkillRankOf(SkillType.THIEF)).isEqualTo(8);
        softly.assertThat(kiara.getCalculatedTotalSkillOf(SkillType.THIEF)).isEqualTo(8);
        softly.assertThat(kiara.getSkillRankOf(SkillType.TROUBADOUR)).isEqualTo(0);
        softly.assertThat(kiara.getCalculatedTotalSkillOf(SkillType.TROUBADOUR)).isEqualTo(0);
        softly.assertThat(kiara.getSkillRankOf(SkillType.WARRIOR)).isEqualTo(0);
        softly.assertThat(kiara.getCalculatedTotalSkillOf(SkillType.WARRIOR)).isEqualTo(0);
        softly.assertThat(kiara.getSkillRankOf(SkillType.WIZARD)).isEqualTo(4);
        softly.assertThat(kiara.getCalculatedTotalSkillOf(SkillType.WIZARD)).isEqualTo(4);

        softly.assertThat(kiara.getSkillRankOf(SkillType.HAFTED)).isEqualTo(-1);
        softly.assertThat(kiara.getCalculatedTotalSkillOf(SkillType.HAFTED)).isEqualTo(0);
        softly.assertThat(kiara.getSkillRankOf(SkillType.MISSILE)).isEqualTo(7);
        softly.assertThat(kiara.getCalculatedTotalSkillOf(SkillType.MISSILE)).isEqualTo(7);
        softly.assertThat(kiara.getSkillRankOf(SkillType.POLE)).isEqualTo(2);
        softly.assertThat(kiara.getCalculatedTotalSkillOf(SkillType.POLE)).isEqualTo(2);
        softly.assertThat(kiara.getSkillRankOf(SkillType.SHIELD)).isEqualTo(-1);
        softly.assertThat(kiara.getCalculatedTotalSkillOf(SkillType.SHIELD)).isEqualTo(0);
        softly.assertThat(kiara.getSkillRankOf(SkillType.SWORD)).isEqualTo(7);
        softly.assertThat(kiara.getCalculatedTotalSkillOf(SkillType.SWORD)).isEqualTo(7);
        softly.assertThat(kiara.getSkillRankOf(SkillType.THROWN)).isEqualTo(-1);
        softly.assertThat(kiara.getCalculatedTotalSkillOf(SkillType.THROWN)).isEqualTo(0);

        softly.assertThat(kiara.getCalcValueOf(InventoryGroup.WEAPON, CalcType.BASE_HIT)).isEqualTo(50);
        softly.assertThat(kiara.getCalcValueOf(InventoryGroup.WEAPON, CalcType.DAMAGE)).isEqualTo(13);
        softly.assertThat(kiara.getCalcValueOf(InventoryGroup.SHIELD, CalcType.DEFENSE)).isEqualTo(0);
        softly.assertThat(kiara.getTotalCalcOf(CalcType.PROTECTION)).isEqualTo(2);

        //////

        softly.assertThat(luthais.school).isEqualTo(SchoolType.ELEMENTAL);
        softly.assertThat(luthais.getLevel()).isEqualTo(20);
        softly.assertThat(luthais.getTotalXp()).isEqualTo(14350);
        softly.assertThat(luthais.getXpNeededForNextLevel()).isEqualTo(2205);
        softly.assertThat(luthais.getCurrentHp()).isEqualTo(88);
        softly.assertThat(luthais.getMaximumHp()).isEqualTo(88);

        softly.assertThat(luthais.getStatRankOf(StatType.INTELLIGENCE)).isEqualTo(30);
        softly.assertThat(luthais.getCalculatedTotalStatOf(StatType.INTELLIGENCE)).isEqualTo(30);
        softly.assertThat(luthais.getStatRankOf(StatType.WILLPOWER)).isEqualTo(30);
        softly.assertThat(luthais.getCalculatedTotalStatOf(StatType.WILLPOWER)).isEqualTo(30);
        softly.assertThat(luthais.getStatRankOf(StatType.DEXTERITY)).isEqualTo(20);
        softly.assertThat(luthais.getCalculatedTotalStatOf(StatType.DEXTERITY)).isEqualTo(20);
        softly.assertThat(luthais.getStatRankOf(StatType.AGILITY)).isEqualTo(12);
        softly.assertThat(luthais.getCalculatedTotalStatOf(StatType.AGILITY)).isEqualTo(12);
        softly.assertThat(luthais.getStatRankOf(StatType.ENDURANCE)).isEqualTo(18);
        softly.assertThat(luthais.getCalculatedTotalStatOf(StatType.ENDURANCE)).isEqualTo(18);
        softly.assertThat(luthais.getStatRankOf(StatType.STRENGTH)).isEqualTo(8);
        softly.assertThat(luthais.getCalculatedTotalStatOf(StatType.STRENGTH)).isEqualTo(8);
        softly.assertThat(luthais.getStatRankOf(StatType.STAMINA)).isEqualTo(50);
        softly.assertThat(luthais.getCalculatedTotalStatOf(StatType.STAMINA)).isEqualTo(50);

        softly.assertThat(luthais.getSkillRankOf(SkillType.ALCHEMIST)).isEqualTo(7);
        softly.assertThat(luthais.getCalculatedTotalSkillOf(SkillType.ALCHEMIST)).isEqualTo(7);
        softly.assertThat(luthais.getSkillRankOf(SkillType.DIPLOMAT)).isEqualTo(0);
        softly.assertThat(luthais.getCalculatedTotalSkillOf(SkillType.DIPLOMAT)).isEqualTo(0);
        softly.assertThat(luthais.getSkillRankOf(SkillType.HEALER)).isEqualTo(8);
        softly.assertThat(luthais.getCalculatedTotalSkillOf(SkillType.HEALER)).isEqualTo(8);
        softly.assertThat(luthais.getSkillRankOf(SkillType.LOREMASTER)).isEqualTo(9);
        softly.assertThat(luthais.getCalculatedTotalSkillOf(SkillType.LOREMASTER)).isEqualTo(9);
        softly.assertThat(luthais.getSkillRankOf(SkillType.MECHANIC)).isEqualTo(6);
        softly.assertThat(luthais.getCalculatedTotalSkillOf(SkillType.MECHANIC)).isEqualTo(6);
        softly.assertThat(luthais.getSkillRankOf(SkillType.MERCHANT)).isEqualTo(0);
        softly.assertThat(luthais.getCalculatedTotalSkillOf(SkillType.MERCHANT)).isEqualTo(0);
        softly.assertThat(luthais.getSkillRankOf(SkillType.RANGER)).isEqualTo(0);
        softly.assertThat(luthais.getCalculatedTotalSkillOf(SkillType.RANGER)).isEqualTo(0);
        softly.assertThat(luthais.getSkillRankOf(SkillType.STEALTH)).isEqualTo(5);
        softly.assertThat(luthais.getCalculatedTotalSkillOf(SkillType.STEALTH)).isEqualTo(5);
        softly.assertThat(luthais.getSkillRankOf(SkillType.THIEF)).isEqualTo(0);
        softly.assertThat(luthais.getCalculatedTotalSkillOf(SkillType.THIEF)).isEqualTo(0);
        softly.assertThat(luthais.getSkillRankOf(SkillType.TROUBADOUR)).isEqualTo(0);
        softly.assertThat(luthais.getCalculatedTotalSkillOf(SkillType.TROUBADOUR)).isEqualTo(0);
        softly.assertThat(luthais.getSkillRankOf(SkillType.WARRIOR)).isEqualTo(0);
        softly.assertThat(luthais.getCalculatedTotalSkillOf(SkillType.WARRIOR)).isEqualTo(0);
        softly.assertThat(luthais.getSkillRankOf(SkillType.WIZARD)).isEqualTo(10);
        softly.assertThat(luthais.getCalculatedTotalSkillOf(SkillType.WIZARD)).isEqualTo(10);

        softly.assertThat(luthais.getSkillRankOf(SkillType.HAFTED)).isEqualTo(0);
        softly.assertThat(luthais.getCalculatedTotalSkillOf(SkillType.HAFTED)).isEqualTo(0);
        softly.assertThat(luthais.getSkillRankOf(SkillType.MISSILE)).isEqualTo(-1);
        softly.assertThat(luthais.getCalculatedTotalSkillOf(SkillType.MISSILE)).isEqualTo(0);
        softly.assertThat(luthais.getSkillRankOf(SkillType.POLE)).isEqualTo(8);
        softly.assertThat(luthais.getCalculatedTotalSkillOf(SkillType.POLE)).isEqualTo(8);
        softly.assertThat(luthais.getSkillRankOf(SkillType.SHIELD)).isEqualTo(-1);
        softly.assertThat(luthais.getCalculatedTotalSkillOf(SkillType.SHIELD)).isEqualTo(0);
        softly.assertThat(luthais.getSkillRankOf(SkillType.SWORD)).isEqualTo(0);
        softly.assertThat(luthais.getCalculatedTotalSkillOf(SkillType.SWORD)).isEqualTo(0);
        softly.assertThat(luthais.getSkillRankOf(SkillType.THROWN)).isEqualTo(8);
        softly.assertThat(luthais.getCalculatedTotalSkillOf(SkillType.THROWN)).isEqualTo(8);

        softly.assertThat(luthais.getCalcValueOf(InventoryGroup.WEAPON, CalcType.BASE_HIT)).isEqualTo(80);
        softly.assertThat(luthais.getCalcValueOf(InventoryGroup.WEAPON, CalcType.DAMAGE)).isEqualTo(10);
        softly.assertThat(luthais.getCalcValueOf(InventoryGroup.SHIELD, CalcType.DEFENSE)).isEqualTo(0);
        softly.assertThat(luthais.getTotalCalcOf(CalcType.PROTECTION)).isEqualTo(3);

        //////

        softly.assertThat(elias.school).isEqualTo(SchoolType.NAMING);
        softly.assertThat(elias.getLevel()).isEqualTo(18);
        softly.assertThat(elias.getTotalXp()).isEqualTo(10545);
        softly.assertThat(elias.getXpNeededForNextLevel()).isEqualTo(1805);
        softly.assertThat(elias.getCurrentHp()).isEqualTo(108);
        softly.assertThat(elias.getMaximumHp()).isEqualTo(108);

        softly.assertThat(elias.getStatRankOf(StatType.INTELLIGENCE)).isEqualTo(30);
        softly.assertThat(elias.getCalculatedTotalStatOf(StatType.INTELLIGENCE)).isEqualTo(30);
        softly.assertThat(elias.getStatRankOf(StatType.WILLPOWER)).isEqualTo(30);
        softly.assertThat(elias.getCalculatedTotalStatOf(StatType.WILLPOWER)).isEqualTo(30);
        softly.assertThat(elias.getStatRankOf(StatType.DEXTERITY)).isEqualTo(25);
        softly.assertThat(elias.getCalculatedTotalStatOf(StatType.DEXTERITY)).isEqualTo(25);
        softly.assertThat(elias.getStatRankOf(StatType.AGILITY)).isEqualTo(18);
        softly.assertThat(elias.getCalculatedTotalStatOf(StatType.AGILITY)).isEqualTo(18);
        softly.assertThat(elias.getStatRankOf(StatType.ENDURANCE)).isEqualTo(30);
        softly.assertThat(elias.getCalculatedTotalStatOf(StatType.ENDURANCE)).isEqualTo(30);
        softly.assertThat(elias.getStatRankOf(StatType.STRENGTH)).isEqualTo(20);
        softly.assertThat(elias.getCalculatedTotalStatOf(StatType.STRENGTH)).isEqualTo(20);
        softly.assertThat(elias.getStatRankOf(StatType.STAMINA)).isEqualTo(60);
        softly.assertThat(elias.getCalculatedTotalStatOf(StatType.STAMINA)).isEqualTo(60);

        softly.assertThat(elias.getSkillRankOf(SkillType.ALCHEMIST)).isEqualTo(0);
        softly.assertThat(elias.getCalculatedTotalSkillOf(SkillType.ALCHEMIST)).isEqualTo(0);
        softly.assertThat(elias.getSkillRankOf(SkillType.DIPLOMAT)).isEqualTo(8);
        softly.assertThat(elias.getCalculatedTotalSkillOf(SkillType.DIPLOMAT)).isEqualTo(8);
        softly.assertThat(elias.getSkillRankOf(SkillType.HEALER)).isEqualTo(0);
        softly.assertThat(elias.getCalculatedTotalSkillOf(SkillType.HEALER)).isEqualTo(0);
        softly.assertThat(elias.getSkillRankOf(SkillType.LOREMASTER)).isEqualTo(0);
        softly.assertThat(elias.getCalculatedTotalSkillOf(SkillType.LOREMASTER)).isEqualTo(0);
        softly.assertThat(elias.getSkillRankOf(SkillType.MECHANIC)).isEqualTo(0);
        softly.assertThat(elias.getCalculatedTotalSkillOf(SkillType.MECHANIC)).isEqualTo(0);
        softly.assertThat(elias.getSkillRankOf(SkillType.MERCHANT)).isEqualTo(0);
        softly.assertThat(elias.getCalculatedTotalSkillOf(SkillType.MERCHANT)).isEqualTo(0);
        softly.assertThat(elias.getSkillRankOf(SkillType.RANGER)).isEqualTo(0);
        softly.assertThat(elias.getCalculatedTotalSkillOf(SkillType.RANGER)).isEqualTo(0);
        softly.assertThat(elias.getSkillRankOf(SkillType.STEALTH)).isEqualTo(0);
        softly.assertThat(elias.getCalculatedTotalSkillOf(SkillType.STEALTH)).isEqualTo(0);
        softly.assertThat(elias.getSkillRankOf(SkillType.THIEF)).isEqualTo(0);
        softly.assertThat(elias.getCalculatedTotalSkillOf(SkillType.THIEF)).isEqualTo(0);
        softly.assertThat(elias.getSkillRankOf(SkillType.TROUBADOUR)).isEqualTo(0);
        softly.assertThat(elias.getCalculatedTotalSkillOf(SkillType.TROUBADOUR)).isEqualTo(0);
        softly.assertThat(elias.getSkillRankOf(SkillType.WARRIOR)).isEqualTo(7);
        softly.assertThat(elias.getCalculatedTotalSkillOf(SkillType.WARRIOR)).isEqualTo(7);
        softly.assertThat(elias.getSkillRankOf(SkillType.WIZARD)).isEqualTo(7);
        softly.assertThat(elias.getCalculatedTotalSkillOf(SkillType.WIZARD)).isEqualTo(7);

        softly.assertThat(elias.getSkillRankOf(SkillType.HAFTED)).isEqualTo(5);
        softly.assertThat(elias.getCalculatedTotalSkillOf(SkillType.HAFTED)).isEqualTo(5);
        softly.assertThat(elias.getSkillRankOf(SkillType.MISSILE)).isEqualTo(-1);
        softly.assertThat(elias.getCalculatedTotalSkillOf(SkillType.MISSILE)).isEqualTo(0);
        softly.assertThat(elias.getSkillRankOf(SkillType.POLE)).isEqualTo(5);
        softly.assertThat(elias.getCalculatedTotalSkillOf(SkillType.POLE)).isEqualTo(5);
        softly.assertThat(elias.getSkillRankOf(SkillType.SHIELD)).isEqualTo(-1);
        softly.assertThat(elias.getCalculatedTotalSkillOf(SkillType.SHIELD)).isEqualTo(0);
        softly.assertThat(elias.getSkillRankOf(SkillType.SWORD)).isEqualTo(7);
        softly.assertThat(elias.getCalculatedTotalSkillOf(SkillType.SWORD)).isEqualTo(7);
        softly.assertThat(elias.getSkillRankOf(SkillType.THROWN)).isEqualTo(-1);
        softly.assertThat(elias.getCalculatedTotalSkillOf(SkillType.THROWN)).isEqualTo(0);

        softly.assertThat(elias.getCalcValueOf(InventoryGroup.WEAPON, CalcType.BASE_HIT)).isEqualTo(50);
        softly.assertThat(elias.getCalcValueOf(InventoryGroup.WEAPON, CalcType.DAMAGE)).isEqualTo(17);
        softly.assertThat(elias.getCalcValueOf(InventoryGroup.SHIELD, CalcType.DEFENSE)).isEqualTo(0);
        softly.assertThat(elias.getTotalCalcOf(CalcType.PROTECTION)).isEqualTo(6);

        //////

        softly.assertThat(onarr.school).isEqualTo(SchoolType.NONE);
        softly.assertThat(onarr.getLevel()).isEqualTo(18);
        softly.assertThat(onarr.getTotalXp()).isEqualTo(10545);
        softly.assertThat(onarr.getXpNeededForNextLevel()).isEqualTo(1805);
        softly.assertThat(onarr.getCurrentHp()).isEqualTo(108);
        softly.assertThat(onarr.getMaximumHp()).isEqualTo(108);

        softly.assertThat(onarr.getStatRankOf(StatType.INTELLIGENCE)).isEqualTo(30);
        softly.assertThat(onarr.getCalculatedTotalStatOf(StatType.INTELLIGENCE)).isEqualTo(30);
        softly.assertThat(onarr.getStatRankOf(StatType.WILLPOWER)).isEqualTo(25);
        softly.assertThat(onarr.getCalculatedTotalStatOf(StatType.WILLPOWER)).isEqualTo(25);
        softly.assertThat(onarr.getStatRankOf(StatType.DEXTERITY)).isEqualTo(23);
        softly.assertThat(onarr.getCalculatedTotalStatOf(StatType.DEXTERITY)).isEqualTo(17);
        softly.assertThat(onarr.getStatRankOf(StatType.AGILITY)).isEqualTo(15);
        softly.assertThat(onarr.getCalculatedTotalStatOf(StatType.AGILITY)).isEqualTo(15);
        softly.assertThat(onarr.getStatRankOf(StatType.ENDURANCE)).isEqualTo(30);
        softly.assertThat(onarr.getCalculatedTotalStatOf(StatType.ENDURANCE)).isEqualTo(30);
        softly.assertThat(onarr.getStatRankOf(StatType.STRENGTH)).isEqualTo(25);
        softly.assertThat(onarr.getCalculatedTotalStatOf(StatType.STRENGTH)).isEqualTo(25);
        softly.assertThat(onarr.getStatRankOf(StatType.STAMINA)).isEqualTo(60);
        softly.assertThat(onarr.getCalculatedTotalStatOf(StatType.STAMINA)).isEqualTo(60);

        softly.assertThat(onarr.getSkillRankOf(SkillType.ALCHEMIST)).isEqualTo(-1);
        softly.assertThat(onarr.getCalculatedTotalSkillOf(SkillType.ALCHEMIST)).isEqualTo(0);
        softly.assertThat(onarr.getSkillRankOf(SkillType.DIPLOMAT)).isEqualTo(0);
        softly.assertThat(onarr.getCalculatedTotalSkillOf(SkillType.DIPLOMAT)).isEqualTo(0);
        softly.assertThat(onarr.getSkillRankOf(SkillType.HEALER)).isEqualTo(4);
        softly.assertThat(onarr.getCalculatedTotalSkillOf(SkillType.HEALER)).isEqualTo(4);
        softly.assertThat(onarr.getSkillRankOf(SkillType.LOREMASTER)).isEqualTo(6);
        softly.assertThat(onarr.getCalculatedTotalSkillOf(SkillType.LOREMASTER)).isEqualTo(6);
        softly.assertThat(onarr.getSkillRankOf(SkillType.MECHANIC)).isEqualTo(0);
        softly.assertThat(onarr.getCalculatedTotalSkillOf(SkillType.MECHANIC)).isEqualTo(0);
        softly.assertThat(onarr.getSkillRankOf(SkillType.MERCHANT)).isEqualTo(0);
        softly.assertThat(onarr.getCalculatedTotalSkillOf(SkillType.MERCHANT)).isEqualTo(0);
        softly.assertThat(onarr.getSkillRankOf(SkillType.RANGER)).isEqualTo(0);
        softly.assertThat(onarr.getCalculatedTotalSkillOf(SkillType.RANGER)).isEqualTo(0);
        softly.assertThat(onarr.getSkillRankOf(SkillType.STEALTH)).isEqualTo(0);
        softly.assertThat(onarr.getCalculatedTotalSkillOf(SkillType.STEALTH)).isEqualTo(0);
        softly.assertThat(onarr.getSkillRankOf(SkillType.THIEF)).isEqualTo(0);
        softly.assertThat(onarr.getCalculatedTotalSkillOf(SkillType.THIEF)).isEqualTo(0);
        softly.assertThat(onarr.getSkillRankOf(SkillType.TROUBADOUR)).isEqualTo(7);
        softly.assertThat(onarr.getCalculatedTotalSkillOf(SkillType.TROUBADOUR)).isEqualTo(7);
        softly.assertThat(onarr.getSkillRankOf(SkillType.WARRIOR)).isEqualTo(9);
        softly.assertThat(onarr.getCalculatedTotalSkillOf(SkillType.WARRIOR)).isEqualTo(9);
        softly.assertThat(onarr.getSkillRankOf(SkillType.WIZARD)).isEqualTo(-1);
        softly.assertThat(onarr.getCalculatedTotalSkillOf(SkillType.WIZARD)).isEqualTo(0);

        softly.assertThat(onarr.getSkillRankOf(SkillType.HAFTED)).isEqualTo(8);
        softly.assertThat(onarr.getCalculatedTotalSkillOf(SkillType.HAFTED)).isEqualTo(8);
        softly.assertThat(onarr.getSkillRankOf(SkillType.MISSILE)).isEqualTo(-1);
        softly.assertThat(onarr.getCalculatedTotalSkillOf(SkillType.MISSILE)).isEqualTo(0);
        softly.assertThat(onarr.getSkillRankOf(SkillType.POLE)).isEqualTo(8);
        softly.assertThat(onarr.getCalculatedTotalSkillOf(SkillType.POLE)).isEqualTo(8);
        softly.assertThat(onarr.getSkillRankOf(SkillType.SHIELD)).isEqualTo(9);
        softly.assertThat(onarr.getCalculatedTotalSkillOf(SkillType.SHIELD)).isEqualTo(9);
        softly.assertThat(onarr.getSkillRankOf(SkillType.SWORD)).isEqualTo(5);
        softly.assertThat(onarr.getCalculatedTotalSkillOf(SkillType.SWORD)).isEqualTo(5);
        softly.assertThat(onarr.getSkillRankOf(SkillType.THROWN)).isEqualTo(8);
        softly.assertThat(onarr.getCalculatedTotalSkillOf(SkillType.THROWN)).isEqualTo(8);

        softly.assertThat(onarr.getCalcValueOf(InventoryGroup.WEAPON, CalcType.BASE_HIT)).isEqualTo(40);
        softly.assertThat(onarr.getCalcValueOf(InventoryGroup.WEAPON, CalcType.DAMAGE)).isEqualTo(24);
        softly.assertThat(onarr.getCalcValueOf(InventoryGroup.SHIELD, CalcType.DEFENSE)).isEqualTo(15);
        softly.assertThat(onarr.getTotalCalcOf(CalcType.PROTECTION)).isEqualTo(16);

        //////

        softly.assertThat(duilio.school).isEqualTo(SchoolType.ELEMENTAL);
        softly.assertThat(duilio.getLevel()).isEqualTo(22);
        softly.assertThat(duilio.getTotalXp()).isEqualTo(18975);
        softly.assertThat(duilio.getXpNeededForNextLevel()).isEqualTo(2645);
        softly.assertThat(duilio.getCurrentHp()).isEqualTo(122);
        softly.assertThat(duilio.getMaximumHp()).isEqualTo(122);

        softly.assertThat(duilio.getStatRankOf(StatType.INTELLIGENCE)).isEqualTo(25);
        softly.assertThat(duilio.getCalculatedTotalStatOf(StatType.INTELLIGENCE)).isEqualTo(25);
        softly.assertThat(duilio.getStatRankOf(StatType.WILLPOWER)).isEqualTo(25);
        softly.assertThat(duilio.getCalculatedTotalStatOf(StatType.WILLPOWER)).isEqualTo(25);
        softly.assertThat(duilio.getStatRankOf(StatType.DEXTERITY)).isEqualTo(30);
        softly.assertThat(duilio.getCalculatedTotalStatOf(StatType.DEXTERITY)).isEqualTo(28);
        softly.assertThat(duilio.getStatRankOf(StatType.AGILITY)).isEqualTo(20);
        softly.assertThat(duilio.getCalculatedTotalStatOf(StatType.AGILITY)).isEqualTo(20);
        softly.assertThat(duilio.getStatRankOf(StatType.ENDURANCE)).isEqualTo(25);
        softly.assertThat(duilio.getCalculatedTotalStatOf(StatType.ENDURANCE)).isEqualTo(25);
        softly.assertThat(duilio.getStatRankOf(StatType.STRENGTH)).isEqualTo(25);
        softly.assertThat(duilio.getCalculatedTotalStatOf(StatType.STRENGTH)).isEqualTo(25);
        softly.assertThat(duilio.getStatRankOf(StatType.STAMINA)).isEqualTo(75);
        softly.assertThat(duilio.getCalculatedTotalStatOf(StatType.STAMINA)).isEqualTo(75);

        softly.assertThat(duilio.getSkillRankOf(SkillType.ALCHEMIST)).isEqualTo(5);
        softly.assertThat(duilio.getCalculatedTotalSkillOf(SkillType.ALCHEMIST)).isEqualTo(5);
        softly.assertThat(duilio.getSkillRankOf(SkillType.DIPLOMAT)).isEqualTo(10);
        softly.assertThat(duilio.getCalculatedTotalSkillOf(SkillType.DIPLOMAT)).isEqualTo(10);
        softly.assertThat(duilio.getSkillRankOf(SkillType.HEALER)).isEqualTo(0);
        softly.assertThat(duilio.getCalculatedTotalSkillOf(SkillType.HEALER)).isEqualTo(0);
        softly.assertThat(duilio.getSkillRankOf(SkillType.LOREMASTER)).isEqualTo(5);
        softly.assertThat(duilio.getCalculatedTotalSkillOf(SkillType.LOREMASTER)).isEqualTo(5);
        softly.assertThat(duilio.getSkillRankOf(SkillType.MECHANIC)).isEqualTo(0);
        softly.assertThat(duilio.getCalculatedTotalSkillOf(SkillType.MECHANIC)).isEqualTo(0);
        softly.assertThat(duilio.getSkillRankOf(SkillType.MERCHANT)).isEqualTo(5);
        softly.assertThat(duilio.getCalculatedTotalSkillOf(SkillType.MERCHANT)).isEqualTo(5);
        softly.assertThat(duilio.getSkillRankOf(SkillType.RANGER)).isEqualTo(5);
        softly.assertThat(duilio.getCalculatedTotalSkillOf(SkillType.RANGER)).isEqualTo(5);
        softly.assertThat(duilio.getSkillRankOf(SkillType.STEALTH)).isEqualTo(5);
        softly.assertThat(duilio.getCalculatedTotalSkillOf(SkillType.STEALTH)).isEqualTo(0);
        softly.assertThat(duilio.getSkillRankOf(SkillType.THIEF)).isEqualTo(5);
        softly.assertThat(duilio.getCalculatedTotalSkillOf(SkillType.THIEF)).isEqualTo(5);
        softly.assertThat(duilio.getSkillRankOf(SkillType.TROUBADOUR)).isEqualTo(10);
        softly.assertThat(duilio.getCalculatedTotalSkillOf(SkillType.TROUBADOUR)).isEqualTo(10);
        softly.assertThat(duilio.getSkillRankOf(SkillType.WARRIOR)).isEqualTo(10);
        softly.assertThat(duilio.getCalculatedTotalSkillOf(SkillType.WARRIOR)).isEqualTo(10);
        softly.assertThat(duilio.getSkillRankOf(SkillType.WIZARD)).isEqualTo(10);
        softly.assertThat(duilio.getCalculatedTotalSkillOf(SkillType.WIZARD)).isEqualTo(10);

        softly.assertThat(duilio.getSkillRankOf(SkillType.HAFTED)).isEqualTo(10);
        softly.assertThat(duilio.getCalculatedTotalSkillOf(SkillType.HAFTED)).isEqualTo(10);
        softly.assertThat(duilio.getSkillRankOf(SkillType.MISSILE)).isEqualTo(-1);
        softly.assertThat(duilio.getCalculatedTotalSkillOf(SkillType.MISSILE)).isEqualTo(0);
        softly.assertThat(duilio.getSkillRankOf(SkillType.POLE)).isEqualTo(10);
        softly.assertThat(duilio.getCalculatedTotalSkillOf(SkillType.POLE)).isEqualTo(10);
        softly.assertThat(duilio.getSkillRankOf(SkillType.SHIELD)).isEqualTo(10);
        softly.assertThat(duilio.getCalculatedTotalSkillOf(SkillType.SHIELD)).isEqualTo(10);
        softly.assertThat(duilio.getSkillRankOf(SkillType.SWORD)).isEqualTo(10);
        softly.assertThat(duilio.getCalculatedTotalSkillOf(SkillType.SWORD)).isEqualTo(10);
        softly.assertThat(duilio.getSkillRankOf(SkillType.THROWN)).isEqualTo(10);
        softly.assertThat(duilio.getCalculatedTotalSkillOf(SkillType.THROWN)).isEqualTo(10);

        softly.assertThat(duilio.getCalcValueOf(InventoryGroup.WEAPON, CalcType.BASE_HIT)).isEqualTo(60);
        softly.assertThat(duilio.getCalcValueOf(InventoryGroup.WEAPON, CalcType.DAMAGE)).isEqualTo(17);
        softly.assertThat(duilio.getCalcValueOf(InventoryGroup.SHIELD, CalcType.DEFENSE)).isEqualTo(15);
        softly.assertThat(duilio.getTotalCalcOf(CalcType.PROTECTION)).isEqualTo(10);

        //////

        softly.assertThat(iellwen.school).isEqualTo(SchoolType.STAR);
        softly.assertThat(iellwen.getLevel()).isEqualTo(20);
        softly.assertThat(iellwen.getTotalXp()).isEqualTo(14350);
        softly.assertThat(iellwen.getXpNeededForNextLevel()).isEqualTo(2205);
        softly.assertThat(iellwen.getCurrentHp()).isEqualTo(110);
        softly.assertThat(iellwen.getMaximumHp()).isEqualTo(110);

        softly.assertThat(iellwen.getStatRankOf(StatType.INTELLIGENCE)).isEqualTo(35);
        softly.assertThat(iellwen.getCalculatedTotalStatOf(StatType.INTELLIGENCE)).isEqualTo(35);
        softly.assertThat(iellwen.getStatRankOf(StatType.WILLPOWER)).isEqualTo(25);
        softly.assertThat(iellwen.getCalculatedTotalStatOf(StatType.WILLPOWER)).isEqualTo(25);
        softly.assertThat(iellwen.getStatRankOf(StatType.DEXTERITY)).isEqualTo(30);
        softly.assertThat(iellwen.getCalculatedTotalStatOf(StatType.DEXTERITY)).isEqualTo(30);
        softly.assertThat(iellwen.getStatRankOf(StatType.AGILITY)).isEqualTo(25);
        softly.assertThat(iellwen.getCalculatedTotalStatOf(StatType.AGILITY)).isEqualTo(25);
        softly.assertThat(iellwen.getStatRankOf(StatType.ENDURANCE)).isEqualTo(30);
        softly.assertThat(iellwen.getCalculatedTotalStatOf(StatType.ENDURANCE)).isEqualTo(30);
        softly.assertThat(iellwen.getStatRankOf(StatType.STRENGTH)).isEqualTo(20);
        softly.assertThat(iellwen.getCalculatedTotalStatOf(StatType.STRENGTH)).isEqualTo(20);
        softly.assertThat(iellwen.getStatRankOf(StatType.STAMINA)).isEqualTo(60);
        softly.assertThat(iellwen.getCalculatedTotalStatOf(StatType.STAMINA)).isEqualTo(60);

        softly.assertThat(iellwen.getSkillRankOf(SkillType.ALCHEMIST)).isEqualTo(0);
        softly.assertThat(iellwen.getCalculatedTotalSkillOf(SkillType.ALCHEMIST)).isEqualTo(0);
        softly.assertThat(iellwen.getSkillRankOf(SkillType.DIPLOMAT)).isEqualTo(0);
        softly.assertThat(iellwen.getCalculatedTotalSkillOf(SkillType.DIPLOMAT)).isEqualTo(0);
        softly.assertThat(iellwen.getSkillRankOf(SkillType.HEALER)).isEqualTo(10);
        softly.assertThat(iellwen.getCalculatedTotalSkillOf(SkillType.HEALER)).isEqualTo(10);
        softly.assertThat(iellwen.getSkillRankOf(SkillType.LOREMASTER)).isEqualTo(0);
        softly.assertThat(iellwen.getCalculatedTotalSkillOf(SkillType.LOREMASTER)).isEqualTo(0);
        softly.assertThat(iellwen.getSkillRankOf(SkillType.MECHANIC)).isEqualTo(0);
        softly.assertThat(iellwen.getCalculatedTotalSkillOf(SkillType.MECHANIC)).isEqualTo(0);
        softly.assertThat(iellwen.getSkillRankOf(SkillType.MERCHANT)).isEqualTo(0);
        softly.assertThat(iellwen.getCalculatedTotalSkillOf(SkillType.MERCHANT)).isEqualTo(0);
        softly.assertThat(iellwen.getSkillRankOf(SkillType.RANGER)).isEqualTo(6);
        softly.assertThat(iellwen.getCalculatedTotalSkillOf(SkillType.RANGER)).isEqualTo(6);
        softly.assertThat(iellwen.getSkillRankOf(SkillType.STEALTH)).isEqualTo(6);
        softly.assertThat(iellwen.getCalculatedTotalSkillOf(SkillType.STEALTH)).isEqualTo(5);
        softly.assertThat(iellwen.getSkillRankOf(SkillType.THIEF)).isEqualTo(0);
        softly.assertThat(iellwen.getCalculatedTotalSkillOf(SkillType.THIEF)).isEqualTo(0);
        softly.assertThat(iellwen.getSkillRankOf(SkillType.TROUBADOUR)).isEqualTo(0);
        softly.assertThat(iellwen.getCalculatedTotalSkillOf(SkillType.TROUBADOUR)).isEqualTo(0);
        softly.assertThat(iellwen.getSkillRankOf(SkillType.WARRIOR)).isEqualTo(10);
        softly.assertThat(iellwen.getCalculatedTotalSkillOf(SkillType.WARRIOR)).isEqualTo(10);
        softly.assertThat(iellwen.getSkillRankOf(SkillType.WIZARD)).isEqualTo(8);
        softly.assertThat(iellwen.getCalculatedTotalSkillOf(SkillType.WIZARD)).isEqualTo(8);

        softly.assertThat(iellwen.getSkillRankOf(SkillType.HAFTED)).isEqualTo(5);
        softly.assertThat(iellwen.getCalculatedTotalSkillOf(SkillType.HAFTED)).isEqualTo(5);
        softly.assertThat(iellwen.getSkillRankOf(SkillType.MISSILE)).isEqualTo(7);
        softly.assertThat(iellwen.getCalculatedTotalSkillOf(SkillType.MISSILE)).isEqualTo(7);
        softly.assertThat(iellwen.getSkillRankOf(SkillType.POLE)).isEqualTo(0);
        softly.assertThat(iellwen.getCalculatedTotalSkillOf(SkillType.POLE)).isEqualTo(0);
        softly.assertThat(iellwen.getSkillRankOf(SkillType.SHIELD)).isEqualTo(-1);
        softly.assertThat(iellwen.getCalculatedTotalSkillOf(SkillType.SHIELD)).isEqualTo(0);
        softly.assertThat(iellwen.getSkillRankOf(SkillType.SWORD)).isEqualTo(10);
        softly.assertThat(iellwen.getCalculatedTotalSkillOf(SkillType.SWORD)).isEqualTo(10);
        softly.assertThat(iellwen.getSkillRankOf(SkillType.THROWN)).isEqualTo(-1);
        softly.assertThat(iellwen.getCalculatedTotalSkillOf(SkillType.THROWN)).isEqualTo(0);

        softly.assertThat(iellwen.getCalcValueOf(InventoryGroup.WEAPON, CalcType.BASE_HIT)).isEqualTo(60);
        softly.assertThat(iellwen.getCalcValueOf(InventoryGroup.WEAPON, CalcType.DAMAGE)).isEqualTo(17);
        softly.assertThat(iellwen.getCalcValueOf(InventoryGroup.SHIELD, CalcType.DEFENSE)).isEqualTo(0);
        softly.assertThat(iellwen.getTotalCalcOf(CalcType.PROTECTION)).isEqualTo(7);

        //////

        softly.assertThat(faeron.school).isEqualTo(SchoolType.NAMING);
        softly.assertThat(faeron.getLevel()).isEqualTo(25);
        softly.assertThat(faeron.getTotalXp()).isEqualTo(27625);
        softly.assertThat(faeron.getXpNeededForNextLevel()).isEqualTo(3380);
        softly.assertThat(faeron.getCurrentHp()).isEqualTo(130);
        softly.assertThat(faeron.getMaximumHp()).isEqualTo(130);

        softly.assertThat(faeron.getStatRankOf(StatType.INTELLIGENCE)).isEqualTo(30);
        softly.assertThat(faeron.getCalculatedTotalStatOf(StatType.INTELLIGENCE)).isEqualTo(30);
        softly.assertThat(faeron.getStatRankOf(StatType.WILLPOWER)).isEqualTo(30);
        softly.assertThat(faeron.getCalculatedTotalStatOf(StatType.WILLPOWER)).isEqualTo(30);
        softly.assertThat(faeron.getStatRankOf(StatType.DEXTERITY)).isEqualTo(30);
        softly.assertThat(faeron.getCalculatedTotalStatOf(StatType.DEXTERITY)).isEqualTo(30);
        softly.assertThat(faeron.getStatRankOf(StatType.AGILITY)).isEqualTo(30);
        softly.assertThat(faeron.getCalculatedTotalStatOf(StatType.AGILITY)).isEqualTo(30);
        softly.assertThat(faeron.getStatRankOf(StatType.ENDURANCE)).isEqualTo(25);
        softly.assertThat(faeron.getCalculatedTotalStatOf(StatType.ENDURANCE)).isEqualTo(25);
        softly.assertThat(faeron.getStatRankOf(StatType.STRENGTH)).isEqualTo(15);
        softly.assertThat(faeron.getCalculatedTotalStatOf(StatType.STRENGTH)).isEqualTo(15);
        softly.assertThat(faeron.getStatRankOf(StatType.STAMINA)).isEqualTo(80);
        softly.assertThat(faeron.getCalculatedTotalStatOf(StatType.STAMINA)).isEqualTo(80);

        softly.assertThat(faeron.getSkillRankOf(SkillType.ALCHEMIST)).isEqualTo(10);
        softly.assertThat(faeron.getCalculatedTotalSkillOf(SkillType.ALCHEMIST)).isEqualTo(10);
        softly.assertThat(faeron.getSkillRankOf(SkillType.DIPLOMAT)).isEqualTo(10);
        softly.assertThat(faeron.getCalculatedTotalSkillOf(SkillType.DIPLOMAT)).isEqualTo(10);
        softly.assertThat(faeron.getSkillRankOf(SkillType.HEALER)).isEqualTo(0);
        softly.assertThat(faeron.getCalculatedTotalSkillOf(SkillType.HEALER)).isEqualTo(0);
        softly.assertThat(faeron.getSkillRankOf(SkillType.LOREMASTER)).isEqualTo(10);
        softly.assertThat(faeron.getCalculatedTotalSkillOf(SkillType.LOREMASTER)).isEqualTo(10);
        softly.assertThat(faeron.getSkillRankOf(SkillType.MECHANIC)).isEqualTo(0);
        softly.assertThat(faeron.getCalculatedTotalSkillOf(SkillType.MECHANIC)).isEqualTo(0);
        softly.assertThat(faeron.getSkillRankOf(SkillType.MERCHANT)).isEqualTo(10);
        softly.assertThat(faeron.getCalculatedTotalSkillOf(SkillType.MERCHANT)).isEqualTo(10);
        softly.assertThat(faeron.getSkillRankOf(SkillType.RANGER)).isEqualTo(10);
        softly.assertThat(faeron.getCalculatedTotalSkillOf(SkillType.RANGER)).isEqualTo(10);
        softly.assertThat(faeron.getSkillRankOf(SkillType.STEALTH)).isEqualTo(10);
        softly.assertThat(faeron.getCalculatedTotalSkillOf(SkillType.STEALTH)).isEqualTo(10);
        softly.assertThat(faeron.getSkillRankOf(SkillType.THIEF)).isEqualTo(10);
        softly.assertThat(faeron.getCalculatedTotalSkillOf(SkillType.THIEF)).isEqualTo(10);
        softly.assertThat(faeron.getSkillRankOf(SkillType.TROUBADOUR)).isEqualTo(10);
        softly.assertThat(faeron.getCalculatedTotalSkillOf(SkillType.TROUBADOUR)).isEqualTo(10);
        softly.assertThat(faeron.getSkillRankOf(SkillType.WARRIOR)).isEqualTo(10);
        softly.assertThat(faeron.getCalculatedTotalSkillOf(SkillType.WARRIOR)).isEqualTo(10);
        softly.assertThat(faeron.getSkillRankOf(SkillType.WIZARD)).isEqualTo(-1);
        softly.assertThat(faeron.getCalculatedTotalSkillOf(SkillType.WIZARD)).isEqualTo(0);

        softly.assertThat(faeron.getSkillRankOf(SkillType.HAFTED)).isEqualTo(10);
        softly.assertThat(faeron.getCalculatedTotalSkillOf(SkillType.HAFTED)).isEqualTo(10);
        softly.assertThat(faeron.getSkillRankOf(SkillType.MISSILE)).isEqualTo(-1);
        softly.assertThat(faeron.getCalculatedTotalSkillOf(SkillType.MISSILE)).isEqualTo(0);
        softly.assertThat(faeron.getSkillRankOf(SkillType.POLE)).isEqualTo(-1);
        softly.assertThat(faeron.getCalculatedTotalSkillOf(SkillType.POLE)).isEqualTo(0);
        softly.assertThat(faeron.getSkillRankOf(SkillType.SHIELD)).isEqualTo(0);
        softly.assertThat(faeron.getCalculatedTotalSkillOf(SkillType.SHIELD)).isEqualTo(0);
        softly.assertThat(faeron.getSkillRankOf(SkillType.SWORD)).isEqualTo(10);
        softly.assertThat(faeron.getCalculatedTotalSkillOf(SkillType.SWORD)).isEqualTo(10);
        softly.assertThat(faeron.getSkillRankOf(SkillType.THROWN)).isEqualTo(-1);
        softly.assertThat(faeron.getCalculatedTotalSkillOf(SkillType.THROWN)).isEqualTo(0);

        softly.assertThat(faeron.getCalcValueOf(InventoryGroup.WEAPON, CalcType.BASE_HIT)).isEqualTo(60);
        softly.assertThat(faeron.getCalcValueOf(InventoryGroup.WEAPON, CalcType.DAMAGE)).isEqualTo(24);
        softly.assertThat(faeron.getCalcValueOf(InventoryGroup.SHIELD, CalcType.DEFENSE)).isEqualTo(0);
        softly.assertThat(faeron.getTotalCalcOf(CalcType.PROTECTION)).isEqualTo(4);

        //////

        softly.assertAll();
    }

}
