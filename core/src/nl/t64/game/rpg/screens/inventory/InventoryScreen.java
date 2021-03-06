package nl.t64.game.rpg.screens.inventory;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.ScreenUtils;
import nl.t64.game.rpg.Utils;
import nl.t64.game.rpg.audio.AudioCommand;
import nl.t64.game.rpg.audio.AudioEvent;
import nl.t64.game.rpg.components.party.HeroItem;
import nl.t64.game.rpg.components.party.InventoryContainer;
import nl.t64.game.rpg.components.party.InventoryDatabase;
import nl.t64.game.rpg.components.party.PartyContainer;
import nl.t64.game.rpg.constants.Constant;
import nl.t64.game.rpg.constants.ScreenType;
import nl.t64.game.rpg.screens.ScreenToLoad;
import nl.t64.game.rpg.screens.inventory.messagedialog.MessageDialog;
import nl.t64.game.rpg.screens.world.conversation.ConversationDialog;
import nl.t64.game.rpg.screens.world.conversation.ConversationObserver;


public class InventoryScreen extends PartySubject implements ScreenToLoad, ConversationObserver {

    private final Stage stage;
    private final ConversationDialog conversationDialog;
    InventoryUI inventoryUI;

    public InventoryScreen() {
        this.stage = new Stage();
        this.conversationDialog = new ConversationDialog();
        this.conversationDialog.addObserver(this);
    }

    public static void load() {
        Utils.getAudioManager().handle(AudioCommand.SE_PLAY_ONCE, AudioEvent.SE_SCROLL);
        Utils.getScreenManager().openParchmentLoadScreen(ScreenType.INVENTORY);
    }

    @Override
    public void onNotifyExitConversation() {
        hideConversationDialog();
    }

    @Override
    public void onNotifyHeroDismiss() {
        HeroItem selectedHero = InventoryUtils.getSelectedHero();
        Utils.getGameData().getHeroes().addHero(selectedHero);
        String heroToDismiss = selectedHero.getId();
        selectPreviousHero();
        Utils.getGameData().getParty().removeHero(heroToDismiss);
        hideConversationDialog();
        notifyHeroDismissed();
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
        Utils.setGamepadInputProcessor(stage);
        stage.addListener(new InventoryScreenListener(this::closeScreen,
                                                      this::selectPreviousHero,
                                                      this::selectNextHero,
                                                      this::selectPreviousTable,
                                                      this::selectNextTable,
                                                      this::tryToDismissHero,
                                                      this::sortInventory,
                                                      this::toggleTooltip,
                                                      this::toggleCompare,
                                                      this::cheatAddGold,
                                                      this::cheatRemoveGold));
        inventoryUI = new InventoryUI(stage);
        new ButtonLabels(stage).create();
    }

    @Override
    public void render(float dt) {
        ScreenUtils.clear(Color.BLACK);
        stage.act(dt);
        stage.draw();
        inventoryUI.update();
        conversationDialog.update(dt);
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
        inventoryUI.unloadAssets();
        stage.clear();
    }

    @Override
    public void dispose() {
        stage.dispose();
        conversationDialog.dispose();
    }

    @Override
    public void setBackground(Image screenshot, Image parchment) {
        stage.addActor(screenshot);
        stage.addActor(parchment);
    }

    private void closeScreen() {
        Gdx.input.setInputProcessor(null);
        Utils.setGamepadInputProcessor(null);
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

    private void selectPreviousHero() {
        Utils.getAudioManager().handle(AudioCommand.SE_PLAY_ONCE, AudioEvent.SE_MENU_CURSOR);
        inventoryUI.updateSelectedHero(InventoryUtils::selectPreviousHero);
    }

    private void selectNextHero() {
        Utils.getAudioManager().handle(AudioCommand.SE_PLAY_ONCE, AudioEvent.SE_MENU_CURSOR);
        inventoryUI.updateSelectedHero(InventoryUtils::selectNextHero);
    }

    private void selectPreviousTable() {
        Utils.getAudioManager().handle(AudioCommand.SE_PLAY_ONCE, AudioEvent.SE_MENU_CURSOR);
        inventoryUI.selectPreviousTable();
    }

    private void selectNextTable() {
        Utils.getAudioManager().handle(AudioCommand.SE_PLAY_ONCE, AudioEvent.SE_MENU_CURSOR);
        inventoryUI.selectNextTable();
    }

    private void tryToDismissHero() {
        PartyContainer party = Utils.getGameData().getParty();
        HeroItem currentHero = InventoryUtils.getSelectedHero();
        String currentHeroId = currentHero.getId();
        if (party.isPlayer(currentHeroId)) {
            String errorMessage = "You cannot dismiss the party leader.";
            var messageDialog = new MessageDialog(errorMessage);
            messageDialog.show(stage, AudioEvent.SE_MENU_ERROR);
        } else {
            conversationDialog.loadConversation("dismiss_" + currentHeroId, currentHeroId);
            conversationDialog.show();
        }
    }

    private void hideConversationDialog() {
        conversationDialog.hideWithFade();
        Gdx.input.setInputProcessor(stage);
        Utils.setGamepadInputProcessor(stage);
    }

    private void sortInventory() {
        Utils.getAudioManager().handle(AudioCommand.SE_PLAY_ONCE, AudioEvent.SE_MENU_CONFIRM);
        Utils.getGameData().getInventory().sort();
        inventoryUI.reloadInventory();
    }

    private void toggleTooltip() {
        Utils.getAudioManager().handle(AudioCommand.SE_PLAY_ONCE, AudioEvent.SE_MENU_CONFIRM);
        inventoryUI.toggleTooltip();
    }

    private void toggleCompare() {
        Utils.getAudioManager().handle(AudioCommand.SE_PLAY_ONCE, AudioEvent.SE_MENU_CONFIRM);
        inventoryUI.toggleCompare();
    }

    private void cheatAddGold() {
        if (Utils.getSettings().isInDebugMode()) {
            InventoryContainer inventory = Utils.getGameData().getInventory();
            if (inventory.hasRoomForResource("gold")
                && inventory.hasRoomForResource("herb")
                && inventory.hasRoomForResource("spice")
                && inventory.hasRoomForResource("gemstone")) {
                Utils.getAudioManager().handle(AudioCommand.SE_PLAY_ONCE, AudioEvent.SE_MENU_ERROR);
                InventoryDatabase idb = InventoryDatabase.getInstance();
                inventory.autoSetItem(idb.createInventoryItem("gold", 100));
                inventory.autoSetItem(idb.createInventoryItem("herb", 100));
                inventory.autoSetItem(idb.createInventoryItem("spice", 100));
                inventory.autoSetItem(idb.createInventoryItem("gemstone", 100));
                inventoryUI.reloadInventory();
            }
        }
    }

    private void cheatRemoveGold() {
        if (Utils.getSettings().isInDebugMode()) {
            InventoryContainer inventory = Utils.getGameData().getInventory();
            if (inventory.hasEnoughOfItem("gold", 1)) {
                Utils.getAudioManager().handle(AudioCommand.SE_PLAY_ONCE, AudioEvent.SE_MENU_ERROR);
                inventory.autoRemoveItem("gold", 1);
                inventoryUI.reloadInventory();
            }
        }
    }

}
