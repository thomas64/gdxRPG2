package nl.t64.game.rpg.interactors;

import nl.t64.game.rpg.Data;
import nl.t64.game.rpg.components.party.HeroContainer;
import nl.t64.game.rpg.components.party.HeroItem;
import nl.t64.game.rpg.components.party.PartyContainer;
import nl.t64.game.rpg.constants.Constant;


public class PartyManager {

    private final HeroContainer heroes;
    private final PartyContainer party;

    public PartyManager(Data data) {
        this.heroes = data.getHeroes();
        this.party = data.getParty();
    }

    public void addFirstHero() {
        addHeroToParty(Constant.PLAYER_ID);
    }

    public void addHeroToParty(String id) {
        HeroItem hero = heroes.getHero(id);
        heroes.removeHero(id);
        try {
            party.addHero(hero);
        } catch (PartyContainer.FullException e) {
            heroes.addHero(e.getRejectedHero());
            // Visual warning message.
        }
    }

    public void removeHeroFromParty(String id) {
        HeroItem hero = party.getHero(id);
        try {
            party.removeHero(id);
            heroes.addHero(hero);
        } catch (PartyContainer.PlayerRemovalException e) {
            // Visual warning message.
        }
    }

}
