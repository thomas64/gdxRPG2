package nl.t64.game.rpg.screens.shop;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.utils.ScreenUtils;
import nl.t64.game.rpg.Utils;
import nl.t64.game.rpg.audio.AudioCommand;
import nl.t64.game.rpg.audio.AudioEvent;
import nl.t64.game.rpg.constants.ScreenType;
import nl.t64.game.rpg.screens.ScreenToLoad;
import nl.t64.game.rpg.screens.ScreenUI;
import nl.t64.game.rpg.screens.inventory.InventoryUtils;


public class ShopScreen extends ScreenToLoad {

    private ShopUI shopUI;
    private String npcId;
    private String shopId;

    public static void load(String npcId, String shopId) {
        Utils.getAudioManager().handle(AudioCommand.SE_PLAY_ONCE, AudioEvent.SE_SCROLL);
        var shopScreen = (ShopScreen) Utils.getScreenManager().getScreen(ScreenType.SHOP);
        shopScreen.npcId = npcId;
        shopScreen.shopId = shopId;
        Utils.getScreenManager().openParchmentLoadScreen(ScreenType.SHOP);
    }

    @Override
    public ScreenUI getScreenUI() {
        return shopUI;
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
        Utils.setGamepadInputProcessor(stage);
        stage.addListener(new ShopScreenListener(this::closeScreen,
                                                 this::takeOne,
                                                 this::takeHalf,
                                                 this::takeFull,
                                                 this::equip,
                                                 this::selectPreviousHero,
                                                 this::selectNextHero,
                                                 this::selectPreviousTable,
                                                 this::selectNextTable,
                                                 this::toggleTooltip,
                                                 this::toggleCompare));

        shopUI = ShopUI.create(stage, npcId, shopId);
        new ShopButtonLabels(stage).create();
    }

    @Override
    public void render(float dt) {
        ScreenUtils.clear(Color.BLACK);
        stage.act(dt);
        stage.draw();
        shopUI.update();
    }

    @Override
    public void hide() {
        shopUI.unloadAssets();
        stage.clear();
    }

    @Override
    public void dispose() {
        stage.dispose();
    }

    private void closeScreen() {
        Gdx.input.setInputProcessor(null);
        Utils.setGamepadInputProcessor(null);
        Utils.getAudioManager().handle(AudioCommand.SE_PLAY_ONCE, AudioEvent.SE_SCROLL);
        fadeParchment();
    }

    private void takeOne() {
        shopUI.takeOne();
    }

    private void takeHalf() {
        shopUI.takeHalf();
    }

    private void takeFull() {
        shopUI.takeFull();
    }

    private void equip() {
        shopUI.equip();
    }

    private void selectPreviousHero() {
        Utils.getAudioManager().handle(AudioCommand.SE_PLAY_ONCE, AudioEvent.SE_MENU_CURSOR);
        shopUI.updateSelectedHero(InventoryUtils::selectPreviousHero);
    }

    private void selectNextHero() {
        Utils.getAudioManager().handle(AudioCommand.SE_PLAY_ONCE, AudioEvent.SE_MENU_CURSOR);
        shopUI.updateSelectedHero(InventoryUtils::selectNextHero);
    }

    private void selectPreviousTable() {
        Utils.getAudioManager().handle(AudioCommand.SE_PLAY_ONCE, AudioEvent.SE_MENU_CURSOR);
        shopUI.selectPreviousTable();
    }

    private void selectNextTable() {
        Utils.getAudioManager().handle(AudioCommand.SE_PLAY_ONCE, AudioEvent.SE_MENU_CURSOR);
        shopUI.selectNextTable();
    }

    private void toggleTooltip() {
        Utils.getAudioManager().handle(AudioCommand.SE_PLAY_ONCE, AudioEvent.SE_MENU_CONFIRM);
        shopUI.toggleTooltip();
    }

    private void toggleCompare() {
        Utils.getAudioManager().handle(AudioCommand.SE_PLAY_ONCE, AudioEvent.SE_MENU_CONFIRM);
        shopUI.toggleCompare();
    }

}
