package nl.t64.game.rpg.components.door;

import com.badlogic.gdx.Gdx;
import nl.t64.game.rpg.Utils;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;


public class DoorContainer {

    private static final String DOOR_CONFIGS = "configs/doors/";
    private static final String FILE_LIST = DOOR_CONFIGS + "_files.txt";

    private final Map<String, Door> doors;

    public DoorContainer() {
        this.doors = new HashMap<>();
        this.loadDoors();
    }

    public Door getDoor(String doorId) {
        return doors.get(doorId);
    }

    private void loadDoors() {
        String[] configFiles = Gdx.files.internal(FILE_LIST).readString().split(System.lineSeparator());
        Arrays.stream(configFiles)
              .map(filePath -> Gdx.files.internal(DOOR_CONFIGS + filePath).readString())
              .map(json -> Utils.readValue(json, Door.class))
              .peek(this::setDependentVariables)
              .forEach(doors::putAll);
    }

    private void setDependentVariables(Map<String, Door> newDoors) {
        newDoors.values().forEach(door -> {
            door.audio = door.type.audioEvent;
            door.width = door.type.width;
            door.height = door.type.height;
            door.isLocked = door.keyId != null;
            door.isClosed = true;
        });
    }

}
