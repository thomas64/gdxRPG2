package nl.t64.game.rpg.screens.world;

import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.math.Vector2;
import nl.t64.game.rpg.Utils;
import nl.t64.game.rpg.components.loot.Loot;
import nl.t64.game.rpg.screens.world.entity.*;
import nl.t64.game.rpg.screens.world.entity.events.LoadEntityEvent;

import java.util.ArrayList;
import java.util.List;


class LootLoader {

    private final GameMap currentMap;
    private final List<Entity> lootList;

    LootLoader(GameMap currentMap) {
        this.currentMap = currentMap;
        this.lootList = new ArrayList<>();
    }

    List<Entity> createLoot() {
        loadSparkles();
        loadChests();
        return List.copyOf(lootList);
    }

    private void loadSparkles() {
        for (RectangleMapObject gameMapSparkle : currentMap.getSparkles()) {
            Loot sparkle = Utils.getGameData().getLoot().getLoot(gameMapSparkle.getName());
            if (!sparkle.isTaken()) {
                loadSparkle(gameMapSparkle, sparkle);
            }
        }
    }

    private void loadChests() {
        for (RectangleMapObject gameMapChest : currentMap.getChests()) {
            Loot chest = Utils.getGameData().getLoot().getLoot(gameMapChest.getName());
            loadChest(gameMapChest, chest);
        }
    }

    private void loadSparkle(RectangleMapObject gameMapSparkle, Loot sparkle) {
        var entity = new Entity(gameMapSparkle.getName(),
                                new InputEmpty(),
                                new PhysicsSparkle(sparkle),
                                new GraphicsSparkle());
        lootList.add(entity);
        Utils.getBrokerManager().actionObservers.addObserver(entity);
        var position = new Vector2(gameMapSparkle.getRectangle().x, gameMapSparkle.getRectangle().y);
        entity.send(new LoadEntityEvent(position));
    }

    private void loadChest(RectangleMapObject gameMapChest, Loot chest) {
        var entity = new Entity(gameMapChest.getName(),
                                new InputEmpty(),
                                new PhysicsChest(chest),
                                new GraphicsChest());
        lootList.add(entity);
        Utils.getBrokerManager().actionObservers.addObserver(entity);
        Utils.getBrokerManager().blockObservers.addObserver(entity);
        var position = new Vector2(gameMapChest.getRectangle().x, gameMapChest.getRectangle().y);
        if (chest.isTaken()) {
            entity.send(new LoadEntityEvent(EntityState.OPENED, position));
        } else {
            entity.send(new LoadEntityEvent(EntityState.IMMOBILE, position));
        }
    }

}
