package nl.t64.game.rpg;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import lombok.Getter;
import lombok.Setter;
import nl.t64.game.rpg.constants.MapTitle;
import nl.t64.game.rpg.tiled.Npc;
import nl.t64.game.rpg.tiled.Portal;
import nl.t64.game.rpg.tiled.SpawnPoint;


public class MapManager {

    private static final String TAG = MapManager.class.getSimpleName();

    private GameMap currentMap = null;

    @Getter
    @Setter
    private boolean mapChanged = false;

    public GameMap getCurrentMap() {
        loadStartOfGameMapIfNecessary();
        return currentMap;
    }

    public void loadMap(MapTitle mapTitle) {
        disposeOldMap();
        currentMap = new GameMap(mapTitle);
        mapChanged = true;
    }

    private void loadStartOfGameMapIfNecessary() {
        if (currentMap == null) {
            loadMap(MapTitle.MAP1);
            currentMap.setPlayerStartOfGameSpawnLocation();
        }
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
