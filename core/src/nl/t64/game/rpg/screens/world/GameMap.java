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
import nl.t64.game.rpg.audio.AudioEvent;
import nl.t64.game.rpg.components.character.Direction;
import nl.t64.game.rpg.constants.Constant;
import nl.t64.game.rpg.screens.world.pathfinding.TiledGraph;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;


class GameMap {

    private static final String MAP_PATH = "maps/";
    private static final String MAPFILE_SUFFIX = ".tmx";

    private static final String QUEST_LAYER = "quest_layer";
    private static final String UPPER_TEXTURE_LAYER = "upper_texture_layer";
    private static final String LOWER_TEXTURE_LAYER = "lower_texture_layer";
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
    final AudioEvent bgm;
    final List<AudioEvent> bgs;

    Vector2 playerSpawnLocation;
    Direction playerSpawnDirection;

    final List<GameMapNpc> npcs = new ArrayList<>();
    final List<GameMapHero> heroes = new ArrayList<>();
    final List<Rectangle> blockers = new ArrayList<>();
    final List<GameMapQuestBlocker> questBlockers = new ArrayList<>();
    final List<GameMapQuestTexture> upperTextures = new ArrayList<>();
    final List<GameMapQuestTexture> lowerTextures = new ArrayList<>();
    final List<GameMapQuestObject> questDiscovers = new ArrayList<>();
    final List<GameMapQuestObject> questCheckers = new ArrayList<>();
    final List<RectangleMapObject> sparkles = new ArrayList<>();
    final List<RectangleMapObject> chests = new ArrayList<>();
    private final List<Rectangle> savePoints = new ArrayList<>();
    private final List<GameMapSpawnPoint> spawnPoints = new ArrayList<>();
    private final List<GameMapPortal> portals = new ArrayList<>();
    private final List<GameMapWarpPoint> warpPoints = new ArrayList<>();
    private final List<RectangleMapObject> notes = new ArrayList<>();

    TiledGraph tiledGraph;

    GameMap(String mapTitle) {
        this.mapTitle = mapTitle;
        this.tiledMap = Utils.getResourceManager().getMapAsset(MAP_PATH + mapTitle + MAPFILE_SUFFIX);
        this.bgm = AudioEvent.valueOf(this.tiledMap.getProperties().get("bgm", String.class).toUpperCase());
        String audioEventStrings = this.tiledMap.getProperties().get("bgs", "NONE", String.class);
        this.bgs = Arrays.stream(audioEventStrings.toUpperCase().split("\\s*,\\s*"))
                         .map(AudioEvent::valueOf)
                         .collect(Collectors.toList());
        this.playerSpawnLocation = new Vector2();

        this.loadNpcs();
        this.loadHeroes();
        this.loadBlockers();
        this.loadQuestLayer();
        this.loadTextureLayers();
        this.loadRestLayer();
        this.loadSavePoints();
        this.loadSpawnPoints();
        this.loadPortals();
        this.loadWarpPoints();
    }

    void setTiledGraph() {
        this.tiledGraph = new TiledGraph(this.getWidth(), this.getHeight(), this::areBlockersCurrentlyBlocking);
    }

    void addToBlockers(Rectangle immobileNpc) {
        blockers.add(immobileNpc);
    }

    void removeFromBlockers(Rectangle immobileNpc) {
        blockers.remove(immobileNpc);
    }

    Stream<Rectangle> getAllBlockers() {
        return Stream.concat(blockers.stream(),
                             questBlockers.stream()
                                          .filter(blocker -> blocker.isActive)
                                          .map(blocker -> blocker.rectangle));
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

    Optional<GameMapQuestObject> getQuestTaskCollisionBy(Rectangle playerRect) {
        return questDiscovers.stream()
                             .filter(discover -> playerRect.overlaps(discover.rectangle))
                             .findFirst();
    }

    Optional<GameMapQuestObject> getQuestTaskBeingCheckedBy(Rectangle checkRect) {
        return questCheckers.stream()
                            .filter(checker -> checkRect.overlaps(checkRect))
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

    private void loadQuestLayer() {
        getMapLayer(QUEST_LAYER).ifPresent(mapLayer -> {
            for (MapObject mapObject : mapLayer.getObjects()) {
                if (mapObject.getName().equalsIgnoreCase("blocker")) {
                    questBlockers.add(new GameMapQuestBlocker(mapObject));
                } else if (mapObject.getName().equalsIgnoreCase("discover")) {
                    questDiscovers.add(new GameMapQuestObject(mapObject));
                } else if (mapObject.getName().equalsIgnoreCase("check")) {
                    questCheckers.add(new GameMapQuestObject(mapObject));
                }
            }
        });
    }

    private void loadTextureLayers() {
        getMapLayer(UPPER_TEXTURE_LAYER).ifPresent(mapLayer -> {
            for (MapObject mapObject : mapLayer.getObjects()) {
                upperTextures.add(new GameMapQuestTexture(mapObject));
            }
        });
        getMapLayer(LOWER_TEXTURE_LAYER).ifPresent(mapLayer -> {
            for (MapObject mapObject : mapLayer.getObjects()) {
                lowerTextures.add(new GameMapQuestTexture(mapObject));
            }
        });
    }

    private void loadRestLayer() {
        getMapLayer(REST_LAYER).ifPresent(mapLayer -> {
            for (MapObject mapObject : mapLayer.getObjects()) {
                RectangleMapObject rectObject = (RectangleMapObject) mapObject;
                if (mapObject.getName().startsWith("sparkle")) {
                    sparkles.add(rectObject);
                } else if (mapObject.getName().startsWith("chest")) {
                    chests.add(rectObject);
                } else if (mapObject.getName().startsWith("note")) {
                    notes.add(rectObject);
                }
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
        for (GameMapQuestBlocker blocker : questBlockers) {
            rect = blocker.rectangle;
            shapeRenderer.rect(rect.x, rect.y, rect.width, rect.height);
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
        for (RectangleMapObject note : notes) {
            rect = note.getRectangle();
            shapeRenderer.rect(rect.x, rect.y, rect.width, rect.height);
        }
        for (RectangleMapObject sparkle : sparkles) {
            rect = sparkle.getRectangle();
            shapeRenderer.rect(rect.x, rect.y, rect.width, rect.height);
        }
        for (RectangleMapObject chest : chests) {
            rect = chest.getRectangle();
            shapeRenderer.rect(rect.x, rect.y, rect.width, rect.height);
        }
        for (GameMapQuestObject discover : questDiscovers) {
            rect = discover.rectangle;
            shapeRenderer.rect(rect.x, rect.y, rect.width, rect.height);
        }
        for (GameMapQuestObject checker : questCheckers) {
            rect = checker.rectangle;
            shapeRenderer.rect(rect.x, rect.y, rect.width, rect.height);
        }
    }

}
