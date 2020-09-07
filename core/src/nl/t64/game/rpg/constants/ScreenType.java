package nl.t64.game.rpg.constants;

import com.badlogic.gdx.Screen;
import lombok.AllArgsConstructor;
import lombok.Getter;
import nl.t64.game.rpg.screens.LoadScreen;
import nl.t64.game.rpg.screens.inventory.InventoryScreen;
import nl.t64.game.rpg.screens.loot.FindScreen;
import nl.t64.game.rpg.screens.loot.ReceiveScreen;
import nl.t64.game.rpg.screens.loot.RewardScreen;
import nl.t64.game.rpg.screens.menu.*;
import nl.t64.game.rpg.screens.shop.ShopScreen;
import nl.t64.game.rpg.screens.world.WorldScreen;


@AllArgsConstructor
public enum ScreenType {

    MENU_MAIN(MenuMain.class),
    MENU_NEW(MenuNew.class),
    MENU_LOAD(MenuLoad.class),
    MENU_SETTINGS(MenuSettings.class),
    MENU_CONTROLS(MenuControls.class),
    MENU_PAUSE(MenuPause.class),
    WORLD(WorldScreen.class),
    LOAD_SCREEN(LoadScreen.class),
    INVENTORY(InventoryScreen.class),
    SHOP(ShopScreen.class),
    FIND(FindScreen.class),
    REWARD(RewardScreen.class),
    RECEIVE(ReceiveScreen.class);

    @Getter
    private final Class<? extends Screen> screenClass;

}
