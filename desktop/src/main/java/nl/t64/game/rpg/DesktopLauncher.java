package nl.t64.game.rpg;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import nl.t64.game.rpg.constants.Constant;


public class DesktopLauncher {

    public static void main(String[] arg) {
        LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();

        config.title = Constant.TITLE;
        config.width = Constant.SCREEN_WIDTH;
        config.height = Constant.SCREEN_HEIGHT;
        config.useGL30 = false;
        config.resizable = false;
        config.foregroundFPS = 60;
        config.fullscreen = true;

        Gdx.app = new LwjglApplication(new Engine(), config);
        Gdx.app.setLogLevel(Application.LOG_ERROR);
    }

}
