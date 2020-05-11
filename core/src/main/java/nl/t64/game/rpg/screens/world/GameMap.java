package nl.t64.game.rpg.screens.world;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import nl.t64.game.rpg.Utils;
import nl.t64.game.rpg.constants.Constant;
import nl.t64.game.rpg.constants.Direction;
import nl.t64.game.rpg.screens.world.pathfinding.TiledGraph;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


class GameMap {

    private static final String MAP_PATH = "maps/";
    private static final String MAPFILE_SUFFIX = ".tmx";

    private static final String SAVE_LAYER = "save_layer";
    private static final String NPC_LAYER = "npc_layer";
    private static final String HERO_LAYER = "hero_layer";
    private static final String COLLISION_LAYER = "collision_layer";
    private static final String SPAWN_LAYER = "spawn_layer";
    private static final String PORTAL_LAYER = "portal_layer";
    private static final String WARP_LAYER = "warp_layer";
    private static final String REST_LAYER = "rest_layer";

    private static final String WIDTH_PROPERTY = "width";
    private static final String HEIGHT_PROPERTY = "height";

    final String mapTitle;
    final TiledMap tiledMap;

    Vector2 playerSpawnLocation;
    Direction playerSpawnDirection;

    final List<GameMapNpc> npcs = new ArrayList<>();
    final List<GameMapHero> heroes = new ArrayList<>();
    final List<Rectangle> blockers = new ArrayList<>();
    private final List<Rectangle> savePoints = new ArrayList<>();
    private final List<GameMapSpawnPoint> spawnPoints = new ArrayList<>();
    private final List<GameMapPortal> portals = new ArrayList<>();
    private final List<GameMapWarpPoint> warpPoints = new ArrayList<>();
    private final List<RectangleMapObject> notes = new ArrayList<>();

    final TiledGraph tiledGraph;

    GameMap(String mapTitle) {
        this.mapTitle = mapTitle;
        this.tiledMap = Utils.getResourceManager().getMapAsset(MAP_PATH + mapTitle + MAPFILE_SUFFIX);
        this.playerSpawnLocation = new Vector2();

        this.loadNpcs();
        this.loadHeroes();
        this.loadBlockers();
        this.loadSavePoints();
        this.loadSpawnPoints();
        this.loadPortals();
        this.loadWarpPoints();
        this.loadNotes();

        this.tiledGraph = new TiledGraph(this.getWidth(), this.getHeight(), this::areBlockersCurrentlyBlocking);
    }

    void addToBlockers(Rectangle immobileNpc) {
        blockers.add(immobileNpc);
    }

    void removeFromBlockers(Rectangle immobileNpc) {
        blockers.remove(immobileNpc);
    }

    boolean areBlockersCurrentlyBlocking(Rectangle characterRect) {
        return blockers.stream().anyMatch(characterRect::overlaps);
    }

    boolean areBlockersCurrentlyBlocking(Vector2 point) {
        return blockers.stream()
                       .anyMatch(blocker -> blocker.contains(point.x * (Constant.TILE_SIZE / 2f) + 1f,
                                                             point.y * (Constant.TILE_SIZE / 2f) + 1f));
    }

    boolean areSavePointsBeingCheckedBy(Rectangle checkRect) {
        return savePoints.stream().anyMatch(checkRect::overlaps);
    }

    Optional<GameMapPortal> getPortalOnCollisionBy(Rectangle playerRect) {
        return portals.stream()
                      .filter(portal -> playerRect.overlaps(portal.rectangle))
                      .findFirst();
    }

    Optional<GameMapWarpPoint> getWarpPointBeingCheckedBy(Rectangle checkRect) {
        return warpPoints.stream()
                         .filter(warpPoint -> checkRect.overlaps(warpPoint.rectangle))
                         .findFirst();
    }

    Optional<String> getNoteIdBeingCheckedBy(Rectangle checkRect) {
        return notes.stream()
                    .filter(note -> checkRect.overlaps(note.getRectangle()))
                    .findFirst()
                    .map(MapObject::getName);
    }

    void setPlayerSpawnLocationForNewLoad(String mapTitle) {
        MapObject dummyObject = new RectangleMapObject();
        dummyObject.setName(mapTitle);
        GameMapPortal spawnForNewLoadPortal = new GameMapPortal(dummyObject, mapTitle);
        setPlayerSpawnLocation(spawnForNewLoadPortal);
    }

