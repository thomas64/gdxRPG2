package nl.t64.game.rpg.screens.shop;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import nl.t64.game.rpg.Utils;
import nl.t64.game.rpg.screens.ScreenUI;
import nl.t64.game.rpg.screens.inventory.HeroesTable;
import nl.t64.game.rpg.screens.inventory.WindowSelector;
import nl.t64.game.rpg.screens.inventory.equipslot.EquipSlotsTables;
import nl.t64.game.rpg.screens.inventory.inventoryslot.InventorySlotsTable;
import nl.t64.game.rpg.screens.inventory.tooltip.ItemSlotTooltip;
import nl.t64.game.rpg.screens.inventory.tooltip.ShopSlotTooltipBuy;
import nl.t64.game.rpg.screens.inventory.tooltip.ShopSlotTooltipSell;

import java.util.List;


class ShopUI extends ScreenUI {

    private static final float EQUIP_WINDOW_POSITION_X = 1566f;
    private static final float EQUIP_WINDOW_POSITION_Y = 50f;
    private static final float INVENTORY_WINDOW_POSITION_X = 1145f;
    private static final float INVENTORY_WINDOW_POSITION_Y = 50f;
    private static final float SHOP_WINDOW_POSITION_X = 532;
    private static final float SHOP_WINDOW_POSITION_Y = 50f;
    private static final float MERCHANT_WINDOW_POSITION_X = 63f;
    private static final float MERCHANT_WINDOW_POSITION_Y = 50f;
    private static final float HEROES_WINDOW_POSITION_X = 63f;
    private static final float HEROES_WINDOW_POSITION_Y = 834f;

    private static final String TITLE_PERSONAL = "   Equipment";
    private static final String TITLE_GLOBAL = "   Inventory";
    private static final String TITLE_SHOP = "   Shop";
    private static final String TITLE_MERCHANT = "   Merchant";
    private static final String TITLE_HEROES = "   Heroes";

    private final Window inventoryWindow;
    private final Window shopWindow;
    private final Window merchantWindow;
    private final Window heroesWindow;
    private final ShopSlotsTable shopSlotsTable;
    private final ItemSlotTooltip shopSlotTooltipSell;
    private final ItemSlotTooltip shopSlotTooltipBuy;

    private ShopUI(Stage stage, ItemSlotTooltip shopSlotTooltipSell, ItemSlotTooltip shopSlotTooltipBuy,
                   Window equipWindow, Window inventoryWindow, Window shopWindow, Window merchantWindow,
                   Window heroesWindow,
                   EquipSlotsTables equipSlotsTables, InventorySlotsTable inventorySlotsTable,
                   ShopSlotsTable shopSlotsTable, HeroesTable heroesTable,
                   List<WindowSelector> tableList, int selectedTableIndex) {
        super(equipWindow, equipSlotsTables, inventorySlotsTable, heroesTable, tableList, selectedTableIndex);
        this.inventoryWindow = inventoryWindow;
        this.shopWindow = shopWindow;
        this.merchantWindow = merchantWindow;
        this.heroesWindow = heroesWindow;
        this.shopSlotsTable = shopSlotsTable;
        this.shopSlotTooltipSell = shopSlotTooltipSell;
        this.shopSlotTooltipBuy = shopSlotTooltipBuy;

        this.setWindowPositions();
        this.addToStage(stage);
        this.setFocusOnSelectedTable();
        this.getSelectedTable().selectCurrentSlot();
    }

    static ShopUI create(Stage stage, String npcId, String shopId) {

        var shopSlotTooltipSell = new ShopSlotTooltipSell();
        var shopSlotTooltipBuy = new ShopSlotTooltipBuy();

        var equipSlotsTables = new EquipSlotsTables(shopSlotTooltipSell);
        var equipWindow = Utils.createDefaultWindow(TITLE_PERSONAL, equipSlotsTables.getCurrentEquipTable());

        var inventorySlotsTable = new InventorySlotsTable(shopSlotTooltipSell);
        var inventoryWindow = Utils.createDefaultWindow(TITLE_GLOBAL, inventorySlotsTable.container);

        var shopSlotsTable = new ShopSlotsTable(shopId, shopSlotTooltipBuy);
        var shopWindow = Utils.createDefaultWindow(TITLE_SHOP, shopSlotsTable.container);

        var merchantTable = new MerchantTable(npcId);
        var merchantWindow = Utils.createDefaultWindow(TITLE_MERCHANT, merchantTable.table);

        var heroesTable = new HeroesTable();
        var heroesWindow = Utils.createDefaultWindow(TITLE_HEROES, heroesTable.heroes);

        List<WindowSelector> tableList = List.of(shopSlotsTable, inventorySlotsTable, equipSlotsTables);
        int selectedTableIndex = 0;

        return new ShopUI(stage, shopSlotTooltipSell, shopSlotTooltipBuy,
                          equipWindow, inventoryWindow, shopWindow, merchantWindow, heroesWindow,
                          equipSlotsTables, inventorySlotsTable, shopSlotsTable, heroesTable,
                          tableList, selectedTableIndex);
    }

    @Override
    public ShopSlotsTable getShopSlotsTable() {
        return shopSlotsTable;
    }

    void takeOne() {
        getSelectedTable().takeOne();
    }

    void takeHalf() {
        getSelectedTable().takeHalf();
    }

    void takeFull() {
        getSelectedTable().takeFull();
    }

    void equip() {
        getSelectedTable().doAction();
    }

    void update() {
        heroesTable.update();

        merchantWindow.pack();
        heroesWindow.pack();
    }

    private void setWindowPositions() {
        equipWindow.setPosition(EQUIP_WINDOW_POSITION_X, EQUIP_WINDOW_POSITION_Y);
        inventoryWindow.setPosition(INVENTORY_WINDOW_POSITION_X, INVENTORY_WINDOW_POSITION_Y);
        shopWindow.setPosition(SHOP_WINDOW_POSITION_X, SHOP_WINDOW_POSITION_Y);
        merchantWindow.setPosition(MERCHANT_WINDOW_POSITION_X, MERCHANT_WINDOW_POSITION_Y);
        heroesWindow.setPosition(HEROES_WINDOW_POSITION_X, HEROES_WINDOW_POSITION_Y);
    }

    private void addToStage(Stage stage) {
        shopSlotTooltipSell.addToStage(stage);
        shopSlotTooltipBuy.addToStage(stage);
        stage.addActor(equipWindow);
        stage.addActor(inventoryWindow);
        stage.addActor(shopWindow);
        stage.addActor(merchantWindow);
        stage.addActor(heroesWindow);
    }

}
