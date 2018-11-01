package nl.t64.game.rpg;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import lombok.Getter;
import lombok.Setter;
import nl.t64.game.rpg.constants.MapTitle;
import nl.t64.game.rpg.profile.ProfileManager;
import nl.t64.game.rpg.profile.ProfileObserver;
import nl.t64.game.rpg.tiled.GameMap;
import nl.t64.game.rpg.tiled.Npc;
import nl.t64.game.rpg.tiled.Portal;
import nl.t64.game.rpg.tiled.SpawnPoint;


public class MapManager implements ProfileObserver {

    private static final String TAG = MapManager.class.getSimpleName();
    private static final MapManager INSTANCE = new MapManager();

    private static final MapTitle START_OF_GAME_MAP = MapTitle.MAP4;

    @Getter
    private GameMap currentMap;
    @Getter
    @Setter
    private Camera camera;

    @Getter
    @Setter
    private boolean mapChanged = false;

    private MapManager() {
    }

    public static MapManager getInstance() {
        return INSTANCE;
    }

    @Override
    public void onNotifyCreate() {
        loadMap(START_OF_GAME_MAP);
        currentMap.setPlayerSpawnLocationForNewLoad(START_OF_GAME_MAP);
        onNotifySave();
    }

    @Override
    public void onNotifySave() {
        ProfileManager.getInstance().setProperty("MapTitle", getCurrentMap().getMapTitle().name());
    }

    @Override
    public void onNotifyLoad() {
        String mapTitleString = ProfileManager.getInstance().getProperty("MapTitle", String.class);
        MapTitle mapTitleEnum = MapTitle.valueOf(mapTitleString);
        loadMap(mapTitleEnum);
        currentMap.setPlayerSpawnLocationForNewLoad(mapTitleEnum);
    }

    public void loadMap(MapTitle mapTitle) {
        disposeOldMap();
        currentMap = new GameMap(mapTitle);
        mapChanged = true;
    }

    private void disposeOldMap() {
        if (currentMap != null) {
            currentMap.dispose();
        }
    }

    public void debug(ShapeRenderer shapeRenderer) {
        Rectangle rect;

        shapeRenderer.setColor(Color.YELLOW);
        for (Rectangle blocker : currentMap.getBlockers()) {
            shapeRenderer.rect(blocker.x, blocker.y, blocker.width, blocker.height);
        }

        shapeRenderer.setColor(Color.BLUE);
        for (Portal portal : currentMap.getPortals()) {
            rect = portal.getRectangle();
            shapeRenderer.rect(rect.x, rect.y, rect.width, rect.height);
        }
        for (SpawnPoint spawnPoint : currentMap.getSpawnPoints()) {
            rect = spawnPoint.getRectangle();
            shapeRenderer.rect(rect.x, rect.y, rect.width, rect.height);
        }
        for (Npc npc : currentMap.getNpcs()) {
            rect = npc.getRectangle();
            shapeRenderer.rect(rect.x, rect.y, rect.width, rect.height);
        }
    }

}
