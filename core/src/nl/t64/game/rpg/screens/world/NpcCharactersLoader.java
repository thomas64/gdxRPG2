package nl.t64.game.rpg.screens.world;

import nl.t64.game.rpg.Utils;
import nl.t64.game.rpg.components.party.HeroItem;
import nl.t64.game.rpg.screens.world.entity.Entity;
import nl.t64.game.rpg.screens.world.entity.GraphicsNpc;
import nl.t64.game.rpg.screens.world.entity.InputNpc;
import nl.t64.game.rpg.screens.world.entity.PhysicsNpc;
import nl.t64.game.rpg.screens.world.entity.events.LoadEntityEvent;

import java.util.ArrayList;
import java.util.List;


class NpcCharactersLoader {

    private final GameMap currentMap;
    private final List<Entity> npcCharacters;

    NpcCharactersLoader(GameMap currentMap) {
        this.currentMap = currentMap;
        this.npcCharacters = new ArrayList<>();
    }

    List<Entity> createNpcs() {
        loadNpcs();
        loadHeroes();
        return List.copyOf(npcCharacters);
    }

    private void loadNpcs() {
        currentMap.npcs.forEach(this::loadNpcCharacter);
    }

    private void loadHeroes() {
        currentMap.heroes.forEach(this::loadHero);
    }

    private void loadHero(GameMapHero gameMapHero) {
        HeroItem hero = Utils.getGameData().getHeroes().getHero(gameMapHero.name);
        if (gameMapHero.hasBeenRecruited == hero.isHasBeenRecruited()) {
            loadNpcCharacter(gameMapHero);
        }
    }

    private void loadNpcCharacter(GameMapNpc gameMapNpc) {
        String characterId = gameMapNpc.name;
        var npcCharacter = new Entity(characterId, new InputNpc(), new PhysicsNpc(), new GraphicsNpc(characterId));
        npcCharacters.add(npcCharacter);
        Utils.getBrokerManager().actionObservers.addObserver(npcCharacter);
        Utils.getBrokerManager().blockObservers.addObserver(npcCharacter);
        Utils.getBrokerManager().bumpObservers.addObserver(npcCharacter);
        npcCharacter.send(new LoadEntityEvent(gameMapNpc.state,
                                              gameMapNpc.direction,
                                              gameMapNpc.getPosition(),
                                              gameMapNpc.conversation));
    }

}
