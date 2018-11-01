package nl.t64.game.rpg.listeners;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import nl.t64.game.rpg.screens.WorldScreen;


public class WorldScreenListener implements InputProcessor {

    private final Runnable openMenuFunction;

    public WorldScreenListener(Runnable openMenuFunction) {
        this.openMenuFunction = openMenuFunction;
    }

    @Override
    public boolean keyDown(int keycode) {
        if (keycode == Input.Keys.ESCAPE) {
            openMenuFunction.run();
        }
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
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
