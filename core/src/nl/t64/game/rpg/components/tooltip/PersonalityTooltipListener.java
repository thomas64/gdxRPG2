package nl.t64.game.rpg.components.tooltip;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import nl.t64.game.rpg.components.party.PersonalityItem;


public class PersonalityTooltipListener extends ClickListener {

    private static final float OFFSET_X = 20f;
    private static final float OFFSET_Y = 10f;

    private final PersonalityTooltip tooltip;
    private final PersonalityItem personalityItem;
    private final Vector2 currentCoords;
    private final Vector2 offset;

    public PersonalityTooltipListener(PersonalityTooltip tooltip, PersonalityItem personalityItem) {
        this.tooltip = tooltip;
        this.personalityItem = personalityItem;
        this.currentCoords = new Vector2();
        this.offset = new Vector2(OFFSET_X, OFFSET_Y);
    }

    @Override
    public boolean mouseMoved(InputEvent event, float x, float y) {
        setTooltipPosition(event, x, y);
        return false;
    }

    @Override
    public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
        setTooltipPosition(event, x, y);
        tooltip.updateDescription(personalityItem);
        tooltip.toFront();
        tooltip.setVisible(true);
    }

    @Override
    public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
        tooltip.setVisible(false);
    }

    private void setTooltipPosition(InputEvent event, float x, float y) {
        currentCoords.set(x, y);
        event.getListenerActor().localToStageCoordinates(currentCoords);
        tooltip.setPosition(currentCoords.x + offset.x, currentCoords.y + offset.y);
    }

}
