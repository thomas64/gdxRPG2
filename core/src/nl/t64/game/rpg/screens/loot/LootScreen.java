package nl.t64.game.rpg.screens.loot;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import lombok.Getter;
import lombok.Setter;
import nl.t64.game.rpg.Utils;
import nl.t64.game.rpg.components.loot.Loot;
import nl.t64.game.rpg.constants.ScreenType;
import nl.t64.game.rpg.profile.ProfileManager;
import nl.t64.game.rpg.profile.ProfileObserver;
import nl.t64.game.rpg.screens.inventory.ListenerMouseImageButton;
import nl.t64.game.rpg.screens.inventory.MessageDialog;
import nl.t64.game.rpg.screens.inventory.tooltip.ButtonToolTip;
import nl.t64.game.rpg.screens.inventory.tooltip.ButtonTooltipListener;


public class LootScreen extends LootSubject implements Screen, ProfileObserver {

    private static final String BUTTON_CLOSE_UP = "close_up";
    private static final String BUTTON_CLOSE_OVER = "close_over";
    private static final String BUTTON_CLOSE_DOWN = "close_down";
    private static final String BUTTON_HELP_UP = "help_up";
    private static final String BUTTON_HELP_OVER = "help_over";
    private static final String BUTTON_HELP_DOWN = "help_down";

    private static final float INVENTORY_WINDOW_POSITION_X = (Gdx.graphics.getWidth() / 2f) - 50f;
    private static final float INVENTORY_WINDOW_POSITION_Y = (Gdx.graphics.getHeight() / 2f) - 135f;
    private static final float LOOT_WINDOW_POSITION_X = (Gdx.graphics.getWidth() / 2f) - 330f;
    private static final float LOOT_WINDOW_POSITION_Y = (Gdx.graphics.getHeight() / 2f) - 39f;

    private static final float BUTTON_SIZE = 32f;
    private static final float BUTTON_SPACE = 5f;
    private static final float RIGHT_SPACE = 6f;
    private static final float TOP_SPACE = 75f;

    private final Stage stage;
    private final ButtonToolTip buttonToolTip;
    @Getter
    private LootUI lootUI;
    @Setter
    private Loot loot;
    @Setter
    private String lootTitle;
    private boolean isMessageShown;

    public LootScreen() {
        this.stage = new Stage();
        this.buttonToolTip = new ButtonToolTip();
    }

    @Override
    public void onNotifyCreateProfile(ProfileManager profileManager) {
        isMessageShown = false;
        onNotifySaveProfile(profileManager);
    }

    @Override
    public void onNotifySaveProfile(ProfileManager profileManager) {
        profileManager.setProperty("isFirstTimeLootMessageShown", isMessageShown);
    }

    @Override
    public void onNotifyLoadProfile(ProfileManager profileManager) {
        isMessageShown = profileManager.getProperty("isFirstTimeLootMessageShown", Boolean.class);
    }

    @Override
    public void show() {
        Gdx.input.setCursorCatched(false);
        Gdx.input.setInputProcessor(stage);
        stage.addListener(new LootScreenListener(this::closeScreen, this::takeItem, this::showHelpMessage));
        createButtonTable();

        lootUI = new LootUI(loot, lootTitle);
        lootUI.inventoryWindow.setPosition(INVENTORY_WINDOW_POSITION_X, INVENTORY_WINDOW_POSITION_Y);
        lootUI.lootWindow.setPosition(LOOT_WINDOW_POSITION_X, LOOT_WINDOW_POSITION_Y);
        lootUI.addToStage(stage);
        lootUI.applyListeners(stage);
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
        var helpButton = createImageButton(BUTTON_HELP_UP, BUTTON_HELP_OVER, BUTTON_HELP_DOWN);
        closeButton.addListener(new ListenerMouseImageButton(this::closeScreen));
        closeButton.addListener(new ButtonTooltipListener(buttonToolTip, "Close screen"));
        helpButton.addListener(new ListenerMouseImageButton(this::showHelpMessage));
        helpButton.addListener(new ButtonTooltipListener(buttonToolTip, "Help dialog"));

        var buttonTable = new Table();
        buttonTable.add(closeButton).size(BUTTON_SIZE).spaceBottom(BUTTON_SPACE).row();
        buttonTable.add(helpButton).size(BUTTON_SIZE);
        buttonTable.pack();
        buttonTable.setPosition((Gdx.graphics.getWidth() * 0.7f) - buttonTable.getWidth() - RIGHT_SPACE,
                                (Gdx.graphics.getHeight() * 0.7f) - buttonTable.getHeight() - TOP_SPACE);
        stage.addActor(buttonTable);
    }

    private void takeItem() {
        if (isDialogVisibleThenClose()) {
            return;
        }
        if (!lootUI.takeItem()) {
            closeScreen();
        }
    }

    private void closeScreen() {
        if (isDialogVisibleThenClose()) {
            return;
        }
        if (lootUI.isEmpty()) {
            loot.clearContent();
            notifyLootTaken();
        } else {
            var newContent = lootUI.getContent();
            loot.updateContent(newContent);
        }

        Utils.getScreenManager().setScreen(ScreenType.WORLD);
        Gdx.input.setCursorCatched(true);
    }

    private void showHelpMessage() {
        if (isDialogVisibleThenClose()) {
            return;
        }
        final String message = "Esc = Close window" + System.lineSeparator() +
                "A = Take full stack" + System.lineSeparator() +
                "Enter = Take full stack" + System.lineSeparator() +
                "Shift = Drag full stack" + System.lineSeparator() +
                "Ctrl = Drag half stack" + System.lineSeparator() +
                "H = This dialog";
        final var messageDialog = new MessageDialog(message);
        messageDialog.setLeftAlignment();
        messageDialog.show(stage);
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
