package nl.t64.game.rpg.screens.menu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import nl.t64.game.rpg.Utils;
import nl.t64.game.rpg.constants.ScreenType;


public abstract class MenuScreen implements Screen {

    private static final String TITLE_FONT = "fonts/colonna.ttf";
    private static final int TITLE_SIZE = 200;
    private static final String MENU_FONT = "fonts/fff_tusj.ttf";
    private static final int MENU_SIZE = 30;
    private static boolean disposedOnce = false;
    BitmapFont titleFont;
    BitmapFont menuFont;
    Stage stage;
    Image screenshot;

    MenuScreen() {
        this.stage = new Stage();
        createFonts();
    }

    private void createFonts() {
        titleFont = Utils.getResourceManager().getTrueTypeAsset(TITLE_FONT, TITLE_SIZE);
        menuFont = Utils.getResourceManager().getTrueTypeAsset(MENU_FONT, MENU_SIZE);
    }

    void setFromScreen(ScreenType screenType) {
        // empty
    }

    public void setBackground(Image screenshot) {
        this.screenshot = screenshot;
        stage.addActor(screenshot);
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
        setupScreen();
    }

    void setupScreen() {
        // empty
    }

    @Override
    public void hide() {
        stage.clear();
        Gdx.input.setInputProcessor(null);
    }

    @Override
    public synchronized void dispose() {
        if (!disposedOnce) {
            titleFont.dispose();
            menuFont.dispose();
            disposedOnce = true;
        }
        stage.clear();
        stage.dispose();
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