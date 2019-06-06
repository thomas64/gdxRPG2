package nl.t64.game.rpg.screens.inventory;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import nl.t64.game.rpg.Utils;
import nl.t64.game.rpg.constants.ScreenType;
import nl.t64.game.rpg.profile.ProfileManager;
import nl.t64.game.rpg.profile.ProfileObserver;


public class InventoryScreen implements Screen, ProfileObserver {

    private static final String SPRITE_PARCHMENT = "sprites/parchment.png";
    private static final float INVENTORY_SLOT_POSITION_X = 1530f;
    private static final float INVENTORY_SLOT_POSITION_Y = 75f;
    private static final float PLAYER_SLOT_POSITION_X = 1130f;
    private static final float PLAYER_SLOT_POSITION_Y = 75f;

    private Stage stage;
    private InventoryUI inventoryUI;

    private Vector2 inventorySlotPosition;
    private Vector2 playerSlotPosition;

    public InventoryScreen() {
        this.stage = new Stage();
    }

    @Override
    public void onNotifyCreate(ProfileManager profileManager) {
        inventorySlotPosition = new Vector2(INVENTORY_SLOT_POSITION_X, INVENTORY_SLOT_POSITION_Y);
        playerSlotPosition = new Vector2(PLAYER_SLOT_POSITION_X, PLAYER_SLOT_POSITION_Y);
        onNotifySave(profileManager);
    }

    @Override
    public void onNotifySave(ProfileManager profileManager) {
        profileManager.setProperty("inventorySlotPosition", inventorySlotPosition);
        profileManager.setProperty("playerSlotPosition", playerSlotPosition);
    }

    @Override
    public void onNotifyLoad(ProfileManager profileManager) {
        inventorySlotPosition = profileManager.getProperty("inventorySlotPosition", Vector2.class);
        playerSlotPosition = profileManager.getProperty("playerSlotPosition", Vector2.class);
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
        applyListeners();

        inventoryUI = new InventoryUI();
        inventoryUI.inventorySlotsWindow.setPosition(inventorySlotPosition.x, inventorySlotPosition.y);
        inventoryUI.playerSlotsWindow.setPosition(playerSlotPosition.x, playerSlotPosition.y);
        inventoryUI.addToStage(stage);
    }

    @Override
    public void render(float dt) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act(dt);
        stage.draw();
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
        stage.clear();
        Gdx.input.setInputProcessor(null);
    }

    @Override
    public void dispose() {
        stage.clear();
        stage.dispose();
    }

    private void closeScreen() {
        storeWindowPositions();
        InventoryCloser.setGlobalInventory(inventoryUI.inventorySlotsWindow);
        InventoryCloser.setPersonalInventory(inventoryUI.equipSlotsTable);
        Utils.getScreenManager().setScreen(ScreenType.WORLD);
    }

    public void setBackground(Image screenshot) {
        var texture = Utils.getResourceManager().getTextureAsset(SPRITE_PARCHMENT);
        var parchment = new Image(texture);
        parchment.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        stage.addActor(screenshot);
        stage.addActor(parchment);
    }

    private void applyListeners() {
        stage.addListener(new InventoryScreenListener(this::closeScreen));
    }

    private void storeWindowPositions() {
        inventorySlotPosition.x = inventoryUI.inventorySlotsWindow.getX();
        inventorySlotPosition.y = inventoryUI.inventorySlotsWindow.getY();
        playerSlotPosition.x = inventoryUI.playerSlotsWindow.getX();
        playerSlotPosition.y = inventoryUI.playerSlotsWindow.getY();
    }

}
