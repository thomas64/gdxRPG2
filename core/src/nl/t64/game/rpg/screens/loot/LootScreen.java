package nl.t64.game.rpg.screens.loot;

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
import nl.t64.game.rpg.components.loot.Loot;
import nl.t64.game.rpg.constants.ScreenType;
import nl.t64.game.rpg.screens.inventory.ListenerMouseImageButton;

import java.util.Collections;


public class LootScreen extends LootSubject implements Screen {

    private static final String BUTTON_CLOSE_UP = "close_up";
    private static final String BUTTON_CLOSE_OVER = "close_over";
    private static final String BUTTON_CLOSE_DOWN = "close_down";

    private static final float INVENTORY_WINDOW_POSITION_X = (Gdx.graphics.getWidth() / 2f) - 50f;
    private static final float INVENTORY_WINDOW_POSITION_Y = (Gdx.graphics.getHeight() / 2f) - 140f;
    private static final float LOOT_WINDOW_POSITION_X = (Gdx.graphics.getWidth() / 2f) - 330f;
    private static final float LOOT_WINDOW_POSITION_Y = (Gdx.graphics.getHeight() / 2f) - 44f;

    private static final float BUTTON_SIZE = 32f;
    private static final float BUTTON_SPACE = 5f;
    private static final float RIGHT_SPACE = 12f;
    private static final float TOP_SPACE = 49f;

    private final Stage stage;
    @Getter
    private LootUI lootUI;
    @Setter
    private Loot loot;
    @Setter
    private String lootTitle;

    public LootScreen() {
        this.stage = new Stage();
    }

    @Override
    public void show() {
        Gdx.input.setCursorCatched(false);
        Gdx.input.setInputProcessor(stage);
        stage.addListener(new LootScreenListener(this::closeScreen, this::takeItem));
        createButtonTable();

        lootUI = new LootUI(loot, lootTitle);
        lootUI.inventoryWindow.setPosition(INVENTORY_WINDOW_POSITION_X, INVENTORY_WINDOW_POSITION_Y);
        lootUI.lootWindow.setPosition(LOOT_WINDOW_POSITION_X, LOOT_WINDOW_POSITION_Y);
        lootUI.addToStage(stage);
        lootUI.applyListeners(stage);
    }

    @Override
    public void render(float dt) {
        Gdx.gl.glClearColor(0f, 0f, 0f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act(dt);
        stage.draw();
        lootUI.update();
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
        lootUI.unloadAssets();
        stage.clear();
        Gdx.input.setInputProcessor(null);
    }

    @Override
    public void dispose() {
        // todo, de buttons bewaren hun mouse-over state op de een of andere vage manier.
        stage.clear();
        stage.dispose();
    }

    void setBackground(Image screenshot, Image parchment) {
        stage.addActor(screenshot);
        stage.addActor(parchment);
    }

    private void createButtonTable() {
        var closeButton = createImageButton(BUTTON_CLOSE_UP, BUTTON_CLOSE_OVER, BUTTON_CLOSE_DOWN);
        closeButton.addListener(new ListenerMouseImageButton(this::closeScreen));

        var buttonTable = new Table();
        buttonTable.add(closeButton).size(BUTTON_SIZE).spaceBottom(BUTTON_SPACE).row();
        buttonTable.pack();
        buttonTable.setPosition((Gdx.graphics.getWidth() * 0.7f) - buttonTable.getWidth() - RIGHT_SPACE,
                                (Gdx.graphics.getHeight() * 0.7f) - buttonTable.getHeight() - TOP_SPACE);
        stage.addActor(buttonTable);
    }

    private void takeItem() {
        if (!lootUI.takeItem()) {
            closeScreen();
        }
    }

    private void closeScreen() {
        if (lootUI.isEmpty()) {
            loot.setContent(Collections.emptyList());
            notifyLootTaken();
        } else {
            loot.setContent(lootUI.getContent());
        }

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