    void setPlayerSpawnLocation(GameMapPortal portal) {
        for (GameMapSpawnPoint spawnPoint : spawnPoints) {
            if (spawnPoint.isInConnectionWith(portal)) {
                playerSpawnLocation.set(spawnPoint.getX(), spawnPoint.getY());
                setPlayerSpawnDirection(portal, spawnPoint);
                return;
            }
        }
    }

    private void setPlayerSpawnDirection(GameMapPortal portal, GameMapSpawnPoint spawnPoint) {
        if (spawnPoint.direction.equals(Direction.NONE)) {
            playerSpawnDirection = portal.enterDirection;
        } else {
            playerSpawnDirection = spawnPoint.direction;
        }
    }

    boolean isOutsideMap(Vector2 point) {
        return point.x < 0 || point.x >= getWidth()
               || point.y < 0 || point.y >= getHeight();
    }

    float getPixelWidth() {
        return getWidth() * (Constant.TILE_SIZE / 2f);
    }

    float getPixelHeight() {
        return getHeight() * (Constant.TILE_SIZE / 2f);
    }

    private int getWidth() {
        return (int) tiledMap.getProperties().get(WIDTH_PROPERTY);
    }

    private int getHeight() {
        return (int) tiledMap.getProperties().get(HEIGHT_PROPERTY);
    }

    private void loadNpcs() {
        getMapLayer(NPC_LAYER).ifPresent(mapLayer -> {
            for (MapObject mapObject : mapLayer.getObjects()) {
                npcs.add(new GameMapNpc(mapObject));
            }
        });
    }

    private void loadHeroes() {
        var gameData = Utils.getGameData();
        getMapLayer(HERO_LAYER).ifPresent(mapLayer -> {
            for (MapObject mapObject : mapLayer.getObjects()) {
                String heroId = mapObject.getName();
                if (gameData.getHeroes().contains(heroId)) {
                    heroes.add(new GameMapHero(mapObject));
                }
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

    private void loadSavePoints() {
        getMapLayer(SAVE_LAYER).ifPresent(mapLayer -> {
            for (MapObject mapObject : mapLayer.getObjects()) {
                RectangleMapObject rectObject = (RectangleMapObject) mapObject;
                savePoints.add(rectObject.getRectangle());
            }
        });
    }

    private void loadSpawnPoints() {
        getMapLayer(SPAWN_LAYER).ifPresent(mapLayer -> {
            for (MapObject mapObject : mapLayer.getObjects()) {
                spawnPoints.add(new GameMapSpawnPoint(mapObject));
            }
        });
    }

    private void loadPortals() {
        getMapLayer(PORTAL_LAYER).ifPresent(mapLayer -> {
            for (MapObject mapObject : mapLayer.getObjects()) {
                portals.add(new GameMapPortal(mapObject, mapTitle));
            }
        });
    }

    private void loadWarpPoints() {
        getMapLayer(WARP_LAYER).ifPresent(mapLayer -> {
            for (MapObject mapObject : mapLayer.getObjects()) {
                warpPoints.add(new GameMapWarpPoint(mapObject, mapTitle));
            }
        });
    }

    private void loadNotes() {
        getMapLayer(REST_LAYER).ifPresent(mapLayer -> {
            for (MapObject mapObject : mapLayer.getObjects()) {
                if (mapObject.getName().startsWith("note")) {
                    RectangleMapObject rectObject = (RectangleMapObject) mapObject;
                    notes.add(rectObject);
                }
            }
        });
    }

    private Optional<MapLayer> getMapLayer(String layerName) {
        return Optional.ofNullable(tiledMap.getLayers().get(layerName));
    }

    void dispose() {
        tiledMap.dispose();
    }

    void debug(ShapeRenderer shapeRenderer) {
        Rectangle rect;

        shapeRenderer.setColor(Color.YELLOW);
        for (Rectangle blocker : blockers) {
            shapeRenderer.rect(blocker.x, blocker.y, blocker.width, blocker.height);
        }

        shapeRenderer.setColor(Color.BLUE);
        for (GameMapPortal portal : portals) {
            rect = portal.rectangle;
            shapeRenderer.rect(rect.x, rect.y, rect.width, rect.height);
        }
        for (GameMapSpawnPoint spawnPoint : spawnPoints) {
            rect = spawnPoint.rectangle;
            shapeRenderer.rect(rect.x, rect.y, rect.width, rect.height);
        }
        for (GameMapNpc npc : npcs) {
            rect = npc.rectangle;
            shapeRenderer.rect(rect.x, rect.y, rect.width, rect.height);
        }
        for (GameMapHero hero : heroes) {
            rect = hero.rectangle;
            shapeRenderer.rect(rect.x, rect.y, rect.width, rect.height);
        }
    }

}
