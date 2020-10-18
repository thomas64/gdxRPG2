package nl.t64.game.rpg.screens.inventory;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import nl.t64.game.rpg.Utils;
import nl.t64.game.rpg.audio.AudioCommand;
import nl.t64.game.rpg.audio.AudioEvent;
import nl.t64.game.rpg.components.loot.Loot;
import nl.t64.game.rpg.components.party.HeroItem;
import nl.t64.game.rpg.components.party.InventoryContainer;
import nl.t64.game.rpg.components.party.InventoryDatabase;
import nl.t64.game.rpg.components.party.PartyContainer;
import nl.t64.game.rpg.constants.ScreenType;
import nl.t64.game.rpg.profile.ProfileManager;
import nl.t64.game.rpg.profile.ProfileObserver;
import nl.t64.game.rpg.screens.ScreenToLoad;
import nl.t64.game.rpg.screens.inventory.tooltip.ButtonToolTip;
import nl.t64.game.rpg.screens.inventory.tooltip.ButtonTooltipListener;
import nl.t64.game.rpg.screens.world.conversation.ConversationDialog;
import nl.t64.game.rpg.screens.world.conversation.ConversationObserver;


public class InventoryScreen extends PartySubject implements ScreenToLoad, ProfileObserver, ConversationObserver {

    private static final String BUTTON_CLOSE_UP = "close_up";
    private static final String BUTTON_CLOSE_OVER = "close_over";
    private static final String BUTTON_CLOSE_DOWN = "close_down";
    private static final String BUTTON_RESET_UP = "reset_up";
    private static final String BUTTON_RESET_OVER = "reset_over";
    private static final String BUTTON_RESET_DOWN = "reset_down";
    private static final String BUTTON_PREVIOUS_UP = "previous_up";
    private static final String BUTTON_PREVIOUS_OVER = "previous_over";
    private static final String BUTTON_PREVIOUS_DOWN = "previous_down";
    private static final String BUTTON_NEXT_UP = "next_up";
    private static final String BUTTON_NEXT_OVER = "next_over";
    private static final String BUTTON_NEXT_DOWN = "next_down";
    private static final String BUTTON_DISMISS_UP = "dismiss_up";
    private static final String BUTTON_DISMISS_OVER = "dismiss_over";
    private static final String BUTTON_DISMISS_DOWN = "dismiss_down";
    private static final String BUTTON_SORT_UP = "sort_up";
    private static final String BUTTON_SORT_OVER = "sort_over";
    private static final String BUTTON_SORT_DOWN = "sort_down";
    private static final String BUTTON_HELP_UP = "help_up";
    private static final String BUTTON_HELP_OVER = "help_over";
    private static final String BUTTON_HELP_DOWN = "help_down";

    private static final float SPELLS_WINDOW_POSITION_X = 1483f;
    private static final float SPELLS_WINDOW_POSITION_Y = 50f;
    private static final float INVENTORY_WINDOW_POSITION_X = 1062f;
    private static final float INVENTORY_WINDOW_POSITION_Y = 50f;
    private static final float EQUIP_WINDOW_POSITION_X = 736f;
    private static final float EQUIP_WINDOW_POSITION_Y = 50f;
    private static final float SKILLS_WINDOW_POSITION_X = 395f;
    private static final float SKILLS_WINDOW_POSITION_Y = 50f;
    private static final float STATS_WINDOW_POSITION_X = 63f;
    private static final float STATS_WINDOW_POSITION_Y = 429f;
    private static final float CALCS_WINDOW_POSITION_X = 63f;
    private static final float CALCS_WINDOW_POSITION_Y = 50f;
    private static final float HEROES_WINDOW_POSITION_X = 63f;
    private static final float HEROES_WINDOW_POSITION_Y = 834f;
    private static final float BUTTON_SIZE = 32f;
    private static final float BUTTON_SPACE = 5f;
    private static final float RIGHT_SPACE = 25f;
    private static final float TOP_SPACE = 50f;

    private final Stage stage;
    private final ConversationDialog conversationDialog;
    private final ButtonToolTip buttonToolTip;
    InventoryUI inventoryUI;

    private Vector2 spellsWindowPosition;
    private Vector2 inventoryWindowPosition;
    private Vector2 equipWindowPosition;
    private Vector2 skillsWindowPosition;
    private Vector2 statsWindowPosition;
    private Vector2 calcsWindowPosition;
    private Vector2 heroesWindowPosition;
    private boolean isMessageShown;

