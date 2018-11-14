package nl.t64.game.rpg.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import nl.t64.game.rpg.Engine;
import nl.t64.game.rpg.Utility;
import nl.t64.game.rpg.listeners.InventoryScreenListener;


public class InventoryScreen implements Screen {

    private static final String SPRITE_PARCHMENT = "sprites/parchment.png";

    private Engine engine;
    private Stage stage;

    private Image background;

    public InventoryScreen(Engine engine) {
        this.engine = engine;
        this.stage = new Stage();
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
        setBackground();
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
        stage.getViewport().setScreenSize(width, height);
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
        dispose();
    }

    @Override
    public void dispose() {
        stage.clear();
        stage.dispose();
        Gdx.input.setInputProcessor(null);
    }

    private void closeScreen() {
        engine.setScreen(engine.getWorldScreen());
    }

    private void setBackground() {
        Utility.loadTextureAsset(SPRITE_PARCHMENT);
        Texture texture = Utility.getTextureAsset(SPRITE_PARCHMENT);
        Sprite sprite = new Sprite(texture);
        background = new Image(sprite);
        background.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        stage.addActor(background);
    }

    private void applyListeners() {
        stage.addListener(new InventoryScreenListener(this::closeScreen));
    }

}
