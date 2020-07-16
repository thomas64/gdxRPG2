package nl.t64.game.rpg.screens.shop;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.DragAndDrop;
import nl.t64.game.rpg.Utils;
import nl.t64.game.rpg.components.party.HeroItem;
import nl.t64.game.rpg.components.party.PartyContainer;
import nl.t64.game.rpg.screens.inventory.*;
import nl.t64.game.rpg.screens.inventory.tooltip.ItemSlotTooltip;
import nl.t64.game.rpg.screens.inventory.tooltip.ShopSlotTooltipBuy;
import nl.t64.game.rpg.screens.inventory.tooltip.ShopSlotTooltipSell;

import java.util.HashMap;
import java.util.Map;


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

    private final Map<String, EquipSlotsTable> equipSlotsTables;
    private final InventorySlotsTable inventorySlotsTable;
    private final ShopSlotsTable shopSlotsTable;
    private final MarchantTable merchantTable;
    private final HeroesTable heroesTable;

    private final DragAndDrop dragAndDrop;

    private final ItemSlotTooltip shopSlotTooltipSell;
    private final ItemSlotTooltip shopSlotTooltipBuy;

    ShopUI(String npcId, String shopId) {
        this.dragAndDrop = new DragAndDrop();
        this.shopSlotTooltipSell = new ShopSlotTooltipSell();
        this.shopSlotTooltipBuy = new ShopSlotTooltipBuy();

        this.equipSlotsTables = new HashMap<>(PartyContainer.MAXIMUM);
        this.fillEquipSlotsTables();
        final EquipSlotsTable equipTableOfSelectedHero = this.equipSlotsTables.get(InventoryUtils.getSelectedHeroId());
        this.equipWindow = Utils.createDefaultWindow(TITLE_PERSONAL, equipTableOfSelectedHero.container);

        this.inventorySlotsTable = new InventorySlotsTable(this.dragAndDrop, this.shopSlotTooltipSell);
        this.inventoryWindow = Utils.createDefaultWindow(TITLE_GLOBAL, this.inventorySlotsTable.container);

        this.shopSlotsTable = new ShopSlotsTable(shopId, this.dragAndDrop, this.shopSlotTooltipBuy);
        this.shopWindow = Utils.createDefaultWindow(TITLE_SHOP, this.shopSlotsTable.container);

        this.merchantTable = new MarchantTable(npcId);
        this.merchantWindow = Utils.createDefaultWindow(TITLE_MERCHANT, this.merchantTable.table);

        this.heroesTable = new HeroesTable();
        this.heroesWindow = Utils.createDefaultWindow(TITLE_HEROES, this.heroesTable.heroes);
    }

    @Override
    public Window getEquipWindow() {
        return equipWindow;
    }

    @Override
    public Map<String, EquipSlotsTable> getEquipSlotsTables() {
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
        shopWindow.addListener(new ListenerMouseScrollPane(stage, shopSlotsTable.scrollPane));
        inventoryWindow.addListener(new ListenerMouseScrollPane(stage, inventorySlotsTable.scrollPane));
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

    private void fillEquipSlotsTables() {
        for (HeroItem hero : Utils.getGameData().getParty().getAllHeroes()) {
            equipSlotsTables.put(hero.getId(), new EquipSlotsTable(hero, dragAndDrop, shopSlotTooltipSell));
        }
    }

}
