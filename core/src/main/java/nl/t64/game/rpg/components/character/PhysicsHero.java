package nl.t64.game.rpg.components.character;

import nl.t64.game.rpg.Utils;
import nl.t64.game.rpg.components.party.HeroContainer;
import nl.t64.game.rpg.components.party.HeroItem;
import nl.t64.game.rpg.components.party.PartyContainer;


public class PhysicsHero extends PhysicsNpc {

    private Character heroCharacter;

    public PhysicsHero(String heroId) {
        super(heroId);
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
        HeroItem hero = heroes.getHero(npcId);

        if (party.isFull()) {
            // Visual warning message.
            super.update(heroCharacter, dt);
        } else {
            isSelected = false;
            heroes.removeHero(npcId);
            party.addHero(hero);
            notifyPartySwap(heroCharacter);
        }
    }

}
