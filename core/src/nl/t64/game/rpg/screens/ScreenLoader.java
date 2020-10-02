package nl.t64.game.rpg.screens;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.ScreenUtils;
import lombok.AllArgsConstructor;
import nl.t64.game.rpg.Utils;
import nl.t64.game.rpg.components.loot.Loot;
import nl.t64.game.rpg.constants.ScreenType;
import nl.t64.game.rpg.screens.loot.FindScreen;
import nl.t64.game.rpg.screens.loot.ReceiveScreen;
import nl.t64.game.rpg.screens.loot.RewardScreen;
import nl.t64.game.rpg.screens.shop.ShopScreen;


@AllArgsConstructor
public class ScreenLoader {

    private final Runnable doBeforeLoadScreen;

    public void openMenuPause() {
        doBeforeLoadScreen.run();
        var menuPause = Utils.getScreenManager().getMenuScreen(ScreenType.MENU_PAUSE);
        menuPause.setBackground(createScreenshot(true));
        Utils.getScreenManager().setScreen(ScreenType.MENU_PAUSE);
        menuPause.updateMenuIndex(0);
    }

    public void openInventoryScreen() {
        openLoadScreen(ScreenType.INVENTORY);
    }

    public void openQuestLogScreen() {
        openLoadScreen(ScreenType.QUEST_LOG);
    }

    public void openFindDialog(Loot loot) {
        var findScreen = (FindScreen) Utils.getScreenManager().getScreen(ScreenType.FIND);
        findScreen.setLoot(loot);
        findScreen.setLootTitle("   Found");
        openLoadScreen(ScreenType.FIND);
    }

    public void openRewardDialog(Loot reward) {
        var rewardScreen = (RewardScreen) Utils.getScreenManager().getScreen(ScreenType.REWARD);
        rewardScreen.setLoot(reward);
        rewardScreen.setLootTitle("   Reward");
        openLoadScreen(ScreenType.REWARD);
    }

    public void openReceiveDialog(Loot receive) {
        var receiveScreen = (ReceiveScreen) Utils.getScreenManager().getScreen(ScreenType.RECEIVE);
        receiveScreen.setLoot(receive);
        receiveScreen.setLootTitle("   Receive");
        openLoadScreen(ScreenType.RECEIVE);
    }

    public void openShopScreen(String npcId, String shopId) {
        var shopScreen = (ShopScreen) Utils.getScreenManager().getScreen(ScreenType.SHOP);
        shopScreen.setNpcId(npcId);
        shopScreen.setShopId(shopId);
        openLoadScreen(ScreenType.SHOP);
    }

    private void openLoadScreen(ScreenType screenType) {
        doBeforeLoadScreen.run();
        var loadScreen = (LoadScreen) Utils.getScreenManager().getScreen(ScreenType.LOAD_SCREEN);
        loadScreen.setScreenType(screenType);
        loadScreen.setBackground(createScreenshot(true));
        Utils.getScreenManager().setScreen(ScreenType.LOAD_SCREEN);
    }

    private Image createScreenshot(boolean withBlur) {
        var screenshot = new Image(ScreenUtils.getFrameBufferTexture());
        if (withBlur) {
            screenshot.setColor(Color.DARK_GRAY);
        }
        return screenshot;
    }

}
