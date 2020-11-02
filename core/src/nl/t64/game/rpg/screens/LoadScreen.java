package nl.t64.game.rpg.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import nl.t64.game.rpg.Utils;
import nl.t64.game.rpg.constants.Constant;
import nl.t64.game.rpg.constants.ScreenType;


public class LoadScreen implements Screen {

    private final Stage stage;
    ScreenType screenTypeToLoad;
    private boolean isLoaded;

    public LoadScreen() {
        this.stage = new Stage();
        this.isLoaded = false;
    }

    @Override
    public void show() {
        stage.addActor(Utils.createScreenshot(true));
        Image parchment = createParchment();
        parchment.addAction(Actions.alpha(0f));
        stage.addActor(parchment);
        parchment.addAction(Actions.sequence(Actions.fadeIn(Constant.FADE_DURATION),
                                             Actions.run(() -> isLoaded = true)));
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void render(float dt) {
        Gdx.gl.glClearColor(0f, 0f, 0f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act(dt);
        stage.draw();
        if (isLoaded) {
            setScreenThatJustLoaded();
        }
    }

    private void setScreenThatJustLoaded() {
        var screenToLoad = (ScreenToLoad) Utils.getScreenManager().getScreen(screenTypeToLoad);
        screenToLoad.setBackground((Image) stage.getActors().get(0),
                                   (Image) stage.getActors().get(1));
        Utils.getScreenManager().setScreen(screenTypeToLoad);
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
        isLoaded = false;
        stage.clear();
        Gdx.input.setInputProcessor(null);
    }

    @Override
    public void dispose() {
        stage.dispose();
    }

    private Image createParchment() {
        if (hasSmallParchment()) {
            return Utils.createSmallParchment();
        } else {
            return Utils.createLargeParchment();
        }
    }

    private boolean hasSmallParchment() {
        return switch (screenTypeToLoad) {
            case FIND, REWARD, RECEIVE -> true;
            default -> false;
        };
    }

}
