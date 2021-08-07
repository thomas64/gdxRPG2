package nl.t64.game.rpg;

import com.badlogic.gdx.Files.FileType;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import nl.t64.game.rpg.constants.Constant;


public class DesktopLauncher {

    public static void main(String[] arg) {
        Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
        config.setTitle(Constant.TITLE);
        config.setWindowIcon("sprites/icon.png");
        config.setResizable(false);
        config.setWindowedMode(Constant.SCREEN_WIDTH, Constant.SCREEN_HEIGHT);
        config.setPreferencesConfig("T64.nl/", FileType.External);

        new Lwjgl3Application(new Engine(), config);
    }

}
