package nl.t64.game.rpg.screens.world;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Vector2;
import lombok.Getter;
import nl.t64.game.rpg.ProfileManager;
import nl.t64.game.rpg.Utils;
import nl.t64.game.rpg.audio.AudioCommand;
import nl.t64.game.rpg.audio.AudioEvent;
import nl.t64.game.rpg.constants.Constant;
import nl.t64.game.rpg.screens.world.entity.Direction;
import nl.t64.game.rpg.screens.world.mapobjects.*;
import nl.t64.game.rpg.subjects.ProfileObserver;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;


public class MapManager implements ProfileObserver {

    private static final String MAP_PATH = "maps/";
    private static final String MAPFILE_SUFFIX = ".tmx";
    private static final String BGM_PROPERTY = "bgm";
    private static final String BGS_PROPERTY = "bgs";
    private static final String DEFAULT_BG = "NONE";

    @Getter
    private GameMap currentMap;
    private String nextMapTitle;
    private FogOfWar fogOfWar;
    private float timer = 0;

    @Override
    public void onNotifyCreateProfile(ProfileManager profileManager) {
        fogOfWar = new FogOfWar();
        loadMap(Constant.STARTING_MAP);
        currentMap.setPlayerSpawnLocationForNewLoad(Constant.STARTING_MAP);
        onNotifySaveProfile(profileManager);
        Utils.getBrokerManager().mapObservers.notifyMapChanged(currentMap);
    }

    @Override
    public void onNotifySaveProfile(ProfileManager profileManager) {
        profileManager.setProperty("mapTitle", currentMap.mapTitle);
        profileManager.setProperty("fogOfWar", fogOfWar);
    }

    @Override
    public void onNotifyLoadProfile(ProfileManager profileManager) {
        String mapTitle = profileManager.getProperty("mapTitle", String.class);
        fogOfWar = profileManager.getProperty("fogOfWar", FogOfWar.class);
        loadMap(mapTitle);
        currentMap.setPlayerSpawnLocationForNewLoad(mapTitle);
        Utils.getBrokerManager().mapObservers.notifyMapChanged(currentMap);
    }

    public void loadMapAfterCutscene(String mapTitle, String cutsceneId) {
        loadMap(mapTitle);
        currentMap.setPlayerSpawnLocationForNewLoad(cutsceneId);
        Utils.getBrokerManager().mapObservers.notifyMapChanged(currentMap);
    }

    public List<Sprite> getLightmapCamera(Camera camera) {
        final float cameraWidth = camera.viewportWidth;
        final float cameraHeight = camera.viewportHeight;
        final float mapWidth = currentMap.getPixelWidth() / camera.zoom;
        final float mapHeight = currentMap.getPixelHeight() / camera.zoom;
        final float minWidth = Math.min(cameraWidth, mapWidth);
        final float minHeight = Math.min(cameraHeight, mapHeight);
        final float halfWidth = minWidth * camera.zoom;
        final float halfHeight = minHeight * camera.zoom;
        final float quarterWidth = minWidth * (camera.zoom / 2f);
        final float quarterHeight = minHeight * (camera.zoom / 2f);

        List<Sprite> lightmap = new ArrayList<>();
        for (Texture texture : currentMap.lightmapCamera) {
            var sprite = new Sprite(texture);
            sprite.setSize(halfWidth, halfHeight);
            sprite.setPosition(-quarterWidth, -quarterHeight);
            lightmap.add(sprite);
        }
        return lightmap;
    }

    public TiledMap getTiledMap() {
        return currentMap.tiledMap;
    }

    public Sprite getLightmapMap() {
        return currentMap.lightmapMap;
    }

    public Optional<Sprite> getLightmapPlayer() {
        return Optional.ofNullable(currentMap.lightmapPlayer);
    }

    public List<GameMapQuestTexture> getLowerMapTextures() {
        return currentMap.lowerTextures;
    }

    public List<GameMapQuestTexture> getUpperMapTextures() {
        return currentMap.upperTextures;
    }

    public void updateFogOfWar(Vector2 playerPosition, float dt) {
        timer += dt;
        if (timer > 1f) {
            timer -= 1f;
            fogOfWar.update(playerPosition, currentMap.mapTitle);
        }
    }

    public void drawFogOfWar(ShapeRenderer shapeRenderer) {
        fogOfWar.draw(shapeRenderer, currentMap.mapTitle);
    }

