package nl.t64.game.rpg.components.character;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import nl.t64.game.rpg.events.Event;
import nl.t64.game.rpg.events.character.LoadPartyMemberEvent;
import nl.t64.game.rpg.events.character.PositionEvent;


public class PhysicsPartyMember extends PhysicsComponent {

    public PhysicsPartyMember() {
        this.boundingBoxWidthPercentage = 0.80f;
        this.boundingBoxHeightPercentage = 0.40f;
    }

    @Override
    public void receive(Event event) {
        if (event instanceof LoadPartyMemberEvent) {
            currentPosition = ((LoadPartyMemberEvent) event).position;
            setBoundingBox();
        }
    }

    @Override
    public void dispose() {
        // empty
    }

    @Override
    public void update(Character partyMember, float dt) {
        partyMember.send(new PositionEvent(currentPosition));
    }

    @Override
    public void debug(ShapeRenderer shapeRenderer) {
        shapeRenderer.setColor(Color.YELLOW);
        shapeRenderer.rect(boundingBox.x, boundingBox.y, boundingBox.width, boundingBox.height);
    }

}
