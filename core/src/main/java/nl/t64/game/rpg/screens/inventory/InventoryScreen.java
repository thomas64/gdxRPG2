package nl.t64.game.rpg.screens.inventory;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import nl.t64.game.rpg.Utils;
import nl.t64.game.rpg.constants.ScreenType;
import nl.t64.game.rpg.profile.ProfileManager;
import nl.t64.game.rpg.profile.ProfileObserver;


public class InventoryScreen implements Screen, ProfileObserver {

    private static final String SPRITE_PARCHMENT = "sprites/parchment.png";
    private static final String BUTTON_CLOSE_UP = "close_up";
    private static final String BUTTON_CLOSE_OVER = "close_over";
    private static final String BUTTON_CLOSE_DOWN = "close_down";
    private static final String BUTTON_RESET_UP = "reset_up";
    private static final String BUTTON_RESET_OVER = "reset_over";
    private static final String BUTTON_RESET_DOWN = "reset_down";

    private static final float INVENTORY_SLOT_POSITION_X = 1462f;
    private static final float INVENTORY_SLOT_POSITION_Y = 66f;
    private static final float PLAYER_SLOT_POSITION_X = 1090f;
    private static final float PLAYER_SLOT_POSITION_Y = 66f;
    private static final float BUTTON_SIZE = 32f;
    private static final float BUTTON_SPACE = 10f;
    private static final float RIGHT_SPACE = 26f;
    private static final float TOP_SPACE = 40f;

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
        stage.addListener(new InventoryScreenListener(this::closeScreen));
        createButtonTable();

        inventoryUI = new InventoryUI();
        inventoryUI.inventorySlotsWindow.setPosition(inventorySlotPosition.x, inventorySlotPosition.y);
        inventoryUI.playerSlotsWindow.setPosition(playerSlotPosition.x, playerSlotPosition.y);
        inventoryUI.addToStage(stage);
    }

    @Override
    public void render(float dt) {
        Gdx.gl.glClearColor(0f, 0f, 0f, 1f);
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

    public void setBackground(Image screenshot) {
        var texture = Utils.getResourceManager().getTextureAsset(SPRITE_PARCHMENT);
        var parchment = new Image(texture);
        parchment.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        stage.addActor(screenshot);
        stage.addActor(parchment);
    }

    private void createButtonTable() {
        var closeButton = createImageButton(BUTTON_CLOSE_UP, BUTTON_CLOSE_OVER, BUTTON_CLOSE_DOWN);
        var resetButton = createImageButton(BUTTON_RESET_UP, BUTTON_RESET_OVER, BUTTON_RESET_DOWN);
        closeButton.addListener(new ListenerMouseImageButton(this::closeScreen));
        resetButton.addListener(new ListenerMouseImageButton(this::resetWindowsPositions));

        var buttonTable = new Table();
        buttonTable.add(closeButton).width(BUTTON_SIZE).height(BUTTON_SIZE).spaceBottom(BUTTON_SPACE).row();
        buttonTable.add(resetButton).width(BUTTON_SIZE).height(BUTTON_SIZE);
        buttonTable.pack();
        buttonTable.setPosition(Gdx.graphics.getWidth() - buttonTable.getWidth() - RIGHT_SPACE,
                                Gdx.graphics.getHeight() - buttonTable.getHeight() - TOP_SPACE);
        stage.addActor(buttonTable);
    }

    private void closeScreen() {
        storeWindowPositions();
        Utils.getScreenManager().setScreen(ScreenType.WORLD);
    }

    private void storeWindowPositions() {
        inventorySlotPosition.x = inventoryUI.inventorySlotsWindow.getX();
        inventorySlotPosition.y = inventoryUI.inventorySlotsWindow.getY();
        playerSlotPosition.x = inventoryUI.playerSlotsWindow.getX();
        playerSlotPosition.y = inventoryUI.playerSlotsWindow.getY();
    }

    private void resetWindowsPositions() {
        inventoryUI.inventorySlotsWindow.setPosition(INVENTORY_SLOT_POSITION_X, INVENTORY_SLOT_POSITION_Y);
        inventoryUI.playerSlotsWindow.setPosition(PLAYER_SLOT_POSITION_X, PLAYER_SLOT_POSITION_Y);
    }

    private ImageButton createImageButton(String up, String over, String down) {
        var button = new ImageButton(createDrawable(up), createDrawable(down));
        button.getStyle().imageOver = createDrawable(over);
        return button;
    }

    private NinePatchDrawable createDrawable(String atlasId) {
        var textureRegion = Utils.getResourceManager().getAtlasTexture(atlasId);
        var ninePatch = new NinePatch(textureRegion, 1, 1, 1, 1);
        return new NinePatchDrawable(ninePatch);
    }

}
