package nl.t64.game.rpg;

import android.os.Bundle;
import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.badlogic.gdx.utils.GdxRuntimeException;

import java.io.IOException;


public class AndroidLauncher extends AndroidApplication {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();

        try {
            Settings settings = Settings.getSettingsFromFile();
            initialize(new Engine(settings), config);
        } catch (IOException e) {
            throw new GdxRuntimeException(e);
        }

    }

}
