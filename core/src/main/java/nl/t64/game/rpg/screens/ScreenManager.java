package nl.t64.game.rpg.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.badlogic.gdx.utils.reflect.ClassReflection;
import com.badlogic.gdx.utils.reflect.ReflectionException;
import nl.t64.game.rpg.Engine;
import nl.t64.game.rpg.constants.ScreenType;
import nl.t64.game.rpg.screens.inventory.InventoryScreen;
import nl.t64.game.rpg.screens.menu.MenuScreen;
import nl.t64.game.rpg.screens.world.WorldScreen;

import java.util.EnumMap;


public class ScreenManager {

    private final EnumMap<ScreenType, Screen> screenCache;

    public ScreenManager() {
        this.screenCache = new EnumMap<>(ScreenType.class);
    }

    public MenuScreen getMenuScreen(ScreenType screenType) {
        return (MenuScreen) getScreen(screenType);
    }

    public WorldScreen getWorldScreen() {
        return (WorldScreen) getScreen(ScreenType.WORLD);
    }

    public InventoryScreen getInventoryScreen() {
        return (InventoryScreen) getScreen(ScreenType.INVENTORY);
    }

    public Screen getScreen(ScreenType screenType) {
        ifNotInScreenCacheAdd(screenType);
        return screenCache.get(screenType);
    }

    public void setScreen(ScreenType screenType) {
        ifNotInScreenCacheAdd(screenType);
        setScreen(screenCache.get(screenType));
    }

    private void setScreen(Screen screen) {
        ((Engine) Gdx.app.getApplicationListener()).setScreen(screen);
    }

    private void ifNotInScreenCacheAdd(ScreenType screenType) {
        if (!screenCache.containsKey(screenType)) {
            try {
                Screen newScreen = (Screen) ClassReflection.getConstructor(screenType.getScreenClass())
                                                           .newInstance();
                screenCache.put(screenType, newScreen);
            } catch (ReflectionException e) {
                throw new GdxRuntimeException("Screen " + screenType + " could not be created.", e);
            }
        }
    }

    public void dispose() {
        for (Screen screen : screenCache.values()) {
            screen.dispose();
        }
        screenCache.clear();
    }

}
