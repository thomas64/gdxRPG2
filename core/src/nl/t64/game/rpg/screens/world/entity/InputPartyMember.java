package nl.t64.game.rpg.screens.world.entity;

import com.badlogic.gdx.ai.pfa.DefaultGraphPath;
import com.badlogic.gdx.math.Vector2;
import nl.t64.game.rpg.screens.world.entity.events.DirectionEvent;
import nl.t64.game.rpg.screens.world.entity.events.Event;
import nl.t64.game.rpg.screens.world.entity.events.PathUpdateEvent;
import nl.t64.game.rpg.screens.world.entity.events.StateEvent;
import nl.t64.game.rpg.screens.world.pathfinding.TiledNode;


public class InputPartyMember extends InputComponent {

    private static final int TWO_NODES = 2;
    private static final int SECOND_NODE = 1;

    private Entity partyMember;
    private DefaultGraphPath<TiledNode> path;

    @Override
    public void receive(Event event) {
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
        setStateAndDirection();
    }

    private void setStateAndDirection() {
        if (path.getCount() > TWO_NODES) {
            setWalking();
        } else {
            setIdle();
        }
    }

    private void setWalking() {
        final TiledNode tiledNode = path.get(SECOND_NODE);
        final Vector2 nodePosition = new Vector2(tiledNode.x, tiledNode.y);
        final Vector2 currentGridPosition = new Vector2(partyMember.getPositionInGrid());
        partyMember.send(new StateEvent(EntityState.WALKING));
        setDirection(nodePosition, currentGridPosition);
    }

    private void setDirection(Vector2 nodePosition, Vector2 currentGridPosition) {
        if (nodePosition.y > currentGridPosition.y) {
            partyMember.send(new DirectionEvent(Direction.NORTH));
        } else if (nodePosition.y < currentGridPosition.y) {
            partyMember.send(new DirectionEvent(Direction.SOUTH));
        } else if (nodePosition.x < currentGridPosition.x) {
            partyMember.send(new DirectionEvent(Direction.WEST));
        } else if (nodePosition.x > currentGridPosition.x) {
            partyMember.send(new DirectionEvent(Direction.EAST));
        }
    }

    private void setIdle() {
        partyMember.send(new StateEvent(EntityState.IDLE));
    }

}
