package nl.t64.game.rpg.screens.inventory;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.badlogic.gdx.utils.Align;
import nl.t64.game.rpg.Utils;
import nl.t64.game.rpg.audio.AudioCommand;
import nl.t64.game.rpg.audio.AudioEvent;


public class MessageDialog {

    private static final String SPRITE_PARCHMENT = "sprites/parchment.png";
    private static final String DIALOG_FONT = "fonts/fff_tusj.ttf";
    private static final int FONT_SIZE = 30;

    private static final long DIALOG_INIT_HEIGHT = 100L;
    private static final float DIALOG_PAD = 60f;

    private final String message;
    private final long dialogHeight;

    private final BitmapFont dialogFont;
    private final Dialog dialog;

    public MessageDialog(String message) {
        this.message = message;
        this.dialogHeight = ((message.lines().count()) * FONT_SIZE) + DIALOG_INIT_HEIGHT;
        this.dialogFont = Utils.getResourceManager().getTrueTypeAsset(DIALOG_FONT, FONT_SIZE);
        this.dialog = this.createDialog();
        this.applyListeners();
    }

    public void show(Stage stage, AudioEvent event) {
        Utils.getAudioManager().handle(AudioCommand.SE_PLAY_ONCE, event);
        dialog.show(stage);
    }

    public void setLeftAlignment() {
        ((Label) dialog.getContentTable().getChildren().get(0)).setAlignment(Align.left);
    }

    private Dialog createDialog() {
        // styles
        var labelStyle = new Label.LabelStyle(dialogFont, Color.BLACK);
        var windowStyle = new Dialog.WindowStyle();
        windowStyle.titleFont = dialogFont;
        windowStyle.titleFontColor = Color.BLACK;

        // actors
        var label = new Label(message, labelStyle);
        label.setAlignment(Align.center);

        // dialog
        var newDialog = new Dialog("", windowStyle);
        newDialog.getTitleLabel().setAlignment(Align.center);
        newDialog.padLeft(DIALOG_PAD);
        newDialog.padRight(DIALOG_PAD);
        newDialog.getContentTable().defaults().width(label.getPrefWidth());

        var sprite = new Sprite(Utils.getResourceManager().getTextureAsset(SPRITE_PARCHMENT));
        newDialog.background(new SpriteDrawable(sprite));
        newDialog.getBackground().setMinHeight(dialogHeight);

        newDialog.setKeepWithinStage(true);
        newDialog.setModal(true);
        newDialog.setMovable(false);
        newDialog.setResizable(false);
        newDialog.text(label);

        return newDialog;
    }

    private void applyListeners() {
        dialog.addListener(new MessageDialogListener(this::hide));
    }

    private void hide() {
        Utils.getAudioManager().handle(AudioCommand.SE_PLAY_ONCE, AudioEvent.SE_CONVERSATION_NEXT);
        dialog.hide();
    }

}
