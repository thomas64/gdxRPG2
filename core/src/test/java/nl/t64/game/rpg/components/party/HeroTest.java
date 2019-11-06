package nl.t64.game.rpg.components.party;

import nl.t64.game.rpg.GameData;
import nl.t64.game.rpg.GameTest;
import nl.t64.game.rpg.constants.Constant;
import nl.t64.game.rpg.profile.ProfileManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
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
        final InventoryItem chest = InventoryDatabase.getInstance().getInventoryItem("basic_light_chest");

        final Optional<String> message1 = mozes.isAbleToEquip(legendaryStaff);
        assertThat(message1.get()).isEqualToIgnoringNewLines("Mozes needs the Pole skill\nto equip that Legendary Staff.");

        final Optional<String> message2 = ryiah.isAbleToEquip(legendaryStaff);
        assertThat(message2.get()).isEqualToIgnoringNewLines("Ryiah needs 30 Intelligence\nto equip that Legendary Staff.");

        final Optional<String> message3 = ryiah.isAbleToEquip(masterworkLance);
        assertThat(message3.get()).isEqualToIgnoringNewLines("Ryiah needs 20 Strength\nto equip that Masterwork Lance.");

        final Optional<String> message4 = mozes.isAbleToEquip(basicDagger);
        assertThat(message4).isEmpty();

        final Optional<String> message5 = mozes.isAbleToEquip(chest);
        assertThat(message5).isEmpty();
    }

    @Test
    void whenHeroesAreCreated_MozesShouldHaveRightStats() {
        final HeroItem mozes = party.getHero("mozes");
        assertThat(party.contains(mozes)).isTrue();

        assertThat(mozes.school).isEqualTo(SchoolType.UNKNOWN);
        assertThat(mozes.getLevel()).isEqualTo(1);
        assertThat(mozes.getTotalXp()).isEqualTo(5);
        assertThat(mozes.getXpNeededForNextLevel()).isEqualTo(20);
        assertThat(mozes.getCurrentHp()).isEqualTo(46);
        assertThat(mozes.getMaximumHp()).isEqualTo(46);

        assertThat(mozes.getStatRankOf(StatItemId.INTELLIGENCE)).isEqualTo(18);
        assertThat(mozes.getCalculatedTotalStatOf(StatItemId.INTELLIGENCE)).isEqualTo(18);
        assertThat(mozes.getStatRankOf(StatItemId.WILLPOWER)).isEqualTo(12);
        assertThat(mozes.getCalculatedTotalStatOf(StatItemId.WILLPOWER)).isEqualTo(12);
        assertThat(mozes.getStatRankOf(StatItemId.DEXTERITY)).isEqualTo(15);
        assertThat(mozes.getCalculatedTotalStatOf(StatItemId.DEXTERITY)).isEqualTo(13);
        assertThat(mozes.getStatRankOf(StatItemId.AGILITY)).isEqualTo(15);
        assertThat(mozes.getCalculatedTotalStatOf(StatItemId.AGILITY)).isEqualTo(15);
        assertThat(mozes.getStatRankOf(StatItemId.ENDURANCE)).isEqualTo(15);
        assertThat(mozes.getCalculatedTotalStatOf(StatItemId.ENDURANCE)).isEqualTo(15);
        assertThat(mozes.getStatRankOf(StatItemId.STRENGTH)).isEqualTo(15);
        assertThat(mozes.getCalculatedTotalStatOf(StatItemId.STRENGTH)).isEqualTo(15);
        assertThat(mozes.getStatRankOf(StatItemId.STAMINA)).isEqualTo(30);
        assertThat(mozes.getCalculatedTotalStatOf(StatItemId.STAMINA)).isEqualTo(30);

        assertThat(mozes.getSkillRankOf(SkillItemId.ALCHEMIST)).isEqualTo(0);
        assertThat(mozes.getCalculatedTotalSkillOf(SkillItemId.ALCHEMIST)).isEqualTo(0);
        assertThat(mozes.getSkillRankOf(SkillItemId.DIPLOMAT)).isEqualTo(0);
        assertThat(mozes.getCalculatedTotalSkillOf(SkillItemId.DIPLOMAT)).isEqualTo(0);
        assertThat(mozes.getSkillRankOf(SkillItemId.HEALER)).isEqualTo(0);
        assertThat(mozes.getCalculatedTotalSkillOf(SkillItemId.HEALER)).isEqualTo(0);
        assertThat(mozes.getSkillRankOf(SkillItemId.LOREMASTER)).isEqualTo(0);
        assertThat(mozes.getCalculatedTotalSkillOf(SkillItemId.LOREMASTER)).isEqualTo(0);
        assertThat(mozes.getSkillRankOf(SkillItemId.MECHANIC)).isEqualTo(0);
        assertThat(mozes.getCalculatedTotalSkillOf(SkillItemId.MECHANIC)).isEqualTo(0);
        assertThat(mozes.getSkillRankOf(SkillItemId.MERCHANT)).isEqualTo(0);
        assertThat(mozes.getCalculatedTotalSkillOf(SkillItemId.MERCHANT)).isEqualTo(0);
        assertThat(mozes.getSkillRankOf(SkillItemId.RANGER)).isEqualTo(0);
        assertThat(mozes.getCalculatedTotalSkillOf(SkillItemId.RANGER)).isEqualTo(0);
        assertThat(mozes.getSkillRankOf(SkillItemId.STEALTH)).isEqualTo(1);
        assertThat(mozes.getCalculatedTotalSkillOf(SkillItemId.STEALTH)).isEqualTo(0);
        assertThat(mozes.getSkillRankOf(SkillItemId.THIEF)).isEqualTo(0);
        assertThat(mozes.getCalculatedTotalSkillOf(SkillItemId.THIEF)).isEqualTo(0);
        assertThat(mozes.getSkillRankOf(SkillItemId.TROUBADOUR)).isEqualTo(1);
        assertThat(mozes.getCalculatedTotalSkillOf(SkillItemId.TROUBADOUR)).isEqualTo(1);
        assertThat(mozes.getSkillRankOf(SkillItemId.WARRIOR)).isEqualTo(3);
        assertThat(mozes.getCalculatedTotalSkillOf(SkillItemId.WARRIOR)).isEqualTo(3);
        assertThat(mozes.getSkillRankOf(SkillItemId.WIZARD)).isEqualTo(1);
        assertThat(mozes.getCalculatedTotalSkillOf(SkillItemId.WIZARD)).isEqualTo(1);

        assertThat(mozes.getSkillRankOf(SkillItemId.HAFTED)).isEqualTo(1);
        assertThat(mozes.getCalculatedTotalSkillOf(SkillItemId.HAFTED)).isEqualTo(1);
        assertThat(mozes.getSkillRankOf(SkillItemId.MISSILE)).isEqualTo(3);
        assertThat(mozes.getCalculatedTotalSkillOf(SkillItemId.MISSILE)).isEqualTo(3);
        assertThat(mozes.getSkillRankOf(SkillItemId.POLE)).isEqualTo(0);
        assertThat(mozes.getCalculatedTotalSkillOf(SkillItemId.POLE)).isEqualTo(0);
        assertThat(mozes.getSkillRankOf(SkillItemId.SHIELD)).isEqualTo(3);
        assertThat(mozes.getCalculatedTotalSkillOf(SkillItemId.SHIELD)).isEqualTo(3);
        assertThat(mozes.getSkillRankOf(SkillItemId.SWORD)).isEqualTo(3);
        assertThat(mozes.getCalculatedTotalSkillOf(SkillItemId.SWORD)).isEqualTo(3);
        assertThat(mozes.getSkillRankOf(SkillItemId.THROWN)).isEqualTo(0);
        assertThat(mozes.getCalculatedTotalSkillOf(SkillItemId.THROWN)).isEqualTo(0);

        assertThat(mozes.getCalcValueOf(InventoryGroup.WEAPON, CalcAttributeId.BASE_HIT)).isEqualTo(40);
        assertThat(mozes.getCalcValueOf(InventoryGroup.WEAPON, CalcAttributeId.DAMAGE)).isEqualTo(13);
        assertThat(mozes.getCalcValueOf(InventoryGroup.SHIELD, CalcAttributeId.DEFENSE)).isEqualTo(5);
        assertThat(mozes.getTotalCalcOf(CalcAttributeId.PROTECTION)).isEqualTo(6);
    }

    @Test
    void whenHeroesAreCreated_LuanaShouldHaveRightStats() {
        final HeroItem luana = heroes.getHero("luana");
        assertThat(party.contains(luana)).isFalse();

        assertThat(luana.school).isEqualTo(SchoolType.ELEMENTAL);
        assertThat(luana.getLevel()).isEqualTo(1);
        assertThat(luana.getTotalXp()).isEqualTo(5);
        assertThat(luana.getXpNeededForNextLevel()).isEqualTo(20);
        assertThat(luana.getCurrentHp()).isEqualTo(31);
        assertThat(luana.getMaximumHp()).isEqualTo(31);

        assertThat(luana.getStatRankOf(StatItemId.INTELLIGENCE)).isEqualTo(14);
        assertThat(luana.getCalculatedTotalStatOf(StatItemId.INTELLIGENCE)).isEqualTo(14);
        assertThat(luana.getStatRankOf(StatItemId.WILLPOWER)).isEqualTo(10);
        assertThat(luana.getCalculatedTotalStatOf(StatItemId.WILLPOWER)).isEqualTo(10);
        assertThat(luana.getStatRankOf(StatItemId.DEXTERITY)).isEqualTo(22);
        assertThat(luana.getCalculatedTotalStatOf(StatItemId.DEXTERITY)).isEqualTo(22);
        assertThat(luana.getStatRankOf(StatItemId.AGILITY)).isEqualTo(20);
        assertThat(luana.getCalculatedTotalStatOf(StatItemId.AGILITY)).isEqualTo(20);
        assertThat(luana.getStatRankOf(StatItemId.ENDURANCE)).isEqualTo(10);
        assertThat(luana.getCalculatedTotalStatOf(StatItemId.ENDURANCE)).isEqualTo(10);
        assertThat(luana.getStatRankOf(StatItemId.STRENGTH)).isEqualTo(8);
        assertThat(luana.getCalculatedTotalStatOf(StatItemId.STRENGTH)).isEqualTo(8);
        assertThat(luana.getStatRankOf(StatItemId.STAMINA)).isEqualTo(20);
        assertThat(luana.getCalculatedTotalStatOf(StatItemId.STAMINA)).isEqualTo(20);

        assertThat(luana.getSkillRankOf(SkillItemId.ALCHEMIST)).isEqualTo(0);
        assertThat(luana.getCalculatedTotalSkillOf(SkillItemId.ALCHEMIST)).isEqualTo(0);
        assertThat(luana.getSkillRankOf(SkillItemId.DIPLOMAT)).isEqualTo(0);
        assertThat(luana.getCalculatedTotalSkillOf(SkillItemId.DIPLOMAT)).isEqualTo(0);
        assertThat(luana.getSkillRankOf(SkillItemId.HEALER)).isEqualTo(0);
        assertThat(luana.getCalculatedTotalSkillOf(SkillItemId.HEALER)).isEqualTo(0);
        assertThat(luana.getSkillRankOf(SkillItemId.LOREMASTER)).isEqualTo(0);
        assertThat(luana.getCalculatedTotalSkillOf(SkillItemId.LOREMASTER)).isEqualTo(0);
        assertThat(luana.getSkillRankOf(SkillItemId.MECHANIC)).isEqualTo(1);
        assertThat(luana.getCalculatedTotalSkillOf(SkillItemId.MECHANIC)).isEqualTo(1);
        assertThat(luana.getSkillRankOf(SkillItemId.MERCHANT)).isEqualTo(0);
        assertThat(luana.getCalculatedTotalSkillOf(SkillItemId.MERCHANT)).isEqualTo(0);
        assertThat(luana.getSkillRankOf(SkillItemId.RANGER)).isEqualTo(0);
        assertThat(luana.getCalculatedTotalSkillOf(SkillItemId.RANGER)).isEqualTo(0);
        assertThat(luana.getSkillRankOf(SkillItemId.STEALTH)).isEqualTo(3);
        assertThat(luana.getCalculatedTotalSkillOf(SkillItemId.STEALTH)).isEqualTo(3);
        assertThat(luana.getSkillRankOf(SkillItemId.THIEF)).isEqualTo(3);
        assertThat(luana.getCalculatedTotalSkillOf(SkillItemId.THIEF)).isEqualTo(3);
        assertThat(luana.getSkillRankOf(SkillItemId.TROUBADOUR)).isEqualTo(0);
        assertThat(luana.getCalculatedTotalSkillOf(SkillItemId.TROUBADOUR)).isEqualTo(0);
        assertThat(luana.getSkillRankOf(SkillItemId.WARRIOR)).isEqualTo(0);
        assertThat(luana.getCalculatedTotalSkillOf(SkillItemId.WARRIOR)).isEqualTo(0);
        assertThat(luana.getSkillRankOf(SkillItemId.WIZARD)).isEqualTo(0);
        assertThat(luana.getCalculatedTotalSkillOf(SkillItemId.WIZARD)).isEqualTo(0);

        assertThat(luana.getSkillRankOf(SkillItemId.HAFTED)).isEqualTo(-1);
        assertThat(luana.getCalculatedTotalSkillOf(SkillItemId.HAFTED)).isEqualTo(0);
        assertThat(luana.getSkillRankOf(SkillItemId.MISSILE)).isEqualTo(-1);
        assertThat(luana.getCalculatedTotalSkillOf(SkillItemId.MISSILE)).isEqualTo(0);
        assertThat(luana.getSkillRankOf(SkillItemId.POLE)).isEqualTo(0);
        assertThat(luana.getCalculatedTotalSkillOf(SkillItemId.POLE)).isEqualTo(0);
        assertThat(luana.getSkillRankOf(SkillItemId.SHIELD)).isEqualTo(-1);
        assertThat(luana.getCalculatedTotalSkillOf(SkillItemId.SHIELD)).isEqualTo(0);
        assertThat(luana.getSkillRankOf(SkillItemId.SWORD)).isEqualTo(1);
        assertThat(luana.getCalculatedTotalSkillOf(SkillItemId.SWORD)).isEqualTo(1);
        assertThat(luana.getSkillRankOf(SkillItemId.THROWN)).isEqualTo(2);
        assertThat(luana.getCalculatedTotalSkillOf(SkillItemId.THROWN)).isEqualTo(2);

        assertThat(luana.getCalcValueOf(InventoryGroup.WEAPON, CalcAttributeId.BASE_HIT)).isEqualTo(40);
        assertThat(luana.getCalcValueOf(InventoryGroup.WEAPON, CalcAttributeId.DAMAGE)).isEqualTo(11);
        assertThat(luana.getCalcValueOf(InventoryGroup.SHIELD, CalcAttributeId.DEFENSE)).isEqualTo(0);
        assertThat(luana.getTotalCalcOf(CalcAttributeId.PROTECTION)).isEqualTo(1);
    }

    @Test
    void whenHeroesAreCreated_ReignaldShouldHaveRightStats() {
        final HeroItem reignald = heroes.getHero("reignald");
        assertThat(party.contains(reignald)).isFalse();

        assertThat(reignald.school).isEqualTo(SchoolType.NONE);
        assertThat(reignald.getLevel()).isEqualTo(8);
        assertThat(reignald.getTotalXp()).isEqualTo(1020);
        assertThat(reignald.getXpNeededForNextLevel()).isEqualTo(405);
        assertThat(reignald.getCurrentHp()).isEqualTo(68);
        assertThat(reignald.getMaximumHp()).isEqualTo(68);

        assertThat(reignald.getStatRankOf(StatItemId.INTELLIGENCE)).isEqualTo(10);
        assertThat(reignald.getCalculatedTotalStatOf(StatItemId.INTELLIGENCE)).isEqualTo(10);
        assertThat(reignald.getStatRankOf(StatItemId.WILLPOWER)).isEqualTo(8);
        assertThat(reignald.getCalculatedTotalStatOf(StatItemId.WILLPOWER)).isEqualTo(8);
        assertThat(reignald.getStatRankOf(StatItemId.DEXTERITY)).isEqualTo(25);
        assertThat(reignald.getCalculatedTotalStatOf(StatItemId.DEXTERITY)).isEqualTo(23);
        assertThat(reignald.getStatRankOf(StatItemId.AGILITY)).isEqualTo(10);
        assertThat(reignald.getCalculatedTotalStatOf(StatItemId.AGILITY)).isEqualTo(10);
        assertThat(reignald.getStatRankOf(StatItemId.ENDURANCE)).isEqualTo(20);
        assertThat(reignald.getCalculatedTotalStatOf(StatItemId.ENDURANCE)).isEqualTo(20);
        assertThat(reignald.getStatRankOf(StatItemId.STRENGTH)).isEqualTo(20);
        assertThat(reignald.getCalculatedTotalStatOf(StatItemId.STRENGTH)).isEqualTo(20);
        assertThat(reignald.getStatRankOf(StatItemId.STAMINA)).isEqualTo(40);
        assertThat(reignald.getCalculatedTotalStatOf(StatItemId.STAMINA)).isEqualTo(40);

        assertThat(reignald.getSkillRankOf(SkillItemId.ALCHEMIST)).isEqualTo(-1);
        assertThat(reignald.getCalculatedTotalSkillOf(SkillItemId.ALCHEMIST)).isEqualTo(0);
        assertThat(reignald.getSkillRankOf(SkillItemId.DIPLOMAT)).isEqualTo(0);
        assertThat(reignald.getCalculatedTotalSkillOf(SkillItemId.DIPLOMAT)).isEqualTo(0);
        assertThat(reignald.getSkillRankOf(SkillItemId.HEALER)).isEqualTo(0);
        assertThat(reignald.getCalculatedTotalSkillOf(SkillItemId.HEALER)).isEqualTo(0);
        assertThat(reignald.getSkillRankOf(SkillItemId.LOREMASTER)).isEqualTo(0);
        assertThat(reignald.getCalculatedTotalSkillOf(SkillItemId.LOREMASTER)).isEqualTo(0);
        assertThat(reignald.getSkillRankOf(SkillItemId.MECHANIC)).isEqualTo(0);
        assertThat(reignald.getCalculatedTotalSkillOf(SkillItemId.MECHANIC)).isEqualTo(0);
        assertThat(reignald.getSkillRankOf(SkillItemId.MERCHANT)).isEqualTo(0);
        assertThat(reignald.getCalculatedTotalSkillOf(SkillItemId.MERCHANT)).isEqualTo(0);
        assertThat(reignald.getSkillRankOf(SkillItemId.RANGER)).isEqualTo(0);
        assertThat(reignald.getCalculatedTotalSkillOf(SkillItemId.RANGER)).isEqualTo(0);
        assertThat(reignald.getSkillRankOf(SkillItemId.STEALTH)).isEqualTo(1);
        assertThat(reignald.getCalculatedTotalSkillOf(SkillItemId.STEALTH)).isEqualTo(0);
        assertThat(reignald.getSkillRankOf(SkillItemId.THIEF)).isEqualTo(-1);
        assertThat(reignald.getCalculatedTotalSkillOf(SkillItemId.THIEF)).isEqualTo(0);
        assertThat(reignald.getSkillRankOf(SkillItemId.TROUBADOUR)).isEqualTo(0);
        assertThat(reignald.getCalculatedTotalSkillOf(SkillItemId.TROUBADOUR)).isEqualTo(0);
        assertThat(reignald.getSkillRankOf(SkillItemId.WARRIOR)).isEqualTo(4);
        assertThat(reignald.getCalculatedTotalSkillOf(SkillItemId.WARRIOR)).isEqualTo(4);
        assertThat(reignald.getSkillRankOf(SkillItemId.WIZARD)).isEqualTo(-1);
        assertThat(reignald.getCalculatedTotalSkillOf(SkillItemId.WIZARD)).isEqualTo(0);

        assertThat(reignald.getSkillRankOf(SkillItemId.HAFTED)).isEqualTo(0);
        assertThat(reignald.getCalculatedTotalSkillOf(SkillItemId.HAFTED)).isEqualTo(0);
        assertThat(reignald.getSkillRankOf(SkillItemId.MISSILE)).isEqualTo(-1);
        assertThat(reignald.getCalculatedTotalSkillOf(SkillItemId.MISSILE)).isEqualTo(0);
        assertThat(reignald.getSkillRankOf(SkillItemId.POLE)).isEqualTo(0);
        assertThat(reignald.getCalculatedTotalSkillOf(SkillItemId.POLE)).isEqualTo(0);
        assertThat(reignald.getSkillRankOf(SkillItemId.SHIELD)).isEqualTo(2);
        assertThat(reignald.getCalculatedTotalSkillOf(SkillItemId.SHIELD)).isEqualTo(2);
        assertThat(reignald.getSkillRankOf(SkillItemId.SWORD)).isEqualTo(4);
        assertThat(reignald.getCalculatedTotalSkillOf(SkillItemId.SWORD)).isEqualTo(4);
        assertThat(reignald.getSkillRankOf(SkillItemId.THROWN)).isEqualTo(2);
        assertThat(reignald.getCalculatedTotalSkillOf(SkillItemId.THROWN)).isEqualTo(2);

        assertThat(reignald.getCalcValueOf(InventoryGroup.WEAPON, CalcAttributeId.BASE_HIT)).isEqualTo(50);
        assertThat(reignald.getCalcValueOf(InventoryGroup.WEAPON, CalcAttributeId.DAMAGE)).isEqualTo(15);
        assertThat(reignald.getCalcValueOf(InventoryGroup.SHIELD, CalcAttributeId.DEFENSE)).isEqualTo(10);
        assertThat(reignald.getTotalCalcOf(CalcAttributeId.PROTECTION)).isEqualTo(8);
    }

    @Test
    void whenHeroesAreCreated_RyiahShouldHaveRightStats() {
        final HeroItem ryiah = heroes.getHero("ryiah");
        assertThat(party.contains(ryiah)).isFalse();

        assertThat(ryiah.school).isEqualTo(SchoolType.NAMING);
        assertThat(ryiah.getLevel()).isEqualTo(3);
        assertThat(ryiah.getTotalXp()).isEqualTo(70);
        assertThat(ryiah.getXpNeededForNextLevel()).isEqualTo(80);
        assertThat(ryiah.getCurrentHp()).isEqualTo(50);
        assertThat(ryiah.getMaximumHp()).isEqualTo(50);

        assertThat(ryiah.getStatRankOf(StatItemId.INTELLIGENCE)).isEqualTo(22);
        assertThat(ryiah.getCalculatedTotalStatOf(StatItemId.INTELLIGENCE)).isEqualTo(22);
        assertThat(ryiah.getStatRankOf(StatItemId.WILLPOWER)).isEqualTo(16);
        assertThat(ryiah.getCalculatedTotalStatOf(StatItemId.WILLPOWER)).isEqualTo(16);
        assertThat(ryiah.getStatRankOf(StatItemId.DEXTERITY)).isEqualTo(20);
        assertThat(ryiah.getCalculatedTotalStatOf(StatItemId.DEXTERITY)).isEqualTo(20);
        assertThat(ryiah.getStatRankOf(StatItemId.AGILITY)).isEqualTo(15);
        assertThat(ryiah.getCalculatedTotalStatOf(StatItemId.AGILITY)).isEqualTo(15);
        assertThat(ryiah.getStatRankOf(StatItemId.ENDURANCE)).isEqualTo(16);
        assertThat(ryiah.getCalculatedTotalStatOf(StatItemId.ENDURANCE)).isEqualTo(16);
        assertThat(ryiah.getStatRankOf(StatItemId.STRENGTH)).isEqualTo(10);
        assertThat(ryiah.getCalculatedTotalStatOf(StatItemId.STRENGTH)).isEqualTo(10);
        assertThat(ryiah.getStatRankOf(StatItemId.STAMINA)).isEqualTo(31);
        assertThat(ryiah.getCalculatedTotalStatOf(StatItemId.STAMINA)).isEqualTo(31);

        assertThat(ryiah.getSkillRankOf(SkillItemId.ALCHEMIST)).isEqualTo(0);
        assertThat(ryiah.getCalculatedTotalSkillOf(SkillItemId.ALCHEMIST)).isEqualTo(0);
        assertThat(ryiah.getSkillRankOf(SkillItemId.DIPLOMAT)).isEqualTo(0);
        assertThat(ryiah.getCalculatedTotalSkillOf(SkillItemId.DIPLOMAT)).isEqualTo(0);
        assertThat(ryiah.getSkillRankOf(SkillItemId.HEALER)).isEqualTo(0);
        assertThat(ryiah.getCalculatedTotalSkillOf(SkillItemId.HEALER)).isEqualTo(0);
        assertThat(ryiah.getSkillRankOf(SkillItemId.LOREMASTER)).isEqualTo(1);
        assertThat(ryiah.getCalculatedTotalSkillOf(SkillItemId.LOREMASTER)).isEqualTo(1);
        assertThat(ryiah.getSkillRankOf(SkillItemId.MECHANIC)).isEqualTo(0);
        assertThat(ryiah.getCalculatedTotalSkillOf(SkillItemId.MECHANIC)).isEqualTo(0);
        assertThat(ryiah.getSkillRankOf(SkillItemId.MERCHANT)).isEqualTo(1);
        assertThat(ryiah.getCalculatedTotalSkillOf(SkillItemId.MERCHANT)).isEqualTo(1);
        assertThat(ryiah.getSkillRankOf(SkillItemId.RANGER)).isEqualTo(0);
        assertThat(ryiah.getCalculatedTotalSkillOf(SkillItemId.RANGER)).isEqualTo(0);
        assertThat(ryiah.getSkillRankOf(SkillItemId.STEALTH)).isEqualTo(0);
        assertThat(ryiah.getCalculatedTotalSkillOf(SkillItemId.STEALTH)).isEqualTo(0);
        assertThat(ryiah.getSkillRankOf(SkillItemId.THIEF)).isEqualTo(0);
        assertThat(ryiah.getCalculatedTotalSkillOf(SkillItemId.THIEF)).isEqualTo(0);
        assertThat(ryiah.getSkillRankOf(SkillItemId.TROUBADOUR)).isEqualTo(0);
        assertThat(ryiah.getCalculatedTotalSkillOf(SkillItemId.TROUBADOUR)).isEqualTo(0);
        assertThat(ryiah.getSkillRankOf(SkillItemId.WARRIOR)).isEqualTo(0);
        assertThat(ryiah.getCalculatedTotalSkillOf(SkillItemId.WARRIOR)).isEqualTo(0);
        assertThat(ryiah.getSkillRankOf(SkillItemId.WIZARD)).isEqualTo(4);
        assertThat(ryiah.getCalculatedTotalSkillOf(SkillItemId.WIZARD)).isEqualTo(4);

        assertThat(ryiah.getSkillRankOf(SkillItemId.HAFTED)).isEqualTo(0);
        assertThat(ryiah.getCalculatedTotalSkillOf(SkillItemId.HAFTED)).isEqualTo(0);
        assertThat(ryiah.getSkillRankOf(SkillItemId.MISSILE)).isEqualTo(-1);
        assertThat(ryiah.getCalculatedTotalSkillOf(SkillItemId.MISSILE)).isEqualTo(0);
        assertThat(ryiah.getSkillRankOf(SkillItemId.POLE)).isEqualTo(3);
        assertThat(ryiah.getCalculatedTotalSkillOf(SkillItemId.POLE)).isEqualTo(3);
        assertThat(ryiah.getSkillRankOf(SkillItemId.SHIELD)).isEqualTo(0);
        assertThat(ryiah.getCalculatedTotalSkillOf(SkillItemId.SHIELD)).isEqualTo(0);
        assertThat(ryiah.getSkillRankOf(SkillItemId.SWORD)).isEqualTo(3);
        assertThat(ryiah.getCalculatedTotalSkillOf(SkillItemId.SWORD)).isEqualTo(3);
        assertThat(ryiah.getSkillRankOf(SkillItemId.THROWN)).isEqualTo(-1);
        assertThat(ryiah.getCalculatedTotalSkillOf(SkillItemId.THROWN)).isEqualTo(0);

        assertThat(ryiah.getCalcValueOf(InventoryGroup.WEAPON, CalcAttributeId.BASE_HIT)).isEqualTo(50);
        assertThat(ryiah.getCalcValueOf(InventoryGroup.WEAPON, CalcAttributeId.DAMAGE)).isEqualTo(4);
        assertThat(ryiah.getCalcValueOf(InventoryGroup.SHIELD, CalcAttributeId.DEFENSE)).isEqualTo(0);
        assertThat(ryiah.getTotalCalcOf(CalcAttributeId.PROTECTION)).isEqualTo(1);
    }

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

        assertThat(valter.getStatRankOf(StatItemId.INTELLIGENCE)).isEqualTo(22);
        assertThat(valter.getCalculatedTotalStatOf(StatItemId.INTELLIGENCE)).isEqualTo(22);
        assertThat(valter.getStatRankOf(StatItemId.WILLPOWER)).isEqualTo(18);
        assertThat(valter.getCalculatedTotalStatOf(StatItemId.WILLPOWER)).isEqualTo(18);
        assertThat(valter.getStatRankOf(StatItemId.DEXTERITY)).isEqualTo(15);
        assertThat(valter.getCalculatedTotalStatOf(StatItemId.DEXTERITY)).isEqualTo(15);
        assertThat(valter.getStatRankOf(StatItemId.AGILITY)).isEqualTo(12);
        assertThat(valter.getCalculatedTotalStatOf(StatItemId.AGILITY)).isEqualTo(12);
        assertThat(valter.getStatRankOf(StatItemId.ENDURANCE)).isEqualTo(15);
        assertThat(valter.getCalculatedTotalStatOf(StatItemId.ENDURANCE)).isEqualTo(15);
        assertThat(valter.getStatRankOf(StatItemId.STRENGTH)).isEqualTo(10);
        assertThat(valter.getCalculatedTotalStatOf(StatItemId.STRENGTH)).isEqualTo(10);
        assertThat(valter.getStatRankOf(StatItemId.STAMINA)).isEqualTo(20);
        assertThat(valter.getCalculatedTotalStatOf(StatItemId.STAMINA)).isEqualTo(20);

        assertThat(valter.getSkillRankOf(SkillItemId.ALCHEMIST)).isEqualTo(3);
        assertThat(valter.getCalculatedTotalSkillOf(SkillItemId.ALCHEMIST)).isEqualTo(3);
        assertThat(valter.getSkillRankOf(SkillItemId.DIPLOMAT)).isEqualTo(0);
        assertThat(valter.getCalculatedTotalSkillOf(SkillItemId.DIPLOMAT)).isEqualTo(0);
        assertThat(valter.getSkillRankOf(SkillItemId.HEALER)).isEqualTo(0);
        assertThat(valter.getCalculatedTotalSkillOf(SkillItemId.HEALER)).isEqualTo(0);
        assertThat(valter.getSkillRankOf(SkillItemId.LOREMASTER)).isEqualTo(2);
        assertThat(valter.getCalculatedTotalSkillOf(SkillItemId.LOREMASTER)).isEqualTo(2);
        assertThat(valter.getSkillRankOf(SkillItemId.MECHANIC)).isEqualTo(2);
        assertThat(valter.getCalculatedTotalSkillOf(SkillItemId.MECHANIC)).isEqualTo(2);
        assertThat(valter.getSkillRankOf(SkillItemId.MERCHANT)).isEqualTo(0);
        assertThat(valter.getCalculatedTotalSkillOf(SkillItemId.MERCHANT)).isEqualTo(0);
        assertThat(valter.getSkillRankOf(SkillItemId.RANGER)).isEqualTo(0);
        assertThat(valter.getCalculatedTotalSkillOf(SkillItemId.RANGER)).isEqualTo(0);
        assertThat(valter.getSkillRankOf(SkillItemId.STEALTH)).isEqualTo(0);
        assertThat(valter.getCalculatedTotalSkillOf(SkillItemId.STEALTH)).isEqualTo(0);
        assertThat(valter.getSkillRankOf(SkillItemId.THIEF)).isEqualTo(0);
        assertThat(valter.getCalculatedTotalSkillOf(SkillItemId.THIEF)).isEqualTo(0);
        assertThat(valter.getSkillRankOf(SkillItemId.TROUBADOUR)).isEqualTo(0);
        assertThat(valter.getCalculatedTotalSkillOf(SkillItemId.TROUBADOUR)).isEqualTo(0);
        assertThat(valter.getSkillRankOf(SkillItemId.WARRIOR)).isEqualTo(0);
        assertThat(valter.getCalculatedTotalSkillOf(SkillItemId.WARRIOR)).isEqualTo(0);
        assertThat(valter.getSkillRankOf(SkillItemId.WIZARD)).isEqualTo(2);
        assertThat(valter.getCalculatedTotalSkillOf(SkillItemId.WIZARD)).isEqualTo(2);

        assertThat(valter.getSkillRankOf(SkillItemId.HAFTED)).isEqualTo(-1);
        assertThat(valter.getCalculatedTotalSkillOf(SkillItemId.HAFTED)).isEqualTo(0);
        assertThat(valter.getSkillRankOf(SkillItemId.MISSILE)).isEqualTo(-1);
        assertThat(valter.getCalculatedTotalSkillOf(SkillItemId.MISSILE)).isEqualTo(0);
        assertThat(valter.getSkillRankOf(SkillItemId.POLE)).isEqualTo(0);
        assertThat(valter.getCalculatedTotalSkillOf(SkillItemId.POLE)).isEqualTo(0);
        assertThat(valter.getSkillRankOf(SkillItemId.SHIELD)).isEqualTo(-1);
        assertThat(valter.getCalculatedTotalSkillOf(SkillItemId.SHIELD)).isEqualTo(0);
        assertThat(valter.getSkillRankOf(SkillItemId.SWORD)).isEqualTo(1);
        assertThat(valter.getCalculatedTotalSkillOf(SkillItemId.SWORD)).isEqualTo(1);
        assertThat(valter.getSkillRankOf(SkillItemId.THROWN)).isEqualTo(1);
        assertThat(valter.getCalculatedTotalSkillOf(SkillItemId.THROWN)).isEqualTo(1);

        assertThat(valter.getCalcValueOf(InventoryGroup.WEAPON, CalcAttributeId.BASE_HIT)).isEqualTo(40);
        assertThat(valter.getCalcValueOf(InventoryGroup.WEAPON, CalcAttributeId.DAMAGE)).isEqualTo(11);
        assertThat(valter.getCalcValueOf(InventoryGroup.SHIELD, CalcAttributeId.DEFENSE)).isEqualTo(0);
        assertThat(valter.getTotalCalcOf(CalcAttributeId.PROTECTION)).isEqualTo(1);
    }

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

        assertThat(galen.getStatRankOf(StatItemId.INTELLIGENCE)).isEqualTo(15);
        assertThat(galen.getCalculatedTotalStatOf(StatItemId.INTELLIGENCE)).isEqualTo(15);
        assertThat(galen.getStatRankOf(StatItemId.WILLPOWER)).isEqualTo(15);
        assertThat(galen.getCalculatedTotalStatOf(StatItemId.WILLPOWER)).isEqualTo(15);
        assertThat(galen.getStatRankOf(StatItemId.DEXTERITY)).isEqualTo(18);
        assertThat(galen.getCalculatedTotalStatOf(StatItemId.DEXTERITY)).isEqualTo(12);
        assertThat(galen.getStatRankOf(StatItemId.AGILITY)).isEqualTo(10);
        assertThat(galen.getCalculatedTotalStatOf(StatItemId.AGILITY)).isEqualTo(10);
        assertThat(galen.getStatRankOf(StatItemId.ENDURANCE)).isEqualTo(20);
        assertThat(galen.getCalculatedTotalStatOf(StatItemId.ENDURANCE)).isEqualTo(20);
        assertThat(galen.getStatRankOf(StatItemId.STRENGTH)).isEqualTo(25);
        assertThat(galen.getCalculatedTotalStatOf(StatItemId.STRENGTH)).isEqualTo(25);
        assertThat(galen.getStatRankOf(StatItemId.STAMINA)).isEqualTo(40);
        assertThat(galen.getCalculatedTotalStatOf(StatItemId.STAMINA)).isEqualTo(40);

        assertThat(galen.getSkillRankOf(SkillItemId.ALCHEMIST)).isEqualTo(-1);
        assertThat(galen.getCalculatedTotalSkillOf(SkillItemId.ALCHEMIST)).isEqualTo(0);
        assertThat(galen.getSkillRankOf(SkillItemId.DIPLOMAT)).isEqualTo(0);
        assertThat(galen.getCalculatedTotalSkillOf(SkillItemId.DIPLOMAT)).isEqualTo(0);
        assertThat(galen.getSkillRankOf(SkillItemId.HEALER)).isEqualTo(0);
        assertThat(galen.getCalculatedTotalSkillOf(SkillItemId.HEALER)).isEqualTo(0);
        assertThat(galen.getSkillRankOf(SkillItemId.LOREMASTER)).isEqualTo(0);
        assertThat(galen.getCalculatedTotalSkillOf(SkillItemId.LOREMASTER)).isEqualTo(0);
        assertThat(galen.getSkillRankOf(SkillItemId.MECHANIC)).isEqualTo(0);
        assertThat(galen.getCalculatedTotalSkillOf(SkillItemId.MECHANIC)).isEqualTo(0);
        assertThat(galen.getSkillRankOf(SkillItemId.MERCHANT)).isEqualTo(0);
        assertThat(galen.getCalculatedTotalSkillOf(SkillItemId.MERCHANT)).isEqualTo(0);
        assertThat(galen.getSkillRankOf(SkillItemId.RANGER)).isEqualTo(4);
        assertThat(galen.getCalculatedTotalSkillOf(SkillItemId.RANGER)).isEqualTo(4);
        assertThat(galen.getSkillRankOf(SkillItemId.STEALTH)).isEqualTo(3);
        assertThat(galen.getCalculatedTotalSkillOf(SkillItemId.STEALTH)).isEqualTo(0);
        assertThat(galen.getSkillRankOf(SkillItemId.THIEF)).isEqualTo(0);
        assertThat(galen.getCalculatedTotalSkillOf(SkillItemId.THIEF)).isEqualTo(0);
        assertThat(galen.getSkillRankOf(SkillItemId.TROUBADOUR)).isEqualTo(0);
        assertThat(galen.getCalculatedTotalSkillOf(SkillItemId.TROUBADOUR)).isEqualTo(0);
        assertThat(galen.getSkillRankOf(SkillItemId.WARRIOR)).isEqualTo(5);
        assertThat(galen.getCalculatedTotalSkillOf(SkillItemId.WARRIOR)).isEqualTo(5);
        assertThat(galen.getSkillRankOf(SkillItemId.WIZARD)).isEqualTo(-1);
        assertThat(galen.getCalculatedTotalSkillOf(SkillItemId.WIZARD)).isEqualTo(0);

        assertThat(galen.getSkillRankOf(SkillItemId.HAFTED)).isEqualTo(5);
        assertThat(galen.getCalculatedTotalSkillOf(SkillItemId.HAFTED)).isEqualTo(5);
        assertThat(galen.getSkillRankOf(SkillItemId.MISSILE)).isEqualTo(3);
        assertThat(galen.getCalculatedTotalSkillOf(SkillItemId.MISSILE)).isEqualTo(3);
        assertThat(galen.getSkillRankOf(SkillItemId.POLE)).isEqualTo(0);
        assertThat(galen.getCalculatedTotalSkillOf(SkillItemId.POLE)).isEqualTo(0);
        assertThat(galen.getSkillRankOf(SkillItemId.SHIELD)).isEqualTo(3);
        assertThat(galen.getCalculatedTotalSkillOf(SkillItemId.SHIELD)).isEqualTo(3);
        assertThat(galen.getSkillRankOf(SkillItemId.SWORD)).isEqualTo(-1);
        assertThat(galen.getCalculatedTotalSkillOf(SkillItemId.SWORD)).isEqualTo(0);
        assertThat(galen.getSkillRankOf(SkillItemId.THROWN)).isEqualTo(-1);
        assertThat(galen.getCalculatedTotalSkillOf(SkillItemId.THROWN)).isEqualTo(0);

        assertThat(galen.getCalcValueOf(InventoryGroup.WEAPON, CalcAttributeId.BASE_HIT)).isEqualTo(30);
        assertThat(galen.getCalcValueOf(InventoryGroup.WEAPON, CalcAttributeId.DAMAGE)).isEqualTo(20);
        assertThat(galen.getCalcValueOf(InventoryGroup.SHIELD, CalcAttributeId.DEFENSE)).isEqualTo(10);
        assertThat(galen.getTotalCalcOf(CalcAttributeId.PROTECTION)).isEqualTo(10);
    }

    @Test
    void whenHeroesAreCreated_JasparShouldHaveRightStats() {
        final HeroItem jaspar = heroes.getHero("jaspar");
        assertThat(party.contains(jaspar)).isFalse();

        assertThat(jaspar.school).isEqualTo(SchoolType.NONE);
        assertThat(jaspar.getLevel()).isEqualTo(12);
        assertThat(jaspar.getTotalXp()).isEqualTo(3250);
        assertThat(jaspar.getXpNeededForNextLevel()).isEqualTo(845);
        assertThat(jaspar.getCurrentHp()).isEqualTo(102);
        assertThat(jaspar.getMaximumHp()).isEqualTo(102);

        assertThat(jaspar.getStatRankOf(StatItemId.INTELLIGENCE)).isEqualTo(6);
        assertThat(jaspar.getCalculatedTotalStatOf(StatItemId.INTELLIGENCE)).isEqualTo(6);
        assertThat(jaspar.getStatRankOf(StatItemId.WILLPOWER)).isEqualTo(11);
        assertThat(jaspar.getCalculatedTotalStatOf(StatItemId.WILLPOWER)).isEqualTo(11);
        assertThat(jaspar.getStatRankOf(StatItemId.DEXTERITY)).isEqualTo(14);
        assertThat(jaspar.getCalculatedTotalStatOf(StatItemId.DEXTERITY)).isEqualTo(4);
        assertThat(jaspar.getStatRankOf(StatItemId.AGILITY)).isEqualTo(8);
        assertThat(jaspar.getCalculatedTotalStatOf(StatItemId.AGILITY)).isEqualTo(8);
        assertThat(jaspar.getStatRankOf(StatItemId.ENDURANCE)).isEqualTo(30);
        assertThat(jaspar.getCalculatedTotalStatOf(StatItemId.ENDURANCE)).isEqualTo(30);
        assertThat(jaspar.getStatRankOf(StatItemId.STRENGTH)).isEqualTo(30);
        assertThat(jaspar.getCalculatedTotalStatOf(StatItemId.STRENGTH)).isEqualTo(30);
        assertThat(jaspar.getStatRankOf(StatItemId.STAMINA)).isEqualTo(60);
        assertThat(jaspar.getCalculatedTotalStatOf(StatItemId.STAMINA)).isEqualTo(60);

        assertThat(jaspar.getSkillRankOf(SkillItemId.ALCHEMIST)).isEqualTo(-1);
        assertThat(jaspar.getCalculatedTotalSkillOf(SkillItemId.ALCHEMIST)).isEqualTo(0);
        assertThat(jaspar.getSkillRankOf(SkillItemId.DIPLOMAT)).isEqualTo(0);
        assertThat(jaspar.getCalculatedTotalSkillOf(SkillItemId.DIPLOMAT)).isEqualTo(0);
        assertThat(jaspar.getSkillRankOf(SkillItemId.HEALER)).isEqualTo(0);
        assertThat(jaspar.getCalculatedTotalSkillOf(SkillItemId.HEALER)).isEqualTo(0);
        assertThat(jaspar.getSkillRankOf(SkillItemId.LOREMASTER)).isEqualTo(-1);
        assertThat(jaspar.getCalculatedTotalSkillOf(SkillItemId.LOREMASTER)).isEqualTo(0);
        assertThat(jaspar.getSkillRankOf(SkillItemId.MECHANIC)).isEqualTo(0);
        assertThat(jaspar.getCalculatedTotalSkillOf(SkillItemId.MECHANIC)).isEqualTo(0);
        assertThat(jaspar.getSkillRankOf(SkillItemId.MERCHANT)).isEqualTo(0);
        assertThat(jaspar.getCalculatedTotalSkillOf(SkillItemId.MERCHANT)).isEqualTo(0);
        assertThat(jaspar.getSkillRankOf(SkillItemId.RANGER)).isEqualTo(0);
        assertThat(jaspar.getCalculatedTotalSkillOf(SkillItemId.RANGER)).isEqualTo(0);
        assertThat(jaspar.getSkillRankOf(SkillItemId.STEALTH)).isEqualTo(1);
        assertThat(jaspar.getCalculatedTotalSkillOf(SkillItemId.STEALTH)).isEqualTo(0);
        assertThat(jaspar.getSkillRankOf(SkillItemId.THIEF)).isEqualTo(-1);
        assertThat(jaspar.getCalculatedTotalSkillOf(SkillItemId.THIEF)).isEqualTo(0);
        assertThat(jaspar.getSkillRankOf(SkillItemId.TROUBADOUR)).isEqualTo(-1);
        assertThat(jaspar.getCalculatedTotalSkillOf(SkillItemId.TROUBADOUR)).isEqualTo(0);
        assertThat(jaspar.getSkillRankOf(SkillItemId.WARRIOR)).isEqualTo(6);
        assertThat(jaspar.getCalculatedTotalSkillOf(SkillItemId.WARRIOR)).isEqualTo(6);
        assertThat(jaspar.getSkillRankOf(SkillItemId.WIZARD)).isEqualTo(-1);
        assertThat(jaspar.getCalculatedTotalSkillOf(SkillItemId.WIZARD)).isEqualTo(0);

        assertThat(jaspar.getSkillRankOf(SkillItemId.HAFTED)).isEqualTo(0);
        assertThat(jaspar.getCalculatedTotalSkillOf(SkillItemId.HAFTED)).isEqualTo(0);
        assertThat(jaspar.getSkillRankOf(SkillItemId.MISSILE)).isEqualTo(-1);
        assertThat(jaspar.getCalculatedTotalSkillOf(SkillItemId.MISSILE)).isEqualTo(0);
        assertThat(jaspar.getSkillRankOf(SkillItemId.POLE)).isEqualTo(6);
        assertThat(jaspar.getCalculatedTotalSkillOf(SkillItemId.POLE)).isEqualTo(6);
        assertThat(jaspar.getSkillRankOf(SkillItemId.SHIELD)).isEqualTo(4);
        assertThat(jaspar.getCalculatedTotalSkillOf(SkillItemId.SHIELD)).isEqualTo(4);
        assertThat(jaspar.getSkillRankOf(SkillItemId.SWORD)).isEqualTo(6);
        assertThat(jaspar.getCalculatedTotalSkillOf(SkillItemId.SWORD)).isEqualTo(6);
        assertThat(jaspar.getSkillRankOf(SkillItemId.THROWN)).isEqualTo(-1);
        assertThat(jaspar.getCalculatedTotalSkillOf(SkillItemId.THROWN)).isEqualTo(0);

        assertThat(jaspar.getCalcValueOf(InventoryGroup.WEAPON, CalcAttributeId.BASE_HIT)).isEqualTo(40);
        assertThat(jaspar.getCalcValueOf(InventoryGroup.WEAPON, CalcAttributeId.DAMAGE)).isEqualTo(17);
        assertThat(jaspar.getCalcValueOf(InventoryGroup.SHIELD, CalcAttributeId.DEFENSE)).isEqualTo(15);
        assertThat(jaspar.getTotalCalcOf(CalcAttributeId.PROTECTION)).isEqualTo(18);
    }

    @Test
    void whenHeroesAreCreated_KiaraShouldHaveRightStats() {
        final HeroItem kiara = heroes.getHero("kiara");
        assertThat(party.contains(kiara)).isFalse();

        assertThat(kiara.school).isEqualTo(SchoolType.ELEMENTAL);
        assertThat(kiara.getLevel()).isEqualTo(12);
        assertThat(kiara.getTotalXp()).isEqualTo(3250);
        assertThat(kiara.getXpNeededForNextLevel()).isEqualTo(845);
        assertThat(kiara.getCurrentHp()).isEqualTo(72);
        assertThat(kiara.getMaximumHp()).isEqualTo(72);

        assertThat(kiara.getStatRankOf(StatItemId.INTELLIGENCE)).isEqualTo(15);
        assertThat(kiara.getCalculatedTotalStatOf(StatItemId.INTELLIGENCE)).isEqualTo(15);
        assertThat(kiara.getStatRankOf(StatItemId.WILLPOWER)).isEqualTo(10);
        assertThat(kiara.getCalculatedTotalStatOf(StatItemId.WILLPOWER)).isEqualTo(10);
        assertThat(kiara.getStatRankOf(StatItemId.DEXTERITY)).isEqualTo(30);
        assertThat(kiara.getCalculatedTotalStatOf(StatItemId.DEXTERITY)).isEqualTo(30);
        assertThat(kiara.getStatRankOf(StatItemId.AGILITY)).isEqualTo(26);
        assertThat(kiara.getCalculatedTotalStatOf(StatItemId.AGILITY)).isEqualTo(26);
        assertThat(kiara.getStatRankOf(StatItemId.ENDURANCE)).isEqualTo(20);
        assertThat(kiara.getCalculatedTotalStatOf(StatItemId.ENDURANCE)).isEqualTo(20);
        assertThat(kiara.getStatRankOf(StatItemId.STRENGTH)).isEqualTo(15);
        assertThat(kiara.getCalculatedTotalStatOf(StatItemId.STRENGTH)).isEqualTo(15);
        assertThat(kiara.getStatRankOf(StatItemId.STAMINA)).isEqualTo(40);
        assertThat(kiara.getCalculatedTotalStatOf(StatItemId.STAMINA)).isEqualTo(40);

        assertThat(kiara.getSkillRankOf(SkillItemId.ALCHEMIST)).isEqualTo(0);
        assertThat(kiara.getCalculatedTotalSkillOf(SkillItemId.ALCHEMIST)).isEqualTo(0);
        assertThat(kiara.getSkillRankOf(SkillItemId.DIPLOMAT)).isEqualTo(0);
        assertThat(kiara.getCalculatedTotalSkillOf(SkillItemId.DIPLOMAT)).isEqualTo(0);
        assertThat(kiara.getSkillRankOf(SkillItemId.HEALER)).isEqualTo(1);
        assertThat(kiara.getCalculatedTotalSkillOf(SkillItemId.HEALER)).isEqualTo(1);
        assertThat(kiara.getSkillRankOf(SkillItemId.LOREMASTER)).isEqualTo(0);
        assertThat(kiara.getCalculatedTotalSkillOf(SkillItemId.LOREMASTER)).isEqualTo(0);
        assertThat(kiara.getSkillRankOf(SkillItemId.MECHANIC)).isEqualTo(0);
        assertThat(kiara.getCalculatedTotalSkillOf(SkillItemId.MECHANIC)).isEqualTo(0);
        assertThat(kiara.getSkillRankOf(SkillItemId.MERCHANT)).isEqualTo(4);
        assertThat(kiara.getCalculatedTotalSkillOf(SkillItemId.MERCHANT)).isEqualTo(4);
        assertThat(kiara.getSkillRankOf(SkillItemId.RANGER)).isEqualTo(0);
        assertThat(kiara.getCalculatedTotalSkillOf(SkillItemId.RANGER)).isEqualTo(0);
        assertThat(kiara.getSkillRankOf(SkillItemId.STEALTH)).isEqualTo(5);
        assertThat(kiara.getCalculatedTotalSkillOf(SkillItemId.STEALTH)).isEqualTo(5);
        assertThat(kiara.getSkillRankOf(SkillItemId.THIEF)).isEqualTo(8);
        assertThat(kiara.getCalculatedTotalSkillOf(SkillItemId.THIEF)).isEqualTo(8);
        assertThat(kiara.getSkillRankOf(SkillItemId.TROUBADOUR)).isEqualTo(0);
        assertThat(kiara.getCalculatedTotalSkillOf(SkillItemId.TROUBADOUR)).isEqualTo(0);
        assertThat(kiara.getSkillRankOf(SkillItemId.WARRIOR)).isEqualTo(0);
        assertThat(kiara.getCalculatedTotalSkillOf(SkillItemId.WARRIOR)).isEqualTo(0);
        assertThat(kiara.getSkillRankOf(SkillItemId.WIZARD)).isEqualTo(4);
        assertThat(kiara.getCalculatedTotalSkillOf(SkillItemId.WIZARD)).isEqualTo(4);

        assertThat(kiara.getSkillRankOf(SkillItemId.HAFTED)).isEqualTo(-1);
        assertThat(kiara.getCalculatedTotalSkillOf(SkillItemId.HAFTED)).isEqualTo(0);
        assertThat(kiara.getSkillRankOf(SkillItemId.MISSILE)).isEqualTo(7);
        assertThat(kiara.getCalculatedTotalSkillOf(SkillItemId.MISSILE)).isEqualTo(7);
        assertThat(kiara.getSkillRankOf(SkillItemId.POLE)).isEqualTo(2);
        assertThat(kiara.getCalculatedTotalSkillOf(SkillItemId.POLE)).isEqualTo(2);
        assertThat(kiara.getSkillRankOf(SkillItemId.SHIELD)).isEqualTo(-1);
        assertThat(kiara.getCalculatedTotalSkillOf(SkillItemId.SHIELD)).isEqualTo(0);
        assertThat(kiara.getSkillRankOf(SkillItemId.SWORD)).isEqualTo(7);
        assertThat(kiara.getCalculatedTotalSkillOf(SkillItemId.SWORD)).isEqualTo(7);
        assertThat(kiara.getSkillRankOf(SkillItemId.THROWN)).isEqualTo(-1);
        assertThat(kiara.getCalculatedTotalSkillOf(SkillItemId.THROWN)).isEqualTo(0);

        assertThat(kiara.getCalcValueOf(InventoryGroup.WEAPON, CalcAttributeId.BASE_HIT)).isEqualTo(50);
        assertThat(kiara.getCalcValueOf(InventoryGroup.WEAPON, CalcAttributeId.DAMAGE)).isEqualTo(13);
        assertThat(kiara.getCalcValueOf(InventoryGroup.SHIELD, CalcAttributeId.DEFENSE)).isEqualTo(0);
        assertThat(kiara.getTotalCalcOf(CalcAttributeId.PROTECTION)).isEqualTo(2);
    }

    @Test
    void whenHeroesAreCreated_LuthaisShouldHaveRightStats() {
        final HeroItem luthais = heroes.getHero("luthais");
        assertThat(party.contains(luthais)).isFalse();

        assertThat(luthais.school).isEqualTo(SchoolType.ELEMENTAL);
        assertThat(luthais.getLevel()).isEqualTo(20);
        assertThat(luthais.getTotalXp()).isEqualTo(14350);
        assertThat(luthais.getXpNeededForNextLevel()).isEqualTo(2205);
        assertThat(luthais.getCurrentHp()).isEqualTo(88);
        assertThat(luthais.getMaximumHp()).isEqualTo(88);

        assertThat(luthais.getStatRankOf(StatItemId.INTELLIGENCE)).isEqualTo(30);
        assertThat(luthais.getCalculatedTotalStatOf(StatItemId.INTELLIGENCE)).isEqualTo(30);
        assertThat(luthais.getStatRankOf(StatItemId.WILLPOWER)).isEqualTo(30);
        assertThat(luthais.getCalculatedTotalStatOf(StatItemId.WILLPOWER)).isEqualTo(30);
        assertThat(luthais.getStatRankOf(StatItemId.DEXTERITY)).isEqualTo(20);
        assertThat(luthais.getCalculatedTotalStatOf(StatItemId.DEXTERITY)).isEqualTo(20);
        assertThat(luthais.getStatRankOf(StatItemId.AGILITY)).isEqualTo(12);
        assertThat(luthais.getCalculatedTotalStatOf(StatItemId.AGILITY)).isEqualTo(12);
        assertThat(luthais.getStatRankOf(StatItemId.ENDURANCE)).isEqualTo(18);
        assertThat(luthais.getCalculatedTotalStatOf(StatItemId.ENDURANCE)).isEqualTo(18);
        assertThat(luthais.getStatRankOf(StatItemId.STRENGTH)).isEqualTo(8);
        assertThat(luthais.getCalculatedTotalStatOf(StatItemId.STRENGTH)).isEqualTo(8);
        assertThat(luthais.getStatRankOf(StatItemId.STAMINA)).isEqualTo(50);
        assertThat(luthais.getCalculatedTotalStatOf(StatItemId.STAMINA)).isEqualTo(50);

        assertThat(luthais.getSkillRankOf(SkillItemId.ALCHEMIST)).isEqualTo(7);
        assertThat(luthais.getCalculatedTotalSkillOf(SkillItemId.ALCHEMIST)).isEqualTo(7);
        assertThat(luthais.getSkillRankOf(SkillItemId.DIPLOMAT)).isEqualTo(0);
        assertThat(luthais.getCalculatedTotalSkillOf(SkillItemId.DIPLOMAT)).isEqualTo(0);
        assertThat(luthais.getSkillRankOf(SkillItemId.HEALER)).isEqualTo(8);
        assertThat(luthais.getCalculatedTotalSkillOf(SkillItemId.HEALER)).isEqualTo(8);
        assertThat(luthais.getSkillRankOf(SkillItemId.LOREMASTER)).isEqualTo(9);
        assertThat(luthais.getCalculatedTotalSkillOf(SkillItemId.LOREMASTER)).isEqualTo(9);
        assertThat(luthais.getSkillRankOf(SkillItemId.MECHANIC)).isEqualTo(6);
        assertThat(luthais.getCalculatedTotalSkillOf(SkillItemId.MECHANIC)).isEqualTo(6);
        assertThat(luthais.getSkillRankOf(SkillItemId.MERCHANT)).isEqualTo(0);
        assertThat(luthais.getCalculatedTotalSkillOf(SkillItemId.MERCHANT)).isEqualTo(0);
        assertThat(luthais.getSkillRankOf(SkillItemId.RANGER)).isEqualTo(0);
        assertThat(luthais.getCalculatedTotalSkillOf(SkillItemId.RANGER)).isEqualTo(0);
        assertThat(luthais.getSkillRankOf(SkillItemId.STEALTH)).isEqualTo(5);
        assertThat(luthais.getCalculatedTotalSkillOf(SkillItemId.STEALTH)).isEqualTo(5);
        assertThat(luthais.getSkillRankOf(SkillItemId.THIEF)).isEqualTo(0);
        assertThat(luthais.getCalculatedTotalSkillOf(SkillItemId.THIEF)).isEqualTo(0);
        assertThat(luthais.getSkillRankOf(SkillItemId.TROUBADOUR)).isEqualTo(0);
        assertThat(luthais.getCalculatedTotalSkillOf(SkillItemId.TROUBADOUR)).isEqualTo(0);
        assertThat(luthais.getSkillRankOf(SkillItemId.WARRIOR)).isEqualTo(0);
        assertThat(luthais.getCalculatedTotalSkillOf(SkillItemId.WARRIOR)).isEqualTo(0);
        assertThat(luthais.getSkillRankOf(SkillItemId.WIZARD)).isEqualTo(10);
        assertThat(luthais.getCalculatedTotalSkillOf(SkillItemId.WIZARD)).isEqualTo(10);

        assertThat(luthais.getSkillRankOf(SkillItemId.HAFTED)).isEqualTo(0);
        assertThat(luthais.getCalculatedTotalSkillOf(SkillItemId.HAFTED)).isEqualTo(0);
        assertThat(luthais.getSkillRankOf(SkillItemId.MISSILE)).isEqualTo(-1);
        assertThat(luthais.getCalculatedTotalSkillOf(SkillItemId.MISSILE)).isEqualTo(0);
        assertThat(luthais.getSkillRankOf(SkillItemId.POLE)).isEqualTo(8);
        assertThat(luthais.getCalculatedTotalSkillOf(SkillItemId.POLE)).isEqualTo(8);
        assertThat(luthais.getSkillRankOf(SkillItemId.SHIELD)).isEqualTo(-1);
        assertThat(luthais.getCalculatedTotalSkillOf(SkillItemId.SHIELD)).isEqualTo(0);
        assertThat(luthais.getSkillRankOf(SkillItemId.SWORD)).isEqualTo(0);
        assertThat(luthais.getCalculatedTotalSkillOf(SkillItemId.SWORD)).isEqualTo(0);
        assertThat(luthais.getSkillRankOf(SkillItemId.THROWN)).isEqualTo(8);
        assertThat(luthais.getCalculatedTotalSkillOf(SkillItemId.THROWN)).isEqualTo(8);

        assertThat(luthais.getCalcValueOf(InventoryGroup.WEAPON, CalcAttributeId.BASE_HIT)).isEqualTo(80);
        assertThat(luthais.getCalcValueOf(InventoryGroup.WEAPON, CalcAttributeId.DAMAGE)).isEqualTo(10);
        assertThat(luthais.getCalcValueOf(InventoryGroup.SHIELD, CalcAttributeId.DEFENSE)).isEqualTo(0);
        assertThat(luthais.getTotalCalcOf(CalcAttributeId.PROTECTION)).isEqualTo(3);
    }

    @Test
    void whenHeroesAreCreated_EliasShouldHaveRightStats() {
        final HeroItem elias = heroes.getHero("elias");
        assertThat(party.contains(elias)).isFalse();

        assertThat(elias.school).isEqualTo(SchoolType.NAMING);
        assertThat(elias.getLevel()).isEqualTo(18);
        assertThat(elias.getTotalXp()).isEqualTo(10545);
        assertThat(elias.getXpNeededForNextLevel()).isEqualTo(1805);
        assertThat(elias.getCurrentHp()).isEqualTo(108);
        assertThat(elias.getMaximumHp()).isEqualTo(108);

        assertThat(elias.getStatRankOf(StatItemId.INTELLIGENCE)).isEqualTo(30);
        assertThat(elias.getCalculatedTotalStatOf(StatItemId.INTELLIGENCE)).isEqualTo(30);
        assertThat(elias.getStatRankOf(StatItemId.WILLPOWER)).isEqualTo(30);
        assertThat(elias.getCalculatedTotalStatOf(StatItemId.WILLPOWER)).isEqualTo(30);
        assertThat(elias.getStatRankOf(StatItemId.DEXTERITY)).isEqualTo(25);
        assertThat(elias.getCalculatedTotalStatOf(StatItemId.DEXTERITY)).isEqualTo(25);
        assertThat(elias.getStatRankOf(StatItemId.AGILITY)).isEqualTo(18);
        assertThat(elias.getCalculatedTotalStatOf(StatItemId.AGILITY)).isEqualTo(18);
        assertThat(elias.getStatRankOf(StatItemId.ENDURANCE)).isEqualTo(30);
        assertThat(elias.getCalculatedTotalStatOf(StatItemId.ENDURANCE)).isEqualTo(30);
        assertThat(elias.getStatRankOf(StatItemId.STRENGTH)).isEqualTo(20);
        assertThat(elias.getCalculatedTotalStatOf(StatItemId.STRENGTH)).isEqualTo(20);
        assertThat(elias.getStatRankOf(StatItemId.STAMINA)).isEqualTo(60);
        assertThat(elias.getCalculatedTotalStatOf(StatItemId.STAMINA)).isEqualTo(60);

        assertThat(elias.getSkillRankOf(SkillItemId.ALCHEMIST)).isEqualTo(0);
        assertThat(elias.getCalculatedTotalSkillOf(SkillItemId.ALCHEMIST)).isEqualTo(0);
        assertThat(elias.getSkillRankOf(SkillItemId.DIPLOMAT)).isEqualTo(8);
        assertThat(elias.getCalculatedTotalSkillOf(SkillItemId.DIPLOMAT)).isEqualTo(8);
        assertThat(elias.getSkillRankOf(SkillItemId.HEALER)).isEqualTo(0);
        assertThat(elias.getCalculatedTotalSkillOf(SkillItemId.HEALER)).isEqualTo(0);
        assertThat(elias.getSkillRankOf(SkillItemId.LOREMASTER)).isEqualTo(0);
        assertThat(elias.getCalculatedTotalSkillOf(SkillItemId.LOREMASTER)).isEqualTo(0);
        assertThat(elias.getSkillRankOf(SkillItemId.MECHANIC)).isEqualTo(0);
        assertThat(elias.getCalculatedTotalSkillOf(SkillItemId.MECHANIC)).isEqualTo(0);
        assertThat(elias.getSkillRankOf(SkillItemId.MERCHANT)).isEqualTo(0);
        assertThat(elias.getCalculatedTotalSkillOf(SkillItemId.MERCHANT)).isEqualTo(0);
        assertThat(elias.getSkillRankOf(SkillItemId.RANGER)).isEqualTo(0);
        assertThat(elias.getCalculatedTotalSkillOf(SkillItemId.RANGER)).isEqualTo(0);
        assertThat(elias.getSkillRankOf(SkillItemId.STEALTH)).isEqualTo(0);
        assertThat(elias.getCalculatedTotalSkillOf(SkillItemId.STEALTH)).isEqualTo(0);
        assertThat(elias.getSkillRankOf(SkillItemId.THIEF)).isEqualTo(0);
        assertThat(elias.getCalculatedTotalSkillOf(SkillItemId.THIEF)).isEqualTo(0);
        assertThat(elias.getSkillRankOf(SkillItemId.TROUBADOUR)).isEqualTo(0);
        assertThat(elias.getCalculatedTotalSkillOf(SkillItemId.TROUBADOUR)).isEqualTo(0);
        assertThat(elias.getSkillRankOf(SkillItemId.WARRIOR)).isEqualTo(7);
        assertThat(elias.getCalculatedTotalSkillOf(SkillItemId.WARRIOR)).isEqualTo(7);
        assertThat(elias.getSkillRankOf(SkillItemId.WIZARD)).isEqualTo(7);
        assertThat(elias.getCalculatedTotalSkillOf(SkillItemId.WIZARD)).isEqualTo(7);

        assertThat(elias.getSkillRankOf(SkillItemId.HAFTED)).isEqualTo(5);
        assertThat(elias.getCalculatedTotalSkillOf(SkillItemId.HAFTED)).isEqualTo(5);
        assertThat(elias.getSkillRankOf(SkillItemId.MISSILE)).isEqualTo(-1);
        assertThat(elias.getCalculatedTotalSkillOf(SkillItemId.MISSILE)).isEqualTo(0);
        assertThat(elias.getSkillRankOf(SkillItemId.POLE)).isEqualTo(5);
        assertThat(elias.getCalculatedTotalSkillOf(SkillItemId.POLE)).isEqualTo(5);
        assertThat(elias.getSkillRankOf(SkillItemId.SHIELD)).isEqualTo(-1);
        assertThat(elias.getCalculatedTotalSkillOf(SkillItemId.SHIELD)).isEqualTo(0);
        assertThat(elias.getSkillRankOf(SkillItemId.SWORD)).isEqualTo(7);
        assertThat(elias.getCalculatedTotalSkillOf(SkillItemId.SWORD)).isEqualTo(7);
        assertThat(elias.getSkillRankOf(SkillItemId.THROWN)).isEqualTo(-1);
        assertThat(elias.getCalculatedTotalSkillOf(SkillItemId.THROWN)).isEqualTo(0);

        assertThat(elias.getCalcValueOf(InventoryGroup.WEAPON, CalcAttributeId.BASE_HIT)).isEqualTo(50);
        assertThat(elias.getCalcValueOf(InventoryGroup.WEAPON, CalcAttributeId.DAMAGE)).isEqualTo(17);
        assertThat(elias.getCalcValueOf(InventoryGroup.SHIELD, CalcAttributeId.DEFENSE)).isEqualTo(0);
        assertThat(elias.getTotalCalcOf(CalcAttributeId.PROTECTION)).isEqualTo(6);
    }

    @Test
    void whenHeroesAreCreated_OnarrShouldHaveRightStats() {
        final HeroItem onarr = heroes.getHero("onarr");
        assertThat(party.contains(onarr)).isFalse();

        assertThat(onarr.school).isEqualTo(SchoolType.NONE);
        assertThat(onarr.getLevel()).isEqualTo(18);
        assertThat(onarr.getTotalXp()).isEqualTo(10545);
        assertThat(onarr.getXpNeededForNextLevel()).isEqualTo(1805);
        assertThat(onarr.getCurrentHp()).isEqualTo(108);
        assertThat(onarr.getMaximumHp()).isEqualTo(108);

        assertThat(onarr.getStatRankOf(StatItemId.INTELLIGENCE)).isEqualTo(30);
        assertThat(onarr.getCalculatedTotalStatOf(StatItemId.INTELLIGENCE)).isEqualTo(30);
        assertThat(onarr.getStatRankOf(StatItemId.WILLPOWER)).isEqualTo(25);
        assertThat(onarr.getCalculatedTotalStatOf(StatItemId.WILLPOWER)).isEqualTo(25);
        assertThat(onarr.getStatRankOf(StatItemId.DEXTERITY)).isEqualTo(23);
        assertThat(onarr.getCalculatedTotalStatOf(StatItemId.DEXTERITY)).isEqualTo(17);
        assertThat(onarr.getStatRankOf(StatItemId.AGILITY)).isEqualTo(15);
        assertThat(onarr.getCalculatedTotalStatOf(StatItemId.AGILITY)).isEqualTo(15);
        assertThat(onarr.getStatRankOf(StatItemId.ENDURANCE)).isEqualTo(30);
        assertThat(onarr.getCalculatedTotalStatOf(StatItemId.ENDURANCE)).isEqualTo(30);
        assertThat(onarr.getStatRankOf(StatItemId.STRENGTH)).isEqualTo(25);
        assertThat(onarr.getCalculatedTotalStatOf(StatItemId.STRENGTH)).isEqualTo(25);
        assertThat(onarr.getStatRankOf(StatItemId.STAMINA)).isEqualTo(60);
        assertThat(onarr.getCalculatedTotalStatOf(StatItemId.STAMINA)).isEqualTo(60);

        assertThat(onarr.getSkillRankOf(SkillItemId.ALCHEMIST)).isEqualTo(-1);
        assertThat(onarr.getCalculatedTotalSkillOf(SkillItemId.ALCHEMIST)).isEqualTo(0);
        assertThat(onarr.getSkillRankOf(SkillItemId.DIPLOMAT)).isEqualTo(0);
        assertThat(onarr.getCalculatedTotalSkillOf(SkillItemId.DIPLOMAT)).isEqualTo(0);
        assertThat(onarr.getSkillRankOf(SkillItemId.HEALER)).isEqualTo(4);
        assertThat(onarr.getCalculatedTotalSkillOf(SkillItemId.HEALER)).isEqualTo(4);
        assertThat(onarr.getSkillRankOf(SkillItemId.LOREMASTER)).isEqualTo(6);
        assertThat(onarr.getCalculatedTotalSkillOf(SkillItemId.LOREMASTER)).isEqualTo(6);
        assertThat(onarr.getSkillRankOf(SkillItemId.MECHANIC)).isEqualTo(0);
        assertThat(onarr.getCalculatedTotalSkillOf(SkillItemId.MECHANIC)).isEqualTo(0);
        assertThat(onarr.getSkillRankOf(SkillItemId.MERCHANT)).isEqualTo(0);
        assertThat(onarr.getCalculatedTotalSkillOf(SkillItemId.MERCHANT)).isEqualTo(0);
        assertThat(onarr.getSkillRankOf(SkillItemId.RANGER)).isEqualTo(0);
        assertThat(onarr.getCalculatedTotalSkillOf(SkillItemId.RANGER)).isEqualTo(0);
        assertThat(onarr.getSkillRankOf(SkillItemId.STEALTH)).isEqualTo(0);
        assertThat(onarr.getCalculatedTotalSkillOf(SkillItemId.STEALTH)).isEqualTo(0);
        assertThat(onarr.getSkillRankOf(SkillItemId.THIEF)).isEqualTo(0);
        assertThat(onarr.getCalculatedTotalSkillOf(SkillItemId.THIEF)).isEqualTo(0);
        assertThat(onarr.getSkillRankOf(SkillItemId.TROUBADOUR)).isEqualTo(7);
        assertThat(onarr.getCalculatedTotalSkillOf(SkillItemId.TROUBADOUR)).isEqualTo(7);
        assertThat(onarr.getSkillRankOf(SkillItemId.WARRIOR)).isEqualTo(9);
        assertThat(onarr.getCalculatedTotalSkillOf(SkillItemId.WARRIOR)).isEqualTo(9);
        assertThat(onarr.getSkillRankOf(SkillItemId.WIZARD)).isEqualTo(-1);
        assertThat(onarr.getCalculatedTotalSkillOf(SkillItemId.WIZARD)).isEqualTo(0);

        assertThat(onarr.getSkillRankOf(SkillItemId.HAFTED)).isEqualTo(8);
        assertThat(onarr.getCalculatedTotalSkillOf(SkillItemId.HAFTED)).isEqualTo(8);
        assertThat(onarr.getSkillRankOf(SkillItemId.MISSILE)).isEqualTo(-1);
        assertThat(onarr.getCalculatedTotalSkillOf(SkillItemId.MISSILE)).isEqualTo(0);
        assertThat(onarr.getSkillRankOf(SkillItemId.POLE)).isEqualTo(8);
        assertThat(onarr.getCalculatedTotalSkillOf(SkillItemId.POLE)).isEqualTo(8);
        assertThat(onarr.getSkillRankOf(SkillItemId.SHIELD)).isEqualTo(9);
        assertThat(onarr.getCalculatedTotalSkillOf(SkillItemId.SHIELD)).isEqualTo(9);
        assertThat(onarr.getSkillRankOf(SkillItemId.SWORD)).isEqualTo(5);
        assertThat(onarr.getCalculatedTotalSkillOf(SkillItemId.SWORD)).isEqualTo(5);
        assertThat(onarr.getSkillRankOf(SkillItemId.THROWN)).isEqualTo(8);
        assertThat(onarr.getCalculatedTotalSkillOf(SkillItemId.THROWN)).isEqualTo(8);

        assertThat(onarr.getCalcValueOf(InventoryGroup.WEAPON, CalcAttributeId.BASE_HIT)).isEqualTo(40);
        assertThat(onarr.getCalcValueOf(InventoryGroup.WEAPON, CalcAttributeId.DAMAGE)).isEqualTo(24);
        assertThat(onarr.getCalcValueOf(InventoryGroup.SHIELD, CalcAttributeId.DEFENSE)).isEqualTo(15);
        assertThat(onarr.getTotalCalcOf(CalcAttributeId.PROTECTION)).isEqualTo(16);
    }

    @Test
    void whenHeroesAreCreated_DuilioShouldHaveRightStats() {
        final HeroItem duilio = heroes.getHero("duilio");
        assertThat(party.contains(duilio)).isFalse();

        assertThat(duilio.school).isEqualTo(SchoolType.ELEMENTAL);
        assertThat(duilio.getLevel()).isEqualTo(22);
        assertThat(duilio.getTotalXp()).isEqualTo(18975);
        assertThat(duilio.getXpNeededForNextLevel()).isEqualTo(2645);
        assertThat(duilio.getCurrentHp()).isEqualTo(122);
        assertThat(duilio.getMaximumHp()).isEqualTo(122);

        assertThat(duilio.getStatRankOf(StatItemId.INTELLIGENCE)).isEqualTo(25);
        assertThat(duilio.getCalculatedTotalStatOf(StatItemId.INTELLIGENCE)).isEqualTo(25);
        assertThat(duilio.getStatRankOf(StatItemId.WILLPOWER)).isEqualTo(25);
        assertThat(duilio.getCalculatedTotalStatOf(StatItemId.WILLPOWER)).isEqualTo(25);
        assertThat(duilio.getStatRankOf(StatItemId.DEXTERITY)).isEqualTo(30);
        assertThat(duilio.getCalculatedTotalStatOf(StatItemId.DEXTERITY)).isEqualTo(28);
        assertThat(duilio.getStatRankOf(StatItemId.AGILITY)).isEqualTo(20);
        assertThat(duilio.getCalculatedTotalStatOf(StatItemId.AGILITY)).isEqualTo(20);
        assertThat(duilio.getStatRankOf(StatItemId.ENDURANCE)).isEqualTo(25);
        assertThat(duilio.getCalculatedTotalStatOf(StatItemId.ENDURANCE)).isEqualTo(25);
        assertThat(duilio.getStatRankOf(StatItemId.STRENGTH)).isEqualTo(25);
        assertThat(duilio.getCalculatedTotalStatOf(StatItemId.STRENGTH)).isEqualTo(25);
        assertThat(duilio.getStatRankOf(StatItemId.STAMINA)).isEqualTo(75);
        assertThat(duilio.getCalculatedTotalStatOf(StatItemId.STAMINA)).isEqualTo(75);

        assertThat(duilio.getSkillRankOf(SkillItemId.ALCHEMIST)).isEqualTo(5);
        assertThat(duilio.getCalculatedTotalSkillOf(SkillItemId.ALCHEMIST)).isEqualTo(5);
        assertThat(duilio.getSkillRankOf(SkillItemId.DIPLOMAT)).isEqualTo(10);
        assertThat(duilio.getCalculatedTotalSkillOf(SkillItemId.DIPLOMAT)).isEqualTo(10);
        assertThat(duilio.getSkillRankOf(SkillItemId.HEALER)).isEqualTo(0);
        assertThat(duilio.getCalculatedTotalSkillOf(SkillItemId.HEALER)).isEqualTo(0);
        assertThat(duilio.getSkillRankOf(SkillItemId.LOREMASTER)).isEqualTo(5);
        assertThat(duilio.getCalculatedTotalSkillOf(SkillItemId.LOREMASTER)).isEqualTo(5);
        assertThat(duilio.getSkillRankOf(SkillItemId.MECHANIC)).isEqualTo(0);
        assertThat(duilio.getCalculatedTotalSkillOf(SkillItemId.MECHANIC)).isEqualTo(0);
        assertThat(duilio.getSkillRankOf(SkillItemId.MERCHANT)).isEqualTo(5);
        assertThat(duilio.getCalculatedTotalSkillOf(SkillItemId.MERCHANT)).isEqualTo(5);
        assertThat(duilio.getSkillRankOf(SkillItemId.RANGER)).isEqualTo(5);
        assertThat(duilio.getCalculatedTotalSkillOf(SkillItemId.RANGER)).isEqualTo(5);
        assertThat(duilio.getSkillRankOf(SkillItemId.STEALTH)).isEqualTo(5);
        assertThat(duilio.getCalculatedTotalSkillOf(SkillItemId.STEALTH)).isEqualTo(0);
        assertThat(duilio.getSkillRankOf(SkillItemId.THIEF)).isEqualTo(5);
        assertThat(duilio.getCalculatedTotalSkillOf(SkillItemId.THIEF)).isEqualTo(5);
        assertThat(duilio.getSkillRankOf(SkillItemId.TROUBADOUR)).isEqualTo(10);
        assertThat(duilio.getCalculatedTotalSkillOf(SkillItemId.TROUBADOUR)).isEqualTo(10);
        assertThat(duilio.getSkillRankOf(SkillItemId.WARRIOR)).isEqualTo(10);
        assertThat(duilio.getCalculatedTotalSkillOf(SkillItemId.WARRIOR)).isEqualTo(10);
        assertThat(duilio.getSkillRankOf(SkillItemId.WIZARD)).isEqualTo(10);
        assertThat(duilio.getCalculatedTotalSkillOf(SkillItemId.WIZARD)).isEqualTo(10);

        assertThat(duilio.getSkillRankOf(SkillItemId.HAFTED)).isEqualTo(10);
        assertThat(duilio.getCalculatedTotalSkillOf(SkillItemId.HAFTED)).isEqualTo(10);
        assertThat(duilio.getSkillRankOf(SkillItemId.MISSILE)).isEqualTo(-1);
        assertThat(duilio.getCalculatedTotalSkillOf(SkillItemId.MISSILE)).isEqualTo(0);
        assertThat(duilio.getSkillRankOf(SkillItemId.POLE)).isEqualTo(10);
        assertThat(duilio.getCalculatedTotalSkillOf(SkillItemId.POLE)).isEqualTo(10);
        assertThat(duilio.getSkillRankOf(SkillItemId.SHIELD)).isEqualTo(10);
        assertThat(duilio.getCalculatedTotalSkillOf(SkillItemId.SHIELD)).isEqualTo(10);
        assertThat(duilio.getSkillRankOf(SkillItemId.SWORD)).isEqualTo(10);
        assertThat(duilio.getCalculatedTotalSkillOf(SkillItemId.SWORD)).isEqualTo(10);
        assertThat(duilio.getSkillRankOf(SkillItemId.THROWN)).isEqualTo(10);
        assertThat(duilio.getCalculatedTotalSkillOf(SkillItemId.THROWN)).isEqualTo(10);

        assertThat(duilio.getCalcValueOf(InventoryGroup.WEAPON, CalcAttributeId.BASE_HIT)).isEqualTo(60);
        assertThat(duilio.getCalcValueOf(InventoryGroup.WEAPON, CalcAttributeId.DAMAGE)).isEqualTo(17);
        assertThat(duilio.getCalcValueOf(InventoryGroup.SHIELD, CalcAttributeId.DEFENSE)).isEqualTo(15);
        assertThat(duilio.getTotalCalcOf(CalcAttributeId.PROTECTION)).isEqualTo(10);
    }

    @Test
    void whenHeroesAreCreated_IellwenShouldHaveRightStats() {
        final HeroItem iellwen = heroes.getHero("iellwen");
        assertThat(party.contains(iellwen)).isFalse();

        assertThat(iellwen.school).isEqualTo(SchoolType.STAR);
        assertThat(iellwen.getLevel()).isEqualTo(20);
        assertThat(iellwen.getTotalXp()).isEqualTo(14350);
        assertThat(iellwen.getXpNeededForNextLevel()).isEqualTo(2205);
        assertThat(iellwen.getCurrentHp()).isEqualTo(110);
        assertThat(iellwen.getMaximumHp()).isEqualTo(110);

        assertThat(iellwen.getStatRankOf(StatItemId.INTELLIGENCE)).isEqualTo(35);
        assertThat(iellwen.getCalculatedTotalStatOf(StatItemId.INTELLIGENCE)).isEqualTo(35);
        assertThat(iellwen.getStatRankOf(StatItemId.WILLPOWER)).isEqualTo(25);
        assertThat(iellwen.getCalculatedTotalStatOf(StatItemId.WILLPOWER)).isEqualTo(25);
        assertThat(iellwen.getStatRankOf(StatItemId.DEXTERITY)).isEqualTo(30);
        assertThat(iellwen.getCalculatedTotalStatOf(StatItemId.DEXTERITY)).isEqualTo(30);
        assertThat(iellwen.getStatRankOf(StatItemId.AGILITY)).isEqualTo(25);
        assertThat(iellwen.getCalculatedTotalStatOf(StatItemId.AGILITY)).isEqualTo(25);
        assertThat(iellwen.getStatRankOf(StatItemId.ENDURANCE)).isEqualTo(30);
        assertThat(iellwen.getCalculatedTotalStatOf(StatItemId.ENDURANCE)).isEqualTo(30);
        assertThat(iellwen.getStatRankOf(StatItemId.STRENGTH)).isEqualTo(20);
        assertThat(iellwen.getCalculatedTotalStatOf(StatItemId.STRENGTH)).isEqualTo(20);
        assertThat(iellwen.getStatRankOf(StatItemId.STAMINA)).isEqualTo(60);
        assertThat(iellwen.getCalculatedTotalStatOf(StatItemId.STAMINA)).isEqualTo(60);

        assertThat(iellwen.getSkillRankOf(SkillItemId.ALCHEMIST)).isEqualTo(0);
        assertThat(iellwen.getCalculatedTotalSkillOf(SkillItemId.ALCHEMIST)).isEqualTo(0);
        assertThat(iellwen.getSkillRankOf(SkillItemId.DIPLOMAT)).isEqualTo(0);
        assertThat(iellwen.getCalculatedTotalSkillOf(SkillItemId.DIPLOMAT)).isEqualTo(0);
        assertThat(iellwen.getSkillRankOf(SkillItemId.HEALER)).isEqualTo(10);
        assertThat(iellwen.getCalculatedTotalSkillOf(SkillItemId.HEALER)).isEqualTo(10);
        assertThat(iellwen.getSkillRankOf(SkillItemId.LOREMASTER)).isEqualTo(0);
        assertThat(iellwen.getCalculatedTotalSkillOf(SkillItemId.LOREMASTER)).isEqualTo(0);
        assertThat(iellwen.getSkillRankOf(SkillItemId.MECHANIC)).isEqualTo(0);
        assertThat(iellwen.getCalculatedTotalSkillOf(SkillItemId.MECHANIC)).isEqualTo(0);
        assertThat(iellwen.getSkillRankOf(SkillItemId.MERCHANT)).isEqualTo(0);
        assertThat(iellwen.getCalculatedTotalSkillOf(SkillItemId.MERCHANT)).isEqualTo(0);
        assertThat(iellwen.getSkillRankOf(SkillItemId.RANGER)).isEqualTo(6);
        assertThat(iellwen.getCalculatedTotalSkillOf(SkillItemId.RANGER)).isEqualTo(6);
        assertThat(iellwen.getSkillRankOf(SkillItemId.STEALTH)).isEqualTo(6);
        assertThat(iellwen.getCalculatedTotalSkillOf(SkillItemId.STEALTH)).isEqualTo(5);
        assertThat(iellwen.getSkillRankOf(SkillItemId.THIEF)).isEqualTo(0);
        assertThat(iellwen.getCalculatedTotalSkillOf(SkillItemId.THIEF)).isEqualTo(0);
        assertThat(iellwen.getSkillRankOf(SkillItemId.TROUBADOUR)).isEqualTo(0);
        assertThat(iellwen.getCalculatedTotalSkillOf(SkillItemId.TROUBADOUR)).isEqualTo(0);
        assertThat(iellwen.getSkillRankOf(SkillItemId.WARRIOR)).isEqualTo(10);
        assertThat(iellwen.getCalculatedTotalSkillOf(SkillItemId.WARRIOR)).isEqualTo(10);
        assertThat(iellwen.getSkillRankOf(SkillItemId.WIZARD)).isEqualTo(8);
        assertThat(iellwen.getCalculatedTotalSkillOf(SkillItemId.WIZARD)).isEqualTo(8);

        assertThat(iellwen.getSkillRankOf(SkillItemId.HAFTED)).isEqualTo(5);
        assertThat(iellwen.getCalculatedTotalSkillOf(SkillItemId.HAFTED)).isEqualTo(5);
        assertThat(iellwen.getSkillRankOf(SkillItemId.MISSILE)).isEqualTo(7);
        assertThat(iellwen.getCalculatedTotalSkillOf(SkillItemId.MISSILE)).isEqualTo(7);
        assertThat(iellwen.getSkillRankOf(SkillItemId.POLE)).isEqualTo(0);
        assertThat(iellwen.getCalculatedTotalSkillOf(SkillItemId.POLE)).isEqualTo(0);
        assertThat(iellwen.getSkillRankOf(SkillItemId.SHIELD)).isEqualTo(-1);
        assertThat(iellwen.getCalculatedTotalSkillOf(SkillItemId.SHIELD)).isEqualTo(0);
        assertThat(iellwen.getSkillRankOf(SkillItemId.SWORD)).isEqualTo(10);
        assertThat(iellwen.getCalculatedTotalSkillOf(SkillItemId.SWORD)).isEqualTo(10);
        assertThat(iellwen.getSkillRankOf(SkillItemId.THROWN)).isEqualTo(-1);
        assertThat(iellwen.getCalculatedTotalSkillOf(SkillItemId.THROWN)).isEqualTo(0);

        assertThat(iellwen.getCalcValueOf(InventoryGroup.WEAPON, CalcAttributeId.BASE_HIT)).isEqualTo(60);
        assertThat(iellwen.getCalcValueOf(InventoryGroup.WEAPON, CalcAttributeId.DAMAGE)).isEqualTo(17);
        assertThat(iellwen.getCalcValueOf(InventoryGroup.SHIELD, CalcAttributeId.DEFENSE)).isEqualTo(0);
        assertThat(iellwen.getTotalCalcOf(CalcAttributeId.PROTECTION)).isEqualTo(7);
    }

    @Test
    void whenHeroesAreCreated_FaeronShouldHaveRightStats() {
        final HeroItem faeron = heroes.getHero("faeron");
        assertThat(party.contains(faeron)).isFalse();

        assertThat(faeron.school).isEqualTo(SchoolType.NAMING);
        assertThat(faeron.getLevel()).isEqualTo(25);
        assertThat(faeron.getTotalXp()).isEqualTo(27625);
        assertThat(faeron.getXpNeededForNextLevel()).isEqualTo(3380);
        assertThat(faeron.getCurrentHp()).isEqualTo(130);
        assertThat(faeron.getMaximumHp()).isEqualTo(130);

        assertThat(faeron.getStatRankOf(StatItemId.INTELLIGENCE)).isEqualTo(30);
        assertThat(faeron.getCalculatedTotalStatOf(StatItemId.INTELLIGENCE)).isEqualTo(30);
        assertThat(faeron.getStatRankOf(StatItemId.WILLPOWER)).isEqualTo(30);
        assertThat(faeron.getCalculatedTotalStatOf(StatItemId.WILLPOWER)).isEqualTo(30);
        assertThat(faeron.getStatRankOf(StatItemId.DEXTERITY)).isEqualTo(30);
        assertThat(faeron.getCalculatedTotalStatOf(StatItemId.DEXTERITY)).isEqualTo(30);
        assertThat(faeron.getStatRankOf(StatItemId.AGILITY)).isEqualTo(30);
        assertThat(faeron.getCalculatedTotalStatOf(StatItemId.AGILITY)).isEqualTo(30);
        assertThat(faeron.getStatRankOf(StatItemId.ENDURANCE)).isEqualTo(25);
        assertThat(faeron.getCalculatedTotalStatOf(StatItemId.ENDURANCE)).isEqualTo(25);
        assertThat(faeron.getStatRankOf(StatItemId.STRENGTH)).isEqualTo(15);
        assertThat(faeron.getCalculatedTotalStatOf(StatItemId.STRENGTH)).isEqualTo(15);
        assertThat(faeron.getStatRankOf(StatItemId.STAMINA)).isEqualTo(80);
        assertThat(faeron.getCalculatedTotalStatOf(StatItemId.STAMINA)).isEqualTo(80);

        assertThat(faeron.getSkillRankOf(SkillItemId.ALCHEMIST)).isEqualTo(10);
        assertThat(faeron.getCalculatedTotalSkillOf(SkillItemId.ALCHEMIST)).isEqualTo(10);
        assertThat(faeron.getSkillRankOf(SkillItemId.DIPLOMAT)).isEqualTo(10);
        assertThat(faeron.getCalculatedTotalSkillOf(SkillItemId.DIPLOMAT)).isEqualTo(10);
        assertThat(faeron.getSkillRankOf(SkillItemId.HEALER)).isEqualTo(0);
        assertThat(faeron.getCalculatedTotalSkillOf(SkillItemId.HEALER)).isEqualTo(0);
        assertThat(faeron.getSkillRankOf(SkillItemId.LOREMASTER)).isEqualTo(10);
        assertThat(faeron.getCalculatedTotalSkillOf(SkillItemId.LOREMASTER)).isEqualTo(10);
        assertThat(faeron.getSkillRankOf(SkillItemId.MECHANIC)).isEqualTo(0);
        assertThat(faeron.getCalculatedTotalSkillOf(SkillItemId.MECHANIC)).isEqualTo(0);
        assertThat(faeron.getSkillRankOf(SkillItemId.MERCHANT)).isEqualTo(10);
        assertThat(faeron.getCalculatedTotalSkillOf(SkillItemId.MERCHANT)).isEqualTo(10);
        assertThat(faeron.getSkillRankOf(SkillItemId.RANGER)).isEqualTo(10);
        assertThat(faeron.getCalculatedTotalSkillOf(SkillItemId.RANGER)).isEqualTo(10);
        assertThat(faeron.getSkillRankOf(SkillItemId.STEALTH)).isEqualTo(10);
        assertThat(faeron.getCalculatedTotalSkillOf(SkillItemId.STEALTH)).isEqualTo(10);
        assertThat(faeron.getSkillRankOf(SkillItemId.THIEF)).isEqualTo(10);
        assertThat(faeron.getCalculatedTotalSkillOf(SkillItemId.THIEF)).isEqualTo(10);
        assertThat(faeron.getSkillRankOf(SkillItemId.TROUBADOUR)).isEqualTo(10);
        assertThat(faeron.getCalculatedTotalSkillOf(SkillItemId.TROUBADOUR)).isEqualTo(10);
        assertThat(faeron.getSkillRankOf(SkillItemId.WARRIOR)).isEqualTo(10);
        assertThat(faeron.getCalculatedTotalSkillOf(SkillItemId.WARRIOR)).isEqualTo(10);
        assertThat(faeron.getSkillRankOf(SkillItemId.WIZARD)).isEqualTo(-1);
        assertThat(faeron.getCalculatedTotalSkillOf(SkillItemId.WIZARD)).isEqualTo(0);

        assertThat(faeron.getSkillRankOf(SkillItemId.HAFTED)).isEqualTo(10);
        assertThat(faeron.getCalculatedTotalSkillOf(SkillItemId.HAFTED)).isEqualTo(10);
        assertThat(faeron.getSkillRankOf(SkillItemId.MISSILE)).isEqualTo(-1);
        assertThat(faeron.getCalculatedTotalSkillOf(SkillItemId.MISSILE)).isEqualTo(0);
        assertThat(faeron.getSkillRankOf(SkillItemId.POLE)).isEqualTo(-1);
        assertThat(faeron.getCalculatedTotalSkillOf(SkillItemId.POLE)).isEqualTo(0);
        assertThat(faeron.getSkillRankOf(SkillItemId.SHIELD)).isEqualTo(0);
        assertThat(faeron.getCalculatedTotalSkillOf(SkillItemId.SHIELD)).isEqualTo(0);
        assertThat(faeron.getSkillRankOf(SkillItemId.SWORD)).isEqualTo(10);
        assertThat(faeron.getCalculatedTotalSkillOf(SkillItemId.SWORD)).isEqualTo(10);
        assertThat(faeron.getSkillRankOf(SkillItemId.THROWN)).isEqualTo(-1);
        assertThat(faeron.getCalculatedTotalSkillOf(SkillItemId.THROWN)).isEqualTo(0);

        assertThat(faeron.getCalcValueOf(InventoryGroup.WEAPON, CalcAttributeId.BASE_HIT)).isEqualTo(60);
        assertThat(faeron.getCalcValueOf(InventoryGroup.WEAPON, CalcAttributeId.DAMAGE)).isEqualTo(24);
        assertThat(faeron.getCalcValueOf(InventoryGroup.SHIELD, CalcAttributeId.DEFENSE)).isEqualTo(0);
        assertThat(faeron.getTotalCalcOf(CalcAttributeId.PROTECTION)).isEqualTo(4);
    }

    @Test
    void whenHeroesAreCreated_ShouldHaveRightStats() {
        final HeroItem mozes = party.getHero("mozes");
        final HeroItem luana = heroes.getHero("luana");
        final HeroItem valter = heroes.getHero("valter");
        final HeroItem luthais = heroes.getHero("luthais");

        assertThat(party.getHero(0)).isEqualTo(mozes);

        assertThat(mozes.getSkillValueOf(InventoryGroup.SHIELD, SkillItemId.STEALTH)).isEqualTo(-5);
        assertThat(mozes.getStatValueOf(InventoryGroup.SHIELD, StatItemId.DEXTERITY)).isEqualTo(-2);
        assertThat(mozes.getId()).isEqualTo("mozes");
        assertThat(mozes.getName()).isEqualTo("Mozes");
        assertThat(mozes.getSchool()).isEqualTo(SchoolType.UNKNOWN);
        assertThat(mozes.equalsHero(mozes)).isTrue();
        assertThat(mozes.getXpDeltaBetweenLevels()).isEqualTo(20);
        assertThat(mozes.getXpToInvest()).isEqualTo(0);
        assertThat(mozes.getXpCostForNextLevelOf(StatItemId.INTELLIGENCE)).isEqualTo(43);
        assertThat(mozes.getXpCostForNextLevelOf(SkillItemId.STEALTH)).isEqualTo(16);
        assertThat(mozes.getGoldCostForNextLevelOf(SkillItemId.STEALTH)).isEqualTo(8);

        assertThat(mozes.getInventoryItem(InventoryGroup.WEAPON)).get().hasFieldOrPropertyWithValue("id", "basic_shortsword");
        assertThat(mozes.getInventoryItem(InventoryGroup.SHIELD)).get().hasFieldOrPropertyWithValue("id", "basic_light_shield");
        assertThat(mozes.getInventoryItem(InventoryGroup.HELMET)).isEmpty();
        assertThat(mozes.getInventoryItem(InventoryGroup.CHEST)).get().hasFieldOrPropertyWithValue("id", "basic_medium_chest");

        assertThat(luana.getInventoryItem(InventoryGroup.WEAPON)).get().hasFieldOrPropertyWithValue("id", "basic_dagger");
        assertThat(luana.getInventoryItem(InventoryGroup.SHIELD)).isEmpty();
        assertThat(luana.getInventoryItem(InventoryGroup.HELMET)).isEmpty();
        assertThat(luana.getInventoryItem(InventoryGroup.CHEST)).get().hasFieldOrPropertyWithValue("id", "basic_light_chest");

        assertThat(luana.getAllSkillsAboveZero()).isEqualTo(List.of(SkillItemId.MECHANIC,
                                                                    SkillItemId.STEALTH,
                                                                    SkillItemId.THIEF,
                                                                    SkillItemId.SWORD,
                                                                    SkillItemId.THROWN));

        assertThat(valter.getAllSpells()).isEqualTo(List.of(SpellType.DRAGON_FLAMES));

        assertThat(luthais.getSpellRankOf(SpellType.FIREBALL)).isEqualTo(8);
        assertThat(luthais.getXpCostForNextLevelOf(SkillItemId.WIZARD)).isEqualTo(0);

        assertThat(CalcAttributeId.WEIGHT.getTitle()).isEqualTo("Weight");
        assertThat(CalcAttributeId.WEIGHT.getDescription(mozes)).isEqualTo("Weight");
        assertThat(SchoolType.NONE.getTitle()).isEqualTo("No");
        assertThat(SchoolType.NONE.getDescription(mozes)).isEqualTo("No");
        assertThat(SpellType.WIND.getTitle()).isEqualTo("Wind");
        assertThat(SpellType.WIND.getDescription(mozes)).isEqualTo("Wind");
        assertThat(InventoryGroup.WEAPON.getTitle()).isEqualTo("Weapon");
        assertThat(InventoryGroup.WEAPON.getDescription(mozes)).isEqualTo("Weapon");
        assertThat(InventoryMinimal.SKILL.getTitle()).isEqualTo("Skill");
        assertThat(InventoryMinimal.SKILL.getDescription(mozes)).isEqualTo("Skill");
    }

}
