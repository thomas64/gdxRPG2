package nl.t64.game.rpg.tiled;

import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import lombok.Getter;
import nl.t64.game.rpg.Utility;
import nl.t64.game.rpg.constants.Constant;
import nl.t64.game.rpg.constants.Direction;
import nl.t64.game.rpg.constants.MapTitle;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Getter
public class GameMap {

    private static final String TAG = GameMap.class.getSimpleName();

    private static final String SAVE_LAYER = "save_layer"; // todo, possible temporary maplayer
    private static final String NPC_LAYER = "npc_layer";
    private static final String COLLISION_LAYER = "collision_layer";
    private static final String PORTAL_LAYER = "portal_layer";
    private static final String SPAWN_LAYER = "spawn_layer";

    private TiledMap tiledMap;
    private MapTitle mapTitle;
    private TiledMapTileLayer bottomLayer;

    private Vector2 playerSpawnLocation;
    private Direction playerSpawnDirection;

    private List<Rectangle> savePoints = new ArrayList<>();
    private List<Npc> npcs = new ArrayList<>();
    private List<Rectangle> blockers = new ArrayList<>();
    private List<Portal> portals = new ArrayList<>();
    private List<SpawnPoint> spawnPoints = new ArrayList<>();


    public GameMap(MapTitle mapTitle) {
        this.mapTitle = mapTitle;
        Utility.loadMapAsset(mapTitle.getMapPath());
        this.tiledMap = Utility.getMapAsset(mapTitle.getMapPath());
        this.bottomLayer = (TiledMapTileLayer) this.tiledMap.getLayers().get(0);
        this.playerSpawnLocation = new Vector2(0, 0);

        loadSavePoints();
        loadNpcs();
        loadBlockers();
        loadPortals();
        loadSpawnPoints();
    }

    public void addToBlockers(Rectangle immobileNpc) {
        blockers.add(immobileNpc);
    }

    public boolean isInCollisionWithBlocker(Rectangle characterRect) {
        return blockers.stream().anyMatch(characterRect::overlaps);
    }

    public void setPlayerSpawnLocationForNewLoad(MapTitle mapTitle) {
        MapObject dummyObject = new RectangleMapObject();
        dummyObject.setName(mapTitle.name());
        Portal startOfGamePortal = new Portal(dummyObject, mapTitle);
        setPlayerSpawnLocation(startOfGamePortal);
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

    private void loadSavePoints() {
        getMapLayer(SAVE_LAYER).ifPresent(mapLayer -> {
            for (MapObject mapObject : mapLayer.getObjects()) {
                RectangleMapObject rectObject = (RectangleMapObject) mapObject;
                savePoints.add(rectObject.getRectangle());
            }
        });
    }

    private void loadNpcs() {
        getMapLayer(NPC_LAYER).ifPresent(mapLayer -> {
            for (MapObject mapObject : mapLayer.getObjects()) {
                npcs.add(new Npc(mapObject));
            }
        });
    }

    private void loadBlockers() {
        getMapLayer(COLLISION_LAYER).ifPresent(mapLayer -> {
            for (MapObject mapObject : mapLayer.getObjects()) {
                RectangleMapObject rectObject = (RectangleMapObject) mapObject;
                blockers.add(rectObject.getRectangle());
            }
        });
    }

    private void loadPortals() {
        getMapLayer(PORTAL_LAYER).ifPresent(mapLayer -> {
            for (MapObject mapObject : mapLayer.getObjects()) {
                portals.add(new Portal(mapObject, mapTitle));
            }
        });
    }

    private void loadSpawnPoints() {
        getMapLayer(SPAWN_LAYER).ifPresent(mapLayer -> {
            for (MapObject mapObject : mapLayer.getObjects()) {
                spawnPoints.add(new SpawnPoint(mapObject));
            }
        });
    }

    private Optional<MapLayer> getMapLayer(String layerName) {
        return Optional.ofNullable(tiledMap.getLayers().get(layerName));
    }

    public void dispose() {
        tiledMap.dispose();
    }

}
