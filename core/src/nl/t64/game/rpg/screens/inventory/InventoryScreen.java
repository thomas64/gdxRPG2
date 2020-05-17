package nl.t64.game.rpg.screens.inventory;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
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
import nl.t64.game.rpg.components.party.HeroItem;
import nl.t64.game.rpg.components.party.InventoryContainer;
import nl.t64.game.rpg.components.party.InventoryDatabase;
import nl.t64.game.rpg.components.party.PartyContainer;
import nl.t64.game.rpg.constants.ConversationCommand;
import nl.t64.game.rpg.constants.ScreenType;
import nl.t64.game.rpg.profile.ProfileManager;
import nl.t64.game.rpg.profile.ProfileObserver;
import nl.t64.game.rpg.screens.world.conversation.ConversationDialog;


public class InventoryScreen extends PartySubject implements Screen, ProfileObserver {

    private static final String SPRITE_PARCHMENT = "sprites/parchment.png";
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
    InventoryUI inventoryUI;

    private Vector2 spellsWindowPosition;
    private Vector2 inventoryWindowPosition;
    private Vector2 equipWindowPosition;
    private Vector2 skillsWindowPosition;
    private Vector2 statsWindowPosition;
    private Vector2 calcsWindowPosition;
    private Vector2 heroesWindowPosition;

    public InventoryScreen() {
        this.stage = new Stage();
        this.conversationDialog = new ConversationDialog(this::handleConversationCommand);
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
    }

    @Override
    public void show() {
        Gdx.input.setCursorCatched(false);
        Gdx.input.setInputProcessor(stage);
        stage.addListener(new InventoryScreenListener(this::closeScreen,
                                                      this::resetWindowsPositions,
                                                      InventoryUtils::selectPreviousHero,
                                                      InventoryUtils::selectNextHero,
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
    }

    @Override
    public void render(float dt) {
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

    void setBackground(Image screenshot) {
        var texture = Utils.getResourceManager().getTextureAsset(SPRITE_PARCHMENT);
        var parchment = new Image(texture);
        parchment.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
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
        resetButton.addListener(new ListenerMouseImageButton(this::resetWindowsPositions));
        previousButton.addListener(new ListenerMouseImageButton(InventoryUtils::selectPreviousHero));
        nextButton.addListener(new ListenerMouseImageButton(InventoryUtils::selectNextHero));
        dismissButton.addListener(new ListenerMouseImageButton(this::tryToDismissHero));
        sortButton.addListener(new ListenerMouseImageButton(this::sortInventory));
        helpButton.addListener(new ListenerMouseImageButton(this::showHelpMessage));

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
        storeWindowPositions();
        Utils.getScreenManager().setScreen(ScreenType.WORLD);
        Gdx.input.setCursorCatched(true);
    }

    private void tryToDismissHero() {
        if (isDialogVisibleThenClose()) {
            return;
        }
        PartyContainer party = Utils.getGameData().getParty();
        HeroItem currentHero = InventoryUtils.getSelectedHero();
        String currentHeroId = currentHero.getId();
        if (party.isPlayer(currentHeroId)) {
            String message = "You cannot dismiss the party leader.";
            MessageDialog messageDialog = new MessageDialog(message);
            messageDialog.show(stage);
        } else {
            conversationDialog.loadConversation("dismiss_" + currentHeroId, currentHeroId);
            conversationDialog.show();
        }
    }

    private void handleConversationCommand(ConversationCommand command) {
        switch (command) {
            case EXIT_CONVERSATION -> hideConversationDialog();
            case HERO_DISMISS -> dismissHero();
            default -> throw new IllegalAccessError("That ConversationCommand cannot be reached here.");
        }
    }

    private void dismissHero() {
        HeroItem selectedHero = InventoryUtils.getSelectedHero();
        Utils.getGameData().getHeroes().addHero(selectedHero);
        String heroToDismiss = selectedHero.getId();
        InventoryUtils.selectPreviousHero();
        Utils.getGameData().getParty().removeHero(heroToDismiss);
        hideConversationDialog();
        notifyHeroDismissed();
    }

    private void hideConversationDialog() {
        conversationDialog.hideWithFade();
        Gdx.input.setInputProcessor(stage);
    }

    private void sortInventory() {
        Utils.getGameData().getInventory().sort();
        inventoryUI.reloadInventory();
    }

    private void cheatAddGold() {
        if (Utils.getSettings().isInDebugMode()) {
            InventoryContainer inventory = Utils.getGameData().getInventory();
            if (inventory.hasRoomForResource("gold")) {
                inventory.autoSetItem(InventoryDatabase.getInstance().createInventoryItem("gold", 10));
                inventoryUI.reloadInventory();
            }
        }
    }

    private void cheatRemoveGold() {
        if (Utils.getSettings().isInDebugMode()) {
            InventoryContainer inventory = Utils.getGameData().getInventory();
            if (inventory.hasEnoughOfResource("gold", 1)) {
                inventory.autoRemoveResource("gold", 1);
                inventoryUI.reloadInventory();
            }
        }
    }

    private void showHelpMessage() {
        if (isDialogVisibleThenClose()) {
            return;
        }
        final String message = "Esc = Close screen" + System.lineSeparator() +
                               "I = Close screen" + System.lineSeparator() +
                               "R = Reset windows" + System.lineSeparator() +
                               "Q = Previous hero" + System.lineSeparator() +
                               "W = Next hero" + System.lineSeparator() +
                               "D = Dismiss hero" + System.lineSeparator() +
                               "S = Sort Inventory" + System.lineSeparator() +
                               "Shift = Drag full stack" + System.lineSeparator() +
                               "Ctrl = Drag half stack" + System.lineSeparator() +
                               "H = This dialog";
        final MessageDialog messageDialog = new MessageDialog(message);
        messageDialog.setLeftAlignment();
        messageDialog.show(stage);
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
