package nl.t64.game.rpg.screens.inventory;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import java.util.function.Consumer;


class HeroesTableSelectListener extends ClickListener {

    private final Consumer<Integer> functionToExecute;
    private final int heroIndex;

    HeroesTableSelectListener(Consumer<Integer> functionToExecute, int heroIndex) {
        this.functionToExecute = functionToExecute;
        this.heroIndex = heroIndex;
    }

    @Override
    public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
        functionToExecute.accept(heroIndex);
        return true;
    }

}
