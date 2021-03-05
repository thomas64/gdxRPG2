package nl.t64.game.rpg.screens.world.entity;

import com.badlogic.gdx.ai.pfa.DefaultGraphPath;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import nl.t64.game.rpg.Utils;
import nl.t64.game.rpg.constants.Constant;
import nl.t64.game.rpg.screens.world.entity.events.*;
import nl.t64.game.rpg.screens.world.pathfinding.TiledNode;


public class PhysicsPartyMember extends PhysicsComponent {

    private Entity partyMember;
    private DefaultGraphPath<TiledNode> path;

    public PhysicsPartyMember() {
        this.boundingBoxWidthPercentage = 0.50f;
        this.boundingBoxHeightPercentage = 0.20f;
    }

    @Override
    public void receive(Event event) {
        if (event instanceof LoadEntityEvent loadEvent) {
            state = loadEvent.state;
            currentPosition = loadEvent.position;
            setBoundingBox();
        }
        if (event instanceof StateEvent stateEvent) {
            state = stateEvent.state;
        }
        if (event instanceof DirectionEvent directionEvent) {
            direction = directionEvent.direction;
        }
        if (event instanceof SpeedEvent speedEvent) {
            velocity = speedEvent.moveSpeed;
        }
        if (event instanceof PathUpdateEvent pathUpdateEvent) {
            path = pathUpdateEvent.path;
        }
    }

    @Override
    public void dispose() {
        // empty
    }

    @Override
    public void update(Entity partyMember, float dt) {
        this.partyMember = partyMember;
        relocate(dt);
        checkObstacles(dt);
        partyMember.send(new PositionEvent(currentPosition));
    }

    private void relocate(float dt) {
        if (state.equals(EntityState.WALKING)) {
            move(dt);
        }
    }

    private void checkObstacles(float dt) {
        if (Utils.getMapManager().areBlockersCurrentlyBlocking(boundingBox)) {
            switch (direction) {
                case SOUTH -> setDirectionWhenBlockersAreSouth();
                case NORTH -> setDirectionWhenBlockersAreNorth();
                case WEST -> setDirectionWhenBlockersAreWest();
                case EAST -> setDirectionWhenBlockersAreEast();
                case NONE -> throw new IllegalArgumentException("Direction 'NONE' is not usable.");
            }
            currentPosition.set(oldPosition);
            move(dt);
        }
    }

    private void setDirectionWhenBlockersAreSouth() {
        final var mapManager = Utils.getMapManager();
        final Vector2 positionInGrid = partyMember.getPositionInGrid();
        final float x = positionInGrid.x;
        final float y = positionInGrid.y;

        if (mapManager.areBlockersCurrentlyBlocking(new Vector2(x + 1, y - 1))) {
            direction = Direction.WEST;
        } else if (mapManager.areBlockersCurrentlyBlocking(new Vector2(x - 1, y - 1))) {
            direction = Direction.EAST;
        }
    }

    private void setDirectionWhenBlockersAreNorth() {
        final var mapManager = Utils.getMapManager();
        final Vector2 positionInGrid = partyMember.getPositionInGrid();
        final float x = positionInGrid.x;
        final float y = positionInGrid.y;

        if (mapManager.areBlockersCurrentlyBlocking(new Vector2(x + 1, y + 1))) {
            direction = Direction.WEST;
        } else if (mapManager.areBlockersCurrentlyBlocking(new Vector2(x - 1, y + 1))) {
            direction = Direction.EAST;
        }
    }

    private void setDirectionWhenBlockersAreWest() {
        final var mapManager = Utils.getMapManager();
        final Vector2 positionInGrid = partyMember.getPositionInGrid();
        final float x = positionInGrid.x;
        final float y = positionInGrid.y;

        if (mapManager.areBlockersCurrentlyBlocking(new Vector2(x - 1, y + 1))) {
            direction = Direction.SOUTH;
        } else if (mapManager.areBlockersCurrentlyBlocking(new Vector2(x - 1, y - 1))) {
            direction = Direction.NORTH;
        }
    }

    private void setDirectionWhenBlockersAreEast() {
        final var mapManager = Utils.getMapManager();
        final Vector2 positionInGrid = partyMember.getPositionInGrid();
        final float x = positionInGrid.x;
        final float y = positionInGrid.y;

        if (mapManager.areBlockersCurrentlyBlocking(new Vector2(x + 1, y + 1))) {
            direction = Direction.SOUTH;
        } else if (mapManager.areBlockersCurrentlyBlocking(new Vector2(x + 1, y - 1))) {
            direction = Direction.NORTH;
        }
    }

    @Override
    public void debug(ShapeRenderer shapeRenderer) {
        shapeRenderer.setColor(Color.YELLOW);
        shapeRenderer.rect(boundingBox.x, boundingBox.y, boundingBox.width, boundingBox.height);
        shapeRenderer.setColor(Color.MAGENTA);
        for (TiledNode tiledNode : path) {
            final int x = (int) (tiledNode.x * (Constant.TILE_SIZE / 2f));
            final int y = (int) (tiledNode.y * (Constant.TILE_SIZE / 2f));
            shapeRenderer.rect(x, y, Constant.TILE_SIZE / 2f, Constant.TILE_SIZE / 2f);
        }
    }

}
