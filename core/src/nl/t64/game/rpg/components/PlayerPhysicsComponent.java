package nl.t64.game.rpg.components;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.Ray;
import nl.t64.game.rpg.MapManager;
import nl.t64.game.rpg.constants.Constant;
import nl.t64.game.rpg.constants.Direction;
import nl.t64.game.rpg.constants.EntityState;
import nl.t64.game.rpg.entities.Entity;
import nl.t64.game.rpg.events.*;
import nl.t64.game.rpg.tiled.Portal;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


public class PlayerPhysicsComponent extends PhysicsComponent {

    private static final String TAG = PlayerPhysicsComponent.class.getSimpleName();
    private static final float MINIMUM_SELECT_DISTANCE = 144f;

    private boolean isMouseSelectEnabled = false;
    private Vector3 mouseSelectCoordinates;
    private Ray selectionRay;
    private boolean isActionPressed = false;


    public PlayerPhysicsComponent() {
        this.boundingBoxWidthPercentage = 0.80f;
        this.boundingBoxHeightPercentage = 0.40f;
        mouseSelectCoordinates = new Vector3(0, 0, 0);
        selectionRay = new Ray(new Vector3(), new Vector3());
    }

    @Override
    public void receive(Event event) {
        if (event instanceof StateEvent) {
            state = ((StateEvent) event).getState();
        }
        if (event instanceof StartPositionEvent) {
            currentPosition = ((StartPositionEvent) event).getPosition();
            setBoundingBox();
        }
        if (event instanceof StartDirectionEvent) {
            direction = ((StartDirectionEvent) event).getDirection();
        }
        if (event instanceof DirectionEvent) {
            direction = ((DirectionEvent) event).getDirection();
        }
        if (event instanceof SpeedEvent) {
            velocity = ((SpeedEvent) event).getMoveSpeed();
        }
        if (event instanceof StartSelectEvent) {
            mouseSelectCoordinates = ((StartSelectEvent) event).getMouseCoordinates();
            isMouseSelectEnabled = true;
        }
        if (event instanceof ActionEvent) {
            isActionPressed = true;
        }

    }

    @Override
    public void dispose() {
    }

    @Override
    public void update(Entity player, MapManager mapManager, List<Entity> npcEntities, float dt) {
        checkMouseSelection(mapManager, npcEntities);
        checkActionPressed(npcEntities);

        relocate(dt);
        checkObstacles(mapManager, npcEntities, dt);
        player.send(new PositionEvent(currentPosition));
    }

    private void checkMouseSelection(MapManager mapManager, List<Entity> npcEntities) {
        if (isMouseSelectEnabled) {
            selectMapEntityCandidate(mapManager, npcEntities);
            isMouseSelectEnabled = false;
        }
    }

    private void checkActionPressed(List<Entity> npcEntities) {
        if (isActionPressed) {
            selectMapEntityCandidate(npcEntities);
            isActionPressed = false;
        }
    }

    private void selectMapEntityCandidate(List<Entity> npcEntities) {
        npcEntities.forEach(npcEntity -> {
            npcEntity.send(new DeselectEvent());
            if (getCheckRect().overlaps(npcEntity.getBoundingBox())) {
                npcEntity.send(new SelectEvent());
            }
        });
    }

    private void selectMapEntityCandidate(MapManager mapManager, List<Entity> npcEntities) {
        mapManager.getCamera().unproject(mouseSelectCoordinates);
        npcEntities.forEach(npcEntity -> {
            npcEntity.send(new DeselectEvent());
            if (npcEntity.getBoundingBox().contains(mouseSelectCoordinates.x, mouseSelectCoordinates.y)) {
                setPossibleSelection(npcEntity);
            }
        });
    }

    private void setPossibleSelection(Entity npcEntity) {
        Rectangle npcBbox = npcEntity.getBoundingBox();
        selectionRay.set(boundingBox.x, boundingBox.y, 0f, npcBbox.x, npcBbox.y, 0f);
        float distance = selectionRay.origin.dst(selectionRay.direction);
        if (distance <= MINIMUM_SELECT_DISTANCE) {
            npcEntity.send(new SelectEvent());
        }
    }

