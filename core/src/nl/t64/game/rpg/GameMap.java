package nl.t64.game.rpg;

import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import lombok.Getter;
import nl.t64.game.rpg.constants.Constant;
import nl.t64.game.rpg.constants.Direction;
import nl.t64.game.rpg.constants.MapLayerName;
import nl.t64.game.rpg.constants.MapTitle;
import nl.t64.game.rpg.tiled.Npc;
import nl.t64.game.rpg.tiled.Portal;
import nl.t64.game.rpg.tiled.SpawnPoint;


public class GameMap {

    private static final String TAG = GameMap.class.getSimpleName();

    @Getter
    private TiledMap tiledMap;
    private MapTitle mapTitle;
    private TiledMapTileLayer bottomLayer;

    @Getter
    private Vector2 playerSpawnLocation;
    @Getter
    private Direction playerSpawnDirection;

    @Getter
    private Array<Npc> npcs = new Array<>();
    @Getter
    private Array<RectangleMapObject> blockers = new Array<>();
    @Getter
    private Array<Portal> portals = new Array<>();
    @Getter
    private Array<SpawnPoint> spawnPoints = new Array<>();


    public GameMap(MapTitle mapTitle) {
        this.mapTitle = mapTitle;
        Utility.loadMapAsset(mapTitle.getMapPath());
        this.tiledMap = Utility.getMapAsset(mapTitle.getMapPath());
        this.bottomLayer = (TiledMapTileLayer) this.tiledMap.getLayers().get(0);
        this.playerSpawnLocation = new Vector2(0, 0);

        loadNpcs();
        loadBlockers();
        loadPortals();
        loadSpawnPoints();
    }

    public void setPlayerStartOfGameSpawnLocation() {
        if (playerSpawnLocation.isZero()) {
            MapObject dummyObject = new RectangleMapObject();
            dummyObject.setName(MapTitle.NONE.name());
            Portal startOfGamePortal = new Portal(dummyObject, MapTitle.MAP1);
            setPlayerSpawnLocation(startOfGamePortal);
        }
    }

    public void setPlayerSpawnLocation(Portal portal) {
        for (SpawnPoint spawnPoint : spawnPoints) {
            if (spawnPoint.isInConnectionWith(portal)) {
                playerSpawnLocation.set(spawnPoint.getX(), spawnPoint.getY());
                setPlayerSpawnDirection(portal, spawnPoint);
                return;
            }
        }
    }

    private void setPlayerSpawnDirection(Portal portal, SpawnPoint spawnPoint) {
        if (spawnPoint.getDirection() == Direction.NONE) {
            playerSpawnDirection = portal.getEnterDirection();
        } else {
            playerSpawnDirection = spawnPoint.getDirection();
        }
    }

    public float getPixelWidth() {
        return bottomLayer.getWidth() * Constant.TILE_SIZE / 2f;
    }

    public float getPixelHeight() {
        return bottomLayer.getHeight() * Constant.TILE_SIZE / 2f;
    }

    private void loadNpcs() {
        MapLayer npcLayer = tiledMap.getLayers().get(MapLayerName.NPC_LAYER.name().toLowerCase());
        for (MapObject mapObject : npcLayer.getObjects()) {
            npcs.add(new Npc(mapObject));
        }
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
            portals.add(new Portal(mapObject, mapTitle));
        }
    }

    private void loadSpawnPoints() {
        MapLayer spawnsLayer = tiledMap.getLayers().get(MapLayerName.SPAWN_LAYER.name().toLowerCase());
        for (MapObject mapObject : spawnsLayer.getObjects()) {
            spawnPoints.add(new SpawnPoint(mapObject));
        }
    }

    public void dispose() {
        tiledMap.dispose();
    }

}
