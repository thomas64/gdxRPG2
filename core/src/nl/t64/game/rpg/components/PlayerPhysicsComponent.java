package nl.t64.game.rpg.components;

import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import nl.t64.game.rpg.MapManager;
import nl.t64.game.rpg.constants.Constant;
import nl.t64.game.rpg.constants.EntityState;
import nl.t64.game.rpg.entities.Entity;
import nl.t64.game.rpg.events.*;
import nl.t64.game.rpg.tiled.Portal;


public class PlayerPhysicsComponent extends PhysicsComponent {

    private static final String TAG = PlayerPhysicsComponent.class.getSimpleName();


    public PlayerPhysicsComponent() {
        this.velocity = new Vector2(192f, 192f);  // 48 * 4
        this.boundingBoxWidthPercentage = 0.80f;
        this.boundingBoxHeightPercentage = 0.40f;
    }

    @Override
    public void receive(Event event) {
        if (event instanceof StateEvent) {
            state = ((StateEvent) event).getState();
        }
        if (event instanceof StartPositionEvent) {
            currentPosition = ((StartPositionEvent) event).getPosition();
        }
        if (event instanceof StartDirectionEvent) {
            direction = ((StartDirectionEvent) event).getDirection();
        }
        if (event instanceof DirectionEvent) {
            direction = ((DirectionEvent) event).getDirection();
        }
    }

    @Override
    public void dispose() {
    }

    @Override
    public void update(Entity entity, MapManager mapManager, Array<Entity> npcEntities, float dt) {
        relocate(dt);
        setBoundingBox();
        checkObstacles(mapManager, npcEntities);
        entity.send(new PositionEvent(currentPosition));
    }

    private void relocate(float dt) {
        if (state == EntityState.WALKING) {
            move(dt);
        }
        if (state == EntityState.ALIGNING) {
            alignToGrid();
        }
    }

    private void checkObstacles(MapManager mapManager, Array<Entity> npcEntities) {
        if (state == EntityState.WALKING) {
            checkBlocker(mapManager);
            checkOtherEntities(npcEntities);
            checkPortals(mapManager);
        }
    }

    private void checkBlocker(MapManager mapManager) {
        for (RectangleMapObject blocker : mapManager.getCurrentMap().getBlockers()) {
            while (boundingBox.overlaps(blocker.getRectangle())) {
                moveBack();
            }
        }
    }

    private void checkOtherEntities(Array<Entity> npcEntities) {
        for (Entity npcEntity : npcEntities) {
            while (boundingBox.overlaps(npcEntity.getBoundingBox())) {
                moveBack();
                npcEntity.send(new StateEvent(EntityState.IDLE));
            }
        }
    }

    private void checkPortals(MapManager mapManager) {
        for (Portal portal : mapManager.getCurrentMap().getPortals()) {
            if (boundingBox.overlaps(portal.getRectangle())) {
                portal.setEnterDirection(direction);
                mapManager.loadMap(portal.getToMapName());
                mapManager.getCurrentMap().setPlayerSpawnLocation(portal);
                return;
            }
        }
    }

    private void alignToGrid() {
        float roundedX = Math.round(currentPosition.x / Constant.TILE_SIZE) * Constant.TILE_SIZE;
        float roundedY = Math.round(currentPosition.y / Constant.TILE_SIZE) * Constant.TILE_SIZE;
        setCurrentPosition(roundedX, roundedY);
    }

}
