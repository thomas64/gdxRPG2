package nl.t64.game.rpg.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import nl.t64.game.rpg.Utils;
import nl.t64.game.rpg.constants.ScreenType;


public class InventoryScreen implements Screen {

    private static final String SPRITE_PARCHMENT = "sprites/parchment.png";

    private Stage stage;

    public InventoryScreen() {
        this.stage = new Stage();
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
        applyListeners();
    }

    @Override
    public void render(float dt) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act(dt);
        stage.draw();
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
        stage.clear();
        Gdx.input.setInputProcessor(null);
    }

    @Override
    public void dispose() {
        stage.clear();
        stage.dispose();
    }

    private void closeScreen() {
        Utils.getScreenManager().setScreen(ScreenType.WORLD);
    }

    public void setBackground(Image screenshot) {
        var texture = Utils.getResourceManager().getTextureAsset(SPRITE_PARCHMENT);
        var sprite = new Sprite(texture);
        var parchment = new Image(sprite);
        parchment.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        stage.addActor(screenshot);
        stage.addActor(parchment);
    }

    private void applyListeners() {
        stage.addListener(new InventoryScreenListener(this::closeScreen));
    }

}
