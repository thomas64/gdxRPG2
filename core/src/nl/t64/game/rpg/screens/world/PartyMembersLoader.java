package nl.t64.game.rpg.screens.world;

import nl.t64.game.rpg.Utils;
import nl.t64.game.rpg.components.character.Character;
import nl.t64.game.rpg.components.character.*;
import nl.t64.game.rpg.components.party.HeroItem;
import nl.t64.game.rpg.components.party.PartyContainer;
import nl.t64.game.rpg.events.character.LoadCharacterEvent;

import java.util.ArrayList;
import java.util.List;


class PartyMembersLoader {

    private final Character player;
    private final List<Character> partyMembers;

    PartyMembersLoader(Character player) {
        this.player = player;
        this.partyMembers = new ArrayList<>(PartyContainer.MAXIMUM - 1);
    }

    List<Character> loadPartyMembers() {
        Utils.getGameData().getParty().getAllHeroes()
             .stream()
             .filter(this::isHeroNotPlayer)
             .map(this::createCharacter)
             .forEach(this::addToPartyMembers);

        return List.copyOf(partyMembers);
    }

    private boolean isHeroNotPlayer(HeroItem hero) {
        return !hero.isPlayer();
    }

    private Character createCharacter(HeroItem hero) {
        String heroId = hero.getId();
        return new Character(heroId,
                             new InputPartyMember(),
                             new PhysicsPartyMember(),
                             new GraphicsPartyMember(heroId));
    }

    private void addToPartyMembers(Character partyMember) {
        partyMembers.add(partyMember);
        partyMember.send(new LoadCharacterEvent(CharacterState.IDLE,
                                                player.getDirection(),
                                                player.getPosition().cpy()));
    }

}
