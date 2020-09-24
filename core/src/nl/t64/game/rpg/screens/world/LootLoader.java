package nl.t64.game.rpg.screens.world;

import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.math.Vector2;
import nl.t64.game.rpg.Utils;
import nl.t64.game.rpg.components.character.Character;
import nl.t64.game.rpg.components.character.*;
import nl.t64.game.rpg.components.loot.Loot;
import nl.t64.game.rpg.events.character.LoadCharacterEvent;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


class LootLoader {

    private final GameMap currentMap;
    private final List<Character> lootList;
    private final boolean withBlockers;

    LootLoader(GameMap currentMap, boolean withBlockers) {
        this.currentMap = currentMap;
        this.lootList = new ArrayList<>();
        this.withBlockers = withBlockers;
    }

    List<Character> createLoot() {
        loadSparkles();
        loadChests();
        return Collections.unmodifiableList(lootList);
    }

    private void loadSparkles() {
        for (RectangleMapObject gameMapSparkle : currentMap.sparkles) {
            Loot sparkle = Utils.getGameData().getLoot().getLoot(gameMapSparkle.getName());
            if (!sparkle.isTaken()) {
                loadSparkle(gameMapSparkle, sparkle);
            }
        }
    }

    private void loadChests() {
        for (RectangleMapObject gameMapChest : currentMap.chests) {
            Loot chest = Utils.getGameData().getLoot().getLoot(gameMapChest.getName());
            loadChest(gameMapChest, chest);
        }
    }

    private void loadSparkle(RectangleMapObject gameMapSparkle, Loot sparkle) {
        var character = new Character(gameMapSparkle.getName(),
                                      new InputLoot(),
                                      new PhysicsSparkle(sparkle),
                                      new GraphicsSparkle());
        lootList.add(character);
        var position = new Vector2(gameMapSparkle.getRectangle().x, gameMapSparkle.getRectangle().y);
        character.send(new LoadCharacterEvent(position));
    }

    private void loadChest(RectangleMapObject gameMapChest, Loot chest) {
        var character = new Character(gameMapChest.getName(),
                                      new InputLoot(),
                                      new PhysicsChest(chest),
                                      new GraphicsChest());
        lootList.add(character);
        var position = new Vector2(gameMapChest.getRectangle().x, gameMapChest.getRectangle().y);
        if (chest.isTaken()) {
            character.send(new LoadCharacterEvent(CharacterState.OPENED, position));
        } else {
            character.send(new LoadCharacterEvent(CharacterState.IMMOBILE, position));
        }
        if (withBlockers) {
            currentMap.addToBlockers(character.getBoundingBox());
        }
    }

}
