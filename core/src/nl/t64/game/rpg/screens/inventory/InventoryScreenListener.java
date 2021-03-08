package nl.t64.game.rpg.screens.inventory;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import lombok.AllArgsConstructor;
import nl.t64.game.rpg.constants.Constant;

import java.util.Arrays;


@AllArgsConstructor
class InventoryScreenListener extends InputListener {

    private final Runnable closeScreenFunction;
    private final Runnable actionFunction;
    private final Runnable previousHeroFunction;
    private final Runnable nextHeroFunction;
    private final Runnable previousTableFunction;
    private final Runnable nextTableFunction;
    private final Runnable dismissHeroFunction;
    private final Runnable sortInventoryFunction;
    private final Runnable toggleTooltipFunction;
    private final Runnable toggleCompareFunction;
    private final Runnable cheatAddGoldFunction;
    private final Runnable cheatRemoveGoldFunction;

    @Override
    public boolean keyDown(InputEvent event, int keycode) {
        if (Arrays.stream(event.getStage().getActors().items).anyMatch(Dialog.class::isInstance)) {
            return true;
        }
        switch (keycode) {
            case Constant.KEYCODE_RIGHT,
                    Input.Keys.I,
                    Input.Keys.ESCAPE -> closeScreenFunction.run();
            case Constant.KEYCODE_BOTTOM,
                    Input.Keys.A -> actionFunction.run();
            case Constant.KEYCODE_L1,
                    Input.Keys.Q -> previousHeroFunction.run();
            case Constant.KEYCODE_R1,
                    Input.Keys.W -> nextHeroFunction.run();
            case Constant.KEYCODE_R3_L,
                    Input.Keys.Z -> previousTableFunction.run();
            case Constant.KEYCODE_R3_R,
                    Input.Keys.X -> nextTableFunction.run();
            case Constant.KEYCODE_TOP,
                    Input.Keys.D -> dismissHeroFunction.run();
            case Constant.KEYCODE_START,
                    Input.Keys.SPACE -> sortInventoryFunction.run();
            case Constant.KEYCODE_SELECT,
                    Input.Keys.T -> toggleTooltipFunction.run();
            case Constant.KEYCODE_L3,
                    Input.Keys.C -> toggleCompareFunction.run();
            case Input.Keys.NUMPAD_ADD -> cheatAddGoldFunction.run();
            case Input.Keys.NUMPAD_SUBTRACT -> cheatRemoveGoldFunction.run();
        }
        return true;
    }

}
