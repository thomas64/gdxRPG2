package nl.t64.game.rpg.screens.inventory;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;


class InventoryScreenListener extends InputListener {

    private final Runnable closeScreenFunction;
    private final Runnable resetWindowsFunction;
    private final Runnable previousHeroFunction;
    private final Runnable nextHeroFunction;
    private final Runnable sortInventoryFunction;
    private final Runnable showHelpFunction;

    InventoryScreenListener(Runnable closeScreenFunction,
                            Runnable resetWindowsFunction,
                            Runnable previousHeroFunction,
                            Runnable nextHeroFunction,
                            Runnable sortInventoryFunction,
                            Runnable showHelpFunction) {
        this.closeScreenFunction = closeScreenFunction;
        this.resetWindowsFunction = resetWindowsFunction;
        this.previousHeroFunction = previousHeroFunction;
        this.nextHeroFunction = nextHeroFunction;
        this.sortInventoryFunction = sortInventoryFunction;
        this.showHelpFunction = showHelpFunction;
    }

    @Override
    public boolean keyDown(InputEvent event, int keycode) {
        switch (keycode) {
            case Input.Keys.I,
                    Input.Keys.ESCAPE -> closeScreenFunction.run();
            case Input.Keys.R -> resetWindowsFunction.run();
            case Input.Keys.Q -> previousHeroFunction.run();
            case Input.Keys.W -> nextHeroFunction.run();
            case Input.Keys.S -> sortInventoryFunction.run();
            case Input.Keys.SHIFT_LEFT,
                    Input.Keys.SHIFT_RIGHT -> InventoryUtils.setShiftPressed(true);
            case Input.Keys.H -> showHelpFunction.run();
        }
        return true;
    }

    @Override
    public boolean keyUp(InputEvent event, int keycode) {
        InventoryUtils.setShiftPressed(false);
        return true;
    }

}
