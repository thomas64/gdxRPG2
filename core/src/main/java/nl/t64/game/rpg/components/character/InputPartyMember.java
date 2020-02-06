package nl.t64.game.rpg.components.character;

import nl.t64.game.rpg.constants.CharacterState;
import nl.t64.game.rpg.constants.Constant;
import nl.t64.game.rpg.events.Event;
import nl.t64.game.rpg.events.character.SpeedEvent;
import nl.t64.game.rpg.events.character.StateEvent;


public class InputPartyMember extends InputComponent {

    @Override
    public void receive(Event event) {
        // empty for now
    }

    @Override
    public void dispose() {
        // empty
    }

    @Override
    public void update(Character partyMember, float dt) {
        partyMember.send(new StateEvent(CharacterState.WALKING));
        partyMember.send(new SpeedEvent(Constant.MOVE_SPEED_2));
    }

}
