package nl.t64.game.rpg.screens.world;

import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.math.Vector2;
import nl.t64.game.rpg.Utils;
import nl.t64.game.rpg.components.door.Door;
import nl.t64.game.rpg.screens.world.entity.*;
import nl.t64.game.rpg.screens.world.entity.events.LoadEntityEvent;

import java.util.ArrayList;
import java.util.List;


public class DoorLoader {

    private final GameMap currentMap;
    private final List<Entity> doorList;

    DoorLoader(GameMap currentMap) {
        this.currentMap = currentMap;
        this.doorList = new ArrayList<>();
    }

    List<Entity> createDoors() {
        loadDoors();
        return List.copyOf(doorList);
    }

    private void loadDoors() {
        for (RectangleMapObject gameMapDoor : currentMap.doors) {
            loadDoor(gameMapDoor);
        }
    }

    private void loadDoor(RectangleMapObject gameMapDoor) {
        Door door = Utils.getGameData().getDoors().getDoor(gameMapDoor.getName());
        door.close();
        var entity = new Entity(gameMapDoor.getName(), new InputEmpty(), new PhysicsDoor(door), new GraphicsDoor(door));
        doorList.add(entity);
        Utils.getBrokerManager().actionObservers.addObserver(entity);
        Utils.getBrokerManager().blockObservers.addObserver(entity);
        var position = new Vector2(gameMapDoor.getRectangle().x, gameMapDoor.getRectangle().y);
        entity.send(new LoadEntityEvent(EntityState.IMMOBILE, position));
    }

}
