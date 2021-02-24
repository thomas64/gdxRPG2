package nl.t64.game.rpg.screens.shop;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import lombok.Getter;
import nl.t64.game.rpg.Utils;
import nl.t64.game.rpg.audio.AudioCommand;
import nl.t64.game.rpg.audio.AudioEvent;
import nl.t64.game.rpg.components.tooltip.ButtonTooltip;
import nl.t64.game.rpg.components.tooltip.ButtonTooltipListener;
import nl.t64.game.rpg.constants.Constant;
import nl.t64.game.rpg.constants.ScreenType;
import nl.t64.game.rpg.screens.ScreenToLoad;
import nl.t64.game.rpg.screens.inventory.ListenerMouseImageButton;


public class ShopScreen implements ScreenToLoad {

    private static final String BUTTON_CLOSE_UP = "close_up";
    private static final String BUTTON_CLOSE_OVER = "close_over";
    private static final String BUTTON_CLOSE_DOWN = "close_down";

    private static final float EQUIP_WINDOW_POSITION_X = 1566f;
    private static final float EQUIP_WINDOW_POSITION_Y = 50f;
    private static final float INVENTORY_WINDOW_POSITION_X = 1145f;
    private static final float INVENTORY_WINDOW_POSITION_Y = 50f;
    private static final float SHOP_WINDOW_POSITION_X = 724f;
    private static final float SHOP_WINDOW_POSITION_Y = 50f;
    private static final float MERCHANT_WINDOW_POSITION_X = 63f;
    private static final float MERCHANT_WINDOW_POSITION_Y = 50f;
    private static final float HEROES_WINDOW_POSITION_X = 63f;
    private static final float HEROES_WINDOW_POSITION_Y = 834f;
    private static final float BUTTON_SIZE = 32f;
    private static final float BUTTON_SPACE = 5f;
    private static final float RIGHT_SPACE = 25f;
    private static final float TOP_SPACE = 50f;

    private final Stage stage;
    private final ButtonTooltip buttonTooltip;
    @Getter
    private ShopUI shopUI;
    private String npcId;
    private String shopId;

    public ShopScreen() {
        this.stage = new Stage();
        this.buttonTooltip = new ButtonTooltip();
    }

    public static void load(String npcId, String shopId) {
        Utils.getAudioManager().handle(AudioCommand.SE_PLAY_ONCE, AudioEvent.SE_SCROLL);
        var shopScreen = (ShopScreen) Utils.getScreenManager().getScreen(ScreenType.SHOP);
        shopScreen.npcId = npcId;
        shopScreen.shopId = shopId;
        Utils.getScreenManager().openParchmentLoadScreen(ScreenType.SHOP);
    }

    @Override
    public void show() {
        Gdx.input.setCursorCatched(false);
        Gdx.input.setInputProcessor(stage);
        stage.addListener(new ShopScreenListener(this::closeScreen));
        buttonTooltip.addToStage(stage);
        createButtonTable();

        shopUI = new ShopUI(npcId, shopId);
        shopUI.equipWindow.setPosition(EQUIP_WINDOW_POSITION_X, EQUIP_WINDOW_POSITION_Y);
        shopUI.inventoryWindow.setPosition(INVENTORY_WINDOW_POSITION_X, INVENTORY_WINDOW_POSITION_Y);
        shopUI.shopWindow.setPosition(SHOP_WINDOW_POSITION_X, SHOP_WINDOW_POSITION_Y);
        shopUI.merchantWindow.setPosition(MERCHANT_WINDOW_POSITION_X, MERCHANT_WINDOW_POSITION_Y);
        shopUI.heroesWindow.setPosition(HEROES_WINDOW_POSITION_X, HEROES_WINDOW_POSITION_Y);
        shopUI.addToStage(stage);
        shopUI.applyListeners(stage);
    }

    @Override
    public void render(float dt) {
        Gdx.gl.glClearColor(0f, 0f, 0f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act(dt);
        stage.draw();
        shopUI.update();
    }

    @Override
    public void resize(int width, int height) {
        // empty
    }

    @Override
    public void pause() {
        // empty
    }

    @Override
    public void resume() {
        // empty
    }

    @Override
    public void hide() {
        shopUI.unloadAssets();
        stage.clear();
    }

    @Override
    public void dispose() {
        // todo, de buttons bewaren hun mouse-over state op de een of andere vage manier.
        stage.dispose();
    }

    @Override
    public void setBackground(Image screenshot, Image parchment) {
        stage.addActor(screenshot);
        stage.addActor(parchment);
    }

    private void createButtonTable() {
        var buttonTable = new Table();
        addButton(buttonTable, BUTTON_CLOSE_UP, BUTTON_CLOSE_OVER, BUTTON_CLOSE_DOWN, this::closeScreen, "Close screen");
        buttonTable.pack();
        buttonTable.setPosition(Gdx.graphics.getWidth() - buttonTable.getWidth() - RIGHT_SPACE,
                                Gdx.graphics.getHeight() - buttonTable.getHeight() - TOP_SPACE);
        stage.addActor(buttonTable);
    }

    private void addButton(Table buttonTable, String up, String over, String down, Runnable action, String title) {
        ImageButton imageButton = Utils.createImageButton(up, over, down);
        imageButton.addListener(new ListenerMouseImageButton(action));
        imageButton.addListener(new ButtonTooltipListener(buttonTooltip, title));
        buttonTable.add(imageButton).size(BUTTON_SIZE).spaceBottom(BUTTON_SPACE).row();
    }

    private void closeScreen() {
        if (isDialogVisibleThenClose()) {
            return;
        }
        Gdx.input.setCursorCatched(true);
        Gdx.input.setInputProcessor(null);
        Utils.getAudioManager().handle(AudioCommand.SE_PLAY_ONCE, AudioEvent.SE_SCROLL);
        fadeParchment();
    }

    private void fadeParchment() {
        var screenshot = (Image) stage.getActors().get(0);
        var parchment = (Image) stage.getActors().get(1);
        stage.clear();
        setBackground(screenshot, parchment);
        parchment.addAction(Actions.sequence(Actions.fadeOut(Constant.FADE_DURATION),
                                             Actions.run(() -> Utils.getScreenManager().setScreen(ScreenType.WORLD))));
    }

    private boolean isDialogVisibleThenClose() {
        Actor possibleOldDialog = stage.getActors().peek();
        if (possibleOldDialog instanceof Dialog dialog) {
            dialog.hide();
            return true;
        }
        return false;
    }

}