    public void updateQuestLayers() {
        currentMap.questBlockers.forEach(GameMapQuestBlocker::update);
        currentMap.upperTextures.forEach(GameMapQuestTexture::update);
        currentMap.lowerTextures.forEach(GameMapQuestTexture::update);
    }

    public AudioEvent getGroundSound(float x, float y) {
        return getGroundSound(new Vector2(x, y));
    }

    public AudioEvent getGroundSound(Vector2 playerFeetPosition) {
        return AudioEvent.from(currentMap.getUnderground(playerFeetPosition));
    }

    public void checkWarpPoint(GameMapWarpPoint warpPoint, Direction playerDirection) {
        Utils.getAudioManager().handle(AudioCommand.SE_PLAY_ONCE, AudioEvent.SE_WARP);
        nextMapTitle = warpPoint.getToMapName();
        Utils.getBrokerManager().mapObservers.notifyMapWillChange(
                () -> changeMapWithCameraShake(warpPoint, playerDirection), warpPoint.getFadeColor());
    }

    public void collisionPortal(GameMapPortal portal, Direction playerDirection) {
        nextMapTitle = portal.getToMapName();
        Utils.getBrokerManager().mapObservers.notifyMapWillChange(
                () -> changeMap(portal, playerDirection), portal.getFadeColor());
    }

    private void changeMapWithCameraShake(GameMapRelocator warpPoint, Direction direction) {
        changeMap(warpPoint, direction);
        Utils.getBrokerManager().mapObservers.notifyShakeCamera();
    }

    private void changeMap(GameMapRelocator portal, Direction direction) {
        portal.setEnterDirection(direction);
        loadMap(portal.getToMapName());
        currentMap.setPlayerSpawnLocation(portal);
        Utils.getBrokerManager().mapObservers.notifyMapChanged(currentMap);
    }

    public void setTiledGraph() {
        currentMap.setTiledGraph();
    }

    public void continueAudio() {
        if (currentMap != null) {
            Utils.getAudioManager().handle(AudioCommand.BGM_PLAY_LOOP, currentMap.bgm);
            Utils.getAudioManager().handle(AudioCommand.BGS_PLAY_LOOP, currentMap.bgs);
        }
    }

    public void fadeAudio() {
        Utils.getAudioManager().possibleBgmFade(currentMap.bgm, getBgmOfMap(getTiledMap(nextMapTitle)));
        Utils.getAudioManager().possibleBgsFade(currentMap.bgs, getBgsOfMap(getTiledMap(nextMapTitle)));
    }

    public void loadMap(String mapTitle) {
        AudioEvent prevBgm = Optional.ofNullable(currentMap).map(map -> map.bgm).orElse(AudioEvent.NONE);
        List<AudioEvent> prevBgs = Optional.ofNullable(currentMap).map(map -> map.bgs).orElse(List.of(AudioEvent.NONE));
        disposeOldMaps();
        currentMap = new GameMap(mapTitle);
        fogOfWar.putIfAbsent(currentMap);
        AudioEvent nextBgm = currentMap.bgm;
        List<AudioEvent> nextBgs = currentMap.bgs;
        Utils.getAudioManager().possibleBgmSwitch(prevBgm, nextBgm);
        Utils.getAudioManager().possibleBgsSwitch(prevBgs, nextBgs);
    }

    public void disposeOldMaps() {
        Utils.getBrokerManager().actionObservers.removeAllObservers();
        Utils.getBrokerManager().blockObservers.removeAllObservers();
        Utils.getBrokerManager().bumpObservers.removeAllObservers();
        Utils.getBrokerManager().collisionObservers.removeAllObservers();
        if (currentMap != null) {
            currentMap.dispose();
            currentMap = null;
        }
    }

    static AudioEvent getBgmOfMap(TiledMap tiledMap) {
        String audioEventString = tiledMap.getProperties().get(BGM_PROPERTY, DEFAULT_BG, String.class);
        return AudioEvent.valueOf(audioEventString.toUpperCase());
    }

    static List<AudioEvent> getBgsOfMap(TiledMap tiledMap) {
        String audioEventStrings = tiledMap.getProperties().get(BGS_PROPERTY, DEFAULT_BG, String.class);
        return Arrays.stream(audioEventStrings.toUpperCase().split("\\s*,\\s*"))
                     .map(AudioEvent::valueOf)
                     .toList();
    }

    static TiledMap getTiledMap(String mapTitle) {
        return Utils.getResourceManager().getMapAsset(MAP_PATH + mapTitle + MAPFILE_SUFFIX);
    }

}