    private void relocate(float dt) {
        if (state == EntityState.WALKING) {
            move(dt);
        }
        if (state == EntityState.ALIGNING) {
            alignToGrid();
        }
    }

    private void checkObstacles(MapManager mapManager, List<Entity> npcEntities, float dt) {
        if (state == EntityState.WALKING && velocity != Constant.MOVE_SPEED_4) {
            turnEntities(npcEntities);
            checkBlocker(mapManager, npcEntities, dt);
            checkPortals(mapManager);
        }
    }

    private void turnEntities(List<Entity> npcEntities) {
        npcEntities.stream()
                   .filter(npc -> getALittleBitBiggerBoundingBox().overlaps(npc.getBoundingBox()))
                   .forEach(npc -> npc.send(new WaitEvent(npc.getPosition(), currentPosition)));
    }

    private void checkBlocker(MapManager mapManager, List<Entity> npcEntities, float dt) {

        List<Rectangle> blockers = mapManager.getCurrentMap().getBlockers();
        List<Rectangle> walkingBlockers = npcEntities.stream()
                                                     .filter(entity -> entity.getState() != EntityState.IMMOBILE)
                                                     .map(Entity::getBoundingBox)
                                                     .collect(Collectors.toList());

        List<Rectangle> copyBlockers = new ArrayList<>(blockers);
        List<Rectangle> copyWalkingBlockers = new ArrayList<>(walkingBlockers);

        possibleMoveSide(copyBlockers, copyWalkingBlockers, dt);
        setRoundPosition();
        if (setPossibleOldPosition(copyBlockers, copyWalkingBlockers)) {
            return;
        }
        possibleMoveBack(blockers, walkingBlockers);
    }

    private void possibleMoveSide(List<Rectangle> copyBlockers, List<Rectangle> copyWalkingBlockers, float dt) {
        Optional<Rectangle> blocker1 = getOnlySingleCollisionBlocker(copyBlockers);
        Optional<Rectangle> blocker2 = getOnlySingleCollisionBlocker(copyWalkingBlockers);

        blocker1.ifPresent(blckr -> {
            moveSide(blckr, dt);
            copyBlockers.remove(blckr);
        });

        blocker2.ifPresent(blckr -> {
            moveSide(blckr, dt);
            copyWalkingBlockers.remove(blckr);
        });
    }

    private Optional<Rectangle> getOnlySingleCollisionBlocker(List<Rectangle> blockerList) {
        int count = 0;
        Rectangle blocker = null;
        for (Rectangle blockerRect : blockerList) {
            if (boundingBox.overlaps(blockerRect)) {
                count++;
                blocker = blockerRect;
            }
        }
        if (count == 1) {
            return Optional.of(blocker);
        } else {
            return Optional.empty();
        }
    }

    private boolean setPossibleOldPosition(List<Rectangle> copyBlockers, List<Rectangle> copyWalkingBlockers) {
        if (copyBlockers.stream().anyMatch(boundingBox::overlaps) ||
                copyWalkingBlockers.stream().anyMatch(boundingBox::overlaps)) {
            currentPosition.set(oldPosition.x, oldPosition.y);
            setBoundingBox();
            return true;
        }
        return false;
    }

