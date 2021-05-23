package nl.t64.game.rpg.screens.world;

import nl.t64.game.rpg.Utils;
import nl.t64.game.rpg.components.condition.ConditionDatabase;
import nl.t64.game.rpg.components.party.HeroItem;
import nl.t64.game.rpg.screens.world.entity.*;
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
        loadEnemies();
        return List.copyOf(npcEntities);
    }

    private void loadNpcs() {
        currentMap.npcs.forEach(this::loadNpcEntity);
    }

    private void loadHeroes() {
        currentMap.heroes.forEach(this::loadHero);
    }

    private void loadEnemies() {
        currentMap.enemies.forEach(this::loadEnemy);
    }

    private void loadHero(GameMapHero gameMapHero) {
        HeroItem hero = Utils.getGameData().getHeroes().getHero(gameMapHero.getName());
        if (gameMapHero.isHasBeenRecruited() == hero.isHasBeenRecruited()) {
            loadNpcEntity(gameMapHero);
        }
    }

    private void loadEnemy(GameMapNpc gameMapNpc) {
        String entityId = gameMapNpc.getName();
        var enemyEntity = new Entity(entityId, new InputEnemy(), new PhysicsEnemy(), new GraphicsEnemy(entityId));
        npcEntities.add(enemyEntity);
        Utils.getBrokerManager().detectionObservers.addObserver(enemyEntity);
        Utils.getBrokerManager().bumpObservers.addObserver(enemyEntity);
        enemyEntity.send(new LoadEntityEvent(gameMapNpc.getState(),
                                             gameMapNpc.getDirection(),
                                             gameMapNpc.getPosition()));
    }

    private void loadNpcEntity(GameMapNpc gameMapNpc) {
        if (!ConditionDatabase.getInstance().isMeetingConditions(gameMapNpc.getConditionIds())) {
            return;
        }
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
