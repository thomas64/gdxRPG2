package nl.t64.game.rpg;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.badlogic.gdx.utils.reflect.ClassReflection;
import com.badlogic.gdx.utils.reflect.ReflectionException;
import lombok.Getter;
import nl.t64.game.rpg.constants.ScreenType;
import nl.t64.game.rpg.profile.ProfileManager;
import nl.t64.game.rpg.screens.WorldScreen;

import java.util.EnumMap;


public class Engine extends Game {

    @Getter
    private static float runTime = 0f;

    @Getter
    private Settings settings;
    @Getter
    private ProfileManager profileManager;
    @Getter
    private GameData gameData;
    private EnumMap<ScreenType, Screen> screenCache;

    public Engine(Settings settings) {
        this.settings = settings;
    }

    private static void updateRunTime() {
        runTime += Gdx.graphics.getDeltaTime();
    }

    @Override
    public void create() {
        profileManager = new ProfileManager();
        gameData = new GameData();
        screenCache = new EnumMap<>(ScreenType.class);

        profileManager.addObserver(gameData);
        profileManager.addObserver(getWorldScreen());

        setScreen(ScreenType.MAIN_MENU);
    }

    @Override
    public void render() {
        updateRunTime();
        super.render();
    }

    @Override
    public void dispose() {
        for (Screen screen : screenCache.values()) {
            screen.dispose();
        }
        screenCache.clear();
    }

    public WorldScreen getWorldScreen() {
        return (WorldScreen) getScreen(ScreenType.WORLD);
    }

    public Screen getScreen(ScreenType screenType) {
        ifNotInScreenCacheAdd(screenType);
        return screenCache.get(screenType);
    }

    public void setScreen(ScreenType screenType) {
        ifNotInScreenCacheAdd(screenType);
        super.setScreen(screenCache.get(screenType));
    }

    private void ifNotInScreenCacheAdd(ScreenType screenType) {
        if (!screenCache.containsKey(screenType)) {
            try {
                Screen newScreen = (Screen) ClassReflection.getConstructor(screenType.getScreenClass(), Engine.class)
                                                           .newInstance(this);
                screenCache.put(screenType, newScreen);
            } catch (ReflectionException e) {
                throw new GdxRuntimeException("Screen " + screenType + " could not be created.", e);
            }
        }
    }

}