    public InventoryScreen() {
        this.stage = new Stage();
        this.conversationDialog = new ConversationDialog();
        this.buttonToolTip = new ButtonToolTip();
        this.conversationDialog.addObserver(this);
    }

    public static void load() {
        Utils.getScreenManager().openParchmentLoadScreen(ScreenType.INVENTORY);
    }

    @Override
    public void onNotifyCreateProfile(ProfileManager profileManager) {
        spellsWindowPosition = new Vector2(SPELLS_WINDOW_POSITION_X, SPELLS_WINDOW_POSITION_Y);
        inventoryWindowPosition = new Vector2(INVENTORY_WINDOW_POSITION_X, INVENTORY_WINDOW_POSITION_Y);
        equipWindowPosition = new Vector2(EQUIP_WINDOW_POSITION_X, EQUIP_WINDOW_POSITION_Y);
        skillsWindowPosition = new Vector2(SKILLS_WINDOW_POSITION_X, SKILLS_WINDOW_POSITION_Y);
        statsWindowPosition = new Vector2(STATS_WINDOW_POSITION_X, STATS_WINDOW_POSITION_Y);
        calcsWindowPosition = new Vector2(CALCS_WINDOW_POSITION_X, CALCS_WINDOW_POSITION_Y);
        heroesWindowPosition = new Vector2(HEROES_WINDOW_POSITION_X, HEROES_WINDOW_POSITION_Y);
        isMessageShown = false;
        onNotifySaveProfile(profileManager);
    }

    @Override
    public void onNotifySaveProfile(ProfileManager profileManager) {
        profileManager.setProperty("spellsWindowPosition", spellsWindowPosition);
        profileManager.setProperty("inventoryWindowPosition", inventoryWindowPosition);
        profileManager.setProperty("equipWindowPosition", equipWindowPosition);
        profileManager.setProperty("skillsWindowPosition", skillsWindowPosition);
        profileManager.setProperty("statsWindowPosition", statsWindowPosition);
        profileManager.setProperty("calcsWindowPosition", calcsWindowPosition);
        profileManager.setProperty("heroesWindowPosition", heroesWindowPosition);
        profileManager.setProperty("isFirstTimeInventoryMessageShown", isMessageShown);
    }

    @Override
    public void onNotifyLoadProfile(ProfileManager profileManager) {
        spellsWindowPosition = profileManager.getProperty("spellsWindowPosition", Vector2.class);
        inventoryWindowPosition = profileManager.getProperty("inventoryWindowPosition", Vector2.class);
        equipWindowPosition = profileManager.getProperty("equipWindowPosition", Vector2.class);
        skillsWindowPosition = profileManager.getProperty("skillsWindowPosition", Vector2.class);
        statsWindowPosition = profileManager.getProperty("statsWindowPosition", Vector2.class);
        calcsWindowPosition = profileManager.getProperty("calcsWindowPosition", Vector2.class);
        heroesWindowPosition = profileManager.getProperty("heroesWindowPosition", Vector2.class);
        isMessageShown = profileManager.getProperty("isFirstTimeInventoryMessageShown", Boolean.class);
    }

    @Override
    public void onNotifyExitConversation() {
        hideConversationDialog();
    }

    @Override
    public void onNotifyShowMessageDialog(String message, AudioEvent event) {
        throw new IllegalCallerException("Impossible to show Message from Inventory.");
    }

    @Override
    public void onNotifyLoadShop() {
        throw new IllegalCallerException("Impossible to load Shop from Inventory.");
    }

    @Override
    public void onNotifyShowRewardDialog(Loot reward) {
        throw new IllegalCallerException("Impossible to show Reward from Inventory.");
    }

    @Override
    public void onNotifyShowReceiveDialog(Loot receive) {
        throw new IllegalCallerException("Impossible to show Receive from Inventory.");
    }

    @Override
    public void onNotifyHeroJoined() {
        throw new IllegalCallerException("Impossible to join Hero from Inventory.");
    }

