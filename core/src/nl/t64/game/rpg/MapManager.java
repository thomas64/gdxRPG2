package nl.t64.game.rpg;

import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Vector2;
import lombok.Getter;
import nl.t64.game.rpg.constants.MapLayerName;
import nl.t64.game.rpg.constants.MapTitle;
import nl.t64.game.rpg.tiled.Portal;
import nl.t64.game.rpg.tiled.SpawnPoint;

import java.util.ArrayList;
import java.util.List;


public class MapManager {

    private static final String TAG = MapManager.class.getSimpleName();
    private final static String PLAYER_START = "PLAYER_START";

    @Getter
    private Vector2 playerSpawnLocation;
    private TiledMap currentMap = null;
    private String currentMapName = null;

    @Getter
    private List<Portal> portals = new ArrayList<>();
    @Getter
    private List<SpawnPoint> spawnPoints = new ArrayList<>();
    @Getter
    private List<RectangleMapObject> blockers = new ArrayList<>();

    public MapManager() {
        playerSpawnLocation = new Vector2(0, 0);
    }

    public TiledMap getCurrentMap() {
        if (currentMap == null) {
            loadMap(MapTitle.MAP1.name());
        }
        return currentMap;
    }

    public void loadMap(String mapName) {
        disposeOldMap();
        loadMapAsset(mapName);
        loadBlockers();
        loadPortals();
        loadSpawnPoints();
        setPlayerStartOfGameSpawnLocation();
    }

    private void disposeOldMap() {
        if (currentMap != null) {
            currentMap.dispose();
        }
    }

    private void loadMapAsset(String mapName) {
        String mapFullPath = MapTitle.valueOf(mapName).getMapPath();
        Utility.loadMapAsset(mapFullPath);
        currentMap = Utility.getMapAsset(mapFullPath);
        currentMapName = mapName;
    }

    private void loadBlockers() {
        blockers.clear();
        MapLayer collisionLayer = currentMap.getLayers().get(MapLayerName.COLLISION_LAYER.name());
        for (MapObject mapObject : collisionLayer.getObjects()) {
            RectangleMapObject rectObject = (RectangleMapObject) mapObject;
            blockers.add(rectObject);
        }
    }

    private void loadPortals() {
        portals.clear();
        MapLayer portalLayer = currentMap.getLayers().get(MapLayerName.PORTAL_LAYER.name());
        for (MapObject mapObject : portalLayer.getObjects()) {
            RectangleMapObject rectObject = (RectangleMapObject) mapObject;
            String toMapLocation = rectObject.getProperties().get("type", String.class);
            Portal p = new Portal(rectObject.getRectangle(), currentMapName, rectObject.getName(), toMapLocation);
            portals.add(p);
        }
    }

    private void loadSpawnPoints() {
        spawnPoints.clear();
        MapLayer spawnsLayer = currentMap.getLayers().get(MapLayerName.SPAWNS_LAYER.name());
        for (MapObject mapObject : spawnsLayer.getObjects()) {
            RectangleMapObject rectObject = (RectangleMapObject) mapObject;
            String fromMapLocation = rectObject.getProperties().get("type", String.class);
            String direction = rectObject.getProperties().get("direction", String.class);
            SpawnPoint s = new SpawnPoint(rectObject.getRectangle(), rectObject.getName(), fromMapLocation, direction);
            spawnPoints.add(s);
        }
    }

    private void setPlayerStartOfGameSpawnLocation() {
        if (playerSpawnLocation.isZero()) {
            Portal startOfGamePortal = new Portal(null, PLAYER_START, null, "");
            setPlayerSpawnLocation(startOfGamePortal);
        }
    }

    public void setPlayerSpawnLocation(Portal portal) {
        for (SpawnPoint spawnPoint : spawnPoints) {
            if (spawnPoint.isInConnectionWith(portal)) {
                playerSpawnLocation.set(spawnPoint.getX(), spawnPoint.getY());
                Logger.playerSpawnLocation(TAG, playerSpawnLocation.x, playerSpawnLocation.y);
                return;
            }
        }
    }

}
