package nl.t64.game.rpg.entities;

import com.badlogic.gdx.Gdx;
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
import nl.t64.game.rpg.GdxRpg2;
import nl.t64.game.rpg.Utility;
import nl.t64.game.rpg.constants.Constant;
import nl.t64.game.rpg.constants.ScreenType;
import nl.t64.game.rpg.profile.ProfileManager;


public class MessageBox {

    private static final String SPRITE_PARCHMENT = "sprites/parchment.png";
    private static final String MENU_FONT = "fonts/fff_tusj.ttf";
    private static final int MENU_SIZE = 30;

    private static final String DIALOG_TEXT = "Overwrite existing profile name?";
    private static final String DIALOG_OVERWRITE = "Overwrite";
    private static final String DIALOG_CANCEL = "Cancel";

    private static final int NUMBER_OF_ITEMS = 2;
    private static final int EXIT_INDEX = 1;

    private GdxRpg2 game;
    private String profileName;

    private Stage stage;
    private BitmapFont menuFont;
    private Dialog dialog;
    private int selectedIndex = 1;

    public MessageBox(GdxRpg2 game, String profileName) {
        this.game = game;
        this.profileName = profileName;

        menuFont = Utility.generateBitmapFontFromFreeTypeFont(MENU_FONT, MENU_SIZE);

        Label.LabelStyle menuStyle = new Label.LabelStyle(menuFont, Color.BLACK);
        TextButton.TextButtonStyle buttonStyle = new TextButton.TextButtonStyle();
        buttonStyle.font = menuFont;
        buttonStyle.fontColor = Color.BLACK;
        buttonStyle.downFontColor = Constant.DARK_RED;
        buttonStyle.overFontColor = Constant.DARK_RED;

        Label overwriteLabel = new Label(DIALOG_TEXT, menuStyle);
        overwriteLabel.setAlignment(Align.center);
        TextButton cancelButton = new TextButton(DIALOG_CANCEL, new TextButton.TextButtonStyle(buttonStyle));
        TextButton overwriteButton = new TextButton(DIALOG_OVERWRITE, new TextButton.TextButtonStyle(buttonStyle));

        Dialog.WindowStyle windowStyle = new Dialog.WindowStyle();
        windowStyle.titleFont = menuFont;
        windowStyle.titleFontColor = Color.BLACK;

        dialog = new Dialog("", windowStyle);
        dialog.getTitleLabel().setAlignment(Align.center);
        dialog.padTop(20);
        dialog.padBottom(40);
        dialog.getContentTable().defaults().width(620);

        Utility.loadTextureAsset(SPRITE_PARCHMENT);
        Texture texture = Utility.getTextureAsset(SPRITE_PARCHMENT);
        Sprite sprite = new Sprite(texture);
        dialog.background(new SpriteDrawable(sprite));
        dialog.getBackground().setMinHeight(170);

        dialog.setKeepWithinStage(true);
        dialog.setModal(true);
        dialog.setMovable(false);
        dialog.setResizable(false);
        dialog.text(overwriteLabel);

        dialog.getButtonTable().add(overwriteButton).spaceRight(50);
        dialog.getButtonTable().add(cancelButton);

        setAllTextButtonsToBlack();
        setCurrentTextButtonToRed();

        dialog.addListener(new DialogKeyListener(this::updateIndex, this::selectItem, NUMBER_OF_ITEMS, EXIT_INDEX));
        overwriteButton.addListener(new ButtonMouseListener(this::updateIndex, this::selectItem, 0));
        cancelButton.addListener(new ButtonMouseListener(this::updateIndex, this::selectItem, 1));
    }

    // todo, complete refactoring.
    // todo, meerdere screens maken, load enzo.
    // todo, de rest van hoofdstuk 4 doornemen.
    // todo, de menu componenten undryen.
    // todo, multiplexer voor het hide probleem

    public void show(Stage stage) {
        this.stage = stage;
        Gdx.input.setInputProcessor(stage);
        dialog.show(stage);
    }

    private void hide() {
        dialog.hide();
        Gdx.input.setInputProcessor(null);
    }

    private void updateIndex(Integer newIndex) {
        selectedIndex = newIndex;
        setAllTextButtonsToBlack();
        setCurrentTextButtonToRed();
    }

    private void selectItem() {
        switch (selectedIndex) {
            case 0:
                ProfileManager.getInstance().createNewProfile(profileName);
                hide();
                game.setScreen(game.getScreenType(ScreenType.WORLD));
                break;
            case 1:
                hide();
                game.setScreen(game.getScreenType(ScreenType.NEW_GAME_MENU));
                break;
            default:
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

}
