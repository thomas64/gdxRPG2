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
    }

    @Override
    public void onNotifyCreate(ProfileManager profileManager) {
        spellsWindowPosition = new Vector2(SPELLS_WINDOW_POSITION_X, SPELLS_WINDOW_POSITION_Y);
        inventoryWindowPosition = new Vector2(INVENTORY_WINDOW_POSITION_X, INVENTORY_WINDOW_POSITION_Y);
        equipWindowPosition = new Vector2(EQUIP_WINDOW_POSITION_X, EQUIP_WINDOW_POSITION_Y);
        skillsWindowPosition = new Vector2(SKILLS_WINDOW_POSITION_X, SKILLS_WINDOW_POSITION_Y);
        statsWindowPosition = new Vector2(STATS_WINDOW_POSITION_X, STATS_WINDOW_POSITION_Y);
        calcsWindowPosition = new Vector2(CALCS_WINDOW_POSITION_X, CALCS_WINDOW_POSITION_Y);
        heroesWindowPosition = new Vector2(HEROES_WINDOW_POSITION_X, HEROES_WINDOW_POSITION_Y);
        onNotifySave(profileManager);
    }

    @Override
    public void onNotifySave(ProfileManager profileManager) {
        profileManager.setProperty("spellsWindowPosition", spellsWindowPosition);
        profileManager.setProperty("inventoryWindowPosition", inventoryWindowPosition);
        profileManager.setProperty("equipWindowPosition", equipWindowPosition);
        profileManager.setProperty("skillsWindowPosition", skillsWindowPosition);
        profileManager.setProperty("statsWindowPosition", statsWindowPosition);
        profileManager.setProperty("calcsWindowPosition", calcsWindowPosition);
        profileManager.setProperty("heroesWindowPosition", heroesWindowPosition);
    }

    @Override
    public void onNotifyLoad(ProfileManager profileManager) {
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
        stage.addListener(new InventoryScreenListener(this::closeScreen));
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
        // todo, de buttons bewaren hun mouse-over state op de een of andere vage manier.
        stage.clear();
        stage.dispose();
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
        Gdx.input.setCursorCatched(true);
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

}
