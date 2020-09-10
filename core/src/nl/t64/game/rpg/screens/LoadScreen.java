package nl.t64.game.rpg.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import lombok.Setter;
import nl.t64.game.rpg.Utils;
import nl.t64.game.rpg.constants.ScreenType;


public class LoadScreen implements Screen {

    private static final String SPRITE_PARCHMENT = "sprites/parchment.png";

    private final Stage stage;
    @Setter
    private ScreenType screenType;
    private int timer = 0;

    public LoadScreen() {
        this.stage = new Stage();
    }

    @Override
    public void show() {
        Gdx.input.setCursorCatched(false);
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void render(float dt) {
        Gdx.gl.glClearColor(0f, 0f, 0f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act(dt);
        stage.draw();
        loadScreen();
    }

    private void loadScreen() {
        timer++;
        if (timer > 1) {
            setScreen();
        }
    }

    private void setScreen() {
        var screenToLoad = (ScreenToLoad) Utils.getScreenManager().getScreen(screenType);
        screenToLoad.setBackground((Image) stage.getActors().get(0),
                                   (Image) stage.getActors().get(1));
        Utils.getScreenManager().setScreen(screenType);
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

    @Override
    public void hide() {
        timer = 0;
        stage.clear();
        Gdx.input.setInputProcessor(null);
    }

    @Override
    public void dispose() {
        stage.clear();
        stage.dispose();
    }

    public void setBackground(Image screenshot) {
        var texture = Utils.getResourceManager().getTextureAsset(SPRITE_PARCHMENT);
        var parchment = new Image(texture);
        if (hasSmallParchment()) {
            float width = Gdx.graphics.getWidth() / 2.4f;
            float height = Gdx.graphics.getHeight() / 3f;
            parchment.setSize(width, height);
            parchment.setPosition((Gdx.graphics.getWidth() / 2f) - (width / 2f),
                                  (Gdx.graphics.getHeight() / 2f) - (height / 2f));
        } else {
            parchment.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        }
        stage.addActor(screenshot);
        stage.addActor(parchment);
    }

    private boolean hasSmallParchment() {
        return screenType.equals(ScreenType.FIND)
               || screenType.equals(ScreenType.REWARD)
               || screenType.equals(ScreenType.RECEIVE);
    }

}
