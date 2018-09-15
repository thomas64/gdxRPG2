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

    public final static float UNIT_SCALE = 1 / 48f;
    private static final String TAG = MapManager.class.getSimpleName();
    private final static String PLAYER_START = "PLAYER_START";

    private Vector2 playerStart;
    private TiledMap currentMap = null;
    private String currentMapName;

    @Getter
    private List<Portal> portals = new ArrayList<>();
    private List<SpawnPoint> spawnPoints = new ArrayList<>();
    @Getter
    private List<RectangleMapObject> blockers = new ArrayList<>();

    public MapManager() {
        playerStart = new Vector2(0, 0);
    }

    public TiledMap getCurrentMap() {
        if (currentMap == null) {
            loadMap(MapTitle.MAP1.name());
        }
        return currentMap;
    }

    public void loadMap(String mapName) {

        String mapFullPath = MapTitle.valueOf(mapName).getMapPath();

        if (currentMap != null) {
            currentMap.dispose();
        }

        Utility.loadMapAsset(mapFullPath);
        if (Utility.isAssetLoaded(mapFullPath)) {
            currentMap = Utility.getMapAsset(mapFullPath);
            currentMapName = mapName;
        } else {
            Logger.mapNotLoaded(TAG, mapFullPath);
            return;
        }

        loadBlockers();
        loadPortals();
        loadSpawnPoints();

        if (playerStart.isZero()) {
            setPlayerSpawnLocation(PLAYER_START, "");
        }
    }

    private void loadBlockers() {
        MapLayer collisionLayer = currentMap.getLayers().get(MapLayerName.COLLISION_LAYER.name());
        for (MapObject mapObject : collisionLayer.getObjects()) {
            RectangleMapObject rectObject = (RectangleMapObject) mapObject;
            blockers.add(rectObject);
        }
    }

    private void loadPortals() {
        MapLayer portalLayer = currentMap.getLayers().get(MapLayerName.PORTAL_LAYER.name());
        for (MapObject mapObject : portalLayer.getObjects()) {
            RectangleMapObject rectObject = (RectangleMapObject) mapObject;
            String toMapLocation = rectObject.getProperties().get("type", String.class);
            if (toMapLocation == null) toMapLocation = "";
            portals.add(
                    new Portal(
                            rectObject.getRectangle(),
                            currentMapName,
                            rectObject.getName(),
                            toMapLocation
                    )
            );
        }
    }

    private void loadSpawnPoints() {
        MapLayer spawnsLayer = currentMap.getLayers().get(MapLayerName.SPAWNS_LAYER.name());
        for (MapObject mapObject : spawnsLayer.getObjects()) {
            RectangleMapObject rectObject = (RectangleMapObject) mapObject;
            String fromMapLocation = rectObject.getProperties().get("type", String.class);
            if (fromMapLocation == null) fromMapLocation = "";
            String direction = rectObject.getProperties().get("direction", String.class);
            if (direction == null) direction = "";
            spawnPoints.add(
                    new SpawnPoint(
                            rectObject.getRectangle(),
                            rectObject.getName(),
                            fromMapLocation,
                            direction
                    )
            );
        }
    }

    public void setPlayerSpawnLocation(String fromMapName, String fromMapLocation) {
        for (SpawnPoint spawnPoint : spawnPoints) {
            if (spawnPoint.getFromMapName().equalsIgnoreCase(fromMapName) &&
                    spawnPoint.getFromMapLocation().equalsIgnoreCase(fromMapLocation)) {
                playerStart.set(spawnPoint.getRectangle().getX(), spawnPoint.getRectangle().getY());
                Logger.playerStart(TAG, playerStart.x, playerStart.y);
                return;
            }
        }
    }

    public Vector2 getPlayerStartUnitScaled() {
        Vector2 playerStart = this.playerStart.cpy();
        playerStart.set(this.playerStart.x * UNIT_SCALE, this.playerStart.y * UNIT_SCALE);
        return playerStart;
    }

}
