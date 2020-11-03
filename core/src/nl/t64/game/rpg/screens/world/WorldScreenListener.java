package nl.t64.game.rpg.screens.world;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import nl.t64.game.rpg.screens.inventory.InventoryScreen;
import nl.t64.game.rpg.screens.menu.MenuPause;
import nl.t64.game.rpg.screens.questlog.QuestLogScreen;


class WorldScreenListener implements InputProcessor {

    private final Runnable doBeforeLoadScreen;
    private final Runnable showHidePartyWindowFunction;

    WorldScreenListener(Runnable doBeforeLoadScreen, Runnable showHidePartyWindowFunction) {
        this.doBeforeLoadScreen = doBeforeLoadScreen;
        this.showHidePartyWindowFunction = showHidePartyWindowFunction;
    }

    @Override
    public boolean keyDown(int keycode) {
        switch (keycode) {
            case Input.Keys.ESCAPE:
            case Input.Keys.I:
            case Input.Keys.L:
                doBeforeLoadScreen.run();
        }

        switch (keycode) {
            case Input.Keys.ESCAPE -> MenuPause.load();
            case Input.Keys.I -> InventoryScreen.load();
            case Input.Keys.L -> QuestLogScreen.load();
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
    public boolean scrolled(float amountX, float amountY) {
        return false;
    }

}