    @Override
    public void onNotifyHeroDismiss() {
        HeroItem selectedHero = InventoryUtils.getSelectedHero();
        Utils.getGameData().getHeroes().addHero(selectedHero);
        String heroToDismiss = selectedHero.getId();
        InventoryUtils.selectPreviousHero();
        Utils.getGameData().getParty().removeHero(heroToDismiss);
        hideConversationDialog();
        notifyHeroDismissed();
    }

    @Override
    public void show() {
        Utils.getAudioManager().handle(AudioCommand.BGM_PAUSE_ALL, AudioEvent.NONE);
        Utils.getAudioManager().handle(AudioCommand.BGS_PAUSE_ALL, AudioEvent.NONE);
        Utils.getAudioManager().handle(AudioCommand.SE_PLAY_ONCE, AudioEvent.SE_SCROLL);
        Gdx.input.setCursorCatched(false);
        Gdx.input.setInputProcessor(stage);
        stage.addListener(new InventoryScreenListener(this::closeScreen,
                                                      this::resetWindowsPositions,
                                                      this::selectPreviousHero,
                                                      this::selectNextHero,
                                                      this::tryToDismissHero,
                                                      this::sortInventory,
                                                      this::showHelpMessage,
                                                      this::cheatAddGold,
                                                      this::cheatRemoveGold));
        createButtonTable();

        inventoryUI = new InventoryUI();
        inventoryUI.spellsWindow.setPosition(spellsWindowPosition.x, spellsWindowPosition.y);
        inventoryUI.inventoryWindow.setPosition(inventoryWindowPosition.x, inventoryWindowPosition.y);
        inventoryUI.equipWindow.setPosition(equipWindowPosition.x, equipWindowPosition.y);
        inventoryUI.skillsWindow.setPosition(skillsWindowPosition.x, skillsWindowPosition.y);
        inventoryUI.statsWindow.setPosition(statsWindowPosition.x, statsWindowPosition.y);
        inventoryUI.calcsWindow.setPosition(calcsWindowPosition.x, calcsWindowPosition.y);
        inventoryUI.heroesWindow.setPosition(heroesWindowPosition.x, heroesWindowPosition.y);
        inventoryUI.addToStage(stage);
        inventoryUI.applyListeners(stage);
        buttonToolTip.addToStage(stage);
    }

