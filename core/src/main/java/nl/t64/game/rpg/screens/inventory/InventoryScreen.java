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

    private static final float INVENTORY_WINDOW_POSITION_X = 1468f;
    private static final float INVENTORY_WINDOW_POSITION_Y = 67f;
    private static final float EQUIP_WINDOW_POSITION_X = 1098f;
    private static final float EQUIP_WINDOW_POSITION_Y = 67f;
    private static final float STATS_WINDOW_POSITION_X = 100f;
    private static final float STATS_WINDOW_POSITION_Y = 67f;
    private static final float HEROES_WINDOW_POSITION_X = 100f;
    private static final float HEROES_WINDOW_POSITION_Y = 827f;
    private static final float BUTTON_SIZE = 32f;
    private static final float BUTTON_SPACE = 10f;
    private static final float RIGHT_SPACE = 26f;
    private static final float TOP_SPACE = 35f;

    private final Stage stage;
    private InventoryUI inventoryUI;

    private Vector2 inventoryWindowPosition;
    private Vector2 equipWindowPosition;
    private Vector2 statsWindowPosition;
    private Vector2 heroesWindowPosition;

    public InventoryScreen() {
        this.stage = new Stage();
    }

    @Override
    public void onNotifyCreate(ProfileManager profileManager) {
        inventoryWindowPosition = new Vector2(INVENTORY_WINDOW_POSITION_X, INVENTORY_WINDOW_POSITION_Y);
        equipWindowPosition = new Vector2(EQUIP_WINDOW_POSITION_X, EQUIP_WINDOW_POSITION_Y);
        statsWindowPosition = new Vector2(STATS_WINDOW_POSITION_X, STATS_WINDOW_POSITION_Y);
        heroesWindowPosition = new Vector2(HEROES_WINDOW_POSITION_X, HEROES_WINDOW_POSITION_Y);
        onNotifySave(profileManager);
    }

    @Override
    public void onNotifySave(ProfileManager profileManager) {
        profileManager.setProperty("inventoryWindowPosition", inventoryWindowPosition);
        profileManager.setProperty("equipWindowPosition", equipWindowPosition);
        profileManager.setProperty("statsWindowPosition", statsWindowPosition);
        profileManager.setProperty("heroesWindowPosition", heroesWindowPosition);
    }

    @Override
    public void onNotifyLoad(ProfileManager profileManager) {
        inventoryWindowPosition = profileManager.getProperty("inventoryWindowPosition", Vector2.class);
        equipWindowPosition = profileManager.getProperty("equipWindowPosition", Vector2.class);
        statsWindowPosition = profileManager.getProperty("statsWindowPosition", Vector2.class);
        heroesWindowPosition = profileManager.getProperty("heroesWindowPosition", Vector2.class);
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
        stage.addListener(new InventoryScreenListener(this::closeScreen));
        createButtonTable();

        inventoryUI = new InventoryUI();
        inventoryUI.inventoryWindow.setPosition(inventoryWindowPosition.x, inventoryWindowPosition.y);
        inventoryUI.equipWindow.setPosition(equipWindowPosition.x, equipWindowPosition.y);
        inventoryUI.statsWindow.setPosition(statsWindowPosition.x, statsWindowPosition.y);
        inventoryUI.heroesWindow.setPosition(heroesWindowPosition.x, heroesWindowPosition.y);
        inventoryUI.addToStage(stage);
    }

    @Override
    public void render(float dt) {
        Gdx.gl.glClearColor(0f, 0f, 0f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        inventoryUI.render();
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
        inventoryWindowPosition.x = inventoryUI.inventoryWindow.getX();
        inventoryWindowPosition.y = inventoryUI.inventoryWindow.getY();
        equipWindowPosition.x = inventoryUI.equipWindow.getX();
        equipWindowPosition.y = inventoryUI.equipWindow.getY();
        statsWindowPosition.x = inventoryUI.statsWindow.getX();
        statsWindowPosition.y = inventoryUI.statsWindow.getY();
        heroesWindowPosition.x = inventoryUI.heroesWindow.getX();
        heroesWindowPosition.y = inventoryUI.heroesWindow.getY();
    }

    private void resetWindowsPositions() {
        inventoryUI.inventoryWindow.setPosition(INVENTORY_WINDOW_POSITION_X, INVENTORY_WINDOW_POSITION_Y);
        inventoryUI.equipWindow.setPosition(EQUIP_WINDOW_POSITION_X, EQUIP_WINDOW_POSITION_Y);
        inventoryUI.statsWindow.setPosition(STATS_WINDOW_POSITION_X, STATS_WINDOW_POSITION_Y);
        inventoryUI.heroesWindow.setPosition(HEROES_WINDOW_POSITION_X, HEROES_WINDOW_POSITION_Y);
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
