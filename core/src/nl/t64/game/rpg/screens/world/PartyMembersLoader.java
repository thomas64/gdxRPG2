package nl.t64.game.rpg.screens.world;

import nl.t64.game.rpg.Utils;
import nl.t64.game.rpg.components.party.HeroItem;
import nl.t64.game.rpg.components.party.PartyContainer;
import nl.t64.game.rpg.screens.world.entity.*;
import nl.t64.game.rpg.screens.world.entity.events.LoadEntityEvent;

import java.util.ArrayList;
import java.util.List;


class PartyMembersLoader {

    private final Entity player;
    private final List<Entity> partyMembers;

    PartyMembersLoader(Entity player) {
        this.player = player;
        this.partyMembers = new ArrayList<>(PartyContainer.MAXIMUM - 1);
    }

    List<Entity> loadPartyMembers() {
        Utils.getGameData().getParty().getAllHeroes()
             .stream()
             .filter(this::isHeroNotPlayer)
             .map(this::createEntity)
             .forEach(this::addToPartyMembers);

        return List.copyOf(partyMembers);
    }

    private boolean isHeroNotPlayer(HeroItem hero) {
        return !hero.isPlayer();
    }

    private Entity createEntity(HeroItem hero) {
        String heroId = hero.getId();
        return new Entity(heroId, new InputPartyMember(), new PhysicsPartyMember(), new GraphicsPartyMember(heroId));
    }

    private void addToPartyMembers(Entity partyMember) {
        partyMembers.add(partyMember);
        partyMember.send(new LoadEntityEvent(EntityState.IDLE, player.getDirection(), player.getPosition().cpy()));
    }

}
