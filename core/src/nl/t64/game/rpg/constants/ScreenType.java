package nl.t64.game.rpg.constants;

import com.badlogic.gdx.Screen;
import lombok.AllArgsConstructor;
import lombok.Getter;
import nl.t64.game.rpg.screens.inventory.InventoryLoadScreen;
import nl.t64.game.rpg.screens.inventory.InventoryScreen;
import nl.t64.game.rpg.screens.loot.LootLoadScreen;
import nl.t64.game.rpg.screens.loot.LootScreen;
import nl.t64.game.rpg.screens.menu.*;
import nl.t64.game.rpg.screens.shop.ShopLoadScreen;
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
    INVENTORY(InventoryScreen.class),
    INVENTORY_LOAD(InventoryLoadScreen.class),
    SHOP(ShopScreen.class),
    SHOP_LOAD(ShopLoadScreen.class),
    LOOT(LootScreen.class),
    LOOT_LOAD(LootLoadScreen.class);

    @Getter
    private final Class<? extends Screen> screenClass;

}
