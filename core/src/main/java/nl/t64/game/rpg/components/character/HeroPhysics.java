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
        HeroContainer heroes = engine.getData().getHeroes();
        PartyContainer party = engine.getData().getParty();
        HeroItem hero = heroes.getHero(heroId);

        heroes.removeHero(heroId);
        try {
            party.addHero(hero);
            removeHeroSpriteFromVisualMap(heroCharacter);
        } catch (PartyContainer.FullException e) {
            heroes.addHero(e.getRejectedHero());
            // Visual warning message.
            super.update(engine, heroCharacter, dt);
        }
    }

    private void removeHeroSpriteFromVisualMap(Character heroCharacter) {
        isSelected = false;
        engine.getWorldScreen().removeNpcCharacter(heroCharacter);
    }

}
