package nl.t64.game.rpg.screens.menu;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;


public class MouseScrollListener extends InputListener {

    private final Runnable scrollFunction;

    MouseScrollListener(Runnable scrollFunction) {
        this.scrollFunction = scrollFunction;
    }

    @Override
    public boolean scrolled(InputEvent event, float x, float y, int amount) {
        scrollFunction.run();
        return true;
    }

}
