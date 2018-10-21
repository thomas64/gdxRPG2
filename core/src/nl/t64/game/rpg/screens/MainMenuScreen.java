package nl.t64.game.rpg.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Stage;
import nl.t64.game.rpg.GdxRpg2;
import nl.t64.game.rpg.components.menu.MainMenuGraphicsComponent;
import nl.t64.game.rpg.components.menu.MainMenuInputComponent;
import nl.t64.game.rpg.components.menu.MainMenuPhysicsComponent;
import nl.t64.game.rpg.entities.Menu;
import nl.t64.game.rpg.events.menu.InitMenuEvent;
import nl.t64.game.rpg.events.menu.RefreshInputEvent;


public class MainMenuScreen implements Screen {

    private GdxRpg2 game;
    private Menu mainMenu;
    private Stage stage;

    public MainMenuScreen(GdxRpg2 game) {
        this.game = game;
        this.stage = new Stage();
        this.mainMenu = new Menu(new MainMenuInputComponent(),
                                 new MainMenuPhysicsComponent(),
                                 new MainMenuGraphicsComponent());
        this.mainMenu.send(new InitMenuEvent(this.stage, 0));
    }

    @Override
    public void show() {
        mainMenu.send(new RefreshInputEvent());
    }

    @Override
    public void render(float dt) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        mainMenu.update(game);
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
        // empty
    }

    @Override
    public void dispose() {
        stage.clear();
        stage.dispose();
        mainMenu.dispose();
    }

}
