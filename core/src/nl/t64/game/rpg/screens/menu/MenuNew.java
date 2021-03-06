package nl.t64.game.rpg.screens.menu;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.ScreenUtils;
import nl.t64.game.rpg.Utils;
import nl.t64.game.rpg.components.cutscene.CutsceneId;
import nl.t64.game.rpg.constants.Constant;
import nl.t64.game.rpg.constants.ScreenType;


public class MenuNew extends MenuScreen {

    private static final String SPRITE_BORDER = "sprites/border.png";

    private static final String PROFILE_LABEL = "Enter profile name:";
    private static final int PROFILE_INPUT_LENGTH = 8 + 1;

    private static final String MENU_ITEM_START = "Start";
    private static final String MENU_ITEM_BACK = "Back";

    private static final float PROFILE_INPUT_WIDTH = 280f;
    private static final float PROFILE_INPUT_HEIGHT = 50f;
    private static final float BUTTON_SPACE_RIGHT = 50f;
    private static final float SPACE_BOTTOM = 10f;

    private static final int NUMBER_OF_ITEMS = 2;
    private static final int EXIT_INDEX = 1;

    private TextField profileText;

    private ListenerKeyInputField listenerKeyInputField;
    private ListenerKeyHorizontal listenerKeyHorizontal;

    private String finalProfileName;
    private StringBuilder profileName;
    private boolean isBgmFading = false;

    @Override
    void setupScreen() {
        setFontColor();
        table = createTable();

        applyListeners();
        stage.addActor(table);
        stage.setKeyboardFocus(table);
        stage.addAction(Actions.alpha(1f));

        finalProfileName = "";
        profileName = new StringBuilder("_");
        updateInput(profileName);

        super.selectedMenuIndex = 0;
        setCurrentTextButtonToSelected();
    }

    @Override
    public void render(float dt) {
        ScreenUtils.clear(Color.BLACK);
        stage.act(dt);
        listenerKeyInputField.updateInputField(profileName);
        listenerKeyHorizontal.updateSelectedIndex(selectedMenuIndex);
        if (isBgmFading) {
            Utils.getAudioManager().fadeBgmBgs();
        }
        stage.draw();
    }

    private void updateInput(StringBuilder newInput) {
        profileName = newInput;
        profileText.setText(profileName.toString());
    }

    private void selectMenuItem() {
        switch (selectedMenuIndex) {
            case 0 -> processStartButton();
            case 1 -> processBackButton();
            default -> throw new IllegalArgumentException("SelectedIndex not found.");
        }
    }

    private void processStartButton() {
        finalProfileName = profileName.substring(0, profileName.length() - 1);
        fadeBeforeCreateNewGame();
    }

    private void fadeBeforeCreateNewGame() {
        stage.addAction(Actions.sequence(Actions.run(() -> isBgmFading = true),
                                         Actions.fadeOut(Constant.FADE_DURATION),
                                         Actions.run(() -> isBgmFading = false),
                                         Actions.run(this::createNewGame)));
    }

    private void createNewGame() {
        Utils.getScreenManager().getWorldScreen();  // just load the constructor.
        Utils.getProfileManager().createNewProfile(finalProfileName);
        Utils.getGameData().getCutscenes().setPlayed(CutsceneId.SCENE_INTRO);
        Utils.getScreenManager().setScreen(ScreenType.SCENE_INTRO);
    }

    @Override
    void setAllTextButtonsToDefault() {
        Table lowerTable = (Table) table.getChild(1); // two tables inside the table, get the second.
        for (Actor actor : lowerTable.getChildren()) {
            ((TextButton) actor).getStyle().fontColor = fontColor;
        }
    }

    @Override
    void setCurrentTextButtonToSelected() {
        Table lowerTable = (Table) table.getChild(1); // two tables inside the table, get the second.
        ((TextButton) lowerTable.getChild(selectedMenuIndex)).getStyle().fontColor = Constant.DARK_RED;
    }

    private Table createTable() {
//        Skin uiskin = new Skin();
//        uiskin.add("default-font", menuFont, BitmapFont.class);
//        TextureAtlas atlas = new TextureAtlas(UISKIN_ATLAS);
//        uiskin.addRegions(atlas);
//        uiskin.load(Gdx.files.internal(UISKIN_JSON));

        // styles
        var menuStyle = new Label.LabelStyle(menuFont, fontColor);
        var buttonStyle = new TextButton.TextButtonStyle();
        buttonStyle.font = menuFont;
        buttonStyle.fontColor = fontColor;

        // actors
        var profileLabel = new Label(PROFILE_LABEL, menuStyle);
        profileText = createProfileText();
        var startButton = new TextButton(MENU_ITEM_START, new TextButton.TextButtonStyle(buttonStyle));
        var backButton = new TextButton(MENU_ITEM_BACK, new TextButton.TextButtonStyle(buttonStyle));

        // table
        var newTable = new Table();
        newTable.setFillParent(true);

        var upperTable = new Table();
        upperTable.add(profileLabel).spaceBottom(SPACE_BOTTOM).row();
        upperTable.add(profileText).size(PROFILE_INPUT_WIDTH, PROFILE_INPUT_HEIGHT);

        var lowerTable = new Table();
        lowerTable.add(startButton).spaceRight(BUTTON_SPACE_RIGHT);
        lowerTable.add(backButton);

        newTable.add(upperTable).spaceBottom(SPACE_BOTTOM).row();
        newTable.add(lowerTable);
        Actor logo = stage.getActors().peek();
        newTable.top().padTop((logo.getHeight() * logo.getScaleY()) + LOGO_PAD + PAD_TOP)
                .right().padRight(((logo.getWidth() * logo.getScaleX()) / 2f) - (newTable.getPrefWidth() / 2f) + (LOGO_PAD / 2f));
        return newTable;
    }

    private TextField createProfileText() {
        // style
        var textFieldStyle = new TextField.TextFieldStyle();
        textFieldStyle.font = menuFont;
        textFieldStyle.fontColor = Color.BLACK;
        var texture = Utils.getResourceManager().getTextureAsset(SPRITE_BORDER);
        var ninepatch = new NinePatch(texture, 1, 1, 1, 1);
        textFieldStyle.background = new NinePatchDrawable(ninepatch);

        // actor
        var textField = new TextField("", textFieldStyle);
        textField.setDisabled(true);
        textField.setMaxLength(PROFILE_INPUT_LENGTH);
        textField.setAlignment(Align.center);
        return textField;
    }

    private void applyListeners() {
        listenerKeyInputField = new ListenerKeyInputField(this::updateInput, PROFILE_INPUT_LENGTH);
        listenerKeyHorizontal = new ListenerKeyHorizontal(super::updateMenuIndex, NUMBER_OF_ITEMS);
        var listenerKeyConfirm = new ListenerKeyConfirm(this::selectMenuItem);
        var listenerKeyCancel = new ListenerKeyCancel(super::updateMenuIndex, this::selectMenuItem, EXIT_INDEX);
        table.addListener(listenerKeyInputField);
        table.addListener(listenerKeyHorizontal);
        table.addListener(listenerKeyConfirm);
        table.addListener(listenerKeyCancel);
    }

}
