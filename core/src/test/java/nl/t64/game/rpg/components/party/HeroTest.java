package nl.t64.game.rpg.components.party;

import nl.t64.game.rpg.Data;
import nl.t64.game.rpg.GameTest;
import nl.t64.game.rpg.constants.Constant;
import nl.t64.game.rpg.profile.ProfileManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;


class HeroTest extends GameTest {

    private Data data;
    private HeroContainer heroes;
    private PartyContainer party;

    @BeforeEach
    private void setup() {
        final var profileManager = new ProfileManager();
        data = new Data();
        data.onNotifyCreate(profileManager);
        heroes = data.getHeroes();
        party = data.getParty();
    }

    private void addHeroToParty(String id) {
        HeroItem hero = heroes.getHero(id);
        heroes.removeHero(id);
        try {
            party.addHero(hero);
        } catch (PartyContainer.FullException e) {
            heroes.addHero(e.getRejectedHero());
            // Visual warning message.
        }
    }

    private void removeHeroFromParty(String id) {
        HeroItem hero = party.getHero(id);
        try {
            party.removeHero(id);
            heroes.addHero(hero);
        } catch (PartyContainer.PlayerRemovalException e) {
            // Visual warning message.
        }
    }

    @Test
    void whenDataIsCreated_ShouldContainMozes() {
        assertThat(data.getHeroes().contains(Constant.PLAYER_ID)).isFalse();
        assertThat(data.getHeroes().getSize()).isEqualTo(13);
        assertThat(data.getParty().contains(Constant.PLAYER_ID)).isTrue();
        assertThat(data.getParty().getSize()).isEqualTo(1);
    }

    @Test
    void whenMozesIsRemovedFromParty_ShouldNotSucceed() {
        assertThat(data.getParty().contains(Constant.PLAYER_ID)).isTrue();
        assertThat(data.getParty().getSize()).isEqualTo(1);

        removeHeroFromParty(Constant.PLAYER_ID);

        assertThat(data.getParty().contains(Constant.PLAYER_ID)).isTrue();
        assertThat(data.getParty().getSize()).isEqualTo(1);
    }

    @Test
    void whenHeroIsAddedToParty_ShouldBeRemovedFromHeroContainer() {
        final String luana = "luana";

        assertThat(data.getHeroes().getSize()).isEqualTo(13);
        assertThat(data.getHeroes().contains(luana)).isTrue();
        assertThat(data.getParty().getSize()).isEqualTo(1);
        assertThat(data.getParty().contains(luana)).isFalse();

        addHeroToParty(luana);

        assertThat(data.getHeroes().getSize()).isEqualTo(12);
        assertThat(data.getHeroes().contains(luana)).isFalse();
        assertThat(data.getParty().getSize()).isEqualTo(2);
        assertThat(data.getParty().contains(luana)).isTrue();
    }

    @Test
    void whenHeroIsRemovedFromParty_ShouldBeAddedToHeroContainer() {
        final String luana = "luana";

        addHeroToParty(luana);

        assertThat(data.getHeroes().getSize()).isEqualTo(12);
        assertThat(data.getHeroes().contains(luana)).isFalse();
        assertThat(data.getParty().getSize()).isEqualTo(2);
        assertThat(data.getParty().contains(luana)).isTrue();

        removeHeroFromParty(luana);

        assertThat(data.getHeroes().getSize()).isEqualTo(13);
        assertThat(data.getHeroes().contains(luana)).isTrue();
        assertThat(data.getParty().getSize()).isEqualTo(1);
        assertThat(data.getParty().contains(luana)).isFalse();
    }

    @Test
    void whenPartyIsFull_ShouldRejectNewHero() {
        addHeroToParty("luana");
        addHeroToParty("reignald");
        addHeroToParty("ryiah");
        addHeroToParty("valter");

        assertThat(data.getHeroes().getSize()).isEqualTo(9);
        assertThat(data.getParty().getSize()).isEqualTo(5);

        addHeroToParty("galen");

        assertThat(data.getHeroes().getSize()).isEqualTo(9);
        assertThat(data.getParty().getSize()).isEqualTo(5);
    }

}
