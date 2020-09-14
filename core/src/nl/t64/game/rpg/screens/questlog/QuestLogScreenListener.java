package nl.t64.game.rpg.screens.questlog;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;


class QuestLogScreenListener extends InputListener {

    private final Runnable closeScreenFunction;

    QuestLogScreenListener(Runnable closeScreenFunction) {
        this.closeScreenFunction = closeScreenFunction;
    }

    @Override
    public boolean keyDown(InputEvent event, int keycode) {
        switch (keycode) {
            case Input.Keys.L,
                    Input.Keys.ESCAPE -> closeScreenFunction.run();
        }
        return true;
    }

}
