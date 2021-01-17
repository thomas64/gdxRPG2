package nl.t64.game.rpg.components.tooltip;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;


public class ButtonTooltipListener extends ClickListener {

    private static final float OFFSET_X = 0f;
    private static final float OFFSET_Y = -100f;

    private final ButtonTooltip tooltip;
    private final String title;
    private final Vector2 currentCoords;
    private final Vector2 offset;
    private boolean isInside;
    private boolean touchDown;

    public ButtonTooltipListener(ButtonTooltip tooltip, String title) {
        this.tooltip = tooltip;
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
        tooltip.setVisible(false);
        touchDown = false;
    }

    @Override
    public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
        touchDown = true;
        tooltip.setVisible(false);
        return true;
    }

    @Override
    public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
        isInside = true;
        if (!touchDown) {
            setTooltipPosition(event, x, y);
            tooltip.updateDescription(title);
            tooltip.toFront();
            tooltip.setVisible(true);
        }
    }

    @Override
    public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
        tooltip.setVisible(false);
        isInside = false;
        touchDown = false;
    }

    private void setTooltipPosition(InputEvent event, float x, float y) {
        currentCoords.set(x, y);
        event.getListenerActor().localToStageCoordinates(currentCoords);
        tooltip.setPosition(currentCoords.x + offset.x, currentCoords.y + offset.y);
    }

}
