package nl.t64.game.rpg.listeners;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import nl.t64.game.rpg.constants.GameState;
import nl.t64.game.rpg.screens.WorldScreen;


public class WorldScreenListener implements InputProcessor {

    @Override
    public boolean keyDown(int keycode) {
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        if (keycode == Input.Keys.P) {
            WorldScreen.setGameState(GameState.PAUSED);
        }
        if (keycode == Input.Keys.Q) {
            Gdx.app.exit();
        }

        if (keycode == Input.Keys.F10) {
            WorldScreen.setShowGrid();
        }
        if (keycode == Input.Keys.F11) {
            WorldScreen.setShowObjects();
        }
        if (keycode == Input.Keys.F12) {
            WorldScreen.setShowDebug();
        }
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }

}
