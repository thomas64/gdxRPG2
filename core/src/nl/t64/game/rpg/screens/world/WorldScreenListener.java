package nl.t64.game.rpg.screens.world;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import lombok.AllArgsConstructor;
import nl.t64.game.rpg.constants.Constant;
import nl.t64.game.rpg.screens.inventory.InventoryScreen;
import nl.t64.game.rpg.screens.menu.MenuPause;
import nl.t64.game.rpg.screens.questlog.QuestLogScreen;


@AllArgsConstructor
class WorldScreenListener extends InputAdapter {

    private final Runnable doBeforeLoadScreen;
    private final Runnable showHidePartyWindowFunction;

    @Override
    public boolean keyDown(int keycode) {
        switch (keycode) {
            case Constant.KEYCODE_START,
                    Constant.KEYCODE_TOP,
                    Constant.KEYCODE_LEFT,
                    Input.Keys.ESCAPE,
                    Input.Keys.I,
                    Input.Keys.L -> doBeforeLoadScreen.run();
        }

        switch (keycode) {
            case Constant.KEYCODE_START,
                    Input.Keys.ESCAPE -> MenuPause.load();
            case Constant.KEYCODE_TOP,
                    Input.Keys.I -> InventoryScreen.load();
            case Constant.KEYCODE_LEFT,
                    Input.Keys.L -> QuestLogScreen.load();
            case Constant.KEYCODE_SELECT,
                    Input.Keys.P -> showHidePartyWindowFunction.run();
        }
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        switch (keycode) {
            case Input.Keys.F10 -> WorldScreen.setShowGrid();
            case Input.Keys.F11 -> WorldScreen.setShowObjects();
            case Input.Keys.F12 -> WorldScreen.setShowDebug();
        }
        return false;
    }

}
