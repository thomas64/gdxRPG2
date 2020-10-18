package nl.t64.game.rpg.screens.menu;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.badlogic.gdx.utils.Align;
import nl.t64.game.rpg.Utils;
import nl.t64.game.rpg.audio.AudioCommand;
import nl.t64.game.rpg.audio.AudioEvent;
import nl.t64.game.rpg.constants.Constant;


class DialogQuestion {

    private static final String SPRITE_PARCHMENT = "sprites/parchment.png";
    private static final String DIALOG_FONT = "fonts/fff_tusj.ttf";
    private static final int FONT_SIZE = 30;

    private static final String DIALOG_YES = "Yes";
    private static final String DIALOG_NO = "No";

    private static final long DIALOG_INIT_HEIGHT = 150L;
    private static final float DIALOG_PAD_TOP = 20f;
    private static final float DIALOG_PAD_BOTTOM = 40f;
    private static final float BUTTON_SPACE_RIGHT = 100f;

    private static final int NUMBER_OF_ITEMS = 2;
    private static final int EXIT_INDEX = 1;

    private final Runnable yesFunction;
    private final String message;
    private final long dialogHeight;

    private final BitmapFont dialogFont;
    private final Dialog dialog;
    private TextButton yesButton;
    private TextButton noButton;

    private ListenerKeyHorizontal listenerKeyHorizontal;

    private int selectedIndex;

    DialogQuestion(Runnable yesFunction, String message) {
        this.message = message;
        this.dialogHeight = ((message.lines().count()) * FONT_SIZE) + DIALOG_INIT_HEIGHT;
        this.yesFunction = yesFunction;
        this.dialogFont = Utils.getResourceManager().getTrueTypeAsset(DIALOG_FONT, FONT_SIZE);
        this.dialog = this.createDialog();
        this.applyListeners();
    }

    void show(Stage stage) {
        dialog.show(stage);
        updateIndex(EXIT_INDEX);
    }

    void update() {
        listenerKeyHorizontal.updateSelectedIndex(selectedIndex);
    }

    private void updateIndex(Integer newIndex) {
        selectedIndex = newIndex;
        setAllTextButtonsToBlack();
        setCurrentTextButtonToRed();
    }

    private void selectDialogItem() {
        switch (selectedIndex) {
            case 0 -> processYesButton();
            case 1 -> processNoButton();
            default -> throw new IllegalArgumentException("SelectedIndex not found.");
        }
    }

    private void processYesButton() {
        dialog.hide();
        yesFunction.run();
    }

    private void processNoButton() {
        Utils.getAudioManager().handle(AudioCommand.SE_STOP, AudioEvent.SE_MENU_CONFIRM);
        Utils.getAudioManager().handle(AudioCommand.SE_PLAY_ONCE, AudioEvent.SE_MENU_BACK);
        dialog.hide();
    }

    private void setAllTextButtonsToBlack() {
        for (Actor actor : dialog.getButtonTable().getChildren()) {
            ((TextButton) actor).getStyle().fontColor = Color.BLACK;
        }
    }

    private void setCurrentTextButtonToRed() {
        ((TextButton) dialog.getButtonTable().getChildren().get(selectedIndex))
                .getStyle().fontColor = Constant.DARK_RED;
    }

    private Dialog createDialog() {
        // styles
        var labelStyle = new Label.LabelStyle(dialogFont, Color.BLACK);
        var buttonStyle = new TextButton.TextButtonStyle();
        buttonStyle.font = dialogFont;
        buttonStyle.fontColor = Color.BLACK;
        var windowStyle = new Dialog.WindowStyle();
        windowStyle.titleFont = dialogFont;
        windowStyle.titleFontColor = Color.BLACK;

        // actors
        var label = new Label(message, labelStyle);
        label.setAlignment(Align.center);
        yesButton = new TextButton(DIALOG_YES, new TextButton.TextButtonStyle(buttonStyle));
        noButton = new TextButton(DIALOG_NO, new TextButton.TextButtonStyle(buttonStyle));

        // table
        var newDialog = new Dialog("", windowStyle);
        newDialog.getTitleLabel().setAlignment(Align.center);
        newDialog.padTop(DIALOG_PAD_TOP);
        newDialog.padBottom(DIALOG_PAD_BOTTOM);
        newDialog.getContentTable().defaults().width(label.getPrefWidth() + BUTTON_SPACE_RIGHT);

        var sprite = new Sprite(Utils.getResourceManager().getTextureAsset(SPRITE_PARCHMENT));
        newDialog.background(new SpriteDrawable(sprite));
        newDialog.getBackground().setMinHeight(dialogHeight);

        newDialog.setKeepWithinStage(true);
        newDialog.setModal(true);
        newDialog.setMovable(false);
        newDialog.setResizable(false);
        newDialog.text(label);

        newDialog.getButtonTable().add(yesButton).spaceRight(BUTTON_SPACE_RIGHT);
        newDialog.getButtonTable().add(noButton);
        return newDialog;
    }

    private void applyListeners() {
        listenerKeyHorizontal = new ListenerKeyHorizontal(this::updateIndex, NUMBER_OF_ITEMS);
        dialog.addListener(listenerKeyHorizontal);
        dialog.addListener(new ListenerKeyConfirm(this::updateIndex, this::selectDialogItem, EXIT_INDEX));
        yesButton.addListener(new ListenerMouseTextButton(this::updateIndex, this::selectDialogItem, 0));
        noButton.addListener(new ListenerMouseTextButton(this::updateIndex, this::selectDialogItem, 1));
    }

}
