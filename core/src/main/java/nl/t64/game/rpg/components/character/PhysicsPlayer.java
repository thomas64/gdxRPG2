package nl.t64.game.rpg.components.character;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import nl.t64.game.rpg.Utils;
import nl.t64.game.rpg.constants.CharacterState;
import nl.t64.game.rpg.constants.Constant;
import nl.t64.game.rpg.constants.Direction;
import nl.t64.game.rpg.events.Event;
import nl.t64.game.rpg.events.character.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Collectors;


public class PhysicsPlayer extends PhysicsComponent {

    private List<Character> npcCharacters;
    private boolean isActionPressed;


    public PhysicsPlayer() {
        this.isActionPressed = false;
        this.boundingBoxWidthPercentage = 0.80f;
        this.boundingBoxHeightPercentage = 0.40f;
    }

    @Override
    public void receive(Event event) {
        if (event instanceof StateEvent) {
            state = ((StateEvent) event).state;
        }
        if (event instanceof StartPositionEvent) {
            currentPosition = ((StartPositionEvent) event).position;
            setBoundingBox();
        }
        if (event instanceof StartDirectionEvent) {
            direction = ((StartDirectionEvent) event).direction;
        }
        if (event instanceof DirectionEvent) {
            direction = ((DirectionEvent) event).direction;
        }
        if (event instanceof SpeedEvent) {
            velocity = ((SpeedEvent) event).moveSpeed;
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
    public void update(Character player, float dt) {
        npcCharacters = Utils.getScreenManager().getWorldScreen().getNpcCharacters();
        checkActionPressed();
        relocate(dt);
        checkObstacles(dt);
        player.send(new PositionEvent(currentPosition));
    }

    private void checkActionPressed() {
        if (isActionPressed) {
            selectNpcCharacterCandidate();
            checkSavePoints();
            checkWarpPoints();
            isActionPressed = false;
        }
    }

    private void selectNpcCharacterCandidate() {
        npcCharacters.forEach(npc -> npc.send(new DeselectEvent()));
        Optional<Character> npcCharacter = npcCharacters.stream()
                                                        .filter(checkRectOverlapsNpc())
                                                        .findFirst();
        npcCharacter.ifPresent(npc -> {
            npc.send(new SelectEvent());
            npc.send(new WaitEvent(npc.getPosition(), currentPosition));
        });
    }

    private void checkSavePoints() {
        Utils.getMapManager().checkSavePoints(getCheckRect());
    }

    private void checkWarpPoints() {
        Utils.getMapManager().checkWarpPoints(getCheckRect(), direction);
    }

    private void relocate(float dt) {
        if (state.equals(CharacterState.WALKING)) {
            move(dt);
        }
        if (state.equals(CharacterState.ALIGNING)) {
            alignToGrid();
        }
    }

    private void checkObstacles(float dt) {
        if (state.equals(CharacterState.WALKING)) {
            if (velocity != Constant.MOVE_SPEED_4) {
                turnCharacters();
                checkBlocker(dt);
            }
            checkPortals();
        } else if (state.equals(CharacterState.IDLE)) {
            turnCharacters();
        }
    }

    private void turnCharacters() {
        npcCharacters.stream()
                     .filter(littleBitBiggerBoxOverlapsNpc().or(checkRectOverlapsNpc()))
                     .forEach(sendWaitEvent());
    }

    private Predicate<Character> checkRectOverlapsNpc() {
        return npcCharacter -> getCheckRect().overlaps(npcCharacter.getBoundingBox());
    }

    private Predicate<Character> littleBitBiggerBoxOverlapsNpc() {
        return npcCharacter -> getALittleBitBiggerBoundingBox().overlaps(npcCharacter.getBoundingBox());
    }

    private Consumer<Character> sendWaitEvent() {
        return npcCharacter -> npcCharacter.send(new WaitEvent(npcCharacter.getPosition(), currentPosition));
    }

    private void checkBlocker(float dt) {
        List<Rectangle> blockers = Utils.getMapManager().getBlockers();
        List<Rectangle> walkingBlockers = npcCharacters.stream()
                                                       .filter(stateIsNotImmobile())
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

    private Predicate<Character> stateIsNotImmobile() {
        return npcCharacter -> !npcCharacter.getState().equals(CharacterState.IMMOBILE);
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
        if (doesPlayerOverlap(copyBlockers) || doesPlayerOverlap(copyWalkingBlockers)) {
            currentPosition.set(oldPosition.x, oldPosition.y);
            setBoundingBox();
            return true;
        }
        return false;
    }

    private void possibleMoveBack(List<Rectangle> blockers, List<Rectangle> walkingBlockers) {
        while (doesPlayerOverlap(blockers) || doesPlayerOverlap(walkingBlockers)) {
            moveBack();
        }
    }

    private boolean doesPlayerOverlap(List<Rectangle> blockers) {
        return blockers.stream().anyMatch(boundingBox::overlaps);
    }

    private void checkPortals() {
        Utils.getMapManager().checkPortals(boundingBox, direction);
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
            case NORTH -> setNorth(checkRect);
            case SOUTH -> setSouth(checkRect);
            case WEST -> setWest(checkRect);
            case EAST -> setEast(checkRect);
            default -> throw new IllegalArgumentException(String.format("Direction '%s' not usable.", direction));
        }
        return checkRect;
    }

    private void setNorth(Rectangle checkRect) {
        checkRect.setWidth(boundingBox.width / 4);
        checkRect.setHeight(boundingBox.height * 2);
        checkRect.setX(boundingBox.x + (boundingBox.width / 2) - (boundingBox.width / 8));
        checkRect.setY(boundingBox.y + (Constant.TILE_SIZE / 2f));
    }

    private void setSouth(Rectangle checkRect) {
        checkRect.setWidth(boundingBox.width / 4);
        checkRect.setX(boundingBox.x + (boundingBox.width / 2) - (boundingBox.width / 8));
        checkRect.setY(boundingBox.y - (Constant.TILE_SIZE / 2f));
    }

    private void setWest(Rectangle checkRect) {
        checkRect.setHeight(boundingBox.height / 4);
        checkRect.setY(boundingBox.y + (boundingBox.height / 2) - (boundingBox.height / 8));
        checkRect.setX(boundingBox.x - (Constant.TILE_SIZE / 2f));
    }

    private void setEast(Rectangle checkRect) {
        checkRect.setHeight(boundingBox.height / 4);
        checkRect.setY(boundingBox.y + (boundingBox.height / 2) - (boundingBox.height / 8));
        checkRect.setX(boundingBox.x + (Constant.TILE_SIZE / 2f));
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
