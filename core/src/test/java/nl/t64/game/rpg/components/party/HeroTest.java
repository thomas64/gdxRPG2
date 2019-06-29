package nl.t64.game.rpg.components.party;

import nl.t64.game.rpg.GameData;
import nl.t64.game.rpg.GameTest;
import nl.t64.game.rpg.constants.Constant;
import nl.t64.game.rpg.profile.ProfileManager;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static nl.t64.game.rpg.components.party.InventoryGroup.*;
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
        assertThat(mozes.getInventoryItem(WEAPON)).get().hasFieldOrPropertyWithValue("id", "basic_shortsword");

        final InventoryItem newWeapon = InventoryDatabase.getInstance().getInventoryItem("masterwork_lance");
        mozes.forceSetInventoryItem(WEAPON, newWeapon);
        assertThat(mozes.getInventoryItem(WEAPON)).get().hasFieldOrPropertyWithValue("id", "masterwork_lance");
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

        softly.assertThat(mozes.getLevel()).isEqualTo(1);
        softly.assertThat(mozes.getTotalXp()).isEqualTo(5);
        softly.assertThat(mozes.getNeededXpForNextLevel()).isEqualTo(20);
        softly.assertThat(mozes.getCurrentHp()).isEqualTo(46);
        softly.assertThat(mozes.getMaximumHp()).isEqualTo(46);

        softly.assertThat(mozes.getOwnStatOf(StatType.INTELLIGENCE)).isEqualTo(18);
        softly.assertThat(mozes.getTotalStatOf(StatType.INTELLIGENCE)).isEqualTo(18);
        softly.assertThat(mozes.getOwnStatOf(StatType.DEXTERITY)).isEqualTo(15);
        softly.assertThat(mozes.getTotalStatOf(StatType.DEXTERITY)).isEqualTo(13);
        softly.assertThat(mozes.getOwnStatOf(StatType.ENDURANCE)).isEqualTo(15);
        softly.assertThat(mozes.getTotalStatOf(StatType.ENDURANCE)).isEqualTo(15);
        softly.assertThat(mozes.getOwnStatOf(StatType.STRENGTH)).isEqualTo(15);
        softly.assertThat(mozes.getTotalStatOf(StatType.STRENGTH)).isEqualTo(15);
        softly.assertThat(mozes.getOwnStatOf(StatType.STAMINA)).isEqualTo(30);
        softly.assertThat(mozes.getTotalStatOf(StatType.STAMINA)).isEqualTo(30);

        softly.assertThat(mozes.getOwnSkillOf(SkillType.STEALTH)).isEqualTo(1);
        softly.assertThat(mozes.getTotalSkillOf(SkillType.STEALTH)).isEqualTo(-5);
        softly.assertThat(mozes.getOwnSkillOf(SkillType.HAFTED)).isEqualTo(1);
        softly.assertThat(mozes.getTotalSkillOf(SkillType.HAFTED)).isEqualTo(1);
        softly.assertThat(mozes.getOwnSkillOf(SkillType.POLE)).isEqualTo(0);
        softly.assertThat(mozes.getTotalSkillOf(SkillType.POLE)).isEqualTo(0);
        softly.assertThat(mozes.getOwnSkillOf(SkillType.SHIELD)).isEqualTo(3);
        softly.assertThat(mozes.getTotalSkillOf(SkillType.SHIELD)).isEqualTo(3);
        softly.assertThat(mozes.getOwnSkillOf(SkillType.SWORD)).isEqualTo(3);
        softly.assertThat(mozes.getTotalSkillOf(SkillType.SWORD)).isEqualTo(3);

        softly.assertThat(mozes.getStatValueOf(WEAPON, StatType.BASE_HIT)).isEqualTo(40);
        softly.assertThat(mozes.getStatValueOf(WEAPON, StatType.DAMAGE)).isEqualTo(13);
        softly.assertThat(mozes.getStatValueOf(SHIELD, StatType.DEFENSE)).isEqualTo(5);
        softly.assertThat(mozes.getTotalStatOf(StatType.PROTECTION)).isEqualTo(6);

        softly.assertThat(mozes.getInventoryItem(WEAPON)).get().hasFieldOrPropertyWithValue("id", "basic_shortsword");
        softly.assertThat(mozes.getInventoryItem(SHIELD)).get().hasFieldOrPropertyWithValue("id", "basic_light_shield");
        softly.assertThat(mozes.getInventoryItem(HELMET)).isEmpty();
        softly.assertThat(mozes.getInventoryItem(CHEST)).get().hasFieldOrPropertyWithValue("id", "basic_medium_chest");

        //////

        softly.assertThat(luana.getLevel()).isEqualTo(1);
        softly.assertThat(luana.getTotalXp()).isEqualTo(5);
        softly.assertThat(luana.getNeededXpForNextLevel()).isEqualTo(20);
        softly.assertThat(luana.getCurrentHp()).isEqualTo(31);
        softly.assertThat(luana.getMaximumHp()).isEqualTo(31);

        softly.assertThat(luana.getOwnStatOf(StatType.INTELLIGENCE)).isEqualTo(14);
        softly.assertThat(luana.getTotalStatOf(StatType.INTELLIGENCE)).isEqualTo(14);
        softly.assertThat(luana.getOwnStatOf(StatType.DEXTERITY)).isEqualTo(22);
        softly.assertThat(luana.getTotalStatOf(StatType.DEXTERITY)).isEqualTo(22);
        softly.assertThat(luana.getOwnStatOf(StatType.ENDURANCE)).isEqualTo(10);
        softly.assertThat(luana.getTotalStatOf(StatType.ENDURANCE)).isEqualTo(10);
        softly.assertThat(luana.getOwnStatOf(StatType.STRENGTH)).isEqualTo(8);
        softly.assertThat(luana.getTotalStatOf(StatType.STRENGTH)).isEqualTo(8);
        softly.assertThat(luana.getOwnStatOf(StatType.STAMINA)).isEqualTo(20);
        softly.assertThat(luana.getTotalStatOf(StatType.STAMINA)).isEqualTo(20);

        softly.assertThat(luana.getOwnSkillOf(SkillType.STEALTH)).isEqualTo(3);
        softly.assertThat(luana.getTotalSkillOf(SkillType.STEALTH)).isEqualTo(3);
        softly.assertThat(luana.getOwnSkillOf(SkillType.HAFTED)).isEqualTo(-1);
        softly.assertThat(luana.getTotalSkillOf(SkillType.HAFTED)).isEqualTo(0);
        softly.assertThat(luana.getOwnSkillOf(SkillType.POLE)).isEqualTo(0);
        softly.assertThat(luana.getTotalSkillOf(SkillType.POLE)).isEqualTo(0);
        softly.assertThat(luana.getOwnSkillOf(SkillType.SHIELD)).isEqualTo(-1);
        softly.assertThat(luana.getTotalSkillOf(SkillType.SHIELD)).isEqualTo(0);
        softly.assertThat(luana.getOwnSkillOf(SkillType.SWORD)).isEqualTo(1);
        softly.assertThat(luana.getTotalSkillOf(SkillType.SWORD)).isEqualTo(1);

        softly.assertThat(luana.getStatValueOf(WEAPON, StatType.BASE_HIT)).isEqualTo(40);
        softly.assertThat(luana.getStatValueOf(WEAPON, StatType.DAMAGE)).isEqualTo(11);
        softly.assertThat(luana.getStatValueOf(SHIELD, StatType.DEFENSE)).isEqualTo(0);
        softly.assertThat(luana.getTotalStatOf(StatType.PROTECTION)).isEqualTo(1);

        softly.assertThat(luana.getInventoryItem(WEAPON)).get().hasFieldOrPropertyWithValue("id", "basic_dagger");
        softly.assertThat(luana.getInventoryItem(SHIELD)).isEmpty();
        softly.assertThat(luana.getInventoryItem(HELMET)).isEmpty();
        softly.assertThat(luana.getInventoryItem(CHEST)).get().hasFieldOrPropertyWithValue("id", "basic_light_chest");

        //////

        softly.assertThat(reignald.getLevel()).isEqualTo(8);
        softly.assertThat(reignald.getTotalXp()).isEqualTo(1020);
        softly.assertThat(reignald.getNeededXpForNextLevel()).isEqualTo(405);
        softly.assertThat(reignald.getCurrentHp()).isEqualTo(68);
        softly.assertThat(reignald.getMaximumHp()).isEqualTo(68);

        softly.assertThat(reignald.getOwnStatOf(StatType.INTELLIGENCE)).isEqualTo(10);
        softly.assertThat(reignald.getTotalStatOf(StatType.INTELLIGENCE)).isEqualTo(10);
        softly.assertThat(reignald.getOwnStatOf(StatType.DEXTERITY)).isEqualTo(25);
        softly.assertThat(reignald.getTotalStatOf(StatType.DEXTERITY)).isEqualTo(23);
        softly.assertThat(reignald.getOwnStatOf(StatType.ENDURANCE)).isEqualTo(20);
        softly.assertThat(reignald.getTotalStatOf(StatType.ENDURANCE)).isEqualTo(20);
        softly.assertThat(reignald.getOwnStatOf(StatType.STRENGTH)).isEqualTo(20);
        softly.assertThat(reignald.getTotalStatOf(StatType.STRENGTH)).isEqualTo(20);
        softly.assertThat(reignald.getOwnStatOf(StatType.STAMINA)).isEqualTo(40);
        softly.assertThat(reignald.getTotalStatOf(StatType.STAMINA)).isEqualTo(40);

        softly.assertThat(reignald.getOwnSkillOf(SkillType.STEALTH)).isEqualTo(1);
        softly.assertThat(reignald.getTotalSkillOf(SkillType.STEALTH)).isEqualTo(-5);
        softly.assertThat(reignald.getOwnSkillOf(SkillType.HAFTED)).isEqualTo(0);
        softly.assertThat(reignald.getTotalSkillOf(SkillType.HAFTED)).isEqualTo(0);
        softly.assertThat(reignald.getOwnSkillOf(SkillType.POLE)).isEqualTo(0);
        softly.assertThat(reignald.getTotalSkillOf(SkillType.POLE)).isEqualTo(0);
        softly.assertThat(reignald.getOwnSkillOf(SkillType.SHIELD)).isEqualTo(2);
        softly.assertThat(reignald.getTotalSkillOf(SkillType.SHIELD)).isEqualTo(2);
        softly.assertThat(reignald.getOwnSkillOf(SkillType.SWORD)).isEqualTo(4);
        softly.assertThat(reignald.getTotalSkillOf(SkillType.SWORD)).isEqualTo(4);

        softly.assertThat(reignald.getStatValueOf(WEAPON, StatType.BASE_HIT)).isEqualTo(50);
        softly.assertThat(reignald.getStatValueOf(WEAPON, StatType.DAMAGE)).isEqualTo(15);
        softly.assertThat(reignald.getStatValueOf(SHIELD, StatType.DEFENSE)).isEqualTo(10);
        softly.assertThat(reignald.getTotalStatOf(StatType.PROTECTION)).isEqualTo(8);

        //////

        softly.assertThat(ryiah.getLevel()).isEqualTo(3);
        softly.assertThat(ryiah.getTotalXp()).isEqualTo(70);
        softly.assertThat(ryiah.getNeededXpForNextLevel()).isEqualTo(80);
        softly.assertThat(ryiah.getCurrentHp()).isEqualTo(50);
        softly.assertThat(ryiah.getMaximumHp()).isEqualTo(50);

        softly.assertThat(ryiah.getOwnStatOf(StatType.INTELLIGENCE)).isEqualTo(22);
        softly.assertThat(ryiah.getTotalStatOf(StatType.INTELLIGENCE)).isEqualTo(22);
        softly.assertThat(ryiah.getOwnStatOf(StatType.DEXTERITY)).isEqualTo(20);
        softly.assertThat(ryiah.getTotalStatOf(StatType.DEXTERITY)).isEqualTo(20);
        softly.assertThat(ryiah.getOwnStatOf(StatType.ENDURANCE)).isEqualTo(16);
        softly.assertThat(ryiah.getTotalStatOf(StatType.ENDURANCE)).isEqualTo(16);
        softly.assertThat(ryiah.getOwnStatOf(StatType.STRENGTH)).isEqualTo(10);
        softly.assertThat(ryiah.getTotalStatOf(StatType.STRENGTH)).isEqualTo(10);
        softly.assertThat(ryiah.getOwnStatOf(StatType.STAMINA)).isEqualTo(31);
        softly.assertThat(ryiah.getTotalStatOf(StatType.STAMINA)).isEqualTo(31);

        softly.assertThat(ryiah.getOwnSkillOf(SkillType.STEALTH)).isEqualTo(0);
        softly.assertThat(ryiah.getTotalSkillOf(SkillType.STEALTH)).isEqualTo(0);
        softly.assertThat(ryiah.getOwnSkillOf(SkillType.HAFTED)).isEqualTo(0);
        softly.assertThat(ryiah.getTotalSkillOf(SkillType.HAFTED)).isEqualTo(0);
        softly.assertThat(ryiah.getOwnSkillOf(SkillType.POLE)).isEqualTo(3);
        softly.assertThat(ryiah.getTotalSkillOf(SkillType.POLE)).isEqualTo(3);
        softly.assertThat(ryiah.getOwnSkillOf(SkillType.SHIELD)).isEqualTo(0);
        softly.assertThat(ryiah.getTotalSkillOf(SkillType.SHIELD)).isEqualTo(0);
        softly.assertThat(ryiah.getOwnSkillOf(SkillType.SWORD)).isEqualTo(3);
        softly.assertThat(ryiah.getTotalSkillOf(SkillType.SWORD)).isEqualTo(3);

        softly.assertThat(ryiah.getStatValueOf(WEAPON, StatType.BASE_HIT)).isEqualTo(50);
        softly.assertThat(ryiah.getStatValueOf(WEAPON, StatType.DAMAGE)).isEqualTo(4);
        softly.assertThat(ryiah.getStatValueOf(SHIELD, StatType.DEFENSE)).isEqualTo(0);
        softly.assertThat(ryiah.getTotalStatOf(StatType.PROTECTION)).isEqualTo(1);

        //////

        softly.assertThat(valter.getLevel()).isEqualTo(2);
        softly.assertThat(valter.getTotalXp()).isEqualTo(25);
        softly.assertThat(valter.getNeededXpForNextLevel()).isEqualTo(45);
        softly.assertThat(valter.getCurrentHp()).isEqualTo(37);
        softly.assertThat(valter.getMaximumHp()).isEqualTo(37);

        softly.assertThat(valter.getOwnStatOf(StatType.INTELLIGENCE)).isEqualTo(22);
        softly.assertThat(valter.getTotalStatOf(StatType.INTELLIGENCE)).isEqualTo(22);
        softly.assertThat(valter.getOwnStatOf(StatType.DEXTERITY)).isEqualTo(15);
        softly.assertThat(valter.getTotalStatOf(StatType.DEXTERITY)).isEqualTo(15);
        softly.assertThat(valter.getOwnStatOf(StatType.ENDURANCE)).isEqualTo(15);
        softly.assertThat(valter.getTotalStatOf(StatType.ENDURANCE)).isEqualTo(15);
        softly.assertThat(valter.getOwnStatOf(StatType.STRENGTH)).isEqualTo(10);
        softly.assertThat(valter.getTotalStatOf(StatType.STRENGTH)).isEqualTo(10);
        softly.assertThat(valter.getOwnStatOf(StatType.STAMINA)).isEqualTo(20);
        softly.assertThat(valter.getTotalStatOf(StatType.STAMINA)).isEqualTo(20);

        softly.assertThat(valter.getOwnSkillOf(SkillType.STEALTH)).isEqualTo(0);
        softly.assertThat(valter.getTotalSkillOf(SkillType.STEALTH)).isEqualTo(0);
        softly.assertThat(valter.getOwnSkillOf(SkillType.HAFTED)).isEqualTo(-1);
        softly.assertThat(valter.getTotalSkillOf(SkillType.HAFTED)).isEqualTo(0);
        softly.assertThat(valter.getOwnSkillOf(SkillType.POLE)).isEqualTo(0);
        softly.assertThat(valter.getTotalSkillOf(SkillType.POLE)).isEqualTo(0);
        softly.assertThat(valter.getOwnSkillOf(SkillType.SHIELD)).isEqualTo(-1);
        softly.assertThat(valter.getTotalSkillOf(SkillType.SHIELD)).isEqualTo(0);
        softly.assertThat(valter.getOwnSkillOf(SkillType.SWORD)).isEqualTo(1);
        softly.assertThat(valter.getTotalSkillOf(SkillType.SWORD)).isEqualTo(1);

        softly.assertThat(valter.getStatValueOf(WEAPON, StatType.BASE_HIT)).isEqualTo(40);
        softly.assertThat(valter.getStatValueOf(WEAPON, StatType.DAMAGE)).isEqualTo(11);
        softly.assertThat(valter.getStatValueOf(SHIELD, StatType.DEFENSE)).isEqualTo(0);
        softly.assertThat(valter.getTotalStatOf(StatType.PROTECTION)).isEqualTo(1);

        //////

        softly.assertThat(galen.getLevel()).isEqualTo(4);
        softly.assertThat(galen.getTotalXp()).isEqualTo(150);
        softly.assertThat(galen.getNeededXpForNextLevel()).isEqualTo(125);
        softly.assertThat(galen.getCurrentHp()).isEqualTo(64);
        softly.assertThat(galen.getMaximumHp()).isEqualTo(64);

        softly.assertThat(galen.getOwnStatOf(StatType.INTELLIGENCE)).isEqualTo(15);
        softly.assertThat(galen.getTotalStatOf(StatType.INTELLIGENCE)).isEqualTo(15);
        softly.assertThat(galen.getOwnStatOf(StatType.DEXTERITY)).isEqualTo(18);
        softly.assertThat(galen.getTotalStatOf(StatType.DEXTERITY)).isEqualTo(12);
        softly.assertThat(galen.getOwnStatOf(StatType.ENDURANCE)).isEqualTo(20);
        softly.assertThat(galen.getTotalStatOf(StatType.ENDURANCE)).isEqualTo(20);
        softly.assertThat(galen.getOwnStatOf(StatType.STRENGTH)).isEqualTo(25);
        softly.assertThat(galen.getTotalStatOf(StatType.STRENGTH)).isEqualTo(25);
        softly.assertThat(galen.getOwnStatOf(StatType.STAMINA)).isEqualTo(40);
        softly.assertThat(galen.getTotalStatOf(StatType.STAMINA)).isEqualTo(40);

        softly.assertThat(galen.getOwnSkillOf(SkillType.STEALTH)).isEqualTo(3);
        softly.assertThat(galen.getTotalSkillOf(SkillType.STEALTH)).isEqualTo(-8);
        softly.assertThat(galen.getOwnSkillOf(SkillType.HAFTED)).isEqualTo(5);
        softly.assertThat(galen.getTotalSkillOf(SkillType.HAFTED)).isEqualTo(5);
        softly.assertThat(galen.getOwnSkillOf(SkillType.POLE)).isEqualTo(0);
        softly.assertThat(galen.getTotalSkillOf(SkillType.POLE)).isEqualTo(0);
        softly.assertThat(galen.getOwnSkillOf(SkillType.SHIELD)).isEqualTo(3);
        softly.assertThat(galen.getTotalSkillOf(SkillType.SHIELD)).isEqualTo(3);
        softly.assertThat(galen.getOwnSkillOf(SkillType.SWORD)).isEqualTo(-1);
        softly.assertThat(galen.getTotalSkillOf(SkillType.SWORD)).isEqualTo(0);

        softly.assertThat(galen.getStatValueOf(WEAPON, StatType.BASE_HIT)).isEqualTo(30);
        softly.assertThat(galen.getStatValueOf(WEAPON, StatType.DAMAGE)).isEqualTo(20);
        softly.assertThat(galen.getStatValueOf(SHIELD, StatType.DEFENSE)).isEqualTo(10);
        softly.assertThat(galen.getTotalStatOf(StatType.PROTECTION)).isEqualTo(10);

        //////

        softly.assertThat(jaspar.getLevel()).isEqualTo(12);
        softly.assertThat(jaspar.getTotalXp()).isEqualTo(3250);
        softly.assertThat(jaspar.getNeededXpForNextLevel()).isEqualTo(845);
        softly.assertThat(jaspar.getCurrentHp()).isEqualTo(102);
        softly.assertThat(jaspar.getMaximumHp()).isEqualTo(102);

        softly.assertThat(jaspar.getOwnStatOf(StatType.INTELLIGENCE)).isEqualTo(6);
        softly.assertThat(jaspar.getTotalStatOf(StatType.INTELLIGENCE)).isEqualTo(6);
        softly.assertThat(jaspar.getOwnStatOf(StatType.DEXTERITY)).isEqualTo(14);
        softly.assertThat(jaspar.getTotalStatOf(StatType.DEXTERITY)).isEqualTo(4);
        softly.assertThat(jaspar.getOwnStatOf(StatType.ENDURANCE)).isEqualTo(30);
        softly.assertThat(jaspar.getTotalStatOf(StatType.ENDURANCE)).isEqualTo(30);
        softly.assertThat(jaspar.getOwnStatOf(StatType.STRENGTH)).isEqualTo(30);
        softly.assertThat(jaspar.getTotalStatOf(StatType.STRENGTH)).isEqualTo(30);
        softly.assertThat(jaspar.getOwnStatOf(StatType.STAMINA)).isEqualTo(60);
        softly.assertThat(jaspar.getTotalStatOf(StatType.STAMINA)).isEqualTo(60);

        softly.assertThat(jaspar.getOwnSkillOf(SkillType.STEALTH)).isEqualTo(1);
        softly.assertThat(jaspar.getTotalSkillOf(SkillType.STEALTH)).isEqualTo(-16);
        softly.assertThat(jaspar.getOwnSkillOf(SkillType.HAFTED)).isEqualTo(0);
        softly.assertThat(jaspar.getTotalSkillOf(SkillType.HAFTED)).isEqualTo(0);
        softly.assertThat(jaspar.getOwnSkillOf(SkillType.POLE)).isEqualTo(6);
        softly.assertThat(jaspar.getTotalSkillOf(SkillType.POLE)).isEqualTo(6);
        softly.assertThat(jaspar.getOwnSkillOf(SkillType.SHIELD)).isEqualTo(4);
        softly.assertThat(jaspar.getTotalSkillOf(SkillType.SHIELD)).isEqualTo(4);
        softly.assertThat(jaspar.getOwnSkillOf(SkillType.SWORD)).isEqualTo(6);
        softly.assertThat(jaspar.getTotalSkillOf(SkillType.SWORD)).isEqualTo(6);

        softly.assertThat(jaspar.getStatValueOf(WEAPON, StatType.BASE_HIT)).isEqualTo(40);
        softly.assertThat(jaspar.getStatValueOf(WEAPON, StatType.DAMAGE)).isEqualTo(17);
        softly.assertThat(jaspar.getStatValueOf(SHIELD, StatType.DEFENSE)).isEqualTo(15);
        softly.assertThat(jaspar.getTotalStatOf(StatType.PROTECTION)).isEqualTo(18);

        //////

        softly.assertThat(kiara.getLevel()).isEqualTo(12);
        softly.assertThat(kiara.getTotalXp()).isEqualTo(3250);
        softly.assertThat(kiara.getNeededXpForNextLevel()).isEqualTo(845);
        softly.assertThat(kiara.getCurrentHp()).isEqualTo(72);
        softly.assertThat(kiara.getMaximumHp()).isEqualTo(72);

        softly.assertThat(kiara.getOwnStatOf(StatType.INTELLIGENCE)).isEqualTo(15);
        softly.assertThat(kiara.getTotalStatOf(StatType.INTELLIGENCE)).isEqualTo(15);
        softly.assertThat(kiara.getOwnStatOf(StatType.DEXTERITY)).isEqualTo(30);
        softly.assertThat(kiara.getTotalStatOf(StatType.DEXTERITY)).isEqualTo(30);
        softly.assertThat(kiara.getOwnStatOf(StatType.ENDURANCE)).isEqualTo(20);
        softly.assertThat(kiara.getTotalStatOf(StatType.ENDURANCE)).isEqualTo(20);
        softly.assertThat(kiara.getOwnStatOf(StatType.STRENGTH)).isEqualTo(15);
        softly.assertThat(kiara.getTotalStatOf(StatType.STRENGTH)).isEqualTo(15);
        softly.assertThat(kiara.getOwnStatOf(StatType.STAMINA)).isEqualTo(40);
        softly.assertThat(kiara.getTotalStatOf(StatType.STAMINA)).isEqualTo(40);

        softly.assertThat(kiara.getOwnSkillOf(SkillType.STEALTH)).isEqualTo(5);
        softly.assertThat(kiara.getTotalSkillOf(SkillType.STEALTH)).isEqualTo(5);
        softly.assertThat(kiara.getOwnSkillOf(SkillType.HAFTED)).isEqualTo(-1);
        softly.assertThat(kiara.getTotalSkillOf(SkillType.HAFTED)).isEqualTo(0);
        softly.assertThat(kiara.getOwnSkillOf(SkillType.POLE)).isEqualTo(2);
        softly.assertThat(kiara.getTotalSkillOf(SkillType.POLE)).isEqualTo(2);
        softly.assertThat(kiara.getOwnSkillOf(SkillType.SHIELD)).isEqualTo(-1);
        softly.assertThat(kiara.getTotalSkillOf(SkillType.SHIELD)).isEqualTo(0);
        softly.assertThat(kiara.getOwnSkillOf(SkillType.SWORD)).isEqualTo(7);
        softly.assertThat(kiara.getTotalSkillOf(SkillType.SWORD)).isEqualTo(7);

        softly.assertThat(kiara.getStatValueOf(WEAPON, StatType.BASE_HIT)).isEqualTo(50);
        softly.assertThat(kiara.getStatValueOf(WEAPON, StatType.DAMAGE)).isEqualTo(13);
        softly.assertThat(kiara.getStatValueOf(SHIELD, StatType.DEFENSE)).isEqualTo(0);
        softly.assertThat(kiara.getTotalStatOf(StatType.PROTECTION)).isEqualTo(2);

        //////

        softly.assertThat(luthais.getLevel()).isEqualTo(20);
        softly.assertThat(luthais.getTotalXp()).isEqualTo(14350);
        softly.assertThat(luthais.getNeededXpForNextLevel()).isEqualTo(2205);
        softly.assertThat(luthais.getCurrentHp()).isEqualTo(88);
        softly.assertThat(luthais.getMaximumHp()).isEqualTo(88);

        softly.assertThat(luthais.getOwnStatOf(StatType.INTELLIGENCE)).isEqualTo(30);
        softly.assertThat(luthais.getTotalStatOf(StatType.INTELLIGENCE)).isEqualTo(30);
        softly.assertThat(luthais.getOwnStatOf(StatType.DEXTERITY)).isEqualTo(20);
        softly.assertThat(luthais.getTotalStatOf(StatType.DEXTERITY)).isEqualTo(20);
        softly.assertThat(luthais.getOwnStatOf(StatType.ENDURANCE)).isEqualTo(18);
        softly.assertThat(luthais.getTotalStatOf(StatType.ENDURANCE)).isEqualTo(18);
        softly.assertThat(luthais.getOwnStatOf(StatType.STRENGTH)).isEqualTo(8);
        softly.assertThat(luthais.getTotalStatOf(StatType.STRENGTH)).isEqualTo(8);
        softly.assertThat(luthais.getOwnStatOf(StatType.STAMINA)).isEqualTo(50);
        softly.assertThat(luthais.getTotalStatOf(StatType.STAMINA)).isEqualTo(50);

        softly.assertThat(luthais.getOwnSkillOf(SkillType.STEALTH)).isEqualTo(5);
        softly.assertThat(luthais.getTotalSkillOf(SkillType.STEALTH)).isEqualTo(5);
        softly.assertThat(luthais.getOwnSkillOf(SkillType.HAFTED)).isEqualTo(0);
        softly.assertThat(luthais.getTotalSkillOf(SkillType.HAFTED)).isEqualTo(0);
        softly.assertThat(luthais.getOwnSkillOf(SkillType.POLE)).isEqualTo(8);
        softly.assertThat(luthais.getTotalSkillOf(SkillType.POLE)).isEqualTo(8);
        softly.assertThat(luthais.getOwnSkillOf(SkillType.SHIELD)).isEqualTo(-1);
        softly.assertThat(luthais.getTotalSkillOf(SkillType.SHIELD)).isEqualTo(0);
        softly.assertThat(luthais.getOwnSkillOf(SkillType.SWORD)).isEqualTo(0);
        softly.assertThat(luthais.getTotalSkillOf(SkillType.SWORD)).isEqualTo(0);

        softly.assertThat(luthais.getStatValueOf(WEAPON, StatType.BASE_HIT)).isEqualTo(80);
        softly.assertThat(luthais.getStatValueOf(WEAPON, StatType.DAMAGE)).isEqualTo(10);
        softly.assertThat(luthais.getStatValueOf(SHIELD, StatType.DEFENSE)).isEqualTo(0);
        softly.assertThat(luthais.getTotalStatOf(StatType.PROTECTION)).isEqualTo(3);

        //////

        softly.assertThat(elias.getLevel()).isEqualTo(18);
        softly.assertThat(elias.getTotalXp()).isEqualTo(10545);
        softly.assertThat(elias.getNeededXpForNextLevel()).isEqualTo(1805);
        softly.assertThat(elias.getCurrentHp()).isEqualTo(108);
        softly.assertThat(elias.getMaximumHp()).isEqualTo(108);

        softly.assertThat(elias.getOwnStatOf(StatType.INTELLIGENCE)).isEqualTo(30);
        softly.assertThat(elias.getTotalStatOf(StatType.INTELLIGENCE)).isEqualTo(30);
        softly.assertThat(elias.getOwnStatOf(StatType.DEXTERITY)).isEqualTo(25);
        softly.assertThat(elias.getTotalStatOf(StatType.DEXTERITY)).isEqualTo(25);
        softly.assertThat(elias.getOwnStatOf(StatType.ENDURANCE)).isEqualTo(30);
        softly.assertThat(elias.getTotalStatOf(StatType.ENDURANCE)).isEqualTo(30);
        softly.assertThat(elias.getOwnStatOf(StatType.STRENGTH)).isEqualTo(20);
        softly.assertThat(elias.getTotalStatOf(StatType.STRENGTH)).isEqualTo(20);
        softly.assertThat(elias.getOwnStatOf(StatType.STAMINA)).isEqualTo(60);
        softly.assertThat(elias.getTotalStatOf(StatType.STAMINA)).isEqualTo(60);

        softly.assertThat(elias.getOwnSkillOf(SkillType.STEALTH)).isEqualTo(0);
        softly.assertThat(elias.getTotalSkillOf(SkillType.STEALTH)).isEqualTo(0);
        softly.assertThat(elias.getOwnSkillOf(SkillType.HAFTED)).isEqualTo(5);
        softly.assertThat(elias.getTotalSkillOf(SkillType.HAFTED)).isEqualTo(5);
        softly.assertThat(elias.getOwnSkillOf(SkillType.POLE)).isEqualTo(5);
        softly.assertThat(elias.getTotalSkillOf(SkillType.POLE)).isEqualTo(5);
        softly.assertThat(elias.getOwnSkillOf(SkillType.SHIELD)).isEqualTo(-1);
        softly.assertThat(elias.getTotalSkillOf(SkillType.SHIELD)).isEqualTo(0);
        softly.assertThat(elias.getOwnSkillOf(SkillType.SWORD)).isEqualTo(7);
        softly.assertThat(elias.getTotalSkillOf(SkillType.SWORD)).isEqualTo(7);

        softly.assertThat(elias.getStatValueOf(WEAPON, StatType.BASE_HIT)).isEqualTo(50);
        softly.assertThat(elias.getStatValueOf(WEAPON, StatType.DAMAGE)).isEqualTo(17);
        softly.assertThat(elias.getStatValueOf(SHIELD, StatType.DEFENSE)).isEqualTo(0);
        softly.assertThat(elias.getTotalStatOf(StatType.PROTECTION)).isEqualTo(6);

        //////

        softly.assertThat(onarr.getLevel()).isEqualTo(18);
        softly.assertThat(onarr.getTotalXp()).isEqualTo(10545);
        softly.assertThat(onarr.getNeededXpForNextLevel()).isEqualTo(1805);
        softly.assertThat(onarr.getCurrentHp()).isEqualTo(108);
        softly.assertThat(onarr.getMaximumHp()).isEqualTo(108);

        softly.assertThat(onarr.getOwnStatOf(StatType.INTELLIGENCE)).isEqualTo(30);
        softly.assertThat(onarr.getTotalStatOf(StatType.INTELLIGENCE)).isEqualTo(30);
        softly.assertThat(onarr.getOwnStatOf(StatType.DEXTERITY)).isEqualTo(23);
        softly.assertThat(onarr.getTotalStatOf(StatType.DEXTERITY)).isEqualTo(17);
        softly.assertThat(onarr.getOwnStatOf(StatType.ENDURANCE)).isEqualTo(30);
        softly.assertThat(onarr.getTotalStatOf(StatType.ENDURANCE)).isEqualTo(30);
        softly.assertThat(onarr.getOwnStatOf(StatType.STRENGTH)).isEqualTo(25);
        softly.assertThat(onarr.getTotalStatOf(StatType.STRENGTH)).isEqualTo(25);
        softly.assertThat(onarr.getOwnStatOf(StatType.STAMINA)).isEqualTo(60);
        softly.assertThat(onarr.getTotalStatOf(StatType.STAMINA)).isEqualTo(60);

        softly.assertThat(onarr.getOwnSkillOf(SkillType.STEALTH)).isEqualTo(0);
        softly.assertThat(onarr.getTotalSkillOf(SkillType.STEALTH)).isEqualTo(0);
        softly.assertThat(onarr.getOwnSkillOf(SkillType.HAFTED)).isEqualTo(8);
        softly.assertThat(onarr.getTotalSkillOf(SkillType.HAFTED)).isEqualTo(8);
        softly.assertThat(onarr.getOwnSkillOf(SkillType.POLE)).isEqualTo(8);
        softly.assertThat(onarr.getTotalSkillOf(SkillType.POLE)).isEqualTo(8);
        softly.assertThat(onarr.getOwnSkillOf(SkillType.SHIELD)).isEqualTo(9);
        softly.assertThat(onarr.getTotalSkillOf(SkillType.SHIELD)).isEqualTo(9);
        softly.assertThat(onarr.getOwnSkillOf(SkillType.SWORD)).isEqualTo(5);
        softly.assertThat(onarr.getTotalSkillOf(SkillType.SWORD)).isEqualTo(5);

        softly.assertThat(onarr.getStatValueOf(WEAPON, StatType.BASE_HIT)).isEqualTo(40);
        softly.assertThat(onarr.getStatValueOf(WEAPON, StatType.DAMAGE)).isEqualTo(24);
        softly.assertThat(onarr.getStatValueOf(SHIELD, StatType.DEFENSE)).isEqualTo(15);
        softly.assertThat(onarr.getTotalStatOf(StatType.PROTECTION)).isEqualTo(16);

        //////

        softly.assertThat(duilio.getLevel()).isEqualTo(22);
        softly.assertThat(duilio.getTotalXp()).isEqualTo(18975);
        softly.assertThat(duilio.getNeededXpForNextLevel()).isEqualTo(2645);
        softly.assertThat(duilio.getCurrentHp()).isEqualTo(122);
        softly.assertThat(duilio.getMaximumHp()).isEqualTo(122);

        softly.assertThat(duilio.getOwnStatOf(StatType.INTELLIGENCE)).isEqualTo(25);
        softly.assertThat(duilio.getTotalStatOf(StatType.INTELLIGENCE)).isEqualTo(25);
        softly.assertThat(duilio.getOwnStatOf(StatType.DEXTERITY)).isEqualTo(30);
        softly.assertThat(duilio.getTotalStatOf(StatType.DEXTERITY)).isEqualTo(28);
        softly.assertThat(duilio.getOwnStatOf(StatType.ENDURANCE)).isEqualTo(25);
        softly.assertThat(duilio.getTotalStatOf(StatType.ENDURANCE)).isEqualTo(25);
        softly.assertThat(duilio.getOwnStatOf(StatType.STRENGTH)).isEqualTo(25);
        softly.assertThat(duilio.getTotalStatOf(StatType.STRENGTH)).isEqualTo(25);
        softly.assertThat(duilio.getOwnStatOf(StatType.STAMINA)).isEqualTo(75);
        softly.assertThat(duilio.getTotalStatOf(StatType.STAMINA)).isEqualTo(75);

        softly.assertThat(duilio.getOwnSkillOf(SkillType.STEALTH)).isEqualTo(5);
        softly.assertThat(duilio.getTotalSkillOf(SkillType.STEALTH)).isEqualTo(-1);
        softly.assertThat(duilio.getOwnSkillOf(SkillType.HAFTED)).isEqualTo(10);
        softly.assertThat(duilio.getTotalSkillOf(SkillType.HAFTED)).isEqualTo(10);
        softly.assertThat(duilio.getOwnSkillOf(SkillType.POLE)).isEqualTo(10);
        softly.assertThat(duilio.getTotalSkillOf(SkillType.POLE)).isEqualTo(10);
        softly.assertThat(duilio.getOwnSkillOf(SkillType.SHIELD)).isEqualTo(10);
        softly.assertThat(duilio.getTotalSkillOf(SkillType.SHIELD)).isEqualTo(10);
        softly.assertThat(duilio.getOwnSkillOf(SkillType.SWORD)).isEqualTo(10);
        softly.assertThat(duilio.getTotalSkillOf(SkillType.SWORD)).isEqualTo(10);

        softly.assertThat(duilio.getStatValueOf(WEAPON, StatType.BASE_HIT)).isEqualTo(60);
        softly.assertThat(duilio.getStatValueOf(WEAPON, StatType.DAMAGE)).isEqualTo(17);
        softly.assertThat(duilio.getStatValueOf(SHIELD, StatType.DEFENSE)).isEqualTo(15);
        softly.assertThat(duilio.getTotalStatOf(StatType.PROTECTION)).isEqualTo(10);

        //////

        softly.assertThat(iellwen.getLevel()).isEqualTo(20);
        softly.assertThat(iellwen.getTotalXp()).isEqualTo(14350);
        softly.assertThat(iellwen.getNeededXpForNextLevel()).isEqualTo(2205);
        softly.assertThat(iellwen.getCurrentHp()).isEqualTo(110);
        softly.assertThat(iellwen.getMaximumHp()).isEqualTo(110);

        softly.assertThat(iellwen.getOwnStatOf(StatType.INTELLIGENCE)).isEqualTo(35);
        softly.assertThat(iellwen.getTotalStatOf(StatType.INTELLIGENCE)).isEqualTo(35);
        softly.assertThat(iellwen.getOwnStatOf(StatType.DEXTERITY)).isEqualTo(30);
        softly.assertThat(iellwen.getTotalStatOf(StatType.DEXTERITY)).isEqualTo(30);
        softly.assertThat(iellwen.getOwnStatOf(StatType.ENDURANCE)).isEqualTo(30);
        softly.assertThat(iellwen.getTotalStatOf(StatType.ENDURANCE)).isEqualTo(30);
        softly.assertThat(iellwen.getOwnStatOf(StatType.STRENGTH)).isEqualTo(20);
        softly.assertThat(iellwen.getTotalStatOf(StatType.STRENGTH)).isEqualTo(20);
        softly.assertThat(iellwen.getOwnStatOf(StatType.STAMINA)).isEqualTo(60);
        softly.assertThat(iellwen.getTotalStatOf(StatType.STAMINA)).isEqualTo(60);

        softly.assertThat(iellwen.getOwnSkillOf(SkillType.STEALTH)).isEqualTo(6);
        softly.assertThat(iellwen.getTotalSkillOf(SkillType.STEALTH)).isEqualTo(5);
        softly.assertThat(iellwen.getOwnSkillOf(SkillType.HAFTED)).isEqualTo(5);
        softly.assertThat(iellwen.getTotalSkillOf(SkillType.HAFTED)).isEqualTo(5);
        softly.assertThat(iellwen.getOwnSkillOf(SkillType.POLE)).isEqualTo(0);
        softly.assertThat(iellwen.getTotalSkillOf(SkillType.POLE)).isEqualTo(0);
        softly.assertThat(iellwen.getOwnSkillOf(SkillType.SHIELD)).isEqualTo(-1);
        softly.assertThat(iellwen.getTotalSkillOf(SkillType.SHIELD)).isEqualTo(0);
        softly.assertThat(iellwen.getOwnSkillOf(SkillType.SWORD)).isEqualTo(10);
        softly.assertThat(iellwen.getTotalSkillOf(SkillType.SWORD)).isEqualTo(10);

        softly.assertThat(iellwen.getStatValueOf(WEAPON, StatType.BASE_HIT)).isEqualTo(60);
        softly.assertThat(iellwen.getStatValueOf(WEAPON, StatType.DAMAGE)).isEqualTo(17);
        softly.assertThat(iellwen.getStatValueOf(SHIELD, StatType.DEFENSE)).isEqualTo(0);
        softly.assertThat(iellwen.getTotalStatOf(StatType.PROTECTION)).isEqualTo(7);

        //////

        softly.assertThat(faeron.getLevel()).isEqualTo(25);
        softly.assertThat(faeron.getTotalXp()).isEqualTo(27625);
        softly.assertThat(faeron.getNeededXpForNextLevel()).isEqualTo(3380);
        softly.assertThat(faeron.getCurrentHp()).isEqualTo(130);
        softly.assertThat(faeron.getMaximumHp()).isEqualTo(130);

        softly.assertThat(faeron.getOwnStatOf(StatType.INTELLIGENCE)).isEqualTo(30);
        softly.assertThat(faeron.getTotalStatOf(StatType.INTELLIGENCE)).isEqualTo(30);
        softly.assertThat(faeron.getOwnStatOf(StatType.DEXTERITY)).isEqualTo(30);
        softly.assertThat(faeron.getTotalStatOf(StatType.DEXTERITY)).isEqualTo(30);
        softly.assertThat(faeron.getOwnStatOf(StatType.ENDURANCE)).isEqualTo(25);
        softly.assertThat(faeron.getTotalStatOf(StatType.ENDURANCE)).isEqualTo(25);
        softly.assertThat(faeron.getOwnStatOf(StatType.STRENGTH)).isEqualTo(15);
        softly.assertThat(faeron.getTotalStatOf(StatType.STRENGTH)).isEqualTo(15);
        softly.assertThat(faeron.getOwnStatOf(StatType.STAMINA)).isEqualTo(80);
        softly.assertThat(faeron.getTotalStatOf(StatType.STAMINA)).isEqualTo(80);

        softly.assertThat(faeron.getOwnSkillOf(SkillType.STEALTH)).isEqualTo(10);
        softly.assertThat(faeron.getTotalSkillOf(SkillType.STEALTH)).isEqualTo(10);
        softly.assertThat(faeron.getOwnSkillOf(SkillType.HAFTED)).isEqualTo(10);
        softly.assertThat(faeron.getTotalSkillOf(SkillType.HAFTED)).isEqualTo(10);
        softly.assertThat(faeron.getOwnSkillOf(SkillType.POLE)).isEqualTo(-1);
        softly.assertThat(faeron.getTotalSkillOf(SkillType.POLE)).isEqualTo(0);
        softly.assertThat(faeron.getOwnSkillOf(SkillType.SHIELD)).isEqualTo(0);
        softly.assertThat(faeron.getTotalSkillOf(SkillType.SHIELD)).isEqualTo(0);
        softly.assertThat(faeron.getOwnSkillOf(SkillType.SWORD)).isEqualTo(10);
        softly.assertThat(faeron.getTotalSkillOf(SkillType.SWORD)).isEqualTo(10);

        softly.assertThat(faeron.getStatValueOf(WEAPON, StatType.BASE_HIT)).isEqualTo(60);
        softly.assertThat(faeron.getStatValueOf(WEAPON, StatType.DAMAGE)).isEqualTo(24);
        softly.assertThat(faeron.getStatValueOf(SHIELD, StatType.DEFENSE)).isEqualTo(0);
        softly.assertThat(faeron.getTotalStatOf(StatType.PROTECTION)).isEqualTo(4);

        //////

        softly.assertAll();
    }

}
