package nl.t64.game.rpg.screens.menu;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;


class ListenerMouseScrollPane extends InputListener {

    private final Runnable scrollFunction;

    ListenerMouseScrollPane(Runnable scrollFunction) {
        this.scrollFunction = scrollFunction;
    }

    @Override
    public boolean scrolled(InputEvent event, float x, float y, float amountX, float amountY) {
        scrollFunction.run();
        return true;
    }

}
