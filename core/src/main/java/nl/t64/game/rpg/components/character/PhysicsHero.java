package nl.t64.game.rpg.components.character;

import nl.t64.game.rpg.Utils;
import nl.t64.game.rpg.components.party.HeroContainer;
import nl.t64.game.rpg.components.party.HeroItem;
import nl.t64.game.rpg.components.party.PartyContainer;
import nl.t64.game.rpg.events.Event;
import nl.t64.game.rpg.events.character.SelectEvent;


public class PhysicsHero extends PhysicsNpc {

    private final String heroId;
    private boolean isSelected;

    private Character heroCharacter;

    public PhysicsHero(String heroId) {
        this.heroId = heroId;
        this.isSelected = false;
    }

    @Override
    public void receive(Event event) {
        super.receive(event);
        if (event instanceof SelectEvent) {
            isSelected = true;
        }
    }

    @Override
    public void update(Character heroCharacter, float dt) {
        this.heroCharacter = heroCharacter;
        if (isSelected) {
            tryToAddHeroToParty(dt);
        } else {
            super.update(heroCharacter, dt);
        }
    }

    private void tryToAddHeroToParty(float dt) {
        HeroContainer heroes = Utils.getGameData().getHeroes();
        PartyContainer party = Utils.getGameData().getParty();
        HeroItem hero = heroes.getHero(heroId);

        if (party.isFull()) {
            // Visual warning message.
            super.update(heroCharacter, dt);
        } else {
            isSelected = false;
            heroes.removeHero(heroId);
            party.addHero(hero);
            Utils.getScreenManager().getWorldScreen().updateAfterPartySwap(heroCharacter);
        }
    }

}