    @Override
    public void render(float dt) {
        if (!isMessageShown) {
            isMessageShown = true;
            showHelpMessage();
        }
        Gdx.gl.glClearColor(0f, 0f, 0f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
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
        Utils.getAudioManager().handle(AudioCommand.SE_PLAY_ONCE, AudioEvent.SE_SCROLL);
        inventoryUI.unloadAssets();
        stage.clear();
        Gdx.input.setInputProcessor(null);
    }

    @Override
    public void dispose() {
        // todo, de buttons bewaren hun mouse-over state op de een of andere vage manier.
        stage.clear();
        stage.dispose();
        conversationDialog.dispose();
    }

    @Override
    public void setBackground(Image screenshot, Image parchment) {
        stage.addActor(screenshot);
        stage.addActor(parchment);
    }

    private void createButtonTable() {
        var closeButton = createImageButton(BUTTON_CLOSE_UP, BUTTON_CLOSE_OVER, BUTTON_CLOSE_DOWN);
        var resetButton = createImageButton(BUTTON_RESET_UP, BUTTON_RESET_OVER, BUTTON_RESET_DOWN);
        var previousButton = createImageButton(BUTTON_PREVIOUS_UP, BUTTON_PREVIOUS_OVER, BUTTON_PREVIOUS_DOWN);
        var nextButton = createImageButton(BUTTON_NEXT_UP, BUTTON_NEXT_OVER, BUTTON_NEXT_DOWN);
        var dismissButton = createImageButton(BUTTON_DISMISS_UP, BUTTON_DISMISS_OVER, BUTTON_DISMISS_DOWN);
        var sortButton = createImageButton(BUTTON_SORT_UP, BUTTON_SORT_OVER, BUTTON_SORT_DOWN);
        var helpButton = createImageButton(BUTTON_HELP_UP, BUTTON_HELP_OVER, BUTTON_HELP_DOWN);
        closeButton.addListener(new ListenerMouseImageButton(this::closeScreen));
        closeButton.addListener(new ButtonTooltipListener(buttonToolTip, "Close screen"));
        resetButton.addListener(new ListenerMouseImageButton(this::resetWindowsPositions));
        resetButton.addListener(new ButtonTooltipListener(buttonToolTip, "Reset windows"));
        previousButton.addListener(new ListenerMouseImageButton(this::selectPreviousHero));
        previousButton.addListener(new ButtonTooltipListener(buttonToolTip, "Previous hero"));
        nextButton.addListener(new ListenerMouseImageButton(this::selectNextHero));
        nextButton.addListener(new ButtonTooltipListener(buttonToolTip, "Next hero"));
        dismissButton.addListener(new ListenerMouseImageButton(this::tryToDismissHero));
        dismissButton.addListener(new ButtonTooltipListener(buttonToolTip, "Dismiss hero"));
        sortButton.addListener(new ListenerMouseImageButton(this::sortInventory));
        sortButton.addListener(new ButtonTooltipListener(buttonToolTip, "Sort inventory"));
        helpButton.addListener(new ListenerMouseImageButton(this::showHelpMessage));
        helpButton.addListener(new ButtonTooltipListener(buttonToolTip, "Help dialog"));

        var buttonTable = new Table();
        buttonTable.add(closeButton).size(BUTTON_SIZE).spaceBottom(BUTTON_SPACE).row();
        buttonTable.add(resetButton).size(BUTTON_SIZE).spaceBottom(BUTTON_SPACE).row();
        buttonTable.add(previousButton).size(BUTTON_SIZE).spaceBottom(BUTTON_SPACE).row();
        buttonTable.add(nextButton).size(BUTTON_SIZE).spaceBottom(BUTTON_SPACE).row();
        buttonTable.add(dismissButton).size(BUTTON_SIZE).spaceBottom(BUTTON_SPACE).row();
        buttonTable.add(sortButton).size(BUTTON_SIZE).spaceBottom(BUTTON_SPACE).row();
        buttonTable.add(helpButton).size(BUTTON_SIZE);
        buttonTable.pack();
        buttonTable.setPosition(Gdx.graphics.getWidth() - buttonTable.getWidth() - RIGHT_SPACE,
                                Gdx.graphics.getHeight() - buttonTable.getHeight() - TOP_SPACE);
        stage.addActor(buttonTable);
    }

    private void closeScreen() {
        if (isDialogVisibleThenClose()) {
            return;
        }
        storeWindowPositions();
        Utils.getScreenManager().setScreen(ScreenType.WORLD);
        Gdx.input.setCursorCatched(true);
    }

    private void selectPreviousHero() {
        Utils.getAudioManager().handle(AudioCommand.SE_PLAY_ONCE, AudioEvent.SE_MENU_CURSOR);
        InventoryUtils.selectPreviousHero();
    }

    private void selectNextHero() {
        Utils.getAudioManager().handle(AudioCommand.SE_PLAY_ONCE, AudioEvent.SE_MENU_CURSOR);
        InventoryUtils.selectNextHero();
    }

    private void tryToDismissHero() {
        if (isDialogVisibleThenClose()) {
            return;
        }
        PartyContainer party = Utils.getGameData().getParty();
        HeroItem currentHero = InventoryUtils.getSelectedHero();
        String currentHeroId = currentHero.getId();
        if (party.isPlayer(currentHeroId)) {
            String errorMessage = "You cannot dismiss the party leader.";
            MessageDialog messageDialog = new MessageDialog(errorMessage);
            messageDialog.show(stage, AudioEvent.SE_MENU_ERROR);
        } else {
            conversationDialog.loadConversation("dismiss_" + currentHeroId, currentHeroId);
            conversationDialog.show();
        }
    }

    private void hideConversationDialog() {
        conversationDialog.hideWithFade();
        Gdx.input.setInputProcessor(stage);
    }

    private void sortInventory() {
        Utils.getAudioManager().handle(AudioCommand.SE_PLAY_ONCE, AudioEvent.SE_MENU_CONFIRM);
        Utils.getGameData().getInventory().sort();
        inventoryUI.reloadInventory();
    }

    private void cheatAddGold() {
        if (Utils.getSettings().isInDebugMode()) {
            InventoryContainer inventory = Utils.getGameData().getInventory();
            if (inventory.hasRoomForResource("gold")
                && inventory.hasRoomForResource("herb")
                && inventory.hasRoomForResource("spice")
                && inventory.hasRoomForResource("gemstone")) {
                InventoryDatabase idb = InventoryDatabase.getInstance();
                inventory.autoSetItem(idb.createInventoryItem("gold", 10));
                inventory.autoSetItem(idb.createInventoryItem("herb", 10));
                inventory.autoSetItem(idb.createInventoryItem("spice", 10));
                inventory.autoSetItem(idb.createInventoryItem("gemstone", 10));
                inventoryUI.reloadInventory();
            }
        }
    }

    private void cheatRemoveGold() {
        if (Utils.getSettings().isInDebugMode()) {
            InventoryContainer inventory = Utils.getGameData().getInventory();
            if (inventory.hasEnoughOfItem("gold", 1)) {
                inventory.autoRemoveItem("gold", 1);
                inventoryUI.reloadInventory();
            }
        }
    }

    private void showHelpMessage() {
        if (isDialogVisibleThenClose()) {
            return;
        }
        final String message = """
                Esc = Close screen
                I = Close screen
                R = Reset windows
                Q = Previous hero
                W = Next hero
                D = Dismiss hero
                S = Sort Inventory
                Shift = Drag full stack
                Ctrl = Drag half stack
                H = This dialog""";
        final var messageDialog = new MessageDialog(message);
        messageDialog.setLeftAlignment();
        messageDialog.show(stage, AudioEvent.SE_CONVERSATION_NEXT);
    }

    private void storeWindowPositions() {
        spellsWindowPosition.x = inventoryUI.spellsWindow.getX();
        spellsWindowPosition.y = inventoryUI.spellsWindow.getY();
        inventoryWindowPosition.x = inventoryUI.inventoryWindow.getX();
        inventoryWindowPosition.y = inventoryUI.inventoryWindow.getY();
        equipWindowPosition.x = inventoryUI.equipWindow.getX();
        equipWindowPosition.y = inventoryUI.equipWindow.getY();
        skillsWindowPosition.x = inventoryUI.skillsWindow.getX();
        skillsWindowPosition.y = inventoryUI.skillsWindow.getY();
        statsWindowPosition.x = inventoryUI.statsWindow.getX();
        statsWindowPosition.y = inventoryUI.statsWindow.getY();
        calcsWindowPosition.x = inventoryUI.calcsWindow.getX();
        calcsWindowPosition.y = inventoryUI.calcsWindow.getY();
        heroesWindowPosition.x = inventoryUI.heroesWindow.getX();
        heroesWindowPosition.y = inventoryUI.heroesWindow.getY();
    }

    private void resetWindowsPositions() {
        Utils.getAudioManager().handle(AudioCommand.SE_PLAY_ONCE, AudioEvent.SE_MENU_CONFIRM);
        inventoryUI.spellsWindow.setPosition(SPELLS_WINDOW_POSITION_X, SPELLS_WINDOW_POSITION_Y);
        inventoryUI.inventoryWindow.setPosition(INVENTORY_WINDOW_POSITION_X, INVENTORY_WINDOW_POSITION_Y);
        inventoryUI.equipWindow.setPosition(EQUIP_WINDOW_POSITION_X, EQUIP_WINDOW_POSITION_Y);
        inventoryUI.skillsWindow.setPosition(SKILLS_WINDOW_POSITION_X, SKILLS_WINDOW_POSITION_Y);
        inventoryUI.statsWindow.setPosition(STATS_WINDOW_POSITION_X, STATS_WINDOW_POSITION_Y);
        inventoryUI.calcsWindow.setPosition(CALCS_WINDOW_POSITION_X, CALCS_WINDOW_POSITION_Y);
        inventoryUI.heroesWindow.setPosition(HEROES_WINDOW_POSITION_X, HEROES_WINDOW_POSITION_Y);
    }

    private ImageButton createImageButton(String up, String over, String down) {
        var buttonStyle = new ImageButton.ImageButtonStyle();
        buttonStyle.up = createDrawable(up);
        buttonStyle.down = createDrawable(down);
        buttonStyle.over = createDrawable(over);
        return new ImageButton(buttonStyle);
    }

    private NinePatchDrawable createDrawable(String atlasId) {
        var textureRegion = Utils.getResourceManager().getAtlasTexture(atlasId);
        var ninePatch = new NinePatch(textureRegion, 1, 1, 1, 1);
        return new NinePatchDrawable(ninePatch);
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
