package nl.t64.game.rpg;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Files;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import nl.t64.game.rpg.constants.Constant;

import java.io.IOException;


public final class DesktopLauncher {

    private DesktopLauncher() {
        throw new IllegalCallerException("DesktopLauncher class");
    }

    public static void main(String[] arg) throws IOException {
        Settings settings = Settings.getSettingsFromFile();

        var config = new LwjglApplicationConfiguration();

        config.title = Constant.TITLE;
        config.width = Constant.SCREEN_WIDTH;
        config.height = Constant.SCREEN_HEIGHT;
        config.addIcon("sprites/icon.png", Files.FileType.Local);
        config.useGL30 = false;
        config.resizable = false;
        config.foregroundFPS = 60;
        config.fullscreen = settings.isFullscreen();

        Gdx.app = new LwjglApplication(new Engine(settings), config);
        Gdx.app.setLogLevel(Application.LOG_ERROR);
        if (config.fullscreen) {
            Gdx.input.setCursorCatched(true);
        }
    }

}