    private void possibleMoveBack(List<Rectangle> blockers, List<Rectangle> walkingBlockers) {
        while (blockers.stream().anyMatch(boundingBox::overlaps) ||
                walkingBlockers.stream().anyMatch(boundingBox::overlaps)) {
            moveBack();
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

    private void moveSide(Rectangle blocker, float dt) {
        if (direction == Direction.NORTH || direction == Direction.SOUTH) {
            blockerIsWest(blocker, dt);
            blockerIsEast(blocker, dt);
        } else if (direction == Direction.WEST || direction == Direction.EAST) {
            blockerIsNorth(blocker, dt);
            blockerIsSouth(blocker, dt);
        }
    }

    private void blockerIsWest(Rectangle blocker, float dt) {
        if (boundingBox.x + (boundingBox.width / 2) > blocker.x + blocker.width) {
            currentPosition.x += velocity * dt;
            if (boundingBox.x > blocker.x + blocker.width) {
                currentPosition.x = blocker.x + blocker.width;
            }
        }
    }

    private void blockerIsEast(Rectangle blocker, float dt) {
        if (boundingBox.x + (boundingBox.width / 2) < blocker.x) {
            currentPosition.x -= velocity * dt;
            if (boundingBox.x + boundingBox.width < blocker.x) {
                currentPosition.x = blocker.x - boundingBox.width;
            }
        }
    }

    private void blockerIsNorth(Rectangle blocker, float dt) {
        if (boundingBox.y + (boundingBox.height / 2) < blocker.y) {
            currentPosition.y -= velocity * dt;
            if (boundingBox.y + boundingBox.height < blocker.y) {
                currentPosition.y = blocker.y - boundingBox.height;
            }
        }
    }

    private void blockerIsSouth(Rectangle blocker, float dt) {
        if (boundingBox.y + (boundingBox.height / 2) > blocker.y + blocker.height) {
            currentPosition.y += velocity * dt;
            if (boundingBox.y > blocker.y + blocker.height) {
                currentPosition.y = blocker.y + blocker.height;
            }
        }
    }

    private void alignToGrid() {
        float roundedX = Math.round(currentPosition.x / Constant.TILE_SIZE) * Constant.TILE_SIZE;
        float roundedY = Math.round(currentPosition.y / Constant.TILE_SIZE) * Constant.TILE_SIZE;
        currentPosition.set(roundedX, roundedY);
    }

    private Rectangle getALittleBitBiggerBoundingBox() {
        Rectangle aLittleBitBiggerBox = new Rectangle(boundingBox);
        aLittleBitBiggerBox.setWidth(boundingBox.width + 4);
        aLittleBitBiggerBox.setHeight(boundingBox.height + 4);
        aLittleBitBiggerBox.setX(boundingBox.x - 2);
        aLittleBitBiggerBox.setY(boundingBox.y - 2);
        return aLittleBitBiggerBox;
    }

    private Rectangle getCheckRect() {
        Rectangle checkRect = new Rectangle(boundingBox);
        switch (direction) {
            case NORTH:
                checkRect.setWidth(boundingBox.width / 4);
                checkRect.setX(boundingBox.x + (boundingBox.width / 2) - (boundingBox.width / 8));
                checkRect.setY(boundingBox.y + (Constant.TILE_SIZE / 2f));
                break;
            case SOUTH:
                checkRect.setWidth(boundingBox.width / 4);
                checkRect.setX(boundingBox.x + (boundingBox.width / 2) - (boundingBox.width / 8));
                checkRect.setY(boundingBox.y - (Constant.TILE_SIZE / 2f));
                break;
            case WEST:
                checkRect.setHeight(boundingBox.height / 4);
                checkRect.setY(boundingBox.y + (boundingBox.height / 2) - (boundingBox.height / 8));
                checkRect.setX(boundingBox.x - (Constant.TILE_SIZE / 2f));
                break;
            case EAST:
                checkRect.setHeight(boundingBox.height / 4);
                checkRect.setY(boundingBox.y + (boundingBox.height / 2) - (boundingBox.height / 8));
                checkRect.setX(boundingBox.x + (Constant.TILE_SIZE / 2f));
                break;
            default:
                throw new IllegalArgumentException(String.format("Direction '%s' not usable.", direction));
        }
        return checkRect;
    }

    @Override
    public void debug(ShapeRenderer shapeRenderer) {
        shapeRenderer.setColor(Color.YELLOW);
        shapeRenderer.rect(boundingBox.x, boundingBox.y, boundingBox.width, boundingBox.height);
        shapeRenderer.setColor(Color.GREEN);
        shapeRenderer.rect(getALittleBitBiggerBoundingBox().x,
                           getALittleBitBiggerBoundingBox().y,
                           getALittleBitBiggerBoundingBox().width,
                           getALittleBitBiggerBoundingBox().height);
        shapeRenderer.setColor(Color.CYAN);
        shapeRenderer.rect(getCheckRect().x,
                           getCheckRect().y,
                           getCheckRect().width,
                           getCheckRect().height);
    }

}
