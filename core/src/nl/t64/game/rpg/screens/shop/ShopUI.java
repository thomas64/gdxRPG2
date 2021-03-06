package nl.t64.game.rpg.screens.shop;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import nl.t64.game.rpg.Utils;
import nl.t64.game.rpg.screens.inventory.HeroesTable;
import nl.t64.game.rpg.screens.inventory.ScreenUI;
import nl.t64.game.rpg.screens.inventory.equipslot.EquipSlotsTables;
import nl.t64.game.rpg.screens.inventory.inventoryslot.InventorySlotsTable;
import nl.t64.game.rpg.screens.inventory.tooltip.ItemSlotTooltip;
import nl.t64.game.rpg.screens.inventory.tooltip.ShopSlotTooltipBuy;
import nl.t64.game.rpg.screens.inventory.tooltip.ShopSlotTooltipSell;


class ShopUI implements ScreenUI {

    private static final String TITLE_PERSONAL = "   Equipment";
    private static final String TITLE_GLOBAL = "   Inventory";
    private static final String TITLE_SHOP = "   Shop";
    private static final String TITLE_MERCHANT = "   Merchant";
    private static final String TITLE_HEROES = "   Heroes";

    final Window equipWindow;
    final Window inventoryWindow;
    final Window shopWindow;
    final Window merchantWindow;
    final Window heroesWindow;

    private final EquipSlotsTables equipSlotsTables;
    private final InventorySlotsTable inventorySlotsTable;
    private final ShopSlotsTable shopSlotsTable;
    private final MerchantTable merchantTable;
    private final HeroesTable heroesTable;

    private final ItemSlotTooltip shopSlotTooltipSell;
    private final ItemSlotTooltip shopSlotTooltipBuy;

    ShopUI(String npcId, String shopId) {
        this.shopSlotTooltipSell = new ShopSlotTooltipSell();
        this.shopSlotTooltipBuy = new ShopSlotTooltipBuy();

        this.equipSlotsTables = new EquipSlotsTables(this.shopSlotTooltipSell);
        this.equipWindow = Utils.createDefaultWindow(TITLE_PERSONAL, this.equipSlotsTables.getCurrentEquipTable());

        this.inventorySlotsTable = new InventorySlotsTable(this.shopSlotTooltipSell);
        this.inventoryWindow = Utils.createDefaultWindow(TITLE_GLOBAL, this.inventorySlotsTable.container);

        this.shopSlotsTable = new ShopSlotsTable(shopId, this.shopSlotTooltipBuy);
        this.shopWindow = Utils.createDefaultWindow(TITLE_SHOP, this.shopSlotsTable.container);

        this.merchantTable = new MerchantTable(npcId);
        this.merchantWindow = Utils.createDefaultWindow(TITLE_MERCHANT, this.merchantTable.table);

        this.heroesTable = new HeroesTable();
        this.heroesWindow = Utils.createDefaultWindow(TITLE_HEROES, this.heroesTable.heroes);
    }

    @Override
    public EquipSlotsTables getEquipSlotsTables() {
        return equipSlotsTables;
    }

    @Override
    public InventorySlotsTable getInventorySlotsTable() {
        return inventorySlotsTable;
    }

    void addToStage(Stage stage) {
        stage.addActor(equipWindow);
        stage.addActor(inventoryWindow);
        stage.addActor(shopWindow);
        stage.addActor(merchantWindow);
        stage.addActor(heroesWindow);
        shopSlotTooltipSell.addToStage(stage);
        shopSlotTooltipBuy.addToStage(stage);
    }

    void applyListeners(Stage stage) {
//        shopWindow.addListener(new ListenerMouseScrollPane(stage, shopSlotsTable.scrollPane));
//        inventoryWindow.addListener(new ListenerMouseScrollPane(stage, inventorySlotsTable.scrollPane));
    }

    void update() {
        merchantTable.update();
        heroesTable.update();

        merchantWindow.pack();
        heroesWindow.pack();
    }

    void unloadAssets() {
        heroesTable.disposePixmapTextures();
    }

}
