package nl.t64.game.rpg.screens.world;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import nl.t64.game.rpg.Utils;
import nl.t64.game.rpg.constants.Constant;
import nl.t64.game.rpg.constants.Direction;
import nl.t64.game.rpg.profile.ProfileManager;
import nl.t64.game.rpg.profile.ProfileObserver;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


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

    public List<Rectangle> getBlockers() {
        return currentMap.blockers;
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
                      warpPoint.setEnterDirection(direction);
                      loadMap(warpPoint.toMapName);
                      currentMap.setPlayerSpawnLocation(warpPoint);
                      observers.forEach(observer -> observer.onMapChanged(currentMap));
                  });
    }

    public void checkPortals(Rectangle playerRect, Direction direction) {
        currentMap.getPortalOnCollisionBy(playerRect)
                  .ifPresent(portal -> {
                      portal.setEnterDirection(direction);
                      loadMap(portal.toMapName);
                      currentMap.setPlayerSpawnLocation(portal);
                      observers.forEach(observer -> observer.onMapChanged(currentMap));
                  });
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
        disposeOldMap();
        currentMap = new GameMap(mapTitle);
    }

    private void disposeOldMap() {
        if (currentMap != null) {
            currentMap.dispose();
        }
    }

}
