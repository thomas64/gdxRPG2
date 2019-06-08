package nl.t64.game.rpg.screens.inventory;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.DragAndDrop;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.badlogic.gdx.utils.Align;
import nl.t64.game.rpg.Utils;
import nl.t64.game.rpg.components.party.GlobalContainer;
import nl.t64.game.rpg.components.party.HeroItem;

import java.util.stream.IntStream;


class InventoryUI {

    private static final String TITLE_FONT = "fonts/fff_tusj.ttf";
    private static final int TITLE_SIZE = 30;
    private static final String TEXT_FONT = "fonts/spectral.ttf";
    private static final int TEXT_SIZE = 20;
    private static final String SPRITE_SILHOUETTE = "sprites/silhouette.png";
    private static final String SPRITE_BORDER = "sprites/border.png";

    private static final String TITLE_GLOBAL = "Global Inventory";
    private static final String TITLE_PERSONAL = "Personal Inventory";
    private static final String TITLE_STATS = "Stats";
    private static final float SLOT_SIZE = 64f;
    private static final int SLOTS_IN_ROW = 6;
    private static final float TITLE_PADDING = 50f;

    private DragAndDrop dragAndDrop;
    private InventorySlotTooltip tooltip;
    Window inventoryWindow;
    Window playerWindow;
    Window statsWindow;
    private Window.WindowStyle windowStyle;

    InventoryUI() {
        this.dragAndDrop = new DragAndDrop();
        this.tooltip = new InventorySlotTooltip();
        this.windowStyle = createWindowStyle();

        createInventoryWindow();
        createPlayerWindow();
        createStatsWindow();
    }

    private static Window.WindowStyle createWindowStyle() {
        var texture = Utils.getResourceManager().getTextureAsset(SPRITE_BORDER);
        var ninepatch = new NinePatch(texture, 1, 1, 1, 1);
        var drawable = new NinePatchDrawable(ninepatch);
        BitmapFont font = Utils.getResourceManager().getTrueTypeAsset(TITLE_FONT, TITLE_SIZE);
        return new Window.WindowStyle(font, Color.BLACK, drawable);
    }

    private void createInventoryWindow() {
        inventoryWindow = new Window(TITLE_GLOBAL, windowStyle);
        GlobalContainer container = Utils.getGameData().getInventory();
        IntStream.rangeClosed(0, container.getLastIndex()).forEach(index -> {
            var inventorySlot = new InventorySlot();
            inventorySlot.addListener(new InventorySlotTooltipListener(tooltip));
            dragAndDrop.addTarget(new InventorySlotTarget(inventorySlot));
            dragAndDrop.addSource(new InventorySlotSource(inventorySlot.amountLabel, dragAndDrop));
            container.getItemAt(index).ifPresent(inventoryItem -> {
                inventorySlot.amount = container.getAmountOfItemAt(index);
                inventorySlot.addToStack(new InventoryImage(inventoryItem));
                dragAndDrop.addSource(new InventorySlotSource(inventorySlot.getCertainInventoryImage(), dragAndDrop));
            });
            inventoryWindow.add(inventorySlot).size(SLOT_SIZE, SLOT_SIZE);
            if ((index + 1) % SLOTS_IN_ROW == 0) {
                inventoryWindow.row();
            }
        });

//        inventoryWindow.debugAll();
        inventoryWindow.padTop(TITLE_PADDING);
        inventoryWindow.getTitleLabel().setAlignment(Align.center);
        inventoryWindow.pack();
    }

    private void createPlayerWindow() {
        playerWindow = new Window(TITLE_PERSONAL, windowStyle);
        var texture = Utils.getResourceManager().getTextureAsset(SPRITE_SILHOUETTE);
        var sprite = new Sprite(texture);
        var silhouette = new SpriteDrawable(sprite);
        var equipSlotsTable = new EquipSlotsTable(dragAndDrop, tooltip).equipSlots;
        equipSlotsTable.setBackground(silhouette);
        playerWindow.add(equipSlotsTable);

//        playerWindow.debugAll();
        playerWindow.padTop(TITLE_PADDING);
        playerWindow.getTitleLabel().setAlignment(Align.center);
        playerWindow.pack();
    }

    private void createStatsWindow() {
        HeroItem heroItem = Utils.getGameData().getParty().getHero(0);    // todo, fix index.

        statsWindow = new Window(TITLE_STATS, windowStyle);

        var statsSkin = new Skin();
        BitmapFont font = Utils.getResourceManager().getTrueTypeAsset(TEXT_FONT, TEXT_SIZE);
        statsSkin.add("default", new Label.LabelStyle(font, Color.BLACK));
        var statsTable = new Table(statsSkin);
        statsTable.defaults().height(26f).align(Align.left);
        statsTable.columnDefaults(0).width(200f);
        statsTable.columnDefaults(1).width(75f);
        statsTable.pad(20f);

        statsTable.add("Intelligence");
        statsTable.add("?").row();
        statsTable.add("Willpower");
        statsTable.add("?").row();
        statsTable.add("Dexterity");
        statsTable.add("?").row();
        statsTable.add("Agility");
        statsTable.add("?").row();
        statsTable.add("Endurance");
        statsTable.add(String.valueOf(heroItem.getOwnEndurance())).row();
        statsTable.add("Strength");
        statsTable.add(String.valueOf(heroItem.getOwnStrength())).row();
        statsTable.add("Stamina");
        statsTable.add(String.valueOf(heroItem.getOwnStamina())).row();
        statsTable.add("").row();
        statsTable.add("XP Remaining");
        statsTable.add(String.valueOf(heroItem.getXpRemaining())).row();
        statsTable.add("Total XP");
        statsTable.add(String.valueOf(heroItem.getTotalXp())).row();
        statsTable.add("Next Level");
        statsTable.add(String.valueOf(heroItem.getNeededXpForNextLevel())).row(); // todo, is dit de juiste van de 2 methodes?
        statsTable.add("").row();
        statsTable.add("Weight");
        statsTable.add("?").row();
        statsTable.add("Movepoints");
        statsTable.add("?").row();
        statsTable.add("Base Hit");
        statsTable.add(String.valueOf(heroItem.getWeaponBaseHit()) + "%").row();
        statsTable.add("Damage");
        statsTable.add(String.valueOf(heroItem.getWeaponDamage())).row(); // todo, flexibel maken, en ook hover maken
        statsTable.add("Protection");
        statsTable.add("?").row();
        statsTable.add("Defense");
        statsTable.add(String.valueOf(heroItem.getShieldDefense()));

        statsWindow.add(statsTable);
//        statsWindow.debugAll();
        statsWindow.padTop(TITLE_PADDING);
        statsWindow.getTitleLabel().setAlignment(Align.center);
        statsWindow.pack();
    }

    void addToStage(Stage stage) {
        stage.addActor(inventoryWindow);
        stage.addActor(playerWindow);
        stage.addActor(statsWindow);
        stage.addActor(tooltip.window);
    }

}
