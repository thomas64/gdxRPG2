package nl.t64.game.rpg.components;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import nl.t64.game.rpg.MapManager;
import nl.t64.game.rpg.constants.EntityState;
import nl.t64.game.rpg.entities.Entity;
import nl.t64.game.rpg.events.*;


public class NpcPhysicsComponent extends PhysicsComponent {

    private static final String TAG = NpcPhysicsComponent.class.getSimpleName();
    private static final int WANDER_BOX_SIZE = 240;
    private static final int WANDER_BOX_POSITION = -96;

    private Rectangle wanderBox;


    public NpcPhysicsComponent() {
        this.velocity = new Vector2(48f, 48f);
        this.boundingBoxWidthPercentage = 0.80f;
        this.boundingBoxHeightPercentage = 1.00f;
    }

    @Override
    public void receive(Event event) {
        if (event instanceof StartStateEvent) {
            state = ((StartStateEvent) event).getState();
        }
        if (event instanceof StateEvent) {
            state = ((StateEvent) event).getState();
        }
        if (event instanceof StartPositionEvent) {
            currentPosition = ((StartPositionEvent) event).getPosition();
            wanderBox = new Rectangle(currentPosition.x + WANDER_BOX_POSITION,
                                      currentPosition.y + WANDER_BOX_POSITION,
                                      WANDER_BOX_SIZE, WANDER_BOX_SIZE);
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
    public void update(Entity entity, MapManager mapManager, Array<Entity> npcEntitiesPlusLastOnePlayer, float dt) {
        relocate(dt);
        setBoundingBox();
        checkObstacles(entity, mapManager, npcEntitiesPlusLastOnePlayer);
        entity.send(new PositionEvent(currentPosition));
    }

    private void relocate(float dt) {
        if (state == EntityState.WALKING) {
            move(dt);
        }
    }

    private void checkObstacles(Entity entity, MapManager mapManager, Array<Entity> npcEntitiesPlusLastOnePlayer) {
        if (state == EntityState.WALKING) {
            boolean moveBack1 = checkWanderBox();
            boolean moveBack2 = checkBlocker(mapManager);
            boolean moveBack3 = checkOtherEntities(entity, npcEntitiesPlusLastOnePlayer);
            if (moveBack1 || moveBack2 || moveBack3) {
                entity.send(new CollisionEvent());
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

    private boolean checkBlocker(MapManager mapManager) {
        boolean moveBack = false;
        for (RectangleMapObject blocker : mapManager.getCurrentMap().getBlockers()) {
            while (boundingBox.overlaps(blocker.getRectangle())) {
                moveBack();
                moveBack = true;
            }
        }
        return moveBack;
    }

    private boolean checkOtherEntities(Entity npc, Array<Entity> npcEntitiesPlusLastOnePlayer) {
        boolean moveBack = false;
        for (Entity npcEntity : npcEntitiesPlusLastOnePlayer) {
            if (npcEntity.equals(npc)) {
                continue;
            }
            while (boundingBox.overlaps(npcEntity.getBoundingBox())) {
                moveBack();
                moveBack = true;
            }
        }
        return moveBack;
    }

    @Override
    public void debug(ShapeRenderer shapeRenderer) {
        shapeRenderer.setColor(Color.PURPLE);
        shapeRenderer.rect(wanderBox.x, wanderBox.y, wanderBox.width, wanderBox.height);
        shapeRenderer.setColor(Color.YELLOW);
        shapeRenderer.rect(boundingBox.x, boundingBox.y, boundingBox.width, boundingBox.height);
    }

}
