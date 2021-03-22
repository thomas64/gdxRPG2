package nl.t64.game.rpg.screens.inventory;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import lombok.AllArgsConstructor;

import java.util.function.IntConsumer;


@AllArgsConstructor
class ListenerKeyVertical extends InputListener {

    private final IntConsumer updateIndexFunction;

    @Override
    public boolean keyDown(InputEvent event, int keycode) {
        switch (keycode) {
            case Input.Keys.UP -> updateIndexFunction.accept(-1);
            case Input.Keys.DOWN -> updateIndexFunction.accept(1);
        }
        return true;
    }

}
