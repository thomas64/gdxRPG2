package nl.t64.game.rpg.screens.world;

import nl.t64.game.rpg.Utils;
import nl.t64.game.rpg.components.party.HeroItem;
import nl.t64.game.rpg.screens.world.entity.Entity;
import nl.t64.game.rpg.screens.world.entity.GraphicsNpc;
import nl.t64.game.rpg.screens.world.entity.InputNpc;
import nl.t64.game.rpg.screens.world.entity.PhysicsNpc;
import nl.t64.game.rpg.screens.world.entity.events.LoadEntityEvent;
import nl.t64.game.rpg.screens.world.mapobjects.GameMapHero;
import nl.t64.game.rpg.screens.world.mapobjects.GameMapNpc;

import java.util.ArrayList;
import java.util.List;


class NpcEntitiesLoader {

    private final GameMap currentMap;
    private final List<Entity> npcEntities;

    NpcEntitiesLoader(GameMap currentMap) {
        this.currentMap = currentMap;
        this.npcEntities = new ArrayList<>();
    }

    List<Entity> createNpcs() {
        loadNpcs();
        loadHeroes();
        return List.copyOf(npcEntities);
    }

    private void loadNpcs() {
        currentMap.npcs.forEach(this::loadNpcEntity);
    }

    private void loadHeroes() {
        currentMap.heroes.forEach(this::loadHero);
    }

    private void loadHero(GameMapHero gameMapHero) {
        HeroItem hero = Utils.getGameData().getHeroes().getHero(gameMapHero.getName());
        if (gameMapHero.isHasBeenRecruited() == hero.isHasBeenRecruited()) {
            loadNpcEntity(gameMapHero);
        }
    }

    private void loadNpcEntity(GameMapNpc gameMapNpc) {
        String entityId = gameMapNpc.getName();
        var npcEntity = new Entity(entityId, new InputNpc(), new PhysicsNpc(), new GraphicsNpc(entityId));
        npcEntities.add(npcEntity);
        Utils.getBrokerManager().actionObservers.addObserver(npcEntity);
        Utils.getBrokerManager().blockObservers.addObserver(npcEntity);
        Utils.getBrokerManager().bumpObservers.addObserver(npcEntity);
        npcEntity.send(new LoadEntityEvent(gameMapNpc.getState(),
                                           gameMapNpc.getDirection(),
                                           gameMapNpc.getPosition(),
                                           gameMapNpc.getConversation()));
    }

}
