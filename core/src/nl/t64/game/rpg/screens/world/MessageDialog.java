package nl.t64.game.rpg.screens.world;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.GdxRuntimeException;
import nl.t64.game.rpg.Utils;


class MessageDialog {

    private static final String SPRITE_PARCHMENT = "sprites/parchment.png";
    private static final String DIALOG_FONT = "fonts/fff_tusj.ttf";
    private static final int FONT_SIZE = 30;

    private static final long DIALOG_INIT_HEIGHT = 100L;
    private static final float DIALOG_PAD = 60f;

    private final InputMultiplexer multiplexer;
    private final Stage stage;
    private final BitmapFont font;
    private final Dialog dialog;
    private Label label;
    private Runnable actionAfterHide;

    MessageDialog(InputMultiplexer multiplexer) {
        this.multiplexer = multiplexer;
        this.stage = new Stage();
        this.font = Utils.getResourceManager().getTrueTypeAsset(DIALOG_FONT, FONT_SIZE);
        this.dialog = createDialog();
        applyListeners();
        this.actionAfterHide = null;
    }

    void dispose() {
        stage.clear();
        stage.dispose();
        try {
            font.dispose();
        } catch (GdxRuntimeException e) {
            // font is already exposed.
        }
    }

    void setMessage(String message) {
        long dialogHeight = ((message.lines().count()) * FONT_SIZE) + DIALOG_INIT_HEIGHT;
        fillDialog(message, dialogHeight);
    }

    void setScreenAfterHide(Runnable actionAfterHide) {
        this.actionAfterHide = actionAfterHide;
    }

    void show() {
        Gdx.input.setInputProcessor(stage);
        dialog.show(stage);
    }

    void update(float dt) {
        stage.act(dt);
        stage.draw();
    }

    private Dialog createDialog() {
        // styles
        var labelStyle = new Label.LabelStyle(font, Color.BLACK);
        var windowStyle = new Dialog.WindowStyle();
        windowStyle.titleFont = font;
        windowStyle.titleFontColor = Color.BLACK;

        // actors
        label = new Label("no message", labelStyle);
        label.setAlignment(Align.center);

        // dialog
        var newDialog = new Dialog("", windowStyle);
        newDialog.getTitleLabel().setAlignment(Align.center);
        newDialog.padLeft(DIALOG_PAD);
        newDialog.padRight(DIALOG_PAD);

        var sprite = new Sprite(Utils.getResourceManager().getTextureAsset(SPRITE_PARCHMENT));
        newDialog.background(new SpriteDrawable(sprite));

        newDialog.setKeepWithinStage(true);
        newDialog.setModal(true);
        newDialog.setMovable(false);
        newDialog.setResizable(false);

        return newDialog;
    }

    private void fillDialog(String message, long dialogHeight) {
        label.setText(message);
        dialog.getContentTable().clear();
        dialog.getContentTable().defaults().width(label.getPrefWidth());
        dialog.getBackground().setMinHeight(dialogHeight);
        dialog.text(label);
    }

    private void applyListeners() {
        dialog.addListener(new MessageDialogListener(this::hide));
    }

    private void hide() {
        Gdx.input.setInputProcessor(multiplexer);
        if (actionAfterHide == null) {
            dialog.hide();
        } else {
            actionAfterHide.run();
            actionAfterHide = null;
            dialog.hide(null);
        }
    }

}