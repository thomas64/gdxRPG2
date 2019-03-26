package nl.t64.game.rpg.components.character;

import nl.t64.game.rpg.Engine;
import nl.t64.game.rpg.components.party.HeroContainer;
import nl.t64.game.rpg.components.party.HeroItem;
import nl.t64.game.rpg.components.party.PartyContainer;
import nl.t64.game.rpg.events.Event;
import nl.t64.game.rpg.events.character.SelectEvent;


public class HeroPhysics extends NpcPhysics {

    private Engine engine;

    private String heroId;
    private boolean isSelected = false;

    public HeroPhysics(String heroId) {
        super();
        this.heroId = heroId;
    }

    @Override
    public void receive(Event event) {
        super.receive(event);
        if (event instanceof SelectEvent) {
            isSelected = true;
        }
    }

    @Override
    public void update(Engine engine, Character heroCharacter, float dt) {
        this.engine = engine;
        if (isSelected) {
            tryToAddHeroToParty(heroCharacter, dt);
        } else {
            super.update(engine, heroCharacter, dt);
        }
    }

    private void tryToAddHeroToParty(Character heroCharacter, float dt) {
        HeroContainer heroes = engine.getGameData().getHeroes();
        PartyContainer party = engine.getGameData().getParty();
        HeroItem hero = heroes.getHero(heroId);

        if (!party.isFull()) {
            heroes.removeHero(heroId);
            party.addHero(hero);
            isSelected = false;
            engine.getWorldScreen().removeNpcCharacter(heroCharacter);
        } else {
            // Visual warning message.
            super.update(engine, heroCharacter, dt);
        }
    }

}
