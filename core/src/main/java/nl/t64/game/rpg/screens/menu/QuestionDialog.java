package nl.t64.game.rpg.screens.menu;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.badlogic.gdx.utils.Align;
import nl.t64.game.rpg.Utility;
import nl.t64.game.rpg.constants.Constant;
import nl.t64.game.rpg.listeners.ButtonMouseListener;
import nl.t64.game.rpg.listeners.ConfirmKeyListener;
import nl.t64.game.rpg.listeners.HorizontalKeyListener;


public class QuestionDialog {

    private static final String SPRITE_PARCHMENT = "sprites/parchment.png";
    private static final String MENU_FONT = "fonts/fff_tusj.ttf";
    private static final int MENU_SIZE = 30;

    private static final String DIALOG_YES = "Yes";
    private static final String DIALOG_NO = "No";

    private static final int DIALOG_WIDTH = 620;
    private static final int DIALOG_INIT_HEIGHT = 150;
    private static final int LINE_HEIGHT = 30;
    private static final int DIALOG_PAD_TOP = 20;
    private static final int DIALOG_PAD_BOTTOM = 40;
    private static final int BUTTON_SPACE_RIGHT = 100;

    private static final int NUMBER_OF_ITEMS = 2;
    private static final int EXIT_INDEX = 1;

    private final Runnable yesFunction;
    private final String message;
    private final int dialogHeight;

    private BitmapFont menuFont;
    private Dialog dialog;
    private TextButton yesButton;
    private TextButton noButton;

    private HorizontalKeyListener horizontalKeyListener;

    private int selectedIndex;

    QuestionDialog(Runnable yesFunction, String message) {
        this.message = message;
        this.dialogHeight = ((message.split("\r\n|\r|\n").length) * LINE_HEIGHT) + DIALOG_INIT_HEIGHT;
        this.yesFunction = yesFunction;
        createFonts();
        this.dialog = createDialog();
        applyListeners();
    }

    void show(Stage stage) {
        dialog.show(stage);
        updateIndex(EXIT_INDEX);
    }

    public void update() {  // handmade equivalent of a Screen.render() method.
        horizontalKeyListener.updateSelectedIndex(selectedIndex);
    }

    private void updateIndex(Integer newIndex) {
        selectedIndex = newIndex;
        setAllTextButtonsToBlack();
        setCurrentTextButtonToRed();
    }

    private void selectMenuItem() {
        switch (selectedIndex) {
            case 0:
                dialog.hide();
                yesFunction.run();
                break;
            case 1:
                dialog.hide();
                break;
            default:
                throw new IllegalArgumentException("SelectedIndex not found.");
        }
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

    private void createFonts() {
        Utility.loadTrueTypeAsset(MENU_FONT, MENU_SIZE);
        menuFont = Utility.getTrueTypeAsset(MENU_FONT);
    }

    private Dialog createDialog() {
        // styles
        Label.LabelStyle menuStyle = new Label.LabelStyle(menuFont, Color.BLACK);
        TextButton.TextButtonStyle buttonStyle = new TextButton.TextButtonStyle();
        buttonStyle.font = menuFont;
        buttonStyle.fontColor = Color.BLACK;
        Dialog.WindowStyle windowStyle = new Dialog.WindowStyle();
        windowStyle.titleFont = menuFont;
        windowStyle.titleFontColor = Color.BLACK;

        // actors
        Label overwriteLabel = new Label(message, menuStyle);
        overwriteLabel.setAlignment(Align.center);
        yesButton = new TextButton(DIALOG_YES, new TextButton.TextButtonStyle(buttonStyle));
        noButton = new TextButton(DIALOG_NO, new TextButton.TextButtonStyle(buttonStyle));

        // table
        Dialog newDialog = new Dialog("", windowStyle);
        newDialog.getTitleLabel().setAlignment(Align.center);
        newDialog.padTop(DIALOG_PAD_TOP);
        newDialog.padBottom(DIALOG_PAD_BOTTOM);
        newDialog.getContentTable().defaults().width(DIALOG_WIDTH);

        Utility.loadTextureAsset(SPRITE_PARCHMENT);
        Texture texture = Utility.getTextureAsset(SPRITE_PARCHMENT);
        Sprite sprite = new Sprite(texture);
        newDialog.background(new SpriteDrawable(sprite));
        newDialog.getBackground().setMinHeight(dialogHeight);

        newDialog.setKeepWithinStage(true);
        newDialog.setModal(true);
        newDialog.setMovable(false);
        newDialog.setResizable(false);
        newDialog.text(overwriteLabel);

        newDialog.getButtonTable().add(yesButton).spaceRight(BUTTON_SPACE_RIGHT);
        newDialog.getButtonTable().add(noButton);
        return newDialog;
    }

    private void applyListeners() {
        horizontalKeyListener = new HorizontalKeyListener(this::updateIndex, NUMBER_OF_ITEMS);
        dialog.addListener(horizontalKeyListener);
        dialog.addListener(new ConfirmKeyListener(this::updateIndex, this::selectMenuItem, EXIT_INDEX));
        yesButton.addListener(createButtonMouseListener(0));
        noButton.addListener(createButtonMouseListener(1));
    }

    private ButtonMouseListener createButtonMouseListener(int index) {
        return new ButtonMouseListener(this::updateIndex, this::selectMenuItem, index);
    }

}
