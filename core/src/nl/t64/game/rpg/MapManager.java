package nl.t64.game.rpg;

import nl.t64.game.rpg.constants.MapTitle;


public class MapManager {

    private static final String TAG = MapManager.class.getSimpleName();

    private GameMap currentMap = null;

    public GameMap getCurrentMap() {
        loadStartOfGameMapIfNecessary();
        return currentMap;
    }

    public void loadMap(String mapName) {
        disposeOldMap();
        currentMap = new GameMap(mapName);
    }

    private void loadStartOfGameMapIfNecessary() {
        if (currentMap == null) {
            loadMap(MapTitle.MAP1.name());
            currentMap.setPlayerStartOfGameSpawnLocation();
        }
    }

    private void disposeOldMap() {
        if (currentMap != null) {
            currentMap.dispose();
        }
    }

}
