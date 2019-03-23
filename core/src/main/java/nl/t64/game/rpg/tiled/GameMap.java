package nl.t64.game.rpg.tiled;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Getter
public class GameMap {

    private static final String TAG = GameMap.class.getSimpleName();

    private static final String MAP_PATH = "maps/";
    private static final String MAPFILE_SUFFIX = ".tmx";

    private static final String SAVE_LAYER = "save_layer"; // todo, possible temporary maplayer
    private static final String NPC_LAYER = "npc_layer";
    private static final String COLLISION_LAYER = "collision_layer";
    private static final String PORTAL_LAYER = "portal_layer";
    private static final String SPAWN_LAYER = "spawn_layer";

    private TiledMap tiledMap;
    private String mapTitle;
    private TiledMapTileLayer bottomLayer;

    private Vector2 playerSpawnLocation;
    private Direction playerSpawnDirection;

    private List<Rectangle> savePoints = new ArrayList<>();
    private List<Npc> npcs = new ArrayList<>();
    private List<Rectangle> blockers = new ArrayList<>();
    private List<Portal> portals = new ArrayList<>();
    private List<SpawnPoint> spawnPoints = new ArrayList<>();


    public GameMap(String mapTitle) {
        this.mapTitle = mapTitle;
        this.tiledMap = Utility.getMapAsset(MAP_PATH + mapTitle + MAPFILE_SUFFIX);
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

    public boolean areBlockersCurrentlyBlocking(Rectangle characterRect) {
        return blockers.stream().anyMatch(characterRect::overlaps);
    }

    public boolean areSavePointsBeingCheckedBy(Rectangle characterRect) {
        return savePoints.stream().anyMatch(characterRect::overlaps);
    }

    public void setPlayerSpawnLocationForNewLoad(String mapTitle) {
        MapObject dummyObject = new RectangleMapObject();
        dummyObject.setName(mapTitle);
        Portal spawnForNewLoadPortal = new Portal(dummyObject, mapTitle);
        setPlayerSpawnLocation(spawnForNewLoadPortal);
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

    public void debug(ShapeRenderer shapeRenderer) {
        Rectangle rect;

        shapeRenderer.setColor(Color.YELLOW);
        for (Rectangle blocker : getBlockers()) {
            shapeRenderer.rect(blocker.x, blocker.y, blocker.width, blocker.height);
        }

        shapeRenderer.setColor(Color.BLUE);
        for (Portal portal : getPortals()) {
            rect = portal.getRectangle();
            shapeRenderer.rect(rect.x, rect.y, rect.width, rect.height);
        }
        for (SpawnPoint spawnPoint : getSpawnPoints()) {
            rect = spawnPoint.getRectangle();
            shapeRenderer.rect(rect.x, rect.y, rect.width, rect.height);
        }
        for (Npc npc : getNpcs()) {
            rect = npc.getRectangle();
            shapeRenderer.rect(rect.x, rect.y, rect.width, rect.height);
        }
    }

}
