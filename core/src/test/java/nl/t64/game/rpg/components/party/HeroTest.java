package nl.t64.game.rpg.components.party;

import nl.t64.game.rpg.GameData;
import nl.t64.game.rpg.GameTest;
import nl.t64.game.rpg.constants.Constant;
import nl.t64.game.rpg.profile.ProfileManager;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static nl.t64.game.rpg.components.inventory.InventoryGroup.*;
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

    private void addHeroToParty(String id) {
        HeroItem hero = heroes.getHero(id);
        heroes.removeHero(id);
        party.addHero(hero);
    }

    private void removeHeroFromParty(String id) {
        HeroItem hero = party.getHero(id);
        party.removeHero(id);
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

        assertThat(heroes.getSize()).isEqualTo(9);
        assertThat(party.getSize()).isEqualTo(5);

        assertThatExceptionOfType(IllegalStateException.class)
                .isThrownBy(() -> addHeroToParty("galen"))
                .withMessage("Party is full.");
    }

    @Test
    void whenHeroesAreCreated_ShouldHaveRightStats() {
        HeroItem mozes = party.getHero("mozes");
        HeroItem luana = heroes.getHero("luana");
        HeroItem reignald = heroes.getHero("reignald");
        HeroItem ryiah = heroes.getHero("ryiah");
        HeroItem valter = heroes.getHero("valter");
        HeroItem galen = heroes.getHero("galen");
        HeroItem jaspar = heroes.getHero("jaspar");
        HeroItem kiara = heroes.getHero("kiara");
        HeroItem luthais = heroes.getHero("luthais");
        HeroItem elias = heroes.getHero("elias");
        HeroItem onarr = heroes.getHero("onarr");
        HeroItem duilio = heroes.getHero("duilio");
        HeroItem iellwen = heroes.getHero("iellwen");
        HeroItem faeron = heroes.getHero("faeron");

        SoftAssertions softly = new SoftAssertions();
        softly.assertThat(mozes.getLevel()).isEqualTo(1);
        softly.assertThat(mozes.getTotalXp()).isEqualTo(5);
        softly.assertThat(mozes.getNeededXpForNextLevel()).isEqualTo(20);
        softly.assertThat(mozes.getCurrentHp()).isEqualTo(46);
        softly.assertThat(mozes.getMaximumHp()).isEqualTo(46);
        softly.assertThat(mozes.getStrength()).isEqualTo(15);
        softly.assertThat(mozes.getProtectionWithShield()).isEqualTo(6);
        softly.assertThat(mozes.getInventoryItem(WEAPON)).get().hasFieldOrPropertyWithValue("id", "basic_shortsword");
        softly.assertThat(mozes.getInventoryItem(SHIELD)).get().hasFieldOrPropertyWithValue("id", "light_buckler_shield");
        softly.assertThat(mozes.getInventoryItem(HELMET)).isEmpty();
        softly.assertThat(mozes.getInventoryItem(CHEST)).get().hasFieldOrPropertyWithValue("id", "medium_basic_chest");

        softly.assertThat(luana.getLevel()).isEqualTo(1);
        softly.assertThat(luana.getTotalXp()).isEqualTo(5);
        softly.assertThat(luana.getNeededXpForNextLevel()).isEqualTo(20);
        softly.assertThat(luana.getCurrentHp()).isEqualTo(31);
        softly.assertThat(luana.getMaximumHp()).isEqualTo(31);
        softly.assertThat(luana.getStrength()).isEqualTo(8);
        softly.assertThat(luana.getProtectionWithShield()).isEqualTo(1);
        softly.assertThat(luana.getInventoryItem(WEAPON)).get().hasFieldOrPropertyWithValue("id", "basic_dagger");
        softly.assertThat(luana.getInventoryItem(SHIELD)).isEmpty();
        softly.assertThat(luana.getInventoryItem(HELMET)).isEmpty();
        softly.assertThat(luana.getInventoryItem(CHEST)).get().hasFieldOrPropertyWithValue("id", "light_basic_chest");

        softly.assertThat(reignald.getLevel()).isEqualTo(8);
        softly.assertThat(reignald.getTotalXp()).isEqualTo(1020);
        softly.assertThat(reignald.getNeededXpForNextLevel()).isEqualTo(405);
        softly.assertThat(reignald.getCurrentHp()).isEqualTo(68);
        softly.assertThat(reignald.getMaximumHp()).isEqualTo(68);
        softly.assertThat(reignald.getStrength()).isEqualTo(20);
        softly.assertThat(reignald.getProtectionWithShield()).isEqualTo(13);

        softly.assertThat(ryiah.getLevel()).isEqualTo(3);
        softly.assertThat(ryiah.getTotalXp()).isEqualTo(70);
        softly.assertThat(ryiah.getNeededXpForNextLevel()).isEqualTo(80);
        softly.assertThat(ryiah.getCurrentHp()).isEqualTo(50);
        softly.assertThat(ryiah.getMaximumHp()).isEqualTo(50);
        softly.assertThat(ryiah.getStrength()).isEqualTo(10);
        softly.assertThat(ryiah.getProtectionWithShield()).isEqualTo(1);

        softly.assertThat(valter.getLevel()).isEqualTo(2);
        softly.assertThat(valter.getTotalXp()).isEqualTo(25);
        softly.assertThat(valter.getNeededXpForNextLevel()).isEqualTo(45);
        softly.assertThat(valter.getCurrentHp()).isEqualTo(37);
        softly.assertThat(valter.getMaximumHp()).isEqualTo(37);
        softly.assertThat(valter.getStrength()).isEqualTo(10);
        softly.assertThat(valter.getProtectionWithShield()).isEqualTo(1);

        softly.assertThat(galen.getLevel()).isEqualTo(4);
        softly.assertThat(galen.getTotalXp()).isEqualTo(150);
        softly.assertThat(galen.getNeededXpForNextLevel()).isEqualTo(125);
        softly.assertThat(galen.getCurrentHp()).isEqualTo(64);
        softly.assertThat(galen.getMaximumHp()).isEqualTo(64);
        softly.assertThat(galen.getStrength()).isEqualTo(25);
        softly.assertThat(galen.getProtectionWithShield()).isEqualTo(13);

        softly.assertThat(jaspar.getLevel()).isEqualTo(12);
        softly.assertThat(jaspar.getTotalXp()).isEqualTo(3250);
        softly.assertThat(jaspar.getNeededXpForNextLevel()).isEqualTo(845);
        softly.assertThat(jaspar.getCurrentHp()).isEqualTo(102);
        softly.assertThat(jaspar.getMaximumHp()).isEqualTo(102);
        softly.assertThat(jaspar.getStrength()).isEqualTo(30);
        softly.assertThat(jaspar.getProtectionWithShield()).isEqualTo(19);

        softly.assertThat(kiara.getLevel()).isEqualTo(12);
        softly.assertThat(kiara.getTotalXp()).isEqualTo(3250);
        softly.assertThat(kiara.getNeededXpForNextLevel()).isEqualTo(845);
        softly.assertThat(kiara.getCurrentHp()).isEqualTo(72);
        softly.assertThat(kiara.getMaximumHp()).isEqualTo(72);
        softly.assertThat(kiara.getStrength()).isEqualTo(15);
        softly.assertThat(kiara.getProtectionWithShield()).isEqualTo(2);

        softly.assertThat(luthais.getLevel()).isEqualTo(20);
        softly.assertThat(luthais.getTotalXp()).isEqualTo(14350);
        softly.assertThat(luthais.getNeededXpForNextLevel()).isEqualTo(2205);
        softly.assertThat(luthais.getCurrentHp()).isEqualTo(88);
        softly.assertThat(luthais.getMaximumHp()).isEqualTo(88);
        softly.assertThat(luthais.getStrength()).isEqualTo(8);
        softly.assertThat(luthais.getProtectionWithShield()).isEqualTo(3);

        softly.assertThat(elias.getLevel()).isEqualTo(18);
        softly.assertThat(elias.getTotalXp()).isEqualTo(10545);
        softly.assertThat(elias.getNeededXpForNextLevel()).isEqualTo(1805);
        softly.assertThat(elias.getCurrentHp()).isEqualTo(108);
        softly.assertThat(elias.getMaximumHp()).isEqualTo(108);
        softly.assertThat(elias.getStrength()).isEqualTo(20);
        softly.assertThat(elias.getProtectionWithShield()).isEqualTo(6);

        softly.assertThat(onarr.getLevel()).isEqualTo(18);
        softly.assertThat(onarr.getTotalXp()).isEqualTo(10545);
        softly.assertThat(onarr.getNeededXpForNextLevel()).isEqualTo(1805);
        softly.assertThat(onarr.getCurrentHp()).isEqualTo(108);
        softly.assertThat(onarr.getMaximumHp()).isEqualTo(108);
        softly.assertThat(onarr.getStrength()).isEqualTo(25);
        softly.assertThat(onarr.getProtectionWithShield()).isEqualTo(19);

        softly.assertThat(duilio.getLevel()).isEqualTo(22);
        softly.assertThat(duilio.getTotalXp()).isEqualTo(18975);
        softly.assertThat(duilio.getNeededXpForNextLevel()).isEqualTo(2645);
        softly.assertThat(duilio.getCurrentHp()).isEqualTo(122);
        softly.assertThat(duilio.getMaximumHp()).isEqualTo(122);
        softly.assertThat(duilio.getStrength()).isEqualTo(25);
        softly.assertThat(duilio.getProtectionWithShield()).isEqualTo(10);

        softly.assertThat(iellwen.getLevel()).isEqualTo(20);
        softly.assertThat(iellwen.getTotalXp()).isEqualTo(14350);
        softly.assertThat(iellwen.getNeededXpForNextLevel()).isEqualTo(2205);
        softly.assertThat(iellwen.getCurrentHp()).isEqualTo(110);
        softly.assertThat(iellwen.getMaximumHp()).isEqualTo(110);
        softly.assertThat(iellwen.getStrength()).isEqualTo(20);
        softly.assertThat(iellwen.getProtectionWithShield()).isEqualTo(7);

        softly.assertThat(faeron.getLevel()).isEqualTo(25);
        softly.assertThat(faeron.getTotalXp()).isEqualTo(27625);
        softly.assertThat(faeron.getNeededXpForNextLevel()).isEqualTo(3380);
        softly.assertThat(faeron.getCurrentHp()).isEqualTo(130);
        softly.assertThat(faeron.getMaximumHp()).isEqualTo(130);
        softly.assertThat(faeron.getStrength()).isEqualTo(15);
        softly.assertThat(faeron.getProtectionWithShield()).isEqualTo(4);
        softly.assertAll();
    }

}
