package nl.t64.game.rpg.screens.inventory.equipslot;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import lombok.AllArgsConstructor;

import java.util.function.Consumer;


@AllArgsConstructor
public class EquipSlotsTableListener extends InputListener {

    static final int SIDE_STEP = 10;

    private final Consumer<Integer> trySelectNewSlot;

    @Override
    public boolean keyDown(InputEvent event, int keycode) {
        switch (keycode) {
            case Input.Keys.UP -> trySelectNewSlot.accept(-1);
            case Input.Keys.DOWN -> trySelectNewSlot.accept(1);
            case Input.Keys.LEFT -> trySelectNewSlot.accept(-SIDE_STEP);
            case Input.Keys.RIGHT -> trySelectNewSlot.accept(SIDE_STEP);
        }
        return true;
    }

}
