package nl.t64.game.rpg.listeners;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import nl.t64.game.rpg.screens.WorldScreen;


public class WorldScreenListener implements InputProcessor {

    private final Runnable openMenuFunction;
    private final Runnable openInventoryFunction;

    public WorldScreenListener(Runnable openMenuFunction, Runnable openInventoryFunction) {
        this.openMenuFunction = openMenuFunction;
        this.openInventoryFunction = openInventoryFunction;
    }

    @Override
    public boolean keyDown(int keycode) {
        switch (keycode) {
            case Input.Keys.ESCAPE:
                openMenuFunction.run();
                break;
            case Input.Keys.I:
                openInventoryFunction.run();
                break;
            default:
                break;
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