package nl.t64.game.rpg.components.character;

import com.badlogic.gdx.ai.pfa.DefaultGraphPath;
import com.badlogic.gdx.math.Vector2;
import nl.t64.game.rpg.Utils;
import nl.t64.game.rpg.constants.CharacterState;
import nl.t64.game.rpg.constants.Direction;
import nl.t64.game.rpg.events.Event;
import nl.t64.game.rpg.events.character.DirectionEvent;
import nl.t64.game.rpg.events.character.StateEvent;
import nl.t64.game.rpg.screens.world.pathfinding.TiledNode;


public class InputPartyMember extends InputComponent {

    private static final int TWO_NODES = 2;
    private static final int SECOND_NODE = 1;

    private Character partyMember;

    @Override
    public void receive(Event event) {
        // empty
    }

    @Override
    public void dispose() {
        // empty
    }

    @Override
    public void update(Character partyMember, float dt) {
        this.partyMember = partyMember;
        setStateAndDirection();
    }

    private void setStateAndDirection() {
        final DefaultGraphPath<TiledNode> path = Utils.getScreenManager().getWorldScreen().getPathOf(partyMember);
        if (path.getCount() > TWO_NODES) {
            setWalking(path);
        } else {
            setIdle();
        }
    }

    private void setWalking(DefaultGraphPath<TiledNode> path) {
        final TiledNode tiledNode = path.get(SECOND_NODE);
        final Vector2 nodePosition = new Vector2(tiledNode.x, tiledNode.y);
        final Vector2 currentGridPosition = new Vector2(partyMember.getPositionInGrid());
        partyMember.send(new StateEvent(CharacterState.WALKING));
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
        partyMember.send(new StateEvent(CharacterState.IDLE));
    }

}
