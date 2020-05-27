package nl.t64.game.rpg.screens.inventory.tooltip;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;


public class ButtonTooltipListener extends ClickListener {

    private static final float OFFSET_X = 0f;
    private static final float OFFSET_Y = -100f;

    private final ButtonToolTip toolTip;
    private final String title;
    private final Vector2 currentCoords;
    private final Vector2 offset;
    private boolean isInside;
    private boolean touchDown;

    public ButtonTooltipListener(ButtonToolTip toolTip, String title) {
        this.toolTip = toolTip;
        this.title = title;
        this.currentCoords = new Vector2();
        this.offset = new Vector2(OFFSET_X, OFFSET_Y);
        this.isInside = false;
        this.touchDown = false;
    }

    @Override
    public boolean mouseMoved(InputEvent event, float x, float y) {
        if (isInside) {
            setTooltipPosition(event, x, y);
        }
        return false;
    }

    @Override
    public void touchDragged(InputEvent event, float x, float y, int pointer) {
        toolTip.setVisible(false);
        touchDown = false;
    }

    @Override
    public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
        touchDown = true;
        toolTip.setVisible(false);
        return true;
    }

    @Override
    public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
        isInside = true;
        if (!touchDown) {
            setTooltipPosition(event, x, y);
            toolTip.updateDescription(title);
            toolTip.toFront();
            toolTip.setVisible(true);
        }
    }

    @Override
    public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
        toolTip.setVisible(false);
        isInside = false;
        touchDown = false;
    }

    private void setTooltipPosition(InputEvent event, float x, float y) {
        currentCoords.set(x, y);
        event.getListenerActor().localToStageCoordinates(currentCoords);
        toolTip.setPosition(currentCoords.x + offset.x, currentCoords.y + offset.y);
    }

}
