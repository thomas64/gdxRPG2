package nl.t64.game.rpg.screens.shop;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import lombok.AllArgsConstructor;
import nl.t64.game.rpg.constants.Constant;

import java.util.Arrays;


@AllArgsConstructor
class ShopScreenListener extends InputListener {

    private final Runnable closeScreenFunction;
    private final Runnable takeOneFunction;
    private final Runnable takeHalfFunction;
    private final Runnable takeFullFunction;
    private final Runnable equipFunction;
    private final Runnable previousHeroFunction;
    private final Runnable nextHeroFunction;
    private final Runnable previousTableFunction;
    private final Runnable nextTableFunction;
    private final Runnable toggleTooltipFunction;
    private final Runnable toggleCompareFunction;

    @Override
    public boolean keyDown(InputEvent event, int keycode) {
        if (Arrays.stream(event.getStage().getActors().items).anyMatch(Dialog.class::isInstance)) {
            return true;
        }
        switch (keycode) {
            case Constant.KEYCODE_RIGHT,
                    Input.Keys.ESCAPE -> closeScreenFunction.run();
            case Constant.KEYCODE_BOTTOM,
                    Input.Keys.A -> takeOneFunction.run();
            case Constant.KEYCODE_LEFT,
                    Input.Keys.S -> takeHalfFunction.run();
            case Constant.KEYCODE_TOP,
                    Input.Keys.D -> takeFullFunction.run();
            case Constant.KEYCODE_START,
                    Input.Keys.E -> equipFunction.run();
            case Constant.KEYCODE_L1,
                    Input.Keys.Q -> previousHeroFunction.run();
            case Constant.KEYCODE_R1,
                    Input.Keys.W -> nextHeroFunction.run();
            case Constant.KEYCODE_R3_L,
                    Input.Keys.Z -> previousTableFunction.run();
            case Constant.KEYCODE_R3_R,
                    Input.Keys.X -> nextTableFunction.run();
            case Constant.KEYCODE_SELECT,
                    Input.Keys.T -> toggleTooltipFunction.run();
            case Constant.KEYCODE_L3,
                    Input.Keys.C -> toggleCompareFunction.run();
        }
        return true;
    }

}
