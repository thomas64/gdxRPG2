package nl.t64.game.rpg;

import com.badlogic.gdx.Game;
import lombok.Getter;
import nl.t64.game.rpg.screens.WorldScreen;
import nl.t64.game.rpg.screens.menu.LoadMenu;
import nl.t64.game.rpg.screens.menu.MainMenu;
import nl.t64.game.rpg.screens.menu.NewMenu;


@Getter
public class GdxRpg2 extends Game {

    private static final GdxRpg2 INSTANCE = new GdxRpg2();

    private MainMenu mainMenuScreen;
    private NewMenu newGameMenuScreen;
    private LoadMenu loadGameMenuScreen;
    private WorldScreen worldScreen;

    private GdxRpg2() {
    }

    public static GdxRpg2 getInstance() {
        return INSTANCE;
    }

    @Override
    public void create() {
        mainMenuScreen = new MainMenu();
        newGameMenuScreen = new NewMenu();
        loadGameMenuScreen = new LoadMenu();
        worldScreen = new WorldScreen();
        setScreen(mainMenuScreen);
    }

    @Override
    public void dispose() {
        mainMenuScreen.dispose();
        newGameMenuScreen.dispose();
        loadGameMenuScreen.dispose();
        worldScreen.dispose();
    }

}
