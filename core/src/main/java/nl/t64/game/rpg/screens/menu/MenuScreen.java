package nl.t64.game.rpg.screens.menu;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import nl.t64.game.rpg.constants.ScreenType;


public abstract class MenuScreen implements Screen {

    void setFromScreen(ScreenType screenType) {
        // empty
    }

    void setBackground(Image screenshot, Image blur) {
        // empty
    }

    @Override
    public void resize(int width, int height) {
        // empty
    }

    @Override
    public void pause() {
        // empty
    }

    @Override
    public void resume() {
        // empty
    }

}
