package nl.t64.game.rpg.components.character;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import nl.t64.game.rpg.Engine;
import nl.t64.game.rpg.constants.CharacterState;
import nl.t64.game.rpg.constants.Constant;
import nl.t64.game.rpg.constants.Direction;
import nl.t64.game.rpg.events.Event;
import nl.t64.game.rpg.events.character.*;
import nl.t64.game.rpg.tiled.Portal;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


public class PlayerPhysics extends PhysicsComponent {

    private static final String TAG = PlayerPhysics.class.getSimpleName();

    private boolean isActionPressed = false;


    public PlayerPhysics() {
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
        if (event instanceof ActionEvent) {
            isActionPressed = true;
        }
    }

    @Override
    public void dispose() {
        // empty
    }

    @Override
    public void update(Character player, Engine engine, List<Character> npcCharacters, float dt) {
        updateTheseFields(player, engine, npcCharacters);
        checkActionPressed();
        relocate(dt);
        checkObstacles(dt);
        thisCharacter.send(new PositionEvent(currentPosition));
        clearTheseFields();
    }

    private void checkActionPressed() {
        if (isActionPressed) {
            selectNpcCharacterCandidate();
            checkSavePoint();
            isActionPressed = false;
        }
    }

    private void selectNpcCharacterCandidate() {
        theOtherCharacters.forEach(npcCharacter -> {
            npcCharacter.send(new DeselectEvent());
            if (getCheckRect().overlaps(npcCharacter.getBoundingBox())) {
                npcCharacter.send(new SelectEvent());
            }
        });
    }

    private void checkSavePoint() {
        if (engine.getWorldScreen().getCurrentMap().areSavePointsBeingCheckedBy(getCheckRect())) {
            engine.getProfileManager().saveProfile();
        }
    }

    private void relocate(float dt) {
        if (state == CharacterState.WALKING) {
            move(dt);
        }
        if (state == CharacterState.ALIGNING) {
            alignToGrid();
        }
    }

    private void checkObstacles(float dt) {
        if (state == CharacterState.WALKING) {
            checkPortals();
            if (velocity != Constant.MOVE_SPEED_4) {
                turnCharacters();
                checkBlocker(dt);
            }
        }
    }

    private void turnCharacters() {
        theOtherCharacters.stream()
                          .filter(npc -> getALittleBitBiggerBoundingBox().overlaps(npc.getBoundingBox()))
                          .forEach(npc -> npc.send(new WaitEvent(npc.getPosition(), currentPosition)));
    }

    private void checkBlocker(float dt) {
        List<Rectangle> blockers = engine.getWorldScreen().getCurrentMap().getBlockers();
        List<Rectangle> walkingBlockers =
                theOtherCharacters.stream()
                                  .filter(npcCharacter -> npcCharacter.getState() != CharacterState.IMMOBILE)
                                  .map(Character::getBoundingBox)
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

        blocker1.ifPresent(blocker -> {
            moveSide(blocker, dt);
            copyBlockers.remove(blocker);
        });

        blocker2.ifPresent(blocker -> {
            moveSide(blocker, dt);
            copyWalkingBlockers.remove(blocker);
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

    private void checkPortals() {
        for (Portal portal : engine.getWorldScreen().getCurrentMap().getPortals()) {
            if (boundingBox.overlaps(portal.getRectangle())) {
                portal.setEnterDirection(direction);
                engine.getWorldScreen().loadMap(portal.getToMapName());
                engine.getWorldScreen().getCurrentMap().setPlayerSpawnLocation(portal);
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
