package nl.t64.game.rpg;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Graphics;
import com.badlogic.gdx.Preferences;
import lombok.Getter;
import nl.t64.game.rpg.constants.Constant;

import java.util.Arrays;
import java.util.Comparator;
import java.util.NoSuchElementException;


public class PreferenceManager {

    private static final String SETTINGS_FILE = "settings.dat";
    private static final String SETTING_SCREEN = "isFullscreen";
    private static final String SETTING_MUSIC = "isMusicOn";
    private static final String SETTING_SOUND = "isSoundOn";
    private static final String SETTING_DEBUG = "isInDebugMode";
    private static final boolean FULLSCREEN_DEFAULT = true;
    private static final boolean MUSIC_DEFAULT = true;
    private static final boolean SOUND_DEFAULT = true;
    private static final boolean DEBUG_MODE_DEFAULT = false;

    private final Preferences preferences;
    @Getter
    private boolean isFullscreen;
    @Getter
    private boolean isMusicOn;
    @Getter
    private boolean isSoundOn;
    @Getter
    private boolean isInDebugMode;

    PreferenceManager() {
        this.preferences = Gdx.app.getPreferences(SETTINGS_FILE);
        this.isFullscreen = this.preferences.getBoolean(SETTING_SCREEN, FULLSCREEN_DEFAULT);
        this.isMusicOn = this.preferences.getBoolean(SETTING_MUSIC, MUSIC_DEFAULT);
        this.isSoundOn = this.preferences.getBoolean(SETTING_SOUND, SOUND_DEFAULT);
        this.isInDebugMode = DEBUG_MODE_DEFAULT;
    }

    public void toggleFullscreen() {
        if (Gdx.graphics.isFullscreen()) {
            setWindowedMode();
        } else {
            setFullscreenMode();
        }
        isFullscreen = Gdx.graphics.isFullscreen();
        preferences.putBoolean(SETTING_SCREEN, isFullscreen);
        preferences.flush();
    }

    public void toggleMusic(boolean mustPlayBgmImmediately) {
        isMusicOn ^= true;
        Utils.getAudioManager().toggleMusic(isMusicOn, mustPlayBgmImmediately);
        preferences.putBoolean(SETTING_MUSIC, isMusicOn);
        preferences.flush();
    }

    public void toggleSound() {
        isSoundOn ^= true;
        Utils.getAudioManager().toggleSound(isSoundOn);
        preferences.putBoolean(SETTING_SOUND, isSoundOn);
        preferences.flush();
    }

    public void toggleDebugMode() {
        isInDebugMode ^= true;
    }

    private void setWindowedMode() {
        Gdx.graphics.setWindowedMode(Constant.SCREEN_WIDTH, Constant.SCREEN_HEIGHT);
    }

    private void setFullscreenMode() {
        Graphics.DisplayMode displayMode = Arrays.stream(Gdx.graphics.getDisplayModes())
                                                 .filter(this::hasGivenResolution)
                                                 .max(Comparator.comparing(this::getRefreshRate))
                                                 .orElseThrow(NoSuchElementException::new);
        Gdx.graphics.setFullscreenMode(displayMode);
    }

    private boolean hasGivenResolution(Graphics.DisplayMode mode) {
        return mode.width == Constant.SCREEN_WIDTH
               && mode.height == Constant.SCREEN_HEIGHT;
    }

    private int getRefreshRate(Graphics.DisplayMode mode) {
        return mode.refreshRate;
    }

}
