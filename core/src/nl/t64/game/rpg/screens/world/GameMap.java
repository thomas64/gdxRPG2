package nl.t64.game.rpg.screens.world;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Vector2;
import nl.t64.game.rpg.Utils;
import nl.t64.game.rpg.audio.AudioEvent;
import nl.t64.game.rpg.constants.Constant;
import nl.t64.game.rpg.screens.world.entity.Direction;
import nl.t64.game.rpg.screens.world.mapobjects.*;
import nl.t64.game.rpg.screens.world.pathfinding.TiledGraph;

import java.util.Collection;
import java.util.List;
import java.util.stream.Stream;


public class GameMap {

    public static final int LIGHTMAP_REGION_MULTIPLIER = 10;

    private static final String SOUND_LAYER = "sound";
    private static final String EVENT_LAYER = "event";
    private static final String CUTSCENE_LAYER = "cutscene";
    private static final String QUEST_LAYER = "quest";
    private static final String UPPER_TEXTURE_LAYER = "upper_texture";
    private static final String LOWER_TEXTURE_LAYER = "lower_texture";
    private static final String SAVE_LAYER = "save";
    private static final String NPC_LAYER = "npc";
    private static final String HERO_LAYER = "hero";
    private static final String ENEMY_LAYER = "enemy";
    private static final String COLLISION_LAYER = "collision";
    private static final String SPAWN_LAYER = "spawn";
    private static final String PORTAL_LAYER = "portal";
    private static final String WARP_LAYER = "warp";
    private static final String REST_LAYER = "rest";
    private static final String LIGHTS_LAYER = "lights";

    private static final String WIDTH_PROPERTY = "width";
    private static final String HEIGHT_PROPERTY = "height";
    private static final String STEP_SOUND_PROPERTY = "step_sound";

    private static final String DEFAULT_STEP_SOUND = "grass";

    final String mapTitle;
    final TiledMap tiledMap;
    final AudioEvent bgm;
    final List<AudioEvent> bgs;
    final TextureRegion parallaxBackground;
    final List<Texture> lightmapCamera;
    final Sprite lightmapMap;
    final Sprite lightmapPlayer;

    Vector2 playerSpawnLocation;
    Direction playerSpawnDirection;

    final List<GameMapNpc> npcs;
    final List<GameMapHero> heroes;
    final List<GameMapNpc> enemies;
    final List<GameMapLight> lights;
    final List<GameMapQuestBlocker> questBlockers;
    final List<GameMapQuestTexture> upperTextures;
    final List<GameMapQuestTexture> lowerTextures;
    final List<RectangleMapObject> sparkles;
    final List<RectangleMapObject> chests;
    final List<RectangleMapObject> doors;
    private final List<RectangleMapObject> sounds;
    private final List<GameMapBlocker> blockers;
    private final List<GameMapEventDiscover> eventDiscovers;
    private final List<GameMapEventChecker> eventCheckers;
    private final List<GameMapCutscene> cutsceneDiscovers;
    private final List<GameMapQuestDiscover> questDiscovers;
    private final List<GameMapQuestChecker> questCheckers;
    private final List<GameMapNote> notes;
    private final List<GameMapSavePoint> savePoints;
    private final List<GameMapSpawnPoint> spawnPoints;
    private final List<GameMapPortal> portals;
    private final List<GameMapWarpPoint> warpPoints;

    private final String defaultStepSound;

    TiledGraph tiledGraph;

