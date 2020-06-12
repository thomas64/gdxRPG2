package nl.t64.game.rpg.screens.shop;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.DragAndDrop;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.badlogic.gdx.utils.Align;
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

    private static final String TITLE_FONT = "fonts/fff_tusj.ttf";
    private static final int TITLE_SIZE = 30;
    private static final String SPRITE_BORDER = "sprites/border.png";

    private static final String TITLE_PERSONAL = "   Equipment";
    private static final String TITLE_GLOBAL = "   Inventory";
    private static final String TITLE_SHOP = "   Shop";
    private static final String TITLE_MERCHANT = "   Merchant";
    private static final String TITLE_HEROES = "   Heroes";
    private static final float TITLE_PADDING = 50f;

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
        fillEquipSlotsTables();
        this.equipWindow = createWindow(TITLE_PERSONAL,
                                        this.equipSlotsTables.get(InventoryUtils.getSelectedHeroId()).equipSlotTable);

        this.inventorySlotsTable = new InventorySlotsTable(this.dragAndDrop, this.shopSlotTooltipSell);
        this.inventoryWindow = createWindow(TITLE_GLOBAL, this.inventorySlotsTable.container);

        this.shopSlotsTable = new ShopSlotsTable(shopId, this.dragAndDrop, this.shopSlotTooltipBuy);
        this.shopWindow = createWindow(TITLE_SHOP, this.shopSlotsTable.container);

        this.merchantTable = new MarchantTable(npcId);
        this.merchantWindow = createWindow(TITLE_MERCHANT, this.merchantTable.table);

        this.heroesTable = new HeroesTable();
        this.heroesWindow = createWindow(TITLE_HEROES, this.heroesTable.heroes);
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

    private static Window.WindowStyle createWindowStyle() {
        var texture = Utils.getResourceManager().getTextureAsset(SPRITE_BORDER);
        var ninepatch = new NinePatch(texture, 1, 1, 1, 1);
        var drawable = new NinePatchDrawable(ninepatch);
        BitmapFont font = Utils.getResourceManager().getTrueTypeAsset(TITLE_FONT, TITLE_SIZE);
        return new Window.WindowStyle(font, Color.BLACK, drawable);
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

    private Window createWindow(String title, Table table) {
        var window = new Window(title, createWindowStyle());
        window.add(table);
        window.padTop(TITLE_PADDING);
        window.getTitleLabel().setAlignment(Align.left);
        window.pack();
        return window;
    }

    private void fillEquipSlotsTables() {
        for (HeroItem hero : Utils.getGameData().getParty().getAllHeroes()) {
            equipSlotsTables.put(hero.getId(), new EquipSlotsTable(hero, dragAndDrop, shopSlotTooltipSell));
        }
    }

}