package nl.t64.game.rpg.screens.loot;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import lombok.AllArgsConstructor;
import nl.t64.game.rpg.constants.Constant;

import java.util.function.IntConsumer;


@AllArgsConstructor
class LootSlotsTableListener extends InputListener {

    private final Runnable closeScreenFunction;
    private final Runnable toggleTooltip;
    private final Runnable takeItemFunction;
    private final IntConsumer selectNewSlot;
    private final int slotsInRow;

    @Override
    public boolean keyDown(InputEvent event, int keycode) {
        switch (keycode) {
            case Constant.KEYCODE_RIGHT,
                    Input.Keys.ESCAPE -> closeScreenFunction.run();
            case Constant.KEYCODE_SELECT,
                    Input.Keys.T -> toggleTooltip.run();
            case Constant.KEYCODE_BOTTOM,
                    Input.Keys.ENTER,
                    Input.Keys.A -> takeItemFunction.run();
            case Input.Keys.UP -> selectNewSlot.accept(-slotsInRow);
            case Input.Keys.DOWN -> selectNewSlot.accept(slotsInRow);
            case Input.Keys.LEFT -> selectNewSlot.accept(-1);
            case Input.Keys.RIGHT -> selectNewSlot.accept(1);
        }
        return true;
    }

}
