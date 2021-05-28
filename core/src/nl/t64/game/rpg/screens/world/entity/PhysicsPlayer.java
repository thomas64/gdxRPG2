package nl.t64.game.rpg.screens.world.entity;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import nl.t64.game.rpg.Utils;
import nl.t64.game.rpg.constants.Constant;
import nl.t64.game.rpg.screens.world.entity.events.*;

import java.util.List;


public class PhysicsPlayer extends PhysicsComponent {

    private boolean isActionPressed;

    public PhysicsPlayer() {
        this.isActionPressed = false;
        this.boundingBoxWidthPercentage = 0.80f;
        this.boundingBoxHeightPercentage = 0.30f;
    }

    @Override
    public void receive(Event event) {
        if (event instanceof LoadEntityEvent loadEvent) {
            direction = loadEvent.direction;
            currentPosition = loadEvent.position;
            setBoundingBox();
        }
        if (event instanceof StateEvent stateEvent) {
            state = stateEvent.state();
        }
        if (event instanceof DirectionEvent directionEvent) {
            direction = directionEvent.direction();
        }
        if (event instanceof SpeedEvent speedEvent) {
            velocity = speedEvent.moveSpeed();
        }
        if (event instanceof ActionEvent) {
            isActionPressed = true;
        }
    }

    @Override
    public void update(Entity player, float dt) {
        checkActionPressed();
        relocate(dt);
        collisionObstacles(dt);
        Utils.getBrokerManager().detectionObservers.notifyDetection(velocity);
        player.send(new PositionEvent(currentPosition));
    }

    private void checkActionPressed() {
        if (isActionPressed) {
            Utils.getBrokerManager().actionObservers.notifyActionPressed(getCheckRect(), direction, currentPosition);
            isActionPressed = false;
        }
    }

    private void collisionObstacles(float dt) {
        if (velocity != Constant.MOVE_SPEED_4) {
            Utils.getBrokerManager().bumpObservers.notifyBump(getALittleBitBiggerBoundingBox(),
                                                              getCheckRect(), currentPosition);
            if (state.equals(EntityState.WALKING)) {
                collisionBlockers(dt);
                Utils.getBrokerManager().collisionObservers.notifyCollision(boundingBox, direction);
            }
        }
    }

    private void collisionBlockers(float dt) {
        List<Rectangle> blockers = Utils.getBrokerManager().blockObservers.getCurrentBlockersFor(boundingBox);
        if (!blockers.isEmpty()) {
            handleBlockers(blockers, dt);
        }
    }

    private void handleBlockers(List<Rectangle> blockers, float dt) {
        if (blockers.size() == 1) {
            moveSide(blockers.get(0), dt);
        }
        setRoundPosition();
        if (hasHitAnotherBlockWhileSideStepping()) {
            return;
        }
        while (doesBoundingBoxOverlapsBlockers()) {
            moveBack();
        }
    }

    private boolean hasHitAnotherBlockWhileSideStepping() {
        List<Rectangle> blockers = Utils.getBrokerManager().blockObservers.getCurrentBlockersFor(boundingBox);
        if (blockers.size() > 1) {
            currentPosition.set(oldPosition);
            setBoundingBox();
            return true;
        }
        return false;
    }

    private void moveSide(Rectangle blocker, float dt) {
        if (direction.equals(Direction.NORTH) || direction.equals(Direction.SOUTH)) {
            playerIsEastOf(blocker, dt);
            playerIsWestOf(blocker, dt);
        } else if (direction.equals(Direction.WEST) || direction.equals(Direction.EAST)) {
            playerIsSouthOf(blocker, dt);
            playerIsNorthOf(blocker, dt);
        }
    }

    private void playerIsEastOf(Rectangle blocker, float dt) {
        if (boundingBox.x + (boundingBox.width / 2) > blocker.x + blocker.width) {
            currentPosition.x += velocity * dt;
            if (boundingBox.x > blocker.x + blocker.width) {
                currentPosition.x = blocker.x + blocker.width;
            }
        }
    }

    private void playerIsWestOf(Rectangle blocker, float dt) {
        if (boundingBox.x + (boundingBox.width / 2) < blocker.x) {
            currentPosition.x -= velocity * dt;
            if (boundingBox.x + boundingBox.width < blocker.x) {
                currentPosition.x = blocker.x - boundingBox.width;
            }
        }
    }

    private void playerIsSouthOf(Rectangle blocker, float dt) {
        if (boundingBox.y + (boundingBox.height / 2) < blocker.y) {
            currentPosition.y -= velocity * dt;
            if (boundingBox.y + boundingBox.height < blocker.y) {
                currentPosition.y = blocker.y - boundingBox.height;
            }
        }
    }

    private void playerIsNorthOf(Rectangle blocker, float dt) {
        if (boundingBox.y + (boundingBox.height / 2) > blocker.y + blocker.height) {
            currentPosition.y += velocity * dt;
            if (boundingBox.y > blocker.y + blocker.height) {
                currentPosition.y = blocker.y + blocker.height;
            }
        }
    }

    private Rectangle getALittleBitBiggerBoundingBox() {
        Rectangle aLittleBitBiggerBox = new Rectangle(boundingBox);
        aLittleBitBiggerBox.setWidth(boundingBox.width + 24f);
        aLittleBitBiggerBox.setHeight(boundingBox.height + 24f);
        aLittleBitBiggerBox.setX(boundingBox.x - 12f);
        aLittleBitBiggerBox.setY(boundingBox.y - 12f);
        return aLittleBitBiggerBox;
    }

    private Rectangle getCheckRect() {
        Rectangle checkRect = new Rectangle();
        switch (direction) {
            case NORTH -> setNorth(checkRect);
            case SOUTH -> setSouth(checkRect);
            case WEST -> setWest(checkRect);
            case EAST -> setEast(checkRect);
            case NONE -> throw new IllegalArgumentException("Direction 'NONE' is not usable.");
        }
        return checkRect;
    }

    private void setNorth(Rectangle checkRect) {
        checkRect.setWidth(boundingBox.width / 4f);
        checkRect.setHeight(Constant.HALF_TILE_SIZE + 1);
        checkRect.setX(boundingBox.x + (boundingBox.width / 2f) - (boundingBox.width / 8f));
        checkRect.setY(boundingBox.y + boundingBox.height);
    }

    private void setSouth(Rectangle checkRect) {
        checkRect.setWidth(boundingBox.width / 4f);
        checkRect.setHeight(Constant.HALF_TILE_SIZE);
        checkRect.setX(boundingBox.x + (boundingBox.width / 2f) - (boundingBox.width / 8f));
        checkRect.setY(boundingBox.y - Constant.HALF_TILE_SIZE);
    }

    private void setWest(Rectangle checkRect) {
        checkRect.setWidth(Constant.HALF_TILE_SIZE);
        checkRect.setHeight(boundingBox.height + 6f);
        checkRect.setX(boundingBox.x - Constant.HALF_TILE_SIZE);
        checkRect.setY(boundingBox.y - 3f);
    }

    private void setEast(Rectangle checkRect) {
        checkRect.setWidth(Constant.HALF_TILE_SIZE);
        checkRect.setHeight(boundingBox.height + 6f);
        checkRect.setX(boundingBox.x + boundingBox.width);
        checkRect.setY(boundingBox.y - 3f);
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
