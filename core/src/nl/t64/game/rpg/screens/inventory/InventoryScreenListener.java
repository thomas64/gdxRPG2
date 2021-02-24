package nl.t64.game.rpg.screens.inventory;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import nl.t64.game.rpg.constants.Constant;


class InventoryScreenListener extends InputListener {

    private final Runnable closeScreenFunction;
    private final Runnable resetWindowsFunction;
    private final Runnable previousHeroFunction;
    private final Runnable nextHeroFunction;
    private final Runnable dismissHeroFunction;
    private final Runnable sortInventoryFunction;
    private final Runnable toggleCompareFunction;
    private final Runnable showHelpFunction;
    private final Runnable cheatAddGoldFunction;
    private final Runnable cheatRemoveGoldFunction;

    InventoryScreenListener(Runnable closeScreenFunction,
                            Runnable resetWindowsFunction,
                            Runnable previousHeroFunction,
                            Runnable nextHeroFunction,
                            Runnable dismissHeroFunction,
                            Runnable sortInventoryFunction,
                            Runnable toggleCompareFunction,
                            Runnable showHelpFunction,
                            Runnable cheatAddGoldFunction,
                            Runnable cheatRemoveGoldFunction) {
        this.closeScreenFunction = closeScreenFunction;
        this.resetWindowsFunction = resetWindowsFunction;
        this.previousHeroFunction = previousHeroFunction;
        this.nextHeroFunction = nextHeroFunction;
        this.dismissHeroFunction = dismissHeroFunction;
        this.sortInventoryFunction = sortInventoryFunction;
        this.toggleCompareFunction = toggleCompareFunction;
        this.showHelpFunction = showHelpFunction;
        this.cheatAddGoldFunction = cheatAddGoldFunction;
        this.cheatRemoveGoldFunction = cheatRemoveGoldFunction;
    }

    @Override
    public boolean keyDown(InputEvent event, int keycode) {
        switch (keycode) {
            case Constant.KEYCODE_TOP,
                    Constant.KEYCODE_RIGHT,
                    Input.Keys.I,
                    Input.Keys.ESCAPE -> closeScreenFunction.run();
            case Input.Keys.R -> resetWindowsFunction.run();
            case Constant.KEYCODE_L1,
                    Input.Keys.Q -> previousHeroFunction.run();
            case Constant.KEYCODE_R1,
                    Input.Keys.W -> nextHeroFunction.run();
            case Input.Keys.D -> dismissHeroFunction.run();
            case Input.Keys.S -> sortInventoryFunction.run();
            case Input.Keys.C -> toggleCompareFunction.run();
            case Input.Keys.SHIFT_LEFT,
                    Input.Keys.SHIFT_RIGHT -> InventoryUtils.setShiftPressed(true);
            case Input.Keys.CONTROL_LEFT,
                    Input.Keys.CONTROL_RIGHT -> InventoryUtils.setCtrlPressed(true);
            case Input.Keys.H -> showHelpFunction.run();
            case Input.Keys.NUMPAD_ADD -> cheatAddGoldFunction.run();
            case Input.Keys.NUMPAD_SUBTRACT -> cheatRemoveGoldFunction.run();
        }
        return true;
    }

    @Override
    public boolean keyUp(InputEvent event, int keycode) {
        InventoryUtils.setShiftPressed(false);
        InventoryUtils.setCtrlPressed(false);
        return true;
    }

}
