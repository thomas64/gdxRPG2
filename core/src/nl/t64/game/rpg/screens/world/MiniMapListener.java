package nl.t64.game.rpg.screens.world;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import lombok.AllArgsConstructor;
import nl.t64.game.rpg.constants.Constant;


@AllArgsConstructor
class MiniMapListener extends InputAdapter {

    private final Runnable closeMiniMap;

    @Override
    public boolean keyDown(int keycode) {
        switch (keycode) {
            case Constant.KEYCODE_SELECT,
                    Constant.KEYCODE_RIGHT,
                    Input.Keys.ESCAPE,
                    Input.Keys.M -> closeMiniMap.run();
        }
        return false;
    }

}
