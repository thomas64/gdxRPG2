package nl.t64.game.rpg.screens.inventory;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;


class EquipWindowListener extends ClickListener {

    private final Runnable functionToExecute;

    EquipWindowListener(Runnable functionToExecute) {
        this.functionToExecute = functionToExecute;
    }

    @Override
    public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
        functionToExecute.run();
    }

}
