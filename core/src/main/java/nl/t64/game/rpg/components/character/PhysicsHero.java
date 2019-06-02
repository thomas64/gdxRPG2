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

    public PhysicsHero(String heroId) {
        super();
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
        if (isSelected) {
            tryToAddHeroToParty(heroCharacter, dt);
        } else {
            super.update(heroCharacter, dt);
        }
    }

    private void tryToAddHeroToParty(Character heroCharacter, float dt) {
        HeroContainer heroes = Utils.getGameData().getHeroes();
        PartyContainer party = Utils.getGameData().getParty();
        HeroItem hero = heroes.getHero(heroId);

        if (party.isFull()) {
            // Visual warning message.
            super.update(heroCharacter, dt);
        } else {
            heroes.removeHero(heroId);
            party.addHero(hero);
            isSelected = false;
            Utils.getScreenManager().getWorldScreen().removeNpcCharacter(heroCharacter);
            Utils.getMapManager().removeFromBlockers(heroCharacter.getBoundingBox());
        }
    }

}
