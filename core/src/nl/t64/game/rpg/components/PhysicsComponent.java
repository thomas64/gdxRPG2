package nl.t64.game.rpg.components;

import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import nl.t64.game.rpg.Camera;
import nl.t64.game.rpg.Logger;
import nl.t64.game.rpg.MapManager;
import nl.t64.game.rpg.constants.Constant;
import nl.t64.game.rpg.constants.Direction;
import nl.t64.game.rpg.constants.EntityState;
import nl.t64.game.rpg.entities.Entity;
import nl.t64.game.rpg.events.*;
import nl.t64.game.rpg.tiled.Portal;

public class PhysicsComponent implements Component {

    private static final String TAG = PhysicsComponent.class.getSimpleName();
    private static final float BOUNDING_BOX_PERCENTAGE = 0.75f;

    private EntityState state;
    private Direction direction = null;
    private Vector2 velocity;
    private Vector2 oldPosition;
    private Vector2 currentPosition;
    private Rectangle boundingBox;

    public PhysicsComponent() {
        this.boundingBox = new Rectangle();
        this.oldPosition = new Vector2();
        this.currentPosition = new Vector2();
        this.velocity = new Vector2(192f, 192f);  // 48 * 4
    }

    @Override
    public void receive(Event event) {
        if (event instanceof StateEvent) {
            state = ((StateEvent) event).getState();
        }
        if (event instanceof StartPositionEvent) {
            currentPosition = ((StartPositionEvent) event).getPosition();
        }
        if (event instanceof DirectionEvent) {
            direction = ((DirectionEvent) event).getDirection();
        }
    }

    @Override
    public void dispose() {
    }

    public void update(Entity entity, MapManager mapManager, Camera camera, float dt) {
        relocate(dt);
        setBoundingBox();
        checkBlocker(mapManager);
        checkPortals(mapManager);
        entity.send(new PositionEvent(currentPosition));
        camera.setPosition(currentPosition);
    }

    private void relocate(float dt) {
        if (state == EntityState.WALKING) {
            move(dt);
        }
        if (state == EntityState.ALIGNING) {
            alignToGrid();
        }
    }

    private void setBoundingBox() {
        float width = Constant.TILE_SIZE * BOUNDING_BOX_PERCENTAGE;
        float height = Constant.TILE_SIZE * BOUNDING_BOX_PERCENTAGE;
        float widthReduction = 1.00f - BOUNDING_BOX_PERCENTAGE;
        float x = currentPosition.x + (Constant.TILE_SIZE * (widthReduction / 2));
        float y = currentPosition.y;
        boundingBox.set(x, y, width, height);
    }

    private void checkBlocker(MapManager mapManager) {
        for (RectangleMapObject blocker : mapManager.getCurrentMap().getBlockers()) {
            while (boundingBox.overlaps(blocker.getRectangle())) {
                moveBack();
            }
        }
    }

    private void checkPortals(MapManager mapManager) {
        for (Portal portal : mapManager.getCurrentMap().getPortals()) {
            if (boundingBox.overlaps(portal.getRectangle())) {

                portal.setEnterDirection(direction);
                mapManager.loadMap(portal.getToMapName());
                mapManager.getCurrentMap().setPlayerSpawnLocation(portal);

                Logger.portalActivated(TAG);
                return;
            }
        }
    }

    private void move(float dt) {
        oldPosition = currentPosition.cpy();

        switch (direction) {
            case NORTH:
                currentPosition.y += velocity.y * dt;
                break;
            case SOUTH:
                currentPosition.y -= velocity.y * dt;
                break;
            case WEST:
                currentPosition.x -= velocity.x * dt;
                break;
            case EAST:
                currentPosition.x += velocity.x * dt;
                break;
        }
    }

    private void moveBack() {
        switch (direction) {
            case NORTH:
                currentPosition.y -= 1;
                break;
            case SOUTH:
                currentPosition.y += 1;
                break;
            case WEST:
                currentPosition.x += 1;
                break;
            case EAST:
                currentPosition.x -= 1;
                break;
        }
        float roundedX = Math.round(currentPosition.x);
        float roundedY = Math.round(currentPosition.y);
        setCurrentPosition(roundedX, roundedY);
        setBoundingBox();
    }

    private void alignToGrid() {
        float roundedX = Math.round(currentPosition.x / Constant.TILE_SIZE) * Constant.TILE_SIZE;
        float roundedY = Math.round(currentPosition.y / Constant.TILE_SIZE) * Constant.TILE_SIZE;
        setCurrentPosition(roundedX, roundedY);
    }

    private void setCurrentPosition(float positionX, float positionY) {
        currentPosition.x = positionX;
        currentPosition.y = positionY;
    }

}
