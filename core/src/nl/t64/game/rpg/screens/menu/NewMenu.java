package nl.t64.game.rpg.screens.menu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.badlogic.gdx.utils.Align;
import nl.t64.game.rpg.GdxRpg2;
import nl.t64.game.rpg.Utility;
import nl.t64.game.rpg.constants.Constant;
import nl.t64.game.rpg.constants.ScreenType;
import nl.t64.game.rpg.profile.ProfileManager;


public class NewMenu implements Screen {

    private static final String SPRITE_PARCHMENT = "sprites/parchment.png";
    private static final String MENU_FONT = "fonts/fff_tusj.ttf";
    private static final int MENU_SIZE = 30;

    private static final String PROFILE_LABEL = "Enter Profile Name:";
    private static final int PROFILE_INPUT_LENGTH = 8 + 1;

    private static final String MENU_ITEM_START = "Start";
    private static final String MENU_ITEM_BACK = "Back";

    private static final int PROFILE_LABEL_SPACE_RIGHT = 20;
    private static final int PROFILE_INPUT_WIDTH = 280;
    private static final int PROFILE_INPUT_HEIGHT = 75;
    private static final int START_BUTTON_SPACE_RIGHT = 150;
    private static final int UPPER_TABLE_SPACE_BOTTOM = 50;

    private static final int NUMBER_OF_ITEMS = 2;
    private static final int EXIT_INDEX = 1;

    private Stage stage;
    private GdxRpg2 game;

    private BitmapFont menuFont;
    private Table table;
    private TextField profileText;
    private TextButton startButton;
    private TextButton backButton;
    private OverwriteDialog overwriteDialog;

    private InputFieldKeyListener inputFieldKeyListener;
    private HorizontalKeyListener horizontalKeyListener;

    private String finalProfileName;
    private StringBuilder profileName;
    private int selectedIndex;

    public NewMenu(GdxRpg2 game) {
        this.game = game;
        this.stage = new Stage();

        createFonts();
        this.table = createTable();
        this.overwriteDialog = new OverwriteDialog(this::createNewGame);

        applyListeners();
        this.stage.addActor(this.table);
        this.stage.setKeyboardFocus(this.table);

        this.finalProfileName = "";
        this.profileName = new StringBuilder("_");
        updateInput(this.profileName);

        this.selectedIndex = 0;
        setCurrentTextButtonToRed();
    }

    @Override
    public void show() {
        ProfileManager.getInstance().storeAllProfiles();
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void render(float dt) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act(dt);
        inputFieldKeyListener.updateInputField(profileName);
        horizontalKeyListener.updateSelectedIndex(selectedIndex);
        overwriteDialog.update(); // for updating the index in de listener.
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().setScreenSize(width, height);
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
        Gdx.input.setInputProcessor(null);
    }

    @Override
    public void dispose() {
        // menuFont.dispose(); is already disposed in MainMenu?
        stage.clear();
        stage.dispose();
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
            case 0:
                processStartButton();
                break;
            case 1:
                game.setScreen(game.getScreenType(ScreenType.MAIN_MENU));
                break;
            default:
                throw new IllegalArgumentException("SelectedIndex not found.");
        }
    }

    private void processStartButton() {
        finalProfileName = profileName.toString().substring(0, profileName.length() - 1);
        boolean exists = ProfileManager.getInstance().doesProfileExist(finalProfileName);
        if (exists) {
            overwriteDialog.show(stage);
        } else {
            createNewGame();
        }
    }

    private void createNewGame() {
        ProfileManager.getInstance().createNewProfile(finalProfileName);
        game.setScreen(game.getScreenType(ScreenType.WORLD));
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

    private void createFonts() {
        Utility.loadTrueTypeAsset(MENU_FONT, MENU_SIZE);
        menuFont = Utility.getTrueTypeAsset(MENU_FONT);
    }

    private Table createTable() {
//        Skin uiskin = new Skin();
//        uiskin.add("default-font", menuFont, BitmapFont.class);
//        TextureAtlas atlas = new TextureAtlas(UISKIN_ATLAS);
//        uiskin.addRegions(atlas);
//        uiskin.load(Gdx.files.internal(UISKIN_JSON));

        // styles
        Label.LabelStyle menuStyle = new Label.LabelStyle(menuFont, Color.WHITE);
        TextButton.TextButtonStyle buttonStyle = new TextButton.TextButtonStyle();
        buttonStyle.font = menuFont;
        buttonStyle.fontColor = Color.WHITE;

        // actors
        Label profileLabel = new Label(PROFILE_LABEL, menuStyle);
        profileText = createProfileText();
        startButton = new TextButton(MENU_ITEM_START, new TextButton.TextButtonStyle(buttonStyle));
        backButton = new TextButton(MENU_ITEM_BACK, new TextButton.TextButtonStyle(buttonStyle));

        // table
        Table newTable = new Table();
        newTable.setFillParent(true);

        Table upperTable = new Table();
        upperTable.add(profileLabel).spaceRight(PROFILE_LABEL_SPACE_RIGHT);
        upperTable.add(profileText).width(PROFILE_INPUT_WIDTH).height(PROFILE_INPUT_HEIGHT);

        Table lowerTable = new Table();
        lowerTable.add(startButton).spaceRight(START_BUTTON_SPACE_RIGHT);
        lowerTable.add(backButton);

        newTable.add(upperTable).spaceBottom(UPPER_TABLE_SPACE_BOTTOM).row();
        newTable.add(lowerTable);
        return newTable;
    }

    private TextField createProfileText() {
        // style
        TextField.TextFieldStyle textFieldStyle = new TextField.TextFieldStyle();
        textFieldStyle.font = menuFont;
        textFieldStyle.fontColor = Color.BLACK;
        Utility.loadTextureAsset(SPRITE_PARCHMENT);
        Texture texture = Utility.getTextureAsset(SPRITE_PARCHMENT);
        Sprite sprite = new Sprite(texture);
        textFieldStyle.background = new SpriteDrawable(sprite);

        // actor
        TextField textField = new TextField("", textFieldStyle);
        textField.setMaxLength(PROFILE_INPUT_LENGTH);
        textField.setAlignment(Align.center);
        return textField;
    }

    private void applyListeners() {
        inputFieldKeyListener = new InputFieldKeyListener(this::updateInput, PROFILE_INPUT_LENGTH);
        horizontalKeyListener = new HorizontalKeyListener(this::updateIndex, NUMBER_OF_ITEMS);
        table.addListener(inputFieldKeyListener);
        table.addListener(horizontalKeyListener);
        table.addListener(new ConfirmKeyListener(this::updateIndex, this::selectMenuItem, EXIT_INDEX));
        startButton.addListener(createButtonMouseListener(0));
        backButton.addListener(createButtonMouseListener(1));
    }

    private ButtonMouseListener createButtonMouseListener(int index) {
        return new ButtonMouseListener(this::updateIndex, this::selectMenuItem, index);
    }

}
