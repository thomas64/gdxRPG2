package nl.t64.game.rpg;

import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Vector2;

import java.util.Hashtable;


public class MapManager {

    public final static float UNIT_SCALE = 1 / 48f;
    private static final String TAG = MapManager.class.getSimpleName();
    private final static String PLAYER_START = "PLAYER_START";
    private Hashtable<Maps, Vector2> playerStartLocationTable;
    private Vector2 playerStartPositionRect;
    private Vector2 closestPlayerStartPosition;
    private Vector2 convertedUnits;
    private Vector2 playerStart;
    private TiledMap currentMap = null;
    private String currentMapName;
    private MapLayer collisionLayer = null;
    private MapLayer portalLayer = null;
    private MapLayer spawnsLayer = null;

    public MapManager() {
        playerStart = new Vector2(0, 0);

        playerStartLocationTable = new Hashtable<>(2);
        playerStartLocationTable.put(Maps.MAP1, playerStart.cpy());
        playerStartLocationTable.put(Maps.MAP2, playerStart.cpy());

        playerStartPositionRect = new Vector2(0, 0);
        closestPlayerStartPosition = new Vector2(0, 0);
        convertedUnits = new Vector2(0, 0);
    }

    public TiledMap getCurrentMap() {
        if (currentMap == null) {
            currentMapName = Maps.MAP1.name();
            loadMap(currentMapName);
        }
        return currentMap;
    }

    public void loadMap(String mapName) {
        playerStart.set(0, 0);
        String mapFullPath = Maps.valueOf(mapName).getMapPath();

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

        collisionLayer = currentMap.getLayers().get(MapLayers.COLLISION_LAYER.name());
        portalLayer = currentMap.getLayers().get(MapLayers.PORTAL_LAYER.name());
        spawnsLayer = currentMap.getLayers().get(MapLayers.SPAWNS_LAYER.name());

        Vector2 start = playerStartLocationTable.get(Maps.valueOf(currentMapName));
        if (start.isZero()) {
            setClosestStartPosition(playerStart);
            start = playerStartLocationTable.get(Maps.valueOf(currentMapName));
        }
        playerStart.set(start.x, start.y);
        Logger.playerStart(TAG, playerStart.x, playerStart.y);
    }

    public void setClosestStartPositionFromScaledUnits(Vector2 position) {
        if (UNIT_SCALE <= 0) return;
        convertedUnits.set(position.x / UNIT_SCALE, position.y / UNIT_SCALE);
        setClosestStartPosition(convertedUnits);
    }

    private void setClosestStartPosition(final Vector2 position) {
        playerStartPositionRect.set(0, 0);
        closestPlayerStartPosition.set(0, 0);
        float shortestDistance = 0f;

        for (MapObject mapObject : spawnsLayer.getObjects()) {
            if (mapObject.getName().equalsIgnoreCase(PLAYER_START)) {
                ((RectangleMapObject) mapObject).getRectangle().getPosition(playerStartPositionRect);
                float distance = position.dst2(playerStartPositionRect);
                if (distance < shortestDistance || shortestDistance == 0) {
                    closestPlayerStartPosition.set(playerStartPositionRect);
                    shortestDistance = distance;
                }
            }
        }
        playerStartLocationTable.put(Maps.valueOf(currentMapName), closestPlayerStartPosition.cpy());
    }

    public MapLayer getCollisionLayer() {
        return collisionLayer;
    }

    public MapLayer getPortalLayer() {
        return portalLayer;
    }

    public Vector2 getPlayerStartUnitScaled() {
        Vector2 playerStart = this.playerStart.cpy();
        playerStart.set(this.playerStart.x * UNIT_SCALE, this.playerStart.y * UNIT_SCALE);
        return playerStart;
    }

    private enum Maps {
        MAP1("map1.tmx"),
        MAP2("map2.tmx");

        private final String mapPath;

        Maps(String mapPath) {
            this.mapPath = "maps/" + mapPath;
        }

        String getMapPath() {
            return mapPath;
        }
    }

    private enum MapLayers {
        COLLISION_LAYER, SPAWNS_LAYER, PORTAL_LAYER;
    }

}
