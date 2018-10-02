package nl.t64.game.rpg;

import lombok.Getter;
import lombok.Setter;
import nl.t64.game.rpg.constants.MapTitle;


public class MapManager {

    private static final String TAG = MapManager.class.getSimpleName();

    private GameMap currentMap = null;

    @Getter
    @Setter
    private boolean mapChanged = false;

    public GameMap getCurrentMap() {
        loadStartOfGameMapIfNecessary();
        return currentMap;
    }

    public void loadMap(MapTitle mapTitle) {
        disposeOldMap();
        currentMap = new GameMap(mapTitle);
        mapChanged = true;
    }

    private void loadStartOfGameMapIfNecessary() {
        if (currentMap == null) {
            loadMap(MapTitle.MAP1);
            currentMap.setPlayerStartOfGameSpawnLocation();
        }
    }

    private void disposeOldMap() {
        if (currentMap != null) {
            currentMap.dispose();
        }
    }

}
