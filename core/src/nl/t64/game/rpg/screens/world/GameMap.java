package nl.t64.game.rpg.screens.world;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import nl.t64.game.rpg.Utils;
import nl.t64.game.rpg.audio.AudioEvent;
import nl.t64.game.rpg.constants.Constant;
import nl.t64.game.rpg.screens.world.entity.Direction;
import nl.t64.game.rpg.screens.world.pathfinding.TiledGraph;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;


class GameMap {

    public static final int LIGHTMAP_REGION_MULTIPLIER = 10;

    private static final String MAP_PATH = "maps/";
    private static final String MAPFILE_SUFFIX = ".tmx";

    private static final String SOUND_LAYER = "sound";
    private static final String EVENT_LAYER = "event";
    private static final String QUEST_LAYER = "quest";
    private static final String UPPER_TEXTURE_LAYER = "upper_texture";
    private static final String LOWER_TEXTURE_LAYER = "lower_texture";
    private static final String SAVE_LAYER = "save";
    private static final String NPC_LAYER = "npc";
    private static final String HERO_LAYER = "hero";
    private static final String COLLISION_LAYER = "collision";
    private static final String SPAWN_LAYER = "spawn";
    private static final String PORTAL_LAYER = "portal";
    private static final String WARP_LAYER = "warp";
    private static final String REST_LAYER = "rest";
    private static final String LIGHTS_LAYER = "lights";

    private static final String WIDTH_PROPERTY = "width";
    private static final String HEIGHT_PROPERTY = "height";
    private static final String BGM_PROPERTY = "bgm";
    private static final String BGS_PROPERTY = "bgs";
    private static final String LIGHTMAP_CAMERA_PROPERTY = "lightmap_camera";
    private static final String LIGHTMAP_MAP_PROPERTY = "lightmap_map";
    private static final String LIGHTMAP_PLAYER_PROPERTY = "lightmap_player";
    private static final String STEP_SOUND_PROPERTY = "step_sound";

    private static final String DEFAULT_BG = "NONE";
    private static final String DEFAULT_LIGHTMAP = "default";
    private static final String DEFAULT_STEP_SOUND = "grass";

    final String mapTitle;
    final TiledMap tiledMap;
    final AudioEvent bgm;
    final List<AudioEvent> bgs;
    final List<Texture> lightmapCamera;
    final Sprite lightmapMap;
    final Sprite lightmapPlayer;

    Vector2 playerSpawnLocation;
    Direction playerSpawnDirection;

    final List<RectangleMapObject> sounds = new ArrayList<>();
    final List<GameMapNpc> npcs = new ArrayList<>();
    final List<GameMapHero> heroes = new ArrayList<>();
    final List<Rectangle> blockers = new ArrayList<>();
    final List<GameMapLight> lights = new ArrayList<>();
    final List<GameMapEvent> eventDiscovers = new ArrayList<>();
    final List<GameMapQuestBlocker> questBlockers = new ArrayList<>();
    final List<GameMapQuestTexture> upperTextures = new ArrayList<>();
    final List<GameMapQuestTexture> lowerTextures = new ArrayList<>();
    final List<GameMapQuestObject> questDiscovers = new ArrayList<>();
    final List<GameMapQuestObject> questCheckers = new ArrayList<>();
    final List<RectangleMapObject> sparkles = new ArrayList<>();
    final List<RectangleMapObject> chests = new ArrayList<>();
    final List<RectangleMapObject> doors = new ArrayList<>();
    private final List<Rectangle> savePoints = new ArrayList<>();
    private final List<GameMapSpawnPoint> spawnPoints = new ArrayList<>();
    private final List<GameMapPortal> portals = new ArrayList<>();
    private final List<GameMapWarpPoint> warpPoints = new ArrayList<>();
    private final List<RectangleMapObject> notes = new ArrayList<>();

    private final String defaultStepSound;

    TiledGraph tiledGraph;

