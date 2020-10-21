package nl.t64.game.rpg.screens.world;

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
import java.util.stream.Collectors;


public class MapManager implements ProfileObserver {

    GameMap currentMap;
    private final List<MapObserver> observers = new ArrayList<>();

    @Override
    public void onNotifyCreateProfile(ProfileManager profileManager) {
        loadMap(Constant.STARTING_MAP);
        currentMap.setPlayerSpawnLocationForNewLoad(Constant.STARTING_MAP);
        onNotifySaveProfile(profileManager);
        observers.forEach(observer -> observer.onMapChanged(currentMap));
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
        observers.forEach(observer -> observer.onMapChanged(currentMap));
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
        return currentMap.getAllBlockers().collect(Collectors.toList());
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
                      warpPoint.setEnterDirection(direction);
                      loadMap(warpPoint.toMapName);
                      currentMap.setPlayerSpawnLocation(warpPoint);
                      observers.forEach(observer -> observer.onMapChanged(currentMap));
                  });
    }

    public void collisionPortals(Rectangle playerRect, Direction direction) {
        currentMap.getPortalOnCollisionBy(playerRect)
                  .ifPresent(portal -> {
                      portal.setEnterDirection(direction);
                      loadMap(portal.toMapName);
                      currentMap.setPlayerSpawnLocation(portal);
                      observers.forEach(observer -> observer.onMapChanged(currentMap));
                  });
    }

    public void collisionQuestTasks(Rectangle playerRect) {
        currentMap.getQuestTaskCollisionBy(playerRect)
                  .ifPresent(GameMapQuestObject::setQuestTaskComplete);
    }

    public void checkQuestTasks(Rectangle checkRect) {
        currentMap.getQuestTaskBeingCheckedBy(checkRect)
                  .ifPresent(GameMapQuestObject::setQuestTaskComplete);
    }

    public void removeFromBlockers(Rectangle immobileNpc) {
        currentMap.removeFromBlockers(immobileNpc);
    }

    public boolean areBlockersCurrentlyBlocking(Rectangle characterRect) {
        return currentMap.areBlockersCurrentlyBlocking(characterRect);
    }

    public boolean areBlockersCurrentlyBlocking(Vector2 point) {
        return currentMap.areBlockersCurrentlyBlocking(point);
    }

    public void addObserver(MapObserver observer) {
        observers.add(observer);
    }

    public void removeObserver(MapObserver observer) {
        observers.remove(observer);
    }

    private void loadMap(String mapTitle) {
        AudioEvent prevBgm = getMapBgm();
        List<AudioEvent> prevBgs = getMapBgs();
        disposeOldMap();
        currentMap = new GameMap(mapTitle);
        AudioEvent nextBgm = currentMap.bgm;
        List<AudioEvent> nextBgs = currentMap.bgs;
        Utils.getAudioManager().possibleBgmSwitch(prevBgm, nextBgm);
        Utils.getAudioManager().possibleBgsSwitch(prevBgs, nextBgs);
    }

    public void disposeOldMap() {
        if (currentMap != null) {
            currentMap.dispose();
            currentMap = null;
        }
    }

    private AudioEvent getMapBgm() {
        if (currentMap != null) {
            return currentMap.bgm;
        } else {
            return AudioEvent.NONE;
        }
    }

    private List<AudioEvent> getMapBgs() {
        if (currentMap != null) {
            return currentMap.bgs;
        } else {
            return List.of(AudioEvent.NONE);
        }
    }

}
