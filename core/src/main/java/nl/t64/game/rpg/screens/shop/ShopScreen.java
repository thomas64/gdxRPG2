package nl.t64.game.rpg.screens.shop;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import lombok.Getter;
import lombok.Setter;
import nl.t64.game.rpg.Utils;
import nl.t64.game.rpg.constants.ScreenType;
import nl.t64.game.rpg.screens.inventory.ListenerMouseImageButton;


public class ShopScreen implements Screen {

    private static final String SPRITE_PARCHMENT = "sprites/parchment.png";
    private static final String BUTTON_CLOSE_UP = "close_up";
    private static final String BUTTON_CLOSE_OVER = "close_over";
    private static final String BUTTON_CLOSE_DOWN = "close_down";

    private static final float EQUIP_WINDOW_POSITION_X = 1566f;
    private static final float EQUIP_WINDOW_POSITION_Y = 50f;
    private static final float INVENTORY_WINDOW_POSITION_X = 1145f;
    private static final float INVENTORY_WINDOW_POSITION_Y = 50f;
    private static final float SHOP_WINDOW_POSITION_X = 724f;
    private static final float SHOP_WINDOW_POSITION_Y = 50f;
    private static final float OWNER_WINDOW_POSITION_X = 63f;
    private static final float OWNER_WINDOW_POSITION_Y = 50f;
    private static final float HEROES_WINDOW_POSITION_X = 63f;
    private static final float HEROES_WINDOW_POSITION_Y = 834f;
    private static final float BUTTON_SIZE = 32f;
    private static final float BUTTON_SPACE = 5f;
    private static final float RIGHT_SPACE = 25f;
    private static final float TOP_SPACE = 50f;

    private final Stage stage;
    @Getter
    private ShopUI shopUI;
    @Setter
    private String npcId;
    @Setter
    private String shopId;

    public ShopScreen() {
        this.stage = new Stage();
    }

    @Override
    public void show() {
        Gdx.input.setCursorCatched(false);
        Gdx.input.setInputProcessor(stage);
        stage.addListener(new ShopScreenListener(this::closeScreen));
        createButtonTable();

        shopUI = new ShopUI(npcId, shopId);
        shopUI.equipWindow.setPosition(EQUIP_WINDOW_POSITION_X, EQUIP_WINDOW_POSITION_Y);
        shopUI.inventoryWindow.setPosition(INVENTORY_WINDOW_POSITION_X, INVENTORY_WINDOW_POSITION_Y);
        shopUI.shopWindow.setPosition(SHOP_WINDOW_POSITION_X, SHOP_WINDOW_POSITION_Y);
        shopUI.ownerWindow.setPosition(OWNER_WINDOW_POSITION_X, OWNER_WINDOW_POSITION_Y);
        shopUI.heroesWindow.setPosition(HEROES_WINDOW_POSITION_X, HEROES_WINDOW_POSITION_Y);
        shopUI.addToStage(stage);
        shopUI.applyListeners(stage);
    }

    @Override
    public void render(float dt) {
        Gdx.gl.glClearColor(0f, 0f, 0f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act(dt);
        stage.draw();
        shopUI.update();
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
        closeButton.addListener(new ListenerMouseImageButton(this::closeScreen));

        var buttonTable = new Table();
        buttonTable.add(closeButton).size(BUTTON_SIZE).spaceBottom(BUTTON_SPACE).row();
        buttonTable.pack();
        buttonTable.setPosition(Gdx.graphics.getWidth() - buttonTable.getWidth() - RIGHT_SPACE,
                                Gdx.graphics.getHeight() - buttonTable.getHeight() - TOP_SPACE);
        stage.addActor(buttonTable);
    }

    private void closeScreen() {
        Utils.getScreenManager().setScreen(ScreenType.WORLD);
        Gdx.input.setCursorCatched(true);
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
