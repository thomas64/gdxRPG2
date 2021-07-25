package nl.t64.game.rpg.screens.world;

import nl.t64.game.rpg.Utils;
import nl.t64.game.rpg.components.condition.ConditionDatabase;
import nl.t64.game.rpg.components.party.HeroItem;
import nl.t64.game.rpg.screens.world.entity.*;
import nl.t64.game.rpg.screens.world.entity.events.LoadEntityEvent;
import nl.t64.game.rpg.screens.world.mapobjects.GameMapEnemy;
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
        currentMap.getNpcs().forEach(this::loadNpcEntity);
    }

    private void loadHeroes() {
        currentMap.getHeroes().forEach(this::loadHero);
    }

    private void loadEnemies() {
        currentMap.getEnemies().forEach(this::loadEnemy);
    }

    private void loadHero(GameMapHero gameMapHero) {
        HeroItem hero = Utils.getGameData().getHeroes().getHero(gameMapHero.getName());
        if (gameMapHero.getHasBeenRecruited() == hero.isHasBeenRecruited()) {
            loadNpcEntity(gameMapHero);
        }
    }

    private void loadEnemy(GameMapEnemy gameMapEnemy) {
        String entityId = gameMapEnemy.getName();
        var enemyEntity = new Entity(entityId, new InputEnemy(), new PhysicsEnemy(), new GraphicsEnemy(entityId));
        npcEntities.add(enemyEntity);
        Utils.getBrokerManager().detectionObservers.addObserver(enemyEntity);
        Utils.getBrokerManager().bumpObservers.addObserver(enemyEntity);
        enemyEntity.send(new LoadEntityEvent(gameMapEnemy.getState(),
                                             gameMapEnemy.getDirection(),
                                             gameMapEnemy.getPosition(),
                                             gameMapEnemy.getBattleId()));
    }

    private void loadNpcEntity(GameMapNpc gameMapNpc) {
        if (!ConditionDatabase.isMeetingConditions(gameMapNpc.getConditionIds())) {
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
