package nl.t64.game.rpg.screens.inventory;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import nl.t64.game.rpg.components.party.HeroItem;

import java.util.function.Consumer;


class HeroesTableSelectListener extends ClickListener {

    private final Consumer<HeroItem> functionToExecute;
    private final HeroItem heroItem;

    HeroesTableSelectListener(Consumer<HeroItem> functionToExecute, HeroItem heroItem) {
        this.functionToExecute = functionToExecute;
        this.heroItem = heroItem;
    }

    @Override
    public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
        functionToExecute.accept(heroItem);
        return true;
    }

}
