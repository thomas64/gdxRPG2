package nl.t64.game.rpg.screens.questlog;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import lombok.AllArgsConstructor;
import nl.t64.game.rpg.constants.Constant;


@AllArgsConstructor
class QuestLogScreenListener extends InputListener {

    private final Runnable closeScreenFunction;

    @Override
    public boolean keyDown(InputEvent event, int keycode) {
        switch (keycode) {
            case Constant.KEYCODE_RIGHT,
                    Input.Keys.L,
                    Input.Keys.ESCAPE -> closeScreenFunction.run();
        }
        return true;
    }

}
