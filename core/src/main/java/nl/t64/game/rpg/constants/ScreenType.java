package nl.t64.game.rpg.constants;

import com.badlogic.gdx.Screen;
import lombok.AllArgsConstructor;
import lombok.Getter;
import nl.t64.game.rpg.screens.InventoryScreen;
import nl.t64.game.rpg.screens.WorldScreen;
import nl.t64.game.rpg.screens.menu.*;


@AllArgsConstructor
public enum ScreenType {

    MAIN_MENU(MainMenu.class),
    NEW_MENU(NewMenu.class),
    LOAD_MENU(LoadMenu.class),
    SETTINGS_MENU(SettingsMenu.class),
    PAUSE_MENU(PauseMenu.class),
    WORLD(WorldScreen.class),
    INVENTORY(InventoryScreen.class);

    @Getter
    private final Class<? extends Screen> screenClass;

}
