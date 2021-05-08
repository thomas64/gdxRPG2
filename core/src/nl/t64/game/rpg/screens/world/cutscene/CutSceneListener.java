package nl.t64.game.rpg.screens.world.cutscene;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import lombok.AllArgsConstructor;
import nl.t64.game.rpg.constants.Constant;


@AllArgsConstructor
class CutSceneListener extends InputListener {

    private final Runnable closeCutscene;

    @Override
    public boolean keyDown(InputEvent event, int keycode) {
        switch (keycode) {
            case Input.Keys.ESCAPE,
                    Constant.KEYCODE_START -> closeCutscene.run();
        }
        return true;
    }

}
