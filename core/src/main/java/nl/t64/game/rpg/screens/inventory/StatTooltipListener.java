package nl.t64.game.rpg.screens.inventory;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import nl.t64.game.rpg.components.party.SuperEnum;


public class StatTooltipListener extends ClickListener {

    private static final float OFFSET_X = 20f;
    private static final float OFFSET_Y = 10f;

    private final StatTooltip toolTip;
    private final SuperEnum superEnum;
    private final Vector2 currentCoords;
    private final Vector2 offset;

    StatTooltipListener(StatTooltip toolTip, SuperEnum superEnum) {
        this.toolTip = toolTip;
        this.superEnum = superEnum;
        this.currentCoords = new Vector2(0, 0);
        this.offset = new Vector2(OFFSET_X, OFFSET_Y);
    }

    @Override
    public boolean mouseMoved(InputEvent event, float x, float y) {
        currentCoords.set(x, y);
        event.getListenerActor().localToStageCoordinates(currentCoords);
        toolTip.setPosition(currentCoords.x + offset.x, currentCoords.y + offset.y);
        return false;
    }

    @Override
    public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
        toolTip.updateDescription(superEnum);
        toolTip.toFront();
        toolTip.setVisible(true);
    }

    @Override
    public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
        toolTip.setVisible(false);
    }

}
