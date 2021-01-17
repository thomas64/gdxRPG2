package nl.t64.game.rpg.screens.world;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import nl.t64.game.rpg.Utils;
import nl.t64.game.rpg.audio.AudioCommand;
import nl.t64.game.rpg.audio.AudioEvent;
import nl.t64.game.rpg.components.character.Direction;
import nl.t64.game.rpg.constants.Constant;
import nl.t64.game.rpg.profile.ProfileManager;
import nl.t64.game.rpg.profile.ProfileObserver;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;


public class MapManager extends MapSubject implements ProfileObserver {

    GameMap currentMap;
    private GameMap newMap;

    @Override
    public void onNotifyCreateProfile(ProfileManager profileManager) {
        loadMap(Constant.STARTING_MAP);
        currentMap.setPlayerSpawnLocationForNewLoad(Constant.STARTING_MAP);
        onNotifySaveProfile(profileManager);
        notifyMapChanged(currentMap);
    }

    @Override
    public void onNotifySaveProfile(ProfileManager profileManager) {
        profileManager.setProperty("mapTitle", currentMap.mapTitle);
    }

    @Override
    public void onNotifyLoadProfile(ProfileManager profileManager) {
        String mapTitle = profileManager.getProperty("mapTitle", String.class);
        loadMap(mapTitle);
        currentMap.setPlayerSpawnLocationForNewLoad(mapTitle);
        notifyMapChanged(currentMap);
    }

    public List<Sprite> getLightmapCamera(Camera camera) {
        final float cameraWidth = camera.viewportWidth;
        final float cameraHeight = camera.viewportHeight;
        final float mapWidth = currentMap.getPixelWidth() / camera.zoom;
        final float mapHeight = currentMap.getPixelHeight() / camera.zoom;
        final float minWidth = Math.min(cameraWidth, mapWidth);
        final float minHeight = Math.min(cameraHeight, mapHeight);
        final float halfWidth = minWidth / 2f;
        final float halfHeight = minHeight / 2f;
        final float quarterWidth = minWidth / 4f;
        final float quarterHeight = minHeight / 4f;

        List<Sprite> lightmap = new ArrayList<>();
        for (Texture texture : currentMap.lightmapCamera) {
            var sprite = new Sprite(texture);
            sprite.setSize(halfWidth, halfHeight);
            sprite.setPosition(-quarterWidth, -quarterHeight);
            lightmap.add(sprite);
        }
        return lightmap;
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

    public void updateQuestLayers() {
        currentMap.questBlockers.forEach(GameMapQuestBlocker::update);
        currentMap.upperTextures.forEach(GameMapQuestTexture::update);
        currentMap.lowerTextures.forEach(GameMapQuestTexture::update);
    }

    public AudioEvent getGroundSound(Vector2 playerFeetPosition) {
        return AudioEvent.from(currentMap.getUnderground(playerFeetPosition));
    }

    public List<Rectangle> getAllBlockers() {
        return currentMap.getAllBlockers().collect(Collectors.toUnmodifiableList());
    }

    public Optional<String> getNoteId(Rectangle checkRect) {
        return currentMap.getNoteIdBeingCheckedBy(checkRect);
    }

    public void checkSavePoints(Rectangle checkRect) {
        if (currentMap.areSavePointsBeingCheckedBy(checkRect)) {
            Utils.getProfileManager().saveProfile();
        }
    }

    public void checkWarpPoints(Rectangle checkRect, Direction direction) {
        currentMap.getWarpPointBeingCheckedBy(checkRect)
                  .ifPresent(warpPoint -> {
                      Utils.getAudioManager().handle(AudioCommand.SE_PLAY_ONCE, AudioEvent.SE_WARP);
                      newMap = new GameMap(warpPoint.toMapName);
                      notifyMapWillChange(() -> changeMapWithCameraShake(warpPoint, direction), warpPoint.fadeColor);
                  });
    }

    public void collisionPortals(Rectangle playerRect, Direction direction) {
        currentMap.getPortalOnCollisionBy(playerRect)
                  .ifPresent(portal -> {
                      newMap = new GameMap(portal.toMapName);
                      notifyMapWillChange(() -> changeMap(portal, direction), portal.fadeColor);
                  });
    }

    private void changeMapWithCameraShake(GameMapPortal portal, Direction direction) {
        changeMap(portal, direction);
        notifyShakeCamera();
    }

    private void changeMap(GameMapPortal portal, Direction direction) {
        portal.setEnterDirection(direction);
        loadMap(portal.toMapName);
        currentMap.setPlayerSpawnLocation(portal);
        notifyMapChanged(currentMap);
    }

    public void collisionEvent(Rectangle playerRect, BiConsumer<String, String> notifyShowConversationDialog) {
        currentMap.getEventCollisionBy(playerRect)
                  .ifPresent(event -> event.startConversation(notifyShowConversationDialog));
    }

    public void collisionQuestTasks(Rectangle playerRect) {
        currentMap.getQuestTaskCollisionBy(playerRect)
                  .ifPresent(GameMapQuestObject::setQuestTaskComplete);
    }

    public void checkQuestTasks(Rectangle checkRect) {
        currentMap.getQuestTaskBeingCheckedBy(checkRect)
                  .forEach(GameMapQuestObject::setQuestTaskComplete);
    }

    public void removeFromBlockers(Rectangle immobileNpc) {
        currentMap.removeFromBlockers(immobileNpc);
        currentMap.setTiledGraph();
    }

    public boolean areBlockersCurrentlyBlocking(Rectangle characterRect) {
        return currentMap.areBlockersCurrentlyBlocking(characterRect);
    }

    public boolean areBlockersCurrentlyBlocking(Vector2 point) {
        return currentMap.areBlockersCurrentlyBlocking(point);
    }

    public void continueAudio() {
        if (currentMap != null) {
            Utils.getAudioManager().handle(AudioCommand.BGM_PLAY_LOOP, currentMap.bgm);
            Utils.getAudioManager().handle(AudioCommand.BGS_PLAY_LOOP, currentMap.bgs);
        }
    }

    public void fadeAudio() {
        Utils.getAudioManager().possibleBgmFade(getMapBgm(currentMap), getMapBgm(newMap));
        Utils.getAudioManager().possibleBgsFade(getMapBgs(currentMap), getMapBgs(newMap));
    }

    private void loadMap(String mapTitle) {
        AudioEvent prevBgm = getMapBgm(currentMap);
        List<AudioEvent> prevBgs = getMapBgs(currentMap);
        disposeOldMaps();
        currentMap = new GameMap(mapTitle);
        AudioEvent nextBgm = getMapBgm(currentMap);
        List<AudioEvent> nextBgs = getMapBgs(currentMap);
        Utils.getAudioManager().possibleBgmSwitch(prevBgm, nextBgm);
        Utils.getAudioManager().possibleBgsSwitch(prevBgs, nextBgs);
    }

    public void disposeOldMaps() {
        if (currentMap != null) {
            currentMap.dispose();
            currentMap = null;
        }
        if (newMap != null) {
            newMap.dispose();
            newMap = null;
        }
    }

    private AudioEvent getMapBgm(GameMap gameMap) {
        return Optional.ofNullable(gameMap)
                       .map(map -> map.bgm)
                       .orElse(AudioEvent.NONE);
    }

    private List<AudioEvent> getMapBgs(GameMap gameMap) {
        return Optional.ofNullable(gameMap)
                       .map(map -> map.bgs)
                       .orElse(List.of(AudioEvent.NONE));
    }

}
