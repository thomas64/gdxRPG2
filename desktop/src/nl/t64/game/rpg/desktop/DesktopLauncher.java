package nl.t64.game.rpg.desktop;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import nl.t64.game.rpg.GdxRpg2;

public class DesktopLauncher {
    public static void main(String[] arg) {
        LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();

        config.title = GdxRpg2.TITLE;
        config.width = GdxRpg2.WIDTH;
        config.height = GdxRpg2.HEIGHT;
        config.useGL30 = false;
        config.resizable = false;
//        config.foregroundFPS = 20;

        Gdx.app = new LwjglApplication(new GdxRpg2(), config);
        //Gdx.app.setLogLevel(Application.LOG_INFO);
        Gdx.app.setLogLevel(Application.LOG_DEBUG);
        //Gdx.app.setLogLevel(Application.LOG_ERROR);
        //Gdx.app.setLogLevel(Application.LOG_NONE);


    }
}
