package nl.t64.game.rpg.screens.world;

import nl.t64.game.rpg.Utils;
import nl.t64.game.rpg.components.character.Character;
import nl.t64.game.rpg.components.character.GraphicsPartyMember;
import nl.t64.game.rpg.components.character.InputPartyMember;
import nl.t64.game.rpg.components.character.PhysicsPartyMember;
import nl.t64.game.rpg.components.party.HeroItem;
import nl.t64.game.rpg.components.party.PartyContainer;
import nl.t64.game.rpg.events.character.LoadPartyMemberEvent;

import java.util.ArrayList;
import java.util.Collections;
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

        return Collections.unmodifiableList(partyMembers);
    }

    private boolean isHeroNotPlayer(HeroItem hero) {
        return !hero.isPlayer();
    }

    private Character createCharacter(HeroItem hero) {
        return new Character(new InputPartyMember(),
                             new PhysicsPartyMember(),
                             new GraphicsPartyMember(hero.getId()));
    }

    private void addToPartyMembers(Character partyMember) {
        partyMembers.add(partyMember);
        partyMember.send(new LoadPartyMemberEvent(player.getDirection(),
                                                  player.getPosition().cpy()));
    }

}
