package nl.t64.game.rpg.constants;

import com.badlogic.gdx.Screen;
import lombok.AllArgsConstructor;
import lombok.Getter;
import nl.t64.game.rpg.screens.InventoryScreen;
import nl.t64.game.rpg.screens.menu.*;
import nl.t64.game.rpg.screens.world.WorldScreen;


@AllArgsConstructor
public enum ScreenType {

    MENU_MAIN(MenuMain.class),
    MENU_NEW(MenuNew.class),
    MENU_LOAD(MenuLoad.class),
    MENU_SETTINGS(MenuSettings.class),
    MENU_PAUSE(MenuPause.class),
    WORLD(WorldScreen.class),
    INVENTORY(InventoryScreen.class);

    @Getter
    private final Class<? extends Screen> screenClass;

}
