package nl.t64.game.rpg.components.party;

import nl.t64.game.rpg.Data;
import nl.t64.game.rpg.GameTest;
import nl.t64.game.rpg.constants.Constant;
import nl.t64.game.rpg.interactors.PartyManager;
import nl.t64.game.rpg.profile.ProfileManager;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;


class HeroTest extends GameTest {

    private ProfileManager profileManager = new ProfileManager();

    @Test
    void whenDataIsCreated_ShouldContainMozes() {
        final var data = new Data();
        data.onNotifyCreate(profileManager);

        assertThat(data.getHeroes().contains(Constant.PLAYER_ID)).isFalse();
        assertThat(data.getHeroes().getSize()).isEqualTo(13);
        assertThat(data.getParty().contains(Constant.PLAYER_ID)).isTrue();
        assertThat(data.getParty().getSize()).isEqualTo(1);
    }

    @Test
    void whenMozesIsRemovedFromParty_ShouldNotSucceed() {
        final var data = new Data();
        data.onNotifyCreate(profileManager);
        final var partyManager = new PartyManager(data);

        partyManager.removeHeroFromParty(Constant.PLAYER_ID);

        assertThat(data.getParty().contains(Constant.PLAYER_ID)).isTrue();
        assertThat(data.getParty().getSize()).isEqualTo(1);
    }

    @Test
    void whenHeroIsAddedToParty_ShouldBeRemovedFromHeroContainer() {
        final var data = new Data();
        data.onNotifyCreate(profileManager);
        final var partyManager = new PartyManager(data);
        final String luana = "luana";

        assertThat(data.getHeroes().getSize()).isEqualTo(13);
        assertThat(data.getHeroes().contains(luana)).isTrue();
        assertThat(data.getParty().getSize()).isEqualTo(1);
        assertThat(data.getParty().contains(luana)).isFalse();

        partyManager.addHeroToParty(luana);

        assertThat(data.getHeroes().getSize()).isEqualTo(12);
        assertThat(data.getHeroes().contains(luana)).isFalse();
        assertThat(data.getParty().getSize()).isEqualTo(2);
        assertThat(data.getParty().contains(luana)).isTrue();
    }

    @Test
    void whenHeroIsRemovedFromParty_ShouldBeAddedToHeroContainer() {
        final var data = new Data();
        data.onNotifyCreate(profileManager);
        final var partyManager = new PartyManager(data);
        final String luana = "luana";

        partyManager.addHeroToParty(luana);

        assertThat(data.getHeroes().getSize()).isEqualTo(12);
        assertThat(data.getHeroes().contains(luana)).isFalse();
        assertThat(data.getParty().getSize()).isEqualTo(2);
        assertThat(data.getParty().contains(luana)).isTrue();

        partyManager.removeHeroFromParty(luana);

        assertThat(data.getHeroes().getSize()).isEqualTo(13);
        assertThat(data.getHeroes().contains(luana)).isTrue();
        assertThat(data.getParty().getSize()).isEqualTo(1);
        assertThat(data.getParty().contains(luana)).isFalse();
    }

    @Test
    void whenPartyIsFull_ShouldRejectNewHero() {
        final var data = new Data();
        data.onNotifyCreate(profileManager);
        final var partyManager = new PartyManager(data);

        partyManager.addHeroToParty("luana");
        partyManager.addHeroToParty("reignald");
        partyManager.addHeroToParty("ryiah");
        partyManager.addHeroToParty("valter");

        assertThat(data.getHeroes().getSize()).isEqualTo(9);
        assertThat(data.getParty().getSize()).isEqualTo(5);

        partyManager.addHeroToParty("galen");

        assertThat(data.getHeroes().getSize()).isEqualTo(9);
        assertThat(data.getParty().getSize()).isEqualTo(5);
    }

}
