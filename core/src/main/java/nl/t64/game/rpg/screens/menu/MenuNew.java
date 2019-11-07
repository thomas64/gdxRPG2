package nl.t64.game.rpg.screens.menu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.badlogic.gdx.utils.Align;
import nl.t64.game.rpg.Utils;
import nl.t64.game.rpg.constants.Constant;
import nl.t64.game.rpg.constants.ScreenType;


public class MenuNew extends MenuScreen {

    private static final String SPRITE_PARCHMENT = "sprites/parchment.png";

    private static final String PROFILE_LABEL = "Enter Profile Name:";
    private static final int PROFILE_INPUT_LENGTH = 8 + 1;
    private static final String DIALOG_MESSAGE = "Overwrite existing profile name?";

    private static final String MENU_ITEM_START = "Start";
    private static final String MENU_ITEM_BACK = "Back";

    private static final float PROFILE_LABEL_SPACE_RIGHT = 20f;
    private static final float PROFILE_INPUT_WIDTH = 280f;
    private static final float PROFILE_INPUT_HEIGHT = 75f;
    private static final float START_BUTTON_SPACE_RIGHT = 150f;
    private static final float UPPER_TABLE_SPACE_BOTTOM = 50f;

    private static final int NUMBER_OF_ITEMS = 2;
    private static final int EXIT_INDEX = 1;

    private Table table;
    private TextField profileText;
    private TextButton startButton;
    private TextButton backButton;
    private DialogQuestion overwriteDialog;

    private ListenerKeyInputField listenerKeyInputField;
    private ListenerKeyHorizontal listenerKeyHorizontal;

    private String finalProfileName;
    private StringBuilder profileName;
    private int selectedIndex;

    public MenuNew() {
        super();
    }

    @Override
    void setupScreen() {
        Utils.getProfileManager().loadAllProfiles();

        table = createTable();
        overwriteDialog = new DialogQuestion(this::createNewGame, DIALOG_MESSAGE);

        applyListeners();
        stage.addActor(table);
        stage.setKeyboardFocus(table);

        finalProfileName = "";
        profileName = new StringBuilder("_");
        updateInput(profileName);

        selectedIndex = 0;
        setCurrentTextButtonToRed();
    }

    @Override
    public void render(float dt) {
        Gdx.gl.glClearColor(0f, 0f, 0f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act(dt);
        listenerKeyInputField.updateInputField(profileName);
        listenerKeyHorizontal.updateSelectedIndex(selectedIndex);
        overwriteDialog.update(); // for updating the index in de listener.
        stage.draw();
    }

    private void updateInput(StringBuilder newInput) {
        profileName = newInput;
        profileText.setText(profileName.toString());
    }

    private void updateIndex(Integer newIndex) {
        selectedIndex = newIndex;
        setAllTextButtonsToWhite();
        setCurrentTextButtonToRed();
    }

    private void selectMenuItem() {
        switch (selectedIndex) {
            case 0 -> processStartButton();
            case 1 -> processBackButton();
            default -> throw new IllegalArgumentException("SelectedIndex not found.");
        }
    }

    private void processStartButton() {
        finalProfileName = profileName.toString().substring(0, profileName.length() - 1);
        boolean profileExists = Utils.getProfileManager().doesProfileExist(finalProfileName);
        if (profileExists) {
            overwriteDialog.show(stage);
        } else {
            createNewGame();
        }
    }

    private void processBackButton() {
        Utils.getScreenManager().setScreen(ScreenType.MENU_MAIN);
    }

    private void createNewGame() {
        Utils.getScreenManager().setScreen(ScreenType.WORLD);
        Utils.getProfileManager().createNewProfile(finalProfileName);
    }

    private void setAllTextButtonsToWhite() {
        Table lowerTable = (Table) table.getChildren().get(1); // two tables inside the table, get the second.
        for (Actor actor : lowerTable.getChildren()) {
            ((TextButton) actor).getStyle().fontColor = Color.WHITE;
        }
    }

    private void setCurrentTextButtonToRed() {
        Table lowerTable = (Table) table.getChildren().get(1); // two tables inside the table, get the second.
        ((TextButton) lowerTable.getChildren().get(selectedIndex)).getStyle().fontColor = Constant.DARK_RED;
    }

    private Table createTable() {
//        Skin uiskin = new Skin();
//        uiskin.add("default-font", menuFont, BitmapFont.class);
//        TextureAtlas atlas = new TextureAtlas(UISKIN_ATLAS);
//        uiskin.addRegions(atlas);
//        uiskin.load(Gdx.files.local(UISKIN_JSON));

        // styles
        var menuStyle = new Label.LabelStyle(menuFont, Color.WHITE);
        var buttonStyle = new TextButton.TextButtonStyle();
        buttonStyle.font = menuFont;
        buttonStyle.fontColor = Color.WHITE;

        // actors
        var profileLabel = new Label(PROFILE_LABEL, menuStyle);
        profileText = createProfileText();
        startButton = new TextButton(MENU_ITEM_START, new TextButton.TextButtonStyle(buttonStyle));
        backButton = new TextButton(MENU_ITEM_BACK, new TextButton.TextButtonStyle(buttonStyle));

        // table
        var newTable = new Table();
        newTable.setFillParent(true);

        var upperTable = new Table();
        upperTable.add(profileLabel).spaceRight(PROFILE_LABEL_SPACE_RIGHT);
        upperTable.add(profileText).width(PROFILE_INPUT_WIDTH).height(PROFILE_INPUT_HEIGHT);

        var lowerTable = new Table();
        lowerTable.add(startButton).spaceRight(START_BUTTON_SPACE_RIGHT);
        lowerTable.add(backButton);

        newTable.add(upperTable).spaceBottom(UPPER_TABLE_SPACE_BOTTOM).row();
        newTable.add(lowerTable);
        return newTable;
    }

    private TextField createProfileText() {
        // style
        var textFieldStyle = new TextField.TextFieldStyle();
        textFieldStyle.font = menuFont;
        textFieldStyle.fontColor = Color.BLACK;
        var sprite = new Sprite(Utils.getResourceManager().getTextureAsset(SPRITE_PARCHMENT));
        textFieldStyle.background = new SpriteDrawable(sprite);

        // actor
        var textField = new TextField("", textFieldStyle);
        textField.setDisabled(true);
        textField.setMaxLength(PROFILE_INPUT_LENGTH);
        textField.setAlignment(Align.center);
        return textField;
    }

    private void applyListeners() {
        listenerKeyInputField = new ListenerKeyInputField(this::updateInput, PROFILE_INPUT_LENGTH);
        listenerKeyHorizontal = new ListenerKeyHorizontal(this::updateIndex, NUMBER_OF_ITEMS);
        table.addListener(listenerKeyInputField);
        table.addListener(listenerKeyHorizontal);
        table.addListener(new ListenerKeyConfirm(this::updateIndex, this::selectMenuItem, EXIT_INDEX));
        startButton.addListener(new ListenerMouseTextButton(this::updateIndex, this::selectMenuItem, 0));
        backButton.addListener(new ListenerMouseTextButton(this::updateIndex, this::selectMenuItem, 1));
    }

}
