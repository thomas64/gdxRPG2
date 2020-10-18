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
import nl.t64.game.rpg.audio.AudioCommand;
import nl.t64.game.rpg.audio.AudioEvent;


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
    private AudioEvent audioEvent;
    private Runnable actionAfterHide;
    private boolean isVisible;
    private int timer;

    MessageDialog(InputMultiplexer multiplexer) {
        this.multiplexer = multiplexer;
        this.stage = new Stage();
        this.font = Utils.getResourceManager().getTrueTypeAsset(DIALOG_FONT, FONT_SIZE);
        this.dialog = this.createDialog();
        this.applyListeners();
        this.actionAfterHide = null;
        this.isVisible = false;
        this.timer = 0;
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

    void setScreenAfterHide(Runnable actionAfterHide) {
        this.actionAfterHide = actionAfterHide;
    }

    void show(String message, AudioEvent event) {
        fillDialog(message);
        dialog.show(stage);
        audioEvent = event;
        isVisible = true;
        timer = 0;
    }

    void update(float dt) {
        if (isVisible) {
            Gdx.input.setInputProcessor(stage);
            playAudio();
        }
        stage.act(dt);
        stage.draw();
    }

    private void playAudio() {
        timer++;            // when dialog really REALLY is in the front, play audio sample. because it is shown for a
        if (timer == 2) {   // real short time, but it must be put on hold until for example RewardScreen is gone.
            Utils.getAudioManager().handle(AudioCommand.SE_PLAY_ONCE, audioEvent);
        }
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

    private void fillDialog(String message) {
        long dialogHeight = ((message.lines().count()) * FONT_SIZE) + DIALOG_INIT_HEIGHT;
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
        isVisible = false;
        timer = 0;
        Gdx.input.setInputProcessor(multiplexer);
        if (actionAfterHide == null) {
            Utils.getAudioManager().handle(AudioCommand.SE_PLAY_ONCE, AudioEvent.SE_CONVERSATION_NEXT);
            dialog.hide();
        } else {
            actionAfterHide.run();
            actionAfterHide = null;
            dialog.hide(null);
        }
    }

}
