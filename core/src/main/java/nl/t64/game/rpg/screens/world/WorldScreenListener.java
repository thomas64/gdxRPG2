package nl.t64.game.rpg.screens.world;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;


class WorldScreenListener implements InputProcessor {

    private final Runnable openMenuFunction;
    private final Runnable openInventoryFunction;
    private final Runnable showHidePartyWindowFunction;

    WorldScreenListener(Runnable openMenuFunction,
                        Runnable openInventoryFunction,
                        Runnable showHidePartyWindowFunction) {
        this.openMenuFunction = openMenuFunction;
        this.openInventoryFunction = openInventoryFunction;
        this.showHidePartyWindowFunction = showHidePartyWindowFunction;
    }

    @Override
    public boolean keyDown(int keycode) {
        switch (keycode) {
            case Input.Keys.ESCAPE -> openMenuFunction.run();
            case Input.Keys.I -> openInventoryFunction.run();
            case Input.Keys.P -> showHidePartyWindowFunction.run();
        }
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        switch (keycode) {
            case Input.Keys.F10 -> WorldScreen.setShowGrid();
            case Input.Keys.F11 -> WorldScreen.setShowObjects();
            case Input.Keys.F12 -> WorldScreen.setShowDebug();
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