    GameMap(String mapTitle) {
        this.mapTitle = mapTitle;
        this.tiledMap = Utils.getResourceManager().getMapAsset(MAP_PATH + mapTitle + MAPFILE_SUFFIX);
        this.bgm = this.loadBgm();
        this.bgs = this.loadBgs();
        this.lightmapCamera = this.loadLightmapCamera();
        this.lightmapMap = this.loadLightmapMap();
        this.lightmapPlayer = this.loadLightmapPlayer();
        this.defaultStepSound = this.tiledMap.getProperties().get(STEP_SOUND_PROPERTY, DEFAULT_STEP_SOUND, String.class);

        this.playerSpawnLocation = new Vector2();

        this.loadSounds();
        this.loadNpcs();
        this.loadHeroes();
        this.loadBlockers();
        this.loadLights();
        this.loadEventLayer();
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

    String getUnderground(Vector2 point) {
        return sounds.stream()
                     .filter(underground -> underground.getRectangle().contains(point))
                     .findAny()
                     .map(MapObject::getName)
                     .orElse(defaultStepSound);
    }

    boolean areSavePointsBeingCheckedBy(Rectangle checkRect) {
        return savePoints.stream().anyMatch(checkRect::overlaps);
    }

    Optional<GameMapPortal> getPortalOnCollisionBy(Rectangle playerRect) {
        return portals.stream()
                      .filter(portal -> playerRect.overlaps(portal.rectangle))
                      .findFirst();
    }

    Optional<GameMapEvent> getEventCollisionBy(Rectangle playerRect) {
        return eventDiscovers.stream()
                             .filter(discover -> playerRect.overlaps(discover.rectangle))
                             .findFirst();
    }

    Optional<GameMapQuestObject> getQuestTaskCollisionBy(Rectangle playerRect) {
        return questDiscovers.stream()
                             .filter(discover -> playerRect.overlaps(discover.rectangle))
                             .findFirst();
    }

    List<GameMapQuestObject> getQuestTaskBeingCheckedBy(Rectangle checkRect) {
        return questCheckers.stream()
                            .filter(checker -> checkRect.overlaps(checker.rectangle))
                            .collect(Collectors.toList());
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

    private AudioEvent loadBgm() {
        return AudioEvent.valueOf(tiledMap.getProperties().get(BGM_PROPERTY, DEFAULT_BG, String.class).toUpperCase());
    }

    private List<AudioEvent> loadBgs() {
        String audioEventStrings = tiledMap.getProperties().get(BGS_PROPERTY, DEFAULT_BG, String.class);
        return Arrays.stream(audioEventStrings.toUpperCase().split("\\s*,\\s*"))
                     .map(AudioEvent::valueOf)
                     .collect(Collectors.toUnmodifiableList());
    }

    private List<Texture> loadLightmapCamera() {
        String lightmapStrings = tiledMap.getProperties().get(LIGHTMAP_CAMERA_PROPERTY, DEFAULT_LIGHTMAP, String.class);
        return Arrays.stream(lightmapStrings.split("\\s*,\\s*"))
                     .map(Utils::createLightmap)
                     .collect(Collectors.toUnmodifiableList());
    }

    private Sprite loadLightmapMap() {
        String id = tiledMap.getProperties().get(LIGHTMAP_MAP_PROPERTY, DEFAULT_LIGHTMAP, String.class);
        Texture texture = Utils.createLightmap(id);
        texture.setWrap(Texture.TextureWrap.Repeat, Texture.TextureWrap.Repeat);
        var region = new TextureRegion(texture);
        region.setRegionWidth(texture.getWidth() * LIGHTMAP_REGION_MULTIPLIER);
        region.setRegionHeight(texture.getHeight() * LIGHTMAP_REGION_MULTIPLIER);
        return new Sprite(region);
    }

    private Sprite loadLightmapPlayer() {
        return Optional.ofNullable(tiledMap.getProperties().get(LIGHTMAP_PLAYER_PROPERTY, String.class))
                       .map(Utils::createLightmap)
                       .map(Sprite::new)
                       .orElse(null);
    }

    private void loadSounds() {
        getMapLayer(SOUND_LAYER).ifPresent(mapLayer -> {
            for (MapObject mapObject : mapLayer.getObjects()) {
                RectangleMapObject rectObject = (RectangleMapObject) mapObject;
                sounds.add(rectObject);
            }
        });
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

    private void loadLights() {
        getMapLayer(LIGHTS_LAYER).ifPresent(mapLayer -> {
            for (MapObject mapObject : mapLayer.getObjects()) {
                lights.add(new GameMapLight(mapObject));
            }
        });
    }

    private void loadEventLayer() {
        getMapLayer(EVENT_LAYER).ifPresent(mapLayer -> {
            for (MapObject mapObject : mapLayer.getObjects()) {
                if (mapObject.getName().equalsIgnoreCase("discover")) {
                    eventDiscovers.add(new GameMapEvent(mapObject));
                }
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
                } else if (mapObject.getName().startsWith("door")) {
                    doors.add(rectObject);
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

        shapeRenderer.setColor(Color.YELLOW);

        blockers.forEach(rect -> shapeRenderer.rect(rect.x, rect.y, rect.width, rect.height));
        questBlockers.stream()
                     .map(GameMapObject::getRectangle)
                     .forEach(rect -> shapeRenderer.rect(rect.x, rect.y, rect.width, rect.height));

        shapeRenderer.setColor(Color.BLUE);

        Stream.of(portals, spawnPoints, npcs, heroes, eventDiscovers, questDiscovers, questCheckers)
              .flatMap(Collection::stream)
              .map(GameMapObject::getRectangle)
              .forEach(rect -> shapeRenderer.rect(rect.x, rect.y, rect.width, rect.height));

        Stream.of(notes, sparkles, chests, doors)
              .flatMap(Collection::stream)
              .map(RectangleMapObject::getRectangle)
              .forEach(rect -> shapeRenderer.rect(rect.x, rect.y, rect.width, rect.height));
    }

}
