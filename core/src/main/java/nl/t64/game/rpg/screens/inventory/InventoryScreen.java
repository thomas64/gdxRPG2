package nl.t64.game.rpg.screens.inventory;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import nl.t64.game.rpg.Utils;
import nl.t64.game.rpg.constants.ScreenType;


public class InventoryScreen implements Screen {

    private static final String SPRITE_PARCHMENT = "sprites/parchment.png";
    private static final int INVENTORY_UI_X = 64;
    private static final int INVENTORY_UI_Y = 64;

    private Stage stage;
    private ShapeRenderer shapeRenderer;
    private InventoryUI inventoryUI;

    public InventoryScreen() {
        this.stage = new Stage();
        this.shapeRenderer = new ShapeRenderer();
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
        applyListeners();

        inventoryUI = new InventoryUI();
        inventoryUI.setPosition(INVENTORY_UI_X, INVENTORY_UI_Y);  // todo, weg?
        inventoryUI.addToStage(stage);
    }

    @Override
    public void render(float dt) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act(dt);
        stage.draw();
//        renderInventoryUIBorder();
    }

    private void renderInventoryUIBorder() {
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(Color.BLACK);
        shapeRenderer.rect(INVENTORY_UI_X, INVENTORY_UI_Y,
                           inventoryUI.inventorySlotsTable.getWidth() + 1f,
                           inventoryUI.inventorySlotsTable.getHeight() + 1f);
        shapeRenderer.rect(INVENTORY_UI_X - 1f, INVENTORY_UI_Y - 1f,
                           inventoryUI.inventorySlotsTable.getWidth() + 3f,
                           inventoryUI.inventorySlotsTable.getHeight() + 3f);
        shapeRenderer.rect(inventoryUI.playerSlotsTable.getX(),
                           inventoryUI.playerSlotsTable.getY(),
                           inventoryUI.playerSlotsTable.getWidth() + 125f,      // todo, anders.
                           inventoryUI.playerSlotsTable.getHeight() + 125f);
        shapeRenderer.end();
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
        InventoryCloser.setGlobalInventory(inventoryUI.inventorySlotsTable);
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

}