    GameMap(String mapTitle) {
        this.mapTitle = mapTitle;
        this.tiledMap = MapManager.getTiledMap(this.mapTitle);
        this.bgm = MapManager.getBgmOfMap(this.tiledMap);
        this.bgs = MapManager.getBgsOfMap(this.tiledMap);

        var loader = new GameMapLayerLoader(tiledMap);

        this.parallaxBackground = loader.loadParallaxBackground();
        this.lightmapCamera = loader.loadLightmapCamera();
        this.lightmapMap = loader.loadLightmapMap();
        this.lightmapPlayer = loader.loadLightmapPlayer();
        this.defaultStepSound = this.tiledMap.getProperties().get(STEP_SOUND_PROPERTY, DEFAULT_STEP_SOUND, String.class);

        this.playerSpawnLocation = new Vector2();


        this.sounds = loader.loadLayer(SOUND_LAYER);
        this.npcs = loader.loadLayer(NPC_LAYER, GameMapNpc::new);
        this.heroes = loader.loadLayer(HERO_LAYER, rectObject -> Utils.getGameData().getHeroes().contains(rectObject.getName()), GameMapHero::new);
        this.enemies = loader.loadLayer(ENEMY_LAYER, GameMapNpc::new);
        this.blockers = loader.loadLayer(COLLISION_LAYER, GameMapBlocker::new);
        this.lights = loader.loadLayer(LIGHTS_LAYER, GameMapLight::new);

        this.eventDiscovers = loader.equalsIgnoreCase(EVENT_LAYER, "discover", GameMapEventDiscover::new);
        this.eventCheckers = loader.equalsIgnoreCase(EVENT_LAYER, "check", GameMapEventChecker::new);

        this.cutsceneDiscovers = loader.equalsIgnoreCase(CUTSCENE_LAYER, "discover", GameMapCutscene::new);

        this.questBlockers = loader.equalsIgnoreCase(QUEST_LAYER, "blocker", GameMapQuestBlocker::new);
        this.questDiscovers = loader.equalsIgnoreCase(QUEST_LAYER, "discover", GameMapQuestDiscover::new);
        this.questCheckers = loader.equalsIgnoreCase(QUEST_LAYER, "check", GameMapQuestChecker::new);

        this.upperTextures = loader.loadTextureLayer(UPPER_TEXTURE_LAYER, GameMapQuestTexture::new);
        this.lowerTextures = loader.loadTextureLayer(LOWER_TEXTURE_LAYER, GameMapQuestTexture::new);

        this.sparkles = loader.startsWith(REST_LAYER, "sparkle");
        this.chests = loader.startsWith(REST_LAYER, "chest");
        this.doors = loader.startsWith(REST_LAYER, "door");
        this.notes = loader.startsWith(REST_LAYER, "note", GameMapNote::new);

        this.savePoints = loader.loadLayer(SAVE_LAYER, GameMapSavePoint::new);
        this.spawnPoints = loader.loadLayer(SPAWN_LAYER, GameMapSpawnPoint::new);
        this.portals = loader.loadLayer(PORTAL_LAYER, rectObject -> new GameMapPortal(rectObject, mapTitle));
        this.warpPoints = loader.loadLayer(WARP_LAYER, rectObject -> new GameMapWarpPoint(rectObject, mapTitle));
    }

    void setTiledGraph() {
        this.tiledGraph = new TiledGraph(getWidth(), getHeight());
    }

    String getUnderground(Vector2 point) {
        return sounds.stream()
                     .filter(underground -> underground.getRectangle().contains(point))
                     .findAny()
                     .map(MapObject::getName)
                     .orElse(defaultStepSound);
    }

    void setPlayerSpawnLocationForNewLoad(String mapTitle) {
        var dummyObject = new RectangleMapObject();
        dummyObject.setName(mapTitle);
        GameMapPortal spawnForNewLoadPortal = new GameMapPortal(dummyObject, mapTitle);
        setPlayerSpawnLocation(spawnForNewLoadPortal);
    }

    void setPlayerSpawnLocation(GameMapRelocator portal) {
        for (GameMapSpawnPoint spawnPoint : spawnPoints) {
            if (spawnPoint.isInConnectionWith(portal)) {
                playerSpawnLocation.set(spawnPoint.getX(), spawnPoint.getY());
                setPlayerSpawnDirection(portal, spawnPoint);
                return;
            }
        }
    }

    private void setPlayerSpawnDirection(GameMapRelocator portal, GameMapSpawnPoint spawnPoint) {
        if (spawnPoint.getDirection().equals(Direction.NONE)) {
            playerSpawnDirection = portal.getEnterDirection();
        } else {
            playerSpawnDirection = spawnPoint.getDirection();
        }
    }

    boolean isOutsideMap(Vector2 point) {
        return point.x < 0 || point.x >= getWidth()
               || point.y < 0 || point.y >= getHeight();
    }

    public float getPixelWidth() {
        return getWidth() * Constant.HALF_TILE_SIZE;
    }

    public float getPixelHeight() {
        return getHeight() * Constant.HALF_TILE_SIZE;
    }

    int getWidth() {
        return (int) tiledMap.getProperties().get(WIDTH_PROPERTY);
    }

    int getHeight() {
        return (int) tiledMap.getProperties().get(HEIGHT_PROPERTY);
    }

    void dispose() {
        tiledMap.dispose();
    }

    void debug(ShapeRenderer shapeRenderer) {

        shapeRenderer.setColor(Color.YELLOW);

        Stream.of(blockers, questBlockers)
              .flatMap(Collection::stream)
              .map(GameMapObject::getRectangle)
              .forEach(rect -> shapeRenderer.rect(rect.x, rect.y, rect.width, rect.height));

        shapeRenderer.setColor(Color.BLUE);

        Stream.of(portals, spawnPoints, npcs, heroes, enemies, eventDiscovers, eventCheckers, cutsceneDiscovers,
                  questDiscovers, questCheckers, notes)
              .flatMap(Collection::stream)
              .map(GameMapObject::getRectangle)
              .forEach(rect -> shapeRenderer.rect(rect.x, rect.y, rect.width, rect.height));

        Stream.of(sparkles, chests, doors)
              .flatMap(Collection::stream)
              .map(RectangleMapObject::getRectangle)
              .forEach(rect -> shapeRenderer.rect(rect.x, rect.y, rect.width, rect.height));
    }

}
