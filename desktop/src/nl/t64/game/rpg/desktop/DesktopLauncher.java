package nl.t64.game.rpg.desktop;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import nl.t64.game.rpg.GdxRpg2;
import nl.t64.game.rpg.constants.Constant;


public class DesktopLauncher {
    public static void main(String[] arg) {
        LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();

        config.title = Constant.TITLE;
        config.width = Constant.SCREEN_WIDTH;
        config.height = Constant.SCREEN_HEIGHT;
        config.useGL30 = false;
        config.resizable = false;
//        config.foregroundFPS = 20;

        Gdx.app = new LwjglApplication(new GdxRpg2(), config);
        //Gdx.app.setLogLevel(Application.LOG_INFO);
        //Gdx.app.setLogLevel(Application.LOG_DEBUG);
        Gdx.app.setLogLevel(Application.LOG_ERROR);
        //Gdx.app.setLogLevel(Application.LOG_NONE);


    }
}
