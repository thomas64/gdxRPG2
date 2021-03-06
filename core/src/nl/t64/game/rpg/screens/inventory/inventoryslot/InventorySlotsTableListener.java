package nl.t64.game.rpg.screens.inventory.inventoryslot;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import lombok.AllArgsConstructor;
import nl.t64.game.rpg.constants.Constant;

import java.util.function.Consumer;


@AllArgsConstructor
public class InventorySlotsTableListener extends InputListener {

    private final Runnable equipItemFunction;
    private final Consumer<Integer> selectNewSlot;
    private final int slotsInRow;

    @Override
    public boolean keyDown(InputEvent event, int keycode) {
        switch (keycode) {
            case Constant.KEYCODE_BOTTOM,
                    Input.Keys.A -> equipItemFunction.run();
            case Input.Keys.UP -> selectNewSlot.accept(-slotsInRow);
            case Input.Keys.DOWN -> selectNewSlot.accept(slotsInRow);
            case Input.Keys.LEFT -> selectNewSlot.accept(-1);
            case Input.Keys.RIGHT -> selectNewSlot.accept(1);
        }
        return true;
    }

}
