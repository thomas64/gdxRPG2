package nl.t64.game.rpg.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Stage;
import nl.t64.game.rpg.GdxRpg2;
import nl.t64.game.rpg.components.menu.NewGameMenuGraphicsComponent;
import nl.t64.game.rpg.components.menu.NewGameMenuInputComponent;
import nl.t64.game.rpg.components.menu.NewGameMenuPhysicsComponent;
import nl.t64.game.rpg.entities.Menu;
import nl.t64.game.rpg.events.menu.InitMenuEvent;
import nl.t64.game.rpg.events.menu.RefreshInputEvent;
import nl.t64.game.rpg.profile.ProfileManager;


public class NewGameMenuScreen implements Screen {

    private GdxRpg2 game;
    private Menu newGameMenu;
    private Stage stage;

    public NewGameMenuScreen(GdxRpg2 game) {
        this.game = game;
        this.stage = new Stage();
        this.newGameMenu = new Menu(new NewGameMenuInputComponent(),
                                    new NewGameMenuPhysicsComponent(),
                                    new NewGameMenuGraphicsComponent());
        this.newGameMenu.send(new InitMenuEvent(this.stage, 0));
    }

    @Override
    public void show() {
        ProfileManager.getInstance().storeAllProfiles();
        newGameMenu.send(new RefreshInputEvent());
    }

    @Override
    public void render(float dt) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        newGameMenu.update(game);
        stage.act();
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
        newGameMenu.dispose();
    }

}
