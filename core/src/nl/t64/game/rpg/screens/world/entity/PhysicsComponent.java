package nl.t64.game.rpg.screens.world.entity;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import nl.t64.game.rpg.Utils;
import nl.t64.game.rpg.constants.Constant;
import nl.t64.game.rpg.screens.world.entity.events.CollisionEvent;


abstract class PhysicsComponent implements Component {

    private static final float WANDER_BOX_SIZE = 240f;
    private static final float WANDER_BOX_POSITION = -96f;

    Entity entity;
    EntityState state;
    Direction direction;
    float velocity;
    Rectangle boundingBox;
    Rectangle wanderBox;
    Vector2 oldPosition;
    Vector2 currentPosition;
    float boundingBoxWidthPercentage;
    float boundingBoxHeightPercentage;

    PhysicsComponent() {
        this.direction = null;
        this.boundingBox = new Rectangle();
        this.oldPosition = new Vector2();
        this.currentPosition = new Vector2();
    }

    @Override
    public void dispose() {
        // empty
    }

    public abstract void update(Entity entity, float dt);

    public abstract void debug(ShapeRenderer shapeRenderer);

    boolean doesBoundingBoxOverlapsBlockers() {
        return Utils.getBrokerManager().blockObservers.getCurrentBlockersFor(boundingBox)
                                                      .stream()
                                                      .filter(blocker -> !boundingBox.equals(blocker))
                                                      .anyMatch(blocker -> boundingBox.overlaps(blocker));
    }

    void setBoundingBox() {
        float width = Constant.TILE_SIZE * boundingBoxWidthPercentage;
        float height = Constant.TILE_SIZE * boundingBoxHeightPercentage;
        float widthReduction = 1.00f - boundingBoxWidthPercentage;
        float x = currentPosition.x + (Constant.TILE_SIZE * (widthReduction / 2));
        float y = currentPosition.y;
        boundingBox.set(x, y, width, height);
    }

    void setWanderBox() {
        wanderBox = new Rectangle(currentPosition.x + WANDER_BOX_POSITION,
                                  currentPosition.y + WANDER_BOX_POSITION,
                                  WANDER_BOX_SIZE, WANDER_BOX_SIZE);
    }

    void relocate(float dt) {
        switch (state) {
            case WALKING, FLYING -> move(dt);
            case ALIGNING -> alignToGrid();
        }
    }

    void move(float dt) {
        oldPosition.set(Math.round(currentPosition.x), Math.round(currentPosition.y));

        switch (direction) {
            case NORTH -> currentPosition.y += velocity * dt;
            case SOUTH -> currentPosition.y -= velocity * dt;
            case WEST -> currentPosition.x -= velocity * dt;
            case EAST -> currentPosition.x += velocity * dt;
            case NONE -> throw new IllegalArgumentException("Direction 'NONE' is not usable.");
        }
        setRoundPosition();
    }

    void moveBack() {
        switch (direction) {
            case NORTH -> currentPosition.y -= 1;
            case SOUTH -> currentPosition.y += 1;
            case WEST -> currentPosition.x += 1;
            case EAST -> currentPosition.x -= 1;
            case NONE -> throw new IllegalArgumentException("Direction 'NONE' is not usable.");
        }
        setRoundPosition();
    }

    void setRoundPosition() {
        currentPosition.set(Math.round(currentPosition.x), Math.round(currentPosition.y));
        setBoundingBox();
    }

    Rectangle getRectangle() {
        return new Rectangle(currentPosition.x, currentPosition.y, Constant.TILE_SIZE, Constant.TILE_SIZE);
    }

    void alignToGrid() {
        float roundedX = Math.round(currentPosition.x / Constant.TILE_SIZE) * Constant.TILE_SIZE;
        float roundedY = Math.round(currentPosition.y / Constant.TILE_SIZE) * Constant.TILE_SIZE;
        currentPosition.set(roundedX, roundedY);
    }

    void checkObstacles() {
        switch (state) {
            case WALKING, FLYING -> {
                boolean moveBack1 = checkWanderBox();
                boolean moveBack2 = checkBlockers();
                if (moveBack1 || moveBack2) {
                    entity.send(new CollisionEvent());
                }
            }
        }
    }

    private boolean checkWanderBox() {
        boolean moveBack = false;
        while (!wanderBox.contains(boundingBox)) {
            moveBack();
            moveBack = true;
        }
        return moveBack;
    }

    private boolean checkBlockers() {
        boolean moveBack = false;
        while (doesBoundingBoxOverlapsBlockers()) {
            moveBack();
            moveBack = true;
        }
        return moveBack;
    }

}
