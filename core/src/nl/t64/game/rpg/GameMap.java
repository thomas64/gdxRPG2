package nl.t64.game.rpg;

import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Vector2;
import lombok.Getter;
import nl.t64.game.rpg.constants.Constant;
import nl.t64.game.rpg.constants.Direction;
import nl.t64.game.rpg.constants.MapLayerName;
import nl.t64.game.rpg.constants.MapTitle;
import nl.t64.game.rpg.tiled.Portal;
import nl.t64.game.rpg.tiled.SpawnPoint;

import java.util.ArrayList;
import java.util.List;


public class GameMap {

    private static final String TAG = GameMap.class.getSimpleName();
    private static final String PLAYER_START = "player_start";

    @Getter
    private TiledMap tiledMap;
    private String mapName;
    private TiledMapTileLayer bottomLayer;

    @Getter
    private Vector2 playerSpawnLocation;
    @Getter
    private Direction playerSpawnDirection;

    @Getter
    private List<RectangleMapObject> blockers = new ArrayList<>();
    @Getter
    private List<Portal> portals = new ArrayList<>();
    @Getter
    private List<SpawnPoint> spawnPoints = new ArrayList<>();


    public GameMap(String mapName) {
        String mapFullPath = MapTitle.valueOf(mapName.toUpperCase()).getMapPath();
        Utility.loadMapAsset(mapFullPath);
        this.tiledMap = Utility.getMapAsset(mapFullPath);
        this.mapName = mapName;
        this.bottomLayer = (TiledMapTileLayer) this.tiledMap.getLayers().get(0);
        this.playerSpawnLocation = new Vector2(0, 0);

        loadBlockers();
        loadPortals();
        loadSpawnPoints();
    }

    public void setPlayerStartOfGameSpawnLocation() {
        if (playerSpawnLocation.isZero()) {
            Portal startOfGamePortal = new Portal(null, PLAYER_START, null, "");
            setPlayerSpawnLocation(startOfGamePortal);
        }
    }

    public void setPlayerSpawnLocation(Portal portal) {
        for (SpawnPoint spawnPoint : spawnPoints) {
            if (spawnPoint.isInConnectionWith(portal)) {
                playerSpawnLocation.set(spawnPoint.getX(), spawnPoint.getY());
                setPlayerSpawnDirection(portal, spawnPoint);
                Logger.playerSpawnLocation(TAG, playerSpawnLocation.x, playerSpawnLocation.y);
                return;
            }
        }
    }

    private void setPlayerSpawnDirection(Portal portal, SpawnPoint spawnPoint) {
        if (spawnPoint.getDirection().isEmpty()) {
            playerSpawnDirection = portal.getEnterDirection();
        } else {
            playerSpawnDirection = Direction.valueOf(spawnPoint.getDirection().toUpperCase());
        }
    }

    public float getPixelWidth() {
        return this.bottomLayer.getWidth() * Constant.TILE_SIZE / 2f;
    }

    public float getPixelHeight() {
        return this.bottomLayer.getHeight() * Constant.TILE_SIZE / 2f;
    }

    private void loadBlockers() {
        MapLayer collisionLayer = tiledMap.getLayers().get(MapLayerName.COLLISION_LAYER.name().toLowerCase());
        for (MapObject mapObject : collisionLayer.getObjects()) {
            RectangleMapObject rectObject = (RectangleMapObject) mapObject;
            blockers.add(rectObject);
        }
    }

    private void loadPortals() {
        MapLayer portalLayer = tiledMap.getLayers().get(MapLayerName.PORTAL_LAYER.name().toLowerCase());
        for (MapObject mapObject : portalLayer.getObjects()) {
            RectangleMapObject rectObject = (RectangleMapObject) mapObject;
            String toMapLocation = rectObject.getProperties().get("type", String.class);
            Portal p = new Portal(rectObject.getRectangle(), mapName, rectObject.getName(), toMapLocation);
            portals.add(p);
        }
    }

    private void loadSpawnPoints() {
        MapLayer spawnsLayer = tiledMap.getLayers().get(MapLayerName.SPAWNS_LAYER.name().toLowerCase());
        for (MapObject mapObject : spawnsLayer.getObjects()) {
            RectangleMapObject rectObject = (RectangleMapObject) mapObject;
            String fromMapLocation = rectObject.getProperties().get("type", String.class);
            String direction = rectObject.getProperties().get("direction", String.class);
            SpawnPoint s = new SpawnPoint(rectObject.getRectangle(), rectObject.getName(), fromMapLocation, direction);
            spawnPoints.add(s);
        }
    }

    public void dispose() {
        tiledMap.dispose();
    }

}
