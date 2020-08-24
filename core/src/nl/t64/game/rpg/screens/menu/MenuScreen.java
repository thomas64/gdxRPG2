package nl.t64.game.rpg.screens.menu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.GdxRuntimeException;
import nl.t64.game.rpg.Utils;
import nl.t64.game.rpg.constants.Constant;
import nl.t64.game.rpg.constants.ScreenType;


public abstract class MenuScreen implements Screen {

    static final float PAD_TOP = 120f;
    static final float PAD_RIGHT = 40f;
    private static final String TITLE_LOGO = "sprites/titlelogo.png";
    private static final String MENU_FONT = "fonts/calibril.ttf";
    private static final int MENU_SIZE = 34;
    final Stage stage;
    final BitmapFont menuFont;

    ScreenType startScreen;             // only MenuMain or MenuPause
    Table table;
    Image background;
    Color fontColor;
    private ScreenType fromScreen;      // any possible last screen

    int selectedMenuIndex;

    MenuScreen() {
        this.stage = new Stage();
        this.menuFont = Utils.getResourceManager().getTrueTypeAsset(MENU_FONT, MENU_SIZE);
    }

    abstract void setupScreen();

    public void setBackground(Image background) {
        this.background = background;
        stage.addActor(background);
        if (startScreen.equals(ScreenType.MENU_PAUSE)) {
            Texture logo = Utils.getResourceManager().getTextureAsset(TITLE_LOGO);
            stage.addActor(new Image(logo));
        }
    }

    public void updateMenuIndex(int newIndex) {
        selectedMenuIndex = newIndex;
        setAllTextButtonsToDefault();
        setCurrentTextButtonToSelected();
    }

    void processButton(ScreenType from, ScreenType toScreen) {
        var screenManager = Utils.getScreenManager();
        var clickedScreen = screenManager.getMenuScreen(toScreen);
        clickedScreen.startScreen = startScreen;
        clickedScreen.fromScreen = from;
        clickedScreen.setBackground(background);
        screenManager.setScreen(toScreen);
    }

    void processBackButton() {
        var screenManager = Utils.getScreenManager();
        var backScreen = screenManager.getMenuScreen(fromScreen);
        backScreen.setBackground(background);
        screenManager.setScreen(fromScreen);
    }

    void setAllTextButtonsToDefault() {
        for (Actor actor : table.getChildren()) {
            ((TextButton) actor).getStyle().fontColor = fontColor;
        }
    }

    void setCurrentTextButtonToSelected() {
        ((TextButton) table.getChildren().get(selectedMenuIndex)).getStyle().fontColor = Constant.DARK_RED;
    }

    void setFontColor() {
        if (startScreen.equals(ScreenType.MENU_MAIN)) {
            fontColor = Color.BLACK;
        } else if (startScreen.equals(ScreenType.MENU_PAUSE)) {
            fontColor = Color.WHITE;
        } else {
            throw new IllegalCallerException("startScreen can only be Main or Pause Screen.");
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
        setupScreen();
    }

    @Override
    public void hide() {
        stage.clear();
        Gdx.input.setInputProcessor(null);
    }

    @Override
    public void dispose() {
        try {
            menuFont.dispose();
        } catch (GdxRuntimeException e) {
            // font is already exposed.
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
